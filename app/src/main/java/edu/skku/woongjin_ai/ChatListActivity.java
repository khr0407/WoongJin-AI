package edu.skku.woongjin_ai;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatListActivity extends AppCompatActivity {
    private DatabaseReference PostReference, mPostReference;
    ArrayList<String> data;
    ArrayAdapter<String> arrayAdapter;

    String id_key, name_key, mynickname;

    ListView chatListView;
    Button create;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatlist);

        chatListView = findViewById(R.id.listView);
        create = findViewById(R.id.create);

        data = new ArrayList<String>();
        mPostReference = FirebaseDatabase.getInstance().getReference().child("chatroom_list");
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        intent = getIntent();
        id_key = intent.getExtras().getString("id");

        if (onlyNumCheck(id_key) == true) {
            PostReference = FirebaseDatabase.getInstance().getReference().child("kakaouser_list");
            final ValueEventListener findNickname = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                        String key = postSnapshot.getKey();
                        if (key.equals(id_key)) {
                            mynickname = postSnapshot.child("nickname").getValue().toString();
                        }
                        Log.d("_id_key", id_key);
                        Log.d("_mynickname", mynickname);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            };
            PostReference.addValueEventListener(findNickname);
        }
        else {
            PostReference = FirebaseDatabase.getInstance().getReference().child("user_list");
            final ValueEventListener findNickname = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                        String key = postSnapshot.getKey();
                        if (key.equals(id_key)) {
                            mynickname = postSnapshot.child("nickname").getValue().toString();
                        }
                        Log.d("_id_key", id_key);
                        Log.d("_mynickname", mynickname);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            };
            PostReference.addValueEventListener(findNickname);
        }

        create.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent_chatfriend = new Intent(ChatListActivity.this, NationGameActivity.class);
                intent_chatfriend.putExtra("id", id_key);
                intent_chatfriend.putExtra("nickname", mynickname);
                startActivity(intent_chatfriend);
            }
        });
        chatListView.setAdapter(arrayAdapter);
        getFirebaseDatabase();

        chatListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                name_key = chatListView.getItemAtPosition(position).toString();
                //Intent intent_Chatroom = new Intent(ChatListActivity.this, ChatroomActivity.class);
                //intent_Chatroom.putExtra("ID", id_key);
                //intent_Chatroom.putExtra("RoomName", name_key);
                //startActivity(intent_Chatroom);
            }
        });
    }

    public void getFirebaseDatabase() {
        try {
            ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    data.clear();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        String user1 = postSnapshot.child("user1").getValue().toString();
                        String user2 = postSnapshot.child("user2").getValue().toString();
                        //Log.d("_mynickname", mynickname);
                        Log.d("_id_key", id_key);
                        Log.d("_user1", user1);
                        Log.d("_user2", user2);
                        if (user1.equals(mynickname)) {
                            FirebasePost_list get = postSnapshot.getValue(FirebasePost_list.class);
                            data.add(get.roomname + " with " + get.user2);
                        }
                        else if (user2.equals(mynickname)) {
                            FirebasePost_list get = postSnapshot.getValue(FirebasePost_list.class);
                            data.add(get.roomname + " with " + get.user1);
                        }
                    }
                    arrayAdapter.clear();
                    arrayAdapter.addAll(data);
                    arrayAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            };
            mPostReference.addValueEventListener(postListener);
        }catch(java.lang.NullPointerException e){
        }
    }

    public boolean onlyNumCheck(String spaceCheck) {
        for (int i = 0 ; i < spaceCheck.length() ; i++) {
            if (spaceCheck.charAt(i) == '1' || spaceCheck.charAt(i) == '2' || spaceCheck.charAt(i) == '3' || spaceCheck.charAt(i) == '4' || spaceCheck.charAt(i) == '5'
                    || spaceCheck.charAt(i) == '6' || spaceCheck.charAt(i) == '7' || spaceCheck.charAt(i) == '8' || spaceCheck.charAt(i) == '9' || spaceCheck.charAt(i) == '0') {
                continue;
            }
            else {
                return false;
            }
        }
        return true;
    }
}