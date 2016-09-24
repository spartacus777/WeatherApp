package kizema.anton.weatherapp.helpers;


import kizema.anton.weatherapp.model.WeatherForcastDto;

public class TemperatureHelper {

    public static int farenheitToCelsius(double far){
        return (int) (((far - 32)*5)/9);
    }

    public static int kelvinToCelsius(double kel){
        return (int) (kel - 273.15);
    }

    public static String getTemperatureString(WeatherForcastDto model){
        StringBuilder builder = new StringBuilder();
        builder.append(model.temp_min).append(" - ").append(model.temp_max).append(" C");
        return builder.toString();
    }
}
