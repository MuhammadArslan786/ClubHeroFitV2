package com.arslan6015.clubherofitv2.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.arslan6015.clubherofitv2.ArticleActivity;
import com.arslan6015.clubherofitv2.Model.Post;
import com.arslan6015.clubherofitv2.R;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {
    private Context context;
    private List<Post> articleLists;
    private static final int REQUESCODE = 2 ;


    //in constructor pass the context, & arraylist type object
    public ArticleAdapter(Context context, List<Post> articleLists) {
        this.context = context;
        this.articleLists = articleLists;
    }

    //in creating adapter we have to implement three method.
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //here list_data.xml layout converts into the view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_data_item_articles, parent, false);
        //Whenever adapter calls return that view into the viewHolder() constructors which is seen down below.
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.title_article.setText(articleLists.get(position).getTitle());
        holder.desp_articles.setText(articleLists.get(position).getDescription());
        holder.time_article.setText(articleLists.get(position).getTimeStamp());
        Picasso.get().load(articleLists.get(position).getPicture()).into(holder.img_article);

        holder.read_articles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ArticleActivity.class);
                intent.putExtra("intentArtTitle",articleLists.get(position).getTitle());
                intent.putExtra("intentArtDesp",articleLists.get(position).getDescription());
                intent.putExtra("intentArtImg",articleLists.get(position).getPicture());
                intent.putExtra("intentArtPostKey",articleLists.get(position).getPostKey());
                intent.putExtra("intentArtTimeStamp",articleLists.get(position).getTimeStamp());
                context.startActivity(intent);
            }
        });
    }

    //Returns the arraylist size.
    @Override
    public int getItemCount() {
        return articleLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // declare list_data.xml Textview
        public TextView title_article, desp_articles, read_articles, time_article;
        private ImageView img_article;

        //ViewHolder constructor
        public ViewHolder(View itemView) {
            super(itemView);
            //initialize the textview of list_data.xml
            title_article = itemView.findViewById(R.id.title_article);
            desp_articles = itemView.findViewById(R.id.desp_articles);
            img_article = itemView.findViewById(R.id.img_article);
            read_articles = itemView.findViewById(R.id.read_articles);
            time_article = itemView.findViewById(R.id.time_article);

        }
    }
}