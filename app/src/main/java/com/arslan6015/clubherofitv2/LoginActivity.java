package com.arslan6015.clubherofitv2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity {

    TextView signupText;
    Button loginBtn;
    EditText emailTxt,passwordTxt;
    FirebaseUser firebaseUser;
    ProgressBar progressLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar

        setContentView(R.layout.activity_login);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //show the activity in full screen

        progressLogin = findViewById(R.id.progressLogin);
        signupText = findViewById(R.id.signupText);
        loginBtn = findViewById(R.id.loginBtn);
        emailTxt = findViewById(R.id.email);
        passwordTxt = findViewById(R.id.password);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        signupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,SignUpActivity.class)); }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                progressLogin.setVisibility(View.VISIBLE);
                String email = emailTxt.getText().toString();
                String password = passwordTxt.getText().toString();

                if (email.isEmpty() || password.isEmpty()){
                    Toast.makeText(LoginActivity.this,"Make sure all fields are filled", Toast.LENGTH_SHORT).show();
                    progressLogin.setVisibility(View.GONE);
                }else {
                    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(
                            new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
//
//                                mProgressBar.setVisibility(View.INVISIBLE);
//                                mRlFadingLayout.setVisibility(View.INVISIBLE);

                                    if (task.isSuccessful()) {
                                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                        finish();
                                    } else {
                                        Toast.makeText(LoginActivity.this, task.getException().getLocalizedMessage() + " :error", Toast.LENGTH_LONG)
                                                .show();
                                        progressLogin.setVisibility(View.GONE);
                                    }
                                }
                            });
                }
            }
        });
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        if (firebaseUser != null)            //check if current user is logged in
        {
            sendUserToHomeActivity();
        }
    }

    private void sendUserToHomeActivity()
    {
        Intent home = new Intent(LoginActivity.this, HomeActivity.class);
        home.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(home);
        finish();
    }

}
