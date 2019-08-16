package edu.skku.woongjin_ai;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private static final int SEARCH_ADDRESS_ACTIVITY = 10000;
    int use_id, use_nickname, use_searchaddress;
    private Spinner yearspinner;
    private Spinner monthspinner;
    private Spinner dayspinner;
    private Spinner genderspinner;
    EditText editTextID, editTextPW, editTextName, editTextNickname;
    Button search_address, search_school, btn_register;
    Button check_id, check_nickname;
    ArrayList<String> yearList;ArrayAdapter<String> yearAdapter;
    ArrayList<String> monthList;ArrayAdapter<String> monthAdapter;
    ArrayList<String> dayList;ArrayAdapter<String> dayAdapter;
    ArrayList<String> genderList;ArrayAdapter<String> genderAdapter;

    EditText et_address, et_address_detail;
    EditText et_school_detail;

    String id, pw, name, nickname, coin;
    String year, month, day, birth, gender;
    String address1, address2;
    String school;

    DatabaseReference mPostReference;
    Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        use_id = 0;
        use_nickname = 0; //check Nickname before register
        use_searchaddress = 0;

        editTextID = (EditText) findViewById(R.id.editTextID);
        editTextPW = (EditText) findViewById(R.id.editTextPW);
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextNickname = (EditText)findViewById(R.id.editTextNickname);
        search_address = (Button) findViewById(R.id.search_address);
        btn_register = (Button) findViewById((R.id.btn_register));
        check_id = (Button)findViewById(R.id.check_id);
        check_nickname = (Button)findViewById(R.id.check_nickname);
        et_address = findViewById(R.id.et_address);
        et_address_detail = findViewById(R.id.et_address_detail);
        et_school_detail = findViewById(R.id.et_school_detail);

        yearList = new ArrayList<>();
        yearList.add("2014");yearList.add("2013");yearList.add("2012");yearList.add("2011");yearList.add("2010");
        yearList.add("2009");yearList.add("2008");yearList.add("2007");
        yearAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, yearList);
        yearspinner = (Spinner) findViewById(R.id.year);
        yearspinner.setAdapter(yearAdapter);
        yearspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                year = (String)yearAdapter.getItem(i);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        monthList = new ArrayList<>();
        monthList.add("1");monthList.add("2");monthList.add("3");monthList.add("4");monthList.add("5");
        monthList.add("6");monthList.add("7");monthList.add("8");monthList.add("9");monthList.add("10");
        monthList.add("11");monthList.add("12");
        monthAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, monthList);
        monthspinner = (Spinner) findViewById(R.id.month);
        monthspinner.setAdapter(monthAdapter);
        monthspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                month = (String)monthAdapter.getItem(i);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        dayList = new ArrayList<>();
        dayList.add("1");dayList.add("2");dayList.add("3");dayList.add("4");dayList.add("5");dayList.add("6");dayList.add("7");dayList.add("8");dayList.add("9");dayList.add("10");
        dayList.add("11");dayList.add("12");dayList.add("13");dayList.add("14");dayList.add("15");dayList.add("16");dayList.add("17");dayList.add("18");dayList.add("19");dayList.add("20");
        dayList.add("21");dayList.add("22");dayList.add("23");dayList.add("24");dayList.add("25");dayList.add("26");dayList.add("27");dayList.add("28");dayList.add("29");dayList.add("30");dayList.add("31");
        dayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, dayList);
        dayspinner = (Spinner) findViewById(R.id.day);
        dayspinner.setAdapter(dayAdapter);
        dayspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                day = (String)dayAdapter.getItem(i);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        genderList = new ArrayList<>();
        genderList.add("남");
        genderList.add("여");
        genderAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, genderList);
        genderspinner = (Spinner) findViewById(R.id.gender);
        genderspinner.setAdapter(genderAdapter);
        genderspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                gender = (String)genderAdapter.getItem(i);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        mPostReference = FirebaseDatabase.getInstance().getReference();

        check_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextID.getText().toString().equals("") || TextUtils.isEmpty(editTextID.getText().toString())) {
                    Toast.makeText(RegisterActivity.this, "사용할 아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                if (onlyNumCheck(editTextID.getText().toString()) == true) {
                    Toast.makeText(RegisterActivity.this, "숫자로만 구성된 ID는 사용이 불가능합니다.", Toast.LENGTH_SHORT).show();
                }
                else {
                    mPostReference.addListenerForSingleValueEvent(checkIDRegister);
                }
            }
        });

        check_nickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextNickname.getText().toString().equals("") || TextUtils.isEmpty(editTextNickname.getText().toString())) {
                    Toast.makeText(RegisterActivity.this, "사용할 닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else {
                    mPostReference.addListenerForSingleValueEvent(checkNicknameRegister);
                }
            }
        });

        search_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterActivity.this, WebViewActivity.class);
                startActivityForResult(i, SEARCH_ADDRESS_ACTIVITY);
                use_searchaddress = 1;
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = editTextID.getText().toString();
                pw = editTextPW.getText().toString();
                name = editTextName.getText().toString();
                nickname = editTextNickname.getText().toString();
                birth = year +"-"+ month + "-" +day;
                address1 = et_address.getText().toString();
                address2 = et_address_detail.getText().toString();
                school = et_school_detail.getText().toString();
                if ((use_id == 0 || use_nickname == 0) && use_searchaddress == 0) {
                    Toast.makeText(RegisterActivity.this, "중복체크 및 주소검색을 먼저 해주세요.", Toast.LENGTH_SHORT).show();
                }
                else if (use_id == 0 || use_nickname == 0) {
                    Toast.makeText(RegisterActivity.this, "ID 체크 및 닉네임 체크를 먼저 해주세요.", Toast.LENGTH_SHORT).show();
                }
                else if (use_searchaddress == 0) {
                    Toast.makeText(RegisterActivity.this, "주소검색을 먼저 해주세요.", Toast.LENGTH_SHORT).show();
                }
                if (id.length() == 0 || pw.length() == 0 || name.length() == 0 || nickname.length() == 0 || birth.length() == 0
                        || gender.length() == 0 || address1.length() == 0 || address2.length() == 0 || school.length() == 0) {
                    Toast.makeText(RegisterActivity.this, "모든 칸을 채워주세요.", Toast.LENGTH_SHORT).show();
                }
                if (spaceCheck(id) == true || spaceCheck(pw) == true || spaceCheck(name) == true || spaceCheck(birth) == true
                        || spaceCheck(gender) == true || spaceCheck(address1) == true || spaceCheck(address2) == true || spaceCheck(school) == true) {
                    Toast.makeText(RegisterActivity.this, "공백으로만 구성된 칸은 존재할 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
                else {
                    postFirebaseDatabaseUserInfo();
                    intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void postFirebaseDatabaseUserInfo() {
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        UserInfo post = new UserInfo(id, pw, name, nickname, address1 + " " + address2, school, gender, birth, "100");
        postValues = post.toMap();
        childUpdates.put("/user_list/" + id + "/", postValues);
        mPostReference.updateChildren(childUpdates);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode, resultCode, intent);
        switch(requestCode){
            case SEARCH_ADDRESS_ACTIVITY:
                if(resultCode == RESULT_OK){
                    String data = intent.getExtras().getString("data");
                    if (data != null)
                        et_address.setText(data);
                }
                break;
        }
    }

    private ValueEventListener checkIDRegister = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Iterator<DataSnapshot> child = dataSnapshot.child("user_list").getChildren().iterator();
            while (child.hasNext()) {
                if (editTextID.getText().toString().equals(child.next().getKey())) {
                    Toast.makeText(getApplicationContext(), "이미 존재하는 ID입니다.", Toast.LENGTH_SHORT).show();
                    mPostReference.removeEventListener(this);
                    return;
                }
            }
            Toast.makeText(getApplicationContext(), "사용할 수 있는 ID입니다.", Toast.LENGTH_SHORT).show();
            use_id = 1;
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    };

    private ValueEventListener checkNicknameRegister = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Iterator<DataSnapshot> child = dataSnapshot.child("kakaouser_list").getChildren().iterator();
            while (child.hasNext()) {
                if (editTextNickname.getText().toString().equals(child.next().child("nickname").getValue())) {
                    Toast.makeText(getApplicationContext(), "이미 존재하는 닉네임입니다.", Toast.LENGTH_SHORT).show();
                    mPostReference.removeEventListener(this);
                    return;
                }
            }
            Iterator<DataSnapshot> child1 = dataSnapshot.child("user_list").getChildren().iterator();
            while (child1.hasNext()) {
                if (editTextNickname.getText().toString().equals(child1.next().child("nickname").getValue())) {
                    Toast.makeText(getApplicationContext(), "이미 존재하는 닉네임입니다.", Toast.LENGTH_SHORT).show();
                    mPostReference.removeEventListener(this);
                    return;
                }
            }
            Toast.makeText(getApplicationContext(), "사용할 수 있는 닉네임입니다.", Toast.LENGTH_SHORT).show();
            use_nickname = 1;
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    };

    public boolean onlyNumCheck(String spaceCheck) {
        for (int i = 0 ; i < spaceCheck.length() ; i++) {
            if (spaceCheck.charAt(i) == '1' || spaceCheck.charAt(i) == '2' || spaceCheck.charAt(i) == '3' || spaceCheck.charAt(i) == '4' || spaceCheck.charAt(i) == '5'
                    || spaceCheck.charAt(i) == '6' || spaceCheck.charAt(i) == '7' || spaceCheck.charAt(i) == '8' || spaceCheck.charAt(i) == '9' || spaceCheck.charAt(i) == '0') {
                continue;
            }
            else {
                return false;
            }
        }
        return true;
    }
    public boolean spaceCheck(String spaceCheck) {
        for (int i = 0 ; i < spaceCheck.length() ; i++) {
            if (spaceCheck.charAt(i) == ' ')
                continue;
            else if (spaceCheck.charAt(i) != ' ') {
                return false;
            }
        }
        return true;
    }
}