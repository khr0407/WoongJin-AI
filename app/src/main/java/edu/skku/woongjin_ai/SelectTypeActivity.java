package edu.skku.woongjin_ai;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
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

public class SelectTypeActivity extends AppCompatActivity implements NewHoonjangFragment.OnFragmentInteractionListener{

    Intent intent, intentHome, intentOX, intentChoice, intentShortword, intentTemplate;
    String id, scriptnm, backgroundID;
    FrameLayout frameOX, frameChoice, frameShortword;
    ImageButton goHome, tmpSave;
    ImageView backgroundImage;
    FirebaseStorage storage;
    private StorageReference storageReference, dataReference;
    DatabaseReference mPostReference;
    NewHoonjangFragment hoonjangFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecttype);

        goHome = (ImageButton) findViewById(R.id.home);
        tmpSave = (ImageButton) findViewById(R.id.save);
        frameOX = (FrameLayout) findViewById(R.id.quiz_ox);
        frameChoice = (FrameLayout) findViewById(R.id.quiz_choice);
        frameShortword = (FrameLayout) findViewById(R.id.quiz_shortword);
        Button showTemplate = (Button) findViewById(R.id.template);
        TextView textViewTitle = (TextView) findViewById(R.id.title);
        backgroundImage = (ImageView) findViewById(R.id.background);

        intent = getIntent();
        id = intent.getStringExtra("id");
        scriptnm = intent.getStringExtra("scriptnm");
        backgroundID = intent.getStringExtra("background");

        textViewTitle.setText("지문 제목: " + scriptnm);

        mPostReference = FirebaseDatabase.getInstance().getReference();

        getFirebaseDatabaseMedalInfo();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getInstance().getReference();
        dataReference = storageReference.child("/scripts_background/" + backgroundID);
        dataReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(SelectTypeActivity.this)
                        .load(uri)
                        .placeholder(R.drawable.bot)
                        .error(R.drawable.btn_x)
                        .into(backgroundImage);
                backgroundImage.setAlpha(0.5f);
            }
        });

        showTemplate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentTemplate = new Intent(SelectTypeActivity.this, TemplateActivity.class);
                intentTemplate.putExtra("id", id);
                intentTemplate.putExtra("scriptnm", scriptnm);
                intentTemplate.putExtra("background", backgroundID);
                startActivity(intentTemplate);
            }
        });

        goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentHome = new Intent(SelectTypeActivity.this, MainActivity.class);
                intentHome.putExtra("id", id);
                startActivity(intentHome);
            }
        });

        tmpSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 임시저장 기능
            }
        });

        frameOX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentOX = new Intent(SelectTypeActivity.this, OXTypeActivity.class);
                intentOX.putExtra("id", id);
                intentOX.putExtra("scriptnm", scriptnm);
                intentOX.putExtra("background", backgroundID);
                startActivity(intentOX);
            }
        });

        frameChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentChoice = new Intent(SelectTypeActivity.this, ChoiceTypeActivity.class);
                intentChoice.putExtra("id", id);
                intentChoice.putExtra("scriptnm", scriptnm);
                intentChoice.putExtra("background", backgroundID);
                startActivity(intentChoice);
            }
        });

        frameShortword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentShortword = new Intent(SelectTypeActivity.this, ShortwordTypeActivity.class);
                intentShortword.putExtra("id", id);
                intentShortword.putExtra("scriptnm", scriptnm);
                intentShortword.putExtra("background", backgroundID);
                startActivity(intentShortword);
            }
        });
    }

    private void getFirebaseDatabaseMedalInfo() {
        mPostReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int MadeCount=0;
                DataSnapshot dataSnapshot1=dataSnapshot.child("user_list/"+id+"my_week_list");
                for(DataSnapshot dataSnapshot2: dataSnapshot1.getChildren()){ //week 껍데기
                    MadeCount+=Integer.parseInt(dataSnapshot2.child("made").getValue().toString());
                }
                Calendar calendar = Calendar.getInstance();
                Date dateS = calendar.getTime();
                String MedalUpdate = new SimpleDateFormat("yyyy-MM-dd").format(dateS);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                hoonjangFragment=new NewHoonjangFragment();
                if(MadeCount==100) {
                    mPostReference.child("user_list/" + id + "/my_medal_list/출제왕").setValue("Lev3##"+MedalUpdate);
                    transaction.replace(R.id.selectMainFrame, hoonjangFragment);
                    Bundle bundle = new Bundle(3);
                    bundle.putString("what", "quiz");
                    bundle.putString("from", "quiz");
                    bundle.putInt("level", 3);
                    hoonjangFragment.setArguments(bundle);
                    transaction.commit();
                }else if(MadeCount==60){
                    mPostReference.child("user_list/" + id + "/my_medal_list/출제왕").setValue("Lev2##"+MedalUpdate);
                    transaction.replace(R.id.selectMainFrame, hoonjangFragment);
                    Bundle bundle = new Bundle(3);
                    bundle.putString("what", "quiz");
                    bundle.putString("from", "quiz");
                    bundle.putInt("level", 2);
                    hoonjangFragment.setArguments(bundle);
                    transaction.commit();
                }else if(MadeCount==30){
                    mPostReference.child("user_list/" + id + "/my_medal_list/출제왕").setValue("Lev1##"+MedalUpdate);
                    transaction.replace(R.id.selectMainFrame, hoonjangFragment);
                    Bundle bundle = new Bundle(3);
                    bundle.putString("what", "quiz");
                    bundle.putString("from", "quiz");
                    bundle.putInt("level", 1);
                    hoonjangFragment.setArguments(bundle);
                    transaction.commit();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
