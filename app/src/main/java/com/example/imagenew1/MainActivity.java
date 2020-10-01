package com.example.imagenew1;

import android.content.Context;
import android.content.DialogInterface;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.net.HttpURLConnection;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.net.Uri;
import java.io.InputStreamReader;
import java.io.OutputStream;
import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedWriter;
import java.util.Map;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.io.BufferedReader;
import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;
import android.util.Base64;

public class MainActivity extends AppCompatActivity  implements LocationListener{
    Button CaptureImageFromCamera, UploadImageToServer,goback;
    ImageView ImageView;
    TextView place_name;
    String img_caption,city,village,date1;

    TextView place_village;
    TextView textView,textView1,date,img,title;
    String img_long,img_lat,roaster_id;
    ProgressDialog progressDialog;
    Intent intent;
    public static final int RequestPermissionCode = 1;
    Bitmap bitmap;
    boolean check = true;
    String GetImageNameFromEditText;
    String ImageNameFieldOnServer = "img_caption";
    String ImagePathFieldOnServer = "img_path";
    String ImageLongitudeOnServer ="img_long";
    String ImageLatitudeOnServer ="img_lat";
    String ImagePlaceOnServer ="img_city";
    String ImageVillageOnServer ="img_village";
    String ImageRoasterIdOnServer ="img_roasterId";
    SessionManager sessionManager;

//    String ImageUploadPathOnSever = "https://jalgaonsports.in/nundurbar/up_img/capture_img_upload_to_server.php";
      String ImageUploadPathOnSever = "http://zp.dpcjalgaon.com/android/capture_img_upload_to_server.php";
//    String ImageUploadPathOnSever = "http://192.168.2.9/cameraloc1/capture_img_upload_to_server.php";
//    String ImageUploadPathOnSever = "http://192.168.43.129/cameraloc/capture_img_upload_to_server.php";

    private LocationManager locationManager;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        textView=findViewById(R.id.textView);
        textView1=findViewById(R.id.textView2);
        CaptureImageFromCamera = (Button)findViewById(R.id.button);
        ImageView = (ImageView)findViewById(R.id.imageView);
        UploadImageToServer = (Button) findViewById(R.id.button2);
        sessionManager=new SessionManager(this);
        place_name =(TextView)findViewById(R.id.img_title);
        date =(TextView)findViewById(R.id.date1);
        img=findViewById(R.id.img);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        place_village =(TextView)findViewById(R.id.img_description);
        if (getIntent().getExtras().getString("date")!=null)
        {
            date.setText(getIntent().getExtras().getString("date"));
        }
        else
        {

            date.setText(new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime()));
        }
        roaster_id=getIntent().getExtras().getString("roaster_id");
        place_name.setText(getIntent().getExtras().getString("place_name"));
        place_village.setText(getIntent().getExtras().getString("place_village"));
        city=getIntent().getExtras().getString("place_name");
        village=getIntent().getExtras().getString("place_village");


        if (date1==null)
        {
            date1=date.getText().toString();
        }
        else
        {
            date1=getIntent().getExtras().getString("date");
        }
        CheckEnableGPS();
        EnableRuntimePermissionToAccessCamera();

        CaptureImageFromCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                startActivityForResult(intent, 7);
            }
        });
        UploadImageToServer.setVisibility(View.GONE);

        UploadImageToServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img_caption=new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
                GetImageNameFromEditText = "IMG" +img_caption +".jpg";

                ImageUploadToServerFunction();
            }
        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);

        if (location != null) {
            onLocationChanged(location);
        }
        else{
            //This is what you need:
            locationManager.requestLocationUpdates(locationManager.NETWORK_PROVIDER, 1000, 0, this);
        }
    }
    private void CheckEnableGPS()
    {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, please enable !!")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id)
                    {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        finish();
                    }
                })
                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                        finish();

                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }



    // Star activity for result method to Set captured image on image view after click.
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 7 && resultCode == RESULT_OK ) {

//            Uri uri = data.getData();

            bitmap=(Bitmap)data.getExtras().get("data");

            // Adding captured image in bitmap.
//                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
//                bitmap= Bitmap.createScaledBitmap(bitmap, 700, 900, true);
            ImageView.setImageBitmap(bitmap);
            place_name.setVisibility(View.GONE);
            place_village.setVisibility(View.GONE);
            date.setVisibility(View.GONE);
            img.setVisibility(View.GONE);
//            textView.setVisibility(View.GONE);
            textView1.setVisibility(View.GONE);
            CaptureImageFromCamera.setVisibility(View.GONE);
            UploadImageToServer.setVisibility(View.VISIBLE);

        }

    }

    // Requesting runtime permission to access camera.
    public void EnableRuntimePermissionToAccessCamera(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                Manifest.permission.CAMERA))
        {

            // Printing toast message after enabling runtime permission.
            Toast.makeText(MainActivity.this,"CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CAMERA}, RequestPermissionCode);

        }
    }
    @Override
    public void onLocationChanged(Location location) {
        img_lat = Double.toString(location.getLatitude());
        img_long = Double.toString(location.getLongitude());
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
    // Upload captured image online on server function.
    public void ImageUploadToServerFunction(){

        ByteArrayOutputStream byteArrayOutputStreamObject ;

        byteArrayOutputStreamObject = new ByteArrayOutputStream();

        // Converting bitmap image to jpeg format, so by default image will upload in jpeg format.

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStreamObject);

        byte[] byteArrayVar = byteArrayOutputStreamObject.toByteArray();

        final String ConvertImage = Base64.encodeToString(byteArrayVar, Base64.DEFAULT);


        class AsyncTaskUploadClass extends AsyncTask<Void,Void,String> {

            @Override
            protected void onPreExecute() {

                super.onPreExecute();

                // Showing progress dialog at image upload time.
                progressDialog = ProgressDialog.show(MainActivity.this,"Image is Uploading","Please Wait",false,false);
            }

            @Override
            protected void onPostExecute(String string1) {

                progressDialog.dismiss();
                Toast.makeText(MainActivity.this,string1,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(),ShaleyPoshan.class);
                intent.putExtra("roaster_id",roaster_id);
                intent.putExtra("city",city);
                intent.putExtra("village",village);
                intent.putExtra("date",date1);

                startActivity(intent);
                finish();


            }

            @Override
            protected String doInBackground(Void... params) {

                ImageProcessClass imageProcessClass = new ImageProcessClass();
                HashMap<String,String> HashMapParams = new HashMap<String,String>();
                HashMapParams.put(ImageNameFieldOnServer, GetImageNameFromEditText);
                HashMapParams.put(ImagePathFieldOnServer, ConvertImage);
                HashMapParams.put(ImageLongitudeOnServer, img_long);
                HashMapParams.put(ImageLatitudeOnServer, img_lat);
                HashMapParams.put(ImagePlaceOnServer, city);
                HashMapParams.put(ImageVillageOnServer, village);
                HashMapParams.put(ImageRoasterIdOnServer, roaster_id);

                String FinalData = imageProcessClass.ImageHttpRequest(ImageUploadPathOnSever, HashMapParams);

                return FinalData;
            }
        }
        AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();

        AsyncTaskUploadClassOBJ.execute();
    }

    public class ImageProcessClass{

        public String ImageHttpRequest(String requestURL,HashMap<String, String> PData) {

            StringBuilder stringBuilder = new StringBuilder();

            try {

                URL url;
                HttpURLConnection httpURLConnectionObject ;
                OutputStream OutPutStream;
                BufferedWriter bufferedWriterObject ;
                BufferedReader bufferedReaderObject ;
                int RC ;

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

                    while ((RC2 = bufferedReaderObject.readLine()) != null){

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

                stringBuilderObject.append(URLEncoder.encode(KEY.getKey(), "UTF-8"));

                stringBuilderObject.append("=");

                stringBuilderObject.append(URLEncoder.encode(KEY.getValue(), "UTF-8"));
            }

            return stringBuilderObject.toString();
        }

    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {

            case RequestPermissionCode:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

//                    Toast.makeText(MainActivity.this,"Permission Granted, Now your application can access CAMERA.", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(MainActivity.this,"Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

}














































//
//
//            @Override
//            protected void onPreExecute() {
//
//                super.onPreExecute();
//
//                // Showing progress dialog at image upload time.
//                progressDialog = ProgressDialog.show(MainActivity.this,"Image is Uploading","Please Wait",false,false);
//            }
//            @Override
//            protected void onPostExecute(String string) {
//
//                super.onPostExecute(string);
//
//                // Dismiss the progress dialog after done uploading.
//                progressDialog.dismiss();
//                Toast.makeText(MainActivity.this,string,Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent();
//                intent.setClass(getApplicationContext(),RoasterUpdate.class);
//                intent.putExtra("roaster_id",roaster_id);
//                intent.putExtra("city",city);
//                intent.putExtra("village",village);
//                intent.putExtra("date",date1);
//
//                startActivity(intent);
//                finish();
//                // Printing uploading success message coming from server on android app.
//
//
//                // Setting image as transparent after done uploading.
//
//
//
//            }
//
//            @Override
//            protected String doInBackground(Void... params) {
//
//                ImageProcessClass imageProcessClass = new ImageProcessClass();
//                HashMap<String,String> HashMapParams = new HashMap<String,String>();
//                HashMapParams.put(ImageNameFieldOnServer, GetImageNameFromEditText);
//                HashMapParams.put(ImagePathFieldOnServer, ConvertImage);
//                HashMapParams.put(ImageLongitudeOnServer, img_long);
//                HashMapParams.put(ImageLatitudeOnServer, img_lat);
//                HashMapParams.put(ImagePlaceOnServer, city);
//                HashMapParams.put(ImageVillageOnServer, village);
//                HashMapParams.put(ImageRoasterIdOnServer, roaster_id);
//
//                String FinalData = imageProcessClass.ImageHttpRequest(ImageUploadPathOnSever, HashMapParams);
//
//                return FinalData;
//            }
//        }
//        AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();
//
//        AsyncTaskUploadClassOBJ.execute();
//    }
//
//
//
//    public class ImageProcessClass{
//
//        public String ImageHttpRequest(String requestURL,HashMap<String, String> PData) {
//
//            StringBuilder stringBuilder = new StringBuilder();
//
//            try {
//
//                URL url;
//                HttpURLConnection httpURLConnectionObject ;
//                OutputStream OutPutStream;
//                BufferedWriter bufferedWriterObject ;
//                BufferedReader bufferedReaderObject ;
//                int RC ;
//
//                url = new URL(requestURL);
//
//                httpURLConnectionObject = (HttpURLConnection) url.openConnection();
//
//                httpURLConnectionObject.setReadTimeout(25000);
//
//                httpURLConnectionObject.setConnectTimeout(25000);
//
//                httpURLConnectionObject.setRequestMethod("POST");
//
//                httpURLConnectionObject.setDoInput(true);
//
//                httpURLConnectionObject.setDoOutput(true);
//
//                OutPutStream = httpURLConnectionObject.getOutputStream();
//
//                bufferedWriterObject = new BufferedWriter(
//
//                        new OutputStreamWriter(OutPutStream, "UTF-8"));
//
//                bufferedWriterObject.write(bufferedWriterDataFN(PData));
//
//                bufferedWriterObject.flush();
//
//                bufferedWriterObject.close();
//
//                OutPutStream.close();
//
//                RC = httpURLConnectionObject.getResponseCode();
//
//                if (RC == HttpsURLConnection.HTTP_OK) {
//
//                    bufferedReaderObject = new BufferedReader(new InputStreamReader(httpURLConnectionObject.getInputStream()));
//
//                    stringBuilder = new StringBuilder();
//
//                    String RC2;
//
//                    while ((RC2 = bufferedReaderObject.readLine()) != null){
//                        stringBuilder.append(RC2);
//                    }
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return stringBuilder.toString();
//        }
//
//        private String bufferedWriterDataFN(HashMap<String, String> HashMapParams) throws UnsupportedEncodingException {
//
//            StringBuilder stringBuilderObject;
//
//            stringBuilderObject = new StringBuilder();
//
//            for (Map.Entry<String, String> KEY : HashMapParams.entrySet()) {
//                if (check)
//                    check = false;
//                else
//                    stringBuilderObject.append("&");
//
//                stringBuilderObject.append(URLEncoder.encode(KEY.getKey(), "UTF-8"));
//
//                stringBuilderObject.append("=");
//
//                stringBuilderObject.append(URLEncoder.encode(KEY.getValue(), "UTF-8"));
//            }
//            return stringBuilderObject.toString();
//        }
//    }
//    @Override
//    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {
//
//        switch (RC) {
//
//            case RequestPermissionCode:
//
//                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED)
//                {
//
////                    Toast.makeText(MainActivity.this,"Permission Granted, Now your application can access CAMERA.", Toast.LENGTH_SHORT).show();
//                }
//                else
//                    {
//
//                    Toast.makeText(MainActivity.this,"Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_LONG).show();
//
//                }
//                break;
//        }
//    }
