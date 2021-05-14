package com.example.sharara.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sharara.Model.Category;
import com.example.sharara.Model.ResultScore;
import com.example.sharara.R;
import com.example.sharara.UI.CategoryActivity;
import com.example.sharara.UI.SetsActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.sharara.UI.CategoryActivity.selected_category_index;

public class AdapterResult extends RecyclerView.Adapter<AdapterResult.ViewHolder> {
    List<ResultScore>catList=new ArrayList<>();
    Context context;
    int sco,scoLen;

    public AdapterResult(List<ResultScore> catList, Context context) {
        this.catList = catList;
        this.context = context;
    }

    public AdapterResult(List<ResultScore> catList) {
        this.catList = catList;
    }

    @Override
    public AdapterResult.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.result, parent, false);
        return new AdapterResult.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterResult.ViewHolder holder, final int position) {

        holder.category_name.setText(catList.get(position).getCategory_name());
        holder.level.setText(String.valueOf(catList.get(position).getLevel()));
        holder.score.setText(String.valueOf(catList.get(position).getScore()));
        holder.scoreLen.setText(String.valueOf(catList.get(position).getScore_len()));
        sco = catList.get(position).getScore();
        scoLen = catList.get(position).getScore_len();
            if (sco ==0){
                Drawable img =holder.imageView.getContext().getResources().getDrawable(R.drawable.star_empty);
                holder.imageView.setImageDrawable(img);
            }else if(sco <= scoLen/2 && sco != 0){
                Drawable img =holder.imageView.getContext().getResources().getDrawable(R.drawable.star1);
                holder.imageView.setImageDrawable(img);
            }else if (sco > scoLen/2 && sco != scoLen){
                Drawable img =holder.imageView.getContext().getResources().getDrawable(R.drawable.star_semi);
                holder.imageView.setImageDrawable(img);
            }else if (sco == scoLen){
                Drawable img =holder.imageView.getContext().getResources().getDrawable(R.drawable.star_full);
                holder.imageView.setImageDrawable(img);
            }


    }

    @Override
    public int getItemCount() {
        return catList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView category_name,level,score,scoreLen;
        CardView  cardView;
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            category_name=itemView.findViewById(R.id.catNameResult);
            level=itemView.findViewById(R.id.levelResult);
            score=itemView.findViewById(R.id.scoreResult);
            imageView=itemView.findViewById(R.id.imgResult);
            scoreLen=itemView.findViewById(R.id.scoreResult2);

        }

    }
}
