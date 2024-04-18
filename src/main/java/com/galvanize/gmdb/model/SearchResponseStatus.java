package com.galvanize.gmdb.model;

import java.util.List;

public class SearchResponseStatus {

    private String responseStatusEntCd;
    private String responseStatusCdDesc;
    private String responseDesc;

    private List<StatusDetail> statusDetails;

    public String getResponseStatusEntCd() {
        return responseStatusEntCd;
    }

    public void setResponseStatusEntCd(String responseStatusEntCd) {
        this.responseStatusEntCd = responseStatusEntCd;
    }

    public String getResponseStatusCdDesc() {
        return responseStatusCdDesc;
    }

    public void setResponseStatusCdDesc(String responseStatusCdDesc) {
        this.responseStatusCdDesc = responseStatusCdDesc;
    }

    public String getResponseDesc() {
        return responseDesc;
    }

    public void setResponseDesc(String responseDesc) {
        this.responseDesc = responseDesc;
    }

    public List<StatusDetail> getStatusDetails() {
        return statusDetails;
    }

    public void setStatusDetails(List<StatusDetail> statusDetails) {
        this.statusDetails = statusDetails;
    }
}
