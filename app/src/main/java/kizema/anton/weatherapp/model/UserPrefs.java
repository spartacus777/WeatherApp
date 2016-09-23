package kizema.anton.weatherapp.model;

public class UserPrefs {

    public double lon;

    public double lat;

    public WeatherCityDto cityDto;

    public boolean hasLatLon(){
        if (lat != 0 || lon != 0){
            return true;
        }

        return false;
    }
}
