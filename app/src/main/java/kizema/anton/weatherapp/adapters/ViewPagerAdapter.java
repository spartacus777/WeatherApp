package kizema.anton.weatherapp.adapters;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import kizema.anton.weatherapp.R;
import kizema.anton.weatherapp.model.WeatherForcastDto;

public class ViewPagerAdapter extends PagerAdapter {

    private Context ctx;
    private List<WeatherForcastDto> list;

    public ViewPagerAdapter(Context ctx) {
        this.ctx = ctx;
        calculate();
    }

    public void setList(List<WeatherForcastDto> list){
        this.list = list;
        notifyDataSetChanged();
        calculate();
    }

    private void calculate(){

    }

    @Override
    public int getCount() {
        if (list == null || list.size() == 0 ) {
            return 0;
        }

        return 5;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Page " + (position + 1);
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.frame_layout_cntrl, collection, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rvPodcasts);

        recyclerView.setLayoutManager(new LinearLayoutManager(ctx, LinearLayoutManager.VERTICAL, false));

        List<WeatherForcastDto> sub = list.subList(position * list.size()/5, (position+1) * list.size()/5);
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
