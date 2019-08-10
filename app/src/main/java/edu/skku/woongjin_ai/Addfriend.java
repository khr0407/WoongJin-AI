package edu.skku.woongjin_ai;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Addfriend extends AppCompatActivity {
    private DatabaseReference mPostReference;
    ListView friend_list;
    ArrayList<String> data;
    ArrayAdapter<String> arrayAdapter;
    String id_key;
    Intent intent, intentAddFriend;
    Button addfriend;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addfriend);


        //friend_list = findViewById(R.id.user_list);

        //data = new ArrayList<String>();

        //final Intent intent = getIntent();
        //id_key = intent.getStringExtra("id");

        mPostReference = FirebaseDatabase.getInstance().getReference().child("user_list");
        arrayAdapter = new ArrayAdapter<String>(Addfriend.this, android.R.layout.simple_list_item_1);
        //friend_list.setAdapter(arrayAdapter);
        getFirebaseDatabase();



    }

    public void getFirebaseDatabase() {
        try {
            final ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String key = snapshot.getKey();
                        Log.d("user key", key);
                        //if (key.equals(id_key))
                        data.add(key);
                    }
                    arrayAdapter.clear();
                    arrayAdapter.addAll(data);
                    arrayAdapter.notifyDataSetChanged();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            };
            mPostReference.addValueEventListener(postListener);

        } catch (java.lang.NullPointerException e) {

        }
    }
}