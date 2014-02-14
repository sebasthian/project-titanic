package sv.project_titanic.connection;

import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class TCPClient
{
	private final ArrayList<ActionListener> listeners = new ArrayList<ActionListener>();
	private Thread runner;
	private Socket clientSocket;
	private ObjectOutputStream toServer;
	private ObjectInputStream fromServer;
	
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
	
	public void connect(String ip) throws UnknownHostException, IOException
	{
		clientSocket = new Socket("localhost", 6665);
		toServer = new ObjectOutputStream(clientSocket.getOutputStream());
		fromServer = new ObjectInputStream(clientSocket.getInputStream());
		
		runner = new Thread(new Runnable(){
			public void run() {
				while(clientSocket.isConnected()) {
					try {
						Object obj = fromServer.readObject();
						notifyListeners()
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
	
	public void send(Object obj)
	{
		try {
			toServer.writeObject(obj);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void addActionListener(ActionListener listener)
	{
		listeners.add(listener);
	}
	
	private void notifyListeners(ClientActionEvent e) 
	{
		int i = 0;
		while(i < listeners.size()) {
			listeners.get(i++).actionPerformed(e);
		}
	}
}
