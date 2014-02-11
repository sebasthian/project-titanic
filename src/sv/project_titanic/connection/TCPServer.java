/**
 * @author Filip Lindeby
 * @version 0.0.1
 *
 */


package sv.project_titanic.connection;
import java.io.*;
import java.net.*;

public class TCPServer implements Runnable
{
	//private Thread runner;
	private ServerSocket serverSocket;
	private int port;
	
	public TCPServer(int port)
	{	
		this.port = port;
	}
	
	/**
	 * Overridden thread run interface method.
	 * @see Runnable
	 */
	public void run() 
	{
		listen();
		onAccept();
		System.out.println(Thread.currentThread());
	}
	
	
	/**
	 *  Create a socket that listens to a specific port.
	 *  Custom error handling.
	 */
	private void listen()
	{
		try {
			serverSocket = new ServerSocket(port);
		} catch(IOException e)
		{
			System.out.println("Error in TCPServer class: Failed to create ServerSocket");
			System.out.println("Method: listen()");
			System.out.println("Exception message:");
			System.out.println(e.getMessage());
		}
	}
	
	
	
	/**
	 * This method contains the server main loop.
	 * Relays the communication between the clients.
	 */
	private void onAccept()
	{
		try {
			Socket clientSocket1 = serverSocket.accept();
			//Socket clientSocket2 = serverSocket.accept();
            DataOutputStream toClient1 = new DataOutputStream(clientSocket1.getOutputStream());
			//DataOutputStream toClient2 = new DataOutputStream(clientSocket2.getOutputStream());
            BufferedReader fromClient1 = new BufferedReader(new InputStreamReader(clientSocket1.getInputStream()));
            //BufferedReader fromClient2 = new BufferedReader(new InputStreamReader(clientSocket1.getInputStream()));
			
            toClient1.writeByte(1);
			//toClient2.writeByte(1);
            
            System.out.println("client connected!");
			while(!serverSocket.isClosed())
			{
				//do stuff.
				//serverSocket.close();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
