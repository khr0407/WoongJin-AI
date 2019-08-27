package edu.skku.woongjin_ai;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShowFriendQuizActivity extends AppCompatActivity {

    //TODO UI 백그라운드 이미지로 바꿀까?? 지문 제목 추가??

    Intent intent;
    String id, scriptnm, background;
    public DatabaseReference mPostReference;
    ListView myFriendQuizListView, likeQuizListView;
    ArrayList<String> myFriendQuizList, likeQuizList, myFriendList;
    ArrayAdapter<String> myFriendQuizAdapter, likeQuizAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showfriendquiz);

        intent = getIntent();
        id = intent.getStringExtra("id");
        scriptnm = intent.getStringExtra("scriptnm");
        background = intent.getStringExtra("background");

        mPostReference = FirebaseDatabase.getInstance().getReference();

        myFriendQuizListView = (ListView) findViewById(R.id.myFriendQuizList);
        likeQuizListView = (ListView) findViewById(R.id.likeQuizList);
        TextView textView = (TextView) findViewById(R.id.textShowFriendQuiz);

        myFriendQuizList = new ArrayList<String>();
        likeQuizList = new ArrayList<String>();
        myFriendList = new ArrayList<String>();
        myFriendQuizAdapter = new ArrayAdapter<String>(ShowFriendQuizActivity.this, android.R.layout.simple_list_item_1);
        likeQuizAdapter = new ArrayAdapter<String>(ShowFriendQuizActivity.this, android.R.layout.simple_list_item_1);

        getFirebaseDatabaseMyFriendQuiz();
        getFirebaseDatabaseLikeQuiz();

        textView.setText(id + " 친구가 낸 문제야!");
    }

    private void getFirebaseDatabaseMyFriendQuiz() {
        mPostReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {            }
        });
    }

    private void getFirebaseDatabaseLikeQuiz() {
        mPostReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.child("quiz_list/").getChildren()) {
                    String key = snapshot.getKey();
                    if(key.equals(scriptnm)) {




                        break;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {            }
        });
    }
}
