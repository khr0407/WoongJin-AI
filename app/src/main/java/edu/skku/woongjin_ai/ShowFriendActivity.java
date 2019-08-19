package edu.skku.woongjin_ai;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class ShowFriendActivity extends Activity {
    private DatabaseReference mPostReference, mPostReference2;;
    ListView friend_list, recommendListView;
    ArrayList<String> data, recommendListArrayList;
    ArrayAdapter<String> arrayAdapter, recommendListArrayAdapter;
    ArrayList<UserInfo> recommendList;
    UserInfo me;
    String id_key;
    Button invitefriend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showfriend);

        invitefriend = (Button) findViewById(R.id.invitefriend);
        friend_list = findViewById(R.id.friend_list);
        recommendListView = findViewById(R.id.recommendfriend_list);
        data = new ArrayList<String>();
        recommendListArrayList = new ArrayList<String>();
        me = new UserInfo();
        recommendList = new ArrayList<UserInfo>();
        recommendList.clear();

        final Intent intent = getIntent();
        id_key = intent.getStringExtra("id");

        mPostReference = FirebaseDatabase.getInstance().getReference();
        mPostReference2 = FirebaseDatabase.getInstance().getReference();
        arrayAdapter = new ArrayAdapter<String>(ShowFriendActivity.this, android.R.layout.simple_list_item_1);
        recommendListArrayAdapter = new ArrayAdapter<String>(ShowFriendActivity.this, android.R.layout.simple_list_item_1);
        friend_list.setAdapter(arrayAdapter);
        recommendListView.setAdapter(recommendListArrayAdapter);

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

                KakaoLinkService.getInstance().sendDefault(ShowFriendActivity.this, params, serverCallbackArgs, new ResponseCallback<KakaoLinkResponse>() {
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
                int[] randList = new int[10]; ////////////////////////////// 10
                for(int i=0; i<5; i++) { ////////////////////////// 5
                    randList[i] = generator.nextInt(cntAll);
                    for(int j=0; j<i; j++) {
                        if(randList[i] == randList[j]) {
                            i--;
                            break;
                        }
                    }
                }

                for(int i=0; i<5; i++) {
                    UserInfo finalRecommend = recommendList.get(randList[i]);
                    String post = finalRecommend.name + "\n" + finalRecommend.birth + "\n" + finalRecommend.school;
                    recommendListArrayList.add(post);
                }
                recommendListArrayAdapter.clear();
                recommendListArrayAdapter.addAll(recommendListArrayList);
                recommendListArrayAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {            }
        };
        mPostReference2.child("user_list").addValueEventListener(postListner);
    }

    public void getFirebaseDatabase() {
        try {
            final ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String key = snapshot.getKey();
                        Log.d("friend key", key);
                        //if (key.equals(id_key))
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
}