package com.bhumika.bookapp;

import java.util.Map;

import retrofit2.http.QueryMap;

public interface GoogleBooksApi {

    //@RequestLine("GET /books/v1/volumes")
    Results findBookByISBN(@QueryMap Map<String, Object> queryParameters);

}