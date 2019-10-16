package com.bhumika.bookapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;

// Received ISBN from Barcode Scanner. Send to GoogleBooks to obtain book information.
class GoogleApiRequest extends AsyncTask<String, Object, Book> {

    private ConnectivityManager mConnectivityManager;
    Context context;
    Book b;
    StringBuffer key;
    private DatabaseReference mRef= FirebaseDatabase.getInstance()
            .getReferenceFromUrl("your_firebase_url_here").child("Books");

    public GoogleApiRequest(Context c) {
        context= c;
    }

    @Override
    protected void onPreExecute() {
        // Check network connection.
        if (isNetworkConnected() == false) {
            // Cancel request.
            Log.i(getClass().getName(), "Not connected to the internet");
            Toast.makeText(context, "No Internet Connection !", Toast.LENGTH_SHORT).show();
            cancel(true);
            return;
        }

        //
        if(!AddBookScan.isEdit) {
            // call to func with firebase code to add book
            key = new StringBuffer(mRef.push().getKey());

        }
        else {
            key = new StringBuffer(MainActivity.clickedBook.getPushKey());
        }
        b = new Book(String.valueOf(AddBookScan.isbnData), String.valueOf(AddBookScan.cnData),
                String.valueOf(AddBookScan.cpData), String.valueOf(AddBookScan.rData),
                String.valueOf(AddBookScan.locData), String.valueOf(AddBookScan.oInfText.getText()),
                String.valueOf(AddBookScan.personId), String.valueOf(key), String.valueOf(Details.isOn));
    }

    @Override
    protected Book doInBackground(String... isbns) {
        // Stop if cancelled
        if (isCancelled()) {
            return null;
        }

        String apiUrlString = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbns[0];
        try {
            HttpURLConnection connection = null;
            // Build Connection.
            try {
                URL url = new URL(apiUrlString);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setReadTimeout(5000); // 5 seconds
                connection.setConnectTimeout(5000); // 5 seconds
                connection.setRequestProperty("key", "your_api_key_here");
            } catch (MalformedURLException e) {
                // Impossible: The only two URLs used in the app are taken from string resources.
                e.printStackTrace();
            } catch (ProtocolException e) {
                // Impossible: "GET" is a perfectly valid request method.
                e.printStackTrace();
            }
            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                Log.w(getClass().getName(), "GoogleBooksAPI request failed. Response Code: " + responseCode);
                connection.disconnect();
                return null;
            }

            // Read data from response.
            StringBuilder builder = new StringBuilder();
            BufferedReader responseReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = responseReader.readLine();
            while (line != null) {
                builder.append(line);
                line = responseReader.readLine();
            }
            String responseString = builder.toString();
            Log.d(getClass().getName(), "Response String: " + responseString);
            JSONObject responseJson = new JSONObject(responseString);
            //
            JSONObject volInfo= responseJson.getJSONArray("items").getJSONObject(0)
                    .getJSONObject("volumeInfo");
            b.setBookName(volInfo.getString("title"));
            Log.d(getClass().getName(),volInfo.getString("title"));
            b.setDescription(volInfo.getString("description"));
            b.setImageUrl(volInfo.getJSONObject("imageLinks").getString("thumbnail"));
            StringBuffer authors= new StringBuffer("");
            for(int i=0; i<volInfo.getJSONArray("authors").length(); ++i)
                authors.append(volInfo.getJSONArray("authors").getString(i));
            b.setAuthor(String.valueOf(authors));
            //
            // Close connection and return response code.
            connection.disconnect();
            return b;
        } catch (SocketTimeoutException e) {
            Log.w(getClass().getName(), "Connection timed out. Returning null");
            Toast.makeText(context, "Connection timeout!\nTry again later", Toast.LENGTH_SHORT).show();
            return null;
        } catch (IOException e) {
            Log.d(getClass().getName(), "IOException when connecting to Google Books API.");
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            Log.d(getClass().getName(), "JSONException when connecting to Google Books API.");
            e.printStackTrace();
            return null;
        }
    }


    protected void onPostExecute(Book b) {
        if (isCancelled()) {
            // Request was cancelled due to no network connection.
            //---showNetworkDialog();
        } else if (b == null) {
            Toast.makeText(context, "Sorry! ISBN not recognized", Toast.LENGTH_SHORT).show();
            Toast.makeText(context, "Use button at top", Toast.LENGTH_SHORT).show();
            //---showSimpleDialog(getResources().getString(R.string.dialog_null_response));
        } else {
            // All went well. Do something with your new JSONObject.
            b.setWasFound("true");
            mRef.child(String.valueOf(key)).setValue(b);
            MainActivity.clickedBook= b;
        }
    }

    protected boolean isNetworkConnected() {

        // Instantiate mConnectivityManager if necessary
        if (mConnectivityManager == null) {
            mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        }
        // Is device connected to the Internet?
        NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }
}
