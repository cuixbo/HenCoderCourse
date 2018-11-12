package hencoder.cxb.com.hencodercourse;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    HenCoderApiService mApiService;
    public static final String TAG = "MainActivity";
    TextView mTvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mApiService = RetrofitManager.getInstance().getHenCoderApiService();
        mTvContent = findViewById(R.id.tv_content);


        mTvContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reqAuthor();
                reqTestPost(1);
//                reqTestPost(2);
            }
        });
    }

    private void reqAuthor() {
        Call<AuthorResp> call = mApiService.author();
        call.enqueue(new Callback<AuthorResp>() {
            @Override
            public void onResponse(Call<AuthorResp> call, Response<AuthorResp> response) {
                if (response.isSuccessful()) {
                    AuthorResp resp = response.body();
                    Log.e(TAG, "resp:" + resp.toString());
                    mTvContent.setText(resp.toString());
                } else {
                    Log.e(TAG, "response un sucess:" + response.code());
                }
            }

            @Override
            public void onFailure(Call<AuthorResp> call, Throwable t) {
                Log.e(TAG, "onFailure:");
            }
        });
    }


    private void reqTestPost(int id) {
        Call<HttpResp> call = mApiService.testPost(id, "cui", "xbc");

        call.enqueue(new Callback<HttpResp>() {
            @Override
            public void onResponse(Call<HttpResp> call, Response<HttpResp> response) {
                if (response.isSuccessful()) {
                    HttpResp resp = response.body();
                    Log.e(TAG, "resp:" + resp.toString());
                    mTvContent.setText(resp.toString());
                } else {
                    Log.e(TAG, "response un sucess:" + response.code());
                }
            }

            @Override
            public void onFailure(Call<HttpResp> call, Throwable t) {
                Log.e(TAG, "onFailure:"+t.getLocalizedMessage());
            }
        });
    }
}
