package support;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;



public class DBConnection {
    Configurations configurations = Configurations.getInstance();
    private static DBConnection dbConnection;

    /**
     * 
     * @return
     */

    public static DBConnection getInstance() {
        if (dbConnection == null) {
            dbConnection = new DBConnection();
        }
        return dbConnection;
    }


    /**
     * 
     * @param marketerAssignedID
     * @param accountNo
     * @param company
     * @return
     */
    public synchronized String validateNewEnrollmentInDatabase(String marketerAssignedID, String offerId, String accountNo, String company) {
        String discoveryServerName = configurations.getProperty("DiscoveryServerName");
        String discoverydatabaseName = configurations.getProperty("DiscoverydatabaseName");
        String newEnrollmentQuery = configurations.getProperty("NewEnrollmentQuery");
        String ratePlanOfferingQuery = configurations.getProperty("RatePlanOfferingQuery");
        String url = "jdbc:sqlserver://" + discoveryServerName + ";databaseName=" + discoverydatabaseName + ";integratedSecurity=true";
        String resultLog = "";
        Connection conn = null;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            System.out.println(newEnrollmentQuery + "'" + marketerAssignedID + "' AND ACCOUNTNO = '" + accountNo + "';");
            ResultSet rs = stmt.executeQuery(newEnrollmentQuery + "'" + marketerAssignedID + "' AND ACCOUNTNO = '" + accountNo + "';");

            while (rs.next()) {

                if (rs.getString("MarketerAssignedID").trim().equalsIgnoreCase(marketerAssignedID.trim())) {
                    resultLog = resultLog + "<pre> MarketerAssignedID : " + marketerAssignedID + " matches with DB </pre>";
                } else {
                    resultLog = resultLog + "<pre> MarketerAssignedID : " + marketerAssignedID + " doesn't match with DB </pre>";
                }

                if (rs.getString("EscoRatePlanOfferingKey").length() > 0) {

                    String offerIdKey = rs.getString("EscoRatePlanOfferingKey");
                    resultLog = resultLog + "<pre> EscoRatePlanOfferingKey in DB : " + offerIdKey + " </pre>";
                    ratePlanOfferingQuery = ratePlanOfferingQuery.replace("_REPLACE_OFFER_ID_", offerIdKey);
                    Statement stmt1 = conn.createStatement();
                    ResultSet rs1 = stmt1.executeQuery(ratePlanOfferingQuery);

                    while (rs1.next()) {
                        resultLog = resultLog + "<pre> Rate Plan Description in DB : " + rs1.getString("Description") + " </pre>";
                        break;
                    }

                } else {
                    resultLog = resultLog + "<pre> EscoRatePlanOfferingKey is empty in DB </pre>";
                }

                if (rs.getString("ACCOUNTNO").trim().equalsIgnoreCase(accountNo.trim())) {
                    resultLog = resultLog + "<pre> ACCOUNTNO : " + accountNo + " matches with DB </pre>";
                } else {
                    resultLog = resultLog + "<pre> ACCOUNTNO : " + accountNo + " doesn't match with DB </pre>";
                }

                if (rs.getString("COMPANY").trim().equalsIgnoreCase(company.trim())) {
                    resultLog = resultLog + "<pre> COMPANY : " + company + " matches with DB </pre>";
                } else {
                    resultLog = resultLog + "<pre> COMPANY : " + company + " doesn't match with DB </pre>";
                }

                if (rs.getString("FIXEDSTART").length() > 0) {
                    resultLog = resultLog + "<pre> Enrollment Start Date in DB : " + rs.getString("FIXEDSTART") + " </pre>";
                } else {
                    resultLog = resultLog + "<pre> Enrollment Start Date is empty in DB </pre>";
                }

                if (rs.getString("FIXEDEND").length() > 0) {
                    resultLog = resultLog + "<pre> Enrollment End Date in DB : " + rs.getString("FIXEDEND") + " </pre>";
                } else {
                    resultLog = resultLog + "<pre> Enrollment End Date is empty in DB </pre>";
                }
            }
            if (resultLog.length() < 1) {
                resultLog = "NO RECORDS CREATED!!!";
            }
            conn.close();
        } catch (Exception e) {
            try {
                conn.close();
            } catch (Exception e1) {}
            return "EXCEPTION IN CONNECTING TO DATABASE!!!";
        }
        return resultLog;
    }



}