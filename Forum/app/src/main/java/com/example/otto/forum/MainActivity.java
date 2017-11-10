package com.example.otto.forum;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.otto.forum.Authentication.DashBoard;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static com.example.otto.forum.R.layout.activity_login;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button loginButton,registerButton;
    EditText emailInputBox,passwordInputBox;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_login);

        //view

        loginButton = (Button)findViewById(R.id.logInButton);
        registerButton = (Button)findViewById(R.id.registerButton);
        emailInputBox = (EditText)findViewById(R.id.emailInputBox);
        passwordInputBox = (EditText)findViewById(R.id.passwordInputBox);


        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
        //authentication
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {

            startActivity(new Intent(MainActivity.this, DashBoard.class));
            finish();
        }

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.logInButton) {
            loginUser(emailInputBox.getText().toString().trim(),passwordInputBox.getText().toString().trim());
        } else if (view.getId() == R.id.registerButton) {
            registerUser();
        }
    }

    private void registerUser() {
        auth.createUserWithEmailAndPassword(emailInputBox.getText().toString().trim(),passwordInputBox.getText().toString().trim()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    startActivity(new Intent(MainActivity.this, DashBoard.class));
                    finish();
                } else {
                    Snackbar snackbar = Snackbar.make(passwordInputBox,"Wrong credentials.",Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
            }
        });
    }

    private void loginUser(String email, String pass) {
        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    startActivity(new Intent(MainActivity.this, DashBoard.class));
                    finish();
                } else {
                    Snackbar snackbar = Snackbar.make(passwordInputBox,"Wrong credentials.",Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
            }
        });
    }

}
