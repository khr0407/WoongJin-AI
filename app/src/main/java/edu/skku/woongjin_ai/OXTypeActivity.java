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
import android.widget.ImageView;
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
        implements ShowScriptFragment.OnFragmentInteractionListener {

    DatabaseReference mPostReference;
    ImageView imageO, imageX, imageScript, imageCheck, imageStar1, imageStar2, imageStar3, imageStar4, imageStar5;
    EditText editQuiz, editDesc;
    Intent intent, intentHome;
    String id, scriptnm, backgroundID;
    String quiz = "", ans = "", desc = "";
    int star = 0;
    int flagAO = 0, flagAX = 0, flagS1 = 0, flagS2 = 0, flagS3 = 0, flagS4 = 0, flagS5 = 0;
    ImageView backgroundImage;
    FirebaseStorage storage;
    private StorageReference storageReference, dataReference;
    Fragment showScriptFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oxtype);

        intent = getIntent();
        id = intent.getStringExtra("id");
        scriptnm = intent.getStringExtra("scriptnm");
        backgroundID = intent.getStringExtra("background");

        showScriptFragment = new ShowScriptFragment();

        ImageView imageHome = (ImageView) findViewById(R.id.home);
        imageO = (ImageView) findViewById(R.id.o);
        imageX = (ImageView) findViewById(R.id.x);
        imageScript = (ImageView) findViewById(R.id.script);
        imageCheck = (ImageView) findViewById(R.id.check);
        imageStar1 = (ImageView) findViewById(R.id.star1);
        imageStar2 = (ImageView) findViewById(R.id.star2);
        imageStar3 = (ImageView) findViewById(R.id.star3);
        imageStar4 = (ImageView) findViewById(R.id.star4);
        imageStar5 = (ImageView) findViewById(R.id.star5);
        editQuiz = (EditText) findViewById(R.id.quiz);
        editDesc = (EditText) findViewById(R.id.desc);
        TextView title = (TextView) findViewById(R.id.title);
        backgroundImage = (ImageView) findViewById(R.id.background);

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

        imageScript.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.contentShowScriptOX, showScriptFragment);
                Bundle bundle = new Bundle(1);
                bundle.putString("scriptnm", scriptnm);
                showScriptFragment.setArguments(bundle);
                transaction.commit();
            }
        });

        imageCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quiz = editQuiz.getText().toString();
                desc = editDesc.getText().toString();
                if(quiz.length() == 0 || desc.length() == 0 || star < 1 || (flagAO == 0 && flagAX == 0)) {
                    Toast.makeText(OXTypeActivity.this, "Fill all blanks", Toast.LENGTH_SHORT).show();
                } else {
                    postFirebaseDatabaseQuizOX();
                }
            }
        });

        imageHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentHome = new Intent(OXTypeActivity.this, MainActivity.class);
                intentHome.putExtra("id", id);
                startActivity(intentHome);
                finish();
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
        QuizOXShortwordTypeInfo post = new QuizOXShortwordTypeInfo(id, quiz, ans, Integer.toString(star), desc, "0");
        postValues = post.toMap();
        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();
        ts = ts + id;
        childUpdates.put("/quiz_list/" + scriptnm + "/type1/" + ts + "/", postValues);
        mPostReference.updateChildren(childUpdates);
        editQuiz.setText("");
        editDesc.setText("");
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}