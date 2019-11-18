package edu.skku.woongjin_ai;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

public class Quizbucketstart extends Activity {
    private DatabaseReference mPostReference, mPostReference2, mPostReference3;
    ListView friend_list;
    ArrayList<String> myFriendList;
    UserInfo me;
    String[] array = {"","","",""};

    String id_key, name_key, nickname_key;
    //    String friend_nickname;

    String newfriend_nickname, newfriend_name, newfriend_id, newfriend_grade, newfriend_school;
    ImageButton invitefriend, addfriend, imageButtonHome;

    Intent intent, intentHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizbucketstart);

        invitefriend = (ImageButton) findViewById(R.id.invitefriend);
        addfriend = (ImageButton) findViewById(R.id.addfriend);
        imageButtonHome = (ImageButton) findViewById(R.id.home);

        friend_list = findViewById(R.id.friend_list);

        myFriendList = new ArrayList<String>();
        me = new UserInfo();

        intent = getIntent();
        id_key = intent.getStringExtra("id");
        nickname_key = intent.getStringExtra("nickname");

        mPostReference2 = FirebaseDatabase.getInstance().getReference();
        //mPostReference3 = FirebaseDatabase.getInstance().getReference();

        mPostReference = FirebaseDatabase.getInstance().getReference().child("user_list").child(id_key).child("my_friend_list");


        friend_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                String friend1 = friend_list.getItemAtPosition(position).toString();
                String friend2 = friend_list.getItemAtPosition(position).toString();
                String friend3 = friend_list.getItemAtPosition(position).toString();
                String friend4 = friend_list.getItemAtPosition(position).toString();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("friend1", friend1);
                intent.putExtra("friend2", friend2);
                intent.putExtra("friend3", friend3);
                intent.putExtra("friend4", friend4);
                intent.putExtra("name", nickname_key);
                startActivity(intent);
            }
        });

        imageButtonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentHome = new Intent(Quizbucketstart.this, MainActivity.class);
                intentHome.putExtra("id", id_key);
                startActivity(intentHome);
            }
        });

//        friend_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                friend_nickname = friend_list.getItemAtPosition(position).toString();
//                check_choose = 1;
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