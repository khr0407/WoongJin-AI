package edu.skku.woongjin_ai;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class QuizbucketOXNext extends AppCompatActivity
        implements ShowScriptFragment.OnFragmentInteractionListener {

    DatabaseReference mPostReference;
    ImageView imageO, imageX;
    ImageView imageHome;
    EditText editQuiz, editDiff;
    Intent intent, intentHome, intentGameList;
    String id_key, scriptnm, backgroundID;
    String timestamp_key, nickname_key, user1_key, user2_key,bucketcnt_key, roomname_key, state_key;
    TextView title;
    char quizcnt;
    String quiz = "", ans = "", diff = "";
    int flagAO = 0, flagAX = 0;
    ImageButton checkButton, scriptButton;
    Fragment showScriptFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizbucketoxnext);

        intent = getIntent();
        id_key = intent.getStringExtra("id");
        scriptnm = intent.getStringExtra("scriptnm");
        backgroundID = intent.getStringExtra("background");
        timestamp_key = intent.getStringExtra("timestamp");
        nickname_key = intent.getStringExtra("nickname");
        user1_key = intent.getStringExtra("user1");
        user2_key = intent.getStringExtra("user2");
        bucketcnt_key = intent.getStringExtra("bucketcnt");
        state_key = intent.getStringExtra("state");
        roomname_key = intent.getStringExtra("roomname");

        imageHome = (ImageView) findViewById(R.id.home);
        imageO = (ImageView) findViewById(R.id.o);
        imageX = (ImageView) findViewById(R.id.x);
        editQuiz = (EditText) findViewById(R.id.quiz);
        editDiff = (EditText) findViewById(R.id.diff);
        title = (TextView) findViewById(R.id.title);
        checkButton = (ImageButton) findViewById(R.id.check);
        scriptButton = (ImageButton) findViewById(R.id.script);

        title.setText("지문 제목: " + scriptnm);
        quizcnt = state_key.charAt(6);

        int count = quizcnt - '0';

        mPostReference = FirebaseDatabase.getInstance().getReference().child("chatroom_bucket_list");

        scriptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showScriptFragment = new ShowScriptFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.contents2, showScriptFragment);
                Bundle bundle = new Bundle(2);
                bundle.putString("scriptnm", scriptnm);
                bundle.putString("type", "makebombox");
                showScriptFragment.setArguments(bundle);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });


        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quiz = editQuiz.getText().toString();
                diff = editDiff.getText().toString();
                if (quiz.length() == 0 || (flagAO == 0 && flagAX == 0)) {
                    Toast.makeText(QuizbucketOXNext.this, "빈 칸을 채워주세요", Toast.LENGTH_SHORT).show();
                } else if (diff.length() == 0 || (flagAO == 0 && flagAX == 0)) {
                    Toast.makeText(QuizbucketOXNext.this, "난이도를 설정해주세요!", Toast.LENGTH_SHORT).show();
                } else {
                    if(count == 0) {
                        postFirebaseDatabaseQuizOX();
                        Toast.makeText(QuizbucketOXNext.this, "출제 완료!", Toast.LENGTH_SHORT).show();
                        intentGameList = new Intent(QuizbucketOXNext.this, QuizbucketMain.class);
                        intentGameList.putExtra("id", id_key);
                        intentGameList.putExtra("nickname", nickname_key);
                        startActivity(intentGameList);
                        finish();
                    }
                    else if(count == 1) { // user2가 user3한테
                        Toast.makeText(QuizbucketOXNext.this, "출제 완료!", Toast.LENGTH_SHORT).show();
                        Map<String, Object> childUpdates = new HashMap<>();
                        Map<String, Object> postValues = null;

                        Firebase_QBOXShortword post = new Firebase_QBOXShortword(quiz, diff, ans, "ox","none", nickname_key);
                        postValues = post.toMap();
                        int temp_cnt = count - '0' + 1;
                        childUpdates.put("quiz"+temp_cnt, postValues);
                        mPostReference.child(timestamp_key).child("quiz_list").updateChildren(childUpdates);
                        mPostReference.child(timestamp_key).child("state").setValue("gaming"+temp_cnt);
                        mPostReference.child(timestamp_key).child("bucketcnt").setValue("bucket"+temp_cnt);
                        editQuiz.setText("");
                        editDiff.setText("");

                        intentGameList = new Intent(QuizbucketOXNext.this, QuizbucketMain.class);
                        intentGameList.putExtra("id", id_key);
                        intentGameList.putExtra("nickname", nickname_key);
                        startActivity(intentGameList);
                        finish();
                    }
                    else if(count == 2) { // user3이 user4한테
                        Toast.makeText(QuizbucketOXNext.this, "출제 완료!", Toast.LENGTH_SHORT).show();
                        Map<String, Object> childUpdates = new HashMap<>();
                        Map<String, Object> postValues = null;

                        Firebase_QBOXShortword post = new Firebase_QBOXShortword(quiz, diff, ans, "ox","none", nickname_key);
                        postValues = post.toMap();
                        int temp_cnt = count - '0' + 1;
                        childUpdates.put("quiz"+temp_cnt, postValues);
                        mPostReference.child(timestamp_key).child("quiz_list").updateChildren(childUpdates);
                        mPostReference.child(timestamp_key).child("state").setValue("gaming"+temp_cnt);
                        mPostReference.child(timestamp_key).child("bucketcnt").setValue("bucket"+temp_cnt);
                        editQuiz.setText("");
                        editDiff.setText("");

                        intentGameList = new Intent(QuizbucketOXNext.this, QuizbucketMain.class);
                        intentGameList.putExtra("id", id_key);
                        intentGameList.putExtra("nickname", nickname_key);
                        startActivity(intentGameList);
                        finish();
                    }
                    else if(count == 3) { // user4가 user5한테
                        Toast.makeText(QuizbucketOXNext.this, "출제 완료!", Toast.LENGTH_SHORT).show();
                        Map<String, Object> childUpdates = new HashMap<>();
                        Map<String, Object> postValues = null;

                        Firebase_QBOXShortword post = new Firebase_QBOXShortword(quiz, diff, ans, "ox","none", nickname_key);
                        postValues = post.toMap();
                        int temp_cnt = count - '0' + 1;
                        childUpdates.put("quiz"+temp_cnt, postValues);
                        mPostReference.child(timestamp_key).child("quiz_list").updateChildren(childUpdates);
                        mPostReference.child(timestamp_key).child("state").setValue("gaming"+temp_cnt);
                        mPostReference.child(timestamp_key).child("bucketcnt").setValue("bucket"+temp_cnt);
                        editQuiz.setText("");
                        editDiff.setText("");

                        intentGameList = new Intent(QuizbucketOXNext.this, QuizbucketMain.class);
                        intentGameList.putExtra("id", id_key);
                        intentGameList.putExtra("nickname", nickname_key);
                        startActivity(intentGameList);
                        finish();
                    }
                }
            }
        });

        imageHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentHome = new Intent(QuizbucketOXNext.this, MainActivity.class);
                intentHome.putExtra("id", id_key);
                startActivity(intentHome);
                finish();
            }
        });

        imageO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flagAO == 0) {
                    if (flagAX == 1) {
                        imageX.setImageResource(R.drawable.x_white);
                        flagAX = 0;
                    }
                    ans = "o";
                    imageO.setImageResource(R.drawable.o_orange);
                    flagAO = 1;
                } else {
                    ans = "";
                    imageO.setImageResource(R.drawable.o_white);
                    flagAO = 0;
                }
            }
        });

        imageX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flagAX == 0) {
                    if (flagAO == 1) {
                        imageO.setImageResource(R.drawable.o_white);
                        flagAO = 0;
                    }
                    ans = "x";
                    imageX.setImageResource(R.drawable.x_orange);
                    flagAX = 1;
                } else {
                    ans = "";
                    imageX.setImageResource(R.drawable.x_white);
                    flagAX = 0;
                }
            }
        });
    }

    private void postFirebaseDatabaseQuizOX() {
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        Firebase_QBOXShortword post = new Firebase_QBOXShortword(quiz, diff, ans, "ox","none", nickname_key);
        postValues = post.toMap();
        int temp_cnt = quizcnt - '0' + 1;
        childUpdates.put("quiz"+temp_cnt, postValues);
        mPostReference.child(timestamp_key).child("quiz_list").updateChildren(childUpdates);
        mPostReference.child(timestamp_key).child("state").setValue("gaming"+temp_cnt);
        mPostReference.child(timestamp_key).child("bucketcnt").setValue("bucket"+temp_cnt);
        editQuiz.setText("");
        editDiff.setText("");
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}