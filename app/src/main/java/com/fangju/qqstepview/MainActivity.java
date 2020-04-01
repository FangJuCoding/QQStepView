package com.fangju.qqstepview;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private QQStepView mStepView;
    private EditText mInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mStepView = findViewById(R.id.setpview);
        mStepView.setStepMax(2000);

        mInput = findViewById(R.id.input_et);
    }

    public void start(View view) {
        String trim = mInput.getText().toString().trim();
        startAnim(Integer.valueOf(trim));
    }

    private void startAnim(int number) {
        ValueAnimator animator = ValueAnimator.ofFloat(0, number);
        animator.setDuration(2000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                mStepView.setCurrentStep((int) animatedValue);
            }
        });
        animator.start();
    }
}
