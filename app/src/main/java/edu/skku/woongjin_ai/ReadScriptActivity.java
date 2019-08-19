package edu.skku.woongjin_ai;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ReadScriptActivity extends AppCompatActivity {
    public DatabaseReference mPostReference;
    Intent intent;
    TextView textview_title, textview_script_1, textview_script_2;
    String userID, title, backgroundID, script;
    ImageView backgroundImage;
    FirebaseStorage storage;
    private StorageReference storageReference, pathReference;
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readscript);

        intent = getIntent();
        textview_title = (TextView)findViewById(R.id.textview_title);
        textview_script_1 = (TextView) findViewById(R.id.textview_script_1);
        textview_script_2 = (TextView)findViewById(R.id.textview_script_2);
        userID= intent.getStringExtra("id");
        title = intent.getStringExtra("scriptnm");
        backgroundID = intent.getStringExtra("background");

        textview_title = (TextView) findViewById(R.id.textview_title);
        backgroundImage = (ImageView) findViewById(R.id.background);

        title = intent.getStringExtra("scriptnm");

        mPostReference = FirebaseDatabase.getInstance().getReference().child("script_list");
        textview_title.setText(title);

        mPostReference = FirebaseDatabase.getInstance().getReference();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        pathReference = storageReference.child("scripts_background/" + backgroundID);

        Glide.with(this).load(pathReference).into(backgroundImage);

        FirebaseDatabase.getInstance().getReference().child("script_list").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String key = snapshot.getKey();
                    if(key.equals(title)) {
                        String script = snapshot.child("text").getValue().toString();
                        //받아온 텍스트
                        String[] array=script.split("###");
                        // 여기서 받아온 텍스트를 반으로 잘라요
                        textview_script_1.setText(array[0]);
                        textview_script_2.setText(array[1]);
                        break;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {            }
        });


    }

}
