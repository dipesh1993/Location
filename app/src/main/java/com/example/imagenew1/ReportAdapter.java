package com.example.imagenew1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.util.List;


public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder> {


    private Context mCtx;
    private List<Reporter> reportList;

    public ReportAdapter(Context mCtx, List<Reporter> reportList) {
        this.mCtx = mCtx;
        this.reportList = reportList;
    }


    @Override
    public ReportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.report_list, null);

        return new ReportViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(ReportViewHolder holder, final int position) {
        Reporter roaster = reportList.get(position);

//        //loading the image
        DateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat newFormat = new SimpleDateFormat("dd-MM-yyyy ");

        String dateStr = null;
        try {
            dateStr = newFormat.format(oldFormat.parse(roaster.getDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (dateStr!=null)
        {
            holder.textViewDate.setText("दिनांक :" + dateStr);

        }
        else
            {
                holder.textViewDate.setVisibility(View.GONE);
            }
            holder.textViewPlace.setText(roaster.getPlace_name());
            holder.textViewVillage.setText(roaster.getPlace_village());
//        holder.textViewRoast.setText(roaster.getRoasterId());
            final Integer roaster_id = roaster.getRoasterId();
            final String place_name = roaster.getPlace_name();
            final String place_village = roaster.getPlace_village();

            final String finalDateStr = dateStr;
            holder.report.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //code specific to first list item
                    Intent intent = new Intent(mCtx, Report.class);
                    intent.putExtra("date", finalDateStr);
                    intent.putExtra("roaster_id", roaster_id.toString());
                    intent.putExtra("place", place_name);
                    intent.putExtra("village", place_village);
                    mCtx.startActivity(intent);
                    ((Activity)mCtx).finish();

                }
            });
        }

    @Override
    public int getItemCount() {
        return reportList.size();
    }

    static class ReportViewHolder extends RecyclerView.ViewHolder {

        TextView textViewDate, textViewPlace, textViewVillage,textViewRoast;

        public RelativeLayout relativeLayout;
        public Button report;

        public ReportViewHolder(View itemView) {
            super(itemView);

            textViewDate = itemView.findViewById(R.id.textViewDate1);
            textViewPlace = itemView.findViewById(R.id.textViewPlace1);
            textViewVillage = itemView.findViewById(R.id.textViewVillage1);
//            textViewPrice = itemView.findViewById(R.id.textViewPrice);
//            relativeLayout = itemView.findViewById(R.id.relativeLayout);
            report = itemView.findViewById(R.id.btn1);

        }
    }

}