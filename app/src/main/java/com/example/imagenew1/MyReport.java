//package com.example.imagenew1;
//import android.content.Context;
//import android.content.Intent;
//import android.support.design.widget.TabLayout;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentTransaction;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.widget.FrameLayout;
//import java.util.HashMap;
//
//public class MyReport extends AppCompatActivity {
//
//    FrameLayout frameLayout;
//    TabLayout tabLayout;
//    Context context;
//    FragmentManager fragmentManager;
//    Fragment fragment=null;
//    FragmentTransaction fragmentTransaction;
//    SessionManager sessionManager;
//    String mId,name1,designation1;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_my_roaster);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        context=this.getApplicationContext();
//        sessionManager=new SessionManager(this);
//        HashMap<String,String> user=sessionManager.getUserDetail();
//        mId=user.get(sessionManager.ID);
//        name1=getIntent().getExtras().getString("name");
//        designation1=getIntent().getExtras().getString("designation");
//
//        frameLayout=(FrameLayout)findViewById(R.id.simpleFrameLayout);
//        tabLayout=(TabLayout)findViewById(R.id.simpleTabLayout);
////        ActionBar actionBar=((MainActivity ) fragment.getActivity()).getSupportActionBar();
//
//        TabLayout.Tab firstTab=tabLayout.newTab();
//        firstTab.setText("शालेय पोषण");
//        firstTab.setIcon(R.drawable.newsicon);
//        tabLayout.addTab(firstTab);
//
//        TabLayout.Tab secondTab=tabLayout.newTab();
//        secondTab.setText("रोस्टर");
//        secondTab.setIcon(R.drawable.visit);
//        tabLayout.addTab(secondTab);
//
//
//
//        fragment=new PoshanReport();
//        fragmentManager = getSupportFragmentManager();
//        fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.simpleFrameLayout, fragment);
//        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//        fragmentTransaction.commit();
//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//
//                switch (tab.getPosition())
//                {
//                    case 0:
//                        fragment=new PoshanReport();
//                        break;
//                    case 1:
//                        fragment=new RosterReport();
//                        break;
//                }
//
//                FragmentManager fm=getSupportFragmentManager();
//                FragmentTransaction ft=fm.beginTransaction();
//                ft.replace(R.id.simpleFrameLayout,fragment);
//                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//                ft.commit();
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//
//        });
//
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.user_menu,menu);
//
//        return true;
//    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id=item.getItemId();
//        switch (id)
//        {
//            case R.id.action_logout:
//                sessionManager.logout();
//                break;
//            case R.id.action_profile:
//                Intent intent=new Intent(this,UserProfile.class);
//                intent.putExtra("name",name1);
//                intent.putExtra("designation",designation1);
//                startActivity(intent);
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//        return true;
//    }
//    @Override
//    public boolean onSupportNavigateUp(){
//        finish();
//        return true;
//    }
//}
//


package com.example.imagenew1;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyReport extends AppCompatActivity {

    //this is the JSON Data URL
    //make sure you are using the correct ip else it will not work
    private static final String URL_PRODUCTS = "http://zp.dpcjalgaon.com/android/myreport.php";
//    private static final String URL_PRODUCTS = "http://192.168.43.129/cameraloc1/myreport.php";

    //a list to store all the products

    List<Reporter> reportList;
    SessionManager sessionManager;
    String mId,name1,designation1;
    //the recyclerview
    RecyclerView recyclerView;
    TextView textView;
    SwipeRefreshLayout pulltoRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myreport);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();
        textView = findViewById(R.id.textView);
        pulltoRefresh = findViewById(R.id.pullToRefresh);
        HashMap<String, String> user = sessionManager.getUserDetail();
        mId = user.get(sessionManager.ID);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getting the recyclerview from xml
        recyclerView = findViewById(R.id.recylcerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2, GridLayout.VERTICAL, false));
        //initializing the productlist
        reportList = new ArrayList<>();
        name1 = getIntent().getExtras().getString("name");
        designation1 = getIntent().getExtras().getString("designation");
        //this method will fetch and parse json
        //to display it in recyclerview

        pulltoRefresh.post(new Runnable() {

            @Override
            public void run()
            {
                pulltoRefresh.setRefreshing(true);
                loadProducts();
            }
        });
        pulltoRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh() {
                loadProducts();
            }
        });
    }

    private void loadProducts() {

        pulltoRefresh.setRefreshing(true);
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

                        reportList.clear();
                            try {
                                //converting the string to json array object
                                JSONArray array = new JSONArray(response);

                                //traversing through all the object
                                for (int i = 0; i < array.length(); i++) {

                                    //getting product object from json array
                                    JSONObject report = array.getJSONObject(i);

                                    //adding the product to product list
                                    reportList.add(new Reporter(
                                            report.getInt("roaster_id"),
                                            report.getString("date"),
                                            report.getString("place_name"),
                                            report.getString("place_village")
                                    ));
                                }

                                //creating adapter object and setting it to recyclerview
                                ReportAdapter adapter = new ReportAdapter(MyReport.this, reportList);
                                if (adapter!=null) {
                                    if (adapter.getItemCount() > 0)
                                    {
                                        recyclerView.setAdapter(adapter);
                                        textView.setVisibility(View.GONE);
                                    }
                                    else
                                    {
                                        textView.setText("No Data Available....");
                                    }
                                }


                            } catch (JSONException e) {

                                e.printStackTrace();
                            }
                        pulltoRefresh.setRefreshing(false);
                        }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pulltoRefresh.setRefreshing(false);
                    }
                })
        {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("user_id", mId);
                return MyData;
            }
        };

        //adding our stringrequest to queue
        Volley.newRequestQueue(this).add(stringRequest);
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
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
