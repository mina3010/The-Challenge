package com.example.sharara.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.sharara.Adapter.AdapterResult;
import com.example.sharara.Data.Database;
import com.example.sharara.Model.ResultScore;
import com.example.sharara.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class ResultsActivity extends AppCompatActivity {

    RecyclerView rv;
    Database db;
    List<ResultScore>scoreList= new ArrayList<>();
    List<ResultScore> newScoreList = new ArrayList<>();
    List<ResultScore> newScore = new ArrayList<>();
    AdapterResult adapter;
    ResultScore rs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        rv = findViewById(R.id.result_RV);

        db = new Database(this);
        scoreList = db.getAllResults();
        Collections.sort(scoreList);



        scoreList = new ArrayList<ResultScore>(new LinkedHashSet<ResultScore>(scoreList));
        for(ResultScore i : scoreList){
            if(!newScoreList.contains(i)){
                newScoreList.add(i);
            }
        }
        Collections.sort(newScoreList);

        adapter = new AdapterResult(newScoreList);
        rv.setAdapter(adapter);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(lm);
        //Collections.sort(scoreList.get(CategoryActivity.selected_category_index).getSortScore());
        rv.setHasFixedSize(true);
        adapter.notifyDataSetChanged();

    }

}