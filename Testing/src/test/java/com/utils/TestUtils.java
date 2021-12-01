package com.utils;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import support.Settings;
import tests.TestSupporter;
import java.util.*;

public class TestUtils extends TestSupporter {

    public static FileInputStream fs;
    public static XSSFWorkbook workbook;
    public static XSSFSheet sheet;
    public static List < String > testcases = new ArrayList < String > ();
    public static List < String > runstatus = new ArrayList < String > ();
    public static HashMap < Integer,
        String > rowAndTestCaseMap = new HashMap < Integer,
        String > ();

    /**
     * 
     * *Author		: Sathish
     * Method name  : getRunStatus
     * Return types : Nil
     * Description  : Method to get run status of the test cases from excel testRun Manager
     */
    public static void getRunStatus() throws Exception {

        try {
            System.out.println(Settings.getInstance().getResourcesDir() + "TestRunManager.xlsx");
            fs = new FileInputStream(Settings.getInstance().getResourcesDir() + "TestRunManager.xlsx");
            workbook = new XSSFWorkbook(fs);
            sheet = workbook.getSheet("TestRunManager");
            int columNo;
            int rowNo;

            for (rowNo = 0; rowNo <= getLastRowNum("TestRunManager"); rowNo++) {

                columNo = getColumnNumForColumnName("TestRunManager", "Test Case Description");
                //System.out.println(sheet.getRow(rowNo).getCell(columNo).getStringCellValue().toString());
                rowAndTestCaseMap.put(rowNo, sheet.getRow(rowNo).getCell(columNo).getStringCellValue().toString());
                if (sheet.getRow(rowNo).getCell(getColumnNumForColumnName("TestRunManager", "ExecutionStatus")).getStringCellValue().equalsIgnoreCase("YES")) {
                    testcases.add(sheet.getRow(rowNo).getCell(columNo).getStringCellValue().toString());
                }
            }
            for (int i = 0; i < testcases.size(); i++) {

                System.out.println(testcases.get(i));
            }

        } catch (FileNotFoundException e) {

        }
    }

    /**
     * 
     * *Author		: Sathish
     * Method name  : getRowNumForTestCase
     * Return types : Row Number
     * Description  : Method to get row number of the test case
     */
    public static Object getRowNumForTestCase(String testcasename) {
        Object a = null;
        for (Map.Entry m: rowAndTestCaseMap.entrySet()) {
            if (m.getValue().toString().equalsIgnoreCase(testcasename)) {
                a = m.getKey();
            }
        }
        return a;
    }

    /**
     * 
     * *Author		: Sathish
     * Method name  : getRowNumForRowName
     * Return types : Row Number
     * Parameters	: sheetname , rowName
     * Description  : Method to get row number based on parameter rowName
     */
    public static int getRowNumForRowName(String sheetname, String rowName) {
        int rownum = 0;
        sheet = workbook.getSheet(sheetname);
        for (int i = 1; i <= getLastRowNum(sheetname); i++) {
            if (rowName.equalsIgnoreCase(sheet.getRow(i).getCell(0).getStringCellValue())) {
                rownum = i;
                break;
            }
        }

        return rownum;
    }

    /**
     * 
     * *Author		: Sathish
     * Method name  : getColumnNumForColumnName
     * Return types : column  Number
     * Parameters	: sheetname , columnname
     * Description  : Method to get row number based on parameter columnname
     */
    public static int getColumnNumForColumnName(String sheetname, String columnname) {
        int colnum = 0;
        sheet = workbook.getSheet(sheetname);
        for (int i = 0; i < getLastColumnNum(sheetname, 0); i++) {
            if (columnname.equalsIgnoreCase(sheet.getRow(0).getCell(i).getStringCellValue())) {
                colnum = i;
                break;
            }
        }

        return colnum;

    }

    /**
     * *Author		: Sathish
     * Method name  : getLastRowNum
     * Return types : int (Last Row Number)
     * Parameters	: sheetname
     * Description  : Method to get last row number based on parameter sheetname
     */
    public static int getLastRowNum(String sheetname) {
        return workbook.getSheet(sheetname).getLastRowNum();
    }

    /**
     * *Author		: Sathish
     * Method name  : getLastColumnNum
     * Return types : int (Last column Number)
     * Parameters	: sheetname
     * Description  : Method to last column number row number based on parameter rownum
     */
    public static int getLastColumnNum(String sheetname, int rownum) {
        return workbook.getSheet(sheetname).getRow(rownum).getLastCellNum();
    }

    /**
     * *Author		: Sathish
     * Method name  : getCellContent
     * Return types : int (Last column Number)
     * Parameters	: sheetname, rownum ,colnum
     * Description  : Method to the cell value from excel file based on parameters
     **/
    public static String getCellContent(String sheetname, int rownum, int colnum) {
        sheet = workbook.getSheet(sheetname);
        return sheet.getRow(rownum).getCell(colnum).getStringCellValue().toString();

    }

    public static String getCellContent(String sheetname, int rownum, String columnname) {
        sheet = workbook.getSheet(sheetname);
        return sheet.getRow(rownum).getCell(getColumnNumForColumnName(sheetname, columnname)).getStringCellValue().toString();

    }

    /**
     * *Author		: Sathish
     * Method name  : getCellContent
     * Return types : int (Last column Number)
     * Parameters	: sheetname, rownum ,colnum
     * Description  : Method to the cell value from excel file based on parameters
     **/
    public static String getCellContent(String sheetname, String rowname, String columnname) {
        sheet = workbook.getSheet(sheetname);
        int rownum = getRowNumForRowName(sheetname, rowname);
        System.out.println(rownum);
        int colnum = getColumnNumForColumnName(sheetname, columnname);
        System.out.println(colnum);
        return sheet.getRow(rownum).getCell(colnum).getStringCellValue().toString();

    }
    public void updateStatusinExcel(String Testcase, String Status, String startTime) {

        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");

        try {
            System.out.println(Settings.getInstance().getResourcesDir() + "TestRunManager.xlsx");
            fs = new FileInputStream(Settings.getInstance().getResourcesDir() + "TestRunManager.xlsx");
            workbook = new XSSFWorkbook(fs);
            sheet = workbook.getSheet("TestRunManager");
            int statuscolumNo;
            int rowNo;
            int startTimecolumNo;
            int endTimecolumNo;
            //System.out.println("TEST");

            for (rowNo = 0; rowNo <= getLastRowNum("TestRunManager"); rowNo++) {

            	statuscolumNo = getColumnNumForColumnName("TestRunManager", "Status");
                startTimecolumNo = getColumnNumForColumnName("TestRunManager", "TestStartTime");
                endTimecolumNo = getColumnNumForColumnName("TestRunManager", "TestEndTime");
                if (sheet.getRow(rowNo).getCell(getColumnNumForColumnName("TestRunManager", "Test Case Description")).getStringCellValue().equalsIgnoreCase(Testcase)) {
                	sheet.getRow(rowNo).createCell(statuscolumNo).setCellValue(Status);
                    sheet.getRow(rowNo).createCell(startTimecolumNo).setCellValue(startTime);
                    sheet.getRow(rowNo).createCell(endTimecolumNo).setCellValue(formatter.format(new Date()));
                    FileOutputStream fileOut = new FileOutputStream(Settings.getInstance().getResourcesDir() + "TestRunManager.xlsx");
                    workbook.write(fileOut);
                    fileOut.close();
                    break;
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 


    }






}