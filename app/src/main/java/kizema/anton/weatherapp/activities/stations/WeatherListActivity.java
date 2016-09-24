package kizema.anton.weatherapp.activities.stations;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import kizema.anton.weatherapp.R;
import kizema.anton.weatherapp.adapters.ViewPagerAdapter;
import kizema.anton.weatherapp.model.WeatherForcastDto;

public class WeatherListActivity extends AppCompatActivity implements WeatherView {

    private static final String PRESENTER_STR = "presenterSaveStr";

    @BindView(R.id.loading)
    public ProgressBar loading;

    @BindView(R.id.pager)
    public ViewPager pager;

    @BindView(R.id.ptTabStrip)
    public PagerTabStrip ptTabStrip;

    private WeatherPresenter weatherPresenter;

    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        init();
        initPresenter(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        weatherPresenter.removeView(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(PRESENTER_STR, weatherPresenter);
        super.onSaveInstanceState(outState);
    }

    private void init() {

        viewPagerAdapter = new ViewPagerAdapter(this);
        pager.setAdapter(viewPagerAdapter);

        viewPagerAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();

                if (viewPagerAdapter.getCount() == 0){
                    loading.setVisibility(View.VISIBLE);
                } else {
                    loading.setVisibility(View.GONE);
                }
            }
        });
    }

    private void initPresenter(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            weatherPresenter = (WeatherPresenter) savedInstanceState.getSerializable(PRESENTER_STR);
        }

        if (weatherPresenter == null) {
            weatherPresenter = new WeatherPresenterImpl(new WeatherInteractorImpl());
        }

        weatherPresenter.setView(this);
    }

    @Override
    public void setData(List<WeatherForcastDto> list) {
        viewPagerAdapter.setList(list);
    }

    @Override
    public void showError() {
        Toast.makeText(this, "No internet", Toast.LENGTH_SHORT).show();
    }

}
