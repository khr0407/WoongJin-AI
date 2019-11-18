package edu.skku.woongjin_ai;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
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
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

public class ShowFriendActivity extends Activity {
    private DatabaseReference mPostReference, mPostReference2;
    private FirebaseDatabase database;
    ListView friend_list, recommendfriend_list, search_list;
    ArrayList<String> myFriendList;
    ArrayList<UserInfo> recommendList, recommendFinalList;
    UserInfo me;
    String id_key, nickname_key;
    EditText findID;
    //    String friend_nickname;
    String myprofile,myschool,mygrade,mynickname,myname;
    String newfriend_nickname;
    String sfriend_nickname, sfriend_name, sfriend_id, sfriend_grade, sfriend_school, sfriend_profile;
    ImageButton invitefriend, addfriend, imageButtonHome;
    Button search, goback;
    Intent intent, intentHome;
    int check_recommend;
    UserInfo searched;
    UserInfo ME;
    ArrayList<UserInfo> searchList;
    ShowFriendListAdapter showFriendListAdapterS;

    ImageButton searchedAddfriend;

    int searchedFlag=0;

    SharedPreferences WhoAmI;
    SharedPreferences.Editor editor;

    FirebaseStorage storage;

    String FriendID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showfriend);




        //searchedFace=(ImageView)findViewById(R.id.friendFace);
        //searchedName=(TextView)findViewById(R.id.friendName);
        //searchedGrade=(TextView)findViewById(R.id.friendGrade);
        //searchedSchool=(TextView)findViewById(R.id.friendSchool);
        //searchedAddfriend=(ImageButton)findViewById(R.id.addFriendButton);
        goback=(Button)findViewById(R.id.goback);

//        check_choose = 0;
        check_recommend = 0;

        invitefriend = (ImageButton) findViewById(R.id.invitefriend);
        imageButtonHome = (ImageButton) findViewById(R.id.home);
        search=(Button)findViewById(R.id.search);

        findID=(EditText)findViewById(R.id.searchID);

        friend_list = findViewById(R.id.friend_list);

        myFriendList = new ArrayList<String>();
        recommendfriend_list = findViewById(R.id.recommendfriend_list);
        search_list=findViewById(R.id.searched_friend);
        me = new UserInfo();
        recommendList = new ArrayList<UserInfo>();
        recommendFinalList = new ArrayList<UserInfo>();
        searchList = new ArrayList<UserInfo>();

        showFriendListAdapterS=new ShowFriendListAdapter();

        intent = getIntent();
        id_key = intent.getStringExtra("id");
        myprofile = intent.getStringExtra("profile");
        myschool = intent.getStringExtra("school");
        mygrade = intent.getStringExtra("grade");
        mynickname = intent.getStringExtra("nickname");
        myname = intent.getStringExtra("name");


        WhoAmI=getSharedPreferences("myinfo", MODE_PRIVATE);
        editor=WhoAmI.edit();
        editor.putString("mynick", mynickname);
        editor.putString("myschool", myschool);
        editor.putString("mygrade", mygrade);
        editor.putString("myprofile", myprofile);
        editor.putString("myname", myname);
        editor.putString("myid", id_key);
        editor.commit();


        mPostReference2 = FirebaseDatabase.getInstance().getReference();
        mPostReference = FirebaseDatabase.getInstance().getReference();
        database = FirebaseDatabase.getInstance();


        getFirebaseDatabase();

        imageButtonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentHome = new Intent(ShowFriendActivity.this, MainActivity.class);
                intentHome.putExtra("id", id_key);
                startActivity(intentHome);
            }
        });

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        search.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String UID=findID.getText().toString();
                if(UID==NULL){
                    Toast.makeText(ShowFriendActivity.this, "검색할 닉네임을 입력하세요!", Toast.LENGTH_SHORT).show();
                }
                else{
                    database.getReference().addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            DataSnapshot dataSnapshot1=dataSnapshot.child("user_list");
                            for(DataSnapshot snapshot: dataSnapshot1.getChildren()){
                                if(snapshot.child("nickname").getValue().toString().equals(UID)) {
                                    searchedFlag=1;
                                    searched = snapshot.getValue(UserInfo.class);
                                    String profileUri = searched.profile;
                                    sfriend_id = searched.id;
                                    sfriend_nickname = searched.nickname;
                                    sfriend_name = searched.name;
                                    sfriend_grade = searched.grade;
                                    sfriend_school = searched.school;
                                    sfriend_profile = profileUri;
                                    ShowFriendListAdapter showFriendListAdapter=new ShowFriendListAdapter();
                                    showFriendListAdapter.addItem(sfriend_profile, sfriend_name, sfriend_grade, sfriend_school,  sfriend_id, sfriend_nickname, true);
                                    search_list.setAdapter(showFriendListAdapter);
                                    search_list.clearChoices();
                                    showFriendListAdapter.notifyDataSetChanged();
                                    break;
                                }
                            }
                            if(searchedFlag==0){
                                Toast.makeText(ShowFriendActivity.this, "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
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
                    myFriendList.clear();

                    DataSnapshot snapshot=dataSnapshot.child("user_list/"+id_key+"/my_friend_list");
                    for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                        String key2 = snapshot1.getKey();
                        myFriendList.add(key2); //myFriendList 속에 내 친구들 id 저장
                    }

                    ShowFriendListAdapter showFriendListAdapter = new ShowFriendListAdapter();
                    DataSnapshot snapshot1=dataSnapshot.child("user_list");
                    for(DataSnapshot snapshot2: snapshot1.getChildren()){
                        String keys=snapshot2.getKey();
                        for(String UID: myFriendList){
                            if(UID.equals(keys)){
                                UserInfo friend=snapshot2.getValue(UserInfo.class);
                                showFriendListAdapter.addItem(friend.profile, friend.name, friend.grade, friend.school, friend.id, friend.nickname, false);
                            }
                        }
                    }
                    friend_list.setAdapter(showFriendListAdapter);
                    friend_list.clearChoices();
                    showFriendListAdapter.notifyDataSetChanged();
                    getFirebaseDatabaseRecommendFriendList();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            };
            mPostReference2.addValueEventListener(postListener);

        } catch (java.lang.NullPointerException e) {

        }
    }

    public void getFirebaseDatabaseRecommendFriendList() {
        final ValueEventListener postListner = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myFriendList.clear();
                recommendList.clear();
                recommendFinalList.clear();

                DataSnapshot snapshot=dataSnapshot.child("user_list/"+id_key+"/my_friend_list");


                for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                    String key2 = snapshot1.getKey();
                    myFriendList.add(key2);
                }

                DataSnapshot snapshot1=dataSnapshot.child("user_list");
                for(DataSnapshot snapshot2: snapshot1.getChildren()){
                    String keys=snapshot2.getKey();
                    int flag=0;
                    for(String UID: myFriendList){
                        if(UID.equals(keys)){
                            flag=1; //이미 내 친구여
                            break;
                        }
                    }
                    if(flag==0) {//아직 내 친구 아님
                        UserInfo friend=snapshot2.getValue(UserInfo.class);
                        String grade=friend.grade;
                        String uid=friend.id;
                        if(grade.equals(mygrade) && !uid.equals(id_key)){
                            recommendList.add(friend);
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
                    showRecommendFriendListAdapter.addItem(finalRecommend.profile, finalRecommend.name, finalRecommend.grade, finalRecommend.school, finalRecommend.id, finalRecommend.nickname, true);
                }
                recommendfriend_list.setAdapter(showRecommendFriendListAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {            }
        };
        mPostReference2.addValueEventListener(postListner);
    }
}