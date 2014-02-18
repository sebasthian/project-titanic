/**
 * @author Filip
 * @version 0.1.0
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Connect to a relay-server.
	 * @param ip
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public void connect(String ip) 
	{
		try {
			clientSocket = new Socket("localhost", 6665);
			toServer = new ObjectOutputStream(clientSocket.getOutputStream());
			fromServer = new ObjectInputStream(clientSocket.getInputStream());
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return;
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return;
		}

		
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

		try {
			runner.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		runner.start();
	}
	
	
	/**
	 * Sends a Java Object to the reciever. This object can be anything.
	 * i.e [String, int, char, Custom Object]
	 * @param obj
	 */
	public void send(Object obj)
	{
		if(clientSocket != null)
			if(clientSocket.isConnected()) {
				try {
					toServer.writeObject(obj);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
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
