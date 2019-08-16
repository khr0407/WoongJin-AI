package edu.skku.woongjin_ai;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import static com.kakao.util.helper.Utility.getPackageInfo;

public class MainActivity extends AppCompatActivity {

    ListView mListView;
    public DatabaseReference mPostReference;
    ArrayList<String> quizList;
    ArrayAdapter<String> adapter;
    Intent intent, intentType, intentMyPage;
    String id;
    String check = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView) findViewById(R.id.listView);
        Button buttonSelectType = (Button) findViewById(R.id.selectType);
        Button buttonMyPage = (Button) findViewById(R.id.myPage);

        intent = getIntent();
        id = intent.getStringExtra("id");
        intentType = new Intent(MainActivity.this, SelectTypeActivity.class);
        intentType.putExtra("id", id);

        mPostReference = FirebaseDatabase.getInstance().getReference();

        quizList = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1);
        mListView.setAdapter(adapter);

        getFirebaseDatabaseQuizList();


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
                intentType.putExtra("scriptnm", quizList.get(position));
                check = intentType.getStringExtra("scriptnm");
            }
        });
    }



    private void getFirebaseDatabaseQuizList(){
        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                quizList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String key = snapshot.getKey();
                    quizList.add(key);
                }
                adapter.clear();
                adapter.addAll(quizList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {            }
        };
        mPostReference.child("quiz_list").addValueEventListener(postListener);
    }
}