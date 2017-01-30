package CallCenter;
import desmoj.core.simulator.*;
import desmoj.core.dist.*;
import java.util.concurrent.TimeUnit;
import desmoj.core.statistic.*;
import java.text.DecimalFormat;
/* 
@author Mahmoud Elbattah 
*/
public class CallCenter extends Model{
  public final double MaxnHoldTime=1;
  public static Count totalReceivedCalls;
  public static Count numberOfHandledCalls;
  public static Count numberOfAbondonedCalls; 
  public static Count numberOfSwitchedCalls; 
    //public static desmoj.core.statistic.Aggregate systemCapacity;
    // public static desmoj.core.statistic.Aggregate systemUtilization;
    
    //public static double sumHandledTime;
    public static double simulationPeriod=1440;
    //Model Parameters
  // protected static int CALL_AGENTS =3;
   protected static int AgentsNo=2;
  // protected static int AGENTS_CATEGORY1 =2;//No. of Agents servig Category1
   // protected static int AGENTS_CATEGORY2 =4;//No. of Agents servig Category2
   // protected static int AGENTS_CATEGORY3 =3;//No. of Agents servig Category3
   // protected static int AGENTS_CATEGORY4 =2;//No. of Agents servig Category4
    
//Random number stream used to draw an arrival time for the incoming call.
   private ContDistExponential inCallArrivalTime;

   /*
    * Random number stream used to draw a handle time for a call.
    * Describes the duration required by the call agent to finish service, 
    * typically measured from the customer's initiation of the call and including 
    * any hold time, talk time and related tasks that follow the transaction.
    */
   private ContDistUniform handleTime1;
   private ContDistUniform handleTime2;
   private ContDistUniform handleTime3;
   private ContDistUniform handleTime4;
  
  private DiscreteDistUniform callLocation;
  private DiscreteDistUniform serviceCategory;
  //A random client to be fetched from database ,according ,
  //and attached with an incoming call
  private DiscreteDistUniform callerID;


   /**
    * A waiting queue object is used to represent the calls being on-hold 
    * Waiting for a call agent to answer 
    */
  // protected Queue<Call> callQueue;
   Queue<?>[][] callQueues = new Queue<?>[18][4];
  // protected Queue<Call> callQueue1;//Category1 Queue
  // protected Queue<Call> callQueue2;//Category2 Queue
  // protected Queue<Call> callQueue3;//Category3 Queue
  // protected Queue<Call> callQueue4;//Category4 Queue
   /**
    * A waiting queue object is used to represent the idle call agents
  */
   // protected Queue<CallAgent> idleAgentQueue;
  
    Queue<?>[][] agentQueues = new Queue<?>[18][4];
  // protected Queue<CallAgent> idleAgentQueue1;//Agents Serving Category1
 //protected Queue<CallAgent> idleAgentQueue2;//Agents Serving Category2
 //protected Queue<CallAgent> idleAgentQueue3;//Agents Serving Category3
 //protected Queue<CallAgent> idleAgentQueue4;//Agents Serving Category4
   /**
    * CallCenterModel constructor.
    *
    * Creates a new CallCenter model via calling
    * the constructor of the superclass.
    *
    * @param owner the model this model is part of (set to null when there is
    *              no such model)
    * @param modelName this model's name
    * @param showInReport flag to indicate if this model shall produce output
    *                     to the report file
    * @param showInTrace flag to indicate if this model shall produce output
    *                    to the trace file
    */
     public CallCenter(Model owner, String modelName, boolean showInReport, 
                                                       boolean showInTrace) {
      super(owner, modelName, showInReport, showInTrace);   
   }
  /**
    * Returns a description of the model to be used in the report.
    * @return model description as a string
    */
   public String description() {
      return "This model describes a queueing system located at a "+
                  "call center facility. Incoming calls are received and "+
                  "required to be be handled by call agents. An idle call agent will "+
                  "take the call and handles the call's problem or complain."+
                  "In case all agents are busy, the incoming call is put on-hold.";
   }  
    /**
    * Returns a sample of the random stream used to determine the
    * time needed to handle the call enquiry by the call agent
    */
    public double getServiceTime(int serviceCategory) {
     double value=0;
        switch(serviceCategory)
        {
            case 0:
                 value=handleTime1.sample();
                break;
            case 1:
                value=handleTime2.sample();
                break;
            case 2:
                value=handleTime3.sample();
                break;
            case 3:
                value=handleTime4.sample();
                break;
        }
       // System.out.println(value);
       return value;
    }
 /*
    public double getServiceTime1() {
    double value=handleTime1.sample();
       return value;
   }
  public double getServiceTime2() {
    double value=handleTime2.sample();
       return value;
   }
  public double getServiceTime3() {
    double value=handleTime3.sample();
       return value;
   }
  public double getServiceTime4() {
    double value=handleTime4.sample();
       return value;
   }*/
    /**
    * Returns a sample of the random stream used to determine
    * the next incoming call arrival time.
    */
   public double getCallArrivalTime() {
      return inCallArrivalTime.sample();
   }
   public long getServiceCategory() {
       return serviceCategory.sample();
   }
   public long getCallLocation() {
       return callLocation.sample();
   }
   public long getCallerID(){
        return callerID.sample();
   }
      /**
    * Activates dynamic model components (events).
    *
    * This method is used to place all events or processes on the
    * internal event list of the simulator which are necessary to start
    * the simulation.
    *
    * In this case, the call center generator event will have to be
    * created and scheduled for the start time of the simulation.
    */

   public void doInitialSchedules() { 
       // create the CallGeneratorEvent
   CallGeneratorEvent callGenerator =
            new CallGeneratorEvent(this, "Call Generator", true);

   // schedule for start of simulation
   callGenerator.schedule(new TimeSpan(0)); 
   }
   
    /**
    * Initializes static model components like distributions and queues.
    */
   public void CreateAgentQueue(int agentCount, Queue<CallAgent> queue){
         CallAgent agent;
       for (int i = 0; i < agentCount ; i++) {
      // create a new call agent
      agent = new CallAgent(this, "Call Agent", true);
      queue.insert(agent);
   }
       
   }
   public void init() {
       
   // initialise the serviceTimeStream
   // Parameters:
   // this                = belongs to this model
   // "ServiceTimeStream" = the name of the stream
   // 3.0                 = minimum time in minutes to handle a call
   // 5.0                 = maximum time in minutes to handle a call
   // true                = show in report?
   // false               = show in trace?
    handleTime1= new ContDistUniform(this, "HandleTimeStream",
                                    6.0, 12.0, true, false);   
   handleTime2= new ContDistUniform(this, "HandleTimeStream",
                                    5.0, 10.0, true, false);   
   handleTime3= new ContDistUniform(this, "HandleTimeStream",
                                    7.0, 14.0, true, false);   
   handleTime4= new ContDistUniform(this, "HandleTimeStream",
                                    8.0, 15.0, true, false);  
  
   // initalise the CallArrivalTimeStream
   inCallArrivalTime= new ContDistExponential(this, "IncallArrivalTimeStream",
                                                   0.5, true, false);
   // necessary because an inter-arrival time can not be negative, but
   // a sample of an exponential distribution can...
   inCallArrivalTime.setNonNegative(true);
  ///
   serviceCategory=new DiscreteDistUniform(this,"Service Category", 1, 4, false, false);
   //The call centres ids in the database range from 10-27
   callLocation=new DiscreteDistUniform(this,"Call Location", 10, 27, false, false);
 
//CallerID randomly distributed from 1-500, simply because we have 2000 client records in databse
   callerID=new DiscreteDistUniform(this,"CallerID", 1, 2000, false, false);

   // initalise the call queues
   for(int i=0;i<18;i++)
   {
   for(int j=0;j<4;j++)
   {
       callQueues[i][j] = new Queue<Call>(this, "Call Centre"+i +" Queue Catrgory"+j, true, true);
     agentQueues[i][j] = new Queue<CallAgent>(this, "Agent-Call Centre"+i+" Queue"+j, true, true);
CreateAgentQueue(AgentsNo, (Queue<CallAgent>) agentQueues[i][j]);
   }
   }
//   callQueues[0] = new Queue<Call>(this, "Call Queue Catrgory1", true, true);
//   callQueues[1] = new Queue<Call>(this, "Call Queue Catrgory2", true, true);
//   callQueues[2] = new Queue<Call>(this, "Call Queue Catrgory3", true, true);
//   callQueues[3] = new Queue<Call>(this, "Call Queue Catrgory4", true, true);
   
   // initalise the idle agents queues
   //idleAgentQueue1 = new Queue<CallAgent>(this, "Idle Agent Queue1", true, true);
  // idleAgentQueue2 = new Queue<CallAgent>(this, "Idle Agent Queue2", true, true);
  // idleAgentQueue3 = new Queue<CallAgent>(this, "Idle Agent Queue3", true, true);
   //idleAgentQueue4 = new Queue<CallAgent>(this, "Idle Agent Queue4", true, true);

   //Place the idle call agents into the idle queue for each category
     //  CreateAgentQueue(AGENTS_CATEGORY1, idleAgentQueue1);
      // CreateAgentQueue(AGENTS_CATEGORY2, idleAgentQueue2);
      // CreateAgentQueue(AGENTS_CATEGORY3, idleAgentQueue3);
      // CreateAgentQueue(AGENTS_CATEGORY4, idleAgentQueue4);

//Initializing counters
   totalReceivedCalls= new Count(this, "Number of Received Calls",true,false);
   numberOfHandledCalls = new Count(this, "Number of Handled Calls",true,false);
   numberOfAbondonedCalls = new Count(this, "Number of Abondoned Calls",true,false);
   numberOfSwitchedCalls = new Count(this, "Number of Switched Calls",true,false);
     //systemCapacity=new Aggregate(this, "System Capacity", true, false);
    //systemUtilization=new Aggregate(this, "System Utilization", true, false);
   }
    public static void main(java.lang.String[] args) {

   //Create model and experiment
   CallCenter model = new CallCenter(null,
                         "Event-Oriented Call Center Model", true, true);
   // null as first parameter because it is the main model and has no mastermodel
   Experiment exp = new Experiment("CallCenterExperiment", 
                         TimeUnit.SECONDS, TimeUnit.MINUTES, null);
   model.connectToExperiment(exp);
   // Set experiment parameters
   exp.setShowProgressBar(true);  // display a progress bar (or not)
   exp.stop(new TimeInstant(simulationPeriod, TimeUnit.MINUTES));   // set end of simulation at 1500 minutes
   exp.tracePeriod(new TimeInstant(0), new TimeInstant(100, TimeUnit.MINUTES));
                                              // set the period of the trace
   exp.debugPeriod(new TimeInstant(0), new TimeInstant(50, TimeUnit.MINUTES));   // and debug output

   Global.RecordExperimentStartTime();
   exp.start();
  
   // --> now the simulation is running until it reaches its end criterion
//   // generate the report (and other output files)
//	DecimalFormat df = new DecimalFormat("###.##");
//
//       // 
//   //Computing average handle time of call   
//   double averageHandleTime=sumHandledTime/numberOfHandledCalls.getValue(); 
// //Computing System Capacity 
//        double  capacity=(double)CALL_AGENTS/averageHandleTime;
//  //Showing capacity in experiment report
//        systemCapacity.update(Math.round(capacity*100.0)/100.0);
//        systemCapacity.setUnit("Call/min");
//      //Computing flow rate in order to get the system utilization
//        double  flowRate=(double)numberOfHandledCalls.getValue()/simulationPeriod;
//         //Showing capacity in experiment report
//        double  utilization=((double)flowRate/capacity)*100;
//        
//        systemUtilization.update(Math.round(utilization*100.0)/100.0);
//       systemUtilization.setUnit("%");
 
  exp.report();

   // stop all threads still alive and close all output files
   exp.finish();
   }
}
