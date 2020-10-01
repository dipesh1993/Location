package com.example.imagenew1;

import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.ParseException;
import java.util.List;

import static java.security.AccessController.getContext;


public class VisitsAdapter extends RecyclerView.Adapter<VisitsAdapter.VisitViewHolder> {


    private Context mCtx;
    private List<Visit> visitList;
    ProgressDialog progressDialog;
    public VisitsAdapter(Context mCtx, List<Visit> visitList) {

        this.mCtx = mCtx;
        this.visitList = visitList;

    }


    @Override
    public VisitViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.visits_list, null);

        return new VisitViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(VisitViewHolder holder, final int position) {

        Visit product = visitList.get(position);
        DateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat newFormat = new SimpleDateFormat("dd-MM-yyyy ");

        String dateStr = null;
        try {
            dateStr = newFormat.format(oldFormat.parse(product.getDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }


            holder.textViewDate.setText("दिनांक :" + dateStr);
            holder.textViewPlace.setText(product.getPlace_name());
            holder.textViewVillage.setText(product.getPlace_village());
            final Integer roaster_id = product.getRoster();
            final String place_name = product.getPlace_name();
            final String place_village = product.getPlace_village();

            final String finalDateStr = dateStr;
            holder.visit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //code specific to first list item
                    Intent intent = new Intent(mCtx, MainActivity.class);
                    intent.putExtra("date", finalDateStr);
                    intent.putExtra("roaster_id", roaster_id.toString());
                    intent.putExtra("place_name", place_name);
                    intent.putExtra("place_village", place_village);
                    mCtx.startActivity(intent);
//                    ((Activity)mCtx).finish();

                }
            });
        }

    @Override
    public int getItemCount() {
        return visitList.size();
    }

    static class VisitViewHolder extends RecyclerView.ViewHolder {

        TextView textViewDate, textViewPlace, textViewVillage;
        public Button visit;

        public VisitViewHolder(View itemView) {
            super(itemView);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewPlace = itemView.findViewById(R.id.textViewPlace);
            textViewVillage = itemView.findViewById(R.id.textViewVillage);
//            textViewPrice = itemView.findViewById(R.id.textViewPrice);
//            relativeLayout = itemView.findViewById(R.id.relativeLayout);
            visit = itemView.findViewById(R.id.btn);

        }
    }

}