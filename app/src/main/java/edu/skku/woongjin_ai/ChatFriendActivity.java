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
import java.util.Map;
import java.util.Random;

public class ChatFriendActivity extends Activity {
    private DatabaseReference mPostReference, mPostReference2, mPostReference3, cPostReference;
    ListView friend_list, recommendfriend_list;
    ArrayList<String> data, myFriendList, recommendListArrayList;
    ArrayAdapter<String> arrayAdapter, recommendListArrayAdapter;
    ArrayList<UserInfo> recommendList, recommendFinalList;
    UserInfo me;

    String id_key, name_key, nickname_key;
    String friend_nickname;
    String newfriend_nickname, newfriend_name, newfriend_id;
    Button invitefriend, addfriend;

    String text_roomname;
    EditText editText_roomname;
    Button create;

    Intent intent;
    int check_choose, check_recommend, flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatfriend);

        check_choose = 0;
        check_recommend = 0;
        flag = 0;

        invitefriend = (Button) findViewById(R.id.invitefriend);
        addfriend = (Button) findViewById(R.id.addfriend);

        editText_roomname = (EditText) findViewById(R.id.roomname);
        create = (Button) findViewById(R.id.create);

        friend_list = findViewById(R.id.friend_list);
        data = new ArrayList<String>();

        myFriendList = new ArrayList<String>();
        recommendListArrayList = new ArrayList<String>();
        recommendfriend_list = findViewById(R.id.recommendfriend_list);
        me = new UserInfo();
        recommendList = new ArrayList<UserInfo>();
        recommendFinalList = new ArrayList<UserInfo>();

        intent = getIntent();
        id_key = intent.getStringExtra("id");
        name_key = intent.getStringExtra("name");
        nickname_key = intent.getStringExtra("nickname");

        cPostReference = FirebaseDatabase.getInstance().getReference().child("chatroom_list");

        mPostReference2 = FirebaseDatabase.getInstance().getReference();
        //mPostReference3 = FirebaseDatabase.getInstance().getReference();
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
                text_roomname = editText_roomname.getText().toString();
                if (check_choose == 1) {
                    if (friend_nickname.length() > 0 && spaceCheck(text_roomname) == false && text_roomname.length() > 0) { //create chat room
                        final ValueEventListener checkRoomRegister = new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                                    String user1 = postSnapshot.child("user1").getValue().toString();
                                    String user2 = postSnapshot.child("user2").getValue().toString();
                                    //Log.d("_mynickname", mynickname);
                                    Log.d("_user1", user1);
                                    Log.d("_user2", user2);
                                    if (editText_roomname.getText().toString().length() > 0 && ((user1.equals(nickname_key) && user2.equals(friend_nickname)) || (user2.equals(nickname_key) && user1.equals(friend_nickname)))) { //있으면
                                        Toast.makeText(getApplicationContext(), "이미 " + friend_nickname + " 와 게임에 참여중입니다.\n 진행중인 request를 먼저 완료해주세요", Toast.LENGTH_SHORT).show();
                                        flag = 1;
                                    }
                                }
                                if (editText_roomname.getText().toString().length() > 0 && flag == 0) { //채팅방이 처음 만들어질 경우
                                    postListDatabase(true);
                                    editText_roomname.setText("");
                                    Toast.makeText(getApplicationContext(), friend_nickname + "와의 게임방이 생성되었습니다.", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                                flag = 0;
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) { }
                        };
                        cPostReference.addValueEventListener(checkRoomRegister);
                    }
                    else if (friend_nickname.length() == 0 || spaceCheck(text_roomname) == true || text_roomname.length() == 0) {
                        editText_roomname.setText("");
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
                UserInfo temp = recommendFinalList.get(position);
                newfriend_id = temp.id;
                newfriend_nickname = temp.nickname;
                newfriend_name = temp.name;
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
                    mPostReference.child(newfriend_id + "/name").setValue(newfriend_name);
                    mPostReference.child(newfriend_id + "/nickname").setValue(newfriend_nickname);

                    if (onlyNumCheck(newfriend_id) == true) {
                        mPostReference3 = FirebaseDatabase.getInstance().getReference().child("kakaouser_list").child(newfriend_id).child("friend");
                        mPostReference3.child(id_key + "/name").setValue(name_key);
                        mPostReference3.child(id_key + "/nickname").setValue(nickname_key);
                    }
                    else if (onlyNumCheck(newfriend_id) == false) {
                        mPostReference3 = FirebaseDatabase.getInstance().getReference().child("user_list").child(newfriend_id).child("friend");
                        mPostReference3.child(id_key + "/name").setValue(name_key);
                        mPostReference3.child(id_key + "/nickname").setValue(nickname_key);
                    }
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
                myFriendList.clear();
                recommendList.clear();
                recommendListArrayList.clear();
                recommendFinalList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String key = snapshot.getKey();
                    if(key.equals("user_list") || key.equals("kakaouser_list")) {
                        for(DataSnapshot snapshot0 : snapshot.getChildren()) {
                            String key1 = snapshot0.getKey();
                            if(key1.equals(id_key)) {
                                me = snapshot0.getValue(UserInfo.class);
                                for(DataSnapshot snapshot1 : snapshot0.child("friend").getChildren()) {
                                    String key2 = snapshot1.getKey();
                                    myFriendList.add(key2);
                                }
                                break;
                            }
                        }
                    }
                }
                String myGrade = me.grade;
                String mySchool = me.school;

                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String key0 = snapshot.getKey();
                    if(key0.equals("user_list") || key0.equals("kakaouser_list")) {
                        for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                            String key = snapshot1.getKey();
                            if(!key.equals(id_key)) {
                                int flag = 0;
                                String uid = snapshot1.getKey();
                                for(String friendID : myFriendList) {
                                    if(uid.equals(friendID)) {
                                        flag = 1;
                                        break;
                                    }
                                }
                                if(flag == 0) {
                                    UserInfo friend = snapshot1.getValue(UserInfo.class);
                                    String grade = friend.grade;
                                    String school = friend.school;
                                    if (grade.equals(myGrade) || school.equals(mySchool)) {
                                        recommendList.add(friend);
                                    }
                                }
                            }
                        }
                    }

                }
                int cntAll = recommendList.size();
                Random generator = new Random();
                int[] randList = new int[cntAll];
                for(int i = 0; i < cntAll; i++) {
                    randList[i] = generator.nextInt(cntAll);
                    for(int j = 0; j < i; j++) {
                        if(randList[i] == randList[j]) {
                            i--;
                            break;
                        }
                    }
                }
                for(int i = 0; i < cntAll; i++) {
                    UserInfo finalRecommend = recommendList.get(randList[i]);
                    recommendFinalList.add(finalRecommend);
                    String post = finalRecommend.nickname + "[" + finalRecommend.name + "]"+ "\n" + finalRecommend.grade + "\n" + finalRecommend.school;
                    recommendListArrayList.add(post);
                }
                recommendListArrayAdapter.clear();
                recommendListArrayAdapter.addAll(recommendListArrayList);
                recommendListArrayAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {            }
        };
        mPostReference2.addValueEventListener(postListner);
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

    public void postListDatabase(boolean add) {
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        if(add) {
            FirebasePost_list post = new FirebasePost_list(editText_roomname.getText().toString(), nickname_key, friend_nickname);
            postValues = post.toMap();
        }
        childUpdates.put(nickname_key + "-" + friend_nickname + ":" + editText_roomname.getText().toString(), postValues);
        cPostReference.updateChildren(childUpdates);
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