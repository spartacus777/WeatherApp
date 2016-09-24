package kizema.anton.weatherapp.activities.stations;


import java.io.Serializable;

public interface WeatherPresenter extends Serializable{

    void setView(WeatherView podactView);

    void removeView(WeatherView podactView);
}
