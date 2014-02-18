/**
 * @author Filip
 * @version 1.0.2
 */

package sv.project_titanic.connection;

import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class TCPClient
{
	private final ArrayList<ActionListener> listeners = new ArrayList<ActionListener>();
	private Thread runner;
	private Socket clientSocket = null;
	private ObjectOutputStream toServer = null;
	private ObjectInputStream fromServer = null;
	
	public TCPClient() {	
	}
	
	public void disconnect() {
		try {
			clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Connect to the "relay-server".
	 * This Function should only be called if there is a running server.
	 * @param ip
	 * @return boolean
	 */
	public boolean connect(String ip) 
	{
		try {
			clientSocket = new Socket("localhost", 6665);
			toServer = new ObjectOutputStream(clientSocket.getOutputStream());
			fromServer = new ObjectInputStream(clientSocket.getInputStream());
		} catch (UnknownHostException e1) {
			
			//e1.printStackTrace();
			return false;
		} catch (IOException e1) {
			//e1.printStackTrace();
			return false;
		}

		
		//This runner thread recieves all objects from the server and notifies all observers.
		runner = new Thread(new Runnable(){
			public void run() {
				while(clientSocket.isConnected()) {
					try {
						Object obj = fromServer.readObject();
						notifyListeners(new ClientActionEvent(this,obj,0,""));
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} 
				
			}
		});

		runner.start();
		return true;
	}
	
	
	/**
	 * Sends a Java Object to the reciever. This object could be anything.
	 * i.e [String, int, char, Custom Object]
	 * @param obj
	 * @return boolean
	 */
	public boolean send(Object obj)
	{
		if(clientSocket == null) { return false; }
		if(!clientSocket.isConnected()) {return false;}
		
		try {
			toServer.writeObject(obj);
		} catch (IOException e) {
			//e.printStackTrace();
			return false;
		}
		return true;
		
	}
	
	/**
	 * Add a standard ActionListener to the client.
	 * 
	 * @param listener 
	 * @see ActionListener
	 */
	public void addActionListener(ActionListener listener)
	{
		listeners.add(listener);
	}
	
	
	/**
	 * "Throw" a ClientActionEvent to the listeners.
	 * ClientActionEvent is almost identical to a normal ActionEvent except that you can also attach
	 * an object to the event.
	 * 
	 * @param e
	 * @see ActionEvent
	 */
	private void notifyListeners(ClientActionEvent e) 
	{
		int i = 0;
		while(i < listeners.size()) {
			listeners.get(i++).actionPerformed(e);
		}
	}
}
