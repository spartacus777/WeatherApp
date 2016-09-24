package kizema.anton.weatherapp.activities.stations;

import java.util.List;

import kizema.anton.weatherapp.model.WeatherForcastDto;

public interface WeatherView {

    void setData(List<WeatherForcastDto> list);

    void showError();

}
