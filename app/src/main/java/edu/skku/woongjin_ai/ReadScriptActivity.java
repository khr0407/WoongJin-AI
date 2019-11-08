package edu.skku.woongjin_ai;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ReadScriptActivity extends AppCompatActivity
        implements SelectStudyTypeFragment.OnFragmentInteractionListener, NewHoonjangFragment.OnFragmentInteractionListener {
    public DatabaseReference mPostReference;
    Intent intent, intentHome, intentMakeQuiz;
    String id, scriptnm, backgroundID, script, studyType = "";
    TextView textview_title, textview_script_1, textview_script_2;
    ImageButton goHome;
    TextView goMakeQuiz;
//    TextView goStudyWord;
//    FirebaseStorage storage;
//    private StorageReference storageReference, dataReference;
    Fragment selectStudyTypeFragment;
    NewHoonjangFragment hoonjangFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readscript);

        intent = getIntent();
        id= intent.getStringExtra("id");
        scriptnm = intent.getStringExtra("scriptnm");
        backgroundID = intent.getStringExtra("background");

        selectStudyTypeFragment = new SelectStudyTypeFragment();

        textview_title = (TextView) findViewById(R.id.textview_title);
        textview_script_1 = (TextView) findViewById(R.id.textview_script_1);
        textview_script_2 = (TextView) findViewById(R.id.textview_script_2);
        goHome = (ImageButton) findViewById(R.id.home);
//        goStudyWord = (TextView) findViewById(R.id.studyWord);
        goMakeQuiz = (TextView) findViewById(R.id.makeQuiz);

        textview_title.setText(scriptnm);

        mPostReference = FirebaseDatabase.getInstance().getReference();

        mPostReference.child("user_list/" + id + "/my_script_list/" + scriptnm + "/time").setValue("2m");
        mPostReference.child("user_list/" + id + "/my_script_list/" + scriptnm + "/word_list/test1/ex").setValue("test1Ex");
        mPostReference.child("user_list/" + id + "/my_script_list/" + scriptnm + "/word_list/test1/meaning").setValue("test1Meaning");
        mPostReference.child("user_list/" + id + "/my_script_list/" + scriptnm + "/word_list/test2/ex").setValue("test2Ex");
        mPostReference.child("user_list/" + id + "/my_script_list/" + scriptnm + "/word_list/test2/meaning").setValue("test2Meaning");
        mPostReference.child("user_list/" + id + "/my_script_list/" + scriptnm + "/word_list/test3/ex").setValue("test3Ex");
        mPostReference.child("user_list/" + id + "/my_script_list/" + scriptnm + "/word_list/test3/meaning").setValue("test3Meaning");

        String coin=mPostReference.child("user_list/" + id + "/coin").getKey();
        int coins=Integer.parseInt(coin);
        coins+=10;
        coin=Integer.toString(coins);
        mPostReference.child("user_list/"+id+"/coin").setValue(coin);


//        storage = FirebaseStorage.getInstance();
//        storageReference = storage.getInstance().getReference();
//        dataReference = storageReference.child("/scripts_background/" + backgroundID);
//        dataReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                Picasso.with(ReadScriptActivity.this)
//                        .load(uri)
//                        .placeholder(R.drawable.bot)
//                        .error(R.drawable.btn_x)
//                        .into(backgroundImage);
//                backgroundImage.setAlpha(0.5f);
//            }
//        });

        mPostReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                script = dataSnapshot.child("script_list/" + scriptnm + "/text").getValue().toString();
                String[] array=script.split("###");
                textview_script_1.setText(array[0]);
                textview_script_2.setText(array[1]);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {            }
        });

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.contentReadScript, selectStudyTypeFragment);
        transaction.commit();

        goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentHome = new Intent(ReadScriptActivity.this, MainActivity.class);
                intentHome.putExtra("id", id);
                startActivity(intentHome);
            }
        });

        goMakeQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentMakeQuiz = new Intent(ReadScriptActivity.this, SelectTypeActivity.class);
                intentMakeQuiz.putExtra("id", id);
                intentMakeQuiz.putExtra("scriptnm", scriptnm);
                intentMakeQuiz.putExtra("background", backgroundID);
                startActivity(intentMakeQuiz);
            }
        });

//        goStudyWord.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            Intent intentStudyWord = new Intent(ReadScriptActivity.this, WordListActivity.class);
//            intentStudyWord.putExtra("scriptnm",scriptnm);
//            intentStudyWord.putExtra("id", id);
//            intentStudyWord.putExtra("background", backgroundID);
//            startActivity(intentStudyWord);
//        }
//    });
}

    public void onStudyTypeInfoSet(String type) {
        studyType = type;

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}