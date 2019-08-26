package edu.skku.woongjin_ai;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SelectBookActivity extends AppCompatActivity {

    Intent intent, intentHome, intentReadScript;
    String id, bookType;
    ImageButton homeButton;
    TextView textView;
    public DatabaseReference mPostReference;
    ListView bookListView;
    ArrayList<String> bookArrayList, backgroundArrayList;
    ArrayAdapter<String> bookArrayAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectbook);

        intent = getIntent();
        id = intent.getStringExtra("id");
        bookType = intent.getStringExtra("bookType");
        intentReadScript = new Intent(SelectBookActivity.this, ReadScriptActivity.class);
        intentReadScript.putExtra("id", id);
        intentReadScript.putExtra("bookType", bookType);

        mPostReference = FirebaseDatabase.getInstance().getReference();

        homeButton = (ImageButton) findViewById(R.id.home);
        bookListView = (ListView) findViewById(R.id.bookList);
        textView = (TextView) findViewById(R.id.selectBook);

        bookArrayList = new ArrayList<String>();
        bookArrayAdapter = new ArrayAdapter<String>(SelectBookActivity.this, android.R.layout.simple_list_item_1);
        bookListView.setAdapter(bookArrayAdapter);

        if(bookType.equals("science")) {
            textView.setText("과학을 선택했구블발바ㅡ라ㅡㅏㄹ");
        } else if()

        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intentReadScript.putExtra("scriptnm", bookArrayList.get(position));
                intentReadScript.putExtra("background", backgroundArrayList.get(position));
                startActivity(intentReadScript);
            }
        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentHome = new Intent(SelectBookActivity.this, MainActivity.class);
                intentHome.putExtra("id", id);
                startActivity(intentHome);
            }
        });
    }

    private void getFirebaseDatabaseBookList(){
        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bookArrayList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String type = snapshot.child("type").getValue().toString();
                    if(type.equals(bookType)) {
                        String key = snapshot.getKey();
                        String background = snapshot.child("background").getValue().toString();
                        bookArrayList.add(key);
                        backgroundArrayList.add(background);
                    }
                }
                bookArrayAdapter.clear();
                bookArrayAdapter.addAll(bookArrayList);
                bookArrayAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {            }
        };
        mPostReference.child("script_list").addValueEventListener(postListener);
    }
}
