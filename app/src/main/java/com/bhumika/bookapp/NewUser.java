package com.bhumika.bookapp;

/**
 * Created by Admin on 11/01/2018.
 */

public class NewUser {
    String email="", name="", googleId="", pushKey="", uuid="";

    public NewUser() {}

    public NewUser(String e, String g, String n,
                   String pk, String u)
    {
        this.email= e;
        this.googleId= g;
        this.name= n;
        this.pushKey= pk;
        this.uuid= u;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String n)
    {
        this.email= n;
    }

    public String getGoogleId()
    {
        return googleId;
    }

    public void setGoogleId(String n)
    {
        this.googleId= n;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String n)
    {
        this.name= n;
    }

    public String getPushKey()
    {
        return pushKey;
    }

    public void setPushKey(String n)
    {
        this.pushKey= n;
    }

    public String getUuid()
    {
        return uuid;
    }

    public void setUuid(String n)
    {
        this.uuid= n;
    }

    @Override
    public String toString()
    {
        return "";
    }

}
