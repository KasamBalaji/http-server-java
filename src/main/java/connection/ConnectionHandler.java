package connection;

import model.HttpRequest;
import parser.HttpParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ConnectionHandler implements  Runnable{

    private Socket socket;

    public ConnectionHandler(Socket clientSocket){
       this.socket=clientSocket;
    }


    @Override
    public void run() {
        try{
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();
            HttpRequest httpRequest = HttpParser.parse(in);
            System.out.println(httpRequest.toString());
            byte[] response =response(httpRequest);
            out.write(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        finally {
            try {
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public byte[] response(HttpRequest req){
        if(req.getPath().equals("/")){
            String response = "HTTP/1.1 200 OK\r\n\r\n";
            return  response.getBytes(StandardCharsets.UTF_8);
        }
        else{
            String response = "HTTP/1.1 404 Not Found\r\n\r\n";
            return  response.getBytes(StandardCharsets.UTF_8);
        }
    }


}
