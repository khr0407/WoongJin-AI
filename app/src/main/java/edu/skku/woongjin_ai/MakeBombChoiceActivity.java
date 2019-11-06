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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class MakeBombChoiceActivity extends AppCompatActivity
        implements ShowScriptFragment.OnFragmentInteractionListener {

    DatabaseReference mPostReference;
    ImageView imageScript, imageCheck, imageHome;
    EditText editQuiz, editAns, editAns1, editAns2, editAns3, editAns4;
    Intent intent, intentHome, intentGameList;
    String id_key, scriptnm, backgroundID;
    String timestamp_key, nickname_key, user1_key, user2_key, roomname_key, state_key;
    char bomb_cnt;
    String quiz = "", ans = "", ans1 = "", ans2 = "", ans3 = "", ans4 = "";
    int flagA1 = 0, flagA2 = 0, flagA3 = 0, flagA4 = 0;
    ImageButton checkButton, scriptButton;
    Fragment showScriptFragment;
    TextView title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_makebombchoice);

        intent = getIntent();
        id_key = intent.getStringExtra("id");
        scriptnm = intent.getStringExtra("scriptnm");
        backgroundID = intent.getStringExtra("background");
        timestamp_key = intent.getStringExtra("timestamp");
        nickname_key = intent.getStringExtra("nickname");
        user1_key = intent.getStringExtra("user1");
        user2_key = intent.getStringExtra("user2");
        state_key = intent.getStringExtra("state");
        roomname_key = intent.getStringExtra("roomname");

        imageHome = (ImageView) findViewById(R.id.home);
        imageScript = (ImageView) findViewById(R.id.script);
        imageCheck = (ImageView) findViewById(R.id.check);
        editQuiz = (EditText) findViewById(R.id.quiz);
        editAns = (EditText) findViewById(R.id.ans);
        editAns1 = (EditText) findViewById(R.id.ans1);
        editAns2 = (EditText) findViewById(R.id.ans2);
        editAns3 = (EditText) findViewById(R.id.ans3);
        editAns4 = (EditText) findViewById(R.id.ans4);
        title = (TextView) findViewById(R.id.title);
        checkButton = (ImageButton) findViewById(R.id.check);
        scriptButton = (ImageButton) findViewById(R.id.script);

        title.setText("지문 제목: " + scriptnm);
        bomb_cnt = state_key.charAt(6);

        mPostReference = FirebaseDatabase.getInstance().getReference().child("gameroom_list");

        scriptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showScriptFragment = new ShowScriptFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.contentShowScriptChoice, showScriptFragment);
                Bundle bundle = new Bundle(2);
                bundle.putString("scriptnm", scriptnm);
                bundle.putString("type", "choice");
                showScriptFragment.setArguments(bundle);
                //transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quiz = editQuiz.getText().toString();
                HintWritingFragment hintWritingFragment1 = (HintWritingFragment) getSupportFragmentManager().findFragmentById(R.id.contentSelectHint);

                quiz = editQuiz.getText().toString();
                ans1 = editAns1.getText().toString();
                ans2 = editAns2.getText().toString();
                ans3 = editAns3.getText().toString();
                ans4 = editAns4.getText().toString();

                if(quiz.length() == 0 || ans.length() == 0 || ans1.length() == 0 || ans2.length() == 0 || ans3.length() == 0 || ans4.length() == 0) {
                    Toast.makeText(MakeBombChoiceActivity.this, "Fill all blanks", Toast.LENGTH_SHORT).show();
                }
                else {
                    postFirebaseDatabaseQuizChoice();
                    Toast.makeText(MakeBombChoiceActivity.this, "출제 완료!", Toast.LENGTH_SHORT).show();
                    intentGameList = new Intent(MakeBombChoiceActivity.this, GameListActivity.class);
                    startActivity(intentGameList);
                    finish();
                }
            }
        });

        imageHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentHome = new Intent(MakeBombChoiceActivity.this, MainActivity.class);
                intentHome.putExtra("id", id_key);
                startActivity(intentHome);
                finish();
            }
        });

        editAns1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagA1 == 0 ) {
                    if(flagA2==0 && flagA3==0 && flagA4==0 ){
                        editAns1.setBackgroundResource(R.drawable.ic_icons_selector_correct);
                        flagA1 = 1;
                        ans = editAns1.getText().toString();
                    }
                    else{
                        Toast.makeText(MakeBombChoiceActivity.this, "먼저 정답을 초기화하세요", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    editAns1.setBackgroundResource(R.drawable.ic_icons_selector_standard);
                    flagA1 = 0;
                    ans="";
                }
            }
        });
        editAns2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagA2 == 0) {
                    if( flagA1==0 && flagA3==0 && flagA4==0){
                        editAns2.setBackgroundResource(R.drawable.ic_icons_selector_correct);
                        flagA2 = 1;
                        ans = editAns2.getText().toString();
                    }
                    else{
                        Toast.makeText(MakeBombChoiceActivity.this, "먼저 정답을 초기화하세요", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    editAns2.setBackgroundResource(R.drawable.ic_icons_selector_standard);
                    flagA2 = 0;
                    ans="";
                }
            }
        });
        editAns3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagA3 == 0 ) {
                    if(flagA2==0 && flagA4==0 && flagA1==0){
                        editAns3.setBackgroundResource(R.drawable.ic_icons_selector_correct);
                        flagA3 = 1;
                        ans = editAns3.getText().toString();
                    }
                    else{
                        Toast.makeText(MakeBombChoiceActivity.this, "먼저 정답을 초기화하세요", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    editAns3.setBackgroundResource(R.drawable.ic_icons_selector_standard);
                    flagA3 = 0;
                    ans = "";
                }
            }
        });
        editAns4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagA4 == 0 ) {
                    if(flagA2==0 && flagA3==0 && flagA1==0){
                        editAns4.setBackgroundResource(R.drawable.ic_icons_selector_correct);
                        flagA4 = 1;
                        ans = editAns4.getText().toString();
                    }
                    else{
                        Toast.makeText(MakeBombChoiceActivity.this, "먼저 정답을 초기화하세요", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    editAns4.setBackgroundResource(R.drawable.ic_icons_selector_standard);
                    flagA4 = 0;
                    ans = "";
                }
            }
        });
    }

    private void postFirebaseDatabaseQuizChoice() {
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        Firebase_BombChoice post = new Firebase_BombChoice(quiz, ans, ans1, ans2, ans3, ans4, "choice", "");
        postValues = post.toMap();
        int temp_cnt = bomb_cnt - '0' + 1;
        childUpdates.put(nickname_key+"quiz"+temp_cnt, postValues);
        mPostReference.child(timestamp_key).child("quiz_list").updateChildren(childUpdates);
        mPostReference.child(timestamp_key).child("state").setValue("gaming"+temp_cnt);
        editQuiz.setText("");
        editAns1.setText("");
        editAns2.setText("");
        editAns3.setText("");
        editAns4.setText("");
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
