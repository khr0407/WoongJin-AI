package edu.skku.woongjin_ai;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView mListView;
    public DatabaseReference mPostReference;
    ArrayList<String> scriptList;
    ArrayAdapter<String> scriptAdapter;
    Intent intent, intentType, intentMyPage;
    String id;
    String check = "";
    Button buttonSelectType, buttonMyPage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView) findViewById(R.id.listView);

        buttonSelectType = (Button) findViewById(R.id.selectType);
        buttonMyPage = (Button) findViewById(R.id.myPage);

        intent = getIntent();
        id = intent.getStringExtra("id");
        intentType = new Intent(MainActivity.this, SelectTypeActivity.class);
        intentType.putExtra("id", id);

        mPostReference = FirebaseDatabase.getInstance().getReference();

        scriptList = new ArrayList<String>();
        scriptAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1);
        mListView.setAdapter(scriptAdapter);

        getFirebaseDatabaseScriptList();

        buttonMyPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentMyPage = new Intent(MainActivity.this, MyPageActivity.class);
                intentMyPage.putExtra("id", id);
                startActivity(intentMyPage);
                finish();
            }
        });

        buttonSelectType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check.length() == 0) {
                    Toast.makeText(MainActivity.this, "Choose a script", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(intentType);
                    finish();
                }
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long i) {
                intentType.putExtra("scriptnm", scriptList.get(position));
                String script_title = scriptList.get(position);
                Intent intent_readscript = new Intent(MainActivity.this, ReadScriptActivity.class);
                intent_readscript.putExtra("scriptnm",script_title);
                startActivity(intent_readscript);
                intentType.putExtra("scriptnm", scriptList.get(position));
                check = intentType.getStringExtra("scriptnm");
            }
        });
    }

    private void getFirebaseDatabaseScriptList(){
        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                scriptAdapter.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String key = snapshot.getKey();
                    scriptList.add(key);
                }
                scriptAdapter.clear();
                scriptAdapter.addAll(scriptList);
                scriptAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {            }
        };
        mPostReference.child("script_list").addValueEventListener(postListener);
    }
}
