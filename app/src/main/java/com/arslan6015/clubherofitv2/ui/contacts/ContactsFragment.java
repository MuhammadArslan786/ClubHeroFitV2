package com.arslan6015.clubherofitv2.ui.contacts;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.arslan6015.clubherofitv2.Adapters.ContactsAdapter;
import com.arslan6015.clubherofitv2.ContactsFriend;
import com.arslan6015.clubherofitv2.Model.ContactInfo;
import com.arslan6015.clubherofitv2.Model.UserGeneralInfo;
import com.arslan6015.clubherofitv2.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class ContactsFragment extends Fragment {

    LinearLayout firstLayout;
    RecyclerView recycler_contact;
    //Firebase
    FirebaseDatabase db;
    DatabaseReference itemListContacts;
    RecyclerView.LayoutManager layoutManager;
    private List<ContactInfo> contactsLists;
    public static ContactsAdapter adapterContacts;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_contacts, container, false);

        db = FirebaseDatabase.getInstance();
        itemListContacts = db.getReference().child("UserInfo");

        recycler_contact = root.findViewById(R.id.recycler_contact);
        recycler_contact.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recycler_contact.setLayoutManager(layoutManager);
        contactsLists = new ArrayList<>();
//        add predefined firebase listener
        itemListContacts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    //use foreach loop
                    contactsLists.clear();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        //get value from the model class
                        ContactInfo l = postSnapshot.getValue(ContactInfo.class);
//                        Log.e("TAG",l.getObtainedMarks());
                        //add these values inside Arraylist
                        Log.e("TAG", postSnapshot.getKey());
                        contactsLists.add(l);
                    }
                    //pass the ArrayList in the constructor of RollNoAdapter
                    adapterContacts = new ContactsAdapter(getContext(), contactsLists);
                    //After completing the process of Adapter set the adapter to the recyclerview
                    recycler_contact.setAdapter(adapterContacts);
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


        return root;
    }
}