

package CallCenter;

import static org.apache.commons.math.util.MathUtils.round;




public class Global {
  public static final int experimentID=101;
  private  static int autoEventID=0;
  private static  int autoProcessID=0;
  public static String experimentStartTime;

  public static final  DBHandler dbHandler=new DBHandler("DB_CCSimulation_DSN");

  public static int AutoEventID()
  {
    return ++autoEventID;
}

public static int AutoProcessID()
{
    return ++autoProcessID;
}
public static void RecordExperimentStartTime()
{
 
    dbHandler.ExecuteCmd("Update Experiment set ExpStartTime=CONVERT(VARCHAR(50),GETDATE(),126) Where ExpID="+experimentID);
    experimentStartTime=dbHandler.ReadExpTime();
    //System.out.println(experimentStartTime);
}
public static String Timestamp(double simulationTime)
{
  

  String[] parts = Double.toString(round(simulationTime,3)).split("\\."); 
 String expr= "CONVERT(VARCHAR(50), DATEADD(ss,"+parts[0]+  ", DATEADD(ms,"+parts[1]+  ",'"+Global.experimentStartTime+"')) ,126)";
 //System.out.println("Part1: "+parts[0]);
 //System.out.println("Part2: " +parts[1]);
 return expr;
 
}
}
