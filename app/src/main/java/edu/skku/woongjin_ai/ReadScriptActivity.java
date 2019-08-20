package edu.skku.woongjin_ai;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class ReadScriptActivity extends AppCompatActivity {
    public DatabaseReference mPostReference;
    Intent intent, intentnextPage;
    String title;
    TextView textview_title, textview_script;
    Button nextPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readscript);

        intent = getIntent();
        title = intent.getStringExtra("title");
        textview_title = (TextView) findViewById(R.id.textview_title);
        textview_script = (TextView) findViewById(R.id.textview_script);
        nextPage = (Button) findViewById(R.id.nextPage);
        title = intent.getStringExtra("scriptnm");

        mPostReference = FirebaseDatabase.getInstance().getReference().child("script_list");
        textview_title.setText(title);
        getFirebaseDatabase();

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
