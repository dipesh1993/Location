package com.example.imagenew1;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;

import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

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

import java.io.IOException;
import java.util.ArrayList;

public class Report extends AppCompatActivity {
    Context context;
    TextView date, place, village, textView;
    String str = "",str1 = "", roaster_id, user_id, date1, place1, village1;
    JSONObject json = null,json1=null;
    HttpResponse response,response1;
    String question;
    String ans;
    ScrollView scrollview;
    LinearLayout linearLayout,ln;
    TextView que,ans1;
    ImageView img;
    ProgressDialog progressDialog,pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        linearLayout =  findViewById(R.id.scrllinear);
        ln =  findViewById(R.id.ln);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        date = findViewById(R.id.date);
        place = findViewById(R.id.place);
        village = findViewById(R.id.village);
        roaster_id = getIntent().getExtras().getString("roaster_id");
        date1 = getIntent().getExtras().getString("date");
        place1 = getIntent().getExtras().getString("place");
        village1 = getIntent().getExtras().getString("village");
        img=findViewById(R.id.img);
        String text = "<b>दिनांक :</b>" + date1;
        date.setText(Html.fromHtml(text));
        String text1 = "<b>शहर :</b>" + place1;
        place.setText(Html.fromHtml(text1));
        String text2 = "<b>गावं/ठिकाण :</b>" + village1;
        village.setText(Html.fromHtml(text2));

        new GetTextView(context).execute();
        new Report.GetTextViewData(context).execute();
    }


    private class GetTextViewData extends AsyncTask<Void, Void, Void> {
        public Context context;


        public GetTextViewData(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(Report.this);
            pDialog.setMessage("Please wait...");
            pDialog.show();
            super.onPreExecute();
        }



        @Override
        protected Void doInBackground(Void... arg0) {

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

            nameValuePairs.add(new BasicNameValuePair("roaster_id", roaster_id));


            try {
                HttpClient myClient = new DefaultHttpClient();
                HttpPost myConnection = new HttpPost("http://zp.dpcjalgaon.com/android/report.php");
//                HttpPost myConnection = new HttpPost("http://192.168.43.129/cameraloc1/report.php");

                myConnection.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                response = myClient.execute(myConnection);
                str = EntityUtils.toString(response.getEntity(), "UTF-8");

            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        protected void onPostExecute(Void result) {
            try {
                    pDialog.dismiss();
                JSONArray jArray = new JSONArray(str);
                for (int i = 0; i < jArray.length(); i++) {

                    json = jArray.getJSONObject(i);
                    question= json.getString("question");
                    ans= json.getString("ans");
                    TableLayout.LayoutParams lp = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
                    lp.setMargins(5, 5, 5, 20);

                    for (int n = 0; n < 1; n++)
                    {
                        TextView tvDebt=new TextView(Report.this);
                        TextView tvDebt1=new TextView(Report.this);
                        tvDebt.setText(question);
                        tvDebt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                        tvDebt.setTextColor(Color.parseColor("#040F49"));
                        tvDebt1.setTextColor(Color.parseColor("#FF3D3D42"));
                        tvDebt.setPadding(30,0,0,0);
                        tvDebt1.setPadding(0,0,25,0);
                        if (tvDebt1.equals(null))
                        {
                            tvDebt.setVisibility(View.GONE);
                        }
                        tvDebt1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                        tvDebt1.setText(ans);
                        tvDebt1.setGravity(Gravity.END);

                        linearLayout.addView(tvDebt, lp);
                        ln.addView(tvDebt1, lp);

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
        @Override
        public boolean onSupportNavigateUp() {
            finish();
            return true;
        }
    private class GetTextView extends AsyncTask<Void, Void, Void> {
        public Context context;


        public GetTextView(Context context) {
            this.context = context;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("roaster_id", roaster_id));



            try {
                HttpClient myClient = new DefaultHttpClient();
                HttpPost myConnection = new HttpPost("http://zp.dpcjalgaon.com/android/reportimage.php");
                myConnection.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                response1 = myClient.execute(myConnection);
                str1 = EntityUtils.toString(response1.getEntity(), "UTF-8");

            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            try {
                JSONArray jArray = new JSONArray(str1);
                json1 = jArray.getJSONObject(0);


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

                String reportimg_url=json1.getString("img_path");



                Picasso.with(Report.this).load(reportimg_url).placeholder(R.drawable.profile).error(R.drawable.profile).into(img);
//                roaster_id.setVisibility(View.GONE);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    }




