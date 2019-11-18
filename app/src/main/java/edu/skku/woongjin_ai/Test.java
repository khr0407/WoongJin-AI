package edu.skku.woongjin_ai;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kakao.kakaolink.v2.KakaoLinkResponse;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.message.template.LinkObject;
import com.kakao.message.template.TextTemplate;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;
import com.kakao.util.helper.log.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

public class Test extends AppCompatActivity {

    private DatabaseReference mPostReference, sPostReference, gPostReference;

    String id_key, nickname_key;

    Intent intent;
    String f1,f2,f3,f4;
    String script;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        intent = getIntent();
        id_key = intent.getStringExtra("id");
        nickname_key = intent.getStringExtra("name");
        f1 = intent.getStringExtra("friend1");
        f2 = intent.getStringExtra("friend2");
        f3 = intent.getStringExtra("friend3");
        f4 = intent.getStringExtra("friend4");
        script = intent.getStringExtra("script");
        Toast.makeText(getApplicationContext(), nickname_key + f1 + f2+f3+f4+script, Toast.LENGTH_SHORT).show();

    }

}