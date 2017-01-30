package CallCenter;
import desmoj.core.simulator.*;
/**
 * The CallAgent entity encapsulates all data relevant for a call agent.
 */
public class CallAgent extends Entity {

   /**
    * Constructor of the van carrier entity.
    *
    * @param owner the model this entity belongs to
    * @param showInTrace flag to indicate if this entity shall produce output
    *                    for the trace
    */
    
   public CallAgent(Model owner, String name, boolean showInTrace) {
      super(owner, name, showInTrace);
   }
}