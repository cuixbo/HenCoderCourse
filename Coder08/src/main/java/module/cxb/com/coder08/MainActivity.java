package module.cxb.com.coder08;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        View view = findViewById(R.id.view);

        // ViewPropertyAnimator
        // ObjectAnimator
        // PropertyValuesHolder
        // AnimatorSet
        // TypeEvaluator
        // ValueAnimator
        // Listener

        // ViewPropertyAnimator
//        view.animate()
//                .translationX(100)
//                .setDuration(1000)
//                .setStartDelay(1000)
//                .start();

        // ObjectAnimator
//        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(view, "rotateDegree", 0, 40);
//        objectAnimator.setDuration(1000);
//        objectAnimator.setStartDelay(1000);
//        objectAnimator.start();

        // PropertyValuesHolder
//        PropertyValuesHolder holder1 = PropertyValuesHolder.ofInt("rotateDegree", 0, 45);
//        PropertyValuesHolder holder2 = PropertyValuesHolder.ofInt("flipDegreeBottom", 0, 270);
//
//        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(view, holder1,holder2);
//        objectAnimator.setDuration(1000);
//        objectAnimator.setStartDelay(1000);
//        objectAnimator.start();

        // AnimatorSet
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator objectAnimator1 = ObjectAnimator.ofInt(view, "flipDegreeBottom", 0, 45);
        ObjectAnimator objectAnimator2 = ObjectAnimator.ofInt(view, "rotateDegree", 0, 270);
        ObjectAnimator objectAnimator3 = ObjectAnimator.ofInt(view, "flipDegreeTop", 0, -45);
        animatorSet.playSequentially(objectAnimator1, objectAnimator2, objectAnimator3);
        animatorSet.setDuration(1000);// each duration
        animatorSet.setStartDelay(3000);
        animatorSet.start();

        // TypeEvaluator
//        ObjectAnimator objectAnimator=ObjectAnimator.ofObject(view,"province",new ProvinceTypeEvaluator(),ProvinceUtil.provinces.get(ProvinceUtil.provinces.size()-1));
//        objectAnimator.setDuration(2000);
//        objectAnimator.start();

    }

    class ProvinceTypeEvaluator implements TypeEvaluator<String> {

        @Override
        public String evaluate(float fraction, String startValue, String endValue) {
            // startIndex + (endValue-startValue)*fraction
            int startIndex = ProvinceUtil.provinces.indexOf(startValue);
            int endIndex = ProvinceUtil.provinces.indexOf(endValue);
            int index = (int) (startIndex + fraction * (endIndex - startIndex));
            return ProvinceUtil.provinces.get(index);
        }
    }

}
