package edu.skku.woongjin_ai;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

import org.w3c.dom.Text;

import java.io.File;

public class ReadScriptActivity extends AppCompatActivity
        implements SelectStudyTypeFragment.OnFragmentInteractionListener {
    public DatabaseReference mPostReference;
    Intent intent, intentHome;
    String userID, title, backgroundID, script, studyType = "";
    TextView textview_title, textview_script_1, textview_script_2;
    ImageView backgroundImage;
    ImageButton goHome, tmpSave, goStudyWord;
    FirebaseStorage storage;
    private StorageReference storageReference, dataReference;
    Fragment selectStudyTypeFragment;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readscript);

        intent = getIntent();
        userID= intent.getStringExtra("id");
        title = intent.getStringExtra("scriptnm");
        backgroundID = intent.getStringExtra("background");

        selectStudyTypeFragment = new SelectStudyTypeFragment();

        textview_title = (TextView) findViewById(R.id.textview_title);
        textview_script_1 = (TextView) findViewById(R.id.textview_script_1);
        textview_script_2 = (TextView) findViewById(R.id.textview_script_2);
        backgroundImage = (ImageView) findViewById(R.id.background);
        goHome = (ImageButton) findViewById(R.id.home);
        tmpSave = (ImageButton) findViewById(R.id.save);
        goStudyWord = (ImageButton) findViewById(R.id.studyWord);

        textview_title.setText(title);

        mPostReference = FirebaseDatabase.getInstance().getReference();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getInstance().getReference();
        dataReference = storageReference.child("/scripts_background/" + backgroundID);
        dataReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(ReadScriptActivity.this)
                        .load(uri)
                        .placeholder(R.drawable.bot)
                        .error(R.drawable.btn_x)
                        .into(backgroundImage);
                backgroundImage.setAlpha(0.5f);
            }
        });

        FirebaseDatabase.getInstance().getReference().child("script_list").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String key = snapshot.getKey();
                    if(key.equals(title)) {
                        script = snapshot.child("text").getValue().toString();
                        String[] array=script.split("###");
                        textview_script_1.setText(array[0]);
                        textview_script_2.setText(array[1]);
                        break;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {            }
        });

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.contentReadScript, selectStudyTypeFragment);
//        Bundle bundle = new Bundle(3);
//        bundle.putString("userID", userID);
//        bundle.putString("scriptnm", script);
//        bundle.putString("backgroundID", backgroundID);
//        selectStudyTypeFragment.setArguments(bundle);
        transaction.commit();

        goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentHome = new Intent(ReadScriptActivity.this, MainActivity.class);
                intentHome.putExtra("id", userID);
                startActivity(intentHome);
            }
        });

        tmpSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 임시저장 기능
            }
        });

        goStudyWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 이미지 바꾸기
                //TODO 단어 공부하기 intent
            }
        });
    }

    public void onStudyTypeInfoSet(String type) {
        studyType = type;
        Log.d("hereeeeeeeeeeeeee", studyType);
        //TODO 받아온 type value로 소리내어 읽기/표시하며 읽기 나누어 구현
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
