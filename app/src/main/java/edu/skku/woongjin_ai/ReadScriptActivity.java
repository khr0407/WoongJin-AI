package edu.skku.woongjin_ai;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

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
    String title;
    TextView textview_title, textview_script;
    ImageView backgroundImage;
    FirebaseStorage storage;
    private StorageReference storageReference;
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readscript);

        intent = getIntent();
        textview_title = (TextView)findViewById(R.id.textview_title);
        textview_script = (TextView)findViewById(R.id.textview_script);
        backgroundImage = (ImageView) findViewById(R.id.background);

        title = intent.getStringExtra("scriptnm");

        mPostReference = FirebaseDatabase.getInstance().getReference().child("script_list");

        textview_title.setText(title);
        getFirebaseDatabase();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        StorageReference pathReference = storageReference.child("scripts_background/고양이.jpg");

        //TODO storage에서 지문 image 다운 및 background에 적용



    }

    private void getFirebaseDatabase() {
        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String key = snapshot.getKey();
                    if(key.equals(title)) {
                        String script = snapshot.child("text").getValue().toString();
                        textview_script.setText(script);
                        break;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {            }
        };
        mPostReference.addValueEventListener(postListener);
    }
}
