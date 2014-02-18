/**
 * @author Filip Lindeby
 * @version 1.0.2
 *
 */

package sv.project_titanic.connection;
import java.io.*;
import java.net.*;

public class TCPServer implements Runnable
{
	
	//TODO små arrayer.
	private Thread client1Runner;
	private Thread client2Runner;
	ObjectOutputStream toClient1;
	ObjectOutputStream toClient2;
	ObjectInputStream fromClient1;
	ObjectInputStream fromClient2;
	Socket clientSocket1;
	Socket clientSocket2;
	
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
			clientSocket1 = serverSocket.accept();
			toClient1 = new ObjectOutputStream(clientSocket1.getOutputStream());
			fromClient1 = new ObjectInputStream(clientSocket1.getInputStream());
			toClient1.writeObject("Connected to server: you are client 1 :)");
			
			clientSocket2 = serverSocket.accept();
	        toClient2 = new ObjectOutputStream(clientSocket2.getOutputStream());
	        fromClient2 = new ObjectInputStream(clientSocket2.getInputStream());
	        
	        
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		

		//This runner thread relays objects sent from client1 to client2.
		client1Runner = new Thread(new Runnable(){
			public void run() {
				while(!serverSocket.isClosed() && clientSocket1.isConnected() && clientSocket2.isConnected()) {
					try {
						Object obj = fromClient1.readObject();
						toClient2.writeObject(obj);
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					
				}
				
			}
			
		});
		
		
		//This runner thread relays objects sent from client2 to client1.
		client2Runner = new Thread(new Runnable(){
			public void run() {
				while(!serverSocket.isClosed() && clientSocket1.isConnected() && clientSocket2.isConnected()) {
					try {
						Object obj = fromClient2.readObject();
						toClient1.writeObject(obj);
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					
				}
			}
			
		});
		client1Runner.start();
		client2Runner.start();
	
	}
	
}
