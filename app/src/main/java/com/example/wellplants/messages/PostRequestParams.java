package com.example.wellplants.messages;

public class PostRequestParams {
    private String url;
    private String stringJsonBody;

    public PostRequestParams(String url, String stringJsonBody) {
        this.url = url;
        this.stringJsonBody = stringJsonBody;
    }

    public String getUrl() {
        return url;
    }

    public String getStringJsonBody() {
        return stringJsonBody;
    }
}
