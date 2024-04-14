import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Main {
  public static void main(String[] args) {
     try(ServerSocket serverSocket = new ServerSocket(4221)) {
       serverSocket.setReuseAddress(true);
       Socket clientSocket = serverSocket.accept(); // Wait for connection from client.
         OutputStream out = clientSocket.getOutputStream();
         String response = "HTTP/1.1 200 OK\r\n\r\n";
         out.write(response.getBytes(StandardCharsets.UTF_8));

       System.out.println("accepted new connection");
     } catch (IOException e) {
       System.out.println("IOException: " + e.getMessage());
     }
  }
}
