package edu.skku.woongjin_ai;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

public class ChatFriendActivity extends Activity {
    private DatabaseReference mPostReference, cPostReference, mPostReference2;
    ListView friend_list, recommendfriend_list;
    ArrayList<String> data, recommendListArrayList;
    ArrayAdapter<String> arrayAdapter, recommendListArrayAdapter;
    ArrayList<UserInfo> recommendList;
    UserInfo me;

    String id_key, friend_nickname, newroomname;
    String mynickname;
    String newfriend_nickname, newfriend_name;
    Button invitefriend, addfriend, create;
    EditText roomname;

    Intent intent;
    int check_choose, check_recommend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatfriend);

        check_choose = 0;
        check_recommend = 0;

        invitefriend = (Button) findViewById(R.id.invitefriend);
        addfriend = (Button)findViewById(R.id.addfriend);
        create = (Button)findViewById(R.id.create);
        roomname = (EditText)findViewById(R.id.roomname);

        friend_list = findViewById(R.id.friend_list);
        data = new ArrayList<String>();

        recommendListArrayList = new ArrayList<String>();
        recommendfriend_list = findViewById(R.id.recommendfriend_list);
        me = new UserInfo();
        recommendList = new ArrayList<UserInfo>();
        recommendList.clear();

        intent = getIntent();
        id_key = intent.getStringExtra("id");
        mynickname = intent.getStringExtra("nickname");

        cPostReference = FirebaseDatabase.getInstance().getReference().child("chatroom_list");

        mPostReference2 = FirebaseDatabase.getInstance().getReference();
        arrayAdapter = new ArrayAdapter<String>(ChatFriendActivity.this, android.R.layout.simple_list_item_1);
        recommendListArrayAdapter = new ArrayAdapter<String>(ChatFriendActivity.this, android.R.layout.simple_list_item_1);

        friend_list.setAdapter(arrayAdapter);
        recommendfriend_list.setAdapter(recommendListArrayAdapter);

        if (onlyNumCheck(id_key) == true) {
            mPostReference = FirebaseDatabase.getInstance().getReference().child("kakaouser_list").child(id_key).child("friend");
        }
        else if (onlyNumCheck(id_key) == false) {
            mPostReference = FirebaseDatabase.getInstance().getReference().child("user_list").child(id_key).child("friend");
        }

        invitefriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextTemplate params = TextTemplate.newBuilder(
                        "친구와 함께 책을 읽고 퀴즈 폭탄을 던지세요!",
                        LinkObject.newBuilder()
                                .setWebUrl("https://skku.edu")
                                .setMobileWebUrl("https://skku.edu")
                                .build()
                )
                        .setButtonTitle("친구야 같이 하자!")
                        .build();
                KakaoLinkService.getInstance().sendDefault(ChatFriendActivity.this, params, serverCallbackArgs, new ResponseCallback<KakaoLinkResponse>() {
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
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                friend_nickname = friend_list.getItemAtPosition(position).toString();
                check_choose = 1;
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newroomname = roomname.getText().toString();
                if (check_choose == 1) {
                    if (friend_nickname.length() > 0 && spaceCheck(newroomname) == false && newroomname.length() > 0) { //create chat room
                        final ValueEventListener checkRoomRegister = new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                                    String user1 = postSnapshot.child("user1").getValue().toString();
                                    String user2 = postSnapshot.child("user2").getValue().toString();
                                    //Log.d("_mynickname", mynickname);
                                    Log.d("_user1", user1);
                                    Log.d("_user2", user2);
                                    if ((user1.equals(mynickname) && user2.equals(friend_nickname)) || (user2.equals(mynickname) && user1.equals(friend_nickname))) {
                                        Toast.makeText(getApplicationContext(), "이미 " + friend_nickname + " 와 게임에 참여중입니다.\n 진행중인 request를 먼저 완료해주세요", Toast.LENGTH_SHORT).show();
                                        break;
                                    }
                                }
                                postListDatabase(true);
                                Toast.makeText(getApplicationContext(), friend_nickname + "와의 게임방이 생성되었습니다.", Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) { }
                        };
                        cPostReference.addValueEventListener(checkRoomRegister);
                    }
                    else if (friend_nickname.length() == 0 || spaceCheck(newroomname) == true || newroomname.length() == 0) {
                        roomname.setText("");
                        Toast.makeText(ChatFriendActivity.this, "채팅방 이름을 바르게 입력해주세요", Toast.LENGTH_SHORT).show();
                    }
                }
                else if (check_choose == 0){
                    Toast.makeText(ChatFriendActivity.this, "채팅할 친구를 골라주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

        recommendfriend_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long i) {
                String temp = recommendListArrayList.get(position).split("\\]")[0];
                newfriend_nickname = temp.split("\\[")[0];
                newfriend_name = temp.split("\\[")[1];
                check_recommend = 1;
            }
        });

        addfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check_recommend == 0) {
                    Toast.makeText(ChatFriendActivity.this, "추가할 친구를 선택하세요.", Toast.LENGTH_SHORT).show();
                }
                else if (check_recommend == 1) {
                    postFirebaseDatabase(true);
                    Toast.makeText(ChatFriendActivity.this, newfriend_nickname + "이 친구리스트에 추가되었습니다.", Toast.LENGTH_SHORT).show();
                    check_recommend = 0;
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
        getFirebaseDatabaseRecommendFriendList();
    }

    private Map<String, String> getServerCallbackArgs() {
        Map<String, String> callbackParameters = new HashMap<>();
        return callbackParameters;
    }

    private ResponseCallback<KakaoLinkResponse> callback;
    private Map<String, String> serverCallbackArgs = getServerCallbackArgs();

    public void getFirebaseDatabaseRecommendFriendList() {
        final ValueEventListener postListner = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot0 : dataSnapshot.getChildren()) {
                    String key = snapshot0.getKey();
                    if(key.equals(id_key)) {
                        me = snapshot0.getValue(UserInfo.class);
                        break;
                    }
                }
                String myBirthYear = me.birth;
                myBirthYear = myBirthYear.substring(0, 4);
                String mySchool = me.school;

                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String key = snapshot.getKey();
                    if(!key.equals(id_key)) {
                        UserInfo friend = snapshot.getValue(UserInfo.class);
                        String birthYear = friend.birth;
                        birthYear = birthYear.substring(0, 4);
                        String school = friend.school;
                        if (birthYear.equals(myBirthYear) || school.equals(mySchool)) {
                            recommendList.add(friend);
                        }
                    }
                }
                int cntAll = recommendList.size();
                Random generator = new Random();
                int[] randList = new int[10]; //TODO 10 나중에 수정??
                for(int i = 0; i < 5; i++) { //TODO 5 나중에 수정??
                    randList[i] = generator.nextInt(cntAll);
                    for(int j = 0; j < i; j++) {
                        if(randList[i] == randList[j]) {
                            i--;
                            break;
                        }
                    }
                }
                for(int i = 0; i < 5; i++) {
                    UserInfo finalRecommend = recommendList.get(randList[i]);
                    String post = finalRecommend.nickname + "[" + finalRecommend.name + "]"+ "\n" + finalRecommend.birth + "\n" + finalRecommend.school;
                    recommendListArrayList.add(post);
                }
                recommendListArrayAdapter.clear();
                recommendListArrayAdapter.addAll(recommendListArrayList);
                recommendListArrayAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {            }
        };
        mPostReference2.child("user_list").addValueEventListener(postListner); //TODO 카카오 유저 추가하기
    }

    public void postFirebaseDatabase(boolean add) {
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        if (add) {
            FirebasePost_friend post = new FirebasePost_friend(newfriend_nickname, newfriend_name);
            postValues = post.toMap();
        }
        childUpdates.put(newfriend_nickname, postValues);
        mPostReference.updateChildren(childUpdates);
    }

    public void postListDatabase(boolean add) {
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        if(add) {
            FirebasePost_list post = new FirebasePost_list(roomname.getText().toString(), mynickname, friend_nickname);
            postValues = post.toMap();
        }
        childUpdates.put(mynickname + "-" + friend_nickname + ":" + roomname.getText().toString(), postValues);
        cPostReference.updateChildren(childUpdates);
        roomname.setText("");
    }

    public void getFirebaseDatabase() {
        try {
            final ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String key = snapshot.getKey();
                        Log.d("friend key", key);
                        data.add(key);
                    }
                    arrayAdapter.clear();
                    arrayAdapter.addAll(data);
                    arrayAdapter.notifyDataSetChanged();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            };
            mPostReference.addValueEventListener(postListener);

        } catch (java.lang.NullPointerException e) {

        }
    }

    public boolean onlyNumCheck(String spaceCheck) {
        for (int i = 0 ; i < spaceCheck.length() ; i++) {
            if (spaceCheck.charAt(i) == '1' || spaceCheck.charAt(i) == '2' || spaceCheck.charAt(i) == '3' || spaceCheck.charAt(i) == '4' || spaceCheck.charAt(i) == '5'
                    || spaceCheck.charAt(i) == '6' || spaceCheck.charAt(i) == '7' || spaceCheck.charAt(i) == '8' || spaceCheck.charAt(i) == '9' || spaceCheck.charAt(i) == '0') {
                continue;
            }
            else {
                return false;
            }
        }
        return true;
    }

    public boolean spaceCheck(String spaceCheck) {
        for (int i = 0 ; i < spaceCheck.length() ; i++) {
            if (spaceCheck.charAt(i) == ' ')
                continue;
            else if (spaceCheck.charAt(i) != ' ') {
                return false;
            }
        }
        return true;
    }
}