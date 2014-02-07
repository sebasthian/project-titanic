package sv.project_titanic.connection;

import java.io.*;
import java.net.*;

class TCPServer{
   public static void main(String argv[]) throws Exception{
         
		 String clientSentence;
         ServerSocket welcomeSocket = new ServerSocket(6789);

         while(true){
            Socket connectionSocket = welcomeSocket.accept();
            
			BufferedReader fromClient =
               new BufferedReader(
				new InputStreamReader(connectionSocket.getInputStream()));
				
            DataOutputStream toClient = 
				new DataOutputStream(connectionSocket.getOutputStream());
            
			clientSentence = fromClient.readLine();
			
            System.out.println("Received: " + clientSentence);
            toClient.writeBytes(clientSentence);
        }
	}
}
