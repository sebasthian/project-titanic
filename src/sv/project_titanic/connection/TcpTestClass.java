package sv.project_titanic.connection;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;

public class TcpTestClass implements ActionListener {

	TCPClient client1;
	TCPClient client2;
	Thread serverThread;
	
	
	public TcpTestClass() throws UnknownHostException, IOException {
		serverThread = new Thread(new TCPServer(6665), "server thread");
		serverThread.start();

		client1 = new TCPClient();
		client1.addActionListener(this);
		client1.connect("localhost");
		client1.send("Hi I'm Client1 :)");
		
		
		System.out.println("server running...");
		client2 = new TCPClient();
		client2.addActionListener(this);
		client2.connect("localhost");
		client2.send("Hi I'm Client2 :)");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		ClientActionEvent ce = (ClientActionEvent)e;
		if(ce.getRecieveObject() instanceof String) {
			System.out.println("ActionPerfomed: " + (String)ce.getRecieveObject());
		}
		

	}

}
