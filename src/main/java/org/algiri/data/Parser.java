package org.algiri.data;

import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    public static List<String> days = new ArrayList<>(){{
        add("ПОНЕДЕЛЬНИК");
        add("ВТОРНИК");
        add("СРЕДА");
        add("ЧЕТВЕРГ");
        add("ПЯТНИЦА");
        add("СУББОТА");
    }};
    @SneakyThrows
    public static void parse(String filename, String... groups) {
        Workbook wb;
        FileInputStream fileInputStream = new FileInputStream("D:\\" + filename);
        wb = new XSSFWorkbook(fileInputStream);
        List<String> groupNameList = List.of(groups);
        int day = 0;
        String timestart = "";
        String timeend  = "";
        for (int i = 6; i<wb.getSheetAt(0).getLastRowNum(); i++) {
            for(Cell cell : wb.getSheetAt(0).getRow(i)) {
                int index = cell.getColumnIndex();
                switch (index) {
                    case 0 -> {
                        if(cell.getStringCellValue().isEmpty()) continue;
                        day = days.indexOf(cell.getStringCellValue().trim());
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
                            DataBase.getINSTANCE().addTimeTableData(day, timestart, timeend,
                                    lessons.values().stream().toList().get(z),
                                    lessons.keySet().stream().toList().get(z),
                                    i%2==1);
                        }
                    }
                }
            }
        }
    }
}
