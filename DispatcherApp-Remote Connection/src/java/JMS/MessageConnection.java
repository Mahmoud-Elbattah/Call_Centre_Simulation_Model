/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package JMS;



//***************************************//
// imports //
//***************************************//
import javax.jms.*;
import javax.naming.*;
import java.util.Hashtable;

/***********************************************************************
* A simple facade common JMS Connection that will encapsulate and handle
* all the jms connection, session, and message transportation.
*
* @author T.A. Nguyen
* @version 1.0, Jan 1, 2005
***********************************************************************/
public class MessageConnection {
 //   tcp://89.101.8.147:61616
    private String factoryName = "org.apache.activemq.jndi.ActiveMQInitialContextFactory";
private String destinationName = "es.uc3m.softlab.cbi4api.basu.event.bpaf";

//private String factoryName = "jms/myConnectionFactory";
//private String destinationName = "jms/myDestination";
private Context context;
private ConnectionFactory connectionFactory;
private Connection connection;
private Session session;
private Destination destination;
private MessageConsumer messageConsumer;
private MessageProducer messageProducer;
private boolean connected = false;

/********************************************************************
* Constructor
********************************************************************/
public MessageConnection() {
}

/********************************************************************
* Attempts to connect to the JMS messaging system.
*
* @throws MessageException If a connect failure occurs.
********************************************************************/
public Context getContext() throws MessageException {
  if (context == null) {
    try {
      //-------------------------------------
      // Initialize JNDI.
      //-------------------------------------
     
      Hashtable properties = new Hashtable(2);
      properties.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
  //  properties.put(Context.PROVIDER_URL, "iiop://89.101.8.147:61616"); //FOR EACH SERVER MUST BE HIS OWN PROVIDER. The iiop provider is for GlassFish.*
   properties.put(Context.PROVIDER_URL, "tcp://89.101.8.147:61616"); //FOR EACH SERVER MUST BE HIS OWN PROVIDER. The iiop provider is for GlassFish.*
   context = new InitialContext(properties);
    
   // context = new InitialContext();
    } catch (NamingException e) {
      disconnect();
      e.printStackTrace();
      throw new MessageException("Naming Exception: " + e);
    }
  }
  return context;
}

/********************************************************************
* Attempts to connect to the JMS messaging system.
*
* @throws MessageException If a connect failure occurs.
********************************************************************/
public MessageConsumer getConsumer() throws MessageException {
  if (messageConsumer == null) {
    try {
    //-------------------------------------
    // Create a message sender.
    //-------------------------------------
      messageConsumer = getSession().createConsumer(getDestination());
      if (!connected) {
        getConnection().start();
        connected = true;
      }
    } catch (JMSException e) {
      disconnect();
      e.printStackTrace();
      throw new MessageException("JMS Create Receiver Exception: " + e);
    }
   }
   return messageConsumer;
}

/********************************************************************
* Attempts to connect to the JMS messaging system.
*
* @throws MessageException If a connect failure occurs.
********************************************************************/
public MessageProducer getProducer() throws MessageException {
  if (messageProducer == null) {
    try {
      //-------------------------------------
      // Create a message sender.
      //-------------------------------------
      messageProducer = getSession().createProducer(getDestination());
      if (!connected) {
        getConnection().start();
        connected = true;
      }
    } catch (JMSException e) {
      disconnect();
      e.printStackTrace();
      throw new MessageException("JMS Create Sender Exception: " + e);
    }
  }
  return messageProducer;
}

/********************************************************************
* Attempts to connect to the JMS messaging system.
*
* @throws MessageException If a connect failure occurs.
********************************************************************/
public ConnectionFactory getConnectionFactory() throws MessageException {
  if (connectionFactory == null) {
    try {
      //------------------------------------------
      // Lookup the connection factory using JNDI.
      //------------------------------------------
       
      connectionFactory = (ConnectionFactory) getContext().lookup(factoryName);
    } catch (NamingException e) {
      disconnect();
      e.printStackTrace();
      throw new MessageException("Naming Lookup Factory Exception: " + e);
    }
  }
  return connectionFactory;
}

/********************************************************************
* Attempts to connect to the JMS messaging system.
*
* @throws MessageException If a connect failure occurs.
********************************************************************/
public Connection getConnection() throws MessageException {
  if (connection == null) {
    try {
      //-------------------------------------
      // Use the connection factory to create
      // a JMS connection.
      //-------------------------------------
      connection = getConnectionFactory().createConnection();
      
    } catch (JMSException e) {
      disconnect();
      e.printStackTrace();
      throw new MessageException("JMS Create Queue Connection Exception: " + e);
    }
   }
   return connection;
}

/********************************************************************
* Attempts to connect to the JMS messaging system.
*
* @throws MessageException If a connect failure occurs.
********************************************************************/
public Session getSession() throws MessageException {
  if (session == null) {
    try {
      //-------------------------------------
      // Use the connection to create a
      // session.
      //-------------------------------------
      session = getConnection().createSession(false, Session.AUTO_ACKNOWLEDGE);
    } catch (JMSException e) {
      disconnect();
      e.printStackTrace();
      throw new MessageException("JMS Create Session Exception: " + e);
    }
  }
  return session;
}

/********************************************************************
* Attempts to connect to the JMS messaging system.
*
* @throws MessageException If a connect failure occurs.
********************************************************************/
public Destination getDestination() throws MessageException {
  if (destination == null) {
    try {
      //-------------------------------------
      // Lookup the destination (queue) using
      // JNDI. Note that the designated
      // destination (destinationName) must be
      // administered within the JMS service
      // environment.
      //-------------------------------------
      destination = (Destination) getContext().lookup(destinationName);
     } catch (NamingException e) {
       disconnect();
       e.printStackTrace();
       throw new MessageException("Naming Lookup Queue Exception: " + e);
     }
   }
   return destination;
}

/********************************************************************
* Attempts to disconnect from the JMS messaging system.
*
* @throws MessageException If a disconnect failure occurs.
********************************************************************/
public void connect() throws MessageException {
  // make sure a connection and a session established.
  getSession();
  if (!connected) {
    try {
      getConnection().start();
      connected = true;
    } catch (JMSException e) {
      e.printStackTrace();
      connected = false;
      disconnect();
      throw new MessageException("JMS Connection Exception: " + e);
    }
  }
}

/********************************************************************
* Attempts to disconnect from the JMS messaging system.
*
* @throws MessageException If a disconnect failure occurs.
********************************************************************/
public void disconnect() throws MessageException {
  if (connection != null) {
    try {
      connection.close();
   } catch (JMSException e) {
      e.printStackTrace();
      throw new MessageException("JMS Exception: " + e);
   }
  }

  connection = null;
  session = null;
  messageProducer = null;
  messageConsumer = null;

  connected = false;
}

/********************************************************************
* Returns true if a connection is currently established.
********************************************************************/
public boolean isConnected() {
   return connected;
}

/********************************************************************
* Returns true if a connection is currently established.
********************************************************************/
public void send(Message message) throws MessageException {
  try {
    getProducer().send(message);
  } catch (JMSException e) {
    e.printStackTrace();
    throw new MessageException("JMS Send Exception: " + e);
  }
}

/********************************************************************
* Attempts to receive a message.
*
* @throws MessageException If the receive fails.
********************************************************************/
public Message receive(int n) throws MessageException {
  Message message = null;

  try {
    message = getConsumer().receive(n);
  } catch (JMSException e) {
    e.printStackTrace();
    throw new MessageException("JMS Receive Exception: " + e);
  }
  return message;
}  
}