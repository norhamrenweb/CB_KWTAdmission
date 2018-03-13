/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cb_kwtadmission;

import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
    public static void main(String[] args) throws SQLException  {
       
        
  
            // TODO code application logic here
            //scan all the field in a checklist school code CBEL checklist id 7 the itemid is 16 & in CBML 4
            //compare the date in the field with today date
            //if the difference is 1 add the student id in a array
            //loop the array to get the phone number of the parent
            // send sms and write a message in a textfile
            ElementaryStudents.getStudents();
            MSHSStudents.getStudents();
          

    }
    
    

}
