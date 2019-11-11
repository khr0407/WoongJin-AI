package edu.skku.woongjin_ai;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.kakao.usermgmt.response.model.User;
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
    String id_key, name_key, nickname_key, grade_key, school_key, profile_key, myGrade, mySchool;
    EditText findID;
    //    String friend_nickname;
    String myprofile,myschool,mygrade,mynickname,myname;
    String newfriend_nickname, newfriend_name, newfriend_id, newfriend_grade, newfriend_school, newfriend_profile;
    String sfriend_nickname, sfriend_name, sfriend_id, sfriend_grade, sfriend_school, sfriend_profile;
    ImageButton invitefriend, addfriend, imageButtonHome;
    Button search, goback, addrecommendfriend;
    TextView searchName, searchGrade, searchSchool;
    ImageView searchFace;
    Intent intent, intentHome;
    //    int check_choose;
    int check_recommend;
    UserInfo searched;
    UserInfo ME;
    ArrayList<UserInfo> searchList;
    ShowFriendListAdapter showFriendListAdapterS;

    ImageView searchedFace;
    TextView searchedName, searchedGrade, searchedSchool;
    ImageButton searchedAddfriend;

    int searchedFlag=0;

    SharedPreferences WhoAmI;
    SharedPreferences.Editor editor;

    FirebaseStorage storage;
    private StorageReference storageReference, dataReference;

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
                database.getReference().addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        DataSnapshot dataSnapshot1=dataSnapshot.child("user_list");
                        for(DataSnapshot snapshot: dataSnapshot1.getChildren()){
                            if(snapshot.getKey().equals(UID)) {
                                //수정필요.. 리스트 초기화하는거. 아님 아예 리스트를 하지 말고 친구추가 버튼을 따로 맹글든가
//                                searchList.clear();
//                                search_list.clearChoices();
//                                showFriendListAdapterS.notifyDataSetChanged();
                                searchedFlag=1;
                                searched = snapshot.getValue(UserInfo.class);
                                //searchedGrade.setText(searched.grade+"학년");
                                //searchedName.setText(searched.nickname);
                                //searchedSchool.setText(searched.school);
                                //searchedAddfriend.setVisibility(View.VISIBLE);
                                //searchedAddfriend.setClickable(true);
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
                                /*
                                if (profileUri.equals("noimage")){
                                    searchedFace.setVisibility(View.VISIBLE);
                                }else{
                                    storage = FirebaseStorage.getInstance();
                                    storageReference = storage.getInstance().getReference();
                                    dataReference = storageReference.child("/profile/" + profileUri);
                                    dataReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Picasso.with(ShowFriendActivity.this)
                                                    .load(uri)
                                                    .error(R.drawable.btn_x)
                                                    .into(searchedFace);
                                        }
                                    });
                                }*/
//                                searchList.add(searched);
//                                showFriendListAdapterS.addItem(searched.profile, searched.nickname, searched.grade, searched.school);
//                                search_list.setAdapter(showFriendListAdapterS);
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
        });
//        friend_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                friend_nickname = friend_list.getItemAtPosition(position).toString();
//                check_choose = 1;
//            }
//        });
/*
        searchedAddfriend.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(searchedFlag==1){
                    postFirebaseFriendInfo();
                    Toast.makeText(ShowFriendActivity.this, newfriend_nickname + "(이)가 친구리스트에 추가되었습니다.", Toast.LENGTH_SHORT).show();
                    searchedFlag=0;
                    getFirebaseDatabase();
                }
            }
        });
*/

/*
        recommendfriend_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long i) {
                UserInfo temp = recommendFinalList.get(position);
                newfriend_id = temp.id;
                newfriend_nickname = temp.nickname;
                newfriend_name = temp.name;
                newfriend_grade = temp.grade;
                newfriend_school = temp.school;
                newfriend_profile=temp.profile;
                check_recommend = 1;
            }
        });
*/
//        search_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                UserInfo temp = searchList.get(position);
//                newfriend_id = temp.id;
//                newfriend_nickname = temp.nickname;
//                newfriend_name = temp.name;
//                newfriend_grade = temp.grade;
//                newfriend_school = temp.school;
//                newfriend_profile=temp.profile;
//                check_recommend = 1;
//            }
//        });

//        addfriend.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (check_recommend == 0) {
//                    Toast.makeText(ShowFriendActivity.this, "추가할 친구를 선택하세요.", Toast.LENGTH_SHORT).show();
//                }
//                else if (check_recommend == 1) {
//                    /*mPostReference.child(newfriend_id + "/name").setValue(newfriend_name);
//                    mPostReference.child(newfriend_id + "/nickname").setValue(newfriend_nickname);
//                    mPostReference.child(newfriend_id + "/grade").setValue(newfriend_grade);
//                    mPostReference.child(newfriend_id + "/school").setValue(newfriend_school);
//                    mPostReference.child(newfriend_id + "/profile").setValue(newfriend_profile);*/
//                    getFirebaseDatabase();
//                    Toast.makeText(ShowFriendActivity.this, newfriend_nickname + "(이)가 친구리스트에 추가되었습니다.", Toast.LENGTH_SHORT).show();
//                    check_recommend = 0;
//                }
//            }
//        });

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


    public void postFirebaseFriendInfo(){
        final ValueEventListener postListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mPostReference.child(sfriend_id + "/name").setValue(sfriend_name);
                mPostReference.child(sfriend_id + "/nickname").setValue(sfriend_nickname);
                mPostReference.child(sfriend_id + "/grade").setValue(sfriend_grade);
                mPostReference.child(sfriend_id + "/school").setValue(sfriend_school);
                mPostReference.child(sfriend_id + "/profile").setValue(sfriend_profile);

                me=dataSnapshot.child("user_list/"+id_key).getValue(UserInfo.class);

                mPostReference2.child("user_list").child(sfriend_id+"/my_friend_list/"+id_key+"/name").setValue(me.name);
                mPostReference2.child("user_list").child(sfriend_id+"/my_friend_list/"+id_key + "/nickname").setValue(me.nickname);
                mPostReference2.child("user_list").child(sfriend_id+"/my_friend_list/"+id_key + "/grade").setValue(me.grade);
                mPostReference2.child("user_list").child(sfriend_id+"/my_friend_list/"+id_key + "/school").setValue(me.school);
                mPostReference2.child("user_list").child(sfriend_id+"/my_friend_list/"+id_key + "/profile").setValue(me.profile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mPostReference.addValueEventListener(postListener);
    }


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
                //addrecommendfriend=(Button) findViewById(R.id.addrecommendfriend);
                //addrecommendfriend.setClickable(true);
                //addrecommendfriend.setVisibility(View.VISIBLE);
                recommendfriend_list.setAdapter(showRecommendFriendListAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {            }
        };
        mPostReference2.addValueEventListener(postListner);
    }
}