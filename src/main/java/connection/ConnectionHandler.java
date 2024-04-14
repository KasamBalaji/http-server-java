package connection;

import enums.HttpStatusCode;
import model.HttpHeader;
import model.HttpRequest;
import model.HttpResponse;
import parser.HttpParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ConnectionHandler implements Runnable {

    private Socket socket;

    public ConnectionHandler(Socket clientSocket) {
        this.socket = clientSocket;
    }


    @Override
    public void run() {
        try {
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();
            HttpRequest httpRequest = HttpParser.parse(in);
            System.out.println(httpRequest.toString());
            HttpResponse response = response(httpRequest);
            writeResponse(response,out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public HttpResponse response(HttpRequest req) {
        String path = req.getPath();


        if (path.equalsIgnoreCase("/")) {
            List<HttpHeader> headers = new ArrayList<>();
            headers.add(new HttpHeader("Content-Type", List.of("text/plain")));
            headers.add(new HttpHeader("Content-Length",List.of("0")));
            return HttpResponse.builder().httpStatusCode(HttpStatusCode.OK)
                    .headers(headers)
                    .build();
        } else if (path.startsWith("/echo/")) {
            int idx = path.indexOf("/echo/");
            String echoText = path.substring(6);

            List<HttpHeader> headers = new ArrayList<>();
            headers.add(new HttpHeader("Content-Type", List.of("text/plain")));
            headers.add(new HttpHeader("Content-Length", List.of(String.valueOf(echoText.length()))));

            return HttpResponse.builder()
                    .httpStatusCode(HttpStatusCode.OK)
                    .headers(headers)
                    .body(echoText)
                    .build();
        } else {
            return HttpResponse.builder()
                    .httpStatusCode(HttpStatusCode.NOT_FOUND)
                    .build();
        }

    }

    public void writeResponse(HttpResponse response,OutputStream out) throws IOException {

        String responseText = response.getText();

        byte[] responseBytes = responseText.getBytes(StandardCharsets.UTF_8);

        out.write(responseBytes);
        out.flush();
    }



}
