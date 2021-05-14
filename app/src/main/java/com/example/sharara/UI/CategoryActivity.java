package com.example.sharara.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.sharara.Adapter.AdapterCategory;
import com.example.sharara.R;

import static com.example.sharara.UI.SplashActivity.catList;

public class CategoryActivity extends AppCompatActivity {

    public static int selected_category_index=0;
    public static String selected_category_name="";
    RecyclerView R_Ca;
    AdapterCategory AD;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        R_Ca =findViewById(R.id.category_rv);




        //ItemOfCategories = db.getCategories();
        AD = new AdapterCategory(catList,this);
        R_Ca.setAdapter(AD);
        //GridLayoutManager gridLayoutManager=new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        R_Ca.setLayoutManager(lm);
        R_Ca.setHasFixedSize(true);

    }
}