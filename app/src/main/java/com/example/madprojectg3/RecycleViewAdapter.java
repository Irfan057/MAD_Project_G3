package com.example.madprojectg3;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.RecycleViewHolder> {


    List<Article> Articlelist;
    Context context;
    public RecycleViewAdapter(List<Article> articlelist, Context context) {
        Articlelist = articlelist;
        this.context = context;
    }
    public void setFilteredList(List<Article> filteredList){
        this.Articlelist = filteredList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public RecycleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_article,parent,false);
        RecycleViewHolder holder = new RecycleViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewHolder holder, int position) {
        holder.TVArticle.setText(Articlelist.get(position).getKeyword());
        Glide.with(this.context).load(Articlelist.get(position).getPhotoUrl()).into(holder.IVArticle);
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WebSearch.class);
                intent.putExtra("SearchURL",Articlelist.get(position).getUrl()); // query contains search string
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Articlelist.size();
    }

    public class RecycleViewHolder extends RecyclerView.ViewHolder {
        ImageView IVArticle;
        TextView TVArticle;
        ConstraintLayout parentLayout;
        public RecycleViewHolder(View ItemView){
            super(ItemView);
            TVArticle = ItemView.findViewById(R.id.TVKeyword);
            IVArticle = ItemView.findViewById(R.id.IVArticle);
            parentLayout = ItemView.findViewById(R.id.one_article_layout);
        }
    }
}