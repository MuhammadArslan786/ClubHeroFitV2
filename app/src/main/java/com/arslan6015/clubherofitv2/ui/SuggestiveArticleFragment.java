package com.arslan6015.clubherofitv2.ui;

import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arslan6015.clubherofitv2.Adapters.ArticleAdapter;
import com.arslan6015.clubherofitv2.Model.Post;
import com.arslan6015.clubherofitv2.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SuggestiveArticleFragment extends Fragment {

    //Firebase
    FirebaseDatabase db;
    DatabaseReference itemListArticles;
    FirebaseUser currentUser;
    RecyclerView recycler_article;
    RecyclerView.LayoutManager layoutManager;
    private List<Post> articleLists;
    //I use RollNoAdapter as static because in the Viewholder i have to call.
    public static ArticleAdapter articleAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_suggestive_article, container, false);
        db = FirebaseDatabase.getInstance();
        itemListArticles = db.getReference().child("Articles");
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        recycler_article = root.findViewById(R.id.recycler_article);
        recycler_article.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recycler_article.setLayoutManager(layoutManager);
        articleLists = new ArrayList<>();
//        add predefined firebase listener
        itemListArticles.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    //use foreach loop
                    articleLists.clear();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        //get value from the model class
                        Post l = postSnapshot.getValue(Post.class);
//                        Log.e("TAG",l.getObtainedMarks());
                        //add these values inside Arraylist
                        Log.e("TAG", postSnapshot.getKey());
                        articleLists.add(l);
                    }
                    //pass the ArrayList in the constructor of RollNoAdapter
                    articleAdapter = new ArticleAdapter(getContext(), articleLists);
                    //After completing the process of Adapter set the adapter to the recyclerview
                    recycler_article.setAdapter(articleAdapter);
                    //Whenever the data is changed it will inform the adapter
                    articleAdapter.notifyDataSetChanged();
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