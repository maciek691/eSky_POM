package pl.esky.other;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class FileInput {


    public ArrayList<String> getData(String testCaseName) throws IOException {

        // create a list of testing data
        ArrayList<String> data = new ArrayList<String>();

        // create an object XSSCWorkbook class
        FileInputStream file = new FileInputStream("src/main/resources/emails.xlsx");
        XSSFWorkbook workbook = new XSSFWorkbook(file);

        // get the right tab
        int sheets = workbook.getNumberOfSheets();
        for (int i = 0; i < sheets; i++) {
            //emails is the name of the tab
            if (workbook.getSheetName(i).equalsIgnoreCase("emails")) {
                XSSFSheet sheet = workbook.getSheetAt(i);

                // identify Testcases column by scanning the entire 1st row
                Iterator<Row> rows = sheet.iterator();
                Row firstRow = rows.next();
                Iterator<Cell> cellIterator = firstRow.cellIterator();
                int k = 0;
                int column = 0;

                while (cellIterator.hasNext()) {
                    Cell value = cellIterator.next();
                    // TestCases it is the name of the column
                    if (value.getStringCellValue().equalsIgnoreCase("TestCases")) {
                        column = k;
                    }
                    k++;
                }
                // once column is identified then scan entire testcase column to identify purchase testcase row
                while (rows.hasNext()) {
                    Row r = rows.next();
                    if (r.getCell(column) != null) {
                        if (r.getCell(column).getStringCellValue().equalsIgnoreCase(testCaseName)) {
                            Iterator<Cell> cv = r.cellIterator();
                            while (cv.hasNext() == true ) {
                                Cell c = cv.next();
                                if (c.getCellType() == CellType.STRING) {
                                    data.add(c.getStringCellValue());
                                } else {
                                    data.add(NumberToTextConverter.toText(c.getNumericCellValue()));
                                }
                            }
                        }
                    }
                }
            }
        }
        return data;
    }
}
