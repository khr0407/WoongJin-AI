package edu.skku.woongjin_ai;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import edu.skku.woongjin_ai.mediarecorder.MediaRecorderActivity;

public class ChoiceTypeActivity extends AppCompatActivity
        implements ShowScriptFragment.OnFragmentInteractionListener, HintWritingFragment.OnFragmentInteractionListener/*, HintVideoFragment.OnFragmentInteractionListener*/{

    DatabaseReference mPostReference;
    ImageView imageScript, imageCheck,imageViewS1, imageViewS2, imageViewS3, imageViewS4, imageViewS5;
    EditText editQuiz, editAns, editAns1, editAns2, editAns3, editAns4;
    Intent intent, intentHome, intentType, intentVideo;
    String id, scriptnm, backgroundID, thisWeek, nickname, bookname;
    String quiz = "", ans = "", ans1 = "", ans2 = "", ans3 = "", ans4 = "", desc = "";
    int star = 0, starInt = 0, oldMadeCnt;
    int flagS1 = 0, flagS2 = 0, flagS3 = 0, flagS4 = 0, flagS5 = 0, flagD = 0;
    int flagA1 =0, flagA2=0, flagA3=0,flagA4 =0;
    ImageView backgroundImage;
    ImageButton checkButton, scriptButton, hintWritingButton, hintVideoButton, noHintButton;
    //    FirebaseStorage storage;
//    private StorageReference storageReference, dataReference;
    Fragment showScriptFragment, hintWritingFragment, hintVideoFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choicetype);

        intent = getIntent();
        id = intent.getStringExtra("id");
        scriptnm = intent.getStringExtra("scriptnm");
        backgroundID = intent.getStringExtra("background");
        nickname = intent.getStringExtra("nickname");
        thisWeek = intent.getStringExtra("thisWeek");

        ImageView imageHome = (ImageView) findViewById(R.id.home);
        imageScript = (ImageView) findViewById(R.id.script);
        imageCheck = (ImageView) findViewById(R.id.check);
        imageViewS1 = (ImageView) findViewById(R.id.star1);
        imageViewS2 = (ImageView) findViewById(R.id.star2);
        imageViewS3 = (ImageView) findViewById(R.id.star3);
        imageViewS4 = (ImageView) findViewById(R.id.star4);
        imageViewS5 = (ImageView) findViewById(R.id.star5);
        editQuiz = (EditText) findViewById(R.id.quiz);
        editAns = (EditText) findViewById(R.id.ans);
        editAns1 = (EditText) findViewById(R.id.ans1);
        editAns2 = (EditText) findViewById(R.id.ans2);
        editAns3 = (EditText) findViewById(R.id.ans3);
        editAns4 = (EditText) findViewById(R.id.ans4);
        TextView title = (TextView) findViewById(R.id.title);
        backgroundImage = (ImageView) findViewById(R.id.background);
        checkButton = (ImageButton) findViewById(R.id.check);
        scriptButton = (ImageButton) findViewById(R.id.script);
        backgroundImage = (ImageView) findViewById(R.id.background);
        hintWritingButton = (ImageButton) findViewById(R.id.hintWriting);
        hintVideoButton = (ImageButton) findViewById(R.id.hintVideo);
        noHintButton = (ImageButton) findViewById(R.id.noHint);


        title.setText("지문 제목: " + scriptnm);

        mPostReference = FirebaseDatabase.getInstance().getReference();

        getFirebaseDatabaseMadeInfo();

//        storage = FirebaseStorage.getInstance();
//        storageReference = storage.getInstance().getReference();
//        dataReference = storageReference.child("/scripts_background/" + backgroundID);
//        dataReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                Picasso.with(ChoiceTypeActivity.this)
//                        .load(uri)
//                        .placeholder(R.drawable.bot)
//                        .error(R.drawable.btn_x)
//                        .into(backgroundImage);
//                backgroundImage.setAlpha(0.5f);
//            }
//        });

        hintWritingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hintWritingFragment = new HintWritingFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.contentSelectHint, hintWritingFragment);
                Bundle bundle = new Bundle(1);
                bundle.putString("type", "choice");
                hintWritingFragment.setArguments(bundle);
                transaction.addToBackStack(null);
                transaction.commit();
//                checkButton.setImageResource(R.drawable.ic_icons_quiz_complete);
                flagD = 1;
            }
        });

        hintVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentVideo = new Intent(ChoiceTypeActivity.this, MediaRecorderActivity.class);
                intentVideo.putExtra("id", id);
                startActivity(intentVideo);
                /*
                hintVideoFragment = new HintVideoFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.contentShowScriptChoice, hintVideoFragment);
                Bundle bundle = new Bundle(1);
                bundle.putString("type", "choice");
                hintVideoFragment.setArguments(bundle);
                transaction.addToBackStack(null);
                transaction.commit();
                */
            }
        });

        noHintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagD != 2) {
                    noHintButton.setBackgroundColor(Color.rgb(255, 153, 0));
                    flagD = 2;
                } else {
                    noHintButton.setBackgroundColor(Color.rgb(255, 255, 255));
                    flagD = 0;
                }
            }
        });

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
                if(flagD == 0) {
                    Toast.makeText(ChoiceTypeActivity.this, "힌트 타입을 고르시오.", Toast.LENGTH_SHORT).show();
                } else {
                    quiz = editQuiz.getText().toString();
                    HintWritingFragment hintWritingFragment1 = (HintWritingFragment) getSupportFragmentManager().findFragmentById(R.id.contentSelectHint);
                    if(flagD == 2) {
                        desc="없음";
                    } else {
                        desc = hintWritingFragment1.editTextHint.getText().toString();
                    }
                    quiz = editQuiz.getText().toString();
                    ans1 = editAns1.getText().toString();
                    ans2 = editAns2.getText().toString();
                    ans3 = editAns3.getText().toString();
                    ans4 = editAns4.getText().toString();

                    if(quiz.length() == 0 || ans.length() == 0 || ans1.length() == 0 || ans2.length() == 0 || ans3.length() == 0 || ans4.length() == 0 || desc.length() == 0 || starInt < 1) {
                        Toast.makeText(ChoiceTypeActivity.this, "Fill all blanks", Toast.LENGTH_SHORT).show();
                    } else {
                        postFirebaseDatabaseQuizChoice();
                        uploadFirebaseUserCoinInfo();
                        if(flagD == 1) hintWritingFragment1.editTextHint.setText("");
                        Toast.makeText(ChoiceTypeActivity.this, "출제 완료!", Toast.LENGTH_SHORT).show();

                        oldMadeCnt++;
                        mPostReference.child("user_list/" + id + "/my_week_list/week" + thisWeek + "/made").setValue(oldMadeCnt);

                        intentType = new Intent(ChoiceTypeActivity.this, SelectTypeActivity.class);
                        intentType.putExtra("id", id);
                        intentType.putExtra("scriptnm", scriptnm);
                        intentType.putExtra("background", backgroundID);
                        intentType.putExtra("nickname", nickname);
                        intentType.putExtra("thisWeek", thisWeek);
                        startActivity(intentType);
                    }
                }


            }
        });

        imageHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentHome = new Intent(ChoiceTypeActivity.this, MainActivity.class);
                intentHome.putExtra("id", id);
                startActivity(intentHome);
            }
        });

        imageViewS1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagS1 == 0) {
                    starInt = 1;
                    imageViewS1.setImageResource(R.drawable.star_full);
                    flagS1 = 1;
                } else {
                    starInt = 0;
                    imageViewS1.setImageResource(R.drawable.star_empty);
                    imageViewS2.setImageResource(R.drawable.star_empty);
                    imageViewS3.setImageResource(R.drawable.star_empty);
                    imageViewS4.setImageResource(R.drawable.star_empty);
                    imageViewS5.setImageResource(R.drawable.star_empty);
                    flagS1 = 0;
                    flagS2 = 0;
                    flagS3 = 0;
                    flagS4 = 0;
                    flagS5 = 0;
                }
            }
        });

        imageViewS2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagS2 == 0) {
                    starInt = 2;
                    imageViewS1.setImageResource(R.drawable.star_full);
                    imageViewS2.setImageResource(R.drawable.star_full);
                    flagS1 = 1;
                    flagS2 = 1;
                } else {
                    starInt = 0;
                    imageViewS1.setImageResource(R.drawable.star_empty);
                    imageViewS2.setImageResource(R.drawable.star_empty);
                    imageViewS3.setImageResource(R.drawable.star_empty);
                    imageViewS4.setImageResource(R.drawable.star_empty);
                    imageViewS5.setImageResource(R.drawable.star_empty);
                    flagS1 = 0;
                    flagS2 = 0;
                    flagS3 = 0;
                    flagS4 = 0;
                    flagS5 = 0;
                }
            }
        });

        imageViewS3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagS3 == 0) {
                    starInt = 3;
                    imageViewS1.setImageResource(R.drawable.star_full);
                    imageViewS2.setImageResource(R.drawable.star_full);
                    imageViewS3.setImageResource(R.drawable.star_full);
                    flagS1 = 1;
                    flagS2 = 1;
                    flagS3 = 1;
                } else {
                    starInt = 0;
                    imageViewS1.setImageResource(R.drawable.star_empty);
                    imageViewS2.setImageResource(R.drawable.star_empty);
                    imageViewS3.setImageResource(R.drawable.star_empty);
                    imageViewS4.setImageResource(R.drawable.star_empty);
                    imageViewS5.setImageResource(R.drawable.star_empty);
                    flagS1 = 0;
                    flagS2 = 0;
                    flagS3 = 0;
                    flagS4 = 0;
                    flagS5 = 0;
                }
            }
        });

        imageViewS4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagS4 == 0) {
                    starInt = 4;
                    imageViewS1.setImageResource(R.drawable.star_full);
                    imageViewS2.setImageResource(R.drawable.star_full);
                    imageViewS3.setImageResource(R.drawable.star_full);
                    imageViewS4.setImageResource(R.drawable.star_full);
                    flagS1 = 1;
                    flagS2 = 1;
                    flagS3 = 1;
                    flagS4 = 1;
                } else {
                    starInt = 0;
                    imageViewS1.setImageResource(R.drawable.star_empty);
                    imageViewS2.setImageResource(R.drawable.star_empty);
                    imageViewS3.setImageResource(R.drawable.star_empty);
                    imageViewS4.setImageResource(R.drawable.star_empty);
                    imageViewS5.setImageResource(R.drawable.star_empty);
                    flagS1 = 0;
                    flagS2 = 0;
                    flagS3 = 0;
                    flagS4 = 0;
                    flagS5 = 0;
                }
            }
        });

        imageViewS5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagS5 == 0) {
                    starInt = 5;
                    imageViewS1.setImageResource(R.drawable.star_full);
                    imageViewS2.setImageResource(R.drawable.star_full);
                    imageViewS3.setImageResource(R.drawable.star_full);
                    imageViewS4.setImageResource(R.drawable.star_full);
                    imageViewS5.setImageResource(R.drawable.star_full);
                    flagS1 = 1;
                    flagS2 = 1;
                    flagS3 = 1;
                    flagS4 = 1;
                    flagS5 = 1;
                } else {
                    starInt = 0;
                    imageViewS1.setImageResource(R.drawable.star_empty);
                    imageViewS2.setImageResource(R.drawable.star_empty);
                    imageViewS3.setImageResource(R.drawable.star_empty);
                    imageViewS4.setImageResource(R.drawable.star_empty);
                    imageViewS5.setImageResource(R.drawable.star_empty);
                    flagS1 = 0;
                    flagS2 = 0;
                    flagS3 = 0;
                    flagS4 = 0;
                    flagS5 = 0;
                }
            }
        });

        editAns1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagA1 == 0 ) {
                    if(flagA2==0 && flagA3==0 && flagA4==0 ){
//                    editAns1.setBackgroundResource(R.drawable.ic_icons_selector_correct);
                        editAns1.setBackgroundColor(Color.rgb(255, 153, 0));
                        flagA1 = 1;
                        ans = editAns1.getText().toString();
                    }
                    else{
                        Toast.makeText(ChoiceTypeActivity.this, "먼저 정답을 초기화하세요", Toast.LENGTH_SHORT).show();
                    }
                } else {
//                    editAns1.setBackgroundResource(R.drawable.ic_icons_selector_standard);
                    editAns1.setBackgroundColor(Color.rgb(255, 255, 255));
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
//                    editAns2.setBackgroundResource(R.drawable.ic_icons_selector_correct);
                        editAns2.setBackgroundColor(Color.rgb(255, 153, 0));
                        flagA2 = 1;
                        ans = editAns2.getText().toString();
                    }
                    else{
                        Toast.makeText(ChoiceTypeActivity.this, "먼저 정답을 초기화하세요", Toast.LENGTH_SHORT).show();
                    }
                } else {
//                    editAns2.setBackgroundResource(R.drawable.ic_icons_selector_standard);
                    editAns2.setBackgroundColor(Color.rgb(255, 255, 255));
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
//                        editAns3.setBackgroundResource(R.drawable.ic_icons_selector_correct);
                        editAns3.setBackgroundColor(Color.rgb(255, 153, 0));
                        flagA3 = 1;
                        ans = editAns3.getText().toString();
                    }
                    else{
                        Toast.makeText(ChoiceTypeActivity.this, "먼저 정답을 초기화하세요", Toast.LENGTH_SHORT).show();
                    }
                } else {
//                    editAns3.setBackgroundResource(R.drawable.ic_icons_selector_standard);
                    editAns3.setBackgroundColor(Color.rgb(255, 255, 255));
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
//                    editAns4.setBackgroundResource(R.drawable.ic_icons_selector_correct);
                        editAns4.setBackgroundColor(Color.rgb(255, 153, 0));
                        flagA4 = 1;
                        ans = editAns4.getText().toString();
                    }
                    else{
                        Toast.makeText(ChoiceTypeActivity.this, "먼저 정답을 초기화하세요", Toast.LENGTH_SHORT).show();
                    }
                } else {
//                    editAns4.setBackgroundResource(R.drawable.ic_icons_selector_standard);
                    editAns4.setBackgroundColor(Color.rgb(255, 255, 255));
                    flagA4 = 0;
                    ans = "";
                }
            }
        });
    }

    private void getFirebaseDatabaseMadeInfo() {
        mPostReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                WeekInfo weekInfo = dataSnapshot.child("user_list/" + id + "/my_week_list/week" + thisWeek).getValue(WeekInfo.class);
                oldMadeCnt = weekInfo.made;

                bookname = dataSnapshot.child("script_list/" + scriptnm + "/book_name").getValue().toString();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {            }
        });
    }

    private void postFirebaseDatabaseQuizChoice() {
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();
        ts = ts + id;
        QuizChoiceTypeInfo post = new QuizChoiceTypeInfo(id, quiz, ans, ans1, ans2, ans3, ans4, Integer.toString(starInt), desc, "0", ts, 1, "없음", 2, scriptnm, bookname);
        postValues = post.toMap();
        childUpdates.put("/quiz_list/" + scriptnm + "/" + ts + "/", postValues);
        mPostReference.updateChildren(childUpdates);
        editQuiz.setText("");
        editAns1.setText("");
        editAns2.setText("");
        editAns3.setText("");
        editAns4.setText("");
    }

    private void uploadFirebaseUserCoinInfo(){
        mPostReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long now = System.currentTimeMillis();
                Date date = new Date(now);
                String today = new SimpleDateFormat("yyMMddHHmm").format(date);
                mPostReference.child("user_list/" + id + "/my_coin_list/" + today + "/get").setValue("10");
                mPostReference.child("user_list/" + id + "/my_coin_list/" + today + "/why").setValue("지문 [" + scriptnm + "]에 대한 퀴즈를 냈어요.");

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

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
