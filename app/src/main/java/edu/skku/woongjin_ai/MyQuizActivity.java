package edu.skku.woongjin_ai;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.media.CamcorderProfile.get;

public class MyQuizActivity extends AppCompatActivity implements SeeOXQuizFragment.OnFragmentInteractionListener, SeeChoiceQuizFragment.OnFragmentInteractionListener, SeeShortQuizFragment.OnFragmentInteractionListener, ShowScriptFragment.OnFragmentInteractionListener, ShowWhoLikedFragment.OnFragmentInteractionListener{

    public DatabaseReference mPostReference, uPostReference;
    Intent intent, intentHome, intentUpdate;
    String id;
    TextView instruction;
    ListView quizlist, likefriends;
    ArrayList<QuizChoiceTypeInfo> myChoiceList;
    ArrayList<QuizOXShortwordTypeInfo> myOXList, myShortList;
    MyFriendQuizListAdapter myQuizListAdapter;
    int flag=0;
    SeeOXQuizFragment OXFragment;
    SeeChoiceQuizFragment ChoiceFragment;
    SeeShortQuizFragment ShortFragment;
    ShowScriptFragment showScriptFragment;
    ShowWhoLikedFragment showWhoLikedFragment;
    ArrayList<UserInfo> Uinfos;
    int cntOX, cntChoice, cntShort;
    String showQkey;
    ShowFriendListAdapter showFriendListAdapter;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seequestion_likeormine);

        ImageView imageHome = (ImageView) findViewById(R.id.home);
        quizlist = (ListView) findViewById(R.id.quizlist);
        likefriends=(ListView) findViewById(R.id.wholiked_list);

        instruction=(TextView)findViewById(R.id.instruction);


        Uinfos=new ArrayList<UserInfo>();

        intent = getIntent();
        id = intent.getStringExtra("id");

        OXFragment=new SeeOXQuizFragment();
        ChoiceFragment=new SeeChoiceQuizFragment();
        ShortFragment=new SeeShortQuizFragment();

        showScriptFragment=new ShowScriptFragment();
        showWhoLikedFragment=new ShowWhoLikedFragment();

        showFriendListAdapter=new ShowFriendListAdapter();

        mPostReference = FirebaseDatabase.getInstance().getReference();


        myQuizListAdapter = new MyFriendQuizListAdapter();

        myChoiceList=new ArrayList<QuizChoiceTypeInfo>();
        myOXList=new ArrayList<QuizOXShortwordTypeInfo>();
        myShortList=new ArrayList<QuizOXShortwordTypeInfo>();

        intentUpdate = new Intent(MyQuizActivity.this, MyQuizActivity.class);
        intentUpdate.putExtra("id", id);
        ////

        getFirebaseDatabaseMyQuizList();

        imageHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentHome = new Intent(MyQuizActivity.this, MainActivity.class);
                intentHome.putExtra("id", id);
                startActivity(intentHome);
                finish();
            }
        });

        quizlist.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long i) {
                if(flag == 1) {
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.remove(OXFragment);
                    fragmentTransaction.commit();
                    OXFragment = new SeeOXQuizFragment();
                } else if(flag == 2) {
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.remove(ChoiceFragment);
                    fragmentTransaction.commit();
                    ChoiceFragment = new SeeChoiceQuizFragment();
                } else if(flag == 3) {
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.remove(ShortFragment);
                    fragmentTransaction.commit();
                    ShortFragment = new SeeShortQuizFragment();
                }

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                if(position < cntOX) {
                    flag = 1;
                    QuizOXShortwordTypeInfo quiz = myOXList.get(position);

                    transaction.replace(R.id.seequiz_fragment, OXFragment);
                    Bundle bundle = new Bundle(12);
                    bundle.putString("id", id);
                    bundle.putString("mine_or_like", "0");
                    bundle.putString("scriptnm", quiz.scriptnm);
                    bundle.putString("question", quiz.question);
                    bundle.putString("answer", quiz.answer);
                    bundle.putString("uid", quiz.uid);
                    bundle.putString("star", quiz.star);
                    bundle.putString("like", quiz.like);
                    bundle.putString("desc", quiz.desc);
                    bundle.putString("key", quiz.key);
                    bundle.putInt("cnt", quiz.cnt);
                    bundle.putInt("type", 1);
                    OXFragment.setArguments(bundle);
                    transaction.commit();
                } else {
                    position -= cntOX;
                    if(position < cntChoice) {
                        flag = 2;
                        QuizChoiceTypeInfo quiz = myChoiceList.get(position);

                        transaction.replace(R.id.seequiz_fragment, ChoiceFragment);
                        Bundle bundle = new Bundle(16);
                        bundle.putString("id", id);
                        bundle.putString("mine_or_like", "0");
                        bundle.putString("scriptnm", quiz.scriptnm);
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
                        bundle.putInt("type", 2);
                        ChoiceFragment.setArguments(bundle);
                        transaction.commit();
                    } else {
                        position -= cntChoice;
                        flag = 3;
                        QuizOXShortwordTypeInfo quiz = myShortList.get(position);

                        transaction.replace(R.id.seequiz_fragment, ShortFragment);
                        Bundle bundle = new Bundle(12);
                        bundle.putString("id", id);
                        bundle.putString("mine_or_like", "0");
                        bundle.putString("scriptnm", quiz.scriptnm);
                        bundle.putString("question", quiz.question);
                        bundle.putString("answer", quiz.answer);
                        bundle.putString("uid", quiz.uid);
                        bundle.putString("star", quiz.star);
                        bundle.putString("like", quiz.like);
                        bundle.putString("desc", quiz.desc);
                        bundle.putString("key", quiz.key);
                        bundle.putInt("cnt", quiz.cnt);
                        bundle.putInt("type", 3);
                        ShortFragment.setArguments(bundle);
                        transaction.commit();
                    }
                }
            }
        });

    }

    public void onFragmentChange(int index, String Qkey, int originalType){
        if(index==0){
            showQkey=Qkey;
            Bundle bundle=new Bundle(2);
            bundle.putInt("type", originalType);
            bundle.putString("key", showQkey);
            showWhoLikedFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.seequiz_fragment, showWhoLikedFragment).commit();
        }else if(index==1){
            getSupportFragmentManager().beginTransaction().replace(R.id.seequiz_fragment, ShortFragment).commit();
        }else if(index==2){
            getSupportFragmentManager().beginTransaction().replace(R.id.seequiz_fragment, ChoiceFragment).commit();
        }else if(index==3){
            getSupportFragmentManager().beginTransaction().replace(R.id.seequiz_fragment, OXFragment).commit();
        }
    }

    public ShowFriendListAdapter getFirebaseDatabaseUserList(String QKEY) {
        ShowFriendListAdapter showFriendListAdapterL=new ShowFriendListAdapter();
        mPostReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Uinfos.clear();
                DataSnapshot snapshot=dataSnapshot.child("user_list");
                for (DataSnapshot snapshot1 : snapshot.getChildren()) { //uid 키값
                    DataSnapshot snapshot2=snapshot1.child("my_script_list");
                    for(DataSnapshot snapshot3:snapshot2.getChildren()){ //스크립트 각각
                        DataSnapshot snapshot5=snapshot3.child("liked_list");
                        for(DataSnapshot snapshot4:snapshot5.getChildren()){
                            String key=snapshot4.getKey();
                            if(key.equals(QKEY)){
                                UserInfo friend = snapshot1.getValue(UserInfo.class);
                                Uinfos.add(friend);
                                showFriendListAdapterL.addItem(friend.profile, friend.nickname + "[" + friend.name + "]", friend.grade, friend.school);
                            }
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {            }
        });
        return showFriendListAdapterL;
    }

    private void getFirebaseDatabaseMyQuizList() {
        mPostReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myOXList.clear();
                myChoiceList.clear();
                myShortList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String key = snapshot.getKey();
                    if (key.equals("quiz_list")) {
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) { //script
                            String scriptTitle = snapshot1.getKey();
                            for (DataSnapshot snapshot2 : snapshot1.getChildren()) { // inside-script
                                String question_key = snapshot2.getKey();
                                if (question_key.endsWith(id)) {
                                    String type = snapshot2.child("type").getValue().toString();
                                    if (type.equals("1")) {
                                        QuizOXShortwordTypeInfo quiz = snapshot2.getValue(QuizOXShortwordTypeInfo.class);
                                        quiz.scriptnm=scriptTitle;
                                        myOXList.add(quiz);
                                        //ox type
                                        //QuizOXShortwordTypeInfo get = snapshot2.getValue(QuizOXShortwordTypeInfo.class);
//                                        String quiz=snapshot2.child("question").getValue().toString();
//                                        String myQuiz = "[" + scriptTitle + "] Q." + quiz;
//                                        myQuizList.add(myQuiz);
//                                        myQuizListAdapter.addItem(myQuiz);
                                    } else if (type.equals("2")) {
                                        QuizChoiceTypeInfo quiz = snapshot2.getValue(QuizChoiceTypeInfo.class);
                                        quiz.scriptnm=scriptTitle;
                                        myChoiceList.add(quiz);
                                        //choice
//                                        QuizChoiceTypeInfo get = snapshot2.getValue(QuizChoiceTypeInfo.class);
//                                        String myQuiz = "[" + scriptTitle + "] Q." + get.question;
//                                        myQuizList.add(myQuiz);
//                                        myQuizListAdapter.addItem(myQuiz);
                                    } else {
                                        QuizOXShortwordTypeInfo quiz = snapshot2.getValue(QuizOXShortwordTypeInfo.class);
                                        quiz.scriptnm=scriptTitle;
                                        myShortList.add(quiz);
                                        //shortwrd
//                                        QuizOXShortwordTypeInfo get = snapshot2.getValue(QuizOXShortwordTypeInfo.class);
//                                        String myQuiz = "[" + scriptTitle + "] Q." + get.question;
//                                        myQuizList.add(myQuiz);
//                                        myQuizListAdapter.addItem(myQuiz);
                                    }
                                    //

                                }
                            }
                        }

                        cntOX = myOXList.size();
                        cntChoice = myChoiceList.size();
                        cntShort = myShortList.size();

                        for(int i=0; i<myOXList.size(); i++)
                            myQuizListAdapter.addItem(myOXList.get(i).question, myOXList.get(i).uid, ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_bigthumb), myOXList.get(i).like);
                        for(int i=0; i<myChoiceList.size(); i++)
                            myQuizListAdapter.addItem(myChoiceList.get(i).question, myChoiceList.get(i).uid, ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_bigthumb), myChoiceList.get(i).like);
                        for(int i=0; i<myShortList.size(); i++)
                            myQuizListAdapter.addItem(myShortList.get(i).question, myShortList.get(i).uid, ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_bigthumb), myShortList.get(i).like);

                        quizlist.setAdapter(myQuizListAdapter);
                    }
                    //final ValueEventListener quiz_list = mPostReference.child("quiz_list").addValueEventListener(postListener);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    /*
    private void getFirebaseDatabaseUserInfo() {
        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Uinfos.clear();
                DataSnapshot snapshot=dataSnapshot.child("user_list");
                for (DataSnapshot snapshot1 : snapshot.getChildren()) { //uid 키값
                    DataSnapshot snapshot2=snapshot1.child("my_script_list");
                    for(DataSnapshot snapshot3:snapshot2.getChildren()){ //스크립트 각각
                        DataSnapshot snapshot5=snapshot3.child("liked_list");
                        for(DataSnapshot snapshot4:snapshot5.getChildren()){
                            String qkey=snapshot4.getKey();
                            if(qkey.equals(key)){
                                UserInfo friend = snapshot1.getValue(UserInfo.class);
                                Uinfos.add(friend);
                                showFriendListAdapter.addItem(getResources().getDrawable(R.drawable.kakao_default_profile_image), friend.nickname + "[" + friend.name + "]", friend.grade, friend.school);
                            }
                        }
                    }
                }
                likedfriends.setAdapter(showFriendListAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
    }
    */

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}