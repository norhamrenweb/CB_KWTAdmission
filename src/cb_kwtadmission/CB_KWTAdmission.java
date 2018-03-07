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
    public static void main(String[] args) throws SQLException {
        String mensaje = "";
        try {
  
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
            
            DateFormat df = new SimpleDateFormat("yyyy_MM_dd_HH_mm");
            String hoy = df.format(new Date(System.currentTimeMillis()));
            
            ResultSet rs = st.executeQuery("Select td.id,td.note as assesdate,si.studentid as studentid from trackingdata td inner join studentinquiry si on td.id=si.inquiryid where td.trackingsystemid = 7 and itemnumber = 1");
            while (rs.next()) {
                String date = rs.getString("assesdate");
                DateTimeFormatter fmt = DateTimeFormat.forPattern("MM/dd/yyyy");
                DateTime assesDate = null;
                assesDate = fmt.parseDateTime(date);
                DateTime today = new DateTime();
                today = today.withHourOfDay(0);
                today = today.withMinuteOfHour(0);
                today = today.withSecondOfMinute(0);
                today = today.withMillisOfSecond(0);
                Days d = Days.daysBetween(today,assesDate);
                int days = d.getDays();
                if (days == 1) {
                    studentids.add(rs.getInt("studentid"));
                    dates.add(assesDate);
                }
                
            }
            if(studentids.isEmpty()){
                File f = new File(hoy+"noStudentsFound.log");
                    FileWriter fichero = null;
                    try {
                        fichero = new FileWriter(f.getAbsoluteFile(),true);
                            // Escribimos linea a linea en el fichero
                        fichero.write("no se encontraron estudiantes");
                        fichero.close();
                    } catch (Exception ex) {
                        System.out.println("Mensaje de la excepción: " + ex.getMessage());
                    }
                    mensaje = "no students found";
            }
                
            
            for (int i = 1; i <studentids.size();i++) {
                String hour = null;
                ResultSet rs2 = st.executeQuery("Select td.id,td.note as hour,si.studentid as studentid from trackingdata td inner join studentinquiry si on td.id=si.inquiryid where td.trackingsystemid = 7 and itemnumber = 2 and si.studentid="+studentids.get(i));
                while (rs2.next()) {
                    hour = rs2.getString("hour");
                }
                ResultSet rs1 = st.executeQuery("Select CellPhone from Students where studentid = " + studentids.get(i));
                if (rs1.next()) {
                    String cell = rs1.getString("cellphone");
                    File f = new File(hoy+"cellphonefound.log");
                    FileWriter fichero = null;
                    try {
                         String message= "Reminder: Your Child's assessment is tomorrow @ The Canadian Bilingual School\n" +
"تذكير: غدا هو يوم الاختبار لإبنك/ ابنتك في المدرسة الكندية ثنائية اللغة";
                        if(hour==null || hour =="")
                        {
                        message= "Reminder: Your Child's assessment is tomorrow @ The Canadian Bilingual School\n" +
"تذكير: غدا هو يوم الاختبار لإبنك/ ابنتك في المدرسة الكندية ثنائية اللغة";
                        }
                        else
                        {
                         message= "Reminder: Your Child's assessment is tomorrow at "+hour+" @ The Canadian Bilingual School\n" +
"تذكير: غدا هو يوم الاختبار لإبنك/ ابنتك  في الساعة  "+hour+" في المدرسة الكندية ثنائية اللغة"   ;
                                }
                        int check = message.length();
                        SendMessage.sendmessage(message, "CB School","00965"+cell);//HAY QUE RELLENAR EL NUMERO PARA PROBAR
                        fichero = new FileWriter(f.getAbsoluteFile(),true);
                            // Escribimos linea a linea en el fichero
                        String m = "Cell phone message data: "
                                + "Cellphone="+cell + "Name Student:" + fetchName(studentids.get(i),st)
                                + " IDstudent="+ studentids.get(i) + " Date="
                                + dates.get(i)+ System.getProperty("line.separator");
                        mensaje +="<br>"+ m;
                        fichero.write(m);
                        fichero.close();
                        
                    } catch (Exception ex) {
                        System.out.println("Mensaje de la excepción: " + ex.getMessage());
                    }
                }else{
                    File f = new File(hoy+"cellphonenotfound.log");
                    FileWriter fichero = null;
                    try {
                        fichero = new FileWriter(f.getAbsoluteFile(),true);
                        String m = "Cell phone not found: "
                                + "IDstudent="+ studentids.get(i) + "Name Student:" + fetchName(studentids.get(i),st) 
                                + " Date=" + dates.get(i)+ System.getProperty("line.separator");
                            
                        mensaje+="<br>"+m;
                        // Escribimos linea a linea en el fichero
                        fichero.write(m);
                        fichero.close();
                    } catch (Exception ex) {
                        System.out.println("Mensaje de la excepción: " + ex.getMessage());
                    }
                }
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CB_KWTAdmission.class.getName()).log(Level.SEVERE, null, ex);
        }
        ArrayList<String> dest = new ArrayList();
        dest.add("nmohamed@eduwebgroup.com");
        dest.add("josepe04@ucm.es");
        Mensaje m = new Mensaje(mensaje,"Admisions sms summary report",dest);
        SendMail.SendMail(m);

    }
    
    private static String fetchName(int id ,Statement st){
        String consulta = "select * from person where personid="+id;
        String ret = "";
        try {
            ResultSet rs = st.executeQuery(consulta);
            while(rs.next()){
                ret+=rs.getString("lastname")+" ,";
                ret+=rs.getString("firstname");
            }
        } catch (SQLException ex) {
            Logger.getLogger(CB_KWTAdmission.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    } 

}
