package com.example.user.qrcode;

public class Json {
    int id;
    int age;
    int checkid;
    String phone;
    String username;
    String time;

    public Json(int id, int age, int checkid, String phone, String username, String time) {
        this.id = id;
        this.age = age;
        this.checkid = checkid;
        this.phone = phone;
        this.username = username;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public int getAge() {
        return age;
    }

    public int getCheckid() {
        return checkid;
    }

    public String getUsername() {
        return username;
    }

    public String getTime() {
        return time;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setCheckid(int checkid) {
        this.checkid = checkid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
