package com.bhumika.bookapp;

import org.json.JSONObject;

public class Book extends JSONObject {

    String bookName="", author="", description="", imageUrl="";
    String contactPerson="", location="", otherInfo="";
    String contact="", user="";
    String rent="", pushKey="", avlbl="";
    String isbn="";
    String wasFound="";

    public Book() {}

    public Book(String i, String ct, String c, String r,
                String loc, String oInf, String u, String p, String avl)
    {
       // bookName=n;
       // author=a;
        isbn= i;
        contact= ct;
        contactPerson=c;
        rent= r;
        location= loc;
        otherInfo= oInf;
        user= u;
        pushKey= p;
        avlbl=avl;
    }

    public String getWasFound() {
        return wasFound;
    }

    public void setWasFound(String wasFound) {
        this.wasFound = wasFound;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getBookName()
    {
        return bookName;
    }

    public void setBookName(String n)
    {
        this.bookName= n;
    }


    public String getLocation()
    {
        return location;
    }

    public void setLocation(String n)
    {
        this.location= n;
    }

    public String getOtherInfo()
    {
        return otherInfo;
    }

    public void setOtherInfo(String n)
    {
        this.otherInfo= n;
    }

    public String getUser()
    {
        return user;
    }

    public void setUser(String n)
    {
        this.user= n;
    }

    public String getAuthor()
    {
        return author;
    }

    public void setAuthor(String n)
    {
        this.author= n;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getContactPerson()
    {
        return contactPerson;
    }

    public void setContactPerson(String n)
    {
        this.contactPerson= n;
    }

    public String getContact()
    {
        return contact;
    }

    public void setContact(String n)
    {
        this.contact= n;
    }

    public String getRent()
    {
        return rent;
    }

    public void setRent(String n) { this.rent= n; }

    public String getPushKey() { return pushKey; }

    public void setPushKey(String k) { this.pushKey= k; }

    public String getAvlbl() { return avlbl; }

    public void setAvlbl(String k) { this.avlbl= k; }

    @Override
    public String toString()
    {
        return "";
    }
}