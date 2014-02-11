import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import sv.project_titanic.connection.*;

public class main
{
	public static void main(String argv[])
	{
		Thread thread1 = new Thread(new TCPServer(5432), "server thread");
		
		thread1.start();
		
		try {
			
			Socket clientSocket = new Socket("localhost", 5432);
			BufferedReader fromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			int serverMessage = fromServer.read();
			System.out.println(serverMessage);
			
			clientSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
