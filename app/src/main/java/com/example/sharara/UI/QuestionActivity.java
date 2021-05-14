package com.example.sharara.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.ArrayMap;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.sharara.Adapter.AdapterSets;
import com.example.sharara.Data.Database;
import com.example.sharara.Model.Question;
import com.example.sharara.Model.ResultScore;
import com.example.sharara.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.example.sharara.Adapter.AdapterSets.pos;
import static com.example.sharara.UI.CategoryActivity.selected_category_index;
import static com.example.sharara.UI.ScoreActivity.score_len;
import static com.example.sharara.UI.ScoreActivity.score_str;
import static com.example.sharara.UI.SetsActivity.setsID;
import static com.example.sharara.UI.SplashActivity.catList;

public class QuestionActivity extends AppCompatActivity implements View.OnClickListener {

    LottieAnimationView lottieTime ;
    private TextView question, qCount, timer;
    private Button option1, option2, option3, option4;
    private ImageView exit_game;
    private ImageButton changed,help;
    private List<Question>questionList=new ArrayList<>();
    private int QuestionNum;
    private CountDownTimer countDownTimer;
    private int score;
    private FirebaseFirestore firestore;
    private int setNom;
    private Dialog loadingDialog;
    Boolean p;
    LinearLayout ln;
    Database db ;
    int num =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);


        loadingDialog=new Dialog(QuestionActivity.this);
        loadingDialog.setContentView(R.layout.loading);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawableResource(R.drawable.progress_background);
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();

        setNom=get_setNom();
        firestore =FirebaseFirestore.getInstance();

        questionList=new ArrayList<>();


        question=findViewById(R.id.question);
        qCount=findViewById(R.id.question_num);
        timer=findViewById(R.id.question_time);
        option1=findViewById(R.id.option1);
        option2=findViewById(R.id.option2);
        option3=findViewById(R.id.option3);
        option4=findViewById(R.id.option4);
        help=findViewById(R.id.help);
        changed=findViewById(R.id.changed);
        lottieTime=findViewById(R.id.animationViewTime);
        exit_game=findViewById(R.id.exit);
        ln=findViewById(R.id.lin);

        //font ui
        Typeface typeface= ResourcesCompat.getFont(this,R.font.elmessiri_medium);
        question.setTypeface(typeface);
        option1.setTypeface(typeface);
        option2.setTypeface(typeface);
        option3.setTypeface(typeface);
        option4.setTypeface(typeface);

        option1.setOnClickListener(this);
        option2.setOnClickListener(this);
        option3.setOnClickListener(this);
        option4.setOnClickListener(this);

        getQuestionList();
        score=0;
        num=0;

        //check def language
        if (Locale.getDefault().getLanguage().equals("ar")) {
            ln.setBackground(getDrawable(R.drawable.timer_dr_ar));
        }


        p=false;
        oneHelp();
        skipQuestion();

        db = new Database(this);

        exit_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(QuestionActivity.this,MainActivity.class);
                QuestionActivity.this.startActivity(i);
                ResultScore res =new ResultScore(CategoryActivity.selected_category_index,CategoryActivity.selected_category_name, pos+1,0,score_len);
                db.insert(res);
                QuestionActivity.this.finish();
            }
        });


    }

    private void skipQuestion() {
        changed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (QuestionNum<questionList.size()-2)
                {

                    p=true;
                    countDownTimer.cancel();
                    changed.setImageDrawable(getDrawable(R.drawable.boycott));
                    changeQuestion();
                    changed.setClickable(false);
                    changed.setEnabled(false);

                }
                else
                {
                    changed.setClickable(false);
                    changed.setEnabled(false);

                }

            }
        });
    }

    private void changeQuestion() {
        option1.setEnabled(true);
        option2.setEnabled(true);
        option3.setEnabled(true);
        option4.setEnabled(true);
        lottieTime.playAnimation();
        if(p==false) {
            if (QuestionNum < questionList.size() - 2) {
                QuestionNum++;
                playAnim(question, 0, 0);
                playAnim(option1, 0, 1);
                playAnim(option2, 0, 2);
                playAnim(option3, 0, 3);
                playAnim(option4, 0, 4);


                qCount.setText(String.valueOf(QuestionNum + 1) + "/" + String.valueOf(questionList.size()));
                timer.setText(String.valueOf(20));
                startTimer();

            } else {
                Intent i = new Intent(new Intent(QuestionActivity.this, ScoreActivity.class));
                i.putExtra("SCORE", String.valueOf(score) + "/" + String.valueOf(questionList.size()-1));
                i.putExtra("SCORE_LEN", questionList.size()-1);
                i.putExtra("SCORE_NUM", score);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        }
        else {
            if (QuestionNum < questionList.size() - 1) {
                QuestionNum++;
                playAnim(question, 0, 0);
                playAnim(option1, 0, 1);
                playAnim(option2, 0, 2);
                playAnim(option3, 0, 3);
                playAnim(option4, 0, 4);


                qCount.setText(String.valueOf(QuestionNum + 1) + "/" + String.valueOf(questionList.size()));
                timer.setText(String.valueOf(20));
                startTimer();

            } else {
                Intent i = new Intent(new Intent(QuestionActivity.this, ScoreActivity.class));
                i.putExtra("SCORE", String.valueOf(score) + "/" + String.valueOf(questionList.size()-1));
                i.putExtra("SCORE_LEN", questionList.size()-1);
                i.putExtra("SCORE_NUM", score);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }

        }

    }

    private void oneHelp() {
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count=0;
                countDownTimer.cancel();
                lottieTime.pauseAnimation();
                help.setImageDrawable(getDrawable(R.drawable.boycott));
                count=1;
                if (count==1){
                    help.setEnabled(false);
                }
            }
        });

    }

    private int get_setNom(){
        Intent intent = getIntent();
        int setNom = intent.getIntExtra("SetNo",1);
        return setNom;
    }

    private void getQuestionList() {
        questionList.clear();
        if (catList.get(selected_category_index).getId() != null || !catList.get(selected_category_index).getId().isEmpty()||catList.size()!=0) {
            firestore.collection("Quiz").document(catList.get(selected_category_index).getId())
                    .collection(setsID.get(setNom)).get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            Map<String, QueryDocumentSnapshot> docList = new ArrayMap<>();
                            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                docList.put(doc.getId(), doc);
                            }
                            QueryDocumentSnapshot quesListDoc = docList.get("QUESTIONS_LIST");
                            if (quesListDoc != null) {
                                String count = quesListDoc.getString("COUNT");
                                for (int i = 0; i < Integer.valueOf(count); i++) {
                                    String quesID = quesListDoc.getString("Q" + String.valueOf(i + 1) + "_ID");
                                    QueryDocumentSnapshot quesDoc = docList.get(quesID);

                                    questionList.add(new Question(
                                            quesDoc.getString("QUESTION"),
                                            quesDoc.getString("A"),
                                            quesDoc.getString("B"),
                                            quesDoc.getString("C"),
                                            quesDoc.getString("D"),
                                            Integer.valueOf(quesDoc.getString("ANSWER"))
                                    ));
                                }
                                setQuestion();
                                loadingDialog.dismiss();
                            } else {
                                Toast.makeText(QuestionActivity.this, " it is empty", Toast.LENGTH_SHORT).show();
                                finish();
                            }


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(QuestionActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    loadingDialog.dismiss();
                }
            });
        }
        else {
                Toast.makeText(this, "it is empty", Toast.LENGTH_SHORT).show();
                finish();
             }


    }

    private void setQuestion() {
        loadingDialog.show();
        timer.setText(String.valueOf(20));

        if (questionList!=null ||questionList.size()!=0|| !questionList.isEmpty())
        {
            question.setText(questionList.get(0).getQuestion());
            option1.setText(questionList.get(0).getOption_1());
            option2.setText(questionList.get(0).getOption_2());
            option3.setText(questionList.get(0).getOption_3());
            option4.setText(questionList.get(0).getOption_4());

            qCount.setText(String.valueOf(1)+ "/" +String.valueOf(questionList.size()));
            startTimer();
            QuestionNum=0;

        }
        else {
            Toast.makeText(this, " it is empty", Toast.LENGTH_SHORT).show();
            finish();
        }
        loadingDialog.dismiss();

    }

    private void startTimer() {
          countDownTimer= new CountDownTimer(22000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if(millisUntilFinished<20000){
                    timer.setText(String.valueOf(millisUntilFinished/1000));

                }
            }

            @Override
            public void onFinish() {
                changeQuestion();
            }
        };
        countDownTimer.start();
    }


    private void playAnim(final View view, final int value, final int viewNum) {
        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500).setStartDelay(100).setInterpolator(new DecelerateInterpolator())
        .setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {

                if (value==0){
                    switch (viewNum){
                        case 0:
                            ((TextView)view).setText(questionList.get(QuestionNum).getQuestion());
                            break;
                        case 1:
                            ((Button)view).setText(questionList.get(QuestionNum).getOption_1());

                            break;
                        case 2:
                            ((Button)view).setText(questionList.get(QuestionNum).getOption_2());
                            break;
                        case 3:
                            ((Button)view).setText(questionList.get(QuestionNum).getOption_3());
                            break;
                        case 4:
                            ((Button)view).setText(questionList.get(QuestionNum).getOption_4());
                            break;
                    }
                    if(viewNum!=0){
                        ((Button)view).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#041575")));
                    }

                    playAnim(view,1,viewNum);

                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                option1.setEnabled(false);
                option2.setEnabled(false);
                option3.setEnabled(false);
                option4.setEnabled(false);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {


            }
        });
    }

    @Override
    public void onClick(View v) {
        int select=0;
        switch (v.getId()){
            case R.id.option1:
                select =1;
                option1.setEnabled(false);
                option2.setEnabled(false);
                option3.setEnabled(false);
                option4.setEnabled(false);
                break;
            case R.id.option2:
                select =2;
                option1.setEnabled(false);
                option2.setEnabled(false);
                option3.setEnabled(false);
                option4.setEnabled(false);
                break;
            case R.id.option3:
                select =3;
                option1.setEnabled(false);
                option2.setEnabled(false);
                option3.setEnabled(false);
                option4.setEnabled(false);
                break;
            case R.id.option4:
                select =4;
                option1.setEnabled(false);
                option2.setEnabled(false);
                option3.setEnabled(false);
                option4.setEnabled(false);
                break;
            default:
        }
        countDownTimer.cancel();
        checkAns(select,v);
    }

    private void checkAns(int select ,View v) {
        if (select==questionList.get(QuestionNum).getCorrectAns()){
            ((Button)v).setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
            score++;


        }else {
            ((Button)v).setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            switch (questionList.get(QuestionNum).getCorrectAns()){
                case 1:
                    option1.setBackgroundTintList(ColorStateList.valueOf(Color.BLUE));
                    break;
                case 2:
                    option2.setBackgroundTintList(ColorStateList.valueOf(Color.BLUE));
                    break;
                case 3:
                    option3.setBackgroundTintList(ColorStateList.valueOf(Color.BLUE));
                    break;
                case 4:
                    option4.setBackgroundTintList(ColorStateList.valueOf(Color.BLUE));
                    break;
            }
        }

        // to delayed question after ans;
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                changeQuestion();
            }
        },2000);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        countDownTimer.cancel();
    }
}