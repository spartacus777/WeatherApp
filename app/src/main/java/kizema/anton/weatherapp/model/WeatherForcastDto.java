package kizema.anton.weatherapp.model;


import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

@Table(name = "WeatherForcastDto")
public class WeatherForcastDto extends Model {

    @Column(name = "cityId")
    public String cityId;

    @Column(name = "time")
    public long time;

    @Column(name = "temp")
    public double temp;

    @Column(name = "temp_min")
    public double temp_min;

    @Column(name = "temp_max")
    public double temp_max;

    @Column(name = "icon")
    public String icon;

    @Column(name = "description")
    public String description;

    public static WeatherForcastDto findByTimeAndCity(long time, String cityId){
        return new Select().from(WeatherForcastDto.class).where("cityId = ?", cityId).where("time = ?", time).executeSingle();
    }

    public static List<WeatherForcastDto> findByCity(String cityId){
        return new Select().from(WeatherForcastDto.class).where("cityId = ?", cityId).execute();
    }
}
