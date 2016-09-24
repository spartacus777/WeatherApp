package kizema.anton.weatherapp.helpers;

import kizema.anton.weatherapp.model.WeatherForcastDto;

public class TimeHelper {

    public static String getTime(WeatherForcastDto dto){
        StringBuilder builder = new StringBuilder();
        builder.append(getProperTimeInt(dto.hour)).append(":").append(getProperTimeInt(dto.minute));

        return builder.toString();
    }

    public static String getProperTimeInt(int t){
        if (t <= 9){
            return "0"+t;
        }

        return ""+t;
    }

    public static String getDayName(WeatherForcastDto dto) {
        StringBuilder builder = new StringBuilder();
        builder.append(getProperTimeInt(dto.dayOfMonth)).append(".").append(getProperTimeInt(dto.monthOfYear));

        return builder.toString();
    }

}
