package edu.skku.woongjin_ai;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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
    Intent intent, intentAddFriend, intent_chatlist, intent_LikeList, intent_QList, intentHome;
    String grade ,school, name, coin,id;
    Button btnFriendList, btnLikeList, btnQList;
    Button logout;
    TextView userGrade, userSchool, userName, userCoin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        intent = getIntent();
        id = intent.getStringExtra("id");

        mPostReference = FirebaseDatabase.getInstance().getReference();

        ImageButton homeButton = (ImageButton) findViewById(R.id.home);
        btnFriendList = (Button)findViewById(R.id.FriendList);
        btnQList = (Button) findViewById(R.id.QList);
        btnLikeList = (Button) findViewById(R.id.LikeList);
        userName = (TextView) findViewById(R.id.userName);
        userSchool = (TextView) findViewById(R.id.userSchool);
        userGrade = (TextView) findViewById(R.id.userGrade);
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

        btnQList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent_LikeList = new Intent(MyPageActivity.this, MyQuizActivity.class);
                intent_LikeList.putExtra("id", id);
                startActivity(intent_LikeList);
            }
        });

        btnLikeList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent_LikeList = new Intent(MyPageActivity.this, MyQuizActivity.class);
                intent_LikeList.putExtra("id", id);
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

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentHome = new Intent(MyPageActivity.this, MainActivity.class);
                intentHome.putExtra("id", id);
                startActivity(intentHome);
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
                        school = get.school;
                        coin = get.coin;
                        userName.setText(name);
                        userSchool.setText(school);
                        userCoin.setText(coin + "코인");
                        break;
                    }
                }
                for (DataSnapshot snapshot : dataSnapshot.child("kakaouser_list").getChildren()) {
                    String key = snapshot.getKey();
                    if (id.equals(key)) {
                        UserInfo get = snapshot.getValue(UserInfo.class);
                        name = get.name;
                        school = get.school;
                        coin = get.coin;
                        userName.setText(name);
                        userSchool.setText(school);
                        userCoin.setText(coin + "코인");
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
