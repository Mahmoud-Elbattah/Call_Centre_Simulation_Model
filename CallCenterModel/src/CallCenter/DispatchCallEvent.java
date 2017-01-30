
package CallCenter;
import desmoj.core.simulator.*;
import java.util.concurrent.TimeUnit;
import static org.apache.commons.math.util.MathUtils.round;

public class DispatchCallEvent extends Event<Call>{
       private CallCenter myModel;
        /**
    * Constructor of the call arrival event
    * Used to create a new call arrival event
    * @param owner the model this event belongs to
    * @param showInTrace flag to indicate if this event shall produce output
    *                    for the trace
    */
   public DispatchCallEvent(Model owner, String name, boolean showInTrace) {
      super(owner, name, showInTrace);
      // store a reference to the model this event is associated with
      myModel = (CallCenter)owner;
   }

   public void DispatchOrEnqueue(Call call, Queue<CallAgent> idleAgentQueue, double serviceTime,Queue<Call> callQueue)
   {
       
     if (!idleAgentQueue.isEmpty())
         {
         //Get a reference to the first agent from the idle agent queue for category1
         CallAgent agent = idleAgentQueue.first();
         // remove it from the idle queue
         idleAgentQueue.remove(agent);   
         //Icrment total of handled calls
         myModel.numberOfHandledCalls.update();
            ServiceEndEvent serviceEnd = new ServiceEndEvent (myModel,
                                                       "ServiceEndEvent", true);
         //And place it on the event list
         serviceEnd.schedule(agent, call, new TimeSpan(serviceTime, TimeUnit.MINUTES));
        
         //Writing the event record into the database
//Auto-generated event ID
      int eventID=Global.AutoEventID();
      //Executing insert statement
      //myModel.presentTime() is the EventTriggerTime of the current event
      //EventTypeID=2 because it's a "Schedule for Service" event (just a database enumeration)
    int prevEventStatus=0;
    if(call.onHold==true)
        prevEventStatus=3;
    else
        prevEventStatus=1;
      
      Global.dbHandler.ExecuteCmd("Insert into Event (CallID,EventID,ExpID,EventTypeID,PrevEventTypeID,EventTriggerTime,TimeStamp,ActivityDefinitionID) "
      + "values("+call.callID+","+eventID+","+Global.experimentID+",2,"+prevEventStatus+","+myModel.presentTime()+","+ Global.Timestamp(myModel.presentTime().getTimeAsDouble())+","+"'DC'"+")");
   
               
         //Writing the event record into the database
        //Auto-generated event ID
      int eventID2=Global.AutoEventID();
       double triggerTime=myModel.presentTime().getTimeAsDouble()+serviceTime;
      //Executing insert statement
      //myModel.presentTime() is the EventTriggerTime of the current event
      //EventTypeID=4 because it's a "Service End" event (just a database enumeration)
      Global.dbHandler.ExecuteCmd("Insert into Event (CallID,EventID,ExpID,EventTypeID,PrevEventTypeID,EventTriggerTime,TimeStamp,ActivityDefinitionID) "
      + "values("+call.callID+","+eventID2+","+Global.experimentID+",5,2,"+triggerTime+","+Global.Timestamp(triggerTime)+","+"'RS'" +")");
         }
         else
         {
         //Insert the call into the queue
         callQueue.insert(call);
         call.onHold=true;
         
        //Writing the event record into the database
        //Auto-generated event ID
     int  eventID3=Global.AutoEventID();
      //Executing insert statement
      //myModel.presentTime() is the EventTriggerTime of the current event
      //EventTypeID=3 because it's a "Put Call On-Hold" event (just a database enumeration)
      Global.dbHandler.ExecuteCmd("Insert into Event (CallID,EventID,ExpID,EventTypeID,PrevEventTypeID,EventTriggerTime,TimeStamp,ActivityDefinitionID) "
      + "values("+call.callID+","+eventID3+","+Global.experimentID+",3,1,"+myModel.presentTime()+","+Global.Timestamp(myModel.presentTime().getTimeAsDouble())+","+"'EQ'" +")");
         }
   }  
   /**
    * This eventRoutine() describes what happens when a call is being received
    *
    */
   public void eventRoutine(Call call) {
      //Incrementing the total received calls count
       myModel.totalReceivedCalls.update();
         //  sendTraceNote("CallQueueLength: "+ myModel.callQueue.length());
       // Inserting incoming call to dispatcher queue
       int callCentre=(int)call.callLocation-10;
       int category=(int)call.serviceCategory-1;
      // System.out.println("callCentre: "+callCentre+"  category:"+category);
       DispatchOrEnqueue(call,(Queue<CallAgent>) myModel.agentQueues[callCentre][category], myModel.getServiceTime(category), (Queue<Call>)myModel.callQueues[callCentre][category]);

//       switch ((int)call.callLocation)
//       {
//    case 1:
//        DispatchOrEnqueue(call, myModel.idleAgentQueue1, myModel.getServiceTime1(), (Queue<Call>) myModel.callQueues[0]);
//        break;
//    case 2:
//        DispatchOrEnqueue(call, myModel.idleAgentQueue2,myModel.getServiceTime2() ,(Queue<Call>)myModel.callQueues[1]);
//        break;
//    case 3:
//        DispatchOrEnqueue(call, myModel.idleAgentQueue3,myModel.getServiceTime3(),(Queue<Call>) myModel.callQueues[2]);
//        break;
//    case 4:
//        DispatchOrEnqueue(call, myModel.idleAgentQueue4,myModel.getServiceTime4() ,(Queue<Call>) myModel.callQueues[3]);
//        break;
//       }
   }
}
