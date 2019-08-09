package edu.skku.woongjin_ai;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyPageActivity extends AppCompatActivity {
    public DatabaseReference mPostReference;
    Intent intent;
    String id, name = "";
    int coin;
    Button buttonFriendList, buttonMyQuiz, buttonLikeList, buttonLetter;
    TextView userNameT, userCoinT;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        mPostReference = FirebaseDatabase.getInstance().getReference();

        intent = getIntent();
        id = intent.getStringExtra("id");

        buttonMyQuiz = (Button) findViewById(R.id.QList);
        buttonFriendList = (Button) findViewById(R.id.friendList);
        buttonLetter = (Button) findViewById(R.id.userLetter);
        buttonLikeList = (Button) findViewById(R.id.LikeList);

        userNameT = (TextView) findViewById(R.id.userName);
        userCoinT = (TextView) findViewById(R.id.userCoin);

        userNameT.setText("ID: " + id);

        getFirebaseDatabaseUserInfo();

        buttonFriendList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAddFriend = new Intent(MyPageActivity.this, FriendActivity.class);
                intentAddFriend.putExtra("id", id);
                startActivity(intentAddFriend);
                finish();
            }
        });

        buttonLikeList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLikeList = new Intent(MyPageActivity.this, LikeListActivity.class);
                intentLikeList.putExtra("id", id);
                startActivity(intentLikeList);
                finish();
            }
        });

        buttonMyQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentMyQuiz = new Intent(MyPageActivity.this, MyQuizActivity.class);
                intentMyQuiz.putExtra("id", id);
                startActivity(intentMyQuiz);
                finish();
            }
        });
    }

    private void getFirebaseDatabaseUserInfo() {
        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String key = snapshot.getKey();
                    if (id.equals(key)) {
                        UserInfo get = snapshot.getValue(UserInfo.class);
                        name = get.name;
                        coin = get.coin;
                        userNameT.setText("이름: " + name);
                        userCoinT.setText("코인: " + coin);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mPostReference.child("user_list").addValueEventListener(postListener);
    }
}