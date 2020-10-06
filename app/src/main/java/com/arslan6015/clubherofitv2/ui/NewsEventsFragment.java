package com.arslan6015.clubherofitv2.ui;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.arslan6015.clubherofitv2.Adapters.NewsAdapter;
import com.arslan6015.clubherofitv2.Model.NewsList;
import com.arslan6015.clubherofitv2.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NewsEventsFragment extends Fragment {

    //Firebase
    FirebaseDatabase db;
    DatabaseReference itemListNews;
    RecyclerView recycler_news;
    RecyclerView.LayoutManager layoutManager;
    private List<NewsList> newsLists;
    //I use RollNoAdapter as static because in the Viewholder i have to call.
    public static NewsAdapter adapterNews;
//    private String saveCurrentDate, saveCurrentTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View root = inflater.inflate(R.layout.fragment_news_events, container, false);
        db = FirebaseDatabase.getInstance();
        itemListNews = db.getReference().child("News");


        recycler_news = root.findViewById(R.id.recycler_news);
        recycler_news.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recycler_news.setLayoutManager(layoutManager);
        newsLists = new ArrayList<>();
//        add predefined firebase listener
        itemListNews.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    //use foreach loop
                    newsLists.clear();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        //get value from the model class
                        NewsList l = postSnapshot.getValue(NewsList.class);
//                        Log.e("TAG",l.getObtainedMarks());
                        //add these values inside Arraylist
                        Log.e("TAG", postSnapshot.getKey());
                        newsLists.add(l);
                    }
                    //pass the ArrayList in the constructor of RollNoAdapter
                    adapterNews = new NewsAdapter(getContext(), newsLists);
                    //After completing the process of Adapter set the adapter to the recyclerview
                    recycler_news.setAdapter(adapterNews);
                    //Whenever the data is changed it will inform the adapter
                    adapterNews.notifyDataSetChanged();
                }
            }

            //In case of any error.
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("TAGError", "The read failed: " + databaseError.getMessage());
            }
        });

        Log.e("TAG", "onCreateView");
        return root;
    }

}
