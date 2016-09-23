package kizema.anton.weatherapp.activities.stations;

import java.util.List;

import kizema.anton.weatherapp.helpers.LocationHelper;
import kizema.anton.weatherapp.model.UserPrefs;
import kizema.anton.weatherapp.model.WeatherForcastDto;

public class StationsPresenterImpl implements StationsPresenter {

    private StationsView podactView;

    private StationsInteractor stationsInteractor;

    private boolean loadDataIsInProgress = false;
    private boolean firstTime = true;

    public StationsPresenterImpl(StationsInteractor stationsInteractor) {
        this.stationsInteractor = stationsInteractor;
    }

    @Override
    public void setView(StationsView podactView) {
        this.podactView = podactView;

        UserPrefs prefs = new UserPrefs();
        if (prefs.hasLatLon()){
            loadData();
        } else {
            LocationHelper.getRealCoordinates(new LocationHelper.OmLocationReceived() {
                @Override
                public void onReceivedLocation(double lat, double lon) {
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
    public void removeView(StationsView podactView) {
        if (podactView == this.podactView){
            this.podactView = null;
        }
    }

    private void loadFromDB(){
        List<WeatherForcastDto> list = stationsInteractor.loadDataFromDB();
        podactView.setData(list);
    }

    private void load() {

        if (loadDataIsInProgress){
            return;
        }

        loadDataIsInProgress = true;
        stationsInteractor.loadData(new StationsInteractor.OnCompletion() {
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
