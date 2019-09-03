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
    Intent intent, intentAddFriend, intent_chatlist, intent_LikeList, intent_QList;
    String id, nickname, name, coin;
    Button btnFriendList, btnuserLetter, btnLikeList, btnQList;
    Button logout;
    TextView userNickname, userName, userCoin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        intent = getIntent();
        id = intent.getStringExtra("id");

        mPostReference = FirebaseDatabase.getInstance().getReference();

//        btnFriendList = (Button)findViewById(R.id.friendsList);
//        btnuserLetter = (Button) findViewById(R.id.userLetter);
        btnLikeList = (Button) findViewById(R.id.LikeList);
        btnQList = (Button) findViewById(R.id.QList);
        btnLikeList = (Button) findViewById(R.id.LikeList);
        //userNickname = (TextView) findViewById(R.id.userID);
        userName = (TextView) findViewById(R.id.userName);
        userCoin = (TextView) findViewById(R.id.userCoin);
        logout = (Button) findViewById(R.id.logout);

        getFirebaseDatabaseUserInfo();

        btnFriendList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentAddFriend = new Intent(MyPageActivity.this, ShowFriendActivity.class);
                intentAddFriend.putExtra("id", id);
                startActivity(intentAddFriend);
            }
        });


        btnuserLetter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent_chatlist = new Intent(MyPageActivity.this, ChatListActivity.class);
                intent_chatlist.putExtra("id", id);
                startActivity(intent_chatlist);
            }
        });

        btnQList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent_LikeList = new Intent(MyPageActivity.this, MyQuizActivity.class);
                intent_LikeList.putExtra("id",id);
                startActivity(intent_LikeList);
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
                        userName.setText("이름: " + name);
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
                        userName.setText("이름: " + name);
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
