package support;
import java.io.*;
import org.apache.poi.ss.usermodel.*;
import java.util.Locale;
/**
 * 
 * @author         :Sathish
 * @since          :Sep 20, 2021
 * @filename       :DataReader.java
 * @version		   :
 * @description    :
 */
public class DataReaderEXCEL extends DataReader{
	private String fileName;
	private String sheetName;
	private String testName;
	Sheet sheet;
	
	public DataReaderEXCEL(String fileName,String sheetName,String testName) {
		this.fileName = fileName;
		this.sheetName = sheetName;
		this.testName = testName;
	}
	
	/**
	 * 
	 * Method Name : get
	 * Return Type : String
	 * Author      : Sathish
	 * Date		   : Sep 20, 2021
	 * Description : This method is to return the test data by giving the column name of the sheet as the parameter
	 * @param dataName : The data which is to be read from the excel file
	 * @return
	 */
	@Override
	public String get(String dataName) {
		InputStream inputStream;
		Workbook wb;
		DataFormatter formatter = new DataFormatter(Locale.US);
		try {
			
			if(sheet==null) {
				inputStream = new FileInputStream(new File(fileName));
				wb = WorkbookFactory.create(inputStream);
				sheet = wb.getSheet(sheetName);
			}

			boolean firstRow = true;
			Row dtColumnNames = null;
			for (Row row : sheet) {
				if (firstRow == true) {
					dtColumnNames = row;
					firstRow = false;
					continue;
				}
				
				Cell cell = row.getCell(1);
				String dtTestName = formatter.formatCellValue(cell).trim();
				//Reference is to point the row corresponding to the test which is being executed
				if(dtTestName.equalsIgnoreCase(testName)) {
					for(int column = 0;column<row.getLastCellNum();column++) {
						if(dtColumnNames.getCell(column).getStringCellValue().trim().equalsIgnoreCase(dataName.trim())) {
							return formatter.formatCellValue(row.getCell(column)).toString().trim();
						}
					}
				}
			}
			return "NOT_FOUND";
			
		} catch (Exception e) {
			return "NOT_FOUND";
		}
	}
}