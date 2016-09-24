package kizema.anton.weatherapp.activities.stations;

import java.util.List;

import kizema.anton.weatherapp.model.WeatherForcastDto;

public class WeatherPresenterImpl implements WeatherPresenter {

    private WeatherView weatherView;

    private WeatherInteractor weatherInteractor;

    private boolean firstTime = true;

    public WeatherPresenterImpl(WeatherInteractor weatherInteractor) {
        this.weatherInteractor = weatherInteractor;
    }

    @Override
    public void setView(WeatherView weatherView) {
        this.weatherView = weatherView;

        if (weatherInteractor.shouldLoadFromLocalDb()){
            loadData();
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
    public void removeView(WeatherView weatherView) {
        if (weatherView == this.weatherView){
            this.weatherView = null;
        }
    }

    @Override
    public void coordinatesUpdated() {
        load();
    }

    private void loadFromDB(){
        List<WeatherForcastDto> list = weatherInteractor.loadDataFromDB();

        if (list != null) {
            weatherView.setData(list);
        }
    }

    private void load() {
        weatherInteractor.loadData(new WeatherInteractor.OnCompletion() {
            @Override
            public void onComplete(List<WeatherForcastDto> list) {
                weatherView.setData(list);
            }

            @Override
            public void onError() {
                weatherView.showError();
            }
        });
    }

}
