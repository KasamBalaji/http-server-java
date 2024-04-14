package handlers;

import model.HttpRequest;
import model.HttpResponse;

public interface RequestHandler {

    boolean canHandle(HttpRequest request);
    HttpResponse handle(HttpRequest request);
}
