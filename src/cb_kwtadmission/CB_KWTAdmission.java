/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cb_kwtadmission;

import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author norhan
 */
public class CB_KWTAdmission {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException {
        try {
    //        SendMessage.sendmessage("prueba mensaje", "eduweb", "0034603751709");//HAY QUE RELLENAR EL NUMERO PARA PROBAR
            // TODO code application logic here
            //scan all the field in a checklist school code CBEL checklist id 7 the itemid is 16 & in CBML 4
            //compare the date in the field with today date
            //if the difference is 1 add the student id in a array
            //loop the array to get the phone number of the parent
            // send sms and write a message in a textfile

            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection cn = DriverManager.getConnection("jdbc:sqlserver://cb-kwt.odbc.renweb.com:1433;databaseName=cb_kwt", "CB_KWT_CUST", "5-cBKwTC)2S$o2h");

            Statement st = cn.createStatement();
            ArrayList<Integer> studentids = new ArrayList<>();
            ArrayList<DateTime> dates = new ArrayList<>();
            ResultSet rs = st.executeQuery("Select td.id,td.note as assesdate,si.studentid as studentid from trackingdata td inner join studentinquiry si on td.id=si.inquiryid where td.trackingsystemid = 7 and itemnumber = 1");
            while (rs.next()) {
                String date = rs.getString("assesdate");
                DateTimeFormatter fmt = DateTimeFormat.forPattern("MM/dd/yyyy");
                DateTime assesDate = null;
                assesDate = fmt.parseDateTime(date);
                DateTime today = new DateTime();
                Days d = Days.daysBetween(assesDate, today);
                int days = d.getDays();
                if (days == 2 && assesDate.isBefore(today) ) {
                    studentids.add(rs.getInt("studentid"));
                    dates.add(assesDate);
                }
            }
            for (int i = 0; i <studentids.size();i++) {
                ResultSet rs1 = st.executeQuery("Select CellPhone from Students where studentid = " + studentids.get(i));
                if (rs1.next()) {
                    String cell = rs1.getString("cellphone");
                    File f = new File("cellphonefound.log");
                    FileWriter fichero = null;
                    try {
                        fichero = new FileWriter(f.getAbsoluteFile(),true);
                            // Escribimos linea a linea en el fichero
                        fichero.write("Cell phone message data: "
                                + "Cellphone="+cell
                                + " IDstudent="+ studentids.get(i) + " Date="
                                + dates.get(i)+ System.getProperty("line.separator"));
                        fichero.close();
                    } catch (Exception ex) {
                        System.out.println("Mensaje de la excepción: " + ex.getMessage());
                    }
                }else{
                    File f = new File("cellphonenotfound.log");
                    FileWriter fichero = null;
                    try {
                        fichero = new FileWriter(f.getAbsoluteFile(),true);
                            // Escribimos linea a linea en el fichero
                        fichero.write("Cell phone not found: "
                                + "IDstudent="+ studentids.get(i) + " Date="
                                + dates.get(i)+ System.getProperty("line.separator"));
                        fichero.close();
                    } catch (Exception ex) {
                        System.out.println("Mensaje de la excepción: " + ex.getMessage());
                    }
                }
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CB_KWTAdmission.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
