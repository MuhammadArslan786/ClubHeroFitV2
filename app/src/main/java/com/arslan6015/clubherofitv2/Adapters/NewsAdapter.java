package com.arslan6015.clubherofitv2.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.recyclerview.widget.RecyclerView;

import com.arslan6015.clubherofitv2.Model.NewsList;
import com.arslan6015.clubherofitv2.R;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;


public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private Context context;
    private List<NewsList> newsLists;

    //in constructor pass the context, & arraylist type object
    public NewsAdapter(Context context, List<NewsList> newsLists) {
        this.context = context;
        this.newsLists = newsLists;
    }

    //in creating adapter we have to implement three method.
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //here list_data.xml layout converts into the view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_data_item, parent, false);
        //Whenever adapter calls return that view into the viewHolder() constructors which is seen down below.
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title_news.setText(newsLists.get(position).getNewsTitle());
        holder.desp_news.setText(newsLists.get(position).getNewsDesp());
        holder.time_news.setText(newsLists.get(position).getUploadTime());
    }

    //Returns the arraylist size.
    @Override
    public int getItemCount() {
        return newsLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // declare list_data.xml Textview
        public TextView title_news, desp_news, time_news;

        //ViewHolder constructor
        public ViewHolder(View itemView) {
            super(itemView);
            //initialize the textview of list_data.xml
            title_news = itemView.findViewById(R.id.title_news);
            desp_news = itemView.findViewById(R.id.desp_news);
            time_news = itemView.findViewById(R.id.time_news);
        }

    }

}