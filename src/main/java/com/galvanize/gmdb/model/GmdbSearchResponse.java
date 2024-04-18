package com.galvanize.gmdb.model;

public class GmdbSearchResponse {


    private Payload payload = null;
    private SearchResponseStatus responseStatus;

    public Payload getPayload() {
        return payload;
    }

    public void setPayload(Payload payload) {
        this.payload = payload;
    }


    public SearchResponseStatus getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(SearchResponseStatus responseStatus) {
        this.responseStatus = responseStatus;
    }
}
