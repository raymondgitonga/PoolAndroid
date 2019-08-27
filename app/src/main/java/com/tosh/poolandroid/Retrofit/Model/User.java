package com.tosh.poolandroid.Retrofit.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {


    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("phone")
    @Expose
    private String phone;

    public User() {
    }

    public String getUserName() {
        return name;
    }

    public void setUserName(String name) {
        this.name = name;
    }

    public String getUserEmail() {
        return email;
    }

    public void setUserEmail(String email) {
        this.email = email;
    }

    public String getUserPhone() {
        return phone;
    }

    public void setUserPhone(String phone) {
        this.phone = phone;
    }

}
