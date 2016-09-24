package kizema.anton.weatherapp.activities.stations;

import java.util.List;

import kizema.anton.weatherapp.model.WeatherForcastDto;

public interface WeatherInteractor {

    interface OnCompletion{
        void onComplete(List<WeatherForcastDto> list);
        void onError();
    }

    void loadData(OnCompletion listener);

    List<WeatherForcastDto> loadDataFromDB();
}
