package kizema.anton.weatherapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import kizema.anton.weatherapp.R;
import kizema.anton.weatherapp.helpers.TemperatureHelper;
import kizema.anton.weatherapp.helpers.TimeHelper;
import kizema.anton.weatherapp.helpers.UILHelper;
import kizema.anton.weatherapp.model.WeatherForcastDto;

public class WeatherDetailActivity extends AppCompatActivity {

    public static final String DTO_ID = "ewq";
    public static final String DTO_TIME = "sdjnew";

    @BindView(R.id.tvTime)
    public TextView tvTime;

    @BindView(R.id.tvTemperature)
    public TextView tvTemperature;

    @BindView(R.id.ivIcon)
    public ImageView ivIcon;

    @BindView(R.id.tvDescr)
    public TextView tvDescr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_detail);
        ButterKnife.bind(this);

        String id = getIntent().getStringExtra(DTO_ID);
        Long time = getIntent().getLongExtra(DTO_TIME, 0);
        WeatherForcastDto dto = WeatherForcastDto.findByTimeAndCity(time, id);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        if (dto != null){
            tvTime.setText(TimeHelper.getTime(dto));
            ImageLoader.getInstance().displayImage(dto.getIconUrl(), ivIcon, UILHelper.defOpts);
            tvDescr.setText(dto.description);
            tvTemperature.setText(TemperatureHelper.getTemperatureString(dto));
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
