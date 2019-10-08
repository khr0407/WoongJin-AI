package edu.skku.woongjin_ai;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.renderscript.Sampler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kakao.kakaolink.v2.KakaoLinkResponse;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.message.template.LinkObject;
import com.kakao.message.template.TextTemplate;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;
import com.kakao.util.helper.log.Logger;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

public class ShowFriendActivity extends Activity {
    private DatabaseReference mPostReference, mPostReference2, sReference;
    private FirebaseDatabase database;
    ListView friend_list, recommendfriend_list, search_list;
    ArrayList<String> myFriendList;
    ArrayList<UserInfo> recommendList, recommendFinalList;
    UserInfo me;
    String id_key, name_key, nickname_key;
    EditText findID;
//    String friend_nickname;
    String newfriend_nickname, newfriend_name, newfriend_id, newfriend_grade, newfriend_school;
    ImageButton invitefriend, addfriend, imageButtonHome;
    Button search;
    TextView searchName, searchGrade, searchSchool;
    ImageView searchFace;
    Intent intent, intentHome;
//    int check_choose;
    int check_recommend;
    UserInfo searched;
    ArrayList<UserInfo> searchList;


    FirebaseStorage storage;
    private StorageReference storageReference, dataReference;

    String FriendID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showfriend);


//        check_choose = 0;
        check_recommend = 0;

        invitefriend = (ImageButton) findViewById(R.id.invitefriend);
        addfriend = (ImageButton) findViewById(R.id.addfriend);
        imageButtonHome = (ImageButton) findViewById(R.id.home);
        search=(Button)findViewById(R.id.search);

        findID=(EditText)findViewById(R.id.searchID);

        friend_list = findViewById(R.id.friend_list);
        search_list=findViewById(R.id.search_list);

        myFriendList = new ArrayList<String>();
        recommendfriend_list = findViewById(R.id.recommendfriend_list);
        me = new UserInfo();
        recommendList = new ArrayList<UserInfo>();
        recommendFinalList = new ArrayList<UserInfo>();
        searchList = new ArrayList<UserInfo>();

        intent = getIntent();
        id_key = intent.getStringExtra("id");
        name_key = intent.getStringExtra("name");
        nickname_key = intent.getStringExtra("nickname");

        mPostReference2 = FirebaseDatabase.getInstance().getReference();
        //mPostReference3 = FirebaseDatabase.getInstance().getReference();

        mPostReference = FirebaseDatabase.getInstance().getReference().child("user_list").child(id_key).child("my_friend_list");
        database = FirebaseDatabase.getInstance();

        getFirebaseDatabaseRecommendFriendList();




        imageButtonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentHome = new Intent(ShowFriendActivity.this, MainActivity.class);
                intentHome.putExtra("id", id_key);
                startActivity(intentHome);
            }
        });

        search.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String UID=findID.getText().toString();
                database.getReference().child("user_list").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ShowFriendListAdapter showFriendListAdapter = new ShowFriendListAdapter();
                        int flag=0;
                        for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                            if(snapshot.getKey().equals(UID)){
                                searched=snapshot.getValue(UserInfo.class);
                                searchList.add(searched);
                                showFriendListAdapter.addItem(ContextCompat.getDrawable(getApplicationContext(), R.drawable.kakao_default_profile_image), searched.nickname + "[" + searched.name + "]", searched.grade, searched.school);
                                search_list.setAdapter(showFriendListAdapter);
                                flag=1;
                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
//        friend_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                friend_nickname = friend_list.getItemAtPosition(position).toString();
//                check_choose = 1;
//            }
//        });

        recommendfriend_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long i) {
                UserInfo temp = recommendFinalList.get(position);
                newfriend_id = temp.id;
                newfriend_nickname = temp.nickname;
                newfriend_name = temp.name;
                newfriend_grade = temp.grade;
                newfriend_school = temp.school;
                check_recommend = 1;
            }
        });

        search_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserInfo temp = searchList.get(position);
                newfriend_id = temp.id;
                newfriend_nickname = temp.nickname;
                newfriend_name = temp.name;
                newfriend_grade = temp.grade;
                newfriend_school = temp.school;
                check_recommend = 1;
            }
        });

        addfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check_recommend == 0) {
                    Toast.makeText(ShowFriendActivity.this, "추가할 친구를 선택하세요.", Toast.LENGTH_SHORT).show();
                }
                else if (check_recommend == 1) {
                    mPostReference.child(newfriend_id + "/name").setValue(newfriend_name);
                    mPostReference.child(newfriend_id + "/nickname").setValue(newfriend_nickname);
                    mPostReference.child(newfriend_id + "/grade").setValue(newfriend_grade);
                    mPostReference.child(newfriend_id + "/school").setValue(newfriend_school);

//                    mPostReference3.child(id_key + "/name").setValue(me.name);
//                    mPostReference3.child(id_key + "/nickname").setValue(me.nickname);
//                    mPostReference3.child(id_key + "/grade").setValue(me.grade);
//                    mPostReference3.child(id_key + "/school").setValue(me.school);

                    Toast.makeText(ShowFriendActivity.this, newfriend_nickname + "(이)가 친구리스트에 추가되었습니다.", Toast.LENGTH_SHORT).show();
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
                recommendFinalList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String key = snapshot.getKey();
                    if(key.equals("user_list")) {
                        for(DataSnapshot snapshot0 : snapshot.getChildren()) {
                            String key1 = snapshot0.getKey();
                            if(key1.equals(id_key)) {
                                me = snapshot0.getValue(UserInfo.class);
                                for(DataSnapshot snapshot1 : snapshot0.child("my_friend_list").getChildren()) {
                                    String key2 = snapshot1.getKey();
                                    myFriendList.add(key2);
                                }

                                break;
                            }
                        }
                    }
                }

                ShowFriendListAdapter showFriendListAdapter = new ShowFriendListAdapter();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String key0 = snapshot.getKey();
                    if(key0.equals("user_list")) {
                        for(DataSnapshot snapshot1 : snapshot.child("my_friend_list").getChildren()) {
                            String uid = snapshot1.getKey();
                            for(String friendID : myFriendList) {
                                if(uid.equals(friendID)) {
                                    UserInfo friend=snapshot1.getValue(UserInfo.class);
                                    showFriendListAdapter.addItem(ContextCompat.getDrawable(getApplicationContext(), R.drawable.kakao_default_profile_image), friend.nickname + "[" + friend.name + "]", friend.grade, friend.school);
                                    friend_list.setAdapter(showFriendListAdapter);
                                    break;
                                    }
                                }

                        }
                    }

                }

                String myGrade = me.grade;
                String mySchool = me.school;

                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String key0 = snapshot.getKey();
                    if(key0.equals("user_list")) {
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
                ShowFriendListAdapter showRecommendFriendListAdapter = new ShowFriendListAdapter();
                for(int i = 0; i < cntAll; i++) {
                    UserInfo finalRecommend = recommendList.get(randList[i]);
                    recommendFinalList.add(finalRecommend);
                    showRecommendFriendListAdapter.addItem(ContextCompat.getDrawable(getApplicationContext(), R.drawable.kakao_default_profile_image), finalRecommend.nickname + "[" + finalRecommend.name + "]", finalRecommend.grade, finalRecommend.school);
                }
                recommendfriend_list.setAdapter(showRecommendFriendListAdapter);
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
                    ShowFriendListAdapter showFriendListAdapter = new ShowFriendListAdapter();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        UserInfo friend = snapshot.getValue(UserInfo.class);
                        showFriendListAdapter.addItem(ContextCompat.getDrawable(getApplicationContext(), R.drawable.kakao_default_profile_image), friend.nickname + "[" + friend.name + "]", friend.grade, friend.school);
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