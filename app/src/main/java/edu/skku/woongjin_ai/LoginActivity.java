package edu.skku.woongjin_ai;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    EditText editTextID, editTextPW;
    String id, pw;
    DatabaseReference mPostReference;
    Intent intent;
    int flag = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button buttonLogin = (Button) findViewById(R.id.buttonLogin);
        Button buttonRegister = (Button) findViewById(R.id.buttonRegister);
        editTextID = (EditText) findViewById(R.id.editTextID);
        editTextPW = (EditText) findViewById(R.id.editTextPW);

        mPostReference = FirebaseDatabase.getInstance().getReference();

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = editTextID.getText().toString();
                pw = editTextPW.getText().toString();

                if(id.length() == 0 || pw.length() == 0) {
                    Toast.makeText(LoginActivity.this, "Fill all blanks", Toast.LENGTH_SHORT).show();
                } else {
                    final ValueEventListener postListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String key = snapshot.getKey();
                                if(id.equals(key)) {
                                    UserInfo get = snapshot.getValue(UserInfo.class);
                                    String password = get.pw;
                                    if(pw.equals(password)) {
                                        flag = 1;
                                        intent = new Intent(LoginActivity.this, MainActivity.class);
                                        intent.putExtra("id", id);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            }
                            if(flag == 0) {
                                Toast.makeText(LoginActivity.this, "Wrong login", Toast.LENGTH_SHORT).show();
                                editTextID.setText("");
                                editTextPW.setText("");
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {         }
                    };
                    mPostReference.child("user_list").addValueEventListener(postListener);
                }
            }
        });
    }
}
