package kizema.anton.weatherapp.activities.stations;

import java.util.List;

import kizema.anton.weatherapp.helpers.LocationHelper;
import kizema.anton.weatherapp.model.UserPrefs;
import kizema.anton.weatherapp.model.WeatherForcastDto;

public class WeatherPresenterImpl implements WeatherPresenter {

    private WeatherView podactView;

    private WeatherInteractor weatherInteractor;

    private boolean loadDataIsInProgress = false;
    private boolean firstTime = true;

    public WeatherPresenterImpl(WeatherInteractor weatherInteractor) {
        this.weatherInteractor = weatherInteractor;
    }

    @Override
    public void setView(WeatherView podactView) {
        this.podactView = podactView;

        UserPrefs prefs = UserPrefs.getPrefs();
        if (prefs.hasLatLon()){
            loadData();
        } else {
            LocationHelper.getRealCoordinates(new LocationHelper.OmLocationReceived() {
                @Override
                public void onReceivedLocation(double lat, double lon) {
                    UserPrefs prefs = UserPrefs.getPrefs();
                    prefs.lon = lon;
                    prefs.lat = lat;
                    prefs.save();
                    loadData();
                }
            });
        }
    }

    private void loadData(){
        loadFromDB();

        if (firstTime){
            load();
            firstTime = false;
        }
    }

    @Override
    public void removeView(WeatherView podactView) {
        if (podactView == this.podactView){
            this.podactView = null;
        }
    }

    private void loadFromDB(){
        List<WeatherForcastDto> list = weatherInteractor.loadDataFromDB();
        podactView.setData(list);
    }

    private void load() {

        if (loadDataIsInProgress){
            return;
        }

        loadDataIsInProgress = true;
        weatherInteractor.loadData(new WeatherInteractor.OnCompletion() {
            @Override
            public void onComplete(List<WeatherForcastDto> list) {
                podactView.setData(list);
                loadDataIsInProgress = false;
            }

            @Override
            public void onError() {
                podactView.showError();
                loadDataIsInProgress = false;
            }
        });
    }

}