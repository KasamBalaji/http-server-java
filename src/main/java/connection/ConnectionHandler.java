package connection;

import enums.HttpStatusCode;
import model.HttpHeader;
import model.HttpRequest;
import model.HttpResponse;
import service.FileHandler;
import service.HttpParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ConnectionHandler implements Runnable {

    private Socket socket;

    private String directory;

    public ConnectionHandler(Socket clientSocket, String directory) {
        this.socket = clientSocket;
        this.directory = directory;
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
        }
        else if(path.equalsIgnoreCase("/user-agent")){

            HttpHeader userAgentheader = req.getHeaders().stream().filter(x-> x.getKey().equalsIgnoreCase("User-Agent")).findFirst().get();
            String body = userAgentheader.getValues().get(0);
            List<HttpHeader> headers = new ArrayList<>();
            headers.add(new HttpHeader("Content-Type", List.of("text/plain")));
            headers.add(new HttpHeader("Content-Length", List.of(String.valueOf(body.length()))));

            return HttpResponse.builder()
                    .httpStatusCode(HttpStatusCode.OK)
                    .headers(headers)
                    .body(body)
                    .build();

        }
        else if(path.contains("/files/")){
            if(req.getHttpMethod().equalsIgnoreCase("GET"))
                return getFileResponse(req);
            else if(req.getHttpMethod().equalsIgnoreCase("POST"))
                return writeFileResponse(req);
        }

        return HttpResponse.builder()
                .httpStatusCode(HttpStatusCode.NOT_FOUND)
                .build();


    }

    public  HttpResponse writeFileResponse(HttpRequest req){
        String path = req.getPath();
        String fileName = path.replaceFirst("^/files/","");

        try{
            FileHandler.writeFile(directory,fileName,req.getBody());
            List<HttpHeader> headers = new ArrayList<>();
            headers.add(new HttpHeader("Content-Type", List.of("text/plain")));
            headers.add(new HttpHeader("Content-Length",List.of("0")));
            return HttpResponse.builder().httpStatusCode(HttpStatusCode.CREATED)
                    .headers(headers)
                    .build();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    public HttpResponse getFileResponse(HttpRequest req){

        String path = req.getPath();
        String fileName = path.replaceFirst("^/files/","");
        try {
            String body = FileHandler.getFileAsString(directory,fileName);

            List<HttpHeader> headers = new ArrayList<>();
            headers.add(new HttpHeader("Content-Type", List.of("application/octet-stream")));
            headers.add(new HttpHeader("Content-Length", List.of(String.valueOf(body.length()))));

            return HttpResponse.builder()
                    .httpStatusCode(HttpStatusCode.OK)
                    .headers(headers)
                    .body(body)
                    .build();

        } catch (IOException e) {
            System.err.println("Unable to find file");
            return HttpResponse.builder()
                    .httpStatusCode(HttpStatusCode.NOT_FOUND)
                    .build();

        }

    }


    public void writeResponse(HttpResponse response,OutputStream out) throws IOException {

        String responseText = response.getText();

        System.out.println("Response is  : \n"+ responseText);
        byte[] responseBytes = responseText.getBytes(StandardCharsets.UTF_8);

        out.write(responseBytes);
        out.flush();
    }



}
