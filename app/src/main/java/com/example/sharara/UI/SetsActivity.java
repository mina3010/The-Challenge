package com.example.sharara.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sharara.Adapter.AdapterSets;
import com.example.sharara.Model.ResultScore;
import com.example.sharara.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import static com.example.sharara.UI.CategoryActivity.selected_category_index;
import static com.example.sharara.UI.SplashActivity.catList;

public class SetsActivity extends AppCompatActivity {

    TextView title;
    RecyclerView Sets_rv;
    AdapterSets Adapter;
    private FirebaseFirestore firestore;
    public static int category_id;
    public static List<String>setsID=new ArrayList<>();
    public static List<ResultScore>listScore=new ArrayList<>();
    GridLayoutManager gridLayoutManager;
    private Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sets);
        Sets_rv =findViewById(R.id.sets_rv);
        title =findViewById(R.id.sets_title);

        loadingDialog=new Dialog(SetsActivity.this);
        loadingDialog.setContentView(R.layout.loading);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawableResource(R.drawable.progress_background);
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();

        firestore=FirebaseFirestore.getInstance();
        category_id=get_catID();
        loadSets();
        gridLayoutManager=new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);
        Sets_rv.setLayoutManager(gridLayoutManager);
        Sets_rv.setHasFixedSize(true);

        String name=get_catName();
        title.setText(name);


    }

    private void loadSets() {

        setsID.clear();

        if (catList!=null||catList.size()!=0||catList.get(selected_category_index).getId() != null || !catList.get(selected_category_index).getId().isEmpty()) {
            firestore.collection("Quiz").document(catList.get(selected_category_index).getId())
                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    long numOfSets = (long) documentSnapshot.get("SETS");

                    String id = "";
                    Log.d("mina1", "" + numOfSets);
                    for (int i = 1; i <= numOfSets; i++) {

                        id = documentSnapshot.getString("SET" + String.valueOf(i) + "_ID");
                        setsID.add(id);

                    }


                    Adapter = new AdapterSets(setsID.size(), SetsActivity.this);
                    Sets_rv.setAdapter(Adapter);
                    loadingDialog.dismiss();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SetsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    loadingDialog.dismiss();
                }
            });
        } else
             {
                Toast.makeText(this, "it is empty", Toast.LENGTH_SHORT).show();
                finish();
             }
    }
    private String get_catName(){
        Intent intent = getIntent();
        String categoryName = intent.getStringExtra("Category");
        return categoryName;
    }
    private int get_catID(){
        Intent intent = getIntent();
        int category_id = intent.getIntExtra("Category_id",1);
        return category_id;
    }
}