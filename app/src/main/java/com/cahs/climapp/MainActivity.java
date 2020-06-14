package com.cahs.climapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ajts.androidmads.fontutils.FontUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    public static String baseUrl = "http://api.openweathermap.org/";
    public static String apiKey = "2e65127e909e178d0af311a81f39948c";
    public static String lat = "4.714873";
    public static String lon = "-74.108520";
    private TextView weatherData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Instantiate textView
        weatherData = findViewById(R.id.textView);
        //Customizar el texto que contiene los datos climáticos
        Typeface typeface = Typeface.createFromAsset(getAssets(), "Lato-Bold.ttf");
        FontUtils fontUtils = new FontUtils();
        fontUtils.applyFontToView(weatherData, typeface);
        //Instanciar el botón
        //Agregar un clickListener para detectar el click sobre este botón
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Llame al método que contiene la lógica
                getCurrentData();
            }
        });
    }
        //Parte funcional de la lógica
    void getCurrentData() {
        //Crear un objeto Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        //Llamo a la interfaz WeatherService que hace la petición
        WeatherService service = retrofit.create(WeatherService.class);
        Call<WeatherResponse> call = service.getCurrentWeatherData(lat, lon, apiKey);
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(@NonNull Call<WeatherResponse> call, @NonNull Response<WeatherResponse> response) {
                if (response.code() == 200) {
                    //Procesar la respuesta
                    WeatherResponse weatherResponse = response.body();
                    assert weatherResponse != null;

                    String string = "País: " +
                            weatherResponse.sys.country +
                            "\n" +
                            "Temperatura: " +
                            weatherResponse.main.temp +
                            "\n" +
                            "Temperatura Mínima: " +
                            weatherResponse.main.temp_min +
                            "\n" +
                            "Temperatura Máxima: " +
                            weatherResponse.main.temp_max +
                            "\n" +
                            "Humedad: " +
                            weatherResponse.main.humidity +
                            "\n" +
                            "Presión: " +
                            weatherResponse.main.pressure;

                    weatherData.setText(string);
                }
            }

            @Override
            public void onFailure(@NonNull Call<WeatherResponse> call, @NonNull Throwable t) {
                weatherData.setText(t.getMessage());
            }
        });
    }
}
