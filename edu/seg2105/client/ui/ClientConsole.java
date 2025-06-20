package edu.seg2105.client.ui;
// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import java.util.Scanner;

import edu.seg2105.client.backend.ChatClient;
import edu.seg2105.client.common.*;

/**
 * This class constructs the UI for a chat client.  It implements the
 * chat interface in order to activate the display() method.
 * Warning: Some of the code here is cloned in ServerConsole 
 *
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Dr Timothy C. Lethbridge  
 * @author Dr Robert Lagani&egrave;re
 */
public class ClientConsole implements ChatIF 
{
  //Class variables *************************************************
  
  /**
   * The default port to connect on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Instance variables **********************************************
  
  /**
   * The instance of the client that created this ConsoleChat.
   */
  ChatClient client;
  
  
  
  /**
   * Scanner to read from the console
   */
  Scanner fromConsole; 

  
  //Constructors ****************************************************

  /**
   * Constructs an instance of the ClientConsole UI.
   *
   * @param host The host to connect to.
   * @param port The port to connect on.
   */
  public ClientConsole(String login,String host, int port) 
  {
    try 
    {
      client= new ChatClient(login, host, port, this); 
      
    } 
    catch(IOException exception) 
    {
      System.out.println("Error: Can't setup connection!"
                + " Terminating client.");
      System.exit(1);
    }
    
    // Create scanner object to read from console
    fromConsole = new Scanner(System.in); 
  }

  
  //Instance methods ************************************************
  
  /**
   * This method waits for input from the console.  Once it is 
   * received, it sends it to the client's message handler.
   */
  public void accept() 
  {
    try
    {
      String message;

      while (true) 
      {
        message = fromConsole.nextLine();
        if (message.startsWith("#")) {
        	handleCommand(message);
        }else {        	
			client.handleMessageFromClientUI(message);
        }
      }
    } 
    catch (Exception ex) 
    {
      System.out.println
        ("Unexpected error while reading from console!");
    }
  }

  private void handleCommand(String cmd) {
	    // split into command + optional argument
	    String[] comArgs = cmd.split(" ");
	    String c = comArgs[0];

	    try {
	      switch (c) {
	        case "#quit":
	          // close if open, then exit
	          if (client.isConnected()) {
	        	  client.closeConnection();
	          }
	          System.exit(0);
	          break;

	        case "#logoff":
	          if (client.isConnected()) {
	            client.closeConnection();
	            System.out.println("** Logged off **");
	          } else {
	            System.out.println("Not connected.");
	          }
	          break;

	        case "#sethost":
	          if (client.isConnected()) {
	            System.out.println("Cannot change host while connected.");
	          } else if (comArgs.length == 2) {
	            client.setHost(comArgs[1]);
	            System.out.println("Host set to " + comArgs[1]);
	         }
	        break;

	        case "#setport":
	          if (client.isConnected()) {
	            System.out.println("Cannot change port while connected.");
	          } else if (comArgs.length == 2) {
	            int p = Integer.parseInt(comArgs[1]);
	            client.setPort(p);
	            System.out.println("Port set to " + p);
	          } 
	          break;

	        case "#login":
	          if (client.isConnected()) {
	            System.out.println("Already connected.");
	          } else {
	            client.openConnection();
	            System.out.println("** Connected to server **");
	          }
	          break;

	        case "#gethost":
	          System.out.println("Current host: " + client.getHost());
	          break;

	        case "#getport":
	          System.out.println("Current port: " + client.getPort());
	          break;

	        default:
	          System.out.println("Unknown command: " + c);
	      }
	    } catch (Exception e) {
	    }
	  }
	  /**
   * This method overrides the method in the ChatIF interface.  It
   * displays a message onto the screen.
   *
   * @param message The string to be displayed.
   */
  public void display(String message) 
  {
    System.out.println("> " + message);
  }
  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of the Client UI.
   *
   * @param args[0] The host to connect to.
   */
  public static void main(String[] args) 
  {
    String host = "";
    int port = 5555;
    if (args.length==2 || args.length == 0){
    	System.out.println("ERROR - No login id specified. Connection aborted");
    	System.exit(0);
    }
    String login = args[0];
    try
    {
      host = args[1];
      if (args.length > 2) {
    	  port = Integer.parseInt(args[2]);
      }
    }
    catch(ArrayIndexOutOfBoundsException e)
    {
      host = "localhost";
    }
    ClientConsole chat= new ClientConsole(login, host, port);
    chat.accept();  //Wait for console data
  }
}
//End of ConsoleChat class
