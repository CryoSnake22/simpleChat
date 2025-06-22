package edu.seg2105.edu.server.backend;
// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 


import java.io.IOException;

import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Constructors ****************************************************
  private String currLogin;
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port) 
  {
    super(port);
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient
    (Object msg, ConnectionToClient client)
  {
	String[] spl = msg.toString().split(" ");
	this.currLogin = (String) client.getInfo("login");
	
	if (currLogin == null) {
		if (spl[0].equals("#login")) {
			String loginId = spl[1].trim();
			client.setInfo("login", loginId);
			try {
				System.out.println("Message received: " + msg + " from " + currLogin);
				client.sendToClient(loginId+" has logged on.");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				client.sendToClient("Error: must login first, terminating connection");
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return;
		
	} else {	
		if (spl[0].equals("#login")) {		
			try {
				client.sendToClient("Error: user already logged in, terminating connection");
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		} 	
	}
	System.out.println("Message received: " + msg + " from " + currLogin);
	this.sendToAllClients(currLogin + "> "+msg);
  }
    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
  }
  
	@Override
	protected void clientConnected(ConnectionToClient client) {
	  super.clientConnected(client);
	  System.out.println(">> Client connected: "
		  + client.getInetAddress().getHostAddress());
	}
	@Override
	  protected void clientDisconnected(ConnectionToClient client) {
	    super.clientDisconnected(client);
	    System.out.println("<< Client disconnected: "
	        + client.getInetAddress().getHostAddress());
	 }
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }
  
  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
  public static void main(String[] args) 
  {
    int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }
	
    EchoServer sv = new EchoServer(port);
    try 
    {
      sv.listen(); //Start listening for connections
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }
}
//End of EchoServer class
