package com.search;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.WorkbookDocument;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.WorksheetDocument;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XlsxConfigurator {

        private Sheet workSheet;
        private Workbook workBook;
        private String path;

        public XlsxConfigurator(String path, String sheetName) {
            this.path = path;
            try {
                // Open the Excel file
                FileInputStream ExcelFile = new FileInputStream(path);
                // Access the required test data sheet
                workBook = WorkbookFactory.create(ExcelFile);
                workSheet = workBook.getSheet(sheetName);
                assert(workSheet!=null);//, "Worksheet: \"" + sheetName + "\" was not found\n");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        // we create an object ExcelUtil class to work with specific excel file
        //in this case, file path will be retrieved from properties file
        public XlsxConfigurator(String sheetName) {
            //we are getting path to the excel file from properties file
            this.path = ConfigurationReader.getProperty("users_test_data");
            try {
                // Open the Excel file
                FileInputStream ExcelFile = new FileInputStream(path);
                // Access the required test data sheet
                workBook = WorkbookFactory.create(ExcelFile);
                //Access specific work sheet
                //Then worksheet, contains table with rows and columns
                workSheet = workBook.getSheet(sheetName);
                //if spreadsheet is empty, throw exception
                assert(workSheet!=null);//Assert.assertNotNull(workSheet, "Worksheet: \"" + sheetName + "\" was not found\n");
            } catch (Exception e) {
                //there is no point to continue without file
                throw new RuntimeException(e);
            }
        }

        public String getCellData(int rowNum, int colNum) {
            Cell cell;
            try {
                cell = workSheet.getRow(rowNum).getCell(colNum);
                String cellData = cell.toString();
                return cellData;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        //this method will return data table as 2d array
        //so we need this format because of data provider.
        public String[][] getDataArray() {

            String[][] data = new String[rowCount()][columnCount()];

            for (int i = 0; i < rowCount(); i++) {
                for (int j = 0; j < columnCount(); j++) {
                    String value = getCellData(i, j);
                    data[i][j] = value;
                }
            }
            return data;

        }

        public List<Map<String, String>> getDataList() {
            // get all columns
            List<String> columns = getColumnsNames();
            // this will be returned
            List<Map<String, String>> data = new ArrayList<Map<String,String>>();

            for (int i = 1; i < rowCount(); i++) {
                // get each row
                Row row = workSheet.getRow(i);
                // create map of the row using the column and value
                // column map key, cell value --> map bvalue
                Map<String, String> rowMap = new HashMap<String, String>();
                for (Cell cell : row) {
                    int columnIndex = cell.getColumnIndex();
                    rowMap.put(columns.get(columnIndex), cell.toString());
                }

                data.add(rowMap);
            }

            return data;
        }

        public List<String> getColumnList(int num) {

        List<String> data = new ArrayList<String>();

        for (int i = 1; i < rowCount(); i++) {
            // get each row
            Row row = workSheet.getRow(i);
            int columnIndex = num;

            Cell cell = row
                    .getCell(columnIndex);
            try {
                data.add(cell
                        .toString());
            }catch (RuntimeException e){
                System.out.println(e+"cell num: "+i);
                return data;
            }

        }

        return data;
    }

        public List<String> getColumnsNames() {
            List<String> columns = new ArrayList<String>();

            for (Cell cell : workSheet.getRow(0)) {
                columns.add(cell.toString());
            }
            return columns;
        }

        public void setCellData(String value, int rowNum, int colNum) {
            Cell cell;
            Row row = workSheet.getRow(rowNum);

            try {
                row = row==null? workSheet.createRow(rowNum):row;
                cell = row.getCell(colNum);

                if (cell == null) {
                    cell = row.createCell(colNum);
                    cell.setCellValue(value);
                } else {
                    cell.setCellValue(value);
                }
                FileOutputStream fileOut = new FileOutputStream(path);
                workBook.write(fileOut);
                fileOut.flush();

                fileOut.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void setCellData(String value, String columnName, int row) {
            int column = getColumnsNames().indexOf(columnName);
            setCellData(value, row, column);
        }

        public int columnCount() {
            return workSheet.getRow(0).getLastCellNum();
        }

        public int rowCount() {
            return workSheet.getLastRowNum();
        }


}
