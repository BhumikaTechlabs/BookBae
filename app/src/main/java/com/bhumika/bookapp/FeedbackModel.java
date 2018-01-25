package com.bhumika.bookapp;

/**
 * Created by Admin on 22/12/2017.
 */

public class FeedbackModel
{
    String feedback, user, acct;

    public FeedbackModel(String f, String u, String a)
    {
        feedback= f; user= u; acct= a;
    }

    public String getFeedback()
    {
        return feedback;
    }

    public String getAcct()
    {
        return acct;
    }

    public String getUser()
    {
        return user;
    }

    public void setFeedback(String u)
    {
        this.feedback= u;
    }

    public void setAcct(String u)
    {
        this.acct= u;
    }

    public void setUser(String u)
    {
        this.user= u;
    }
}
