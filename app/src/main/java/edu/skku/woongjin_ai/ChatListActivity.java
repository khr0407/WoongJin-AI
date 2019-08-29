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
    private DatabaseReference PostReference, mPostReference, sPostReference;
    ArrayList<String> data;
    ArrayAdapter<String> arrayAdapter;

    ArrayList<String> scriptdata;
    ArrayAdapter<String> scriptAdapter;

    String id_key, name_key, nickname_key;
    String roomname, partner;

    ListView chatlist, scriptlist;
    Button create, goquiz, readagain, gobomb;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatlist);

        chatlist = findViewById(R.id.chatlist);
        scriptlist = findViewById(R.id.scriptlist);
        create = findViewById(R.id.create);
        goquiz = findViewById(R.id.goquiz);
        readagain = findViewById(R.id.readagain);
        gobomb = findViewById(R.id.gobomb);

        data = new ArrayList<String>();
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        mPostReference = FirebaseDatabase.getInstance().getReference().child("chatroom_list");

        scriptdata = new ArrayList<String>();
        scriptAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        sPostReference = FirebaseDatabase.getInstance().getReference();

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
                            name_key = postSnapshot.child("name").getValue().toString();
                            nickname_key = postSnapshot.child("nickname").getValue().toString();
                        }
                        Log.d("_id_key", id_key);
                        Log.d("_name_key", name_key);
                        Log.d("_mynickname_key", nickname_key);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            };
            PostReference.addListenerForSingleValueEvent(findNickname);
        }
        else {
            PostReference = FirebaseDatabase.getInstance().getReference().child("user_list");
            final ValueEventListener findNickname = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                        String key = postSnapshot.getKey();
                        if (key.equals(id_key)) {
                            name_key = postSnapshot.child("name").getValue().toString();
                            nickname_key = postSnapshot.child("nickname").getValue().toString();
                        }
                        Log.d("_id_key", id_key);
                        Log.d("_name_key", name_key);
                        Log.d("_mynickname_key", nickname_key);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            };
            PostReference.addListenerForSingleValueEvent(findNickname);
        }

        create.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent_chatfriend = new Intent(ChatListActivity.this, ChatFriendActivity.class);
                intent_chatfriend.putExtra("id", id_key);
                intent_chatfriend.putExtra("name", name_key);
                intent_chatfriend.putExtra("nickname", nickname_key);
                startActivity(intent_chatfriend);
            }
        });

        goquiz.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent_nationgame = new Intent(ChatListActivity.this, NationGameActivity.class);
                intent_nationgame.putExtra("id", id_key);
                //intent_nationgame.putExtra("name", name_key);
                //intent_nationgame.putExtra("nickname", nickname_key);
                startActivity(intent_nationgame);
            }
        });

        /*readagain.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent_tempreadagain = new Intent(ChatListActivity.this, Temp_readagainActivity.class);
                intent_tempreadagain.putExtra("id", id_key);
                //intent_nationgame.putExtra("name", name_key);
                //intent_nationgame.putExtra("nickname", nickname_key);
                startActivity(intent_tempreadagain);
            }
        });*/

        gobomb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent_tempgobomb = new Intent(ChatListActivity.this, Temp_GobombActivity.class);
                //intent_tempgoquiz.putExtra("id", id_key);
                //intent_nationgame.putExtra("name", name_key);
                //intent_nationgame.putExtra("nickname", nickname_key);
                startActivity(intent_tempgobomb);
            }
        });


        chatlist.setAdapter(arrayAdapter);
        getFirebaseDatabase();

        chatlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                roomname = chatlist.getItemAtPosition(position).toString().split("\\[")[0];
                String temp = chatlist.getItemAtPosition(position).toString().split("\\[")[1];
                partner = temp.split("\\]")[0];
                /*Intent intent_nationgame = new Intent(ChatListActivity.this, NationGameActivity.class);
                intent_nationgame.putExtra("id", id_key);
                intent_nationgame.putExtra("roomname", roomname);
                startActivityForResult(intent_nationgame, 1);*/
            }
        });
    }

    /*public void getFriendScriptList() {
        try {
            ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    scriptdata.clear();
                    for (DataSnapshot postSnapshot : dataSnapshot.child("user_list").getChildren()) {
                        String key = postSnapshot.child("nickname").getValue().toString();
                        if (key.equals(partner)) {

                        }
                    }
                    for (DataSnapshot postSnapshot : dataSnapshot.child("kakaouser_list").getChildren()) {
                        String key = postSnapshot.child("nickname").getValue().toString();

                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            };
            sPostReference.addValueEventListener(postListener);
        } catch(java.lang.NullPointerException e){
        }
    }*/

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
                        if (user1.equals(nickname_key)) {
                            FirebasePost_list get = postSnapshot.getValue(FirebasePost_list.class);
                            data.add(get.roomname + "[" + get.user2 + "]");
                        }
                        else if (user2.equals(nickname_key)) {
                            FirebasePost_list get = postSnapshot.getValue(FirebasePost_list.class);
                            data.add(get.roomname + "[" + get.user1 + "]");
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
            mPostReference.addListenerForSingleValueEvent(postListener);
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