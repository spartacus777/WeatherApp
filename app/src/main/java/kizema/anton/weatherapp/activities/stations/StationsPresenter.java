package kizema.anton.weatherapp.activities.stations;


import java.io.Serializable;

public interface StationsPresenter extends Serializable{

    void setView(StationsView podactView);

    void removeView(StationsView podactView);
}
