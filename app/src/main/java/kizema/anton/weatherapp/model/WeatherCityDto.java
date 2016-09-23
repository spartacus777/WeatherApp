package kizema.anton.weatherapp.model;


import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "WeatherCityDto")
public class WeatherCityDto extends Model{

    @Column(name = "cityId", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public String cityId;

    @Column(name = "name")
    public String name;

    @Column(name = "timeUpdate")
    public long timeUpdate;

    @Column(name = "lon")
    public double lon;

    @Column(name = "lat")
    public double lat;

    public static WeatherCityDto findById(String id){
        return new Select().from(WeatherCityDto.class).where("cityId = ?", id).executeSingle();
    }

}
