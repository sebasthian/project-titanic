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
		onAccepted();
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
	private void onAccepted()
	{
		try {
			
			Socket clientSocket1 = serverSocket.accept();
			ObjectOutputStream toClient1 = new ObjectOutputStream(clientSocket1.getOutputStream());
			ObjectInputStream fromClient1 = new ObjectInputStream(clientSocket1.getInputStream());
			//toClient1.writeByte(ConnectionMessages.CLIENT_CONNECTED);
			
			//Socket clientSocket2 = serverSocket.accept();
            //DataOutputStream toClient2 = new DataOutputStream(clientSocket2.getOutputStream());
            //BufferedReader fromClient2 = new BufferedReader(new InputStreamReader(clientSocket1.getInputStream()));
			//toClient2.writeByte(ConnectionMessages.CLIENT_CONNECTED);
			
		

			while(!serverSocket.isClosed())
			{
				
				Object obj = fromClient1.readObject();
				if(obj instanceof String) {
					System.out.println("Server: Client says '" + (String)obj + "'" );
					toClient1.writeObject("MESSAGE BACK TO YOU :) : " + obj);
				}
				
				
				//int ch = -1;
				//while(clientSocket1.getInputStream().read() != -1)
				//{
				//	System.out.println("client 1: " + ch);
				//}
				
				/*while(clientSocket2.getInputStream().read() != -1)
				{
					System.out.println("client 2: " + ch);
				}*/
				
				//serverSocket.close();
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
}
