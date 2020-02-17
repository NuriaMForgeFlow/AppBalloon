package com.example.appballoon.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appballoon.Common.Common;
import com.example.appballoon.Documentation;
import com.example.appballoon.Menu;
import com.example.appballoon.Model.WeatherForecastResult;
import com.example.appballoon.R;
import com.example.appballoon.WeatherActivity;
import com.squareup.picasso.Picasso;

import static com.example.appballoon.WeatherActivity.mSQLiteHelperWeather;

public class WeatherForecastAdapter  extends RecyclerView.Adapter<WeatherForecastAdapter.MyViewHolder> {

    Context context;
    WeatherForecastResult weatherForecastResult;
    public static  String d_temperature,d_description,d_date,d_wind,d_pressure,d_city,d_coord;

    public WeatherForecastAdapter(Context context, WeatherForecastResult weatherForecastResult) {
        this.context = context;
        this.weatherForecastResult = weatherForecastResult;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(context).inflate(R.layout.item_weather_forecast,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Picasso.get().load(new StringBuilder("https://openweathermap.org/img/w/")
                .append(weatherForecastResult.list.get(position).weather.get(0).getIcon())
                .append(".png").toString()).into(holder.img_weather);

        String date_time= Common.convertUnixToDate(weatherForecastResult
                .list.get(position).dt);

        holder.txt_date_time.setText(new StringBuilder(date_time));

        holder.txt_description.setText(new StringBuilder(weatherForecastResult.list.get(position)
        .weather.get(0).getDescription()));

        int temperature= (int) weatherForecastResult.list.get(position).main.getTemp();

        holder.txt_temperature.setText(new StringBuilder(String.valueOf(temperature)).append("°C"));

        holder.txt_pressure.setText(new StringBuilder(String.valueOf(weatherForecastResult.list.get(position).main.getPressure())));

        holder.txt_wind.setText(new StringBuilder(String.valueOf(weatherForecastResult.list.get(position).wind.toString())));

    }

    @Override
    public int getItemCount() {
        return weatherForecastResult.list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView txt_date_time,txt_description,txt_temperature,txt_wind,txt_pressure;
        ImageView img_weather;
        LinearLayout weather_item;

        public MyViewHolder(View itemView){
            super (itemView);

            img_weather=(ImageView) itemView.findViewById(R.id.img_weather);
            txt_date_time=(TextView) itemView.findViewById(R.id.txt_date);
            txt_description=(TextView) itemView.findViewById(R.id.txt_description);
            txt_temperature=(TextView) itemView.findViewById(R.id.txt_temperature);
            txt_wind=(TextView)itemView.findViewById(R.id.txt_wind);
            txt_pressure=(TextView)itemView.findViewById(R.id.txt_pressure);
            weather_item=(LinearLayout)itemView.findViewById(R.id.weather_item);


            //Add wind information
        }

    }

}
