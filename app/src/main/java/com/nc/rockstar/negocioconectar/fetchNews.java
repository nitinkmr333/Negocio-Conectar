package com.nc.rockstar.negocioconectar;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class fetchNews extends AppCompatActivity {

    private RequestQueue newsQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        newsQueue = Volley.newRequestQueue(this);

    }

    private StringRequest searchNameStringRequest(String nameSearch) {
        final String API = "fe0e554c0ed24a8da06134a60cffa2f4";
        final String NAME_SEARCH = "&q=";
        final String DATA_SOURCE = "&ds=Standard Reference";
        final String FOOD_GROUP = "&fg=";
        final String SORT = "&sort=r";
        final String MAX_ROWS = "&max=25";
        final String BEGINNING_ROW = "&offset=0";
        final String URL_PREFIX = "https://newsapi.org/v2/top-headlines?country=india&";

        //String url = URL_PREFIX + API + NAME_SEARCH + nameSearch + DATA_SOURCE + FOOD_GROUP + SORT + MAX_ROWS + BEGINNING_ROW;
        String url = URL_PREFIX + API;

        // 1st param => type of method (GET/PUT/POST/PATCH/etc)
        // 2nd param => complete url of the API
        // 3rd param => Response.Listener -> Success procedure
        // 4th param => Response.ErrorListener -> Error procedure
        return new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    // 3rd param - method onResponse lays the code procedure of success return
                    // SUCCESS
                    @Override
                    public void onResponse(String response) {
                        // try/catch block for returned JSON data
                        // see API's documentation for returned format
                        try {
                            JSONObject result = new JSONObject(response).getJSONObject("list");
                            int maxItems = result.getInt("end");
                            JSONArray resultList = result.getJSONArray("item");
                            System.out.println(resultList);

                        // Rest of the code

                            // catch for the JSON parsing error
                        } catch (JSONException e) {
                            Toast.makeText(fetchNews.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    } // public void onResponse(String response)
                }, // Response.Listener<String>()
                new Response.ErrorListener() {
                    // 4th param - method onErrorResponse lays the code procedure of error return
                    // ERROR
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // display a simple message on the screen
                        Toast.makeText(fetchNews.this, "Food source is not responding (USDA API)", Toast.LENGTH_LONG).show();
                    }
                });
    }


    public void btnSearchClickEventHandler() {

        // Some code
        String TAG_SEARCH_NAME = "Apple";
        String txt = "BMW";

        //txtSearch.getText()

        // cancelling all requests about this search if in queue
        //newsQueue.cancelAll(TAG_SEARCH_NAME);

        // first StringRequest: getting items searched
        StringRequest stringRequest = searchNameStringRequest(txt.toString());
        stringRequest.setTag(TAG_SEARCH_NAME);

        // executing the request (adding to queue)
        newsQueue.add(stringRequest);
    }

}
