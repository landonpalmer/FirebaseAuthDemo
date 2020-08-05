package com.example.firebaseauthdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button button_register;
    private EditText editText_email;
    private EditText editText_password;
    private TextView textView_signIn;

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();

        // Checks to see if user is already signed in
        if(firebaseAuth.getCurrentUser() != null) {
            // profile activity here
            finish();
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        }

        progressDialog = new ProgressDialog(this);

        button_register = (Button) findViewById(R.id.button_register);

        editText_email = (EditText) findViewById(R.id.editText_email);
        editText_password = (EditText) findViewById(R.id.editText_password);

        textView_signIn = (TextView) findViewById(R.id.textView_signIn);

        button_register.setOnClickListener(this);
        textView_signIn.setOnClickListener(this);
    }

    private void registerUser() {
        String email = editText_email.getText().toString().trim();
        String password = editText_password.getText().toString().trim();

        if(TextUtils.isEmpty(email)) {
            // email is empty
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            // stop function from execution further
            return;
        }

        if(TextUtils.isEmpty(password)) {
            // password is empty
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            // stop function from execution further
            return;
        }

        // if validations are ok
        // we will first show a progress bar

        progressDialog.setMessage("Registering User...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            // user is successfully registered and logged in
                            finish();
                            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));

                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Could not register... please try again!", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();

                        }
                    }
                });

    }
    @Override
    public void onClick(View view) {
        if(view == button_register) {
            registerUser();
        }

        if(view == textView_signIn) {
            // will open login activity here
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
}