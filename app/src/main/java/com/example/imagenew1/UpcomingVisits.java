package com.example.imagenew1;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import java.util.HashMap;

public class UpcomingVisits extends AppCompatActivity {

    FrameLayout frameLayout;
    TabLayout tabLayout;
    Context context;
    FragmentManager fragmentManager;
    Fragment fragment=null;
    FragmentTransaction fragmentTransaction;
    SessionManager sessionManager;
    String mId,name1,designation1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_roaster);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context=this.getApplicationContext();
        sessionManager=new SessionManager(this);
        HashMap<String,String> user=sessionManager.getUserDetail();
        mId=user.get(sessionManager.ID);
        name1=getIntent().getExtras().getString("name");
        designation1=getIntent().getExtras().getString("designation");

        frameLayout=(FrameLayout)findViewById(R.id.simpleFrameLayout);
        tabLayout=(TabLayout)findViewById(R.id.simpleTabLayout);
//        ActionBar actionBar=((MainActivity ) fragment.getActivity()).getSupportActionBar();

        TabLayout.Tab firstTab=tabLayout.newTab();
        firstTab.setText("School");
        firstTab.setIcon(R.drawable.newsicon);
        tabLayout.addTab(firstTab);

        TabLayout.Tab secondTab=tabLayout.newTab();
        secondTab.setText("Roster");
        secondTab.setIcon(R.drawable.visit);
        tabLayout.addTab(secondTab);



        fragment=new School();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.simpleFrameLayout, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                switch (tab.getPosition())
                {
                    case 0:
                        fragment=new School();
                        break;
                    case 1:
                        fragment=new Roster();
                        break;
                }

                FragmentManager fm=getSupportFragmentManager();
                FragmentTransaction ft=fm.beginTransaction();
                ft.replace(R.id.simpleFrameLayout,fragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });

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
}
