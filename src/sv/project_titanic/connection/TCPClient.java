package sv.project_titanic.connection;

import java.io.*;
import java.net.*;

class TCPClient{
	public static void main(String argv[]) throws Exception{
	
		String input;
		String modifiedInput;
		
		BufferedReader userInput = 
			new BufferedReader(new InputStreamReader(System.in));
			
		Socket clientSocket = new Socket("localhost", 6789);
		
		DataOutputStream toServer = 
			new DataOutputStream(clientSocket.getOutputStream());
		
		BufferedReader fromServer = 
			new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			
		input = fromUser.readLine();
		
		toServer.writeBytes(input + '\n');
		
		modifiedInput = fromServer.readLine();
		System.out.println("FROM SERVER: " + modifiedInput);
		clientSocket.close();
	}
}
