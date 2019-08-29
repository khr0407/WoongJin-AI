package edu.skku.woongjin_ai;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.Iterator;

import static com.kakao.usermgmt.StringSet.nickname;

public class MyPageActivity extends AppCompatActivity {

    public DatabaseReference mPostReference;
    Intent intent, intentAddFriend, intent_chatlist, intent_LikeList, intent_QList, intentHome;
    String grade ,school, name, coin, id, nickname;
    Button btnFriendList, btnuserLetter, btnLikeList, btnQList;
    Button logout;
    ImageButton homeButton;
    TextView userGrade, userSchool, userName, userCoin;
    TextView textViewCorrectL, textViewCorrectT, textViewLikeL, textViewLikeT, textViewLevelL, textViewLevelT;
    UserInfo me;
    ArrayList<HoInfo> hoInfos;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        intent = getIntent();
        id = intent.getStringExtra("id");

        mPostReference = FirebaseDatabase.getInstance().getReference();

        homeButton = (ImageButton) findViewById(R.id.home);
        btnFriendList = (Button)findViewById(R.id.FriendList);
        btnQList = (Button) findViewById(R.id.QList);
        btnLikeList = (Button) findViewById(R.id.LikeList);
        userName = (TextView) findViewById(R.id.userName);
        userSchool = (TextView) findViewById(R.id.userSchool);
        userGrade = (TextView) findViewById(R.id.userGrade);
        userCoin = (TextView) findViewById(R.id.userCoin);
        logout = (Button) findViewById(R.id.logout);
        textViewCorrectL = (TextView) findViewById(R.id.lastCorrectCnt);
        textViewCorrectT = (TextView) findViewById(R.id.thisCorrectCnt);
        textViewLikeL = (TextView) findViewById(R.id.lastLikeCnt);
        textViewLikeT = (TextView) findViewById(R.id.thisLikeCnt);
        textViewLevelL = (TextView) findViewById(R.id.lastLevel);
        textViewLevelT = (TextView) findViewById(R.id.thisLevel);

        hoInfos = new ArrayList<HoInfo>();

        getFirebaseDatabaseUserInfo();

        btnFriendList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentAddFriend = new Intent(MyPageActivity.this, ShowFriendActivity.class);
                intentAddFriend.putExtra("id", id);
                intentAddFriend.putExtra("name", name);
                intentAddFriend.putExtra("nickname", nickname);
                startActivity(intentAddFriend);
            }
        });
/*
        btnuserLetter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent_chatlist = new Intent(MyPageActivity.this, ChatListActivity.class);
                intent_chatlist.putExtra("id", id);
                startActivity(intent_chatlist);
            }
        });
        */

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
//                intent_LikeList = new Intent(MyPageActivity.this, MyQuizActivity.class);
//                intent_LikeList.putExtra("id", id);
//                startActivity(intent_LikeList);
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
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String key = snapshot.getKey();
                    if(key.equals("kakaouser_list") || key.equals("user_list")) {
                        for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                            String key1 = snapshot1.getKey();
                            if(key1.equals(id)) {
                                me = snapshot1.getValue(UserInfo.class);
                                userName.setText(me.name);
                                userSchool.setText(me.school);
                                userGrade.setText(me.grade);
                                userCoin.setText(me.coin + " 코인");
                                for(DataSnapshot snapshot2 :snapshot1.child("ho_list").getChildren()) {
                                    HoInfo hoInfo = snapshot2.getValue(HoInfo.class);
                                    hoInfos.add(hoInfo);
                                }
                                break;
                            }
                        }
                    }
                }

                int hoNum = hoInfos.size();
                if(hoNum < 2) {
                    textViewCorrectL.setText("0");
                    textViewCorrectT.setText(Integer.toString(hoInfos.get(hoNum-1).correct));
                    textViewLikeL.setText("0");
                    textViewLikeT.setText(Integer.toString(hoInfos.get(hoNum-1).like));
                    textViewLevelL.setText("0.0");
                    textViewLevelT.setText(Float.toString(hoInfos.get(hoNum-1).level));

                } else {
                    textViewCorrectL.setText(Integer.toString(hoInfos.get(hoNum-2).correct));
                    textViewCorrectT.setText(Integer.toString(hoInfos.get(hoNum-1).correct));
                    textViewLikeL.setText(Integer.toString(hoInfos.get(hoNum-2).like));
                    textViewLikeT.setText(Integer.toString(hoInfos.get(hoNum-1).like));
                    textViewLevelL.setText(Float.toString(hoInfos.get(hoNum-2).level));
                    textViewLevelT.setText(Float.toString(hoInfos.get(hoNum-1).level));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        mPostReference.addValueEventListener(postListener);
    }
}
