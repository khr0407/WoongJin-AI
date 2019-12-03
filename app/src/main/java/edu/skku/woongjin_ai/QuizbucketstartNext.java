package edu.skku.woongjin_ai;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kakao.kakaolink.v2.KakaoLinkResponse;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.message.template.LinkObject;
import com.kakao.message.template.TextTemplate;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;
import com.kakao.util.helper.log.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QuizbucketstartNext extends AppCompatActivity {
    private DatabaseReference mPostReference,gPostReference;
    ListView friend_list, script_list;
    ArrayList<ShowFriendListItem> data;
    ArrayList<String> scriptArrayList;
    ShowFriendListAdapter arrayAdapter;
    ScriptListAdapter ScriptListAdapter;

    String id_key, nickname_key, timestamp_key, script_key, user1_key, user2_key, roomname_key, bucketcnt_key, state_key;
    String friend_nickname, script_name;
    ImageButton invitefriend, imageButtonHome;
    char bkcnt;
    int count;

    long Solved;
    String text_roomname;
    EditText editText_roomname;
    Button create;

    Intent intent, intent_quizsend;
    int check_choose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizbucketstart3);

        check_choose = 0;

        invitefriend = (ImageButton) findViewById(R.id.invitefriend);
        imageButtonHome = (ImageButton) findViewById(R.id.home);

        create = (Button) findViewById(R.id.create);

        friend_list = findViewById(R.id.friend_list);
        data = new ArrayList<>();
        scriptArrayList = new ArrayList<String>();

        intent = getIntent();
        id_key = intent.getStringExtra("id");
        nickname_key = intent.getStringExtra("nickname");

        arrayAdapter = new ShowFriendListAdapter();
        ScriptListAdapter = new ScriptListAdapter();

        friend_list.setAdapter(arrayAdapter);

        timestamp_key = intent.getStringExtra("timestamp");
        id_key = intent.getStringExtra("id");
        nickname_key = intent.getStringExtra("nickname");
        script_key = intent.getStringExtra("scriptnm");
        user1_key = intent.getStringExtra("user1");
        user2_key = intent.getStringExtra("user2");
        bucketcnt_key = intent.getStringExtra("bucketcnt");
        roomname_key = intent.getStringExtra("roomname");
        state_key = intent.getStringExtra("state");


        mPostReference = FirebaseDatabase.getInstance().getReference().child("user_list").child(id_key).child("my_friend_list");
        gPostReference = FirebaseDatabase.getInstance().getReference().child("chatroom_bucket_list");


        imageButtonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentHome = new Intent(QuizbucketstartNext.this, MainActivity.class);
                intentHome.putExtra("id", id_key);
                startActivity(intentHome);
            }
        });

        invitefriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextTemplate params = TextTemplate.newBuilder(
                        "친구와 함께 퀴즈버킷챌린지를 시작해보자!",
                        LinkObject.newBuilder()
                                .setWebUrl("https://skku.edu")
                                .setMobileWebUrl("https://skku.edu")
                                .build()
                )
                        .setButtonTitle("친구야 같이 하자!")
                        .build();
                KakaoLinkService.getInstance().sendDefault(QuizbucketstartNext.this, params, serverCallbackArgs, new ResponseCallback<KakaoLinkResponse>() {
                    @Override
                    public void onFailure(ErrorResult errorResult) {
                        Logger.e(errorResult.toString());
                    }
                    @Override
                    public void onSuccess(KakaoLinkResponse result) {
                    }
                });
            }
        });

        friend_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) { //custom listview에서 data 받아오기
                ShowFriendListItem temp_show = (ShowFriendListItem) parent.getItemAtPosition(position);
                friend_nickname = temp_show.getNameFriend();
                check_choose = 1;
            }
        });

        bkcnt = bucketcnt_key.charAt(6);
        count = bkcnt - '0';

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check_choose == 1) {
                    if (friend_nickname.length() > 0 ) { //create chat room
                        final ValueEventListener checkRoomRegister = new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (count == 1) {
                                    //setvalue로 user3의 이름을 설정해야함
                                    gPostReference.child(timestamp_key).child("user3").setValue(friend_nickname);
                                    Toast.makeText(QuizbucketstartNext.this, "상대방에게 다시 문제를 내보자!", Toast.LENGTH_SHORT).show();
                                    intent_quizsend = new Intent(QuizbucketstartNext.this, Quizbucketquiztype.class);
                                    intent_quizsend.putExtra("timestamp", timestamp_key);
                                    intent_quizsend.putExtra("id", id_key);
                                    intent_quizsend.putExtra("nickname", nickname_key);
                                    intent_quizsend.putExtra("scriptnm", script_key);
                                    intent_quizsend.putExtra("user1", user1_key);
                                    intent_quizsend.putExtra("user2", user2_key);
                                    intent_quizsend.putExtra("bucketcnt", bucketcnt_key);
                                    intent_quizsend.putExtra("roomname", roomname_key);
                                    intent_quizsend.putExtra("state", state_key);
                                    startActivity(intent_quizsend);
                                } // user2가 3에게 문제낼때

                                else if (count == 2) {
                                    gPostReference.child(timestamp_key).child("user4").setValue(friend_nickname);
                                    Toast.makeText(QuizbucketstartNext.this, "상대방에게 다시 문제를 내보자!", Toast.LENGTH_SHORT).show();
                                    intent_quizsend = new Intent(QuizbucketstartNext.this, Quizbucketquiztype.class);
                                    intent_quizsend.putExtra("timestamp", timestamp_key);
                                    intent_quizsend.putExtra("id", id_key);
                                    intent_quizsend.putExtra("nickname", nickname_key);
                                    intent_quizsend.putExtra("scriptnm", script_key);
                                    intent_quizsend.putExtra("user1", user1_key);
                                    intent_quizsend.putExtra("user2", user2_key);
                                    intent_quizsend.putExtra("bucketcnt", bucketcnt_key);
                                    intent_quizsend.putExtra("roomname", roomname_key);
                                    intent_quizsend.putExtra("state", state_key);
                                    startActivity(intent_quizsend);
                                } // user3이 4에게 문제낼때

                                else if (count == 3) {
                                    gPostReference.child(timestamp_key).child("user5").setValue(friend_nickname);
                                    Toast.makeText(QuizbucketstartNext.this, "상대방에게 다시 문제를 내보자!", Toast.LENGTH_SHORT).show();
                                    intent_quizsend = new Intent(QuizbucketstartNext.this, Quizbucketquiztype.class);
                                    intent_quizsend.putExtra("timestamp", timestamp_key);
                                    intent_quizsend.putExtra("id", id_key);
                                    intent_quizsend.putExtra("nickname", nickname_key);
                                    intent_quizsend.putExtra("scriptnm", script_key);
                                    intent_quizsend.putExtra("user1", user1_key);
                                    intent_quizsend.putExtra("user2", user2_key);
                                    intent_quizsend.putExtra("bucketcnt", bucketcnt_key);
                                    intent_quizsend.putExtra("roomname", roomname_key);
                                    intent_quizsend.putExtra("state", state_key);
                                    startActivity(intent_quizsend);
                                } // user4가 5에게 문제낼때

                                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                                    String user1 = postSnapshot.child("user1").getValue().toString();
                                    String user2 = postSnapshot.child("user2").getValue().toString();
                                    String bucketcnt = postSnapshot.child("bucketcnt").getValue().toString();
                                    String state = postSnapshot.child("state").getValue().toString();
                                    Log.d("_user1", user1);
                                    Log.d("_user2", user2);
                                }
                                finish();
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) { }
                        };
                        gPostReference.addValueEventListener(checkRoomRegister);
                    }
                }
                else if (check_choose == 0){
                    Toast.makeText(QuizbucketstartNext.this, "문제를 보낼 친구를 골라주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

        callback = new ResponseCallback<KakaoLinkResponse>() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                Toast.makeText(getApplicationContext(), errorResult.getErrorMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(KakaoLinkResponse result) {
                Toast.makeText(getApplicationContext(), "Successfully sent KakaoLink v2 message.", Toast.LENGTH_LONG).show();
            }
        };
        getFirebaseDatabase();
    }

    private Map<String, String> getServerCallbackArgs() {
        Map<String, String> callbackParameters = new HashMap<>();
        return callbackParameters;
    }

    private ResponseCallback<KakaoLinkResponse> callback;
    private Map<String, String> serverCallbackArgs = getServerCallbackArgs();


    public void getFirebaseDatabase() {
        try {
            final ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ShowFriendListAdapter showFriendListAdapter = new ShowFriendListAdapter();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        UserInfo friend = snapshot.getValue(UserInfo.class);
                        showFriendListAdapter.addItem(friend.profile, friend.nickname, friend.grade, friend.school, friend.id, friend.nickname, false);
                    }
                    friend_list.setAdapter(showFriendListAdapter);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            };
            mPostReference.addValueEventListener(postListener);
        } catch (java.lang.NullPointerException e) {
        }
    }
}