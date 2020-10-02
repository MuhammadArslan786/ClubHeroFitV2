package com.arslan6015.clubherofitv2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.arslan6015.clubherofitv2.Adapters.ContactsAdapter;
import com.arslan6015.clubherofitv2.Model.ContactInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.arslan6015.clubherofitv2.ui.contacts.ContactsFragment.adapterContacts;

public class ContactsFriend extends AppCompatActivity {

    FirebaseDatabase db;
    DatabaseReference itemListContacts;
    String nameIntent,emailIntent,idIntent,profileImageIntent;
    CircleImageView profile_image_con;
    TextView name_con,email_con;
    Button addBtn;
    RecyclerView recycler_contacts_info;
    RecyclerView.LayoutManager layoutManager;
    private List<ContactInfo> contactsLists;
    FirebaseAuth mAuth;
    String currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_friend);

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Contact Info");
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser().getUid();

        Intent intent = getIntent();
        nameIntent = intent.getStringExtra("txtName");
        emailIntent = intent.getStringExtra("txtEmail");
        idIntent = intent.getStringExtra("txtId");
        profileImageIntent = intent.getStringExtra("txtProfileImage");

        Log.e("TAGID",idIntent);

        db = FirebaseDatabase.getInstance();
        itemListContacts = db.getReference().child("UserInfo");


        profile_image_con = findViewById(R.id.profile_image_con);
        name_con = findViewById(R.id.name_con);
        email_con = findViewById(R.id.email_con);
        addBtn = findViewById(R.id.addBtn);

        Picasso.get().load(profileImageIntent).placeholder(R.drawable.maleicon).into(profile_image_con);
        name_con.setText(nameIntent);
        email_con.setText(emailIntent);

        recycler_contacts_info = findViewById(R.id.recycler_contacts_info);
        recycler_contacts_info.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_contacts_info.setLayoutManager(layoutManager);
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
                    adapterContacts = new ContactsAdapter(ContactsFriend.this, contactsLists);
                    //After completing the process of Adapter set the adapter to the recyclerview
                    recycler_contacts_info.setAdapter(adapterContacts);
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

        Log.e("TAGC",currentUser);
        Log.e("TAGID",idIntent);

        if (idIntent.equals(currentUser)) {
            addBtn.setAlpha(0.5f);
            addBtn.setClickable(false);
        }

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (idIntent.equals(currentUser)){
                    Toast.makeText(ContactsFriend.this,"You can not send message to yourself",Toast.LENGTH_LONG).show();
                }else {
                    Intent intent1 = new Intent(ContactsFriend.this,ChatsActivity.class);
                    intent1.putExtra("ChatPersonId",idIntent);
                    intent1.putExtra("ChatPersonName",nameIntent);
                    intent1.putExtra("ChatPersonEmail",emailIntent);
                    intent1.putExtra("ChatPersonImage",profileImageIntent);
                    startActivity(intent1);
                }

            }
        });

    }


    @Override
    public boolean onSupportNavigateUp() {
//        startActivity(new Intent(ContactsFriend.this));
        onBackPressed();
        return true;
    }
}