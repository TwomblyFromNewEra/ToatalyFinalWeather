package com.example.findweatherapp;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, GetDataFromInternet.AsyncResponse {

    private static final String TAG = "MainActivity";

    private Button searchButton;
    private EditText searchField;
    private TextView cityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchField=findViewById(R.id.searchField);

        cityName=findViewById(R.id.CityName);

        searchButton=findViewById(R.id.buttonSearch);
        searchButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

            URL url=buildUrl(searchField.getText().toString());

            cityName.setText(searchField.getText().toString());

            new GetDataFromInternet(this).execute(url);

    }

    private URL buildUrl (String city) {




            Uri builtUri= Uri.parse("http://api.weatherapi.com/v1/forecast.json?key=8feaf30b69074108bb9100826231605&q=" + city + "&days=4&aqi=no&alerts=no");
        URL url = null;

        try {
            url=new URL(builtUri.toString());

        } catch (MalformedURLException e){
            e.printStackTrace();
        }
        Log.d(TAG, "buildUrl: "+url);
        return url;

    }
    @SuppressLint("SetTextI18n")
    @Override
    public void proccessFinish(String output) {
        Log.d(TAG, "proccessFinish: "+ output);
        try {

            JSONObject resultJSON=new JSONObject(output);
            JSONObject weather=resultJSON.getJSONObject("current");
            JSONObject condition = weather.getJSONObject("condition");

            TextView temp=findViewById(R.id.CurTemp);
            String temp_C=weather.getString("temp_c");
            String temp_C_string=Float.toString(Float.parseFloat(temp_C));
            temp.setText(temp_C_string);

            TextView Condition = findViewById(R.id.condition);
            String condi = condition.getString("text");
            Condition.setText(condi);


            TextView MaxMinMain =  findViewById(R.id.MaxMinTEmp);
            JSONArray forecastDays = resultJSON.getJSONObject("forecast").getJSONArray("forecastday");


            TextView SunriseText = findViewById(R.id.MoonriseText);
            TextView SunsetText = findViewById(R.id.MoonsetText);
            TextView MoonphaseText = findViewById(R.id.MoonPhaseText);
            TextView illumination = findViewById(R.id.IllumText);


            String max1 = null ;   String min1 = null;
            String max2= null;     String min2= null;
            String max3= null;     String min3= null;
            String max4= null;     String min4= null;

            String dateTomorrow = null;
            String dayAfterTommorow= null;
            String ThirdDay= null;

            TextView Date1 = findViewById(R.id.DateTommorow);
            TextView Date2 = findViewById(R.id.DateAfterTommorow);
            TextView Date3 = findViewById(R.id.DateThirdDay);

            TextView MaxMin2 = findViewById(R.id.MaxMin2);
            TextView MaxMin3 = findViewById(R.id.MaxMin3);
            TextView MaxMin4 = findViewById(R.id.MaxMin4);

            ImageView FirstDayPng = findViewById(R.id.FirstDayPng);
            ImageView SecondDayPng = findViewById(R.id.SeconDayPng);
            ImageView ThirdDayPng = findViewById(R.id.ThirdDayPng);
            String weatherCondition = null;



            for (int i = 0; i < forecastDays.length() ; i++) {

                JSONObject forecastDay = forecastDays.getJSONObject(i);
                JSONObject day = forecastDay.getJSONObject("day");
                String maxTemp = day.getString("maxtemp_c");
                String minTemp = day.getString("mintemp_c");
                JSONObject dayCondition = day.getJSONObject("condition");
                weatherCondition =dayCondition.getString("text");
                FirstDayPng.setImageResource(GetImage.SetDrawable("Sunny"));

                String date = forecastDay.getString("date");
                JSONObject astro = forecastDay.getJSONObject("astro");
                String Sunset = astro.getString("sunset");
                String Sunrise = astro.getString("sunrise");
                String Moonphase = astro.getString("moon_phase");
                String Moonillum = astro.getString("moon_illumination");
                switch (i) {
                    case 0:
                        max1 = maxTemp;
                        min1 = minTemp;
                        SunriseText.setText(Sunrise);
                        SunsetText.setText(Sunset);
                        MoonphaseText.setText(Moonphase);
                        illumination.setText(Moonillum);
                case 1:
                    max2 = maxTemp;
                    min2 = minTemp;
                    dateTomorrow = date;
                    FirstDayPng.setImageResource(GetImage.SetDrawable(weatherCondition));

                    break;

                case 2:
                    max3 = maxTemp;
                    min3 = minTemp;
                    dayAfterTommorow = date;
                    SecondDayPng.setImageResource(GetImage.SetDrawable(weatherCondition));
                    break;

                case 3:
                    max4 = maxTemp;
                    min4 = minTemp;
                    ThirdDay = date;
                    ThirdDayPng.setImageResource(GetImage.SetDrawable(weatherCondition));
                    break;
            }
                }
                MaxMinMain.setText(max1 + "/" + min1);
                MaxMin2.setText(max2 +"/" + min2);
                MaxMin3.setText(max3 + "/" + min3);
                MaxMin4.setText(max4 + "/" + min4);

                Date1.setText(dateTomorrow);
                Date2.setText(dayAfterTommorow);
                Date3.setText(ThirdDay);

            }


        catch (JSONException e){
            e.printStackTrace();
        }
    }
}