package edu.skku.woongjin_ai;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MyPageActivity extends AppCompatActivity {
    Intent intent, intentAddFriend;
    String id;
    String userName, userCoin;
    Button buttonFriendList, LikeList, QList, userLetter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        intent = getIntent();
        id = intent.getStringExtra("id");

        buttonFriendList = findViewById(R.id.friendList);
        LikeList = findViewById(R.id.LikeList);
        QList = findViewById(R.id.QList);
        TextView userNameT = (TextView) findViewById(R.id.userName);

        userNameT.setText("ID: " + id);



        buttonFriendList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Fintent = new Intent(MyPageActivity.this, FriendActivity.class);
                Fintent.putExtra("id", id);
                startActivity(Fintent);
                finish();
            }
        });

        LikeList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Lintent = new Intent(MyPageActivity.this, LikeListActivity.class);
                Lintent.putExtra("id", id);
                startActivity(Lintent);
                finish();
            }
        });

        QList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Qintent = new Intent(MyPageActivity.this, MyQuizActivity.class);
                Qintent.putExtra("id", id);
                startActivity(Qintent);
                finish();
            }
        });
    }


}