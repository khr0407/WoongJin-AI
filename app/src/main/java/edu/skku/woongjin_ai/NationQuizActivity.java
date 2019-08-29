package edu.skku.woongjin_ai;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NationQuizActivity extends AppCompatActivity {

    Intent intent, intentHome, intentMakeQuiz, intentFriendQuiz;
    String id, quizType;
    TextView textView;
    ImageButton homeButton;
    public DatabaseReference mPostReference;
    ListView studiedBookListView;
    ArrayList<String> studiedBookArrayList, backgroundArrayList;
    ArrayAdapter<String> studiedBookArrayAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nationquiz);

        intent = getIntent();
        id = intent.getStringExtra("id");
        quizType = intent.getStringExtra("quizType");

        mPostReference = FirebaseDatabase.getInstance().getReference();

        textView = (TextView) findViewById(R.id.nationQuiz);
        homeButton = (ImageButton) findViewById(R.id.home);
        studiedBookListView = (ListView) findViewById(R.id.studiedBookList);

        studiedBookArrayList = new ArrayList<String>();
        backgroundArrayList = new ArrayList<String>();
        studiedBookArrayAdapter = new ArrayAdapter<String>(NationQuizActivity.this, android.R.layout.simple_list_item_1);
        studiedBookListView.setAdapter(studiedBookArrayAdapter);

        getFirebaseDatabaseStudiedBookList();

        if(quizType.equals("me")) {
            textView.setText(id + "가 읽은 책 목록이야~\n추가로 문제를 내고 싶은 책을 클릭하면 문제를 만들 수 있어!\n연필 아이콘 개수는 " + id + "가 낸 문제 개수와 같아");
        } else if(quizType.equals("friend")) {
            textView.setText(id + "가 읽은 책 목록이야~\n책을 클릭하면 다른 친구들이 낸 문제를 풀어보고 평가할 수 있어!");
        }

        studiedBookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long i) {
                if(quizType.equals("me")) {
                    intentMakeQuiz = new Intent(NationQuizActivity.this, SelectTypeActivity.class);
                    intentMakeQuiz.putExtra("id", id);
                    intentMakeQuiz.putExtra("scriptnm", studiedBookArrayList.get(position));
                    intentMakeQuiz.putExtra("background", backgroundArrayList.get(position));
                    startActivity(intentMakeQuiz);
                    finish();
                } else if(quizType.equals("friend")) {
                    intentFriendQuiz = new Intent(NationQuizActivity.this, ShowFriendQuizActivity.class);
                    intentFriendQuiz.putExtra("id", id);
                    intentFriendQuiz.putExtra("scriptnm", studiedBookArrayList.get(position));
                    intentFriendQuiz.putExtra("background", backgroundArrayList.get(position));
                    startActivity(intentFriendQuiz);
                    finish();
                }
            }
        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentHome = new Intent(NationQuizActivity.this, MainActivity.class);
                intentHome.putExtra("id", id);
                startActivity(intentHome);
                finish();
            }
        });
    }

    private void getFirebaseDatabaseStudiedBookList() {
        mPostReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                studiedBookArrayList.clear();
                backgroundArrayList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String key = snapshot.getKey();
                    if(key.equals("kakaouser_list") || key.equals("user_list")) {
                        for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                            String key1 = snapshot1.getKey();
                            if(key1.equals(id)) {
                                for(DataSnapshot snapshot2 : snapshot1.child("scripts").getChildren()) {
                                    String scriptnm = snapshot2.getKey();
                                    studiedBookArrayList.add(scriptnm);
                                }
                                break;
                            }
                        }
                    }
                }
                studiedBookArrayAdapter.clear();
                studiedBookArrayAdapter.addAll(studiedBookArrayList);
                studiedBookArrayAdapter.notifyDataSetChanged();
                for(DataSnapshot snapshot : dataSnapshot.child("script_list").getChildren()) {
                    String key = snapshot.getKey();
                    for(String script : studiedBookArrayList) {
                        if(key.equals(script)) {
                            String background = snapshot.child("background").getValue().toString();
                            backgroundArrayList.add(background);
                            break;
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {            }
        });
    }
}
