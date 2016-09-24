package kizema.anton.weatherapp.control;

import java.io.IOException;

import kizema.anton.weatherapp.api.ApiConstants;
import kizema.anton.weatherapp.api.ApiEndpoint;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpControler {

    private static HttpControler instance;

    private Retrofit retrofit;
    private ApiEndpoint apiService;

    private HttpControler (){
        init();
    }

    public static synchronized HttpControler getInstance(){
        if (instance == null){
            instance = new HttpControler();
        }

        return instance;
    }

    private void init(){
        OkHttpClient.Builder httpClient =
                new OkHttpClient.Builder();

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                HttpUrl originalHttpUrl = original.url();

                HttpUrl url = originalHttpUrl.newBuilder()
                        .addQueryParameter("appid", ApiConstants.WEATHER_APP_ID)
                        .build();

                Request.Builder requestBuilder = original.newBuilder()
                        .url(url);

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        retrofit = new Retrofit.Builder()
                .client(httpClient.build())
                .baseUrl(ApiConstants.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiEndpoint.class);
    }

    public ApiEndpoint getApiService(){
        return apiService;
    }

}
