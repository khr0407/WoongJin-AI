package edu.skku.woongjin_ai;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethod;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kakao.auth.ErrorCode;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {
    SessionCallback callback;
    EditText editTextID, editTextPW;
    String id, pw;
    Button buttonLogin, buttonRegister;
    DatabaseReference mPostReference;
    Intent intent;
    int flag = 0;
    String savedKakao;
    Intent intent_kakaoregister;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        callback = new SessionCallback();
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        editTextID = (EditText) findViewById(R.id.editTextID);
        editTextPW = (EditText) findViewById(R.id.editTextPW);

        mPostReference = FirebaseDatabase.getInstance().getReference();
        intent_kakaoregister = new Intent(LoginActivity.this, KakaoRegisterActivity.class);

        if (!isLoggedIn()) //카카오톡 로그인이 되어있지 않을 경우
            Session.getCurrentSession().addCallback(callback);
        else { //카카오톡 로그인이 되어있을 경우
            File file = getBaseContext().getFileStreamPath("memos.txt");
            String data = null;
            if (file.exists()) {
                FileInputStream fis = null;
                StringBuffer buffer = new StringBuffer();
                try {
                    fis = openFileInput("memos.txt");
                    BufferedReader iReader = new BufferedReader(new InputStreamReader((fis)));
                    data = iReader.readLine();
                    iReader.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Intent intent_main = new Intent(LoginActivity.this, MainActivity.class);
            intent_main.putExtra("id", data);
            startActivity(intent_main);
            finish();
        }

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = editTextID.getText().toString();
                pw = editTextPW.getText().toString();

                if(id.length() == 0 || pw.length() == 0) {
                    Toast.makeText(LoginActivity.this, "Fill all blanks", Toast.LENGTH_SHORT).show();
                } else {
                    final ValueEventListener postListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String key = snapshot.getKey();
                                if(id.equals(key)) {
                                    UserInfo get = snapshot.getValue(UserInfo.class);
                                    String password = get.pw;
                                    if(pw.equals(password)) {
                                        flag = 1;
                                        intent = new Intent(LoginActivity.this, MainActivity.class);
                                        intent.putExtra("id", id);
                                        startActivity(intent);
                                    }
                                }
                            }
                            if(flag == 0) {
                                Toast.makeText(LoginActivity.this, "Wrong login", Toast.LENGTH_SHORT).show();
                                editTextID.setText("");
                                editTextPW.setText("");
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {         }
                    };
                    mPostReference.child("user_list").addValueEventListener(postListener);
                }
            }
        });
    }
    public boolean isLoggedIn() {
        return !Session.getCurrentSession().isClosed();
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) { //간편로그인시 호출
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private class SessionCallback implements ISessionCallback {
        @Override
        public void onSessionOpened() {
            UserManagement.getInstance().requestMe(new MeResponseCallback() {
                @Override
                public void onFailure(ErrorResult errorResult) {
                    String message = "사용자 정보를 얻어오는 데 실패하였습니다 + " + errorResult;
                    Logger.d(message);

                    ErrorCode result = ErrorCode.valueOf(errorResult.getErrorCode());
                    if (result == ErrorCode.CLIENT_ERROR_CODE) {
                        finish();
                    } else {
                        //redirectMainActivity();
                    }
                }

                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                }

                @Override
                public void onNotSignedUp() {
                }

                @Override
                public void onSuccess(UserProfile userProfile) { //로그인 성공시, 사용자의 정보 리턴
                    savedKakao = userProfile.getId() + "";
                    FileOutputStream fos = null;
                    try {
                        fos = openFileOutput("memos.txt", Context.MODE_PRIVATE);
                        fos.write(savedKakao.getBytes());
                        fos.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //Log.e("UserProfile", userProfile.toString());
                    mPostReference.addListenerForSingleValueEvent(checkIDRegister);
                    Log.e("userProfile","" + userProfile.getId());
                    Log.d("myLog", "userProfile" + userProfile.getId());
                    Log.d("myLog", "userProfile" + userProfile.getNickname());
                    Log.d("myLog", "userProfile" + userProfile.getThumbnailImagePath());
                    //postFirebaseDatabase(true);

                    intent_kakaoregister.putExtra("id", savedKakao);
                    Toast.makeText(getApplicationContext(), "카카오톡 계정으로 로그인 되었습니다", Toast.LENGTH_SHORT).show();
                    startActivity(intent_kakaoregister);
                    finish();
                }
            });
        }
        @Override
        public void onSessionOpenFailed(KakaoException exception) { //세선 연결 실패시
            Toast.makeText(getApplicationContext(), "세션 연결 실패", Toast.LENGTH_SHORT).show();
        }
    }
    private ValueEventListener checkIDRegister = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for (DataSnapshot ds : dataSnapshot.child("kakaouser_list").getChildren()) {
                String key = ds.getKey();
                if (savedKakao.equals(key)) {
                    //Toast.makeText(getApplicationContext(), "카카오 계정으로 가입되어 있는 ID입니다.", Toast.LENGTH_SHORT).show();
                    Intent intent_main = new Intent(LoginActivity.this, MainActivity.class);
                    intent_main.putExtra("id", savedKakao);
                    startActivity(intent_main);
                    finish();
                    return;
                }
            }
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    };
}