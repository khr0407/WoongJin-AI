package edu.skku.woongjin_ai;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyQuizActivity extends AppCompatActivity {

    public DatabaseReference mPostReference;
    Intent intent, intentHome;
    String id;
    TextView instruction;
    ListView quizlist;
    ArrayList<String> myQuizList;
    QuizListAdapter myQuizListAdapter;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seequestion_likeormine);

        ImageView imageHome = (ImageView) findViewById(R.id.home);
        quizlist = (ListView) findViewById(R.id.quizlist);
        instruction=(TextView)findViewById(R.id.instruction);


        intent = getIntent();
        id = intent.getStringExtra("id");

        mPostReference = FirebaseDatabase.getInstance().getReference();

        myQuizList = new ArrayList<String>();
        myQuizListAdapter = new QuizListAdapter();

        ////
        adapter=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, myQuizList);
        quizlist.setAdapter(adapter);


        imageHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentHome = new Intent(MyQuizActivity.this, MainActivity.class);
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
                myQuizList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String key = snapshot.getKey();
                    if (key.equals("quiz_list")) {
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) { //script
                            String scriptTitle = snapshot1.getKey();
                            for (DataSnapshot snapshot2 : snapshot1.getChildren()) { // inside-script
                                String question_key = snapshot2.getKey();
                                if (question_key.endsWith(id)) {
                                    int typeInfo = Integer.parseInt(snapshot2.child("type").getValue().toString());
                                    if (typeInfo==1) {
                                        //ox type
                                        //QuizOXShortwordTypeInfo get = snapshot2.getValue(QuizOXShortwordTypeInfo.class);
                                        String quiz=snapshot2.child("question").getValue().toString();
                                        String myQuiz = "[" + scriptTitle + "] Q." + quiz;
                                        myQuizList.add(myQuiz);
                                        myQuizListAdapter.addItem(myQuiz);
                                    } else if (typeInfo==2) {
                                        //choice
                                        QuizChoiceTypeInfo get = snapshot2.getValue(QuizChoiceTypeInfo.class);
                                        String myQuiz = "[" + scriptTitle + "] Q." + get.question;
                                        myQuizList.add(myQuiz);
                                        myQuizListAdapter.addItem(myQuiz);
                                    } else {
                                        //shortwrd
                                        QuizOXShortwordTypeInfo get = snapshot2.getValue(QuizOXShortwordTypeInfo.class);
                                        String myQuiz = "[" + scriptTitle + "] Q." + get.question;
                                        myQuizList.add(myQuiz);
                                        myQuizListAdapter.addItem(myQuiz);
                                    }
                                }
                            }
                            quizlist.setAdapter(myQuizListAdapter);
                        }
                    }
                    //final ValueEventListener quiz_list = mPostReference.child("quiz_list").addValueEventListener(postListener);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        mPostReference.addValueEventListener(postListener);
    }
}