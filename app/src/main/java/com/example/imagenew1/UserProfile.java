package com.example.imagenew1;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class UserProfile extends AppCompatActivity {
    Context context;
    String str = "",mId,name1,designation1,img_caption,GetImageNameFromEditText,img_url;
    JSONObject json = null;
    HttpResponse response;
    SessionManager sessionManager;
    TextView fname,fdesignation;
    EditText name,mobile,email,designation,workplace,username,password;
    Button save;
    private static int RESULT_LOAD_IMAGE = 1;
    ImageView pro_img;
    boolean check = true;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        name= findViewById(R.id.name);
        fname= findViewById(R.id.textView7);
        fdesignation= findViewById(R.id.textView8);
        mobile=findViewById(R.id.editText3);
        email= findViewById(R.id.editText4);
        designation=findViewById(R.id.editText5);
        workplace=findViewById(R.id.editText6);
        username=findViewById(R.id.editText7);
        password=findViewById(R.id.editText8);
        save=findViewById(R.id.button3);
        pro_img=findViewById(R.id.imageView5);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sessionManager =new SessionManager(this);
        sessionManager.checkLogin();
        HashMap<String,String> user=sessionManager.getUserDetail();
        mId=user.get(sessionManager.ID);

        pro_img.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        new UserProfile.GetTextViewData(context).execute();
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            pro_img.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            pro_img.buildDrawingCache();
            img_caption=new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
            GetImageNameFromEditText = "IMG" +img_caption +".jpg";
            Bitmap bm = pro_img.getDrawingCache();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
            byte[] b = baos.toByteArray();
            final String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
            class AsyncTaskUploadClass extends AsyncTask<Void, Void, String> {

                @Override
                protected void onPreExecute() {

                    super.onPreExecute();

                    // Showing progress dialog at image upload time.
//                    progressDialog = ProgressDialog.show(MainActivity.this, "Image is Uploading", "Please Wait", false, false);
                }

                @Override
                protected void onPostExecute(String string1) {

//                    progressDialog.dismiss();
                    Toast.makeText(UserProfile.this, string1, Toast.LENGTH_SHORT).show();

//                Intent intent = new Intent();
//                intent.setClass(getApplicationContext(),Profile.class);
//                intent.putExtra("img_url",string1);
//                intent.putExtra("city",city);
//                intent.putExtra("village",village);
//                intent.putExtra("date",date1);
//
//                startActivity(intent);


                }

                @Override
                protected String doInBackground(Void... params) {

                    UserProfile.ImageProcessClass imageProcessClass = new UserProfile.ImageProcessClass();
                    HashMap<String, String> HashMapParams = new HashMap<String, String>();

                    HashMapParams.put("img_path", encodedImage);
                    HashMapParams.put("img_name", GetImageNameFromEditText);
                    HashMapParams.put("emp_id", mId);
//                HashMapParams.put(ImageVillageOnServer, village);
//                HashMapParams.put(ImageRoasterIdOnServer, roaster_id);

                    String FinalData = imageProcessClass.ImageHttpRequest("http://zp.dpcjalgaon.com/android/profile_image_upload.php", HashMapParams);

                    return FinalData;
                }
            }
            AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();

            AsyncTaskUploadClassOBJ.execute();
        }
    }
    public class ImageProcessClass {

        public String ImageHttpRequest(String requestURL, HashMap<String, String> PData) {

            StringBuilder stringBuilder = new StringBuilder();

            try {

                URL url;
                HttpURLConnection httpURLConnectionObject;
                OutputStream OutPutStream;
                BufferedWriter bufferedWriterObject;
                BufferedReader bufferedReaderObject;
                int RC;

                url = new URL(requestURL);

                httpURLConnectionObject = (HttpURLConnection) url.openConnection();

                httpURLConnectionObject.setReadTimeout(19000);

                httpURLConnectionObject.setConnectTimeout(19000);

                httpURLConnectionObject.setRequestMethod("POST");

                httpURLConnectionObject.setDoInput(true);

                httpURLConnectionObject.setDoOutput(true);

                OutPutStream = httpURLConnectionObject.getOutputStream();

                bufferedWriterObject = new BufferedWriter(

                        new OutputStreamWriter(OutPutStream, "UTF-8"));

                bufferedWriterObject.write(bufferedWriterDataFN(PData));

                bufferedWriterObject.flush();

                bufferedWriterObject.close();

                OutPutStream.close();

                RC = httpURLConnectionObject.getResponseCode();

                if (RC == HttpsURLConnection.HTTP_OK) {

                    bufferedReaderObject = new BufferedReader(new InputStreamReader(httpURLConnectionObject.getInputStream()));

                    stringBuilder = new StringBuilder();

                    String RC2;

                    while ((RC2 = bufferedReaderObject.readLine()) != null) {

                        stringBuilder.append(RC2);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return stringBuilder.toString();
        }

        private String bufferedWriterDataFN(HashMap<String, String> HashMapParams) throws UnsupportedEncodingException {

            StringBuilder stringBuilderObject;

            stringBuilderObject = new StringBuilder();

            for (Map.Entry<String, String> KEY : HashMapParams.entrySet()) {

                if (check)

                    check = false;
                else
                    stringBuilderObject.append("&");

                try {
                    stringBuilderObject.append(URLEncoder.encode(KEY.getKey(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                stringBuilderObject.append("=");

                stringBuilderObject.append(URLEncoder.encode(KEY.getValue(), "UTF-8"));
            }

            return stringBuilderObject.toString();
        }
    }
    public void save(View view) {
        name1 = name.getText().toString();
        String mobile1 = mobile.getText().toString();
        String email1 = email.getText().toString();
        designation1 = designation.getText().toString();
        String workplace1 = workplace.getText().toString();
        String username1 = username.getText().toString();
        String password1 = password.getText().toString();
        String type="update";
        BackgroundWorker backgroundWorker= new BackgroundWorker(getApplicationContext());
        backgroundWorker.execute(type,name1,mobile1,email1,designation1,workplace1,username1,password1);
    }

    public class BackgroundWorker extends AsyncTask<String,String,String> {
        Context context;
        public BackgroundWorker(Context ctx) {
            context = ctx;
        }

        @Override
        protected String doInBackground(String... strings) {
            String type = strings[0];

            String updateURL = "http://zp.dpcjalgaon.com/android/updateUser.php";
            if (type.equals("update")) {
                String name = strings[1];
                String mobile = strings[2];
                String email = strings[3];
                String designation = strings[4];
                String workplace = strings[5];
                String username = strings[6];
                String password = strings[7];
                try {
                    URL url = new URL(updateURL);
                    try {
                        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                        httpURLConnection.setRequestMethod("POST");
                        httpURLConnection.setDoOutput(true);
                        httpURLConnection.setDoInput(true);
                        OutputStream outputStream = httpURLConnection.getOutputStream();
                        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
                        BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);

                        String insert_data = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8")
                                + "&" + URLEncoder.encode("mobile", "UTF-8") + "=" + URLEncoder.encode(mobile, "UTF-8")
                                + "&" + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8")
                                + "&" + URLEncoder.encode("designation", "UTF-8") + "=" + URLEncoder.encode(designation, "UTF-8")
                                + "&" + URLEncoder.encode("workplace", "UTF-8") + "=" + URLEncoder.encode(workplace, "UTF-8")
                                + "&" + URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8")
                                + "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8")
                                + "&" + URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(mId, "UTF-8");
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
        protected void onPreExecute() {

            super.onPreExecute();
            progressDialog = new ProgressDialog(UserProfile.this);
            progressDialog.setMessage("Saving Data...");
            progressDialog.show();
            progressDialog.setCancelable(true);
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            Toast.makeText(context, s, Toast.LENGTH_LONG).show();

            super.onPostExecute(s);
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
            progressDialog = new ProgressDialog(UserProfile.this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            progressDialog.setCancelable(true);

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("user_id", mId));



            try {
                HttpClient myClient = new DefaultHttpClient();
                HttpPost myConnection = new HttpPost("http://zp.dpcjalgaon.com/android/userProfile.php");
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
            progressDialog.dismiss();
//
//            try {
//                DateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd");
//                DateFormat newFormat = new SimpleDateFormat("dd-MM-yyyy ");
//
//                String dateStr = newFormat.format(oldFormat.parse(json.getString("date")));
//            date.setText(dateStr);
//                    date.setText(json.getString("date"));
            try {
                name.setText(json.getString("name"));
                mobile.setText(json.getString("mobile_no"));
                email.setText(json.getString("email"));
                designation.setText(json.getString("designation"));
                workplace.setText(json.getString("work_place"));
                username.setText(json.getString("username"));
                password.setText(json.getString("password"));

                fname.setText(json.getString("name"));
                fdesignation.setText(json.getString("designation"));
                img_url=json.getString("img_link");



                Picasso.with(UserProfile.this).load(img_url).placeholder(R.drawable.profile).error(R.drawable.profile).into(pro_img);
//                roaster_id.setVisibility(View.GONE);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            }
        }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_menu1,menu);

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
                Intent intent=new Intent(this,Profile.class);
//                intent.putExtra("name",name1);
//                intent.putExtra("designation",designation1);
                startActivity(intent);
                finish();

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
    }

