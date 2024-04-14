package model;

import enums.HttpStatusCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import static constants.StringConstants.CRLF;
import static constants.StringConstants.SPACE;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HttpResponse {
    @Builder.Default
    private String version = "HTTP/1.1";
    private HttpStatusCode httpStatusCode;
    private List<HttpHeader> headers;
    private String body;

    public String getText(){
        StringBuilder sb = new StringBuilder();
        sb.append(version);
        sb.append(SPACE);
        sb.append(httpStatusCode.getText());
        sb.append(CRLF);
        if(headers!=null) {
            for (HttpHeader header : headers) {
                sb.append(header.getText());
                sb.append(CRLF);
            }
        }
        sb.append(CRLF);
        if(body!=null){
            sb.append(body);
        }

        return sb.toString();

    }
}
