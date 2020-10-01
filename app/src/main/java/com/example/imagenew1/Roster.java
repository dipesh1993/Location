package com.example.imagenew1;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Roster extends Fragment {
    View view;
    String user_id;
        List<Visit> productList;
    private static final String URL_PRODUCTS = "http://zp.dpcjalgaon.com/android/popup.php";
//    private static final String URL_PRODUCTS = "http://192.168.43.129/cameraloc1/popup.php";

    String mId;
    RecyclerView recyclerView;
    SessionManager sessionManager;
    private ProgressDialog pDialog;
    TextView message;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        productList = new ArrayList<>();

        View view = inflater.inflate(R.layout.roster, container, false);
        recyclerView = view.findViewById(R.id.recylcerView);
        message=view.findViewById(R.id.textView19);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity().getApplicationContext(), 2, GridLayout.VERTICAL, false));
        sessionManager = new SessionManager(getActivity());
        HashMap<String, String> user = sessionManager.getUserDetail();
        mId = user.get(sessionManager.ID);

        this.view = view;
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.show();
        loadIntoListView();
        return view;

    }


            private void loadIntoListView() {
//                pulltoRefresh.setRefreshing(true);
                /*
                 * Creating a String Request
                 * The request type is GET defined by first parameter
                 * The URL is defined in the second parameter
                 * Then we have a Response Listener and a Error Listener
                 * In response listener we will get the JSON response as a String
                 * */
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_PRODUCTS,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                            pDialog.dismiss();
                                productList.clear();
                                try {
                                    //converting the string to json array object
                                    JSONArray array = new JSONArray(response);

                                    //traversing through all the object
                                    for (int i = 0; i < array.length(); i++) {

                                        //getting product object from json array
                                        JSONObject product = array.getJSONObject(i);

                                        //adding the product to product list
                                        productList.add(new Visit(
                                            product.getInt("roaster_id"),
                                            product.getString("date"),
                                            product.getString("place_name"),
                                            product.getString("place_village")
                                    ));
                                    }


                                    //creating adapter object and setting it to recyclerview
                                    if (isAdded())
                                    {
                                        VisitsAdapter adapter = new VisitsAdapter(getActivity().getApplicationContext(), productList);


                                        if (adapter != null)
                                        {
                                            if (adapter.getItemCount() > 0) {
                                                recyclerView.setAdapter(adapter);
                                            } else {
                                                Toast.makeText(getActivity().getApplicationContext(), "No Data Available....", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }

                                }
                                catch (JSONException e) {

                                    e.printStackTrace();
                                    message.setText("No Data Available....");
                                }
//                                pulltoRefresh.setRefreshing(false);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
//                                pulltoRefresh.setRefreshing(false);
                            }
                        })
                {
                    protected Map<String, String> getParams ()
                    {
                        Map<String, String> MyData = new HashMap<String, String>();
                        MyData.put("user_id", mId);
                        return MyData;
                    }
                };


                //adding our stringrequest to queue
                Volley.newRequestQueue(getActivity().getApplicationContext()).add(stringRequest);
            }

}

