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

public class ShortwordTypeActivity extends AppCompatActivity
        implements ShowScriptFragment.OnFragmentInteractionListener, HintWritingFragment.OnFragmentInteractionListener{

    DatabaseReference mPostReference;
    ImageView imageScript, imageCheck, imageStar1, imageStar2, imageStar3, imageStar4, imageStar5;
    EditText editQuiz, editAns, editDesc;
    Intent intent, intentHome;
    String id, scriptnm, backgroundID;
    String quiz = "", ans = "", desc = "";
    int star = 0;
    int flagS1 = 0, flagS2 = 0, flagS3 = 0, flagS4 = 0, flagS5 = 0;
    ImageView backgroundImage;
    ImageButton hintWritingButton, hintVideoButton, noHintButton;
    FirebaseStorage storage;
    private StorageReference storageReference, dataReference;
    Fragment showScriptFragment, hintWritingFragment;

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
        //editDesc = (EditText) findViewById(R.id.desc);
        TextView title = (TextView) findViewById(R.id.title);
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

            }
        });

        imageScript.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.contentShowScriptShortword, showScriptFragment);
                Bundle bundle = new Bundle(2);
                bundle.putString("scriptnm", scriptnm);
                bundle.putString("type", "shortword");
                showScriptFragment.setArguments(bundle);
                transaction.commit();
            }
        });

        imageCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quiz = editQuiz.getText().toString();
                ans = editAns.getText().toString();
                desc = editDesc.getText().toString();
                if(quiz.length() == 0 || ans.length() == 0 || desc.length() == 0 || star < 1) {
                    Toast.makeText(ShortwordTypeActivity.this, "Fill all blanks", Toast.LENGTH_SHORT).show();
                } else {
                    postFirebaseDatabaseQuizShortword();
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
                    imageStar1.setImageResource(R.drawable.checked_circle_white);
                    flagS1 = 1;
                } else {
                    star--;
                    imageStar1.setImageResource(R.drawable.unchecked_circle_white);
                    flagS1 = 0;
                }
            }
        });

        imageStar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagS2 == 0) {
                    star++;
                    imageStar2.setImageResource(R.drawable.checked_circle_white);
                    flagS2 = 1;
                } else {
                    star--;
                    imageStar2.setImageResource(R.drawable.unchecked_circle_white);
                    flagS2 = 0;
                }
            }
        });

        imageStar3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagS3 == 0) {
                    star++;
                    imageStar3.setImageResource(R.drawable.checked_circle_white);
                    flagS3 = 1;
                } else {
                    star--;
                    imageStar3.setImageResource(R.drawable.unchecked_circle_white);
                    flagS3 = 0;
                }
            }
        });

        imageStar4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagS4 == 0) {
                    star++;
                    imageStar4.setImageResource(R.drawable.checked_circle_white);
                    flagS4 = 1;
                } else {
                    star--;
                    imageStar4.setImageResource(R.drawable.unchecked_circle_white);
                    flagS4 = 0;
                }
            }
        });

        imageStar5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagS5 == 0) {
                    star++;
                    imageStar5.setImageResource(R.drawable.checked_circle_white);
                    flagS5 = 1;
                } else {
                    star--;
                    imageStar5.setImageResource(R.drawable.unchecked_circle_white);
                    flagS5 = 0;
                }
            }
        });
    }

    private void postFirebaseDatabaseQuizShortword() {
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        QuizOXShortwordTypeInfo post = new QuizOXShortwordTypeInfo(id, quiz, ans, Integer.toString(star), desc, "0");
        postValues = post.toMap();
        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();
        ts = ts + id;
        childUpdates.put("/quiz_list/" + scriptnm + "/type3/" + ts + "/", postValues);
        mPostReference.updateChildren(childUpdates);
        editQuiz.setText("");
        editAns.setText("");
        editDesc.setText("");
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}