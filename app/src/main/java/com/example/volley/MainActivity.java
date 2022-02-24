package com.example.volley;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    Handler mainHandler = new Handler();
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView uname = findViewById(R.id.uname);
        TextView username = findViewById(R.id.username);
        TextView remains = findViewById(R.id.remains);
        TextView userid = findViewById(R.id.userid);
        TextView email = findViewById(R.id.email);
        TextView phone = findViewById(R.id.phone);
        TextView gender = findViewById(R.id.gender);
        TextView location = findViewById(R.id.location);
        textView = findViewById(R.id.textView);
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://randomuser.me/api/", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray arr = response.getJSONArray("results");
                    JSONObject obj = arr.getJSONObject(0);
                    String url = (obj.getJSONObject("picture")).getString("large");
                    new FetchImage(url).start();
                    String name = (obj.getJSONObject("name")).getString("title") + " " + (obj.getJSONObject("name")).getString("first") + " " + (obj.getJSONObject("name")).getString("last");
                    uname.setText(name);
                    String u = (obj.getJSONObject("login")).getString("username");
                    username.setText(u);
                    String gen = obj.getString("gender");
                    gender.setText(gen);
                    String loc = ((obj.getJSONObject("location").getJSONObject("street")).getString("number") + ", " + ((obj.getJSONObject("location")).getJSONObject("street")).getString("name") + ", " +  (obj.getJSONObject("location")).getString("city") + ", ";
                    location.setText(loc);
                    String remain = (obj.getJSONObject("location")).getString("state") + ", " + (obj.getJSONObject("location")).getString("country");
                    remains.setText(remain);
                    String e = obj.getString("email");
                    email.setText(e);
                    String ph = obj.getString("phone");
                    phone.setText(ph);
                    String id = (obj.getJSONObject("id")).getString("value");
                    userid.setText(id);
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("myApp","Something went wrong.");
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
    class FetchImage extends Thread{

        String URL;
        Bitmap bitmap;

        FetchImage(String URL){

            this.URL = URL;

        }

        @Override
        public void run() {

            InputStream inputStream = null;
            try {
                inputStream = new URL(URL).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }

            mainHandler.post(new Runnable() {
                @Override
                public void run() {

                    ImageView imageView = findViewById(R.id.imageView);
                    imageView.setImageBitmap(bitmap);

                }
            });
        }
    }
}