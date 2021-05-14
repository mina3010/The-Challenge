package com.example.sharara.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.sharara.Model.Category;
import com.example.sharara.R;
import com.example.sharara.UI.CategoryActivity;
import com.example.sharara.UI.SetsActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.sharara.UI.CategoryActivity.selected_category_index;

public class AdapterCategory extends RecyclerView.Adapter<AdapterCategory.ViewHolder> {
    List<Category>catList=new ArrayList<>();
    Context context;
    RecyclerView mRecyclethrView;

    public AdapterCategory(List<Category> catList, Context context) {
        this.catList = catList;
        this.context = context;
    }


    @Override
    public AdapterCategory.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new AdapterCategory.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterCategory.ViewHolder holder, final int position) {

        holder.category_name.setText(catList.get(position).getName());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected_category_index=position;
                CategoryActivity.selected_category_name=catList.get(position).getName();
                Intent intent = new Intent(context, SetsActivity.class);
//                intent.putExtra("Category",catList.get(position).getName());
//                intent.putExtra("Category_id",position+1);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return catList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout Rv;
        TextView category_name;
        CardView  cardView;
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            category_name=itemView.findViewById(R.id.category_name);
            cardView=itemView.findViewById(R.id.category_card);
            Rv=itemView.findViewById(R.id.RV_Cat);
            //imageView=itemView.findViewById(R.id.imageCategory);
        }
    }
}
