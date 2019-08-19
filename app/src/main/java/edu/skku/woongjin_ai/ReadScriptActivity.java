package edu.skku.woongjin_ai;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.module.AppGlideModule;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.io.File;

public class ReadScriptActivity extends AppCompatActivity {
    public DatabaseReference mPostReference;
    Intent intent;
    String userID, title, backgroundID, script;
    TextView textview_title, textview_script;
    ImageView backgroundImage;
    FirebaseStorage storage;
    private StorageReference storageReference, pathReference;
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readscript);

        intent = getIntent();
        userID= intent.getStringExtra("id");
        title = intent.getStringExtra("scriptnm");
        backgroundID = intent.getStringExtra("background");

        textview_title = (TextView) findViewById(R.id.textview_title);
        textview_script = (TextView) findViewById(R.id.textview_script);
        backgroundImage = (ImageView) findViewById(R.id.background);

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
                        script = snapshot.child("text").getValue().toString();
                        textview_script.setText(script);
                        break;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {            }
        });


    }

}
