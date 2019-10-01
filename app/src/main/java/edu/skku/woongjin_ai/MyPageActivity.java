package edu.skku.woongjin_ai;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import static com.kakao.usermgmt.StringSet.nickname;

public class MyPageActivity extends AppCompatActivity {

    public DatabaseReference mPostReference;
    Intent intent, intentGoHome, intentAddFriend, intent_LikeList, intent_QList, intentHome, intent_Record;
    String id, profileUri;
    Button btnFriendList, btnLikeList, btnQList, btnChangePicture, btnUpload, btnRecord;
    Button logout;
    ImageButton goHome;
    ImageView attendw, readw, quizw, quizhunterw, bombmasterw, bucketw;
    TextView userGrade, userSchool, userName, userCoin, userName1, userGrade1;
    TextView attendd, readd, quizd, quizhunterd, bombmasterd, bucketd;
    TextView textViewCorrectL, textViewCorrectT, textViewLikeL, textViewLikeT, textViewLevelL, textViewLevelT;
    ImageView myFace;
    UserInfo me;
    ArrayList<HoInfo> hoInfos;
    FirebaseStorage storage;
    private StorageReference storageReference, dataReference;
    private static final int REQUEST_CODE = 0;
    private static final String TAG = "MyPageActivity";
    private Uri filePath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        intent = getIntent();
        id = intent.getStringExtra("id");

        mPostReference = FirebaseDatabase.getInstance().getReference();

        ImageButton homeButton = (ImageButton) findViewById(R.id.home);
        btnFriendList = (Button) findViewById(R.id.FriendList);
        btnQList = (Button) findViewById(R.id.QList);
        btnLikeList = (Button) findViewById(R.id.LikeList);
        userName = (TextView) findViewById(R.id.userName);
        userName1=(TextView)findViewById(R.id.userName1);
        userSchool = (TextView) findViewById(R.id.userSchool);
        userGrade = (TextView) findViewById(R.id.userGrade);
        userGrade1 = (TextView) findViewById(R.id.userGrade1);
        userCoin = (TextView) findViewById(R.id.userCoin);
        logout = (Button) findViewById(R.id.logout);
        textViewCorrectL = (TextView) findViewById(R.id.lastCorrectCnt);
        textViewCorrectT = (TextView) findViewById(R.id.thisCorrectCnt);
        textViewLikeL = (TextView) findViewById(R.id.lastLikeCnt);
        textViewLikeT = (TextView) findViewById(R.id.thisLikeCnt);
        textViewLevelL = (TextView) findViewById(R.id.lastLevel);
        textViewLevelT = (TextView) findViewById(R.id.thisLevel);
        btnChangePicture = (Button) findViewById(R.id.changePicture);
        btnUpload = (Button) findViewById(R.id.upload);
        myFace = (ImageView) findViewById(R.id.myFace);
        btnRecord=(Button)findViewById(R.id.record);
        goHome=(ImageButton)findViewById(R.id.home);
        attendw=(ImageView)findViewById(R.id.attend_wang);
        quizw=(ImageView)findViewById(R.id.quiz_wang);
        readw=(ImageView)findViewById(R.id.read_lot_wang);
        quizhunterw=(ImageView)findViewById(R.id.quiz_hunter);
        bombmasterw=(ImageView)findViewById(R.id.bomb_master);
        bucketw=(ImageView)findViewById(R.id.bucket_wang);
        attendd=(TextView)findViewById(R.id.attend_wang_date);
        readd=(TextView)findViewById(R.id.read_lot_wang_date);
        quizd=(TextView)findViewById(R.id.quiz_wang_date);
        quizhunterd=(TextView)findViewById(R.id.quiz_hunter_date);
        bombmasterd=(TextView)findViewById(R.id.bomb_master_date);
        bucketd=(TextView)findViewById(R.id.bucket_wang_date);


        hoInfos = new ArrayList<HoInfo>();

        getFirebaseDatabaseUserInfo();


        goHome.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                intentGoHome=new Intent(MyPageActivity.this, MainActivity.class);
                intentGoHome.putExtra("id", id);
                startActivity(intentGoHome);
            }
        });

        btnRecord.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                intent_Record=new Intent(MyPageActivity.this, MyRecordActivity.class);
                intent_Record.putExtra("id", id);
                startActivity(intent_Record);
            }
        });

        btnFriendList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentAddFriend = new Intent(MyPageActivity.this, ShowFriendActivity.class);
                intentAddFriend.putExtra("id", id);
                startActivity(intentAddFriend);
            }
        });

        btnQList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent_QList = new Intent(MyPageActivity.this, MyQuizActivity.class);
                intent_QList.putExtra("id", id);
                startActivity(intent_QList);
            }
        });

        btnLikeList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                intent_LikeList = new Intent(MyPageActivity.this, LikeQuizActivity.class);
//                intent_LikeList.putExtra("id", id);
//                startActivity(intent_LikeList);
            }
        });

        btnChangePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //업로드
                uploadFile();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManagement.getInstance().requestLogout(new LogoutResponseCallback() { //카카오톡은 매번 로그아웃됨
                    @Override
                    public void onCompleteLogout() {
                        Intent intent = new Intent(MyPageActivity.this, LoginActivity.class);
                        ActivityCompat.finishAffinity(MyPageActivity.this);
                        startActivity(intent);
                    }
                });
            }
        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentHome = new Intent(MyPageActivity.this, MainActivity.class);
                intentHome.putExtra("id", id);
                startActivity(intentHome);
            }
        });
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            filePath = data.getData();
            Log.d(TAG, "uri:" + String.valueOf(filePath));
            try {
                InputStream in = getContentResolver().openInputStream(data.getData());
                Bitmap img = BitmapFactory.decodeStream(in);
                in.close();
                myFace.setImageBitmap(img);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
        }
    }

    //upload the file
    private void uploadFile() {
        //업로드할 파일이 있으면 수행
        if (filePath != null) {
            //업로드 진행 Dialog 보이기
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("업로드중...");
            progressDialog.show();

            //storage
            FirebaseStorage storage = FirebaseStorage.getInstance();

            //Unique한 파일명을 만들자.
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMHH_mmss");
            Date now = new Date();
            String filename = formatter.format(now) + ".png";
            //storage 주소와 폴더 파일명을 지정해 준다.
            StorageReference storageRef = storage.getReferenceFromUrl("gs://woongjin-ai.appspot.com").child("profile/" + filename);
            //올라가거라...
            storageRef.putFile(filePath)
                    //성공시
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss(); //업로드 진행 Dialog 상자 닫기
                            Toast.makeText(getApplicationContext(), "업로드 완료!", Toast.LENGTH_SHORT).show();
                            mPostReference.child("user_list/" + id +"/profile").setValue(filename);
                        }
                    })
                    //실패시
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "업로드 실패!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    //진행중
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            @SuppressWarnings("VisibleForTests") //이걸 넣어 줘야 아랫줄에 에러가 사라진다. 넌 누구냐?
                                    double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            //dialog에 진행률을 퍼센트로 출력해 준다
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "% ...");
                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), "파일을 먼저 선택하세요.", Toast.LENGTH_SHORT).show();
        }
    }


    private void getFirebaseDatabaseUserInfo() {
        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String key = snapshot.getKey();
                    if (key.equals("user_list")) {
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            String key1 = snapshot1.getKey();
                            if (key1.equals(id)) {
                                me = snapshot1.getValue(UserInfo.class);
                                userName.setText(me.name);
                                userName1.setText(me.name+" 학생");
                                userSchool.setText(me.school);
                                userGrade.setText(me.grade + "학년");
                                userGrade1.setText(me.grade + "학년");
                                userCoin.setText(me.coin + " 코인");
                                profileUri=me.profile;
                                if(!profileUri.equals("noimage")) {
                                    storage = FirebaseStorage.getInstance();
                                    storageReference = storage.getInstance().getReference();
                                    dataReference = storageReference.child("/profile/" + profileUri);
                                    dataReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Picasso.with(MyPageActivity.this)
                                                    .load(uri)
                                                    .error(R.drawable.btn_x)
                                                    .into(myFace);
                                        }
                                    });
                                }
                                for(DataSnapshot snapshot2: snapshot1.getChildren()){
                                    String key2=snapshot2.getKey();
                                    if(key2.equals("my_week_list")){
                                        long idx=snapshot2.getChildrenCount();
                                        String This="week"+idx;
                                        String Last="week"+(idx-1);

                                        textViewCorrectT.setText(snapshot2.child(This).child("correct").getValue().toString());
                                        textViewLikeT.setText(snapshot2.child(This).child("like").getValue().toString());
                                        textViewLevelT.setText(snapshot2.child(This).child("level").getValue().toString());

                                        if(idx>=2) {
                                            textViewCorrectL.setText(snapshot2.child(Last).child("correct").getValue().toString());
                                            textViewLikeL.setText(snapshot2.child(Last).child("like").getValue().toString());
                                            textViewLevelL.setText(snapshot2.child(Last).child("level").getValue().toString());
                                        }

                                        break;
                                    }

                                    else if(key2.equals("my_medal_list")){
                                        for(DataSnapshot snapshot3: snapshot2.getChildren()){
                                            String what_wang=snapshot3.getKey(); //출석왕? 무슨왕?
                                            String date=snapshot3.getValue().toString();
                                            switch (what_wang){
                                                case "출석왕":
                                                    attendw.setImageDrawable(getDrawable(R.drawable.ic_hunjang1));
                                                    attendd.setText(date);
                                                    break;
                                                case "다독왕":
                                                    readw.setImageDrawable(getDrawable(R.drawable.ic_hunjang1));
                                                    readd.setText(date);
                                                    break;
                                                case "출제왕":
                                                    quizw.setImageDrawable(getDrawable(R.drawable.ic_hunjang1));
                                                    quizd.setText(date);
                                                    break;
                                                case "문제사냥꾼":
                                                    quizhunterw.setImageDrawable(getDrawable(R.drawable.ic_hunjang1));
                                                    quizhunterd.setText(date);
                                                    break;
                                                case "폭탄마스터":
                                                    bombmasterw.setImageDrawable(getDrawable(R.drawable.ic_hunjang1));
                                                    bombmasterd.setText(date);
                                                    break;
                                                case "협동왕":
                                                    bucketw.setImageDrawable(getDrawable(R.drawable.ic_hunjang1));
                                                    bucketd.setText(date);
                                                    break;
                                                default:
                                                    break;
                                            }
                                        }
                                    }
                                }
                                break;
                            }
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        mPostReference.addValueEventListener(postListener);
    }
}
