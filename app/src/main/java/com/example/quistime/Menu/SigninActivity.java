package com.example.quistime.Menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.quistime.Models.Login;
import com.example.quistime.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SigninActivity extends AppCompatActivity {
    private EditText txtEmail, txtPassword, txtNama;
    private Button btnDatar;
    private FirebaseAuth auth;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        txtEmail = findViewById(R.id.txtSEmail);
        txtPassword = findViewById(R.id.txtSPass);
        txtNama = findViewById(R.id.txtSNama);
        btnDatar = findViewById(R.id.btnSTambah);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();

        btnDatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tambah();
            }
        });
    }

    public void Tambah(){
        final String email = txtEmail.getText().toString().trim();
        final String password = txtPassword.getText().toString().trim();
        final String nama = txtNama.getText().toString().trim();

        if(nama.isEmpty()) {
            txtNama.setError("Masukkan Nama");
        }else if(email.isEmpty()){
            txtEmail.setError("Isikan Email");
        }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            txtEmail.setError("Masukkan Email Valid");
        }else if (password.isEmpty()){
            txtPassword.setError("Isikan Password");
        }else if (password.length() < 6){
            txtPassword.setError("Password Minimal 6 Karakter");
        }else {
            auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(SigninActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        tambahData(new Login(email, password, nama));
                        Intent intent = new Intent(SigninActivity.this,LoginDosenActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }else {
                        Toast.makeText(SigninActivity.this, "Gagal Tambah Login "+ task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    private void tambahData(Login login){
        database.child("Dosen").push().setValue(login).addOnSuccessListener(SigninActivity.this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(SigninActivity.this, "Berhasil Login", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
