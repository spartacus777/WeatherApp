package kizema.anton.weatherapp.activities.stations;

import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kizema.anton.weatherapp.control.HttpControler;
import kizema.anton.weatherapp.model.UserPrefs;
import kizema.anton.weatherapp.model.WeatherCityDto;
import kizema.anton.weatherapp.model.WeatherFiveDayList;
import kizema.anton.weatherapp.model.WeatherForcastDto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherInteractorImpl implements WeatherInteractor {

    public WeatherInteractorImpl() {}

    @Override
    public void loadData(final OnCompletion listener) {

        Log.e("RRR", " ===== LOAD DATA ===== ");

        UserPrefs prefs = UserPrefs.getPrefs();
        Map<String, String> data = new HashMap<>();
        data.put("lon", "" + prefs.lon);
        data.put("lat", "" + prefs.lat);

        Call<WeatherFiveDayList> call = HttpControler.getInstance().getApiService().getFiveDayForecast(data);
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
