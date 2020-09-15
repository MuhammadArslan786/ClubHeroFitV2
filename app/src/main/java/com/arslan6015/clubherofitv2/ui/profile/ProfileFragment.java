package com.arslan6015.clubherofitv2.ui.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.arslan6015.clubherofitv2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    FirebaseDatabase database;
    DatabaseReference mRef;
    TextView fullNameProfile, emailProfile;
    CircleImageView profile_image, profile_add;
    FirebaseAuth mAuth;
    String currentUserId;
    DatabaseReference Rootref;
    StorageReference UserProfileImageRef;
    private static final int GalleryPick = 1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        fullNameProfile = root.findViewById(R.id.fullNameProfile);
        emailProfile = root.findViewById(R.id.emailProfile);
        profile_image = root.findViewById(R.id.profile_image);
        profile_add = root.findViewById(R.id.profile_add);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        Rootref = FirebaseDatabase.getInstance().getReference();
        UserProfileImageRef = FirebaseStorage.getInstance().getReference("Profile Images");

        database = FirebaseDatabase.getInstance();
        mRef = database.getReference("UserInfo")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("fullName");

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                fullNameProfile.setText(value);
                emailProfile.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                Log.d("TAG", "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });

        profile_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GalleryPick);

//                Intent chooser = Intent.createChooser()
//                chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] { cameraIntent });
//                startActivityForResult(chooser, GalleryPick);
            }
        });

        retriverUserProfileImage();
        return root;
    }

    private void retriverUserProfileImage() {
        Rootref.child("UserInfo").child(currentUserId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("image"))) {
                            String Image = dataSnapshot.child("image").getValue().toString();
                            Picasso.get().load(Image).into(profile_image);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("TAG", "RetriveUserInfoError: " + error.getMessage());
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GalleryPick && resultCode == RESULT_OK && data != null) {
            Log.e("TAG", "called");
            Uri ImageUri = data.getData();
            Log.e("TAG", "ImageUri: " + ImageUri);

            StorageReference filepath = UserProfileImageRef.child(currentUserId + ".jpg");
            final UploadTask uploadTask = filepath.putFile(ImageUri);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String downloadUrl = uri.toString();
                            Rootref.child("UserInfo").child(currentUserId).child("image").setValue(downloadUrl)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(getContext(), "Image Uploaded", Toast.LENGTH_LONG).show();
                                        }
                                    });
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("TAG", "Error: " + e.getMessage());
                }
            });
        }

    }
}