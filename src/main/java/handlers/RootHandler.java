package handlers;

import model.HttpRequest;
import model.HttpResponse;

public class RootHandler implements RequestHandler{
    @Override
    public boolean canHandle(HttpRequest request) {
        if(request.getPath().equalsIgnoreCase("/"))
            return true;
        return false;
    }

    @Override
    public HttpResponse handle(HttpRequest request) {
        return null;
    }
}
