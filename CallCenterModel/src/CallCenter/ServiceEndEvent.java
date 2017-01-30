package CallCenter;
import desmoj.core.simulator.*;
import java.util.concurrent.TimeUnit;
import sun.management.Agent;
/**
 * This class represents the service end event
 * in the CallCenter model.
 * It occurs when a call has been successfully handled by the agent.
 */
public class ServiceEndEvent extends EventOf2Entities<CallAgent,Call>{
      /**
    * A reference to the model this event is a part of.
    */
   private CallCenter myModel;
 /**
    * Constructor of the service end event
    * Used to create a new service end event
    * @param owner the model this event belongs to
    * @param showInTrace flag to indicate if this event shall produce output
    *                    for the trace
    */
   public ServiceEndEvent(Model owner, String name, boolean showInTrace) {
      super(owner, name, showInTrace);
      // store a reference to the model this event is associated with
      myModel = (CallCenter)owner;
     
   }
   public void CheckCallQueue(Queue<Call> callQueue,CallAgent agent,double serviceTime,Queue<CallAgent> idleAgentQueue)
   {
       //Looping through the calls being on-hold
      //If the call's waiting time exceeded the max waiting time
      //given by 'myModel.MaxnHoldTime'
      //Then the call abondons the queue 
      //And numberOfAbondonedCalls is incremented
       for(int i=0;i< callQueue.length();i++ )
       {
            Call waitingCall= callQueue.get(i);
            double  waitingTime=myModel.presentTime().getTimeAsDouble()- waitingCall.startTime;
            if(waitingCall.onHold==true&&waitingTime>myModel.MaxnHoldTime)   
              {
                  if(waitingCall.switchedCall==true)//Case that a call abandons the queue
                  {
                //System.out.println(waitingTime);
                callQueue.remove(i);
                myModel.numberOfAbondonedCalls.update();
                
                 //Writing the event record into the database
                //Auto-generated event ID
                int eventID=Global.AutoEventID();
                //Executing insert statement
                //myModel.presentTime() is the EventTriggerTime of the current event
                //EventTypeID=4 because it's a "Client Abandons Queue" event (just a database enumeration)
                Global.dbHandler.ExecuteCmd("Insert into Event (CallID,EventID,ExpID,EventTypeID,PrevEventTypeID,EventTriggerTime,TimeStamp,ActivityDefinitionID) "
                + "values("+waitingCall.callID+","+eventID+","+Global.experimentID+",4,3,"+myModel.presentTime()+","+Global.Timestamp(myModel.presentTime().getTimeAsDouble())+","+"EQ" +")");
                  }
                  else//Switching call to the backup call centre
                  {
                    callQueue.remove(i);
                    myModel.numberOfSwitchedCalls.update();
                    waitingCall.switchedCall=true;
                    //Recounting waiting time at the new queue
                    waitingCall.startTime=myModel.presentTime().getTimeAsDouble();
                    int backupCenterIndex=(int)waitingCall.backupCenter-10;
                    int categoryIndex=(int)waitingCall.serviceCategory-1;
                    Queue<Call> switchCallQueue=  (Queue<Call>)myModel.callQueues[backupCenterIndex][categoryIndex];
                    switchCallQueue.insert(waitingCall);
                    //Recordnig new event for switching the call
               
    //Auto-generated event ID
      int eventID=Global.AutoEventID();
      //Executing insert statement
      //model.presentTime() is the EventTriggerTime of the current event
      //EventTypeID=1 because it still a "Open.NotRunning.Ready" event (just a database enumeration)
      Global.dbHandler.ExecuteCmd("Insert into Event (CallID,EventID,ExpID,EventTypeID,PrevEventTypeID,EventTriggerTime,TimeStamp,ActivityDefinitionID) "
      + "values("+waitingCall.callID+ "," +eventID+","+Global.experimentID+",1,3,"+myModel.presentTime()+","+Global.Timestamp(myModel.presentTime().getTimeAsDouble())+","+"'SC'"+")");
      //Writing the "CallAssignments" record
      Global.dbHandler.ExecuteCmd("Insert into CallAssignments (CallID,LocationID) "
      + "values("+waitingCall.callID+","+waitingCall.backupCenter +")");
                  }
             
                  }
       }
        // check if there are other calls waiting
      if (!callQueue.isEmpty())
          {
         // YES, there is at least one other call waiting
         // remove the first waiting call from the queue
         Call nextCall = callQueue.first();
        //In case the call is on-hold
         nextCall.onHold=false;
         callQueue.remove(nextCall);
         //Create a new service end event
         ServiceEndEvent event = new ServiceEndEvent(myModel, "ServiceEndEvent", true);
         // and schedule it for at the appropriate time
         event.schedule(agent, nextCall, new TimeSpan(serviceTime, TimeUnit.MINUTES));
      }
      else {
         // NO, there are no calls on-hold
         // --> the agent is marked as idle
         idleAgentQueue.insert(agent);
         // the agent is now waiting for a new incoming call to arrive.
      }
   }
   public void eventRoutine(CallAgent agent, Call call) {


// pass the departure of the truck to the trace
      sendTraceNote(call + "Call ends.");
//Selecting the call queue according to the service category
      
       int callCentre=(int)call.callLocation-10;
       int category=(int)call.serviceCategory-1;
       CheckCallQueue((Queue<Call>)myModel.callQueues[callCentre][category], agent,myModel.getServiceTime(category), (Queue<CallAgent>) myModel.agentQueues[callCentre][category]);

//  switch ((int)call.serviceCategory)
//  {
//         case 1:
//             CheckCallQueue((Queue<Call>)myModel.callQueues[0], agent,myModel.getServiceTime1(), myModel.idleAgentQueue1);
//             break;
//         case 2:
//            CheckCallQueue((Queue<Call>)myModel.callQueues[1], agent,myModel.getServiceTime2(), myModel.idleAgentQueue2);
//             break;
//         case 3:
//            CheckCallQueue((Queue<Call>)myModel.callQueues[2], agent,myModel.getServiceTime3(), myModel.idleAgentQueue3);
//             break;
//         case 4:
//             CheckCallQueue((Queue<Call>)myModel.callQueues[3], agent,myModel.getServiceTime4(), myModel.idleAgentQueue4);
//             break;
//  }  
    }
}
