package com.example.imagenew1;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;



public class Profile extends AppCompatActivity {

    Context context;
    String str = "",str1 = "",img_url;
    JSONObject json = null;
    JSONObject json1 = null;
    HttpResponse response,response1;
    SessionManager sessionManager;
    TextView textView,name,designation;
    String mId,name1,designation1,date1,city,village;
    Toolbar toolbar;
    ProgressBar progressBar;
    ImageView pro_img;
    ProgressDialog progressDialog;

    boolean internet_connection()
    {
        //Check if connected to internet, output accordingly
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        ActionBar bar = getActionBar();
        if(bar!=null){
            TextView tv = new TextView(getApplicationContext());
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT, // Width of TextView
                    RelativeLayout.LayoutParams.WRAP_CONTENT); // Height of TextView
            tv.setLayoutParams(lp);
            tv.setText(bar.getTitle());
            tv.setGravity(Gravity.CENTER);
            tv.setTextColor(Color.WHITE);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            bar.setCustomView(tv);
        }
        pro_img=findViewById(R.id.imageView5);
        textView=findViewById(R.id.textView);
        name=findViewById(R.id.textView7);
        designation=findViewById(R.id.textView8);
        sessionManager =new SessionManager(this);
        sessionManager.checkLogin();
        HashMap<String,String> user=sessionManager.getUserDetail();
        mId=user.get(sessionManager.ID);
        if (internet_connection()){
//            textView.setText(mId);
        new GetText(context).execute();
        }
        else{
            //create a snackbar telling the user there is no internet connection and issuing a chance to reconnect
            final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                    "No internet connection.",
                    Snackbar.LENGTH_INDEFINITE);
            snackbar.setActionTextColor(ContextCompat.getColor(Profile.this,
                    R.color.colorAmber));
            snackbar.setAction(R.string.try_again, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (internet_connection())
                    {
//            textView.setText(mId);
                        new GetText(context).execute();
                    }
                    else {
                        Toast.makeText(Profile.this,"please connect to internet and Try Again",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    //recheck internet connection and call DownloadJson if there is internet

                }
            }).show();
        }

    }


    public void mProfile(View view)
    {
            Intent intent = new Intent(this, UserProfile.class);
            intent.putExtra("name", name1);
            intent.putExtra("designation", designation1);
            startActivity(intent);

    }

    public void upcoming(View view) {
        Intent intent=new Intent(this,UpcomingVisits.class);
        intent.putExtra("name",name1);
        intent.putExtra("designation",designation1);
        startActivity(intent);
    }

    public void roaster(View view)
    {
        Intent intent=new Intent(this,MyRoaster.class);
        intent.putExtra("name",name1);
        intent.putExtra("designation",designation1);
        intent.putExtra("user_id",mId);
        startActivity(intent);
    }

    public void myreport(View view) {
        Intent intent=new Intent(this,MyReport.class);
        intent.putExtra("name",name1);
        intent.putExtra("designation",designation1);
        intent.putExtra("user_id",mId);
        startActivity(intent);
    }




    private class GetText extends AsyncTask<Void, Void, Void> {
            public Context context;


            public GetText(Context   context) {
                this.context = context;
            }

            @Override
            protected void onPreExecute() {
                progressDialog = new ProgressDialog(Profile.this);
                progressDialog.setMessage("Loading...");
                progressDialog.show();
                progressDialog.setCancelable(false);
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... arg0) {

                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("user_id", mId));



                try {
                    HttpClient myClient = new DefaultHttpClient();
                    HttpPost myConnection = new HttpPost("http://zp.dpcjalgaon.com/android/userProfile.php");
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
            progressDialog.dismiss();
                try {
                    name1=json1.getString("name");
                    designation1=json1.getString("designation");
                    name.setText(name1);
                    designation.setText(designation1);
                    img_url=json1.getString("img_link");

                        Picasso.with(Profile.this).load(img_url).placeholder(R.drawable.profile).error(R.drawable.profile).into(pro_img);

                    new GetTextViewData(context).execute();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    private class GetTextViewData extends AsyncTask<Void, Void, Void> {
        public Context context;


        public GetTextViewData(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0)
        {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("user_id", mId));


            try {
                HttpClient myClient = new DefaultHttpClient();
                HttpPost myConnection = new HttpPost("http://zp.dpcjalgaon.com/android/popup.php");
//                HttpPost myConnection = new HttpPost("http://192.168.43.129/cameraloc1/popup.php");

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
            if (json != null && !json.equals("null")) {
                try {
                    if (json.getString("date").equals(new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime())))
                    {
                        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Profile.this);
    // ...Irrelevant code for customizing the buttons and title
                        LayoutInflater inflater = Profile.this.getLayoutInflater();
                        final View dialogView = inflater.inflate(R.layout.custompopup, null);
                        dialogBuilder.setView(dialogView);
                        Button visit = dialogView.findViewById(R.id.visit);
                        Button cancel = dialogView.findViewById(R.id.cancel);
                        TextView close = dialogView.findViewById(R.id.close);

    //                final AlertDialog show = dialogBuilder.show();
                        final TextView date = (TextView) dialogView.findViewById(R.id.date1);
                        final TextView place_name = (TextView) dialogView.findViewById(R.id.place_name);
                        final TextView place_village = (TextView) dialogView.findViewById(R.id.village_name);
                        final TextView user_id = (TextView) dialogView.findViewById(R.id.user_id);
                        final TextView roaster_id = (TextView) dialogView.findViewById(R.id.roaster_id);

                        try {

                            DateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd");
                            DateFormat newFormat = new SimpleDateFormat("dd-MM-yyyy ");

                            String dateStr = newFormat.format(oldFormat.parse(json.getString("date")));
                            date.setText("दिनांक :" + dateStr);
                            //                    date.setText(json.getString("date"));
                            place_name.setText("शहर :" + json.getString("place_name"));
                            place_village.setText("गांव/ठिकाण  :" + json.getString("place_village"));
                            user_id.setText(json.getString("user_id"));
                            roaster_id.setText(json.getString("roaster_id"));
                            date1 = dateStr;
                            city = json.getString("place_name");
                            village = json.getString("place_village");
                            user_id.setVisibility(View.GONE);
                            roaster_id.setVisibility(View.GONE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        final AlertDialog alertDialog = dialogBuilder.create();
                        visit.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Profile.this, MainActivity.class);
                                intent.putExtra("roaster_id", roaster_id.getText().toString());
                                intent.putExtra("date", date1);
                                intent.putExtra("place_name", city);
                                intent.putExtra("place_village", village);

                                startActivity(intent);
                                alertDialog.dismiss();

                            }
                        });
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.dismiss();

                            }
                        });
                        close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.dismiss();
                            }
                        });
                        alertDialog.show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
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
        finish();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id)
        {
            case R.id.action_logout:
                sessionManager.logout();
                break;
            case R.id.action_profile:
                Intent intent=new Intent(this,UserProfile.class);
                intent.putExtra("name",name1);
                intent.putExtra("designation",designation1);
                startActivity(intent);
        }
        return true;
    }

}

