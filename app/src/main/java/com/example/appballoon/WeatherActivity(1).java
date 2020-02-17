package com.example.appballoon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appballoon.Adapter.WeatherForecastAdapter;
import com.example.appballoon.Common.Common;
import com.example.appballoon.DBHelper.SQLiteHelper;
import com.example.appballoon.DBHelper.SQLiteHelperWeather;
import com.example.appballoon.Model.WeatherForecastResult;
import com.example.appballoon.Retrofit.IOpenWeatherInterface;
import com.example.appballoon.Retrofit.RetrofitClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.label305.asynctask.SimpleAsyncTask;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;
import com.example.appballoon.Common.Common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

import static com.example.appballoon.Common.Common.convertUnixToDate;

public class WeatherActivity extends AppCompatActivity {
    private List<String> lstCities;
    private MaterialSearchBar searchBar;
    ProgressBar loading;
    LinearLayout weather_panel;
    CompositeDisposable compositeDisposable;
    IOpenWeatherInterface mService;
    public static Button btn_save;
    public static SQLiteHelperWeather mSQLiteHelperWeather;
    Toolbar toolbar;
    TextView txt_city_name, txt_geo_coord;
    public static String d_city,d_coord;
    RecyclerView recycler_forecast;
    WeatherForecastResult weatherForecastResult;



    public WeatherActivity() {
        compositeDisposable = new CompositeDisposable();
        Retrofit retrofit = RetrofitClient.getInstance();
        mService=retrofit.create(IOpenWeatherInterface.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        txt_city_name= (TextView) findViewById(R.id.txt_city_name);
        txt_geo_coord= (TextView) findViewById(R.id.txt_geo_coord);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Weather Forecast");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Menu.class));
            }
        });
        recycler_forecast=(RecyclerView) findViewById(R.id.recycler_forecast);
        recycler_forecast.setHasFixedSize(true);
        recycler_forecast.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        weather_panel=(LinearLayout) findViewById(R.id.weather_panel);
        loading=(ProgressBar)findViewById(R.id.loading);
        searchBar=(MaterialSearchBar)findViewById(R.id.searchBar);
        searchBar.setEnabled(false);
        btn_save=findViewById(R.id.btn_save_w);
        new WeatherActivity.LoadCitites().execute();

        mSQLiteHelperWeather = new SQLiteHelperWeather(this,"WEATHER1.sqlite",null,1);
        mSQLiteHelperWeather.queryData("CREATE TABLE IF NOT EXISTS WEATHER1(id INTEGER PRIMARY KEY, date VARCHAR, place VARCHAR, coordinates VARCHAR, description VARCHAR, temperature VARCHAR, wind VARCHAR, pressure VARCHAR)");
    }




    private class LoadCitites extends SimpleAsyncTask<List<String>> {
        @Override
        protected List<String> doInBackgroundSimple() {
            lstCities=new ArrayList<>();
            try{
                StringBuilder builder=new StringBuilder();
                InputStream is=getResources().openRawResource(R.raw.cities_spain);
                InputStreamReader reader =new InputStreamReader(is);
                BufferedReader in = new BufferedReader(reader);

                String readed;
                while((readed=in.readLine())!=null)
                    builder.append(readed);
                lstCities=new Gson().fromJson(builder.toString(),new TypeToken<List<String>>(){}.getType());

            } catch (IOException e) {
                e.printStackTrace();
            }
            return lstCities;
        }

        @Override
        protected void onSuccess(final List<String> listCity) {
            super.onSuccess(listCity);

            searchBar.setEnabled(true);
            searchBar.addTextChangeListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    List<String> suggest=new ArrayList<>();
                    for(String search : listCity)
                    {
                        if(search.toLowerCase().contains(searchBar.getText().toLowerCase()))
                            suggest.add(search);
                    }
                    searchBar.setLastSuggestions(suggest);

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
                @Override
                public void onSearchStateChanged(boolean enabled) {

                }

                @Override
                public void onSearchConfirmed(CharSequence text) {
                    getForecastWeatherInformation(text.toString());

                    searchBar.setLastSuggestions(listCity);

                }

                @Override
                public void onButtonClicked(int buttonCode) {

                }
            });

            searchBar.setLastSuggestions(listCity);
            loading.setVisibility(View.GONE);

        }
    }



    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }



    private void getForecastWeatherInformation(String cityName) {

        compositeDisposable.add(mService.getWeatherByCity(cityName,
                Common.APP_ID,
                "metric")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WeatherForecastResult>() {
                    @Override
                    public void accept(WeatherForecastResult weatherForecastResult) throws Exception {
                        displayForecastWeather (weatherForecastResult);
                        btn_save.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                for(int position=0; position<=8; position++) {
                                    try {
                                        mSQLiteHelperWeather.insertData(
                                                convertUnixToDate(weatherForecastResult.list.get(position).dt),
                                                weatherForecastResult.city.name,
                                                weatherForecastResult.city.coord.toString(),
                                                weatherForecastResult.list.get(position).weather.get(0).getDescription(),
                                                String.valueOf(weatherForecastResult.list.get(position).main.getTemp()),
                                                weatherForecastResult.list.get(position).wind.toString(),
                                                String.valueOf(weatherForecastResult.list.get(position).main.getPressure())
                                        );
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }
                                startActivity(new Intent(WeatherActivity.this, Documentation.class));
                            }

                        });
                        //Display panel
                        weather_panel.setVisibility(View.VISIBLE);
                        hideKeyboard();
                        loading.setVisibility(View.GONE);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d("ERROR",""+throwable.getMessage());
                    }
                })


        );

    }







    private void displayForecastWeather(WeatherForecastResult weatherForecastResult) {
        txt_city_name.setText(new StringBuilder(weatherForecastResult.city.name+", "+weatherForecastResult.city.country));
        txt_geo_coord.setText(new StringBuilder(weatherForecastResult.city.coord.toString()));
        d_city=weatherForecastResult.city.name;
        d_coord=weatherForecastResult.city.coord.toString();
        WeatherForecastAdapter adapter = new WeatherForecastAdapter(getApplicationContext(),weatherForecastResult);
        recycler_forecast.setAdapter(adapter);
    }



    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        }

    }




}
