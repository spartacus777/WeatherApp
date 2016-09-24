package kizema.anton.weatherapp.model;


import com.activeandroid.ActiveAndroid;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import kizema.anton.weatherapp.helpers.TemperatureHelper;

public class WeatherFiveDayList {

    @SerializedName("city")
    private WeatherCity city;

    @SerializedName("list")
    private List<WeatherUpdate> weatherUpdateList;

    public List<WeatherUpdate> getWeatherUpdateList() {
        return weatherUpdateList;
    }

    public void setWeatherUpdateList(List<WeatherUpdate> weatherUpdateList) {
        this.weatherUpdateList = weatherUpdateList;
    }

    public WeatherCity getCity() {
        return city;
    }

    public void setCity(WeatherCity city) {
        this.city = city;
    }

    public static class WeatherIcon {

        @SerializedName("icon")
        private String icon;

        @SerializedName("description")
        private String description;

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    public static class WeatherUpdate {

        @SerializedName("dt")
        private long date;

        @SerializedName("main")
        private Main main;

        @SerializedName("weather")
        private List<WeatherIcon> weatherIcons;

        public long getDate() {
            return date;
        }

        public void setDate(long date) {
            this.date = date;
        }

        public Main getMain() {
            return main;
        }

        public void setMain(Main main) {
            this.main = main;
        }

        public List<WeatherIcon> getWeatherIcons() {
            return weatherIcons;
        }

        public void setWeatherIcons(List<WeatherIcon> weatherIcons) {
            this.weatherIcons = weatherIcons;
        }
    }

    public static class Main {

        @SerializedName("temp")
        private double temp;

        @SerializedName("temp_min")
        private double temp_min;

        @SerializedName("temp_max")
        private double temp_max;

        public double getTemp() {
            return temp;
        }

        public void setTemp(double temp) {
            this.temp = temp;
        }

        public double getTemp_min() {
            return temp_min;
        }

        public void setTemp_min(double temp_min) {
            this.temp_min = temp_min;
        }

        public double getTemp_max() {
            return temp_max;
        }

        public void setTemp_max(double temp_max) {
            this.temp_max = temp_max;
        }
    }

    public static class WeatherCity {
        @SerializedName("id")
        private String id;

        @SerializedName("name")
        private String name;

        @SerializedName("coord")
        private Coordinates coord;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Coordinates getCoord() {
            return coord;
        }

        public void setCoord(Coordinates coord) {
            this.coord = coord;
        }
    }

    public static class Coordinates {
        @SerializedName("lon")
        private double lon;

        @SerializedName("lat")
        private double lat;

        public double getLon() {
            return lon;
        }

        public void setLon(double lon) {
            this.lon = lon;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }
    }


    public static List<WeatherForcastDto> getWeatherForcastDtos(WeatherFiveDayList weatherFiveDayList) {
//        Log.d("RRR", "City : " + weatherFiveDayList.getCity().getName());
//        Log.d("RRR", "Lat : " + weatherFiveDayList.getCity().getCoord().getLat());

        WeatherCityDto dto = WeatherCityDto.findById(weatherFiveDayList.getCity().getId());
        if (dto == null) {
            dto = new WeatherCityDto();
            dto.cityId = weatherFiveDayList.getCity().getId();
        }

        dto.name = weatherFiveDayList.getCity().getName();
        dto.lat = weatherFiveDayList.getCity().getCoord().getLat();
        dto.lon = weatherFiveDayList.getCity().getCoord().getLon();
        dto.timeUpdate = System.currentTimeMillis();
        dto.save();

        UserPrefs prefs = UserPrefs.getPrefs();
        prefs.cityId = dto.cityId;
        prefs.save();

        List<WeatherForcastDto> list = new ArrayList<>();

        for (WeatherFiveDayList.WeatherUpdate w : weatherFiveDayList.getWeatherUpdateList()) {
            WeatherForcastDto weather = WeatherForcastDto.findByTimeAndCity(w.getDate(), dto.cityId);

            if (weather == null) {
                weather = new WeatherForcastDto();
                weather.cityId = dto.cityId;

                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(w.getDate() * 1000);

                weather.dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
                weather.monthOfYear = 1+cal.get(Calendar.MONTH);
                weather.year = cal.get(Calendar.YEAR);
                weather.hour = cal.get(Calendar.HOUR_OF_DAY);
                weather.minute = cal.get(Calendar.MINUTE);
                weather.time = w.getDate();
            }

            weather.description = w.getWeatherIcons().get(0).getDescription();
            weather.icon = w.getWeatherIcons().get(0).getIcon();

            weather.temp = TemperatureHelper.kelvinToCelsius(w.getMain().getTemp());
            weather.temp_max = TemperatureHelper.kelvinToCelsius(w.getMain().getTemp_max());
            weather.temp_min = TemperatureHelper.kelvinToCelsius(w.getMain().getTemp_min());

            list.add(weather);
        }

        ActiveAndroid.beginTransaction();
        try {
            for (WeatherForcastDto m : list) {
                m.save();
            }
            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }

        return list;
    }

}
