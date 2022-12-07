package org.algiri;

import lombok.SneakyThrows;
import org.algiri.model.Function;
import org.algiri.model.Lesson;
import org.algiri.model.UserData;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DataBase {

    private static DataBase INSTANCE;
    private Sheet timetable;
    private Sheet users;
    @SneakyThrows
    public DataBase() {
        FileInputStream fileInputStream = new FileInputStream("timetable.xlsx");
        timetable = new XSSFWorkbook(fileInputStream).getSheetAt(0);
        FileInputStream fileInputStream2 = new FileInputStream("users.xlsx");
        users = new XSSFWorkbook(fileInputStream2).getSheetAt(0);
        fileInputStream.close();
        fileInputStream2.close();
    }

    public static DataBase getINSTANCE() {
        if(INSTANCE == null)
            INSTANCE = new DataBase();
        return INSTANCE;
    }

    @SneakyThrows
    public UserData getUserData(long userId){
        UserData data = new UserData();
        for(int i = 0; i<=users.getLastRowNum(); i++) {
            long id = (long) users.getRow(i).getCell(0).getNumericCellValue();
            if (id==userId) {
                data.setId(id);
                data.setGroup(users.getRow(i).getCell(1).getStringCellValue());
                data.setName(users.getRow(i).getCell(2).getStringCellValue());
                break;
            }
        }
        return data;
    }
    @SneakyThrows
    public List<UserData> getAllUsersData(){
        List<UserData> result = new ArrayList<>();
        for(int i = 0; i<users.getLastRowNum()+1; i++) {
            UserData user = new UserData(
                    (long)users.getRow(i).getCell(0).getNumericCellValue(),
                    users.getRow(i).getCell(1).getStringCellValue(),
                    users.getRow(i).getCell(2).getStringCellValue());
            result.add(user);
        }
        return result;
    }
    @SneakyThrows
    public List<Lesson> getTimeTableData(boolean isNumerator, Function function, String group) {
        if(function == Function.NEXT_WEEK || function == Function.TOMORROW_SUNDAY)
            isNumerator = !isNumerator;
        List<Lesson> result = new ArrayList<>();
        for(int i = 0; i<=timetable.getLastRowNum(); i++) {
            Lesson lesson = createLesson(i);
            if(lesson.isNumerator() == isNumerator) {
                if (function == Function.TEACHER)
                    result.add(lesson);
                else if(lesson.getGroup().equals(group))
                    result.add(lesson);
            }
        }
        return result;
    }

    private Lesson createLesson(int rowNum) {
        Lesson lesson = new Lesson();
        lesson.setDay((int)timetable.getRow(rowNum).getCell(0).getNumericCellValue());
        lesson.setTimeStart(timetable.getRow(rowNum).getCell(1).getStringCellValue());
        lesson.setTimeEnd(timetable.getRow(rowNum).getCell(2).getStringCellValue());
        lesson.setName(timetable.getRow(rowNum).getCell(3).getStringCellValue());
        lesson.setGroup(timetable.getRow(rowNum).getCell(4).getStringCellValue());
        lesson.setNumerator(timetable.getRow(rowNum).getCell(5).getBooleanCellValue());
        return lesson;
    }

    @SneakyThrows
    public UserData insertUsersData(long id, String group, String name) {
        UserData user = new UserData();
        Row newRow = users.createRow(users.getLastRowNum()+1);
        newRow.createCell(0).setCellValue(id);
        newRow.createCell(1).setCellValue(group);
        newRow.createCell(2).setCellValue(name);
        user.setId(id);
        user.setGroup(group);
        user.setName(name);
        FileOutputStream outFile = new FileOutputStream("users.xlsx");
        users.getWorkbook().write(outFile);
        outFile.close();
        return user;
    }

    @SneakyThrows
    public void updateUsersData(long userId, String group) {
        for(int i = 0; i<users.getLastRowNum()+1; i++) {
            if ((long)users.getRow(i).getCell(0).getNumericCellValue()==userId) {
                users.getRow(i).getCell(1).setCellValue(group);
            }
        }
        FileOutputStream outFile = new FileOutputStream("users.xlsx");
        users.getWorkbook().write(outFile);
        outFile.close();
    }
}