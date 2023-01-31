package org.algiri.data;
import lombok.SneakyThrows;
import org.algiri.model.Lesson;
import org.algiri.model.UserData;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataBase {
    private static DataBase INSTANCE;
    private Connection connection;
    private Statement statement;
    @SneakyThrows
    public DataBase() {
        String url = "jdbc:sqlite:parabot.db";
        connection = DriverManager.getConnection(url);
        statement = connection.createStatement();
    }
    public static DataBase getINSTANCE() {
        if(INSTANCE == null)
            INSTANCE = new DataBase();
        return INSTANCE;
    }
    @SneakyThrows
    public UserData getUserData(long userId) {
        UserData user = new UserData();
        ResultSet rs = statement.executeQuery("select g.* from users g where g.user_id = " + userId);
        while(rs.next()) {
            user.setId(rs.getLong("user_id"));
            user.setGroupId(rs.getInt("group"));
            user.setName(rs.getString("name"));
        }
        return user;

    }
    @SneakyThrows
    public UserData insertUserData(long userId, String group, String name) {
        statement.executeUpdate(String.format("insert into users " +
                "(user_id, 'group', name, message_count)" +
                "values(%d, '%s', '%s', 1)",
                userId, getGroupId(group), name));
        return new UserData(userId, group, name);
    }
    @SneakyThrows
    public void updateUsersData(long userId, String group) {
        statement.executeUpdate(String.format("update users set 'group' = %d where user_id = %d",
                getGroupId(group), userId));
    }
    private int getGroupId(String group) throws SQLException {
        return statement.executeQuery("select g.* from groups g where g.'group' = '" + group + "'").getInt("id");
    }

    @SneakyThrows
    public List<Lesson> getTimeTable(int groupId) {
        List<Lesson> result = new ArrayList<>();

        String sql = "select t.* from timetable t where t.'group' = " + groupId;
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()) {
            Lesson lesson = new Lesson(rs.getInt("day"),
                    rs.getString("timestart"),
                    rs.getString("timeend"),
                    rs.getString("lesson"),
                    "group",
                    rs.getBoolean("isNumerator"));
            result.add(lesson);
        }
        return result;
    }
    @SneakyThrows
    public void addTimeTableData(int day, String timeStart, String timeEnd, String name, String group, boolean isNumerator) {
        statement.execute(String.format("INSERT INTO timetable " +
                "(day, timestart, timeend, lesson, 'group', isNumerator)" +
                "values(%d, '%s', '%s', '%s', %d, %b)",
                day, timeStart, timeEnd, name, getGroupId(group), isNumerator));
    }

    @SneakyThrows
    public List<String> getGroups() {
        List<String> result = new ArrayList<>();
        ResultSet rs = statement.executeQuery("select * from groups");
        while (rs.next())
            result.add(rs.getString("group"));
        return result;
    }
}
