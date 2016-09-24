package kizema.anton.weatherapp.helpers;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class RetainedFragment <T> extends Fragment {

    // data object we want to retain
    private T data;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public void setData(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }
}
