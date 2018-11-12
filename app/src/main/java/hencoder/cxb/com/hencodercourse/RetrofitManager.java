package hencoder.cxb.com.hencodercourse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {

    private volatile static RetrofitManager retrofitManager;
    private Retrofit retrofit;
    private Gson gson;
    private String baseUrl = "https://api.hencoder.com";


    private RetrofitManager() {
        initRetrofitManager();
    }

    private void initRetrofitManager() {
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(defaultHttpClient())
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson()))
                .build();
    }

    public static RetrofitManager getInstance() {
        if (retrofitManager == null) {
            synchronized (RetrofitManager.class) {
                retrofitManager = new RetrofitManager();
            }
        }
        return retrofitManager;
    }

    //gson
    private Gson gson() {
        if (gson == null) {
            return new GsonBuilder().setLenient().create();
        }
        return gson;
    }

    //okhttp添加网络拦截以及缓存
    private OkHttpClient defaultHttpClient() {
//        //开发环境中，打印日志 发布版不在打印
//        HttpLoggingInterceptor.Level level = HttpLoggingInterceptor.Level.BODY;
//        //新建log拦截器
//        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
//            @Override
//            public void log(String message) {
//                Log.i("OkHttpClient", "OkHttpMessage:" + message);
//            }
//        });
//        loggingInterceptor.setLevel(level);
        return new OkHttpClient.Builder()
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
//                .cache(cache())
//                .addNetworkInterceptor(new CatcheInterceptor())
//                .addInterceptor(loggingInterceptor)
                .build();
    }

    public HenCoderApiService getHenCoderApiService() {
        return retrofit.create(HenCoderApiService.class);
    }

}
