package enums;

import constants.StringConstants;
import lombok.Getter;

public enum HttpStatusCode {

    OK("200", "OK"),
    NOT_FOUND("404", "Not Found"),
    CREATED("201","Created");

    @Getter
    public final String statusCode;

    public final String statusText;


    HttpStatusCode(String statusCode, String statusText) {
        this.statusCode = statusCode;
        this.statusText = statusText;
    }

    public String getText() {
        return statusCode +
                StringConstants.SPACE +
                statusText;
    }


}
