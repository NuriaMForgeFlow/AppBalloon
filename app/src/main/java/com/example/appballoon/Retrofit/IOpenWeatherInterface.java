package com.example.appballoon.Retrofit;

import com.example.appballoon.Model.WeatherForecastResult;
import com.example.appballoon.Model.WeatherResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IOpenWeatherInterface {
    @GET("weather")
    Observable<WeatherResult> getWeatherByLatLng(@Query("lat") String lat,
                                                 @Query("lon") String lng,
                                                 @Query("appid") String appid,
                                                 @Query("units") String unit);

    @GET("forecast")
    Observable<WeatherForecastResult> getWeatherByCity(@Query("q") String cityName,
                                                       @Query("appid") String appid,
                                                       @Query("units") String unit);


}
