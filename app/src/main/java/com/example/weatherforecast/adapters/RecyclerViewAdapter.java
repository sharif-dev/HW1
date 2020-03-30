package com.example.weatherforecast.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherforecast.R;
import com.example.weatherforecast.model.City;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Context mContext;
    private List<City> mData;

    public RecyclerViewAdapter(Context mContext, List<City> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.city_row_item, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.city_name.setText(mData.get(position).getName());
        holder.city_longitude.setText(mData.get(position).getLongitude());
        holder.city_latitude.setText(mData.get(position).getLatitude());

        if (mData.get(position).getTxt_and_progressBar_visibility()) {
            holder.wait_txt.setVisibility(View.VISIBLE);
            holder.wait_p.setVisibility(View.VISIBLE);
        } else {
            holder.wait_txt.setVisibility(View.INVISIBLE);
            holder.wait_p.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView city_name;
        TextView city_longitude;
        TextView city_latitude;
        TextView wait_txt;
        ProgressBar wait_p;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            city_name = itemView.findViewById(R.id.name);
            city_longitude = itemView.findViewById(R.id.longitude);
            city_latitude = itemView.findViewById(R.id.latitude);
            wait_txt = itemView.findViewById(R.id.wait);
            wait_p = itemView.findViewById(R.id.pBar);

        }
    }

}
