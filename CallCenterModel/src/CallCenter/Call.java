package CallCenter;
import desmoj.core.simulator.*;
public class Call extends Entity {
    /**
    * Constructor of the Call entity.
    * @param owner the model this entity belongs to
    * @param showInTrace flag to indicate if this entity shall produce output
    *                    for the trace
    */
   public   long callID;
   public  long callerID;
   public   double startTime;
   public long serviceCategory;
   public long callLocation;
   public  boolean onHold=false;
   public int backupCenter;
   public String processID;
   //Sets to true in case of switching a call to another call centre in case of unbalancing
   public boolean switchedCall=false;
   public Call(Model owner, String name, boolean showInTrace) {
      super(owner, name, showInTrace);
      //Setting the unique Call ID
      callID=  this.getIdentNumber(); 
     
      CallCenter model =(CallCenter)owner;
      //
      callerID= model.getCallerID();
      
      //Recording the call arrival time
      startTime= owner.presentTime().getTimeAsDouble();
     
      //Setting the call service category, which reflects the call center 
     //to which the call will be forwarded
      serviceCategory=model.getServiceCategory();
      callLocation=model.getCallLocation();
      //Setting the backup call centre according to the call location
      backupCenter=SetBackupCenter(callLocation);
   }
   private int SetBackupCenter(long location)
   {
       int backupCentreID=0;
       switch ((int)location)
       {
           case 10:
           backupCentreID= 12;
           
           case 11:
           backupCentreID= 14; 
           
           case 12:
           backupCentreID= 10;
           
           case 13:
           backupCentreID= 20; 
           
           case 14:
           backupCentreID= 15; 
           
           case 15:
           backupCentreID= 16;
           
           case 16:
           backupCentreID= 17; 
           
           case 17:
           backupCentreID= 16; 
               
           case 18:
           backupCentreID= 21; 
           
           case 19:
           backupCentreID= 13; 
                      
           case 20:
           backupCentreID= 22; 
                             
           case 21:
           backupCentreID= 18; 
                         
           case 22:
           backupCentreID= 23; 
                                   
           case 23:
           backupCentreID= 22; 
                                             
           case 24:
           backupCentreID= 25; 
                                                       
           case 25:
           backupCentreID= 24;
               
           case 26:
           backupCentreID= 27; 
           
           case 27:
           backupCentreID= 26; 
       }
       return backupCentreID;
   }
}
           
