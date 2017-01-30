package CallCenter;
import desmoj.core.simulator.*;
import java.util.concurrent.TimeUnit;

public class CallGeneratorEvent extends ExternalEvent{
     
    public CallGeneratorEvent(Model owner, String name, boolean showInTrace) {
      super(owner, name, showInTrace);
   }
      /**
    * The eventRoutine() describes the generating of a new incoming call.
    *
    * It creates a new call, a new IncomingCallEvent
    * and schedules itself again for the next new call generation.
    */
   public void eventRoutine() {

      // get a reference to the model
      CallCenter model = (CallCenter)getModel();

      // create a new call
      Call newCall = new Call(model, "New Call", true);
      // create a new call arrival event
      DispatchCallEvent callArrival = new DispatchCallEvent(model,
                                              "DispatchCallEvent", true);
      // and schedule it for the current point in time
      callArrival.schedule(newCall, new TimeSpan(0));
 
      // schedule this call generator again for the next call arrival time

      schedule(new TimeSpan(model.getCallArrivalTime(), TimeUnit.MINUTES));
      // from inside to outside...
      // draw a new inter-arrival time value
      // wrap it in a TimeSpan object
      // and schedule this event for the current point in time + the
      // inter-arrival time
        
      //Inserting the process, which the new call will be associated with
       
      newCall.processID= "IC"+ String.format("%04d", Global.AutoProcessID()) ;
       
      Global.dbHandler.ExecuteCmd("Insert into Process (ProcessDefinitionID,ProcessName) "
      + "values('"+newCall.processID+"','Incoming.Call')");
     //Saving the new call record into the database
      Global.dbHandler.ExecuteCmd("Insert into Call (CallID,ClientID,CategoryID,ProcessDefinitionID) "
      + "values("+newCall.callID+","+newCall.callerID+","+newCall.serviceCategory+",'"+newCall.processID+"')");
   
     //Writing the event record into the database
    //Auto-generated event ID
      int eventID=Global.AutoEventID();
      //Executing insert statement
      //model.presentTime() is the EventTriggerTime of the current event
      //EventTypeID=1 because it's a "New Incoming Call" event (just a database enumeration)
      Global.dbHandler.ExecuteCmd("Insert into Event (CallID,EventID,ExpID,EventTypeID,PrevEventTypeID,EventTriggerTime,TimeStamp,ActivityDefinitionID) "
      + "values("+newCall.callID+ "," +eventID+","+Global.experimentID+",1,1,"+model.presentTime()+","+Global.Timestamp(model.presentTime().getTimeAsDouble())+ ","+"'PR'"+")");
//Writing the "CallAssignments" record
         Global.dbHandler.ExecuteCmd("Insert into CallAssignments (CallID,LocationID) "
      + "values("+newCall.callID+","+newCall.callLocation +")");

   }
}
