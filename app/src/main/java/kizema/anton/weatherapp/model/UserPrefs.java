package kizema.anton.weatherapp.model;


import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "UserPrefs")
public class UserPrefs extends Model{

    @Column(name = "lon")
    public double lon;

    @Column(name = "lat")
    public double lat;

    @Column(name = "cityId")
    public String cityId = "";

    public boolean hasLatLon(){
        if (lat != 0 || lon != 0){
            return true;
        }

        return false;
    }

    public static UserPrefs getPrefs(){
        UserPrefs prefs = new Select().from(UserPrefs.class).executeSingle();

        if (prefs == null){
            prefs = new UserPrefs();
        }

        return prefs;
    }
}
