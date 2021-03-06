package kizema.anton.weatherapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import kizema.anton.weatherapp.R;
import kizema.anton.weatherapp.helpers.TemperatureHelper;
import kizema.anton.weatherapp.helpers.TimeHelper;
import kizema.anton.weatherapp.helpers.UILHelper;
import kizema.anton.weatherapp.model.WeatherForcastDto;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private List<WeatherForcastDto> stationList;

    private OnWeatherClickListener listener;

    public interface OnWeatherClickListener{
        void onWeatherClick(WeatherForcastDto dto);
    }

    public MainAdapter(List<WeatherForcastDto> stationList) {
        this.stationList = stationList;
        notifyDataSetChanged();
    }

    public MainAdapter() {}

    public void setOnWeatherClickListener(OnWeatherClickListener listener){
        this.listener = listener;
    }

    public void setData(List<WeatherForcastDto> busModels) {
        this.stationList = busModels;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvTime)
        public TextView tvTime;

        @BindView(R.id.tvDescr)
        public TextView tvDescr;

        @BindView(R.id.tvMinMax)
        public TextView tvMinMax;

        @BindView(R.id.ivIcon)
        public ImageView ivIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View parentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_item, parent, false);
        return new ViewHolder(parentView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final WeatherForcastDto model = stationList.get(position);

        ImageLoader.getInstance().displayImage(model.getIconUrl(), holder.ivIcon, UILHelper.defOpts);
        holder.tvTime.setText(TimeHelper.getTime(model));
        holder.tvDescr.setText(model.description);
        holder.tvMinMax.setText(TemperatureHelper.getTemperatureString(model));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null){
                    listener.onWeatherClick(model);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (stationList == null) {
            return 0;
        }

        return stationList.size();
    }

}
