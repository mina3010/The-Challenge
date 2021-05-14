package com.example.sharara.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.sharara.Adapter.AdapterCategory;
import com.example.sharara.Adapter.AdapterSets;
import com.example.sharara.Data.Database;
import com.example.sharara.Model.ResultScore;
import com.example.sharara.R;

import java.util.ArrayList;
import java.util.List;

import static com.example.sharara.Adapter.AdapterSets.*;

public class ScoreActivity extends AppCompatActivity {

    LottieAnimationView lottie ,starFull_1,starFull_2,starFull_3;
    public TextView score ;
    private Button done;
    List<ResultScore>scores=new ArrayList<>();
    Database db ;
    public static String score_str;
    public static int score_num;
    public static int score_len;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        score =findViewById(R.id.txt_resultScore);
        done =findViewById(R.id.btn_done);
        lottie =findViewById(R.id.animationView);
        starFull_1 =findViewById(R.id.animationViewStarFull_1);
        starFull_2 =findViewById(R.id.animationViewStarFull_2);
        starFull_3 =findViewById(R.id.animationViewStarFull_3);

        score_str= getIntent().getStringExtra("SCORE");
        score_num= getIntent().getIntExtra("SCORE_NUM",0);
        score_len= getIntent().getIntExtra("SCORE_LEN",0);

        animationStarsShow();

        score.setText(score_str);
        SetsActivity.setsID.set(pos,score_str);
        //AdapterSets.scores.set(pos,r).setScore(score_str);

        db = new Database(this);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(ScoreActivity.this,MainActivity.class);
                ScoreActivity.this.startActivity(i);
                ResultScore res =new ResultScore(CategoryActivity.selected_category_index,CategoryActivity.selected_category_name, pos+1,score_num,score_len);
                db.insert(res);
                ScoreActivity.this.finish();
            }
        });
    }

    private void animationStarsShow() {
        if (score_num ==0){
            starFull_1.setAnimation(R.raw.empty_star);
            starFull_2.setAnimation(R.raw.empty_star);
            starFull_3.setAnimation(R.raw.empty_star);

        }else if(score_num <= score_len/2 && score_num != 0){
            starFull_1.setAnimation(R.raw.empty_star);
            starFull_2.setAnimation(R.raw.star_animaition);
            starFull_3.setAnimation(R.raw.empty_star);

        }else if (score_num > score_len/2 &&score_num != score_len){
            starFull_1.setAnimation(R.raw.star_animaition);
            starFull_2.setAnimation(R.raw.star_animaition);
            starFull_3.setAnimation(R.raw.empty_star);

        }else if (score_num == score_len){
            starFull_1.setAnimation(R.raw.star_animaition);
            starFull_2.setAnimation(R.raw.star_animaition);
            starFull_3.setAnimation(R.raw.star_animaition);
        }
    }

}