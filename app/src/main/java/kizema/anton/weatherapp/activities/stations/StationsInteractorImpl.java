package kizema.anton.weatherapp.activities.stations;

import android.util.Log;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kizema.anton.weatherapp.api.ApiConstants;
import kizema.anton.weatherapp.api.ApiEndpoint;
import kizema.anton.weatherapp.model.UserPrefs;
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

        UserPrefs prefs = UserPrefs.getPrefs();
        Map<String, String> data = new HashMap<>();
        data.put("lon", "" + prefs.lon);
        data.put("lat", "" + prefs.lat);

        Call<WeatherFiveDayList> call = apiService.getFiveDayForcast(data);
        call.enqueue(new Callback<WeatherFiveDayList>() {
            @Override
            public void onResponse(Call<WeatherFiveDayList> call, final Response<WeatherFiveDayList> response) {

                List<WeatherForcastDto> list = WeatherFiveDayList.getWeatherForcastDtos(response.body());
                listener.onComplete(list);
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
        UserPrefs prefs = UserPrefs.getPrefs();

        if (prefs.cityId.equals("")) {
            return null;
        }

        WeatherCityDto dto = WeatherCityDto.findById(prefs.cityId);
        if (dto != null) {
            List<WeatherForcastDto> list = WeatherForcastDto.findByCity(dto.cityId);
            return list;
        }

        return null;
    }
}
