package edu.skku.woongjin_ai;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

public class ChoiceTypeActivity extends AppCompatActivity
        implements ShowScriptFragment.OnFragmentInteractionListener, HintWritingFragment.OnFragmentInteractionListener{

    DatabaseReference mPostReference;
    ImageView imageScript, imageCheck, imageStar1, imageStar2, imageStar3, imageStar4, imageStar5;
    EditText editQuiz, editAns, editAns1, editAns2, editAns3, editAns4, editDesc;
    Intent intent, intentHome;
    String id, scriptnm, backgroundID;
    String quiz = "", ans = "", ans1 = "", ans2 = "", ans3 = "", ans4 = "", desc = "";
    int star = 0;
    int flagS1 = 0, flagS2 = 0, flagS3 = 0, flagS4 = 0, flagS5 = 0, flagD=0;
    int flagA1 =0, flagA2=0, flagA3=0,flagA4 =0;
    ImageView backgroundImage;
    ImageButton checkButton, scriptButton, hintWritingButton, hintVideoButton, noHintButton;
    FirebaseStorage storage;
    private StorageReference storageReference, dataReference;
    Fragment showScriptFragment, hintWritingFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choicetype);

        intent = getIntent();
        id = intent.getStringExtra("id");
        scriptnm = intent.getStringExtra("scriptnm");
        backgroundID = intent.getStringExtra("background");

        showScriptFragment = new ShowScriptFragment();
        hintWritingFragment = new HintWritingFragment();

        ImageView imageHome = (ImageView) findViewById(R.id.home);
        imageScript = (ImageView) findViewById(R.id.script);
        imageCheck = (ImageView) findViewById(R.id.check);
        imageStar1 = (ImageView) findViewById(R.id.star1);
        imageStar2 = (ImageView) findViewById(R.id.star2);
        imageStar3 = (ImageView) findViewById(R.id.star3);
        imageStar4 = (ImageView) findViewById(R.id.star4);
        imageStar5 = (ImageView) findViewById(R.id.star5);
        editQuiz = (EditText) findViewById(R.id.quiz);
        editAns = (EditText) findViewById(R.id.ans);
        editAns1 = (EditText) findViewById(R.id.ans1);
        editAns2 = (EditText) findViewById(R.id.ans2);
        editAns3 = (EditText) findViewById(R.id.ans3);
        editAns4 = (EditText) findViewById(R.id.ans4);
        //editDesc = (EditText) findViewById(R.id.desc);
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

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getInstance().getReference();
        dataReference = storageReference.child("/scripts_background/" + backgroundID);
        dataReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(ChoiceTypeActivity.this)
                        .load(uri)
                        .placeholder(R.drawable.bot)
                        .error(R.drawable.btn_x)
                        .into(backgroundImage);
                backgroundImage.setAlpha(0.5f);
            }
        });

        hintWritingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkButton.setImageResource(R.drawable.ic_icons_quiz_complete);
                flagD = 1;
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.contentSelectHint, hintWritingFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        hintVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        noHintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkButton.setImageResource(R.drawable.ic_icons_quiz_complete);
                noHintButton.setImageResource(R.drawable.ic_icons_no_hint_after);
                flagD = 1;
                desc = "null";
            }
        });

        scriptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                //ans = editAns.getText().toString();//문제코드
                ans1 = editAns1.getText().toString();
                ans2 = editAns2.getText().toString();
                ans3 = editAns3.getText().toString();
                ans4 = editAns4.getText().toString();
                //desc = editDesc.getText().toString();

                if(flagD == 0) {
                    Toast.makeText(ChoiceTypeActivity.this, "힌트 타입을 고르시오.", Toast.LENGTH_SHORT).show();
                } else {
                    quiz = editQuiz.getText().toString();

                    HintWritingFragment hintWritingFragment1 = (HintWritingFragment) getSupportFragmentManager().findFragmentById(R.id.contentSelectHint);
                    desc = hintWritingFragment1.editTextHint.getText().toString();

                    if(quiz.length() == 0 || ans.length() == 0 || ans1.length() == 0 || ans2.length() == 0 || ans3.length() == 0 || ans4.length() == 0 || desc.length() == 0 || star < 1) {
                        Toast.makeText(ChoiceTypeActivity.this, "Fill all blanks", Toast.LENGTH_SHORT).show();
                    } else {
                        postFirebaseDatabaseQuizChoice();
                        hintWritingFragment1.editTextHint.setText("");
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
                //finish();
            }
        });

        imageStar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagS1 == 0) {
                    star++;
                    imageStar1.setImageResource(R.drawable.ic_icons_difficulty_star_full);
                    flagS1 = 1;
                } else {
                    star--;
                    imageStar1.setImageResource(R.drawable.ic_icons_difficulty_star_empty);
                    flagS1 = 0;
                }
            }
        });

        imageStar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagS2 == 0) {
                    star++;
                    imageStar2.setImageResource(R.drawable.ic_icons_difficulty_star_full);
                    flagS2 = 1;
                } else {
                    star--;
                    imageStar2.setImageResource(R.drawable.ic_icons_difficulty_star_empty);
                    flagS2 = 0;
                }
            }
        });

        imageStar3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagS3 == 0) {
                    star++;
                    imageStar3.setImageResource(R.drawable.ic_icons_difficulty_star_full);
                    flagS3 = 1;
                } else {
                    star--;
                    imageStar3.setImageResource(R.drawable.ic_icons_difficulty_star_empty);
                    flagS3 = 0;
                }
            }
        });

        imageStar4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagS4 == 0) {
                    star++;
                    imageStar4.setImageResource(R.drawable.ic_icons_difficulty_star_full);
                    flagS4 = 1;
                } else {
                    star--;
                    imageStar4.setImageResource(R.drawable.ic_icons_difficulty_star_empty);
                    flagS4 = 0;
                }
            }
        });

        imageStar5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagS5 == 0) {
                    star++;
                    imageStar5.setImageResource(R.drawable.ic_icons_difficulty_star_full);
                    flagS5 = 1;
                } else {
                    star--;
                    imageStar5.setImageResource(R.drawable.ic_icons_difficulty_star_empty);
                    flagS5 = 0;
                }
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
                        Toast.makeText(ChoiceTypeActivity.this, "먼저 정답을 초기화하세요", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(ChoiceTypeActivity.this, "먼저 정답을 초기화하세요", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(ChoiceTypeActivity.this, "먼저 정답을 초기화하세요", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(ChoiceTypeActivity.this, "먼저 정답을 초기화하세요", Toast.LENGTH_SHORT).show();
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
        QuizChoiceTypeInfo post = new QuizChoiceTypeInfo(id, quiz, ans, ans1, ans2, ans3, ans4, Integer.toString(star), desc, "0");
        postValues = post.toMap();
        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();
        ts = ts + id;
        childUpdates.put("/quiz_list/" + scriptnm + "/type2/" + ts + "/", postValues);
        mPostReference.updateChildren(childUpdates);
        editQuiz.setText("");
        //editAns.setText("");
        editAns1.setText("");
        editAns2.setText("");
        editAns3.setText("");
        editAns4.setText("");
        //editDesc.setText("");
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
