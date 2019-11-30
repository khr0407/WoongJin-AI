package edu.skku.woongjin_ai;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/*
from SelectBookActivity
지문 공부하기 - 소리내어 읽기(미구현) / 표시하며 읽기
 */

public class ReadScriptActivity extends AppCompatActivity
        implements SelectStudyTypeFragment.OnFragmentInteractionListener, NewHoonjangFragment.OnFragmentInteractionListener {
    public DatabaseReference mPostReference;
    Intent intent, intentHome, intentMakeQuiz;
    String id, scriptnm, backgroundID, script, studyType = "", nickname, thisWeek;
    TextView textview_title, textview_script_1, textview_script_2;
    ImageButton goHome;
    TextView goMakeQuiz;
<<<<<<< HEAD
=======
//    FirebaseStorage storage;
//    private StorageReference storageReference, dataReference;
>>>>>>> a1edf660fa4291434aa7c8c49e9a8c054e406cf1
    Fragment selectStudyTypeFragment;
    NewHoonjangFragment hoonjangFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readscript);

        intent = getIntent();
        id= intent.getStringExtra("id");
        scriptnm = intent.getStringExtra("scriptnm");
        backgroundID = intent.getStringExtra("background");
        nickname = intent.getStringExtra("nickname");
        thisWeek = intent.getStringExtra("thisWeek");

        selectStudyTypeFragment = new SelectStudyTypeFragment();

        textview_title = (TextView) findViewById(R.id.textview_title);
        textview_script_1 = (TextView) findViewById(R.id.textview_script_1);
        textview_script_2 = (TextView) findViewById(R.id.textview_script_2);
        goHome = (ImageButton) findViewById(R.id.home);
        goMakeQuiz = (TextView) findViewById(R.id.makeQuiz);

        textview_title.setText(scriptnm);
        textview_script_1.setMovementMethod(new ScrollingMovementMethod());
        textview_script_2.setMovementMethod(new ScrollingMovementMethod());

        mPostReference = FirebaseDatabase.getInstance().getReference();

        // 데이터베이스 단어장에 임시 단어 저장
        mPostReference.child("user_list/" + id + "/my_script_list/" + scriptnm + "/time").setValue("2m");
        mPostReference.child("user_list/" + id + "/my_script_list/" + scriptnm + "/word_list/test1/ex").setValue("test1Ex");
        mPostReference.child("user_list/" + id + "/my_script_list/" + scriptnm + "/word_list/test1/meaning").setValue("test1Meaning");
        mPostReference.child("user_list/" + id + "/my_script_list/" + scriptnm + "/word_list/test2/ex").setValue("test2Ex");
        mPostReference.child("user_list/" + id + "/my_script_list/" + scriptnm + "/word_list/test2/meaning").setValue("test2Meaning");
        mPostReference.child("user_list/" + id + "/my_script_list/" + scriptnm + "/word_list/test3/ex").setValue("test3Ex");
        mPostReference.child("user_list/" + id + "/my_script_list/" + scriptnm + "/word_list/test3/meaning").setValue("test3Meaning");

<<<<<<< HEAD
=======
        // 배경 이미지
//        storage = FirebaseStorage.getInstance();
//        storageReference = storage.getInstance().getReference();
//        dataReference = storageReference.child("/scripts_background/" + backgroundID);
//        dataReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                Picasso.with(ReadScriptActivity.this)
//                        .load(uri)
//                        .placeholder(R.drawable.bot)
//                        .error(R.drawable.btn_x)
//                        .into(backgroundImage);
//                backgroundImage.setAlpha(0.5f);
//            }
//        });

        // 데이터베이스에서 지문 가져오기
>>>>>>> a1edf660fa4291434aa7c8c49e9a8c054e406cf1
        mPostReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                script = dataSnapshot.child("script_list/" + scriptnm + "/text").getValue().toString();
                String[] array=script.split("###");
                textview_script_1.setText(array[0]);
                textview_script_2.setText(array[1]);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {            }
        });

<<<<<<< HEAD
=======
        // 소리내어 읽기 / 표시하며 읽기 선택
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.contentReadScript, selectStudyTypeFragment);
//        transaction.commit();
>>>>>>> a1edf660fa4291434aa7c8c49e9a8c054e406cf1

        // 메인페이지 버튼 이벤트
        goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentHome = new Intent(ReadScriptActivity.this, MainActivity.class);
                intentHome.putExtra("id", id);
                startActivity(intentHome);
            }
        });

        // 질문 만들기 버튼 이벤트
        goMakeQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFirebaseUserCoinInfo();
                intentMakeQuiz = new Intent(ReadScriptActivity.this, SelectTypeActivity.class);
                intentMakeQuiz.putExtra("id", id);
                intentMakeQuiz.putExtra("scriptnm", scriptnm);
                intentMakeQuiz.putExtra("background", backgroundID);
                intentMakeQuiz.putExtra("nickname", nickname);
                intentMakeQuiz.putExtra("thisWeek", thisWeek);
                startActivity(intentMakeQuiz);
            }
        });

        // 지문 단어 공부하기 버튼 이벤트
//        goStudyWord.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            Intent intentStudyWord = new Intent(ReadScriptActivity.this, WordListActivity.class);
//            intentStudyWord.putExtra("scriptnm",scriptnm);
//            intentStudyWord.putExtra("id", id);
//            intentStudyWord.putExtra("background", backgroundID);
//            startActivity(intentStudyWord);
//        }
//    });
    }

    // 코인 수여, 데이터베이스에 저장
    private void uploadFirebaseUserCoinInfo(){
        mPostReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long now = System.currentTimeMillis();
                Date date = new Date(now);
                String today = new SimpleDateFormat("yyMMddHHmm").format(date);
                mPostReference.child("user_list/" + id + "/my_coin_list/" + today + "/get").setValue("20");
                mPostReference.child("user_list/" + id + "/my_coin_list/" + today + "/why").setValue("지문 [" + scriptnm + "]을(를) 읽었어요.");

                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    String key=dataSnapshot1.getKey();
                    if(key.equals("user_list")){
                        String mycoin=dataSnapshot1.child(id).child("coin").getValue().toString();
                        int coin = Integer.parseInt(mycoin) + 10;
                        String coin_convert = Integer.toString(coin);
                        mPostReference.child("user_list/" + id).child("coin").setValue(coin_convert);
                        break;
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void onStudyTypeInfoSet(String type) {
        studyType = type;

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}