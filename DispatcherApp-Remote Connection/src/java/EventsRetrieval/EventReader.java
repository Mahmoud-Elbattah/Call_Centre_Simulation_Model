/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package EventsRetrieval;

import java.sql.*;
import java.util.ArrayList;
public class EventReader {
    private  Connection con;
    private  Statement st;
  public  EventReader (String DatabaseName)
    {
        try
        {
        Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
        con=DriverManager.getConnection("jdbc:odbc:"+DatabaseName);
        st= con.createStatement();
        }
            catch (Exception e)
                {
                            e.printStackTrace();
                }
    }
    //A function to read the event details from "EventView" view
     public ArrayList<Event>  ReadEvents(int triggerTime)
   {
      ArrayList<Event> eventList = new ArrayList<Event>();
        try
      {
          
   ResultSet rs=st.executeQuery("Select EventID,CallID,ClientID,CallCenterID,ProcessDefinitionID,ActivityDefinitionID,EventStatus,Category,Location,TimeStamp,ActivityName,PreEventStatus from EventView where RoundedTriggerTime="+triggerTime);
    //  ResultSet rs=st.executeQuery("Select * from EventView");
  
   while(rs.next())
       {

       Event newEvent=new Event();
       newEvent.eventID= Integer.parseInt(rs.getString("EventID"));
       newEvent.callID= Integer.parseInt(rs.getString("CallID"));
       newEvent.clientID= Integer.parseInt(rs.getString("ClientID"));
       
       newEvent.callCenterID= rs.getString("CallCenterID");
       newEvent.processDefinitionID= rs.getString("ProcessDefinitionID");
       newEvent.activityDefinitionID= rs.getString("ActivityDefinitionID");
       newEvent.eventStatus= rs.getString("EventStatus");
       newEvent.category= rs.getString("Category");
       newEvent.location =rs.getString("Location");
       newEvent.timeStamp= rs.getString("TimeStamp");
       newEvent.activityName= rs.getString("ActivityName");
       newEvent.previousState=rs.getString("PreEventStatus");
    
      eventList.add(newEvent);

       }
      }
      catch (Exception e)
      {
      //  e.printStackTrace();
      }
       return eventList;
   }
public  void FlagSuccess(int eventID)
{
   try
      {
 st.executeQuery("Update Event Set SuccessfullyTransmitted=1 where EventID="+eventID);
      }
   
       catch (Exception e)
      {
        //e.printStackTrace();
      }
}
}
