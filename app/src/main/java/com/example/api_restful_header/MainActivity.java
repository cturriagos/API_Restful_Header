package com.example.api_restful_header;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.wifi.rtt.ResponderLocation;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TextView txtid;
    private RequestQueue requestQueue;
    private String url = "https://dbrrkk.herokuapp.com/webresources/client";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtid = findViewById(R.id.txtLista);
        Button btnLista = findViewById(R.id.btnLista);

        requestQueue = Volley.newRequestQueue(this);

        btnLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtid.setText("");
                consumirVolley();
            }
        });
    }

    private void consumirVolley(){

        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        int size = response.length();
                        response = fixEncoding(response);
                        try {
                            JSONArray jsa = new JSONArray(response);
                            for(int i = 0; i < jsa.length(); i++){
                                JSONObject ob = jsa.getJSONObject(i);
                                String id_client = ob.getString("id_client");
                                String name = ob.getString("name");
                                String last_name = ob.getString("last_name");
                                String email = ob.getString("email");
                                String dni = ob.getString("dni");
                                String state = ob.getString("state");

                                txtid.setText(id_client + "\n" + name + "\n" + last_name + "\n" +
                                              email + "\n" + dni + "\n" + state);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // JSONObject jsont = new JSONObject(response);
                          //  Log.d("response", response);

                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", String.valueOf(error));
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Content-Type", "application/json; charset=utf-8");
                params.put("Accept", "application/json");
                params.put("email_header", "celso.turriago2017@uteq.edu.ec");

                return params;
            }
        };
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(request);
        } else {
            requestQueue.add(request);
        }
    }

    public static String fixEncoding(String response) {
        try {
            byte[] u = response.toString().getBytes(
                    "ISO-8859-1");
            response = new String(u, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        return response;
    }
}