package com.example.sharara.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sharara.Data.Database;
import com.example.sharara.Model.ResultScore;
import com.example.sharara.UI.CategoryActivity;
import com.example.sharara.UI.QuestionActivity;
import com.example.sharara.R;
import com.example.sharara.UI.ScoreActivity;
import com.example.sharara.UI.SetsActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.sharara.UI.ScoreActivity.score_str;
import static com.example.sharara.UI.SetsActivity.listScore;
import static com.example.sharara.UI.SetsActivity.setsID;

public class AdapterSets extends RecyclerView.Adapter<AdapterSets.ViewHolder> {
    private int numOfSets;
    private Database db;
    public static List<ResultScore> scores = new ArrayList<>();
    private Context context;
    public static int pos;

    public AdapterSets(int numOfSets, Context context) {
        this.numOfSets = numOfSets;
        this.context = context;
    }


    @Override
    public AdapterSets.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.set, parent, false);
        return new AdapterSets.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterSets.ViewHolder holder, final int position) {

        pos = position;
        holder.num.setText(String.valueOf(position + 1));

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if(score_str==null||score_str.isEmpty()){
                Intent intent = new Intent(context, QuestionActivity.class);
                intent.putExtra("SetNo", position);
                context.startActivity(intent);
//                }else {
//                    Toast.makeText(context, "Sorry you can't try again this level", Toast.LENGTH_SHORT).show();
//                }

            }

        });
    }

    @Override
    public int getItemCount() {
        return numOfSets;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView num;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            num = itemView.findViewById(R.id.sets_number);
            cardView = itemView.findViewById(R.id.sets_card);
        }
    }
}
       // public void insert(int position, String score_str) {

//            db = new Database(itemView.getContext());
//            ResultScore res =new ResultScore(CategoryActivity.selected_category_index,CategoryActivity.selected_category_name,position+1,score_str);
//            db.insert(res);

//            ArrayList<ResultScore> Sco= db.getCustomerScore(CategoryActivity.selected_category_index,position+1);
//            ResultScore fSco= Sco.get(position+1);
//            fScore=String.valueOf(fSco);



