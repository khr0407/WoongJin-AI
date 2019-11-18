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
    ImageView imageViewS1, imageViewS2, imageViewS3, imageViewS4, imageViewS5;
    EditText editQuiz, editAns;
    Intent intent, intentHome;
    String id, scriptnm, backgroundID;
    String quiz = "", ans = "", desc = "";
    int star = 0, starInt = 0;
    int flagS1 = 0, flagS2 = 0, flagS3 = 0, flagS4 = 0, flagS5 = 0, flagD = 0;
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

        ImageView imageHome = (ImageView) findViewById(R.id.home);
        imageViewS1 = (ImageView) findViewById(R.id.star1);
        imageViewS2 = (ImageView) findViewById(R.id.star2);
        imageViewS3 = (ImageView) findViewById(R.id.star3);
        imageViewS4 = (ImageView) findViewById(R.id.star4);
        imageViewS5 = (ImageView) findViewById(R.id.star5);
        editQuiz = (EditText) findViewById(R.id.quiz);
        editAns = (EditText) findViewById(R.id.ans);
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
                hintWritingFragment = new HintWritingFragment();
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
                hintVideoFragment = new HintVideoFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.contentShowScriptShortword, hintVideoFragment);
                Bundle bundle = new Bundle(1);
                bundle.putString("type", "shortword");
                hintVideoFragment.setArguments(bundle);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        noHintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagD != 2) {
                    noHintButton.setImageResource(R.drawable.ic_icons_no_hint_after);
                    checkButton.setImageResource(R.drawable.ic_icons_quiz_complete);
                    flagD = 2;
                } else {
                    noHintButton.setImageResource(R.drawable.ic_icons_no_hint_before);
                    checkButton.setImageResource(R.drawable.ic_icons_quiz_complete_inactivate);
                    flagD = 0;
                }
            }
        });

        scriptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showScriptFragment = new ShowScriptFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.contentShowScriptShortword, showScriptFragment);
                Bundle bundle = new Bundle(2);
                bundle.putString("scriptnm", scriptnm);
                bundle.putString("type", "shortword");
                showScriptFragment.setArguments(bundle);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagD == 0) {
                    Toast.makeText(ShortwordTypeActivity.this, "힌트 타입을 고르시오.", Toast.LENGTH_SHORT).show();
                } else {
                    HintWritingFragment hintWritingFragment1 = (HintWritingFragment) getSupportFragmentManager().findFragmentById(R.id.contentSelectHint);
                    if(flagD == 2) {
                        desc="없음";
                    } else {
                        desc = hintWritingFragment1.editTextHint.getText().toString();
                    }
                    quiz = editQuiz.getText().toString();
                    ans = editAns.getText().toString();

                    if(quiz.length() == 0 || ans.length() == 0 || desc.length() == 0 || starInt < 1 ) {
                        Toast.makeText(ShortwordTypeActivity.this, "Fill all blanks", Toast.LENGTH_SHORT).show();
                    } else {
                        postFirebaseDatabaseQuizShortword();
                        if(flagD == 1) hintWritingFragment1.editTextHint.setText("");
                        Toast.makeText(ShortwordTypeActivity.this, "출제 완료!", Toast.LENGTH_SHORT).show();
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

        imageViewS1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagS1 == 0) {
                    starInt = 1;
                    imageViewS1.setImageResource(R.drawable.ic_icons_difficulty_star_full);
                    flagS1 = 1;
                } else {
                    starInt = 0;
                    imageViewS1.setImageResource(R.drawable.ic_icons_difficulty_star_empty);
                    imageViewS2.setImageResource(R.drawable.ic_icons_difficulty_star_empty);
                    imageViewS3.setImageResource(R.drawable.ic_icons_difficulty_star_empty);
                    imageViewS4.setImageResource(R.drawable.ic_icons_difficulty_star_empty);
                    imageViewS5.setImageResource(R.drawable.ic_icons_difficulty_star_empty);
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
                    imageViewS1.setImageResource(R.drawable.ic_icons_difficulty_star_full);
                    imageViewS2.setImageResource(R.drawable.ic_icons_difficulty_star_full);
                    flagS1 = 1;
                    flagS2 = 1;
                } else {
                    starInt = 0;
                    imageViewS1.setImageResource(R.drawable.ic_icons_difficulty_star_empty);
                    imageViewS2.setImageResource(R.drawable.ic_icons_difficulty_star_empty);
                    imageViewS3.setImageResource(R.drawable.ic_icons_difficulty_star_empty);
                    imageViewS4.setImageResource(R.drawable.ic_icons_difficulty_star_empty);
                    imageViewS5.setImageResource(R.drawable.ic_icons_difficulty_star_empty);
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
                    imageViewS1.setImageResource(R.drawable.ic_icons_difficulty_star_full);
                    imageViewS2.setImageResource(R.drawable.ic_icons_difficulty_star_full);
                    imageViewS3.setImageResource(R.drawable.ic_icons_difficulty_star_full);
                    flagS1 = 1;
                    flagS2 = 1;
                    flagS3 = 1;
                } else {
                    starInt = 0;
                    imageViewS1.setImageResource(R.drawable.ic_icons_difficulty_star_empty);
                    imageViewS2.setImageResource(R.drawable.ic_icons_difficulty_star_empty);
                    imageViewS3.setImageResource(R.drawable.ic_icons_difficulty_star_empty);
                    imageViewS4.setImageResource(R.drawable.ic_icons_difficulty_star_empty);
                    imageViewS5.setImageResource(R.drawable.ic_icons_difficulty_star_empty);
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
                    imageViewS1.setImageResource(R.drawable.ic_icons_difficulty_star_full);
                    imageViewS2.setImageResource(R.drawable.ic_icons_difficulty_star_full);
                    imageViewS3.setImageResource(R.drawable.ic_icons_difficulty_star_full);
                    imageViewS4.setImageResource(R.drawable.ic_icons_difficulty_star_full);
                    flagS1 = 1;
                    flagS2 = 1;
                    flagS3 = 1;
                    flagS4 = 1;
                } else {
                    starInt = 0;
                    imageViewS1.setImageResource(R.drawable.ic_icons_difficulty_star_empty);
                    imageViewS2.setImageResource(R.drawable.ic_icons_difficulty_star_empty);
                    imageViewS3.setImageResource(R.drawable.ic_icons_difficulty_star_empty);
                    imageViewS4.setImageResource(R.drawable.ic_icons_difficulty_star_empty);
                    imageViewS5.setImageResource(R.drawable.ic_icons_difficulty_star_empty);
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
                    imageViewS1.setImageResource(R.drawable.ic_icons_difficulty_star_full);
                    imageViewS2.setImageResource(R.drawable.ic_icons_difficulty_star_full);
                    imageViewS3.setImageResource(R.drawable.ic_icons_difficulty_star_full);
                    imageViewS4.setImageResource(R.drawable.ic_icons_difficulty_star_full);
                    imageViewS5.setImageResource(R.drawable.ic_icons_difficulty_star_full);
                    flagS1 = 1;
                    flagS2 = 1;
                    flagS3 = 1;
                    flagS4 = 1;
                    flagS5 = 1;
                } else {
                    starInt = 0;
                    imageViewS1.setImageResource(R.drawable.ic_icons_difficulty_star_empty);
                    imageViewS2.setImageResource(R.drawable.ic_icons_difficulty_star_empty);
                    imageViewS3.setImageResource(R.drawable.ic_icons_difficulty_star_empty);
                    imageViewS4.setImageResource(R.drawable.ic_icons_difficulty_star_empty);
                    imageViewS5.setImageResource(R.drawable.ic_icons_difficulty_star_empty);
                    flagS1 = 0;
                    flagS2 = 0;
                    flagS3 = 0;
                    flagS4 = 0;
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
        QuizOXShortwordTypeInfo post = new QuizOXShortwordTypeInfo(id, quiz, ans, Integer.toString(starInt), desc, "0", ts, 1, "없음", 3);
        postValues = post.toMap();
        childUpdates.put("/quiz_list/" + scriptnm + "/" + ts + "/", postValues);
        mPostReference.updateChildren(childUpdates);
        editQuiz.setText("");
        editAns.setText("");
    }
    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}