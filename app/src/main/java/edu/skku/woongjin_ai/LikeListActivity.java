package edu.skku.woongjin_ai;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LikeListActivity extends AppCompatActivity {

    public DatabaseReference mPostReference;
    Intent intent, intentHome;
    String id;
    ListView mListView;
    ArrayList<String> myQuizList;
    ArrayAdapter<String> myQuizadapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_likelist);

        ImageView imageHome = (ImageView) findViewById(R.id.home);
        mListView = (ListView) findViewById(R.id.listView);

        intent = getIntent();
        id = intent.getStringExtra("id");

        mPostReference = FirebaseDatabase.getInstance().getReference();

        myQuizList = new ArrayList<String>();
        myQuizadapter = new ArrayAdapter<String>(LikeListActivity.this, android.R.layout.simple_list_item_1);
        mListView.setAdapter(myQuizadapter);

        imageHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentHome = new Intent(LikeListActivity.this, MainActivity.class);
                intentHome.putExtra("id", id);
                startActivity(intentHome);
                finish();
            }
        });

        getFirebaseDatabaseMyQuizList();
    }
    private void getFirebaseDatabaseMyQuizList() {
        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myQuizadapter.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String scriptTitle = snapshot.getKey();
                    for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                        String type = snapshot1.getKey();
                        if(type.equals("type2")) {
                            for(DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                QuizChoiceTypeInfo get = snapshot2.getValue(QuizChoiceTypeInfo.class);
                                if(id.equals(get.uid)) {
                                    String myQuiz = "지문 제목: " + scriptTitle + "\nQ. "+ get.question
                                            + "\nA1. " + get.answer1 + " A2. " + get.answer2 + " A3. " + get.answer3 +
                                            " A4. " + get.answer4 + "\nA. " + get.answer + "\n해설: " + get.desc +
                                            "\n난이도: " + get.star + "\n좋아요: " + get.like;
                                    myQuizList.add(myQuiz);
                                }
                            }
                        } else {
                            for(DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                QuizOXShortwordTypeInfo get = snapshot2.getValue(QuizOXShortwordTypeInfo.class);
                                if(id.equals(get.uid)) {
                                    String myQuiz = "지문 제목: " + scriptTitle + "\nQ. "+ get.question
                                            + "\nA. " + get.answer + "\n해설: " + get.desc +
                                            "\n난이도: " + get.star + "\n좋아요: " + get.like;
                                    myQuizList.add(myQuiz);
                                }
                            }
                        }
                    }
                }
                myQuizadapter.clear();
                myQuizadapter.addAll(myQuizList);
                myQuizadapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {            }
        };
        mPostReference.addValueEventListener(postListener);
    }
}
