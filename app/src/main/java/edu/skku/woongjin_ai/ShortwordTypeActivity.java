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

public class ShortwordTypeActivity extends AppCompatActivity
        implements ShowScriptFragment.OnFragmentInteractionListener, HintWritingFragment.OnFragmentInteractionListener, HintVideoFragment.OnFragmentInteractionListener{

    DatabaseReference mPostReference;
    ImageView imageStar1, imageStar2, imageStar3, imageStar4, imageStar5;
    EditText editQuiz, editAns, editDesc;
    Intent intent, intentHome;
    String id, scriptnm, backgroundID;
    String quiz = "", ans = "", desc = "";
    int star = 0;
    int flagS1 = 0, flagS2 = 0, flagS3 = 0, flagS4 = 0, flagS5 = 0, flagD=0, flagB=0, flagNoHint=0;
    ImageView backgroundImage;
    ImageButton checkButton, scriptButton, hintWritingButton, hintVideoButton, noHintButton;
    FirebaseStorage storage;
    private StorageReference storageReference, dataReference;
    Fragment showScriptFragment, hintWritingFragment, hintVideoFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shortwordtype);

        intent = getIntent();
        id = intent.getStringExtra("id");
        scriptnm = intent.getStringExtra("scriptnm");
        backgroundID = intent.getStringExtra("background");

        showScriptFragment = new ShowScriptFragment();
        hintWritingFragment = new HintWritingFragment();
        hintVideoFragment = new HintVideoFragment();

        ImageView imageHome = (ImageView) findViewById(R.id.home);
        imageStar1 = (ImageView) findViewById(R.id.star1);
        imageStar2 = (ImageView) findViewById(R.id.star2);
        imageStar3 = (ImageView) findViewById(R.id.star3);
        imageStar4 = (ImageView) findViewById(R.id.star4);
        imageStar5 = (ImageView) findViewById(R.id.star5);
        editQuiz = (EditText) findViewById(R.id.quiz);
        editAns = (EditText) findViewById(R.id.ans);
        //editDesc = (EditText) findViewById(R.id.desc);
        TextView title = (TextView) findViewById(R.id.title);
        backgroundImage = (ImageView) findViewById(R.id.background);
        checkButton = (ImageButton) findViewById(R.id.check);
        scriptButton = (ImageButton) findViewById(R.id.script);
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
                Picasso.with(ShortwordTypeActivity.this)
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
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.contentSelectHint, hintWritingFragment);
                Bundle bundle = new Bundle(1);
                bundle.putString("type", "shortword");
                hintWritingFragment.setArguments(bundle);
                transaction.addToBackStack(null);
                transaction.commit();
                checkButton.setImageResource(R.drawable.ic_icons_quiz_complete);
                flagD = 1;
            }
        });

        hintVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.contentShowScriptShortword, hintVideoFragment);
                Bundle bundle = new Bundle(1);
                bundle.putString("type", "shortword");
                hintVideoFragment.setArguments(bundle);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
/*
        noHintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkButton.setImageResource(R.drawable.ic_icons_quiz_complete);
                noHintButton.setImageResource(R.drawable.ic_icons_no_hint_after);
                flagD = 1;
                flagNoHint=1;
                desc = "NoHint";
            }
        });*/
        noHintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagNoHint == 0) {
                    noHintButton.setImageResource(R.drawable.ic_icons_no_hint_after);
                    checkButton.setImageResource(R.drawable.ic_icons_quiz_complete);
                    flagD = 2;
                    flagNoHint = 1;
                } else {
                    noHintButton.setImageResource(R.drawable.ic_icons_no_hint_before);
                    checkButton.setImageResource(R.drawable.ic_icons_quiz_complete_inactivate);
                    flagD = 0;
                    flagNoHint = 0;
                }
                //TODO 힌트 없음도 fragment 만들어?
            }
        });

        scriptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagB == 1) {
                    scriptButton.setImageResource(R.drawable.ic_icons_see_script);
                    flagB = 0;
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.remove(hintVideoFragment);
                    transaction.commit();
                } else {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.contentShowScriptShortword, showScriptFragment);
                    Bundle bundle = new Bundle(2);
                    bundle.putString("scriptnm", scriptnm);
                    bundle.putString("type", "shortword");
                    showScriptFragment.setArguments(bundle);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        });

        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagD == 0) {
                    Toast.makeText(ShortwordTypeActivity.this, "힌트 타입을 고르시오.", Toast.LENGTH_SHORT).show();
                } else {
                    quiz = editQuiz.getText().toString();
                    ans=editAns.getText().toString();
                    HintWritingFragment hintWritingFragment1 = (HintWritingFragment) getSupportFragmentManager().findFragmentById(R.id.contentSelectHint);

                    if(flagNoHint==0)
                        desc = hintWritingFragment1.editTextHint.getText().toString();
                    else
                        desc="힌트가 없습니다!";

                    if(quiz.length() == 0 || ans.length() == 0 || desc.length() == 0 || star < 1 ) {
                        Toast.makeText(ShortwordTypeActivity.this, "Fill all blanks", Toast.LENGTH_SHORT).show();
//                        Log.d("quiz_length", quiz );
//                        Log.d("ans_length", ans);
//                        Log.d("desc_length", desc);

                    } else {
                        postFirebaseDatabaseQuizShortword();
                        if(flagNoHint==0)
                            hintWritingFragment1.editTextHint.setText("");
                    }
                }
            }
        });

        imageHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentHome = new Intent(ShortwordTypeActivity.this, MainActivity.class);
                intentHome.putExtra("id", id);
                startActivity(intentHome);
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
    }

    private void postFirebaseDatabaseQuizShortword() {
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();
        ts = ts + id;
        QuizOXShortwordTypeInfo post = new QuizOXShortwordTypeInfo(id, quiz, ans, Integer.toString(star), desc, "0", ts, 1);
        postValues = post.toMap();
        childUpdates.put("/quiz_list/" + scriptnm + "/type3/" + ts + "/", postValues);
        mPostReference.updateChildren(childUpdates);
        editQuiz.setText("");
        editAns.setText("");
        //editDesc.setText("");
    }
    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}