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


public class SchoolAdapter extends RecyclerView.Adapter<SchoolAdapter.VisitViewHolder> {


    private Context mCtx;

    private List<SchoolData> visitList;

    public SchoolAdapter(Context mCtx, List<SchoolData> visitList) {

        this.mCtx = mCtx;
        this.visitList = visitList;

    }


    @Override
    public VisitViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.schoo_list, null);

        return new VisitViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(VisitViewHolder holder, final int position) {

        SchoolData product = visitList.get(position);



        holder.textViewDate.setText(product.getSchool_name());
//        holder.textViewPlace;
        holder.textViewVillage.setText(product.getPlaceName());
        final Integer s_id = product.getS_id();
        final String school_name = product.getSchool_name();
        final String place = product.getPlaceName();

        holder.visit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //code specific to first list item
                Intent intent = new Intent(mCtx, MainActivity.class);
                intent.putExtra("roaster_id", s_id.toString());
                intent.putExtra("place_name", school_name);
                intent.putExtra("place_village", place);
                mCtx.startActivity(intent);
                ((Activity)mCtx).finish();


            }
        });
    }

    @Override
    public int getItemCount() {
        return visitList.size();
    }

    static class VisitViewHolder extends RecyclerView.ViewHolder {

        TextView textViewDate, textViewPlace, textViewVillage;
        ProgressBar progressBar;
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