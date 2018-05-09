package com.arimdor.sharednotes.repository.model;

public class User {
    private String token;
    private String nickname;

    public User() {
    }

    public User(String token, String nickname) {
        this.token = token;
        this.nickname = nickname;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
