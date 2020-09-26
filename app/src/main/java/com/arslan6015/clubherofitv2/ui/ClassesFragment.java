package com.arslan6015.clubherofitv2.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.arslan6015.clubherofitv2.Adapters.ClassesAdapter;
import com.arslan6015.clubherofitv2.Model.ClassesList;
import com.arslan6015.clubherofitv2.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ClassesFragment extends Fragment {

    EditText name, time;

    //Firebase
    FirebaseDatabase db;
    DatabaseReference itemListClasses;
    RecyclerView recycler_classes;
    RecyclerView.LayoutManager layoutManager;
    private List<ClassesList> classesLists;
    //I use RollNoAdapter as static because in the Viewholder i have to call.
    public static ClassesAdapter adapterClasses;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_classes, container, false);
        db = FirebaseDatabase.getInstance();
        itemListClasses = db.getReference().child("Classes");

        recycler_classes = root.findViewById(R.id.recycler_classes);
        recycler_classes.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recycler_classes.setLayoutManager(layoutManager);
        classesLists = new ArrayList<>();
//        add predefined firebase listener
        itemListClasses.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    //use foreach loop
                    classesLists.clear();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        //get value from the model class
                        ClassesList l = postSnapshot.getValue(ClassesList.class);
//                        Log.e("TAG",l.getObtainedMarks());
                        //add these values inside Arraylist
                        Log.e("TAG", postSnapshot.getKey());
                        classesLists.add(l);
                    }
                    //pass the ArrayList in the constructor of RollNoAdapter
                    adapterClasses = new ClassesAdapter(getContext(), classesLists);
                    //After completing the process of Adapter set the adapter to the recyclerview
                    recycler_classes.setAdapter(adapterClasses);
                    //Whenever the data is changed it will inform the adapter
                    adapterClasses.notifyDataSetChanged();
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