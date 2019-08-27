package edu.skku.woongjin_ai;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
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

import java.util.ArrayList;

public class WordListActivity extends AppCompatActivity{
        //implements ShowScriptFragment.OnFragmentInteractionListener

    DatabaseReference mPostReference;
    Intent intent, intentHome;
    String id, scriptnm, backgroundID;
    ImageView backgroundImage;
    ImageButton scriptButton;
    FirebaseStorage storage;
    ListView mListView;
    ArrayList<String> wordArraylist = new ArrayList();
    //Fragment showScriptFragment;

    private StorageReference storageReference, dataReference;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wordlist);

        intent = getIntent();
        id = intent.getStringExtra("id");
        scriptnm = intent.getStringExtra("scriptnm");
        backgroundID = intent.getStringExtra("background");
        mListView = (ListView)findViewById(R.id.wordlist);
        ImageView imageHome = (ImageView) findViewById(R.id.home);
        TextView title = (TextView) findViewById(R.id.title);
        backgroundImage = (ImageView) findViewById(R.id.background);
        scriptButton = (ImageButton) findViewById(R.id.script);


        title.setText("지문 제목: " + scriptnm);
        //showScriptFragment = new ShowScriptFragment();

        mPostReference = FirebaseDatabase.getInstance().getReference();
        getFirebaseDatabaseWordList();
        //dataSetting();


        storage = FirebaseStorage.getInstance();
        storageReference = storage.getInstance().getReference();
        dataReference = storageReference.child("/scripts_background/" + backgroundID);
        dataReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(WordListActivity.this)
                        .load(uri)
                        .placeholder(R.drawable.bot)
                        .error(R.drawable.btn_x)
                        .into(backgroundImage);
                backgroundImage.setAlpha(0.5f);
            }
        });
/*
        scriptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.wordlistFragment, showScriptFragment);
                Bundle bundle = new Bundle(2);
                bundle.putString("scriptnm", scriptnm);
                showScriptFragment.setArguments(bundle);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
*/
        imageHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentHome = new Intent(WordListActivity.this, MainActivity.class);
                intentHome.putExtra("id", id);
                startActivity(intentHome);
            }
        });
    }

    private void getFirebaseDatabaseWordList(){

        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.child("user_list").getChildren()) {
                    String key = snapshot.getKey();
                    if(id.equals(key))
                    {
                        for(DataSnapshot snapshot1 : snapshot.child("scripts").getChildren()) {
                            String scriptkey = snapshot1.getKey();
                            if (scriptnm.equals(scriptkey)) {
                                for(DataSnapshot snapshot2 : snapshot1.child("word_list").getChildren()) {
                                    WordListAdapter wAdapter = new WordListAdapter();
                                    String wordlistkey = snapshot2.getKey();
                                    wordArraylist.add(wordlistkey);
                                    for(int i = 0 ; i < wordArraylist.size(); i++) {
                                        wAdapter.addItem(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_icons_pen_hand), wordArraylist.get(i), ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_icons_learn), ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_icons_script));
                                    }
                                    mListView.setAdapter(wAdapter);
                                }
                            }
                        }
                    }
                }
                for(DataSnapshot snapshot : dataSnapshot.child("kakaouser_list").getChildren()) {
                    String key = snapshot.getKey();
                    if(id.equals(key))
                    {

                        for(DataSnapshot snapshot1 : snapshot.child("scripts").getChildren()) {
                            String scriptkey = snapshot1.getKey();
                            if (scriptnm.equals(scriptkey)) {
                                for(DataSnapshot snapshot2 : snapshot1.child("word_list").getChildren()) {
                                    WordListAdapter wAdapter = new WordListAdapter();
                                    String wordlistkey = snapshot2.getKey();
                                    wordArraylist.add(wordlistkey);
                                    for(int i = 0 ; i < wordArraylist.size(); i++) {
                                        wAdapter.addItem(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_icons_pen_hand), wordArraylist.get(i), ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_icons_learn), ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_icons_script));
                                    }
                                    mListView.setAdapter(wAdapter);
                                }
                            }
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {            }
        };
        mPostReference.addValueEventListener(postListener);


    }
/*
    private void dataSetting() {

        WordListAdapter wAdapter = new WordListAdapter();
        for(int i = 0 ; i < wordArraylist.size(); i++) {
            Log.d("요기요8", wordArraylist.get(0));
            wAdapter.addItem(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_icons_pen_hand), wordArraylist.get(i), ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_icons_learn), ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_icons_script));
        }
        mListView.setAdapter(wAdapter);
    }
    */
/*
    public void onFragmentInteraction(Uri uri) {

    }
*/
}