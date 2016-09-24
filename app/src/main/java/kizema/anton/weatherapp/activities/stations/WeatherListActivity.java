package kizema.anton.weatherapp.activities.stations;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import kizema.anton.weatherapp.R;
import kizema.anton.weatherapp.activities.WeatherDetailActivity;
import kizema.anton.weatherapp.adapters.MainAdapter;
import kizema.anton.weatherapp.adapters.ViewPagerAdapter;
import kizema.anton.weatherapp.control.AppConstants;
import kizema.anton.weatherapp.helpers.RetainedFragment;
import kizema.anton.weatherapp.model.UserPrefs;
import kizema.anton.weatherapp.model.WeatherCityDto;
import kizema.anton.weatherapp.model.WeatherForcastDto;

public class WeatherListActivity extends AppCompatActivity implements WeatherView,
        MainAdapter.OnWeatherClickListener{

    private static final String PRESENTER_STR = "presenterSaveStr";

    @BindView(R.id.llNoData)
    public View llNoData;

    @BindView(R.id.pager)
    public ViewPager pager;

    @BindView(R.id.ptTabStrip)
    public PagerTabStrip ptTabStrip;

    private WeatherPresenter weatherPresenter;

    private ViewPagerAdapter viewPagerAdapter;

    private RetainedFragment <WeatherPresenter> dataFragment;

    private String curDisplayCityId = "-1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Log.d("LOC", "onCreate");

        init();
        initPresenter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("LOC", "onDestroy");

        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        dataFragment.setData(weatherPresenter);
        weatherPresenter.removeView(this);
    }

    private void init() {

        viewPagerAdapter = new ViewPagerAdapter(this);
        pager.setAdapter(viewPagerAdapter);

        viewPagerAdapter.setListener(this);

        viewPagerAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();

                if (viewPagerAdapter.getCount() == 0){
                    llNoData.setVisibility(View.VISIBLE);
                } else {
                    llNoData.setVisibility(View.GONE);
                }
            }
        });

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter(AppConstants.UPDATE_COORD_SIGNAL));
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            UserPrefs prefs = UserPrefs.getPrefs();
            WeatherCityDto dto = WeatherCityDto.findById(curDisplayCityId);
            if (dto == null || dto.lon != prefs.lon || dto.lat != prefs.lat){
                weatherPresenter.coordinatesUpdated();
            }
        }
    };

    private void initPresenter() {

        FragmentManager fm = getSupportFragmentManager();
        dataFragment = (RetainedFragment<WeatherPresenter>) fm.findFragmentByTag(PRESENTER_STR);

        if (dataFragment == null) {

            dataFragment = new RetainedFragment<>();
            fm.beginTransaction().add(dataFragment, PRESENTER_STR).commit();

            weatherPresenter = new WeatherPresenterImpl(new WeatherInteractorImpl());
            dataFragment.setData(weatherPresenter);
        }

        weatherPresenter = dataFragment.getData();
        weatherPresenter.setView(this);
    }

    @Override
    public void setData(List<WeatherForcastDto> list) {
        viewPagerAdapter.setList(list);

        if (list != null && list.size() > 0) {
            curDisplayCityId = list.get(0).cityId;

            Log.d("LOC", "setData for cityID :: " + curDisplayCityId);
        }
    }

    @Override
    public void showError() {
        Toast.makeText(this, "No internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onWeatherClick(WeatherForcastDto dto) {
        Intent intent = new Intent(this, WeatherDetailActivity.class);
        intent.putExtra(WeatherDetailActivity.DTO_ID, dto.cityId);
        intent.putExtra(WeatherDetailActivity.DTO_TIME, dto.time);
        startActivity(intent);
    }
}
