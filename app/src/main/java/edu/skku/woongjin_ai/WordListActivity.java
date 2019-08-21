package edu.skku.woongjin_ai;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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

public class WordListActivity extends AppCompatActivity {

    ListView wordlistView;
    ArrayAdapter<String> wordlistadapter;
    ArrayList<String> wordlist;
    String userID, title, backgroundID;
    Intent intent, intentHome, intentlearn, intentscript;
    ImageButton goHome, learn, script;
    ImageView backgroundImage;
    FirebaseStorage storage;

    //private StorageReference storageReference, dataReference;
    public DatabaseReference mPostReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_wordlist);

        intent = getIntent();
        mPostReference = FirebaseDatabase.getInstance().getReference();
        backgroundImage = (ImageView) findViewById(R.id.background);
        goHome = (ImageButton) findViewById(R.id.home);
        userID= intent.getStringExtra("id");
        title = intent.getStringExtra("scriptnm");
        backgroundID = intent.getStringExtra("background");
        wordlist = new ArrayList<>();
        wordlistView = (ListView) this.findViewById(R.id.wordlist);

        mPostReference = FirebaseDatabase.getInstance().getReference();

        storage = FirebaseStorage.getInstance();
        //storageReference = storage.getInstance().getReference();
        //dataReference = storageReference.child("/scripts_background/" + backgroundID);
        //dataReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            //@Override
            //public void onSuccess(Uri uri) {
           //     Picasso.with(WordListActivity.this)
          //              .load(uri)
           //             .placeholder(R.drawable.bot)
           //             .error(R.drawable.btn_x)
           //             .into(backgroundImage);
           //     backgroundImage.setAlpha(0.5f);
           // }
       // });
/*
        goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentHome = new Intent(WordListActivity.this, MainActivity.class);
                intentHome.putExtra("id", userID);
                startActivity(intentHome);
            }
        });
*/
        //CustomAdapter adapter = new CustomAdapter(this,0, wordlist);
        //adapter.notifyDataSetChanged();

        getFirebaseDatabaseWordList();

    }
/*
    private class CustomAdapter extends ArrayAdapter<String> {
        private ArrayList<String> wordlist;

        public CustomAdapter(Context context, int textViewResourceId, ArrayList<String> objects) {
            super(context, textViewResourceId, objects);
            this.wordlist = objects;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.listview_item, null);
            }

            ImageView imageView = (ImageView) v.findViewById(R.id.imageView);
            TextView textView = (TextView)v.findViewById(R.id.textView);
            textView.setText(wordlist.get(position));

            final String text = wordlist.get(position);

            learn = (ImageButton)v.findViewById(R.id.learn);
            script = (ImageButton)v.findViewById(R.id.script);

            learn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intentlearn = new Intent(WordListActivity.this, learnActivity.class);
                    intentlearn.putExtra("scriptnm", title);
                    startActivity(intentlearn);
                }
            });

            script.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intentscript = new Intent(WordListActivity.this, scriptActivity.class);
                    intentscript.putExtra("scriptnm", title);
                    startActivity(intentscript);
                }
            });
        return v;
        }
    }
*/
    private void getFirebaseDatabaseWordList(){

        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String key = snapshot.getKey();
                    if(userID.equals(key))
                    {
                        for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                            String scriptkey = snapshot1.getKey();
                            if (title.equals(scriptkey)) {
                                for(DataSnapshot snapshot2 : snapshot.getChildren()) {
                                    String wordlistkey = snapshot2.getKey();
                                    if(wordlist.equals(wordlistkey))
                                    {
                                        wordlist.add(wordlistkey);
                                    }
                                    wordlistadapter.addAll(wordlist);
                                    wordlistView.setAdapter(wordlistadapter);
                                    }
                                }
                            }
                        }

                    }
                }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {            }
        };
        mPostReference.child("user_list").addValueEventListener(postListener);
    }
}