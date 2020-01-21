import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Analizator {

    // Метод разархивирования и сортировки файлов
    public void analyzing(String zip_path, String tools_path, String out_path) {
        File fileZ = new File(zip_path);
        File fileT = new File(tools_path);
        File fileO = new File(out_path);
        Workbook book;
        FileOutputStream fout;

        System.out.println(Arrays.toString(fileT.getName().split("\\.")));

        if (!fileZ.exists() || !fileO.exists() || !fileT.exists())
            return;

        try (ZipInputStream zin = new ZipInputStream(new FileInputStream(fileZ))) {
            String arr[] = fileT.getName().split("\\.");

            if (arr[arr.length - 1].equals("xls")) {
                book = new HSSFWorkbook(new FileInputStream(fileT));
            } else if (arr[arr.length - 1].equals("xlsx")){
                book = new XSSFWorkbook(new FileInputStream(fileT));
            } else return;

            // todo Считываем колонки из fileT и создаём папки под кажду колонку
            Sheet sheet = book.getSheet(book.getSheetName(0)); // Берём лист
            Row row = sheet.getRow(0); // Берём строку с именами столбцов
            Cell cell;
            LinkedList<String> listOfCellsName = new LinkedList<>();
            File newDirectory;

            for (int i = 1; i <= row.getLastCellNum(); i++) {
                cell = row.getCell(i);

                if (cell != null && cell.getStringCellValue() != null){
                    listOfCellsName.add(row.getCell(i).getStringCellValue());

                    String path = out_path + "/" + listOfCellsName.getLast();

                    newDirectory = new File(path);
                    newDirectory.mkdir();
                }
            }

            // todo Считываем значение из ячеек и создаём под каждое уникальное значение подпапку

            // todo Копируем из архива файлы в нужные подпапки

            ZipEntry entry;
            String name;
            long size;
            Random r = new Random();
//
//            FileUtils.copyFile(fileT, fileO);

//            while((entry = zin.getNextEntry()) != null) {
//                fout = new FileOutputStream(fileO.getAbsoluteFile() + "/" + String.valueOf(r.nextInt()) + ".jpg");
//
//                name = entry.getName(); // получим название файла
//                size = entry.getSize();  // получим его размер в байтах
//                System.out.printf("File name: %s \t File size: %d \n", name, size);
//
//                // распаковка
//                for (int c = zin.read(); c != -1; c = zin.read()) {
//                    fout.write(c);
//                }
//
//                fout.flush();
//                zin.closeEntry();
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
