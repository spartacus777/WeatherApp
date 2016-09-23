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
import kizema.anton.weatherapp.App;
import kizema.anton.weatherapp.R;
import kizema.anton.weatherapp.model.WeatherForcastDto;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private List<WeatherForcastDto> stationList;

    public MainAdapter(List<WeatherForcastDto> stationList) {
        this.stationList = stationList;
        notifyDataSetChanged();
    }

    public MainAdapter() {}

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

        WeatherForcastDto model = stationList.get(position);

        ImageLoader.getInstance().displayImage(model.getIconUrl(), holder.ivIcon, App.defOpts);
        holder.tvTime.setText(""+model.time);
        holder.tvDescr.setText(model.description);
        holder.tvMinMax.setText(model.temp_min + "/" + model.temp_max);
    }

    @Override
    public int getItemCount() {
        if (stationList == null) {
            return 0;
        }

        return stationList.size();
    }

}
