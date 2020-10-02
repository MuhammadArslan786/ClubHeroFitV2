package com.arslan6015.clubherofitv2.ui.chats;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SearchView;

import com.arslan6015.clubherofitv2.Adapters.ContactsAdapter;
import com.arslan6015.clubherofitv2.Model.ContactInfo;
import com.arslan6015.clubherofitv2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatsListFragment extends Fragment {

    LinearLayout firstLayout;
    RecyclerView recycler_chats;
    //Firebase
    FirebaseDatabase db;
    DatabaseReference itemListChats;
    RecyclerView.LayoutManager layoutManager;
    private List<ContactInfo> chatsLists;
    private List<ContactInfo> searchList;
    public static ContactsAdapter adapterContacts;
    String currentUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_chats_list, container, false);

        db = FirebaseDatabase.getInstance();
        itemListChats = db.getReference().child("ChatsList");

        currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        recycler_chats = root.findViewById(R.id.recycler_chats);
        recycler_chats.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recycler_chats.setLayoutManager(layoutManager);
        chatsLists = new ArrayList<>();
        searchList = new ArrayList<>();
//        add predefined firebase listener
        itemListChats.child(currentUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    //use foreach loop
                    chatsLists.clear();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        //get value from the model class
                        ContactInfo l = postSnapshot.getValue(ContactInfo.class);
//                        Log.e("TAG",l.getObtainedMarks());
                        //add these values inside Arraylist
                        Log.e("TAG", postSnapshot.getKey());
                        chatsLists.add(l);
                    }
                    //pass the ArrayList in the constructor of RollNoAdapter
                    adapterContacts = new ContactsAdapter(getContext(), chatsLists);
                    //After completing the process of Adapter set the adapter to the recyclerview
                    recycler_chats.setAdapter(adapterContacts);
                    //Whenever the data is changed it will inform the adapter
                    adapterContacts.notifyDataSetChanged();
                }
            }

            //In case of any error.
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("TAGError", "The read failed: " + databaseError.getMessage());
            }
        });
//        processSearch("A");
        //set color to searchview
        SearchView search_view_chats = root.findViewById(R.id.search_view_chats);
        search_view_chats.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                processSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                processSearch(newText);
                return false;
            }
        });
//        searchAutoComplete.sethin(getResources().getColorandroid.R.color.white));
//        searchAutoComplete.setTextColor(getResources().getColor(android.R.color.white));
        return root;
    }


    private void processSearch(String newText) {
        itemListChats.child(currentUser).orderByChild("fullName").startAt(newText).endAt(newText + "\uf8ff").
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            //use foreach loop
                            searchList.clear();
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                //get value from the model class
                                ContactInfo l = postSnapshot.getValue(ContactInfo.class);
//                        Log.e("TAG",l.getObtainedMarks());
                                //add these values inside Arraylist
                                Log.e("TAG", postSnapshot.getKey());
                                searchList.add(l);
                            }
                            Log.e("TAG", "listSize: " + searchList.size());
                            //pass the ArrayList in the constructor of RollNoAdapter
                            adapterContacts = new ContactsAdapter(getContext(), searchList);
                            //After completing the process of Adapter set the adapter to the recyclerview
                            recycler_chats.setAdapter(adapterContacts);
                            //Whenever the data is changed it will inform the adapter
                            adapterContacts.notifyDataSetChanged();
                        }
                    }

                    //In case of any error.
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("TAGError", "The read failed: " + databaseError.getMessage());
                    }
                });
    }
}