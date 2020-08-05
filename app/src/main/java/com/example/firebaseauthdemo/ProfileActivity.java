package com.example.firebaseauthdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;

    private TextView textView_userEmail;
    private Button button_logout;

    private DatabaseReference databaseReference;

    private EditText editText_name, editText_address;

    private Button button_save;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        databaseReference = FirebaseDatabase.getInstance().getReference();

        editText_name = (EditText) findViewById(R.id.editText_name);
        editText_address = (EditText) findViewById(R.id.editText_address);
        button_save = (Button) findViewById(R.id.button_save);



        FirebaseUser user = firebaseAuth.getCurrentUser();

        textView_userEmail = (TextView) findViewById(R.id.textView_userEmail);
        textView_userEmail.setText("Welcome " + user.getEmail());
        button_logout = (Button) findViewById(R.id.button_logout);

        button_logout.setOnClickListener(this);
        button_save.setOnClickListener(this);
    }

    private void saveUserInformation() {
        String name = editText_name.getText().toString().trim();
        String add = editText_address.getText().toString().trim();

        UserInformation userInformation = new UserInformation(name, add);

        FirebaseUser user = firebaseAuth.getCurrentUser();

        databaseReference.child(user.getUid()).setValue(userInformation);

        Toast.makeText(this, "Information Saved!" , Toast.LENGTH_LONG).show();
    }
    @Override
    public void onClick(View view) {

        if(view == button_logout) {
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        if(view == button_save) {
            saveUserInformation();
        }
    }
}