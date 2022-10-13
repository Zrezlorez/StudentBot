package org.algiri;

import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.algiri.bots.AbstractBot.GROUPNAME_LIST;

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
    public static void parse() {
        Workbook wb;
        Workbook wb2;
        FileInputStream fileInputStream = new FileInputStream("");
        FileInputStream fileInputStream2 = new FileInputStream("timetable.xlsx");
        wb = new XSSFWorkbook(fileInputStream);
        wb2 = new XSSFWorkbook(fileInputStream2);

        int day = 0;
        String timestart = "";
        String timeend  = "";
        Sheet sheet = wb2.getSheetAt(0);
        int num = 1;
        for (int i = 1; i<77; i++) {
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
                                .replaceAll("\\s([А-ЯЁ])", " $1"));

                        if(Pattern.compile("[лЛ]ек|культура").matcher(value.toString()).find()) {
                            for(int x = 0; x<5; x++)
                                lessons.put(GROUPNAME_LIST.get(x), value.toString());
                        }
                        if(value.toString().contains("Иностранный")) {
                            Matcher matcher = Pattern.compile("[0-9]+[А-я ]+/[0-9А-я]+").matcher(value.toString());
                            value = new StringBuilder("Иностранный язык");
                            while (matcher.find()) {
                                value.append(" - ").append(matcher.group(0));
                            }
                            lessons.put(GROUPNAME_LIST.get(cell.getColumnIndex()-2), value.toString());
                        }
                        lessons.put(GROUPNAME_LIST.get(cell.getColumnIndex()-2), Pattern.compile(".[лЛ]аб").matcher(value.toString()).replaceAll("\nлаб"));
                        for(int z = 0; z< lessons.size(); z++) {
                            sheet.createRow(num-1).createCell(0).setCellValue(day);
                            sheet.getRow(num-1).createCell(1).setCellValue(timestart);
                            sheet.getRow(num-1).createCell(2).setCellValue(timeend);
                            sheet.getRow(num-1).createCell(3).setCellValue(lessons.values().stream().toList().get(z));
                            sheet.getRow(num-1).createCell(4).setCellValue(lessons.keySet().stream().toList().get(z));
                            sheet.getRow(num-1).createCell(5).setCellValue(cell.getCellStyle().getFillForegroundColor()==0);
                            num++;
                        }
                    }
                }
            }
            FileOutputStream outFile = new FileOutputStream("D:\\z.xlsx");
            wb2.write(outFile);
        }
    }
}
