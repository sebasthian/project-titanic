package sv.project_titanic.connection;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;

public class TcpTestClass implements ActionListener {

	TCPClient client;	
	Thread serverThread;
	
	
	public TcpTestClass() throws UnknownHostException, IOException {
		serverThread = new Thread(new TCPServer(6665), "server thread");
		serverThread.start();

		client = new TCPClient();
		client.connect("localhost");
		client.send("[MESSAGE]");
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {

	}

}
