import connection.ConnectionHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Main {
  public static void main(String[] args) {
     try(ServerSocket serverSocket = new ServerSocket(4221)) {
       serverSocket.setReuseAddress(true);

         while(true){
             Socket clientSocket = serverSocket.accept();// Wait for connection from client.
             System.out.println("accepted new connection");
             Thread t = new Thread(new ConnectionHandler(clientSocket));
             t.start();
         }
     } catch (IOException e) {
       System.out.println("IOException: " + e.getMessage());
     }
  }
}
