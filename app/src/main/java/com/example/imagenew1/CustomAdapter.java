package com.example.imagenew1;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.content.Intent.getIntent;

public class CustomAdapter extends BaseAdapter
{
    Context context;
    String date[];
    String place[];
    String village[];
    String status[];
    String user_id;
    String roaster_id[];
    LayoutInflater inflter;


    public CustomAdapter(Context applicationContext, String[] date, String[] place, String[] village, String[] status, String user_id,String[] roaster_id) {
        this.context = applicationContext;
        this.date = date;
        this.place = place;
        this.village=village;
        this.status=status;
        this.user_id=user_id;
        this.roaster_id=roaster_id;

        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount()
    {
        return date.length;

    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        view = inflter.inflate(R.layout.activity_listview, null);
        final TextView date1 = (TextView) view.findViewById(R.id.textView);
        final TextView place1 = (TextView) view.findViewById(R.id.textView3);
        final TextView village1 = view.findViewById(R.id.textView2);
        final TextView roaster1 = view.findViewById(R.id.tv);
        final TextView status1 = view.findViewById(R.id.textView4);
        final ImageView img = view.findViewById(R.id.imageView9);
//        date1.setText( "दिनांक :"+date[i]);
//        place1.setText("शहर :" + place[i]);
//        village1.setText("गावं :"+village[i]);
//        status1.setText(status[i]);
//        roaster1.setText(roaster_id[i]);
//        roaster1.setVisibility(View.GONE);
        String text = "<b>दिनांक :</b>" + date[i];
        date1.setText( Html.fromHtml(text));
        final String date2=date[i];
        String text1 = "<b>शहर :</b>" + place[i];
        place1.setText(Html.fromHtml(text1));
        final String place2=place[i];
        String text2 = "<b>गावं/ठिकाण :</b>" + village[i];
        final String village2=village[i];
        village1.setText(Html.fromHtml(text2));
        status1.setText(status[i]);
        roaster1.setText(roaster_id[i]);
        roaster1.setVisibility(View.GONE);
        if(!status[i].equals("visited"))
        {
            status1.setText("Not Visited");
            img.setBackgroundResource(R.drawable.wrong);
        }
        else
        {
            status1.setText("Visited");
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
//                int position=(Integer)v.getTag();
                String roaster_id= (String) roaster1.getText();
                String date=  date2;
                String place=  place2;
                String village=  village2;
                if(status[i].equals("visited") )
                {

                    Intent intent = new Intent(context, Report.class);
                    intent.putExtra("roaster_id", roaster_id);
                    intent.putExtra("date", date);
                    intent.putExtra("place", place);
                    intent.putExtra("village", village);
//                    intent.putExtra("user_id", user_id);
                    context.startActivity(intent);

                }
                else
                {
                    Toast.makeText(context,"Not Visited Yet",Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }
}