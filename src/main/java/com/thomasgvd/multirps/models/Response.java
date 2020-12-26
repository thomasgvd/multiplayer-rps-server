package com.thomasgvd.multirps.models;

public class Response {

    private int type;
    private String content;
    private boolean respondToOne;
    private boolean respondToAll;
    private boolean successful;

    public Response() {
        successful = false;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isRespondToOne() {
        return respondToOne;
    }

    public void setRespondToOne(boolean respondToOne) {
        this.respondToOne = respondToOne;
    }

    public boolean isRespondToAll() {
        return respondToAll;
    }

    public void setRespondToAll(boolean respondToAll) {
        this.respondToAll = respondToAll;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }
}
