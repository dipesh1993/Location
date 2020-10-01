package com.example.imagenew1;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

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
import java.util.List;

public class NotVisitedFragment extends Fragment
{
    View view;
    String user_id;
    ListView simpleList;
    ProgressDialog pDialog;
    //    int flags[] ={R.drawable.pic1,R.drawable.pic2,R.drawable.pic3,R.drawable.pic4,R.drawable.demoprofile,R.drawable.hands};
//
//    String countryList[] = {"India", "China", "australia", "Portugle", "America", "NewZealand"};
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news, container, false);
        user_id = getActivity().getIntent().getExtras().getString("user_id");
        getJSON("http://zp.dpcjalgaon.com/android/notvisitedfragment.php");
        this.view=view;
        return view;
    }
    private void getJSON(final String urlWebService) {

        class GetJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pDialog = new ProgressDialog(getActivity());
                pDialog.setMessage("Please wait...");
                pDialog.show();
            }


            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            protected void onPostExecute(String s) {
                pDialog.dismiss();
                super.onPostExecute(s);

                try {
                    loadIntoListView(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    URL url = new URL(urlWebService);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setReadTimeout(10000);
                    con.setConnectTimeout(15000);
                    con.setRequestMethod("POST");
                    con.setDoInput(true);
                    con.setDoOutput(true);

                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("user_id", user_id));

                    OutputStream os = con.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(getQuery(params));
                    writer.flush();
                    writer.close();
                    os.close();

                    con.connect();
                    StringBuilder sb = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }
                    return sb.toString().trim();
                } catch (Exception e) {
                    return null;
                }
            }
        }
        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void loadIntoListView(String json) throws JSONException, ParseException {
        JSONArray jsonArray = new JSONArray(json);
        String[] date = new String[jsonArray.length()];
        String[] place = new String[jsonArray.length()];
        String[] village = new String[jsonArray.length()];
        String[] status = new String[jsonArray.length()];
        String[] roaster = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            DateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat newFormat = new SimpleDateFormat("dd-MM-yyyy ");

            date[i] = newFormat.format(oldFormat.parse(obj.getString("date")));
//            date[i] = obj.getString("date");
            place[i] = obj.getString("place_name");
            village[i] = obj.getString("place_village");
            status[i] = obj.getString("status");
            roaster[i] = obj.getString("roaster_id");
        }

        simpleList = (ListView) view.findViewById(R.id.simpleListView);
        if (isAdded()) {
            CustomAdapter customAdapter = new CustomAdapter(getActivity().getApplicationContext(), date, place, village, status, user_id, roaster);
            if (customAdapter != null) {
                if (customAdapter.getCount() > 0) {
                    simpleList.setAdapter(customAdapter);
                } else {
                    Toast.makeText(getContext(), "NO Data Available..", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (NameValuePair pair : params)
        {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }

        return result.toString();
    }
}
