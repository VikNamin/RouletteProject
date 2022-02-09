package org.o7planning.rouletteproject;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Random;

public class RouletteActivity extends AppCompatActivity {

    private final static String scoreVariableKey = "SCORE_VARIABLE";
    private final static String degreeVariableKey = "DEGREE_VARIABLE";
    private final static String defaultDegreeVariableKey = "DEFAULT_DEGREE_VARIABLE";
    private String score = "";
    private TextView textView;
    private ImageView rouletteView;
    private Random random;
    private int old_degree = 0;
    private int degree = 0;
    private int defaultDegree = 0;
    private static int finalDegree = 0;
    private static final float FACTOR = 4.86f;
    private String[] resultArr = {"32 RED","15 BLACK","19 RED","4 BLACK",
            "21 RED","2 BLACK","25 RED","17 BLACK", "34 RED",
            "6 BLACK","27 RED","13 BLACK","36 RED","11 BLACK","30 RED",
            "8 BLACK","23 RED","10 BLACK","5 RED","24 BLACK","16 RED","33 BLACK",
            "1 RED","20 BLACK","14 RED","31 BLACK","9 RED","22 BLACK","18 RED",
            "29 BLACK","7 RED","28 BLACK","12 RED","35 BLACK","3 RED","26 BLACK","0"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roulette);
        textView = findViewById(R.id.textView);
        rouletteView = findViewById(R.id.rouletteView);
        random = new Random();
    }

    public void onClickSpin(View view) {
        old_degree = degree % 360;
        degree = random.nextInt(3600) + 720;
        RotateAnimation rotate = new RotateAnimation(old_degree, degree, RotateAnimation.RELATIVE_TO_SELF,
                0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(3600);
        rotate.setFillAfter(true);
        rotate.setInterpolator(new DecelerateInterpolator());
        rotate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                textView.setText(score);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                System.out.println(360 - (degree % 360) + defaultDegree);
                if ((360 - (degree % 360) + defaultDegree >= 0) && (360 - (degree % 360) + defaultDegree <= 360)){
                    finalDegree = 360 - (degree % 360) + defaultDegree;
                    score = getResult(finalDegree);
                    textView.setText(score);
                }
                else if(360 - (degree % 360) + defaultDegree > 360) {
                    System.out.println(Math.abs(360 - (360 - (degree % 360) + defaultDegree)));
                    finalDegree = Math.abs(360 - (360 - (degree % 360) + defaultDegree));
                    score = getResult(finalDegree);
                    textView.setText(score);
                }
                else {
                    System.out.println(Math.abs(360 - (degree % 360) - defaultDegree));
                    finalDegree = Math.abs(360 - (degree % 360) - defaultDegree);
                    score = getResult(finalDegree);
                    textView.setText(score);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        rouletteView.startAnimation(rotate);
    }

    private String getResult(int degree)
    {
        String text = "";

        int factor_x = 1;
        int factor_y = 3;
        for(int i = 0;i < 37; i++){
            if(degree >= (FACTOR * factor_x) && degree < (FACTOR * factor_y))
            {
                text = resultArr[i];
                return text;
            }
            factor_x += 2;
            factor_y += 2;
        }
        if(degree >= (FACTOR * 73) && degree < 360 || degree >= 0 && degree < (FACTOR * 1)) text = resultArr[resultArr.length - 1];

        return text;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(scoreVariableKey, score);
        defaultDegree = finalDegree;
        outState.putInt(degreeVariableKey, degree);
        outState.putInt(defaultDegreeVariableKey, defaultDegree);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        score = savedInstanceState.getString(scoreVariableKey);
        degree = savedInstanceState.getInt(degreeVariableKey);
        defaultDegree = savedInstanceState.getInt(defaultDegreeVariableKey);
        textView.setText(score);
        rouletteView.animate().rotation(-defaultDegree);
    }
}