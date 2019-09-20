package edu.skku.woongjin_ai;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class OXTypeActivity extends AppCompatActivity
        implements ShowScriptFragment.OnFragmentInteractionListener, HintWritingFragment.OnFragmentInteractionListener, HintVideoFragment.OnFragmentInteractionListener {

    DatabaseReference mPostReference;
    ImageView imageO, imageX;
    EditText editQuiz;
    Intent intent, intentHome;
    String id, scriptnm, backgroundID;
    String quiz = "", ans = "", desc = "";
    int star = 0 , starInt = 0;
    ImageView imageViewS1, imageViewS2, imageViewS3, imageViewS4, imageViewS5;
    int flagAO = 0, flagAX = 0, flagS1 = 0, flagS2 = 0, flagS3 = 0, flagS4 = 0, flagS5 = 0, flagD = 0, flagNoHint = 0;
    ImageView backgroundImage;
    ImageButton checkButton, scriptButton, hintWritingButton, hintVideoButton, noHintButton;
    FirebaseStorage storage;
    private StorageReference storageReference, dataReference;
    Fragment showScriptFragment, hintWritingFragment, hintVideoFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oxtype);

        intent = getIntent();
        id = intent.getStringExtra("id");
        scriptnm = intent.getStringExtra("scriptnm");
        backgroundID = intent.getStringExtra("background");

        showScriptFragment = new ShowScriptFragment();
        hintWritingFragment = new HintWritingFragment();
        hintVideoFragment = new HintVideoFragment();

        ImageView imageHome = (ImageView) findViewById(R.id.home);
        imageO = (ImageView) findViewById(R.id.o);
        imageX = (ImageView) findViewById(R.id.x);
        imageViewS1 = (ImageView) findViewById(R.id.star1);
        imageViewS2 = (ImageView) findViewById(R.id.star2);
        imageViewS3 = (ImageView) findViewById(R.id.star3);
        imageViewS4 = (ImageView) findViewById(R.id.star4);
        imageViewS5 = (ImageView) findViewById(R.id.star5);
        editQuiz = (EditText) findViewById(R.id.quiz);
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
                Picasso.with(OXTypeActivity.this)
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
                bundle.putString("type", "ox");
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
                transaction.replace(R.id.contentShowScriptOX, hintVideoFragment);
                Bundle bundle = new Bundle(1);
                bundle.putString("type", "ox");
                hintVideoFragment.setArguments(bundle);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

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
            }
        });

        scriptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.contentShowScriptOX, showScriptFragment);
                Bundle bundle = new Bundle(2);
                bundle.putString("scriptnm", scriptnm);
                bundle.putString("type", "ox");
                showScriptFragment.setArguments(bundle);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagD == 0) {
                    Toast.makeText(OXTypeActivity.this, "힌트 타입을 고르시오.", Toast.LENGTH_SHORT).show();
                } else {
                    HintWritingFragment hintWritingFragment1 = (HintWritingFragment) getSupportFragmentManager().findFragmentById(R.id.contentSelectHint);
                    if(flagD == 2) {
                        desc = "없음";
                    } else {
                        desc = hintWritingFragment1.editTextHint.getText().toString();
                    }
                    quiz = editQuiz.getText().toString();

                    if(quiz.length() == 0 || desc.length() == 0 || starInt < 1 || (flagAO == 0 && flagAX == 0)) {
                        Toast.makeText(OXTypeActivity.this, "빈 칸을 채워주세요", Toast.LENGTH_SHORT).show();
                    } else {
                        postFirebaseDatabaseQuizOX();
                        if(flagD == 1) hintWritingFragment1.editTextHint.setText("");
                        Toast.makeText(OXTypeActivity.this, "출제 완료!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        imageHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentHome = new Intent(OXTypeActivity.this, MainActivity.class);
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

        imageO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagAO == 0) {
                    if(flagAX == 1) {
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
                if(flagAX == 0) {
                    if(flagAO == 1) {
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
        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();
        ts = ts + id;
        QuizOXShortwordTypeInfo post = new QuizOXShortwordTypeInfo(id, quiz, ans, Integer.toString(starInt), desc, "0", ts, 1);
        postValues = post.toMap();
        childUpdates.put("/quiz_list/" + scriptnm + "/type1/" + ts + "/", postValues);
        mPostReference.updateChildren(childUpdates);
        editQuiz.setText("");
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}