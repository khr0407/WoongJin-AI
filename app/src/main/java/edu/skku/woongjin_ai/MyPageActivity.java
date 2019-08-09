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
    Button buttonFriendList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        intent = getIntent();
        id = intent.getStringExtra("id");

        buttonFriendList = findViewById(R.id.friendList);
        TextView userNmT = (TextView) findViewById(R.id.userName);

        userNmT.setText("ID: " + id);

        buttonFriendList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentAddFriend = new Intent(MyPageActivity.this, ScriptActivity.class);
                intentAddFriend.putExtra("id", id);
                startActivity(intentAddFriend);
                finish();
            }
        });

    }


}
