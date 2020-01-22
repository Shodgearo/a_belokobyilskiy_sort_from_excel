import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Analizator {

    // Метод разархивирования и сортировки файлов
    void analyzing(String zip_path, String tools_path, String out_path) {
        File fileZ = new File(zip_path);
        File fileT = new File(tools_path);
        File fileO = new File(out_path);
        Workbook book;
        FileOutputStream fout;

        // Если настроек не достаточно, то выходим
        if (!fileZ.exists() || !fileO.exists() || !fileT.exists())
            return;

        try (ZipInputStream zin = new ZipInputStream(new FileInputStream(fileZ))) {
            String arr[] = fileT.getName().split("\\.");

            switch (arr[arr.length - 1]) {
                case "xls":
                    book = new HSSFWorkbook(new FileInputStream(fileT));
                    break;
                case "xlsx":
                    book = new XSSFWorkbook(new FileInputStream(fileT));
                    break;
                default:
                    return;
            }

            Sheet sheet = book.getSheet(book.getSheetName(0)); // Берём лист
            Row row = sheet.getRow(0); // Берём строку с именами столбцов
            Cell cell;
            File newDirectory;

            // Создаём папки с названием ячеек в шапке
            for (int i = 1; i <= row.getLastCellNum(); i++) {
                cell = row.getCell(i);

                if (cell != null && cell.getStringCellValue() != null){
                    String path = out_path + "/" + row.getCell(i).getStringCellValue();

                    newDirectory = new File(path);
                    newDirectory.mkdir();
                }
            }

            ZipEntry entry;

            while((entry = zin.getNextEntry()) != null) {
                // Если это не файл, то делать нечего
                if (!entry.getName().contains("."))
                    continue;

                String arr_elements_path[] = entry.getName().split("/");
                String file_name = arr_elements_path[arr_elements_path.length - 1];

                // Ищем строку в экселе с этим фалом
                Row row_search = searchByName(file_name, sheet);

                if (row_search == null)
                    continue;

                fout = new FileOutputStream(fileO.getAbsoluteFile() + "/" + file_name);

                // распаковка
                for (int c = zin.read(); c != -1; c = zin.read()) {
                    fout.write(c);
                }

                for (int i = 1; i <= row.getLastCellNum(); i++) {
                    cell = row_search.getCell(i);
                    Cell cellX = row.getCell(i);

                    if (cellX == null)
                        continue;

                    String head_name = cellX.getStringCellValue();

                    if (cell != null
                            && cell.getCellType() == CellType.STRING
                            && !cell.getStringCellValue().equals(" ")) {
                        File file1 = new File(fileO.getAbsoluteFile() + "/" + file_name);
                        File file2 = new File(fileO.getAbsoluteFile()
                                + "/"
                                + head_name
                                + "/"
                                + cell.getStringCellValue()
                                + "/"
                                + file_name);

                        FileUtils.copyFile(file1, file2);
                    }
                }

                // Удаляем временный файл
                new File(fileO.getAbsoluteFile() + "/" + file_name).delete();

                fout.flush();
                zin.closeEntry();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Row searchByName(String file_name, Sheet sheet) {
        Cell cell;
        Row row;

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            row = sheet.getRow(i);
            cell = row.getCell(0);

            // Если такой файл есть в екселе, то возвращаем строку
            if (cell != null && cell.getStringCellValue().equals(file_name)) {
                return row;
            }
        }

        // Если ничего не нашли, то идём дальше
        return null;
    }
}
