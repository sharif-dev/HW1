package com.example.weatherforecast.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherforecast.R;
import com.example.weatherforecast.model.WeatherCondition;

import java.util.List;

public class DarkSkyViewAdapter extends RecyclerView.Adapter<DarkSkyViewAdapter.MyViewHolder> {
    private Context mContext;
    private List<WeatherCondition> mData;

    public DarkSkyViewAdapter(Context mContext, List<WeatherCondition> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView day;
        TextView date;
        TextView summary;
        TextView temperatureMax;
        TextView temperatureMin;
        TextView humidity;
        TextView pressure;
        LinearLayout linearLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            day = itemView.findViewById(R.id.day);
            date = itemView.findViewById(R.id.date);
            summary = itemView.findViewById(R.id.summary);
            temperatureMax = itemView.findViewById(R.id.max);
            temperatureMin = itemView.findViewById(R.id.min);
            humidity = itemView.findViewById(R.id.humidity);
            pressure = itemView.findViewById(R.id.pressure);
            linearLayout = itemView.findViewById(R.id.linearLayout);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.weather_row_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.day.setText(mData.get(position).getDay());
        holder.date.setText(mData.get(position).getDate());
        holder.summary.setText(mData.get(position).getSummary());
        holder.temperatureMax.setText(mData.get(position).getTemperatureMax());
        holder.temperatureMin.setText(mData.get(position).getTemperatureMin());
        holder.humidity.setText(mData.get(position).getHumidity());
        holder.pressure.setText(mData.get(position).getPressure());
        background(holder.linearLayout, position);
    }

    private void background(LinearLayout linearLayout, int position) {
        switch (mData.get(position).getIcon()) {
            case "clear-day":
                linearLayout.setBackgroundResource(R.drawable.clear_day);
                break;
            case "clear-night":
                linearLayout.setBackgroundResource(R.drawable.clear_night);
                break;
            case "rain":
                linearLayout.setBackgroundResource(R.drawable.rain);
                break;
            case "snow":
                linearLayout.setBackgroundResource(R.drawable.snow);
                break;
            case "sleet":
                linearLayout.setBackgroundResource(R.drawable.sleet);
                break;
            case "fog":
                linearLayout.setBackgroundResource(R.drawable.fog);
                break;
            case "cloudy":
                linearLayout.setBackgroundResource(R.drawable.cloudy);
                break;
            case "partly-cloudy-day":
                linearLayout.setBackgroundResource(R.drawable.partly_cloudy_day);
                break;
            case "partly-cloudy-night":
                linearLayout.setBackgroundResource(R.drawable.partly_cloudy_night);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
