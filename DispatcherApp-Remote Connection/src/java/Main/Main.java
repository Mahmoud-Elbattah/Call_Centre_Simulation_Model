/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Main;

import EventsRetrieval.Event;
import EventsRetrieval.EventReader;
import JMS.MessageException;
import JMS.MessageReceiver;
import JMS.MessageSender;
import exBPAFFormat.MessageFormatter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

/***********************************************************************
* This class provides a test application for the MessageSender and
* MessageReceiver classes.
*
* @author T.A. Nguyen
* @version 1.0, Jan 1, 2005
***********************************************************************/
public class Main {

/********************************************************************
* Application main entry point.
********************************************************************/
public static void main(String[] args) {
    int clockTicker=0;
int stopTime=1450;
int count=0;
    //Object used to format the message with the required exBPAFFormat
 MessageFormatter msgFormatter=new MessageFormatter();
//Object that reads the events from the database, and performs
//Conevrts the retrieved records into  business objects of type 'Event'
 EventReader reader=new EventReader("DB_CCSimulation_DSN");

    try {
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://89.101.8.147:61616");
            Connection connection = connectionFactory.createConnection();
            //connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue("es.uc3m.softlab.cbi4api.basu.event.bpaf");
            MessageProducer producer = session.createProducer(destination);

           
        for(clockTicker=0;clockTicker<stopTime;clockTicker++)
        {
            //Arralist of 'Event' objects according to that retrieved from the database
            ArrayList<Event> eventsList=reader.ReadEvents(clockTicker);
           // System.out.println(eventsList.size());
            for(int i=0;i<=eventsList.size()-1;i++)
             {
            //Generating the exBPAFFormat messages
             String xmlMsg= msgFormatter.CreateXmlMsg(eventsList.get(i));
       
             // Create a JMS message
           ObjectMessage message = session.createObjectMessage(xmlMsg);
            producer.send(message);
            //System.out.println(xmlMsg);
            System.out.println("Sending message no. "+count);

             //Recording the success of event transmitting to JMS queue
              reader.FlagSuccess(eventsList.get(i).eventID);
             count++;
            Thread.sleep(500);
             }
        }
            
         producer.close();
         session.close();
         System.out.println("Done: no. of events sent:"+count);
    }
    
    catch(Exception ex) {
            ex.printStackTrace();
        }


} 
    
//    catch (JMSException jmsEx) {
//    System.out.println("JMS Exception: " + jmsEx);
//  } 
//    catch (MessageException msgEx) {
//    System.out.println("Message Exception: " + msgEx);
//  }
//    catch (InterruptedException ex) {
//   }
//}
}