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
        implements FriendOXQuizFragment.OnFragmentInteractionListener, FriendChoiceQuizFragment.OnFragmentInteractionListener, FriendShortwordQuizFragment.OnFragmentInteractionListener, ShowScriptFragment.OnFragmentInteractionListener, ShowHintFragment.OnFragmentInteractionListener, CorrectFriendQuizFragment.OnFragmentInteractionListener, WrongFriendQuizFragment.OnFragmentInteractionListener {

    Intent intent, intentHome, intentUpdate;
    String id, scriptnm, background;
    DatabaseReference mPostReference;
    ListView myFriendQuizListView, likeQuizListView;
    ArrayList<String> likeQuizList, myFriendList, solvedQuizList;
    ArrayList<QuizOXShortwordTypeInfo> myFriendOXQuizList, myFriendShortQuizList, myFriendOXQuizListR, myFriendShortQuizListR;
    ArrayList<QuizChoiceTypeInfo> myFriendChoiceQuizList, myFriendChoiceQuizListR;
    MyFriendQuizListAdapter myFriendQuizListAdapter;
    FriendOXQuizFragment friendOXQuizFragment;
    FriendChoiceQuizFragment friendChoiceQuizFragment;
    FriendShortwordQuizFragment friendShortwordQuizFragment;
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
        friendChoiceQuizFragment = new FriendChoiceQuizFragment();
        friendShortwordQuizFragment = new FriendShortwordQuizFragment();
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
        solvedQuizList = new ArrayList<String>();
        myFriendOXQuizList = new ArrayList<QuizOXShortwordTypeInfo>();
        myFriendOXQuizListR = new ArrayList<QuizOXShortwordTypeInfo>();
        myFriendShortQuizList = new ArrayList<QuizOXShortwordTypeInfo>();
        myFriendShortQuizListR = new ArrayList<QuizOXShortwordTypeInfo>();
        myFriendChoiceQuizList = new ArrayList<QuizChoiceTypeInfo>();
        myFriendChoiceQuizListR = new ArrayList<QuizChoiceTypeInfo>();
        myFriendQuizListAdapter = new MyFriendQuizListAdapter();

        getFirebaseDatabaseUserInfo();
        getFirebaseDatabaseMyFriendQuiz();
        getFirebaseDatabaseLikeQuiz();

        intentUpdate = new Intent(ShowFriendQuizActivity.this, ShowFriendQuizActivity.class);
        intentUpdate.putExtra("id", id);
        intentUpdate.putExtra("scriptnm", scriptnm);
        intentUpdate.putExtra("background", background);

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
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.remove(friendChoiceQuizFragment);
                    fragmentTransaction.commit();
                    friendChoiceQuizFragment = new FriendChoiceQuizFragment();
                } else if(flag == 3) {
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.remove(friendShortwordQuizFragment);
                    fragmentTransaction.commit();
                    friendShortwordQuizFragment = new FriendShortwordQuizFragment();
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
                        flag = 2;
                        QuizChoiceTypeInfo quiz = myFriendChoiceQuizListR.get(position);

                        transaction.replace(R.id.contentShowFriendQuiz, friendChoiceQuizFragment);
                        Bundle bundle = new Bundle(14);
                        bundle.putString("id", id);
                        bundle.putString("scriptnm", scriptnm);
                        bundle.putString("question", quiz.question);
                        bundle.putString("answer", quiz.answer);
                        bundle.putString("answer1", quiz.answer1);
                        bundle.putString("answer2", quiz.answer2);
                        bundle.putString("answer3", quiz.answer3);
                        bundle.putString("answer4", quiz.answer4);
                        bundle.putString("uid", quiz.uid);
                        bundle.putString("star", quiz.star);
                        bundle.putString("like", quiz.like);
                        bundle.putString("desc", quiz.desc);
                        bundle.putString("key", quiz.key);
                        bundle.putInt("cnt", quiz.cnt);
                        friendChoiceQuizFragment.setArguments(bundle);
                        transaction.commit();
                    } else {
                        position -= cntChoice;
                        flag = 3;
                        QuizOXShortwordTypeInfo quiz = myFriendShortQuizListR.get(position);

                        transaction.replace(R.id.contentShowFriendQuiz, friendShortwordQuizFragment);
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
                        friendShortwordQuizFragment.setArguments(bundle);
                        transaction.commit();
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

                for(DataSnapshot snapshot : dataSnapshot.child("user_list/" + id + "/my_friend_list").getChildren()) {
                    String key = snapshot.getKey();
                    myFriendList.add(key);
                }

                for(DataSnapshot snapshot : dataSnapshot.child("quiz_list/" + scriptnm).getChildren()) {
                    String type = snapshot.child("type").getValue().toString();
                    String uid = snapshot.child("uid").getValue().toString();
                    for(String friend : myFriendList) {
                        if(friend.equals(uid)) {
                            int flag = 0;
                            for(String solved : solvedQuizList) {
                                String key = snapshot.getKey();
                                if(solved.equals(key)) {
                                    flag = 1;
                                    break;
                                }
                            }
                            if(flag == 0) {
                                if(type.equals("1")) {
                                    QuizOXShortwordTypeInfo quiz = snapshot.getValue(QuizOXShortwordTypeInfo.class);
                                    myFriendOXQuizList.add(quiz);
                                } else if(type.equals("2")) {
                                    QuizChoiceTypeInfo quiz = snapshot.getValue(QuizChoiceTypeInfo.class);
                                    myFriendChoiceQuizList.add(quiz);
                                } else if(type.equals("3")) {
                                    QuizOXShortwordTypeInfo quiz = snapshot.getValue(QuizOXShortwordTypeInfo.class);
                                    myFriendShortQuizList.add(quiz);
                                }
                            }
                            break;
                        }
                    }
                }

//                for(DataSnapshot snapshot : dataSnapshot.child("quiz_list/" + scriptnm).getChildren()) {
//                    String key = snapshot.getKey();
//                    if(key.equals("type1")) {
//                        for(DataSnapshot snapshot1 : snapshot.getChildren()) {
//                            String uid = snapshot1.child("uid").getValue().toString();
//                            for(String friend : myFriendList) {
//                                if(uid.equals(friend)) {
//                                    int flag = 0;
//                                    for(String solvedQuiz : solvedQuizList) {
//                                        String key2 = snapshot1.getKey();
//                                        if(solvedQuiz.equals(key2)) {
//                                            flag = 1;
//                                            break;
//                                        }
//                                    }
//                                    if(flag == 0) {
//                                        QuizOXShortwordTypeInfo quiz = snapshot1.getValue(QuizOXShortwordTypeInfo.class);
//                                        myFriendOXQuizList.add(quiz);
//                                    }
//                                    break;
//                                }
//                            }
//                        }
//                    } else if(key.equals("type2")) {
//                        for(DataSnapshot snapshot1 : snapshot.getChildren()) {
//                            String uid = snapshot1.child("uid").getValue().toString();
//                            for(String friend : myFriendList) {
//                                if(uid.equals(friend)) {
//                                    int flag = 0;
//                                    for(String solvedQuiz : solvedQuizList) {
//                                        String key2 = snapshot1.getKey();
//                                        if(solvedQuiz.equals(key2)) {
//                                            flag = 1;
//                                            break;
//                                        }
//                                    }
//                                    if(flag == 0) {
//                                        QuizChoiceTypeInfo quiz = snapshot1.getValue(QuizChoiceTypeInfo.class);
//                                        myFriendChoiceQuizList.add(quiz);
//                                    }
//                                    break;
//                                }
//                            }
//                        }
//                    } else if(key.equals("type3")) {
//                        for(DataSnapshot snapshot1 : snapshot.getChildren()) {
//                            String uid = snapshot1.child("uid").getValue().toString();
//                            for(String friend : myFriendList) {
//                                if(uid.equals(friend)) {
//                                    int flag = 0;
//                                    for(String solvedQuiz : solvedQuizList) {
//                                        String key2 = snapshot1.getKey();
//                                        if(solvedQuiz.equals(key2)) {
//                                            flag = 1;
//                                            break;
//                                        }
//                                    }
//                                    if(flag == 0) {
//                                        QuizOXShortwordTypeInfo quiz = snapshot1.getValue(QuizOXShortwordTypeInfo.class);
//                                        myFriendShortQuizList.add(quiz);
//                                    }
//                                    break;
//                                }
//                            }
//                        }
//                    }
//                }

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
        mPostReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                solvedQuizList.clear();
                me = dataSnapshot.child("user_list/" + id).getValue(UserInfo.class);
                textView.setText(me.nickname + "의 친구가 낸 문제야!");

                for(DataSnapshot snapshot : dataSnapshot.child("user_list/" + id + "/my_script_list/" + scriptnm + "/solved_list").getChildren()) {
                    String key = snapshot.getKey();
                    solvedQuizList.add(key);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
