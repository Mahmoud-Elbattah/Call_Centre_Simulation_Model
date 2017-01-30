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
* A simple facade for receiving messages from a PTP domain.
*
* @author T.A. Nguyen
* @version 1.0, Jan 1, 2005
***********************************************************************/
public class MessageReceiver {

private MessageConnection messageConnection;

/********************************************************************
* Constructor
********************************************************************/
public MessageReceiver() {
}

private MessageConnection getMessageConnection() throws MessageException {
  if (messageConnection == null) {
    messageConnection = new MessageConnection();
  }
  return messageConnection;
}

/********************************************************************
* Attempts to receive a message.
*
* @throws MessageException If the receive fails.
********************************************************************/
public Message receive() throws MessageException {
  return getMessageConnection().receive(1);
}

public void connect() throws MessageException {
  getMessageConnection().getConsumer();
}

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
}
