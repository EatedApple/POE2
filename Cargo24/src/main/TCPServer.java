package main;


import java.io.*;
import java.net.*;
import java.util.Date;
import java.util.function.Consumer;
import java.net.ServerSocket;
import java.net.Socket;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class TCPServer extends Thread {
	private DatagramSocket socket;
	private Integer port = 8000;
	private String msg;
	public Consumer<String> callback;

	public void setCallback(Consumer<String> callback) {
		this.callback = callback;
	}
	
	public void run() {
		while (true) {
			try (ServerSocket serverSocket = new ServerSocket(port)) {
				System.out.println("Server is listening on port " + port);
	            while (true) {
	                Socket socket = serverSocket.accept();
	 
	                System.out.println("[ "+socket.getInetAddress()+" ] client connected");
	                OutputStream output = socket.getOutputStream();
	                PrintWriter writer = new PrintWriter(output, true);
	                writer.println(new Date().toString());

	                InputStream input = socket.getInputStream();
	                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
	                this.callback.accept(reader.readLine());
	            }
				
				 //JSONParser parser = new JSONParser();
				 //Object obj = parser.parse("");
//				 JSONObject jsonObj = (JSONObject) obj;
//
//				 String la = (String) jsonObj.get("load_addr");
//				 String aa = (String) jsonObj.get("alight_addr"); 
				 
//				 System.out.println(la + " / " + aa);
				 
			} catch (IOException ex) {
				System.out.println("Server exception: " + ex.getMessage());
				ex.printStackTrace();
				//socket.close();
			}
		}
	}
}
