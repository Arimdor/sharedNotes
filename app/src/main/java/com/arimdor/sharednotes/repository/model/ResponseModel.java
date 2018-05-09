package com.arimdor.sharednotes.repository.model;

public class ResponseModel<E> {

    private int responseID;
    private E data;
    private String message;

    public ResponseModel() {
    }

    public ResponseModel(int responseID, E data, String message) {
        this.responseID = responseID;
        this.data = data;
        this.message = message;
    }

    public int getResponseID() {
        return responseID;
    }

    public void setResponseID(int responseID) {
        this.responseID = responseID;
    }

    public E getData() {
        return data;
    }

    public void setData(E data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
