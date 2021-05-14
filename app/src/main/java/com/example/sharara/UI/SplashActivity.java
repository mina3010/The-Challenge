package com.example.sharara.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sharara.Model.Category;
import com.example.sharara.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class SplashActivity extends AppCompatActivity {

    public static List<Category> catList=new ArrayList<>();
    private FirebaseFirestore firestore;
    TextView appName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        appName=findViewById(R.id.App_name);

        Typeface typeface= ResourcesCompat.getFont(this,R.font.chicken_quiche);
        appName.setTypeface(typeface);

        Animation anim = AnimationUtils.loadAnimation(this,R.anim.myanim);
        appName.setAnimation(anim);

        firestore =FirebaseFirestore.getInstance();

        new Thread(){
            @Override
            public void run() {
                // sleep(3000);
                loadData();

            }
        }.start();
        }

    private void loadData() {
        catList.clear();
        firestore.collection("Quiz").document("Category").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot doc =task.getResult();

                    if (doc.exists()){
                        long count = (long)doc.get("COUNT");
                        for(int i=1 ;i<=count ;i++){
                            String catName= doc.getString("CAT" + String.valueOf(i) + "_NAME");
                            String catID= doc.getString("CAT" + String.valueOf(i) + "_ID");
                            catList.add(new Category(catID,catName));
                        }

                        startActivity(new Intent(SplashActivity.this,Intro_app.class));
                        SplashActivity.this.finish();


                    }else {
                        Toast.makeText(SplashActivity.this, "No Category Document Exists ", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }else {
                    Toast.makeText(SplashActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}