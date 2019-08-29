package edu.skku.woongjin_ai;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

public class ShowFriendQuizActivity extends AppCompatActivity
        implements FriendOXQuizFragment.OnFragmentInteractionListener, ShowScriptFragment.OnFragmentInteractionListener, ShowHintFragment.OnFragmentInteractionListener, CorrectFriendQuizFragment.OnFragmentInteractionListener, WrongFriendQuizFragment.OnFragmentInteractionListener {

    //TODO UI 백그라운드 이미지로 바꿀까?? 지문 제목 추가??

    Intent intent, intentHome;
    String id, scriptnm, background;
    public DatabaseReference mPostReference;
    ListView myFriendQuizListView, likeQuizListView;
    ArrayList<String> likeQuizList, myFriendList;
    ArrayList<QuizOXShortwordTypeInfo> myFriendOXQuizList, myFriendShortQuizList, myFriendOXQuizListR, myFriendShortQuizListR;
    ArrayList<QuizChoiceTypeInfo> myFriendChoiceQuizList, myFriendChoiceQuizListR;
    MyFriendQuizListAdapter myFriendQuizListAdapter;
    FriendOXQuizFragment friendOXQuizFragment;
    ShowScriptFragment showScriptFragment;
    ShowHintFragment showHintFragment;
    CorrectFriendQuizFragment correctFriendQuizFragment;
    WrongFriendQuizFragment wrongFriendQuizFragment;
    TextView textView;
    int cntOX, cntChoice, cntShort, flag = 0;
    UserInfo me;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showfriendquiz);

        intent = getIntent();
        id = intent.getStringExtra("id");
        scriptnm = intent.getStringExtra("scriptnm");
        background = intent.getStringExtra("background");

        mPostReference = FirebaseDatabase.getInstance().getReference();

        friendOXQuizFragment = new FriendOXQuizFragment();
        showScriptFragment = new ShowScriptFragment();
        showHintFragment = new ShowHintFragment();
        correctFriendQuizFragment = new CorrectFriendQuizFragment();
        wrongFriendQuizFragment = new WrongFriendQuizFragment();

        myFriendQuizListView = (ListView) findViewById(R.id.myFriendQuizList);
        likeQuizListView = (ListView) findViewById(R.id.likeQuizList);
        textView = (TextView) findViewById(R.id.textShowFriendQuiz);
        ImageButton homeButton = (ImageButton) findViewById(R.id.home);

        likeQuizList = new ArrayList<String>();
        myFriendList = new ArrayList<String>();
        myFriendOXQuizList = new ArrayList<QuizOXShortwordTypeInfo>();
        myFriendOXQuizListR = new ArrayList<QuizOXShortwordTypeInfo>();
        myFriendShortQuizList = new ArrayList<QuizOXShortwordTypeInfo>();
        myFriendShortQuizListR = new ArrayList<QuizOXShortwordTypeInfo>();
        myFriendChoiceQuizList = new ArrayList<QuizChoiceTypeInfo>();
        myFriendChoiceQuizListR = new ArrayList<QuizChoiceTypeInfo>();
        myFriendQuizListAdapter = new MyFriendQuizListAdapter();

        getFirebaseDatabaseMyFriendQuiz();
        getFirebaseDatabaseLikeQuiz();

        getFirebaseDatabaseUserInfo();

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentHome = new Intent(ShowFriendQuizActivity.this, MainActivity.class);
                intentHome.putExtra("id", id);
                startActivity(intentHome);
            }
        });

        myFriendQuizListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long i) {
                if(flag == 1) {
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.remove(friendOXQuizFragment);
                    fragmentTransaction.commit();
                    friendOXQuizFragment = new FriendOXQuizFragment();
                } else if(flag == 2) {
//                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                    fragmentTransaction.remove(friendChoiceQuizFragment);
//                    fragmentTransaction.commit();
                } else if(flag == 3) {
//                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                    fragmentTransaction.remove(friendShortwordQuizFragment);
//                    fragmentTransaction.commit();
                }

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                if(position < cntOX) {
                    flag = 1;
                    QuizOXShortwordTypeInfo quiz = myFriendOXQuizListR.get(position);

                    transaction.replace(R.id.contentShowFriendQuiz, friendOXQuizFragment);
                    Bundle bundle = new Bundle(10);
                    bundle.putString("id", id);
                    bundle.putString("scriptnm", scriptnm);
                    bundle.putString("question", quiz.question);
                    bundle.putString("answer", quiz.answer);
                    bundle.putString("uid", quiz.uid);
                    bundle.putString("star", quiz.star);
                    bundle.putString("like", quiz.like);
                    bundle.putString("desc", quiz.desc);
                    bundle.putString("key", quiz.key);
                    bundle.putInt("cnt", quiz.cnt);
                    friendOXQuizFragment.setArguments(bundle);
                    transaction.commit();
                } else {
                    position -= cntOX;
                    if(position < cntChoice) {
//                        flag = 2;
                        QuizChoiceTypeInfo quiz = myFriendChoiceQuizList.get(position);


                    } else {
                        position -= cntChoice;
//                        flag = 3;
                        QuizOXShortwordTypeInfo quiz = myFriendShortQuizList.get(position);


                    }
                }
            }
        });
    }

    private void getFirebaseDatabaseMyFriendQuiz() {
        mPostReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myFriendList.clear();
                myFriendOXQuizList.clear();
                myFriendChoiceQuizList.clear();
                myFriendShortQuizList.clear();
                myFriendOXQuizListR.clear();
                myFriendChoiceQuizListR.clear();
                myFriendShortQuizListR.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String key = snapshot.getKey();
                    if(key.equals("kakaouser_list") || key.equals("user_list")) {
                        for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                            String key1 = snapshot1.getKey();
                            if(key1.equals(id)) {
                                for(DataSnapshot snapshot2 : snapshot1.child("friend").getChildren()) {
                                    String key2 = snapshot2.getKey();
                                    myFriendList.add(key2);
                                }
                                break;
                            }
                        }
                    }
                }

                for(DataSnapshot snapshot : dataSnapshot.child("quiz_list/" + scriptnm).getChildren()) {
                    String key = snapshot.getKey();
                    if(key.equals("type1")) {
                        for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                            String uid = snapshot1.child("uid").getValue().toString();
                            for(String friend : myFriendList) {
                                if(uid.equals(friend)) {
                                    QuizOXShortwordTypeInfo quiz = snapshot1.getValue(QuizOXShortwordTypeInfo.class);
                                    myFriendOXQuizList.add(quiz);
                                    break;
                                }
                            }
                        }
                    } else if(key.equals("type2")) {
                        for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                            String uid = snapshot1.child("uid").getValue().toString();
                            for(String friend : myFriendList) {
                                if(uid.equals(friend)) {
                                    QuizChoiceTypeInfo quiz = snapshot1.getValue(QuizChoiceTypeInfo.class);
                                    myFriendChoiceQuizList.add(quiz);
                                    break;
                                }
                            }
                        }
                    } else if(key.equals("type3")) {
                        for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                            String uid = snapshot1.child("uid").getValue().toString();
                            for(String friend : myFriendList) {
                                if(uid.equals(friend)) {
                                    QuizOXShortwordTypeInfo quiz = snapshot1.getValue(QuizOXShortwordTypeInfo.class);
                                    myFriendShortQuizList.add(quiz);
                                    break;
                                }
                            }
                        }
                    }
                }

                Random generator = new Random();
                cntOX = myFriendOXQuizList.size();
                cntChoice = myFriendChoiceQuizList.size();
                cntShort = myFriendShortQuizList.size();
                int[] randList = new int[5];

                int cnt = 5;
                if(cnt > cntOX) cnt = cntOX;
                for(int i=0; i<cnt; i++) {
                    randList[i] = generator.nextInt(cntOX);
                    for(int j = 0; j < i; j++) {
                        if(randList[i] == randList[j]) {
                            i--;
                            break;
                        }
                    }
                }
                for(int i=0; i<cnt; i++) {
                    myFriendQuizListAdapter.addItem(myFriendOXQuizList.get(randList[i]).question, myFriendOXQuizList.get(randList[i]).uid, ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_bigthumb), myFriendOXQuizList.get(randList[i]).like);
                    myFriendOXQuizListR.add(myFriendOXQuizList.get(randList[i]));
                }

                cnt = 5;
                if(cnt > cntChoice) cnt = cntChoice;
                for(int i=0; i<cnt; i++) {
                    randList[i] = generator.nextInt(cntChoice);
                    for(int j = 0; j < i; j++) {
                        if(randList[i] == randList[j]) {
                            i--;
                            break;
                        }
                    }
                }
                for(int i=0; i<cnt; i++) {
                    myFriendQuizListAdapter.addItem(myFriendChoiceQuizList.get(randList[i]).question, myFriendChoiceQuizList.get(randList[i]).uid, ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_bigthumb), myFriendChoiceQuizList.get(randList[i]).like);
                    myFriendChoiceQuizListR.add(myFriendChoiceQuizList.get(randList[i]));
                }

                cnt = 5;
                if(cnt > cntShort) cnt = cntShort;
                for(int i=0; i<cnt; i++) {
                    randList[i] = generator.nextInt(cntShort);
                    for(int j = 0; j < i; j++) {
                        if(randList[i] == randList[j]) {
                            i--;
                            break;
                        }
                    }
                }
                for(int i=0; i<cnt; i++) {
                    myFriendQuizListAdapter.addItem(myFriendShortQuizList.get(randList[i]).question, myFriendShortQuizList.get(randList[i]).uid, ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_bigthumb), myFriendShortQuizList.get(randList[i]).like);
                    myFriendShortQuizListR.add(myFriendShortQuizList.get(randList[i]));
                }

                myFriendQuizListView.setAdapter(myFriendQuizListAdapter);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {            }
        });
    }

    private void getFirebaseDatabaseLikeQuiz() {
        mPostReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.child("quiz_list/").getChildren()) {
                    String key = snapshot.getKey();
                    if(key.equals(scriptnm)) {




                        break;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {            }
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
                                textView.setText(me.nickname + "의 친구가 낸 문제야!");
                                break;
                            }
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        mPostReference.addValueEventListener(postListener);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
