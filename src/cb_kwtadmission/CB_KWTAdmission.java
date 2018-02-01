/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cb_kwtadmission;

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
            // TODO code application logic here
            //scan all the field in a checklist school code CBEL checklist id 7 the itemid is 16 & in CBML 4
            //compare the date in the field with today date
            //if the difference is 1 add the student id in a array
            //loop the array to get the phone number of the parent
            // send sms and write a message in a textfile
            
            
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection cn = DriverManager.getConnection("jdbc:sqlserver://deb-qat.odbc.renweb.com:1433;databaseName=deb_qat","DEB_QAT_CUST","UnderQuiet+227");
            
            Statement st = cn.createStatement();
            ArrayList<Integer> studentids = new ArrayList<>();
            ResultSet rs = st.executeQuery("Select td.id,td.note as assesdate,si.studentid as studentid from trackingdata td inner join studentinquiry si on td.id=si.inquiryid where td.trackingsystemid = 7 and itemnumber = 1");
            while(rs.next())
            {
                DateTime assesDate = new DateTime(rs.getString("assesdate"));
                DateTime today = new DateTime();
                Days d = Days.daysBetween(assesDate, today);
            int days = d.getDays();
                 if(days==1)
                 {
                     studentids.add(rs.getInt("studentid"));
                 }
            }
            for(Integer s:studentids)
            {
            ResultSet rs1 = st.executeQuery("Select CellPhone from Students where studentid = " + s);
            while(rs1.next())
            {
                String cell = rs1.getString("cellphone");
                //send the message and write in teh text file, also write if the cell phone was not found
            }
            }
            } catch (ClassNotFoundException ex) {
            Logger.getLogger(CB_KWTAdmission.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
