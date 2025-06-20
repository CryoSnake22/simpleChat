package edu.seg2105.edu.server.backend;
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
public class ServerConsole implements ChatIF 
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
  EchoServer server;
  
  
  
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
  public ServerConsole(EchoServer ser) 
  {
    
	this.server = ser;
    // Create scanner object to read from console
    this.fromConsole = new Scanner(System.in); 
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
        	String msg = "SERVER MSG> " + message;
            display(msg);
            if (server.getNumberOfClients() > 0) {
                server.sendToAllClients(msg);
            } else {
                display("(no clients connected)");
            }	
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
	          server.stopListening();
	          server.close();;
	          System.exit(0);
	          break;

	        case "#stop":
	        	server.stopListening();
	        	display("Server no longer listening for connections");
	          break;

	        case "#close":
	        	server.stopListening();
	        	server.close();
	        	display("Server closed");
	        break;

	        case "#setport":
	          if (server.isListening()) {
	            System.out.println("Cannot change port while connected.");
	          } else if (comArgs.length == 2) {
	            int p = Integer.parseInt(comArgs[1]);
	            server.setPort(p);
	            System.out.println("Port set to " + p);
	          } 
	          break;

	        case "#start":
	          if (server.isListening()) {
	            System.out.println("Already connected.");
	          } else {
	        	  server.listen();
	          }
	          break;


	        case "#getport":
	          System.out.println("Current port: " + server.getPort());
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
    int port;

    try
    {
      host = args[0];
      if (args.length > 1) {
    	  port = Integer.parseInt(args[1]);
      }
    }
    catch(ArrayIndexOutOfBoundsException e)
    {
      host = "localhost";
    }
    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }
    EchoServer server = new EchoServer(port);
    try {
    	server.listen();
    } catch (Exception e) {

    }
    ServerConsole console= new ServerConsole(server);
    console.accept();  //Wait for console data
  }
}
//End of ConsoleChat class
