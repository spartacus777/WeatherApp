package kizema.anton.weatherapp.adapters;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import kizema.anton.weatherapp.R;
import kizema.anton.weatherapp.helpers.TimeHelper;
import kizema.anton.weatherapp.model.WeatherForcastDto;

public class ViewPagerAdapter extends PagerAdapter {

    private Context ctx;
    private List<WeatherForcastDto> list;
    private Map<String, List<WeatherForcastDto>> map;
    private List<String> keyDay;

    public ViewPagerAdapter(Context ctx) {
        this.ctx = ctx;
    }

    public void setList(List<WeatherForcastDto> list) {
        this.list = list;

        calculate();
        notifyDataSetChanged();
    }

    private void calculate() {
        map = new LinkedHashMap<>();
        keyDay = new ArrayList<>(5);

        for (WeatherForcastDto dto : list) {
            String day = TimeHelper.getDayName(dto);

            if (map.get(day) == null) {
                List<WeatherForcastDto> smallList = new ArrayList<>();
                smallList.add(dto);
                keyDay.add(day);
                map.put(day, smallList);
            } else {
                map.get(day).add(dto);
            }
        }
    }

    @Override
    public int getCount() {
        if (list == null || list.size() == 0) {
            return 0;
        }

        return 5;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return keyDay.get(position);
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.frame_layout_cntrl, collection, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rvPodcasts);

        recyclerView.setLayoutManager(new LinearLayoutManager(ctx, LinearLayoutManager.VERTICAL, false));

        List<WeatherForcastDto> sub = map.get(keyDay.get(position));
        final MainAdapter adapter = new MainAdapter(sub);
        recyclerView.setAdapter(adapter);

        collection.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1) {
    }

    @Override
    public void startUpdate(View arg0) {
    }

    @Override
    public void finishUpdate(View arg0) {
    }
}
