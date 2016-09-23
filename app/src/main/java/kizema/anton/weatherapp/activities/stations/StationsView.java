package kizema.anton.weatherapp.activities.stations;

import java.util.List;

import kizema.anton.weatherapp.model.WeatherForcastDto;

public interface StationsView {

    void setData(List<WeatherForcastDto> list);

    void showError();

}
