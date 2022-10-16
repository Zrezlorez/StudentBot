package org.algiri;

import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DataBase {
    public Sheet timetable;
    public Sheet users;

    @SneakyThrows
    public DataBase(){
        FileInputStream fileInputStream = new FileInputStream("timetable.xlsx");
        FileInputStream fileInputStream2 = new FileInputStream("users.xlsx");
        timetable = new XSSFWorkbook(fileInputStream).getSheetAt(0);
        users = new XSSFWorkbook(fileInputStream2).getSheetAt(0);
    }

    @SneakyThrows
    public List<String> getUserData(long userId){
        List<String> result = new ArrayList<>();
        for(int i = 0; i<users.getLastRowNum()+1; i++) {
            long user = (long) users.getRow(i).getCell(0).getNumericCellValue();
            if (user==userId) {
                result.add(String.valueOf((long)users.getRow(i).getCell(0).getNumericCellValue()));
                result.add(users.getRow(i).getCell(1).getStringCellValue());
                result.add(users.getRow(i).getCell(2).getStringCellValue());
            }
        }
        return result;
    }

    @SneakyThrows
    public List<String> getTimeTableData(boolean isNumerator, int day, String group, int function) {
        List<String> result = new ArrayList<>();
        if (function>2) isNumerator=!isNumerator;
        for(int i = 0; i<timetable.getLastRowNum()+1; i++) {
            boolean isNowWeek = timetable.getRow(i).getCell(5).getBooleanCellValue()==isNumerator && timetable.getRow(i).getCell(4).getStringCellValue().equals(group);
            if(!isNowWeek) continue;
            if (function%2==0) {
                addToResult(result, i);
                continue;
            }
            if (timetable.getRow(i).getCell(0).getNumericCellValue()==day) {
                addToResult(result, i);
            }
        }
        return result;
    }

    private void addToResult(List<String> result, int i) {
        result.add(String.valueOf(Math.round(timetable.getRow(i).getCell(0).getNumericCellValue())));
        result.add(timetable.getRow(i).getCell(1).getStringCellValue());
        result.add(timetable.getRow(i).getCell(2).getStringCellValue());
        result.add(timetable.getRow(i).getCell(3).getStringCellValue());
        result.add(timetable.getRow(i).getCell(4).getStringCellValue());
        result.add(String.valueOf(timetable.getRow(i).getCell(5).getBooleanCellValue()));
    }

    @SneakyThrows
    public List<String> insertUsersData(long id, String group, String name) {
        List<String> result = new ArrayList<>();
        Row newRow = users.createRow(users.getLastRowNum());
        newRow.createCell(0).setCellValue(id);
        result.add(String.valueOf(id));
        newRow.createCell(1).setCellValue(group);
        result.add(group);
        newRow.createCell(2).setCellValue(name);
        result.add(name);
        FileOutputStream outFile = new FileOutputStream("users.xlsx");
        users.getWorkbook().write(outFile);
        outFile.close();
        return result;
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