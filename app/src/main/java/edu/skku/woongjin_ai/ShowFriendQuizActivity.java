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
import android.widget.Button;
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

    Intent intent, intentHome, intentUpdate, intentMyPage;
    String id, scriptnm, background, bookName;
    DatabaseReference mPostReference;
    ListView quizListView;
    ArrayList<String> likeQuizList, myFriendList, solvedQuizList;
    ArrayList<QuizOXShortwordTypeInfo> myFriendOXQuizList, myFriendShortQuizList, myFriendOXQuizListR, myFriendShortQuizListR;
    ArrayList<QuizOXShortwordTypeInfo> likeOXQuizList, likeShortQuizList, likeOXQuizListR, likeShortQuizListR;
    ArrayList<QuizChoiceTypeInfo> myFriendChoiceQuizList, myFriendChoiceQuizListR;
    ArrayList<QuizChoiceTypeInfo> likeChoiceQuizList, likeChoiceQuizListR;
    MyFriendQuizListAdapter myFriendQuizListAdapter;
    LikeQuizListAdapter likeQuizListAdapter;
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
    Button friendQuizButton, likeQuizButton;
    boolean isFriendQuiz = true;

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

        quizListView = (ListView) findViewById(R.id.quizList);
        textView = (TextView) findViewById(R.id.textShowFriendQuiz);
        ImageButton homeButton = (ImageButton) findViewById(R.id.home);
        ImageButton myPageButton = (ImageButton) findViewById(R.id.myPage);
        friendQuizButton = (Button) findViewById(R.id.friendQuiz);
        likeQuizButton = (Button) findViewById(R.id.likeQuiz);

        likeQuizList = new ArrayList<String>();
        myFriendList = new ArrayList<String>();
        solvedQuizList = new ArrayList<String>();
        myFriendOXQuizList = new ArrayList<QuizOXShortwordTypeInfo>();
        myFriendOXQuizListR = new ArrayList<QuizOXShortwordTypeInfo>();
        myFriendShortQuizList = new ArrayList<QuizOXShortwordTypeInfo>();
        myFriendShortQuizListR = new ArrayList<QuizOXShortwordTypeInfo>();
        myFriendChoiceQuizList = new ArrayList<QuizChoiceTypeInfo>();
        myFriendChoiceQuizListR = new ArrayList<QuizChoiceTypeInfo>();
        likeOXQuizList = new ArrayList<QuizOXShortwordTypeInfo>();
        likeOXQuizListR = new ArrayList<QuizOXShortwordTypeInfo>();
        likeChoiceQuizList = new ArrayList<QuizChoiceTypeInfo>();
        likeChoiceQuizListR = new ArrayList<QuizChoiceTypeInfo>();
        likeShortQuizList = new ArrayList<QuizOXShortwordTypeInfo>();
        likeShortQuizListR = new ArrayList<QuizOXShortwordTypeInfo>();

        myFriendQuizListAdapter = new MyFriendQuizListAdapter();
        likeQuizListAdapter = new LikeQuizListAdapter();

        getFirebaseDatabaseUserInfo();
        getFirebaseDatabaseMyFriendQuiz();
        getFirebaseDatabaseLikeQuiz();

        intentUpdate = new Intent(ShowFriendQuizActivity.this, ShowFriendQuizActivity.class);
        intentUpdate.putExtra("id", id);
        intentUpdate.putExtra("scriptnm", scriptnm);
        intentUpdate.putExtra("background", background);

        friendQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFriendQuiz) {
                    isFriendQuiz = true;
                    quizListView.setAdapter(myFriendQuizListAdapter);

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
                    flag = 0;
                }
            }
        });

        likeQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFriendQuiz) {
                    isFriendQuiz = false;
                    quizListView.setAdapter(likeQuizListAdapter);

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
                    flag = 0;
                }
            }
        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentHome = new Intent(ShowFriendQuizActivity.this, MainActivity.class);
                intentHome.putExtra("id", id);
                startActivity(intentHome);
            }
        });

        myPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentMyPage = new Intent(ShowFriendQuizActivity.this, MyPageActivity.class);
                intentMyPage.putExtra("id", id);
                startActivity(intentMyPage);
            }
        });

        quizListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                    QuizOXShortwordTypeInfo quiz;
                    if(isFriendQuiz) quiz = myFriendOXQuizListR.get(position);
                    else quiz = likeOXQuizListR.get(position);

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
                        QuizChoiceTypeInfo quiz;
                        if(isFriendQuiz) quiz = myFriendChoiceQuizListR.get(position);
                        else quiz = likeChoiceQuizListR.get(position);

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
                        QuizOXShortwordTypeInfo quiz;
                        if(isFriendQuiz) quiz = myFriendShortQuizListR.get(position);
                        else quiz = likeShortQuizListR.get(position);

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
                            boolean isSolved = false;
                            for(String solved : solvedQuizList) {
                                String key = snapshot.getKey();
                                if(solved.equals(key)) {
                                    isSolved = true;
                                    break;
                                }
                            }
                            if(!isSolved) {
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

                quizListView.setAdapter(myFriendQuizListAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {            }
        });
    }

    private void getFirebaseDatabaseLikeQuiz() {
        mPostReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bookName = dataSnapshot.child("script_list/" + scriptnm + "/book_name").getValue().toString();

                likeOXQuizList.clear();
                likeOXQuizListR.clear();
                likeChoiceQuizList.clear();
                likeChoiceQuizListR.clear();
                likeShortQuizList.clear();
                likeShortQuizListR.clear();
                for(DataSnapshot snapshot : dataSnapshot.child("quiz_list/" + scriptnm).getChildren()) {
                    boolean isSolved = false;
                    for(String solved : solvedQuizList) {
                        String key = snapshot.getKey();
                        if(solved.equals(key)) {
                            isSolved = true;
                            break;
                        }
                    }

                    if(!isSolved) {
                        String type = snapshot.child("type").getValue().toString();
                        if (type.equals("1")) {
                            QuizOXShortwordTypeInfo quiz = snapshot.getValue(QuizOXShortwordTypeInfo.class);
                            likeOXQuizList.add(quiz);
                        } else if (type.equals("2")) {
                            QuizChoiceTypeInfo quiz = snapshot.getValue(QuizChoiceTypeInfo.class);
                            likeChoiceQuizList.add(quiz);
                        } else if (type.equals("3")) {
                            QuizOXShortwordTypeInfo quiz = snapshot.getValue(QuizOXShortwordTypeInfo.class);
                            likeShortQuizList.add(quiz);
                        }
                    }
                }

                //TODO 퀴즈 순서 랜덤으로 섞기, 좋아요 개수 순으로 상위 몇개 뽑기

                for(int i=0; i<likeOXQuizList.size(); i++) {
                    float star = Float.parseFloat(likeOXQuizList.get(i).star);
                    if(star < 1.5)
                        likeQuizListAdapter.addItem(likeOXQuizList.get(i).like, ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), bookName, scriptnm, likeOXQuizList.get(i).question);
                    else if (star >= 1.5 && star < 2.5)
                        likeQuizListAdapter.addItem(likeOXQuizList.get(i).like, ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), bookName, scriptnm, likeOXQuizList.get(i).question);
                    else if (star >= 2.5 && star < 3.5)
                        likeQuizListAdapter.addItem(likeOXQuizList.get(i).like, ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), bookName, scriptnm, likeOXQuizList.get(i).question);
                    else if (star >= 3.5 && star < 4.5)
                        likeQuizListAdapter.addItem(likeOXQuizList.get(i).like, ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), bookName, scriptnm, likeOXQuizList.get(i).question);
                    else
                        likeQuizListAdapter.addItem(likeOXQuizList.get(i).like, ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), bookName, scriptnm, likeOXQuizList.get(i).question);

                    likeOXQuizListR.add(likeOXQuizList.get(i));
                }

                for(int i=0; i<likeChoiceQuizList.size(); i++) {
                    float star = Float.parseFloat(likeChoiceQuizList.get(i).star);
                    if(star < 1.5)
                        likeQuizListAdapter.addItem(likeChoiceQuizList.get(i).like, ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), bookName, scriptnm, likeChoiceQuizList.get(i).question);
                    else if (star >= 1.5 && star < 2.5)
                        likeQuizListAdapter.addItem(likeChoiceQuizList.get(i).like, ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), bookName, scriptnm, likeChoiceQuizList.get(i).question);
                    else if (star >= 2.5 && star < 3.5)
                        likeQuizListAdapter.addItem(likeChoiceQuizList.get(i).like, ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), bookName, scriptnm, likeChoiceQuizList.get(i).question);
                    else if (star >= 3.5 && star < 4.5)
                        likeQuizListAdapter.addItem(likeChoiceQuizList.get(i).like, ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), bookName, scriptnm, likeChoiceQuizList.get(i).question);
                    else
                        likeQuizListAdapter.addItem(likeChoiceQuizList.get(i).like, ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), bookName, scriptnm, likeChoiceQuizList.get(i).question);

                    likeChoiceQuizListR.add(likeChoiceQuizList.get(i));
                }

                for(int i=0; i<likeShortQuizList.size(); i++) {
                    float star = Float.parseFloat(likeShortQuizList.get(i).star);
                    if(star < 1.5)
                        likeQuizListAdapter.addItem(likeShortQuizList.get(i).like, ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), bookName, scriptnm, likeShortQuizList.get(i).question);
                    else if (star >= 1.5 && star < 2.5)
                        likeQuizListAdapter.addItem(likeShortQuizList.get(i).like, ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), bookName, scriptnm, likeShortQuizList.get(i).question);
                    else if (star >= 2.5 && star < 3.5)
                        likeQuizListAdapter.addItem(likeShortQuizList.get(i).like, ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), bookName, scriptnm, likeShortQuizList.get(i).question);
                    else if (star >= 3.5 && star < 4.5)
                        likeQuizListAdapter.addItem(likeShortQuizList.get(i).like, ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), bookName, scriptnm, likeShortQuizList.get(i).question);
                    else
                        likeQuizListAdapter.addItem(likeShortQuizList.get(i).like, ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), bookName, scriptnm, likeShortQuizList.get(i).question);

                    likeShortQuizListR.add(likeShortQuizList.get(i));
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
