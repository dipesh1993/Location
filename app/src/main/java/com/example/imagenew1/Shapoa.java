package com.example.imagenew1;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Shapoa extends Fragment {
    View view;
    String user_id,dateStr;
    List<Visit> productList;
    //    private static final String URL_PRODUCTS = "http://192.168.2.9/cameraloc1/popup.php";
    SessionManager sessionManager;
    Context context;
    String str = "";
    JSONObject json = null;
    HttpResponse response;
    String t, t1, t2, t3, t4, s, mId;
    Button regbtn;
    String roaster_id1, place_name, place_village, date, name1, designation1;
    TextView questio1, questio2, questio3, date1, place, village;
    RatingBar ratingbar;
    ProgressDialog progressDialog;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        productList = new ArrayList<>();

        View view = inflater.inflate(R.layout.shaleyposhan, container, false);
        questio1 = (TextView) view.findViewById(R.id.textView2);
        questio2 = (TextView) view.findViewById(R.id.textView3);
        questio3 = (TextView) view.findViewById(R.id.textView4);


        date1 = view.findViewById(R.id.date);
        place = view.findViewById(R.id.place);
        village = view.findViewById(R.id.village);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        regbtn = (Button) view.findViewById(R.id.button);
        ratingbar = (RatingBar) view.findViewById(R.id.ratingBar);
        sessionManager = new SessionManager(getActivity().getApplicationContext());
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
        mId = user.get(sessionManager.ID);
        roaster_id1=getActivity().getIntent().getExtras().getString("roaster_id");

        place_name=getActivity().getIntent().getExtras().getString("city");
        place_village=getActivity().getIntent().getExtras().getString("village");
        date=getActivity().getIntent().getExtras().getString("date");
        String text = "<b>दिनांक :</b>" + date;
        date1.setText(Html.fromHtml(text));
        String text1 = "<b>नावं:</b>" + place_name;
        place.setText(Html.fromHtml(text1));
        String text2 = "<b>गावं :</b>" + place_village;
        village.setText(Html.fromHtml(text2));
        DateFormat oldFormat = new SimpleDateFormat("dd-MM-yyyy");

        DateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd");


        try {
            dateStr = newFormat.format(oldFormat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        new GetText(context).execute();

//        new RoasterUpdate.GetText(context).execute();

        final Switch sw = (Switch) view.findViewById(R.id.switch1);
        final Switch sw1 = (Switch) view.findViewById(R.id.switch2);
        final Switch sw2 = (Switch) view.findViewById(R.id.editText2);


        sw.setTextOff("नाही");
        sw.setTextOn("होय");
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switch (buttonView.getId()) {
                    case R.id.switch1:
                        if (isChecked) {
                            t = sw.getTextOn().toString();
                        } else {
                            t = sw.getTextOff().toString();
                            ;
                        }
                        break;
                }
            }
        });
        sw1.setTextOff("नाही");
        sw1.setTextOn("होय");
        sw1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switch (buttonView.getId()) {
                    case R.id.switch2:
                        if (isChecked) {
                            t1 = sw1.getTextOn().toString();
                        } else {
                            t1 = sw1.getTextOn().toString();

                        }
                        break;
                    // Do something
                }
            }
        });
        sw2.setTextOff("नाही");
        sw2.setTextOn("होय");
        sw2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switch (buttonView.getId()) {
                    case R.id.switch3:
                        if (isChecked) {
                            t2 = sw2.getTextOn().toString();
                        } else {
                            t2 = sw2.getTextOn().toString();

                        }
                        break;
                    // Do something
                }
            }
        });
        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    String question1 = questio1.getText().toString();
                    String question2 = questio2.getText().toString();
                    String question3 = questio3.getText().toString();

                    final String roaster_id = roaster_id1;


                    String tb = t;
                    if (tb == null) {
                        tb = "नाही";
                    }
                    String tb1 = t1;
                    if (tb1 == null) {
                        tb1 = "नाही";
                    }
                    String tb2 = t2;
                    if (tb2 == null)
                    {
                        tb2 = "नाही";
                    }
                    String type = "insert";
                    BackgroundWorker backgroundWorker = new BackgroundWorker(context);
                    backgroundWorker.execute(type, question1, question2, question3, tb, tb1, tb2,roaster_id,dateStr);
                }
        });

        this.view = view;
//        loadIntoListView();
        return view;

    }
    public class BackgroundWorker extends AsyncTask<String,String,String> {
        Context context;

        public BackgroundWorker(Context ctx) {
            context = ctx;
        }

        @Override
        protected String doInBackground(String... strings) {
            String type = strings[0];
            String insertURL = "http://zp.dpcjalgaon.com/android/shaleyposhan.php";
//            String insertURL = "http://192.168.2.9/cameraloc1/shaleyposhan.php";

            if (type.equals("insert")) {

                String question1 = strings[1];
                String question2 = strings[2];
                String question3 = strings[3];
                String tb = strings[4];
                String tb1 = strings[5];
                String tb2 = strings[6];
                String roaster_id = strings[7];
                String date = strings[8];
                try {
                    URL url = new URL(insertURL);
                    try {
                        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                        httpURLConnection.setRequestMethod("POST");
                        httpURLConnection.setDoOutput(true);
                        httpURLConnection.setDoInput(true);
                        OutputStream outputStream = httpURLConnection.getOutputStream();
                        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
                        BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
                        String insert_data = URLEncoder.encode("question1", "UTF-8") + "=" + URLEncoder.encode(question1, "UTF-8")
                                + "&" + URLEncoder.encode("question2", "UTF-8") + "=" + URLEncoder.encode(question2, "UTF-8")
                                + "&" + URLEncoder.encode("question3", "UTF-8") + "=" + URLEncoder.encode(question3, "UTF-8")
                                + "&" + URLEncoder.encode("switch1", "UTF-8") + "=" + URLEncoder.encode(tb, "UTF-8")
                                + "&" + URLEncoder.encode("switch2", "UTF-8") + "=" + URLEncoder.encode(tb1, "UTF-8")
                                + "&" + URLEncoder.encode("switch3", "UTF-8") + "=" + URLEncoder.encode(tb2, "UTF-8")
                                + "&" + URLEncoder.encode("roaster_id", "UTF-8") + "=" + URLEncoder.encode(roaster_id, "UTF-8")
                                + "&" + URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(date, "UTF-8");
                        bufferedWriter.write(insert_data);
                        bufferedWriter.flush();
                        bufferedWriter.close();
                        InputStream inputStream = httpURLConnection.getInputStream();
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "ISO-8859-1");
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        String result = "";
                        String line = "";
                        StringBuilder stringBuilder = new StringBuilder();
                        while ((line = bufferedReader.readLine()) != null) {
                            stringBuilder.append(line).append("\n");

                        }
                        result = stringBuilder.toString();
                        bufferedReader.close();
                        inputStream.close();
                        httpURLConnection.disconnect();
                        return result;

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(getContext(),"Saving Data..","Please Wait",false,false);
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            Toast.makeText(getActivity().getApplicationContext(), s, Toast.LENGTH_LONG).show();
            Intent intent=new Intent(getActivity().getApplicationContext(),MyReport.class);
            intent.putExtra("name",name1);
            intent.putExtra("designation",designation1);
            getContext().startActivity(intent);
            getActivity().onBackPressed();

//            finish();
            super.onPostExecute(s);
        }
    }
    //    @Override
//    public void onBackPressed() {
//        new AlertDialog.Builder(this)
//                .setTitle("Exit?")
//                .setMessage("Are you sure you want to exit?")
//                .setNegativeButton(android.R.string.no, null)
//                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//
//                    public void onClick(DialogInterface arg0, int arg1) {
//                        setResult(RESULT_OK, new Intent().putExtra("EXIT", true));
//                        finish();
//                    }
//                }).create().show();
//    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id)
        {
            case R.id.action_logout:
                sessionManager.logout();
                break;
            case R.id.action_profile:
//                Intent intent=new Intent(this,UserProfile.class);
//                intent.putExtra("name",name1);
//                intent.putExtra("designation",designation1);
//                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private class GetText extends AsyncTask<Void, Void, Void> {
        public Context context;


        public GetText(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("user_id", mId));



            try {
                HttpClient myClient = new DefaultHttpClient();
                HttpPost myConnection = new HttpPost("http://zp.dpcjalgaon.com/android/userProfile.php");
//                HttpPost myConnection = new HttpPost("http://192.168.43.129/cameraloc1/userProfile.php");

                myConnection.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                response = myClient.execute(myConnection);
                str = EntityUtils.toString(response.getEntity(), "UTF-8");

            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            try {
                JSONArray jArray = new JSONArray(str);
                json = jArray.getJSONObject(0);


            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        protected void onPostExecute(Void result) {


            try {
                name1=json.getString("name");
                designation1=json.getString("designation");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
