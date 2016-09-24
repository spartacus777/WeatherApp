package kizema.anton.weatherapp.adapters;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import kizema.anton.weatherapp.R;
import kizema.anton.weatherapp.helpers.TimeHelper;
import kizema.anton.weatherapp.model.WeatherForcastDto;

public class ViewPagerAdapter extends PagerAdapter {

    private Context ctx;
    private List<WeatherForcastDto> list;
    private Map<String, List<WeatherForcastDto>> map;
    private List<String> keyDay;

    private SparseArray<ViewHolder> holders = new SparseArray<>();

    public ViewPagerAdapter(Context ctx) {
        this.ctx = ctx;
    }

    public void setList(List<WeatherForcastDto> list) {
        int prevSize = 0;
        if (this.list != null){
            prevSize = this.list.size();
        }
        this.list = list;

        calculate();

        if (prevSize != list.size()){
            notifyDataSetChanged();
        }

        for (int i=0; i<holders.size(); ++i){
            List<WeatherForcastDto> sub = map.get(keyDay.get(i));
            if (holders.get(i) != null) {
                holders.get(i).update(sub);
            }
        }


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
        if (keyDay == null || keyDay.size() == 0) {
            return 0;
        }

        return keyDay.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return keyDay.get(position);
    }

    public static class ViewHolder{

        @BindView(R.id.rvPodcasts)
        public RecyclerView recyclerView;

        private MainAdapter adapter;

        public ViewHolder(View itemView){
            ButterKnife.bind(this, itemView);

            recyclerView.setLayoutManager(new LinearLayoutManager(
                    recyclerView.getContext(), LinearLayoutManager.VERTICAL, false));


            adapter = new MainAdapter();
            recyclerView.setAdapter(adapter);
        }

        public void update(List<WeatherForcastDto> sub){
            adapter.setData(sub);
        }
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.frame_layout_cntrl, collection, false);

        ViewHolder h = new ViewHolder(view);
        holders.put(position, h);

        List<WeatherForcastDto> sub = map.get(keyDay.get(position));
        h.update(sub);

        collection.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        holders.remove(position);
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
