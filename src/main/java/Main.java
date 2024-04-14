import connection.ConnectionHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Main {
  public static void main(String[] args) {
     try(ServerSocket serverSocket = new ServerSocket(4221)) {
         String directory=null;
         if(args.length>=2) {
             String command= args[0];
             if(command.equalsIgnoreCase("--directory"))
                 directory= args[1];
         }
       serverSocket.setReuseAddress(true);

         while(true){
             Socket clientSocket = serverSocket.accept();// Wait for connection from client.
             System.out.println("accepted new connection");
             Thread t = new Thread(new ConnectionHandler(clientSocket, directory));
             t.start();
         }
     } catch (IOException e) {
       System.out.println("IOException: " + e.getMessage());
     }
  }
}
