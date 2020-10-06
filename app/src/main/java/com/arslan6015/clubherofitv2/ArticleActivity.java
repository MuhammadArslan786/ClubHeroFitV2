package com.arslan6015.clubherofitv2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ArticleActivity extends AppCompatActivity {
    TextView article_title, article_desp, article_upload_time;
    ImageView article_img;

    Dialog popAddPost;
    ImageView popupPostImage, popupAddBtn;
    TextView popupTitle, popupDescription;
    ProgressBar popupClickProgress;
    private Uri pickedImgUri = null;
    private static final int REQUESCODE = 2;
    String intImg, intTitle, intDesp, intPostkey, intTimeStamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        intTitle = intent.getStringExtra("intentArtTitle");
        intDesp = intent.getStringExtra("intentArtDesp");
        intImg = intent.getStringExtra("intentArtImg");
        intPostkey = intent.getStringExtra("intentArtPostKey");
        intTimeStamp = intent.getStringExtra("intentArtTimeStamp");
        getSupportActionBar().setTitle(intTitle);

        article_title = findViewById(R.id.article_title);
        article_desp = findViewById(R.id.article_desp);
        article_img = findViewById(R.id.article_img);
        article_upload_time = findViewById(R.id.article_upload_time);

        article_title.setText(intTitle);
        article_desp.setText(intDesp);
        Picasso.get().load(intImg).into(article_img);
        article_upload_time.setText(intTimeStamp);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}