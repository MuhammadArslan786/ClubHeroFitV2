package com.arslan6015.clubherofitv2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.arslan6015.clubherofitv2.Model.UserGeneralInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    TextView GoToLoginText;
    Button SignUpBtn;
    EditText emailSignUp,passwordSignUp,confimrPasswordSignUp,fullNameSignUp;
    ProgressBar progressSignUp;
    FirebaseDatabase database;
    DatabaseReference dbRefrence;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        database = FirebaseDatabase.getInstance();
        dbRefrence = database.getReference("UserInfo");

        fullNameSignUp = findViewById(R.id.fullNameSignUp);
        progressSignUp = findViewById(R.id.progressSignUp);
        emailSignUp = findViewById(R.id.emailSignUp);
        passwordSignUp = findViewById(R.id.passwordSignUp);
        confimrPasswordSignUp = findViewById(R.id.confimrPasswordSignUp);

        GoToLoginText = findViewById(R.id.GoToLoginText);
        GoToLoginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
            }
        });

        SignUpBtn = findViewById(R.id.SignUpBtn);
        SignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressSignUp.setVisibility(View.VISIBLE);
                final String fullName = fullNameSignUp.getText().toString();
                String pass = passwordSignUp.getText().toString();
                String confirmPass = confimrPasswordSignUp.getText().toString();
                final String email = emailSignUp.getText().toString();

                if (!pass.equals(confirmPass)) {
                    Toast.makeText(SignUpActivity.this, "Password Doesn't match", Toast.LENGTH_LONG).show();
                    progressSignUp.setVisibility(View.GONE);
                }else if (email.isEmpty() || pass.isEmpty() || confirmPass.isEmpty()){
                    Toast.makeText(SignUpActivity.this, "Make sure to fill all fields", Toast.LENGTH_LONG).show();
                    progressSignUp.setVisibility(View.GONE);
                } else {
                    progressSignUp.setVisibility(View.VISIBLE);
//                    mRlFading.setVisibility(View.VISIBLE);
                    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(
                            new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {

                                        UserGeneralInfo userGeneralInfo = new UserGeneralInfo(fullName,email);

                                        dbRefrence.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .setValue(userGeneralInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(SignUpActivity.this, "SignUp Complete", Toast.LENGTH_SHORT)
                                                        .show();
                                            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                                            finish();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT)
                                                        .show();
                                                Log.e("TAG",e.getMessage());
                                            }
                                        });

//
//                                        Toast.makeText(SignUpActivity.this, "SignUp Complete", Toast.LENGTH_SHORT)
//                                                .show();
//                                        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
//                                        finish();
                                    } else {
                                        progressSignUp.setVisibility(View.GONE);
                                        Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG)
                                                .show();
                                    }

                                }
                            });
                }
            }

        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}