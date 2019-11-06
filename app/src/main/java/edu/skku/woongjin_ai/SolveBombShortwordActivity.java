package edu.skku.woongjin_ai;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SolveBombShortwordActivity extends AppCompatActivity implements ShowScriptFragment.OnFragmentInteractionListener {
    DatabaseReference mPostReference;
    Intent intent;
    String timestamp_key, id_key, nickname_key, user1_key, user2_key, roomname_key, script_key, state_key, question_key, answer_key;
    char bomb_cnt;
    TextView timer, gamers, question;
    EditText answer_edit;
    ImageButton imageButtonScript;
    Button imageButtonCheck;
    String user_answer;
    Fragment showScriptFragment;

    int second = 120;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solvebombshortword);

        timer = (TextView) findViewById(R.id.timer);
        gamers = (TextView) findViewById(R.id.gamers);
        question = (TextView) findViewById(R.id.question);
        answer_edit = (EditText) findViewById(R.id.answer_edit);
        imageButtonScript = (ImageButton) findViewById(R.id.script);
        imageButtonCheck = (Button)findViewById(R.id.check);

        mHandler.sendEmptyMessage(0);

        intent = getIntent();
        timestamp_key = intent.getStringExtra("timestamp");
        id_key = intent.getStringExtra("id");
        nickname_key = intent.getStringExtra("nickname");
        script_key = intent.getStringExtra("scriptnm");
        user1_key = intent.getStringExtra("user1");
        user2_key = intent.getStringExtra("user2");
        roomname_key = intent.getStringExtra("roomname");
        state_key = intent.getStringExtra("state");
        question_key = intent.getStringExtra("question");
        answer_key = intent.getStringExtra("answer");

        bomb_cnt = state_key.charAt(6);
        mPostReference = FirebaseDatabase.getInstance().getReference().child("gameroom_list").child(timestamp_key).child("quiz_list");

        gamers.setText(user1_key + " vs " + user2_key);
        question.setText(question_key);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        imageButtonCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ValueEventListener check = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            String quiznum = postSnapshot.getKey();
                            if (quiznum.contains("quiz" + bomb_cnt)) {
                                mPostReference.child(quiznum).child("state").setValue(nickname_key);
                                break;
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) { }
                };
                mPostReference.addValueEventListener(check);

                user_answer = answer_edit.getText().toString();
                if (user_answer.equals("")) {
                    Toast.makeText(SolveBombShortwordActivity.this, "정답을 입력하세요.", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (user_answer.equals(answer_key)) {
                        if (bomb_cnt == '6') {
                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                            EndBombFragment fragment = new EndBombFragment();
                            Bundle bundle = new Bundle(4);
                            bundle.putString("id", id_key);
                            bundle.putString("nickname", nickname_key);
                            bundle.putString("user1", user1_key);
                            bundle.putString("user2", user2_key);
                            fragment.setArguments(bundle);
                            transaction.replace(R.id.contents, fragment);
                            transaction.commit();
                        }
                        else if (bomb_cnt != '6') {
                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                            CorrectBombFragment fragment = new CorrectBombFragment();
                            Bundle bundle = new Bundle(8);
                            bundle.putString("timestamp", timestamp_key);
                            bundle.putString("id", id_key);
                            bundle.putString("nickname", nickname_key);
                            bundle.putString("user1", user1_key);
                            bundle.putString("user2", user2_key);
                            bundle.putString("roomname", roomname_key);
                            bundle.putString("scriptnm", script_key);
                            bundle.putString("state", state_key);
                            fragment.setArguments(bundle);
                            transaction.replace(R.id.contents, fragment);
                            transaction.commit();
                        }
                    }
                    else if (!user_answer.equals(answer_key)) {
                        Toast.makeText(SolveBombShortwordActivity.this, "다시 시도해보세요.", Toast.LENGTH_SHORT).show();
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        WrongBombFragment fragment = new WrongBombFragment();
                        Bundle bundle = new Bundle(4);
                        bundle.putString("id", id_key);
                        bundle.putString("nickname", nickname_key);
                        bundle.putString("user1", user1_key);
                        bundle.putString("user2", user2_key);
                        fragment.setArguments(bundle);
                        transaction.replace(R.id.contents, fragment);
                        transaction.commit();
                    }
                }
            }
        });

        imageButtonScript.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showScriptFragment = new ShowScriptFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.contents, showScriptFragment);
                Bundle bundle = new Bundle(2);
                bundle.putString("scriptnm", script_key);
                bundle.putString("type", "ox");
                showScriptFragment.setArguments(bundle);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            second--;
            timer.setText(second);
            mHandler.sendEmptyMessageDelayed(0,1000);

            if (second == 0){
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                WrongBombFragment fragment = new WrongBombFragment();
                Bundle bundle = new Bundle(4);
                bundle.putString("id", id_key);
                bundle.putString("nickname", nickname_key);
                bundle.putString("user1", user1_key);
                bundle.putString("user2", user2_key);
                fragment.setArguments(bundle);
                transaction.replace(R.id.contents, fragment);
                transaction.commit();
            }
        }
    };

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
    
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
