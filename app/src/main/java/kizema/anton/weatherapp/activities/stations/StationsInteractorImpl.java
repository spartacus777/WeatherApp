package kizema.anton.weatherapp.activities.stations;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kizema.anton.weatherapp.api.ApiConstants;
import kizema.anton.weatherapp.api.ApiEndpoint;
import kizema.anton.weatherapp.model.WeatherCityDto;
import kizema.anton.weatherapp.model.WeatherFiveDayList;
import kizema.anton.weatherapp.model.WeatherForcastDto;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StationsInteractorImpl implements StationsInteractor {

    private Retrofit retrofit;

    private ApiEndpoint apiService;

    public StationsInteractorImpl() {

        OkHttpClient.Builder httpClient =
                new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                HttpUrl originalHttpUrl = original.url();

                HttpUrl url = originalHttpUrl.newBuilder()
                        .addQueryParameter("appid", ApiConstants.WEATHER_APP_ID)
                        .build();

                // Request customization: add request headers
                Request.Builder requestBuilder = original.newBuilder()
                        .url(url);

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        retrofit = new Retrofit.Builder()
                .client(httpClient.build())
                .baseUrl("http://api.openweathermap.org")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiEndpoint.class);
    }

    @Override
    public void loadData(final OnCompletion listener) {

        Log.e("RRR", " ===== LOAD DATA ===== ");

        Map<String, String> data = new HashMap<>();
        data.put("lon", "139");
        data.put("lat", "35");

        Call<WeatherFiveDayList> call = apiService.getFiveDayForcast(data);
        call.enqueue(new Callback<WeatherFiveDayList>() {
            @Override
            public void onResponse(Call<WeatherFiveDayList> call, final Response<WeatherFiveDayList> response) {

                /**
                 * TODO remove - emulating long-timed job
                 */
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        response.body().save();


                        Log.d("RRR", "City : " + response.body().getCity().getName());
                        Log.d("RRR", "Lat : " + response.body().getCity().getCoord().getLat());

                        WeatherCityDto dto = new WeatherCityDto();
                        dto.cityId = response.body().getCity().getId();
                        dto.name = response.body().getCity().getName();
                        dto.lat = response.body().getCity().getCoord().getLat();
                        dto.lon = response.body().getCity().getCoord().getLon();
                        dto.timeUpdate = System.currentTimeMillis();

                        List<WeatherForcastDto> list = new ArrayList<>();

                        for (WeatherFiveDayList.WeatherUpdate w : response.body().getWeatherUpdateList()){
                            WeatherForcastDto weather = new WeatherForcastDto();
                            weather.cityDto = dto;
                            weather.description = w.getWeatherIcons().get(0).getDescription();
                            weather.icon = w.getWeatherIcons().get(0).getIcon();

                            weather.temp = w.getMain().getTemp();
                            weather.temp_max = w.getMain().getTemp_max();
                            weather.temp_min = w.getMain().getTemp_min();

                            weather.time = w.getDate();

                            list.add(weather);
                        }

                        listener.onComplete(list);
                        Log.d("RRR", "Loaded");
                    }
                }, 2000);

            }

            @Override
            public void onFailure(Call<WeatherFiveDayList> call, Throwable t) {
                Log.d("RR", t + call.toString());

                listener.onError();
            }
        });
    }

    @Override
    public List<WeatherForcastDto> loadDataFromDB() {
        return null;
    }
}
