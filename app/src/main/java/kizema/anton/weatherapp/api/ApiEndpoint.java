package kizema.anton.weatherapp.api;

import java.util.Map;

import kizema.anton.weatherapp.model.WeatherFiveDayList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface ApiEndpoint {

    @GET("/data/2.5/forecast")
    Call<WeatherFiveDayList> getFiveDayForcast(
            @QueryMap Map<String, String> options
    );

}
