package com.example.loginandsignupactivitytutorial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignupActivity extends AppCompatActivity {
    TextView textViewAlreadyHaveAnAccount;
    EditText editTextPersonName, editTextSignupEmail, editTextSignupPassword, editTextConfirmPassword;
    AppCompatButton signupButton;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        editTextPersonName = findViewById(R.id.editTextPersonName);
        editTextSignupEmail = findViewById(R.id.editTextSignupEmail);
        editTextSignupPassword = findViewById(R.id.editTextSignupPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);



        signupButton = findViewById(R.id.signupButton);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performAuthentication();
            }
        });

        textViewAlreadyHaveAnAccount = findViewById(R.id.textViewAlreadyHaveAnAccount);
        textViewAlreadyHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            }
        });
    }

    private void performAuthentication() {
        String signupUserName = editTextPersonName.getText().toString();
        String signupEmail = editTextSignupEmail.getText().toString();
        String signupPassword = editTextSignupPassword.getText().toString();
        String confirmPassword = editTextConfirmPassword.getText().toString();
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        if(signupUserName.isEmpty() || signupUserName.length() < 4){
            editTextPersonName.setError("Enter username above 4 characters");
        }
        else if(!signupEmail.matches(emailPattern)) {
            editTextSignupEmail.setError("Enter Valid Email Address ");
            editTextSignupEmail.requestFocus();
            Toast.makeText(SignupActivity.this, "Enter Valid Email Address ", Toast.LENGTH_SHORT).show();
        } else if(signupPassword.isEmpty()){
            editTextSignupPassword.setError("Your Password is empty. Enter Password");
            if (signupPassword.length() < 8) {
                editTextSignupPassword.setError("Password should contain 8 or more characters");
            }
        } else if(!signupPassword.matches(confirmPassword)) {
            editTextConfirmPassword.setError("Password not match in Both fields");
        }
        else {
            progressDialog.setTitle("Registering");
            progressDialog.setMessage("Please wait, we are Registering your Account");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(signupEmail,signupPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        sendUserToNextActivity();
                        Toast.makeText(SignupActivity.this, "Registration Completed", Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(SignupActivity.this, "ERROR: "+task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    private void sendUserToNextActivity() {
        Intent intent = new Intent(SignupActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}