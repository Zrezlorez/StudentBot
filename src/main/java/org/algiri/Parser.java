package org.algiri;

import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
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
    public static void parse(Statement user) {
        Workbook wb;
        try {
            FileInputStream fileInputStream = new FileInputStream("D:\\is.xlsx");
            wb = new XSSFWorkbook(fileInputStream);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        int day = 0;
        String timestart = "";
        String timeend  = "";

        for (int i = 1; i<77; i++) {
            List<String> lessons = new ArrayList<>();
            boolean isNumerator = i%2==0;
            if((i>37 && i<52) || i>64) {
                isNumerator = i%2==1;
            }
            for(Cell cell : wb.getSheetAt(0).getRow(i)) {
                int index = cell.getColumnIndex();
                switch (index){
                    case 0 -> {
                        if(cell.getStringCellValue().isEmpty()) continue;
                        day = days.indexOf(cell.getStringCellValue());
                    }
                    case 1 -> { if(cell.getStringCellValue().isEmpty()) continue;
                        timestart = cell.getStringCellValue().replace("\n", "").replace(".", ":");
                        timeend = timestart.substring(6, 11);
                        timestart = timestart.substring(0, 5);
                    }
                    case 2, 3, 4, 5, 6 -> {
                        if(cell.getStringCellValue().isEmpty()) {
                            lessons.add("-");
                            continue;
                        }
                        StringBuilder value = new StringBuilder(cell.getStringCellValue().trim()
                                .replaceAll("\\s+", " ")
                                .replaceAll("\\s([А-ЯЁ])", " $1"));

                        if(Pattern.compile("лек|культура").matcher(value.toString()).find()) {
                            for(int x = 0; x<5; x++)
                                lessons.add(value.toString());
                            continue;
                        }
                        if(value.toString().contains("Иностранный")) {
                            Matcher matcher = Pattern.compile("[0-9]+[А-я ]+/[0-9А-я]+").matcher(value.toString());
                            value = new StringBuilder("Иностранный язык");
                            while (matcher.find()) {
                                value.append(" - ").append(matcher.group(0));
                            }
                            lessons.add(value.toString());
                            continue;
                        }
                        lessons.add(Pattern.compile(".[лЛ]аб").matcher(value.toString()).replaceAll("\nлаб"));

                    }
                }
            }
            String sql = String.format("INSERT INTO public.timetable(" +
                    "day, timestart, timeend, is211, is212, is213, is214, is215, \"isNumerator\")" +
                    " VALUES (%d, '%s', '%s', '%s', '%s', '%s', '%s', '%s', %b);", day, timestart, timeend,
                    lessons.get(0),  lessons.get(1), lessons.get(2), lessons.get(3), lessons.get(4), isNumerator);
            user.execute(sql);
        }
    }
}
