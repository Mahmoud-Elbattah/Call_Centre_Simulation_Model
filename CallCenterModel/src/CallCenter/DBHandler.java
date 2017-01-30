
package CallCenter;

import java.sql.*;

public class DBHandler {
    private Connection con;
    private Statement st;
    DBHandler (String DatabaseName)
    {
        try
        {
        Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
        con=DriverManager.getConnection("jdbc:odbc:"+DatabaseName);
       
        }
            catch (Exception e)
                {
                     e.printStackTrace();
                }
    }
   public void ExecuteCmd(String cmd)
   {
       try
       {
               st= con.createStatement();
               st.executeQuery(cmd);
                con.commit();
       }
         catch (Exception e)
                {
                 e.printStackTrace();
                }
   }
   public String ReadExpTime()
   {
       String experimentStartTime="";
         try{
   ResultSet rs=st.executeQuery("Select ExpStartTime from Experiment where ExpID="+Global.experimentID);
     
    while(rs.next())
       {
        experimentStartTime=rs.getString("ExpStartTime");
       }
    }
        catch (Exception e)
      {
        //e.printStackTrace();
      } 
   return experimentStartTime;
   }
   
}
