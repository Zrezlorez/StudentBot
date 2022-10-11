package org.algiri;

import lombok.SneakyThrows;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataBase {
    public Statement user;

    @SneakyThrows
    public DataBase(){
        Class.forName("org.postgresql.Driver");
        Connection db = DriverManager.getConnection("jdbc:postgresql:postgres", "postgres", "12111980");
        user = db.createStatement();
    }

    @SneakyThrows
    public List<String> getUserData(String data){
    List<String> result = new ArrayList<>();
        ResultSet rs = user.executeQuery("SELECT * FROM users " + data);
        while (rs.next()){
            for(int i = 1; i<5; i++)
                result.add(rs.getString(i));
        }
        return result;
    }

    @SneakyThrows
    public List<String> getTimeTableData(String data){
        List<String> result = new ArrayList<>();
        ResultSet rs = user.executeQuery("SELECT * FROM timetable " + data);
        while (rs.next()){
            for(int i = 1; i<10; i++)
                result.add(rs.getString(i));
        }


        return result;
    }

    @SneakyThrows
    public void insertUsersData(long id, String name, boolean isVk) {
        user.execute(
                String.format("INSERT INTO public.users(id, \"group\", name, \"isVk\") VALUES (%d, '?', '%s', %b);", id, name, isVk));
    }

    @SneakyThrows
    public void updateUsersData(long id, String data){
        user.execute(
                String.format("UPDATE users SET \"group\" = '%s' WHERE id = %d;", data, id));
    }
}