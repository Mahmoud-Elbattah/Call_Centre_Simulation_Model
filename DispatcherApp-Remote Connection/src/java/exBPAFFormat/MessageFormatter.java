
package exBPAFFormat;

import EventsRetrieval.*;
import java.io.*;

import org.w3c.dom.*;

import javax.xml.parsers.*;

import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

public class MessageFormatter {

    
    public String CreateXmlMsg(Event newEvent) {
       String xmlString="";
        try {

            //Creating an empty XML Document
            DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            //Creating the XML tree

            //create the root element and add it to the document
            Element node1 = doc.createElement("ns2:Event");
                 //Adding "EventID" attribute
            node1.setAttribute("EventID",Integer.toString(newEvent.eventID));
             //Adding ServerID attribute
           node1.setAttribute("ServerID",newEvent.callCenterID);
           
           //Adding ProcessDefinitionID attribute
           node1.setAttribute("ProcessDefinitionID","IC");
          //Adding ProcessName attribute
           node1.setAttribute("ProcessName",newEvent.activityName);
           //Adding Timestamp attribute
           node1.setAttribute("Timestamp",newEvent.timeStamp);
            //Adding xmlns:ns2 attribute
           node1.setAttribute("xmlns:ns2","http://www.uc3m.es/softlab/basu/event");
           doc.appendChild(node1);
               
            //Adding 'EventDetails' child element
            Element node2 = doc.createElement("EventDetails");
         if(!newEvent.previousState.equals(newEvent.eventStatus))
            node2.setAttribute("PreviousState", newEvent.previousState);
           
         node2.setAttribute("CurrentState", newEvent.eventStatus);
    
            node1.appendChild(node2);
         
//Adding 'correlation' child element
         
             Element node3 = doc.createElement("Correlation");
             Element node4 = doc.createElement("CorrelationData");

          
            //Creating coorelation element, 'CallID'
            Element node5 = doc.createElement("CorrelationElement");
            node5.setAttribute("key","CallID");
           node5.setAttribute("value",Integer.toString(newEvent.callID));
            //Adding to the 'corrDataElement' element
            node4.appendChild(node5);
            node3.appendChild(node4);
            node1.appendChild(node3);

          //Adding the payload emlements
           //Adding payload "Country"
            Element node6 = doc.createElement("Payload");
            node6.setAttribute("key", "Country");
            node6.setAttribute("value", newEvent.location);
            node1.appendChild(node6);

             
            //Adding payload "Category"
           Element node7 = doc.createElement("Payload");
            node7.setAttribute("key", "Category");
            node7.setAttribute("value", newEvent.category);
            node1.appendChild(node7);
            
            //Adding payload "CustomerID"
            Element node8 = doc.createElement("Payload");
            node8.setAttribute("key", "CustomerID");
            node8.setAttribute("value",Integer.toString( newEvent.clientID));
            node1.appendChild(node8);
            
         

            
            //set up a transformer
            TransformerFactory transfac = TransformerFactory.newInstance();
            Transformer trans = transfac.newTransformer();
            trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            trans.setOutputProperty(OutputKeys.INDENT, "yes");

            //create string from xml tree
            StringWriter sw = new StringWriter();
            StreamResult result = new StreamResult(sw);
            DOMSource source = new DOMSource(doc);
            trans.transform(source, result);
            xmlString = sw.toString();         
        }
        catch (Exception e) {
            System.out.println(e);
        }
         return xmlString;
    }
}
