package edu.skku.woongjin_ai;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private Spinner genderspinner;
    private Spinner yearspinner;
    private Spinner monthspinner;
    private Spinner dayspinner;
    EditText editTextID, editTextNM, editTextPW;
    ArrayList<String> yearList;
    ArrayAdapter<String> yearAdapter;
    ArrayList<String> monthList;
    ArrayAdapter<String> monthAdapter;
    ArrayList<String> dayList;
    ArrayAdapter<String> dayAdapter;
    ArrayList<String> genderList;
    ArrayAdapter<String> genderAdapter;

    String id, nm, pw, coin;
    DatabaseReference mPostReference;
    Intent intent;
    int flag = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button button = (Button) findViewById(R.id.button);
        editTextID = (EditText) findViewById(R.id.editTextID);
        editTextNM = (EditText) findViewById(R.id.editTextNM);
        editTextPW = (EditText) findViewById(R.id.editTextPW);

        yearList = new ArrayList<>();
        yearList.add("2014");
        yearList.add("2013");
        yearList.add("2012");
        yearList.add("2011");
        yearList.add("2010");
        yearList.add("2009");
        yearList.add("2008");
        yearList.add("2007");
        yearAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, yearList);
        yearspinner = (Spinner) findViewById(R.id.year);
        yearspinner.setAdapter(yearAdapter);

        monthList = new ArrayList<>();
        monthList.add("1");
        monthList.add("2");
        monthList.add("3");
        monthList.add("4");
        monthList.add("5");
        monthList.add("6");
        monthList.add("7");
        monthList.add("8");
        monthList.add("9");
        monthList.add("10");
        monthList.add("11");
        monthList.add("12");
        monthAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, monthList);
        monthspinner = (Spinner) findViewById(R.id.month);
        monthspinner.setAdapter(monthAdapter);

        dayList = new ArrayList<>();
        dayList.add("1");
        dayList.add("2");
        dayList.add("3");
        dayList.add("4");
        dayList.add("5");
        dayList.add("6");
        dayList.add("7");
        dayList.add("8");
        dayList.add("9");
        dayList.add("10");
        dayList.add("11");
        dayList.add("12");
        dayList.add("13");
        dayList.add("14");
        dayList.add("15");
        dayList.add("16");
        dayList.add("17");
        dayList.add("18");
        dayList.add("19");
        dayList.add("20");
        dayList.add("21");
        dayList.add("22");
        dayList.add("23");
        dayList.add("24");
        dayList.add("25");
        dayList.add("26");
        dayList.add("27");
        dayList.add("28");
        dayList.add("29");
        dayList.add("30");
        dayList.add("31");
        dayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, dayList);
        dayspinner = (Spinner) findViewById(R.id.day);
        dayspinner.setAdapter(dayAdapter);

        genderList = new ArrayList<>();
        genderList.add("남");
        genderList.add("여");
        genderAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, genderList);
        genderspinner = (Spinner) findViewById(R.id.gender);
        genderspinner.setAdapter(genderAdapter);

        mPostReference = FirebaseDatabase.getInstance().getReference();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = editTextID.getText().toString();
                nm = editTextNM.getText().toString();
                pw = editTextPW.getText().toString();

                if (id.length() == 0 || nm.length() == 0 || pw.length() == 0) { //spinner선택되지 않았을때
                    Toast.makeText(RegisterActivity.this, "Fill all blanks", Toast.LENGTH_SHORT).show();
                } else {
                    final ValueEventListener postListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(flag > -1) {
                                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    String key = snapshot.getKey();
                                    if(id.equals(key)) {
                                        Toast.makeText(RegisterActivity.this, "ID is overlapped", Toast.LENGTH_SHORT).show();
                                        editTextID.setText("");
                                        editTextNM.setText("");
                                        editTextPW.setText("");
                                        flag = 1;
                                        break;
                                    }
                                    else flag = 0;
                                }
                                if(flag == 0) {
                                    flag = -1;
                                    postFirebaseDatabaseUserInfo();

                                    intent = new Intent(RegisterActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {        }
                    };
                    mPostReference.child("user_list").addValueEventListener(postListener);
                }
            }
        });
    }

    private void postFirebaseDatabaseUserInfo() {
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        UserInfo post = new UserInfo(id, nm, pw, coin);
        postValues = post.toMap();
        childUpdates.put("/user_list/" + id + "/", postValues);
        mPostReference.updateChildren(childUpdates);
    }
}
