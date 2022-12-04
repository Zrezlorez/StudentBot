package org.algiri;

import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    public static List<String> days = new ArrayList<>(){{
        add("П О Н Е Д Е Л Ь Н И К");
        add("В Т О Р Н И К");
        add("С Р Е Д А");
        add("Ч Е Т В Е Р Г");
        add("П Я Т Н И Ц А");
        add("С У Б Б О Т А");
    }};
    @SneakyThrows
    public static void parse(String filename, String... groups) {
        Workbook wb;
        Workbook wb2;
        FileInputStream fileInputStream = new FileInputStream("D:\\" + filename);
        FileInputStream fileInputStream2 = new FileInputStream("timetable.xlsx");
        wb = new XSSFWorkbook(fileInputStream);
        wb2 = new XSSFWorkbook(fileInputStream2);
        List<String> groupNameList = List.of(groups);
        int day = 0;
        String timestart = "";
        String timeend  = "";
        Sheet sheet = wb2.getSheetAt(0);
        int num = wb2.getSheetAt(0).getLastRowNum();
        for (int i = 1; i<wb.getSheetAt(0).getLastRowNum(); i++) {
            for(Cell cell : wb.getSheetAt(0).getRow(i)) {
                int index = cell.getColumnIndex();
                switch (index) {
                    case 0 -> {
                        if(cell.getStringCellValue().isEmpty()) continue;
                        day = days.indexOf(cell.getStringCellValue());
                    }
                    case 1 -> { if(cell.getStringCellValue().isEmpty()) continue;
                        timestart = cell.getStringCellValue().replace("\n", "").replace(".", ":");
                        timeend = timestart.substring(6, 11);
                        timestart = timestart.substring(0, 5);
                    }
                }
                if(index >= 2 && index<= 6) {
                    Map<String, String> lessons = new TreeMap<>();
                    if(!cell.getStringCellValue().isEmpty()) {
                        StringBuilder value = new StringBuilder(cell.getStringCellValue().trim()
                                .replaceAll("\\s+", " ")
                                .replaceAll("\\s([а-я])", " $1"));

                        if(Pattern.compile("[Лл]ек|культура").matcher(value.toString()).find()) {
                            for (String s : groupNameList) lessons.put(s, value.toString());
                        }
                        if(value.toString().contains("Иностранный")) {
                            Matcher matcher = Pattern.compile("[0-9]+[А-я]+/[0-9А-я]+").matcher(value.toString());
                            value = new StringBuilder("Иностранный язык");
                            while (matcher.find()) {
                                value.append(" - ").append(matcher.group(0));
                            }
                            lessons.put(groupNameList.get(cell.getColumnIndex()-2), value.toString());
                        }
                        lessons.put(groupNameList.get(cell.getColumnIndex()-2), Pattern.compile(".[Лл]аб").matcher(value.toString()).replaceAll("\nлаб"));
                        for(int z = 0; z< lessons.size(); z++) {
                            num++;
                            sheet.createRow(num).createCell(0).setCellValue(day);
                            sheet.getRow(num).createCell(1).setCellValue(timestart);
                            sheet.getRow(num).createCell(2).setCellValue(timeend);
                            sheet.getRow(num).createCell(3).setCellValue(lessons.values().stream().toList().get(z));
                            sheet.getRow(num).createCell(4).setCellValue(lessons.keySet().stream().toList().get(z));
                            sheet.getRow(num).createCell(5).setCellValue(cell.getCellStyle().getFillForegroundColor()==0);
                        }
                    }
                }
            }
            FileOutputStream outFile = new FileOutputStream("timetable.xlsx");
            wb2.write(outFile);
        }
    }
}
