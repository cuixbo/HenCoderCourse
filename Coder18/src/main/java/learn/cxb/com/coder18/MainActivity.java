package learn.cxb.com.coder18;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        rxTest();
        rxTest1();
    }

    private void rxTest() {
        Single.just(9)
                .subscribe(new SingleObserver<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d("xbc", "======onSubscribe");
                    }

                    @Override
                    public void onSuccess(Integer integer) {
                        Log.d("xbc", "======onSuccess:"+integer);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("xbc", "======onError:"+e.getMessage());
                    }
                });

        Observable.just(9,4,1,7,0,2,5)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d("xbc", "======onSubscribe");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d("xbc", "======onNext"+integer);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("xbc", "======onError"+e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d("xbc", "======onComplete");
                    }
                });

        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(4);
                emitter.onNext(3);
                emitter.onComplete();
//                emitter.onError(new Exception("404"));
                emitter.onNext(2);
                Log.d("xbc", "======subscribe,over");
            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d("xbc", "======onSubscribe");
            }

            @Override
            public void onNext(Integer integer) {
                Log.d("xbc", "======onNext"+integer);
            }

            @Override
            public void onError(Throwable e) {
                Log.d("xbc", "======onError"+e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d("xbc", "======onComplete");
            }
        });


    }

    private void rxTest1() {
        Single.just(5)
                .map(new Function<Integer, String>() {
                    @Override
                    public String apply(Integer integer) throws Exception {
                        Log.d("xbc", "======1:"+Thread.currentThread().getName());
                        return String.valueOf(integer);
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .map(new Function<String, Integer>() {
                    @Override
                    public Integer apply(String s) throws Exception {
                        Log.d("xbc", "======2:"+Thread.currentThread().getName());
                        return Integer.valueOf(s);
                    }
                })
                .subscribeOn(Schedulers.computation())
                .map(new Function<Integer, String>() {
                    @Override
                    public String apply(Integer integer) throws Exception {
                        Log.d("xbc", "======3:"+Thread.currentThread().getName());
                        return String.valueOf(integer);
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .map(new Function<String, Integer>() {
                    @Override
                    public Integer apply(String s) throws Exception {
                        Log.d("xbc", "======4:"+Thread.currentThread().getName());
                        return Integer.valueOf(s);
                    }
                })
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.io())
                .subscribe(new SingleObserver<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d("xbc", "======onSubscribe:"+Thread.currentThread().getName());
                    }

                    @Override
                    public void onSuccess(Integer integer) {
                        Log.d("xbc", "======onSuccess:"+Thread.currentThread().getName());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }
}
