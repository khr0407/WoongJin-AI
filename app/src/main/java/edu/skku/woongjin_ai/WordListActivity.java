package edu.skku.woongjin_ai;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class WordListActivity extends AppCompatActivity {

    ListView wordlistView;
    ArrayAdapter<String> wordlistadapter;
    ArrayList<String> wordlist;
    String id, scripts;
    Intent intent;
    public DatabaseReference mPostReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPostReference = FirebaseDatabase.getInstance().getReference();

        wordlistView = (ListView)findViewById(R.id.wordlist);
        wordlist = new ArrayList<>();

        intent = getIntent();
        id = intent.getStringExtra("id");
        scripts =  intent.getStringExtra("scriptnm");
        wordlistadapter = new ArrayAdapter<String>(WordListActivity.this, android.R.layout.simple_list_item_1);
        wordlistView.setAdapter(wordlistadapter);

        getFirebaseDatabaseScriptList();

    }

    private void getFirebaseDatabaseScriptList(){

        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String key = snapshot.getKey();
                    if(id.equals(key))
                    {
                        for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                            String scriptkey = snapshot1.getKey();
                            if (scripts.equals(scriptkey)) {
                                for(DataSnapshot snapshot2 : snapshot.getChildren()) {
                                    String wordlistkey = snapshot2.getKey();
                                    if(wordlist.equals(wordlistkey))
                                    {
                                        wordlist.add(wordlistkey);
                                    }
                                    wordlistadapter.clear();
                                    wordlistadapter.addAll(wordlist);
                                    wordlistadapter.notifyDataSetChanged();
                                    }
                                }
                            }
                        }

                    }
                }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {            }
        };
        mPostReference.child("user_list").addValueEventListener(postListener);
    }
}