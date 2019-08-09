package edu.skku.woongjin_ai;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddfriendActivity extends AppCompatActivity {
    private DatabaseReference mPostReference;
    private DatabaseReference mPostReference2;
    ListView add_list;
    ArrayList<String> data;
    ArrayAdapter<String> arrayAdapter;
    String id_key;
    Button enter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addfriend);

        add_list = findViewById(R.id.addfriendList);

        data = new ArrayList<String>();

        //friend_list = findViewById(R.id.friend_list);

        data = new ArrayList<String>();

        final Intent intent = getIntent();
        id_key = intent.getStringExtra("id");

        mPostReference2 = FirebaseDatabase.getInstance().getReference().child("user_list").child(id_key).child("friend");
        mPostReference = FirebaseDatabase.getInstance().getReference().child("user_list");


        arrayAdapter = new ArrayAdapter<String>(AddfriendActivity.this, android.R.layout.simple_list_item_1);
        add_list.setAdapter(arrayAdapter);
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