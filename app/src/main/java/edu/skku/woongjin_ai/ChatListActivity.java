package edu.skku.woongjin_ai;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatListActivity extends AppCompatActivity {
    private DatabaseReference mPostReference;
    ArrayList<String> data;
    ArrayAdapter<String> arrayAdapter;

    String id_key, name_key;

    ListView chatListView;
    EditText name_edit;
    Button button;

    Spinner spinner;
    private DatabaseReference sPostReference;
    ArrayList<String> spinner_data;
    ArrayAdapter<String> spinner_Adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatlist);

        //Intent intent = getIntent();
        //id_key = intent.getExtras().getString("id");

        chatListView = findViewById(R.id.listView);
        name_edit = findViewById(R.id.name_edit);
        button = findViewById(R.id.create);

        spinner = findViewById(R.id.spinner);

        spinner_data = new ArrayList<String>();
        spinner_Adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spinner_data);
        sPostReference = FirebaseDatabase.getInstance().getReference().child("DY").child("friend"); //modify DY to id_key

        data = new ArrayList<String>();
        //mPostReference = FirebaseDatabase.getInstance().getReference().child("chatroom_list");
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);


        spinner.setAdapter(spinner_Adapter);
        getSpinnerDatabase();
        /*button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                name_key = name_edit.getText().toString();
                if (spaceCheck(name_key) == false && name_key.length() > 0) { //create chat room
                    postFirebaseDatabase(true);
                }
                else if (spaceCheck(name_key) == true || name_key.length() == 0) {
                    name_edit.setText("");
                }
            }
        });
        chatListView.setAdapter(arrayAdapter);
        getFirebaseDatabase();*/

        /*chatListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                name_key = chatListView.getItemAtPosition(position).toString();
                Intent intent_Chatroom = new Intent(ChatListActivity.this, ChatroomActivity.class);
                intent_Chatroom.putExtra("ID", id_key);
                intent_Chatroom.putExtra("RoomName", name_key);
                startActivity(intent_Chatroom);
            }
        });*/
    }
    public void getSpinnerDatabase() {
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                spinner_data.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    String key = postSnapshot.getKey();
                    spinner_data.add(key);
                }
                spinner_Adapter.clear();
                spinner_Adapter.addAll(spinner_data);
                spinner_Adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        sPostReference.addValueEventListener(postListener);
    }
    /*public void postFirebaseDatabase(boolean add) {
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        if(add) {
            FirebasePost_list post = new FirebasePost_list(name_edit.getText().toString());
            postValues = post.toMap();
        }
        childUpdates.put(name_edit.getText().toString(), postValues);
        mPostReference.updateChildren(childUpdates);
        name_edit.setText("");
    }

    public void getFirebaseDatabase() {
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                data.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    String key = postSnapshot.getKey();
                    FirebasePost_list get = postSnapshot.getValue(FirebasePost_list.class);
                    data.add(get.roomname.toString());
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
    }*/

    public boolean spaceCheck(String spaceCheck) {
        for (int i = 0 ; i < spaceCheck.length() ; i++) {
            if (spaceCheck.charAt(i) == ' ')
                continue;
            else if (spaceCheck.charAt(i) != ' ') {
                return false;
            }
        }
        return true;
    }
}