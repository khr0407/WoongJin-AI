package edu.skku.woongjin_ai;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
    String id, bookType, nickname, thisWeek;
    ImageButton homeButton;
    TextView textView;
    public DatabaseReference mPostReference;
    ListView bookListView;
    ArrayList<String> bookArrayList, backgroundArrayList, studiedBookArrayList;
    SelectBookListAdapter selectBookListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectbook);

        intent = getIntent();
        id = intent.getStringExtra("id");
        bookType = intent.getStringExtra("bookType");
        nickname = intent.getStringExtra("nickname");
        thisWeek = intent.getStringExtra("thisWeek");

        intentReadScript = new Intent(SelectBookActivity.this, ReadScriptActivity.class);
        intentReadScript.putExtra("id", id);
        intentReadScript.putExtra("bookType", bookType);
        intentReadScript.putExtra("nickname", nickname);
        intentReadScript.putExtra("thisWeek", thisWeek);

        mPostReference = FirebaseDatabase.getInstance().getReference();

        homeButton = (ImageButton) findViewById(R.id.home);
        bookListView = (ListView) findViewById(R.id.bookList);
        textView = (TextView) findViewById(R.id.selectBook);

        bookArrayList = new ArrayList<String>();
        backgroundArrayList = new ArrayList<String>();
        studiedBookArrayList = new ArrayList<String>();
        selectBookListAdapter = new SelectBookListAdapter();

        getFirebaseDatabaseBookList();

        if(bookType.equals("science")) {
            textView.setText("과학을 선택했구나!\n아래 목록에서 읽고싶은 지문을 선택해줘~");
        } else if(bookType.equals("history")){
            textView.setText("역사를 선택했구나!\n아래 목록에서 읽고싶은 지문을 선택해줘~");
        }else if(bookType.equals("news")){
            textView.setText("어린이 시사를 선택했구나!\n아래 목록에서 읽고싶은 지문을 선택해줘~");
        }else if(bookType.equals("morality")){
            textView.setText("도덕을 선택했구나!\n아래 목록에서 읽고싶은 지문을 선택해줘~");
        }else if(bookType.equals("mistery")){
            textView.setText("미스터리 소설을 선택했구나!\n아래 목록에서 읽고싶은 지문을 선택해줘~");
        }else if(bookType.equals("comics")){
            textView.setText("웃긴 이야기를 선택했구나!\n아래 목록에서 읽고싶은 지문을 선택해줘~");
        }else if(bookType.equals("oldstory")){
            textView.setText("전래동화를 선택했구나!\n아래 목록에서 읽고싶은 지문을 선택해줘~");
        }else if(bookType.equals("greatman")){
            textView.setText("위인전을 선택했구나!\n아래 목록에서 읽고싶은 지문을 선택해줘~");
        }

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
        mPostReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                studiedBookArrayList.clear();
                bookArrayList.clear();
                backgroundArrayList.clear();

                for(DataSnapshot snapshot : dataSnapshot.child("user_list/" + id + "/my_script_list").getChildren()) {
                    String key = snapshot.getKey();
                    studiedBookArrayList.add(key);
                }

                for(DataSnapshot snapshot : dataSnapshot.child("script_list").getChildren()){
                    String type = snapshot.child("type").getValue().toString();
                    if(type.equals(bookType)) {
                        String key = snapshot.getKey();
                        int flag = 0;
                        for(String script : studiedBookArrayList) {
                            if(key.equals(script)) {
                                flag = 1;
                                break;
                            }
                        }
                        if(flag == 0) {
                            String key1 = snapshot.getKey();
                            String background = snapshot.child("background").getValue().toString();
                            String bookname = snapshot.child("book_name").getValue().toString();
                            bookArrayList.add(key1);
                            backgroundArrayList.add(background);
                            selectBookListAdapter.addItem(key1, bookname);
                        }
                    }
                }
                bookListView.setAdapter(selectBookListAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {            }
        });
    }
}
