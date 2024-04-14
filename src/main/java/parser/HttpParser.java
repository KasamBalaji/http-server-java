package parser;

import model.HttpHeader;
import model.HttpRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class HttpParser {

    public static HttpRequest parse(InputStream ins) {

        BufferedReader br = new BufferedReader(new InputStreamReader(ins));

        // Parse first Line
        String input;
        List<String> headerLines = new ArrayList<>();
        String startLine;
        try {
            startLine = br.readLine();
            while ((input = br.readLine())!=null) {
                if(input.equalsIgnoreCase(""))
                    break;
                headerLines.add(input);
            }

            HttpRequest request = parseStartLine(startLine);
            List<HttpHeader> headers = parseHeaders(headerLines);
            request.setHeaders(headers);
            return request;
        } catch (IOException | HttpParseException e) {
            throw new RuntimeException(e);
        }

    }

    public static List<HttpHeader> parseHeaders(List<String> headerLines) {

        List<HttpHeader> headers = new ArrayList<>();
        for (String headerLine : headerLines) {
            try {
                String key = headerLine.split(": ")[0];
                String values = headerLine.split(": ")[1];

                List<String> valuesList = List.of(values.split(";"));

                HttpHeader header = new HttpHeader(key, valuesList);
                headers.add(header);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
        return headers;

    }


    public static HttpRequest parseStartLine(String startLine) throws HttpParseException {

        HttpRequest req = new HttpRequest();
        try {
            List<String> parts = List.of(startLine.split(" "));
            req.setHttpMethod(parts.get(0));
            req.setPath(parts.get(1));
            req.setVersion(parts.get(2));
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Unable to parse Start Line");
            throw new HttpParseException("Unable to parse Start Line");
        }
        return req;
    }

    public static class HttpParseException extends Exception {
        public HttpParseException(String message) {
            super(message);
        }

    }


}
