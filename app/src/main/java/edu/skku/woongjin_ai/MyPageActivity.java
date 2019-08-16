package edu.skku.woongjin_ai;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import java.util.Iterator;

import static com.kakao.usermgmt.StringSet.nickname;

public class MyPageActivity extends AppCompatActivity {

    public DatabaseReference mPostReference;
    Intent intent, intentAddFriend, intent_chatlist;
    String id, nickname, name, coin;
    Button buttonFriendList;
    Button userLetter;
    Button logout;
    TextView userNickname, userName, userCoin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        intent = getIntent();
        id = intent.getStringExtra("id");

        mPostReference = FirebaseDatabase.getInstance().getReference();

        buttonFriendList = findViewById(R.id.friendList);
        userLetter = findViewById(R.id.userLetter);
        userNickname = (TextView) findViewById(R.id.userNickname);
        userName = (TextView) findViewById(R.id.userName);
        userCoin = (TextView) findViewById(R.id.userCoin);
        logout = (Button) findViewById(R.id.logout);

        getFirebaseDatabaseUserInfo();

        buttonFriendList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentAddFriend = new Intent(MyPageActivity.this, ShowFriendActivity.class);
                intentAddFriend.putExtra("id", id);
                startActivity(intentAddFriend);
            }
        });

        userLetter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent_chatlist = new Intent(MyPageActivity.this, ChatListActivity.class);
                intent_chatlist.putExtra("id", id);
                startActivity(intent_chatlist);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                UserManagement.getInstance().requestLogout(new LogoutResponseCallback() { //카카오톡은 매번 로그아웃됨
                    @Override
                    public void onCompleteLogout() {
                        Intent intent = new Intent(MyPageActivity.this, LoginActivity.class);
                        ActivityCompat.finishAffinity(MyPageActivity.this);
                        startActivity(intent);
                    }
                });
            }
        });
    }
/*
    private void getFirebaseDatabaseUserInfo() {
        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> child = dataSnapshot.child("user_list").getChildren().iterator();
                while (child.hasNext()) {
                    String key = dataSnapshot.getKey();
                    if(id.equals(key)) {
                        UserInfo get = dataSnapshot.getValue(UserInfo.class);

                        name = get.name;
                        nickname = get.nickname;
                        coin = get.coin;
                        userName.setText("이름: " + userName);
                        userNickname.setText("닉네임: " + nickname);
                        userCoin.setText("코인: " + coin);
                        break;
                    }
                }
                Iterator<DataSnapshot> child1 = dataSnapshot.child("kakaouser_list").getChildren().iterator();
                while (child.hasNext()) {
                    String key = dataSnapshot.getKey();
                    if(id.equals(key)) {
                        UserInfo get = dataSnapshot.getValue(UserInfo.class);
                        name = get.name;
                        nickname = get.nickname;
                        coin = get.coin;
                        userName.setText("이름: " + userName);
                        userNickname.setText("닉네임: " + nickname);
                        userCoin.setText("코인: " + coin);
                        break;
                    }
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {            }
        };
    }
    */
    private void getFirebaseDatabaseUserInfo() {
        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.child("user_list").getChildren()) {
                    String key = snapshot.getKey();
                    if (id.equals(key)) {
                        UserInfo get = snapshot.getValue(UserInfo.class);
                        name = get.name;
                        nickname = get.nickname;
                        coin = get.coin;
                        userName.setText("이름" + name);
                        userNickname.setText("닉네임: " + nickname);
                        userCoin.setText("코인: " + coin);
                        break;
                    }
                }
                for (DataSnapshot snapshot : dataSnapshot.child("kakaouser_list").getChildren()) {
                    String key = snapshot.getKey();
                    if (id.equals(key)) {
                        UserInfo get = snapshot.getValue(UserInfo.class);
                        name = get.name;
                        nickname = get.nickname;
                        coin = get.coin;
                        userName.setText("이름" + name);
                        userNickname.setText("닉네임: " + nickname);
                        userCoin.setText("코인: " + coin);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        mPostReference.addValueEventListener(postListener);
    }
}