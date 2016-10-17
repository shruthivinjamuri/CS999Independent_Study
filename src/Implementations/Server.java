package Implementations;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.xml.stream.XMLStreamException;

public class Server {

	public static void main(String[] args) throws IOException, XMLStreamException {
		
//		if(args.length != 1){
//			System.err.println("Usage: java Server <port number>");
//			System.exit(1);
//		}
//		
//		int portNumber = Integer.parseInt(args[0]);
		int portNumber = 2222;
		
		try(
			ServerSocket serverSocket = new ServerSocket(portNumber);
			Socket clientSocket = serverSocket.accept();
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			){
			String inputLine="", outputLine="";
			BufferedReader StdIn = new BufferedReader(new InputStreamReader(System.in));
			
			// Initiate conversation with client
			XMLParser parser = new XMLParser();
			out.println("Start");
			
			while((inputLine = in.readLine()) != null){
				System.out.println("CLient: "+ inputLine);
				outputLine = parser.processInput(inputLine);
				System.out.println("CLient: "+ outputLine);
				out.println(outputLine);
				if(outputLine.equals("Bye.")){
					break;
				}
			}
		} catch (IOException e){
			System.out.println("Exception caught when trying to listen on port "+ portNumber + 
					" listening for a connection.");
			System.out.println(e.getMessage());
		}
		
		

	}

}
