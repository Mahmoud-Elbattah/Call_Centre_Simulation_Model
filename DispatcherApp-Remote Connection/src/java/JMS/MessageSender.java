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

/***********************************************************************
* A simple facade for sending messages in a Point-to-Point domain.
*
* @author T.A. Nguyen
* @version 1.0, Jan 1, 2005
***********************************************************************/
public class MessageSender {
private MessageConnection messageConnection;

/********************************************************************
* Constructor
********************************************************************/
public MessageSender() {
}

/********************************************************************
* Create a MessageConnection
*
* @throws MessageException If the send fails.
********************************************************************/
private MessageConnection getMessageConnection() throws MessageException {
  if (messageConnection == null) {
    messageConnection = new MessageConnection();
  }
  return messageConnection;
}

/********************************************************************
* Attempt to get a QueueSession from the MessageConnection object.
*
* @throws MessageException If the send fails.
********************************************************************/
private Session getSession() throws MessageException {
  if (getMessageConnection() != null) {
    return getMessageConnection().getSession();
  }
  return null;
}

/********************************************************************
* Attempts to send the passed message.
*
* @throws MessageException If the send fails.
********************************************************************/
public void send(Message message) throws MessageException {
  if (message != null) {
    getMessageConnection().send(message);
  }
}

/********************************************************************
* Attempts to establish the connection.
*
* @throws MessageException If the send fails.
********************************************************************/
public void connect() throws MessageException {
  getMessageConnection().getProducer();
}

/********************************************************************
* Attempts to disconnection.
*
* @throws MessageException If the send fails.
********************************************************************/
public void disconnect() throws MessageException {
  if (messageConnection != null) {
    try {
      messageConnection.disconnect();
      messageConnection = null;
    } catch (MessageException e) {
      messageConnection = null;
      e.printStackTrace();
      throw e;
    }
  }
}

/********************************************************************
* Attempts to return a newly constructed TextMessage.
*
* @throws MessageException If the construction fails.
********************************************************************/
public TextMessage createTextMessage() throws MessageException {
  TextMessage message = null;

  try {
    message = getSession().createTextMessage();
  } catch (JMSException e) {
    throw new MessageException("JMS Create Text Message Exception: " + e);
  }
  return message;
}

/********************************************************************
* Attempts to return a newly constructed BytesMessage.
*
* @throws MessageException If the construction fails.
********************************************************************/
public BytesMessage createBytesMessage() throws MessageException {
  BytesMessage message = null;

  try {
    message = getSession().createBytesMessage();
  } catch (JMSException e) {
    throw new MessageException("JMS Create Bytes Exception: " + e);
  }
  return message;
}

/********************************************************************
* Attempts to return a newly constructed ObjectMessage.
*
* @throws MessageException If the construction fails.
********************************************************************/
public ObjectMessage createObjectMessage() throws MessageException {
  ObjectMessage message = null;

  try {
    message = getSession().createObjectMessage();
  } catch (JMSException e) {
    throw new MessageException("JMS Create Object Message Exception: " + e);
  }

  return message;
}

/********************************************************************
* Attempts to return a newly constructed MapMessage.
*
* @throws MessageException If the construction fails.
********************************************************************/
public MapMessage createMapMessage() throws MessageException {
  MapMessage message = null;

  try {
    message = getSession().createMapMessage();
  } catch (JMSException e) {
    throw new MessageException("JMS Create Map Message Exception: " + e);
  }

  return message;
}

/********************************************************************
* Attempts to return a newly constructed StreamMessage.
*
* @throws MessageException If the construction fails.
********************************************************************/
public StreamMessage createStreamMessage() throws MessageException {
  StreamMessage message = null;

  try {
    message = getSession().createStreamMessage();
  } catch (JMSException e) {
    throw new MessageException("JMS Create Stream Message Exception: " + e);
  }

  return message;
}
}
