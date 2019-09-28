package edu.skku.woongjin_ai;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CorrectFriendQuizFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private CorrectFriendQuizFragment.OnFragmentInteractionListener mListener;

    String id, scriptnm, uid, star, like, key;
    Button buttonSubmit;
    ImageView imageViewS1, imageViewS2, imageViewS3, imageViewS4, imageViewS5, imageViewThumb;
    int cnt, starInt = 0, flagS1 = 0, flagS2 = 0, flagS3 = 0, flagS4 = 0, flagS5 = 0, flagT = 0;
    float oldLevel;
    public DatabaseReference mPostReference;
    ArrayList<HoInfo> hoInfos;
    int hoNum = 0, flagK = 0;

    public CorrectFriendQuizFragment() {

    }

    public static CorrectFriendQuizFragment newInstance(String param1, String param2) {
        CorrectFriendQuizFragment fragment = new CorrectFriendQuizFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_correctfriendquiz, container, false);
        final Context context = container.getContext();

        id = getArguments().getString("id");
        scriptnm = getArguments().getString("scriptnm");
        uid = getArguments().getString("uid");
        star = getArguments().getString("star");
        like = getArguments().getString("like");
        key = getArguments().getString("key");
        cnt = getArguments().getInt("cnt");

        mPostReference = FirebaseDatabase.getInstance().getReference();

        TextView textViewID = (TextView) view.findViewById(R.id.textCheckCorrect);
        buttonSubmit = (Button) view.findViewById(R.id.submitCheckCorrect);
        imageViewS1 = (ImageView) view.findViewById(R.id.star1);
        imageViewS2 = (ImageView) view.findViewById(R.id.star2);
        imageViewS3 = (ImageView) view.findViewById(R.id.star3);
        imageViewS4 = (ImageView) view.findViewById(R.id.star4);
        imageViewS5 = (ImageView) view.findViewById(R.id.star5);
        imageViewThumb = (ImageView) view.findViewById(R.id.thumbCheckCorrect);

        hoInfos = new ArrayList<HoInfo>();

        oldLevel = Float.parseFloat(star);

        textViewID.setText("정답이야 " + id + "! 수고했어^^");

        mPostReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String key = snapshot.getKey();
                    if(key.equals("kakaouser_list") || key.equals("user_list")) {
                        for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                            String key1 = snapshot1.getKey();
                            if(key1.equals(uid)) {
                                if(key.equals("kakaouser_list")) flagK = 1;
                                for(DataSnapshot snapshot2 : snapshot1.child("ho_list").getChildren()) {
                                    HoInfo hoInfo = snapshot2.getValue(HoInfo.class);
                                    hoInfos.add(hoInfo);
                                }
                                hoNum = hoInfos.size();
                                break;
                            }
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {                        }
        });


        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(starInt == 0) {
                    Toast.makeText(context, "난이도를 선택해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    float oldLevel = hoInfos.get(hoNum-1).level;
                    int oldCnt = hoInfos.get(hoNum-1).cnt;
                    float newLevel = (oldLevel*oldCnt + starInt) / (oldCnt + 1);
                    if(flagK == 0) {
                        mPostReference.child("user_list/" + uid + "/ho_list/ho" + hoNum + "/level").setValue(newLevel);
                        mPostReference.child("user_list/" + uid + "/ho_list/ho" + hoNum + "/cnt").setValue(oldCnt + 1);
                    } else {
                        mPostReference.child("kakaouser_list/" + uid + "/ho_list/ho" + hoNum + "/level").setValue(newLevel);
                        mPostReference.child("kakaouser_list/" + uid + "/ho_list/ho" + hoNum + "/cnt").setValue(oldCnt + 1);
                    }

                    oldLevel = Integer.parseInt(star);
                    newLevel = (oldLevel*cnt + starInt) / (cnt + 1);
                    mPostReference.child("quiz_list/" + scriptnm + "/type1/" + key + "/star").setValue(Float.toString(newLevel));
                    mPostReference.child("quiz_list/" + scriptnm + "/type1/" + key + "/cnt").setValue(cnt+1);

                    Toast.makeText(context, "제출이 완료되었습니다.", Toast.LENGTH_SHORT).show();

                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.remove(((ShowFriendQuizActivity)getActivity()).correctFriendQuizFragment);
                    fragmentTransaction.remove(((ShowFriendQuizActivity)getActivity()).friendOXQuizFragment);
                    fragmentTransaction.commit();
                }
            }
        });

        imageViewThumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagT == 0) {
                    Toast.makeText(context, "좋아요를 눌렀습니다", Toast.LENGTH_SHORT).show();

                    int oldLike = hoInfos.get(hoNum-1).like;
                    if(flagK == 0) mPostReference.child("user_list/" + uid + "/ho_list/ho" + hoNum + "/like").setValue(oldLike+1);
                    else mPostReference.child("kakaouser_list/" + uid + "/ho_list/ho" + hoNum + "/like").setValue(oldLike+1);

                    int newLike = Integer.parseInt(like);
                    newLike++;
                    like = Integer.toString(newLike);
                    mPostReference.child("quiz_list/" + scriptnm + "/type1/" + key + "/like/").setValue(like);

                    flagT = 1;
                } else {
                    Toast.makeText(context, "이미 좋아요를 눌렀습니다", Toast.LENGTH_SHORT).show();
                }
            }
        });

        imageViewS1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagS1 == 0) {
                    starInt = 1;
                    imageViewS1.setImageResource(R.drawable.ic_icons_difficulty_star_full);
                    flagS1 = 1;
                } else {
                    starInt = 0;
                    imageViewS1.setImageResource(R.drawable.ic_icons_difficulty_star_empty);
                    imageViewS2.setImageResource(R.drawable.ic_icons_difficulty_star_empty);
                    imageViewS3.setImageResource(R.drawable.ic_icons_difficulty_star_empty);
                    imageViewS4.setImageResource(R.drawable.ic_icons_difficulty_star_empty);
                    imageViewS5.setImageResource(R.drawable.ic_icons_difficulty_star_empty);
                    flagS1 = 0;
                    flagS2 = 0;
                    flagS3 = 0;
                    flagS4 = 0;
                    flagS5 = 0;
                }
            }
        });

        imageViewS2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagS2 == 0) {
                    starInt = 2;
                    imageViewS1.setImageResource(R.drawable.ic_icons_difficulty_star_full);
                    imageViewS2.setImageResource(R.drawable.ic_icons_difficulty_star_full);
                    flagS1 = 1;
                    flagS2 = 1;
                } else {
                    starInt = 0;
                    imageViewS1.setImageResource(R.drawable.ic_icons_difficulty_star_empty);
                    imageViewS2.setImageResource(R.drawable.ic_icons_difficulty_star_empty);
                    imageViewS3.setImageResource(R.drawable.ic_icons_difficulty_star_empty);
                    imageViewS4.setImageResource(R.drawable.ic_icons_difficulty_star_empty);
                    imageViewS5.setImageResource(R.drawable.ic_icons_difficulty_star_empty);
                    flagS1 = 0;
                    flagS2 = 0;
                    flagS3 = 0;
                    flagS4 = 0;
                    flagS5 = 0;
                }
            }
        });

        imageViewS3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagS3 == 0) {
                    starInt = 3;
                    imageViewS1.setImageResource(R.drawable.ic_icons_difficulty_star_full);
                    imageViewS2.setImageResource(R.drawable.ic_icons_difficulty_star_full);
                    imageViewS3.setImageResource(R.drawable.ic_icons_difficulty_star_full);
                    flagS1 = 1;
                    flagS2 = 1;
                    flagS3 = 1;
                } else {
                    starInt = 0;
                    imageViewS1.setImageResource(R.drawable.ic_icons_difficulty_star_empty);
                    imageViewS2.setImageResource(R.drawable.ic_icons_difficulty_star_empty);
                    imageViewS3.setImageResource(R.drawable.ic_icons_difficulty_star_empty);
                    imageViewS4.setImageResource(R.drawable.ic_icons_difficulty_star_empty);
                    imageViewS5.setImageResource(R.drawable.ic_icons_difficulty_star_empty);
                    flagS1 = 0;
                    flagS2 = 0;
                    flagS3 = 0;
                    flagS4 = 0;
                    flagS5 = 0;
                }
            }
        });

        imageViewS4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagS4 == 0) {
                    starInt = 4;
                    imageViewS1.setImageResource(R.drawable.ic_icons_difficulty_star_full);
                    imageViewS2.setImageResource(R.drawable.ic_icons_difficulty_star_full);
                    imageViewS3.setImageResource(R.drawable.ic_icons_difficulty_star_full);
                    imageViewS4.setImageResource(R.drawable.ic_icons_difficulty_star_full);
                    flagS1 = 1;
                    flagS2 = 1;
                    flagS3 = 1;
                    flagS4 = 1;
                } else {
                    starInt = 0;
                    imageViewS1.setImageResource(R.drawable.ic_icons_difficulty_star_empty);
                    imageViewS2.setImageResource(R.drawable.ic_icons_difficulty_star_empty);
                    imageViewS3.setImageResource(R.drawable.ic_icons_difficulty_star_empty);
                    imageViewS4.setImageResource(R.drawable.ic_icons_difficulty_star_empty);
                    imageViewS5.setImageResource(R.drawable.ic_icons_difficulty_star_empty);
                    flagS1 = 0;
                    flagS2 = 0;
                    flagS3 = 0;
                    flagS4 = 0;
                    flagS5 = 0;
                }
            }
        });

        imageViewS5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagS5 == 0) {
                    starInt = 5;
                    imageViewS1.setImageResource(R.drawable.ic_icons_difficulty_star_full);
                    imageViewS2.setImageResource(R.drawable.ic_icons_difficulty_star_full);
                    imageViewS3.setImageResource(R.drawable.ic_icons_difficulty_star_full);
                    imageViewS4.setImageResource(R.drawable.ic_icons_difficulty_star_full);
                    imageViewS5.setImageResource(R.drawable.ic_icons_difficulty_star_full);
                    flagS1 = 1;
                    flagS2 = 1;
                    flagS3 = 1;
                    flagS4 = 1;
                    flagS5 = 1;
                } else {
                    starInt = 0;
                    imageViewS1.setImageResource(R.drawable.ic_icons_difficulty_star_empty);
                    imageViewS2.setImageResource(R.drawable.ic_icons_difficulty_star_empty);
                    imageViewS3.setImageResource(R.drawable.ic_icons_difficulty_star_empty);
                    imageViewS4.setImageResource(R.drawable.ic_icons_difficulty_star_empty);
                    imageViewS5.setImageResource(R.drawable.ic_icons_difficulty_star_empty);
                    flagS1 = 0;
                    flagS2 = 0;
                    flagS3 = 0;
                    flagS4 = 0;
                    flagS5 = 0;
                }
            }
        });

        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CorrectFriendQuizFragment.OnFragmentInteractionListener) {
            mListener = (CorrectFriendQuizFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}