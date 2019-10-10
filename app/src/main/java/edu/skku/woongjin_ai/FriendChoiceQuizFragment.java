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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FriendChoiceQuizFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private FriendChoiceQuizFragment.OnFragmentInteractionListener mListener;

    String id, scriptnm, question, answer, answer1, answer2, answer3, answer4, uid, star, like, hint, key, ans = "";
    int cnt, flagA1 = 0, flagA2 = 0, flagA3 = 0, flagA4 = 0;
    float starFloat;
    ImageView imageViewS2, imageViewS3, imageViewS4, imageViewS5;
    ImageButton imageButtonScript, imageButtonHint;
    Button imageButtonCheck;
    TextView textViewAns1, textViewAns2, textViewAns3, textViewAns4;

    public FriendChoiceQuizFragment() {

    }

    public static FriendChoiceQuizFragment newInstance(String param1, String param2) {
        FriendChoiceQuizFragment fragment = new FriendChoiceQuizFragment();
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
        final View view = inflater.inflate(R.layout.fragment_friendchoicequiz, container, false);
        final Context context = container.getContext();

        id = getArguments().getString("id");
        scriptnm = getArguments().getString("scriptnm");
        question = getArguments().getString("question");
        answer = getArguments().getString("answer");
        answer1 = getArguments().getString("answer1");
        answer2 = getArguments().getString("answer2");
        answer3 = getArguments().getString("answer3");
        answer4 = getArguments().getString("answer4");
        uid = getArguments().getString("uid");
        star = getArguments().getString("star");
        like = getArguments().getString("like");
        hint = getArguments().getString("desc");
        key = getArguments().getString("key");
        cnt = getArguments().getInt("cnt");

        TextView textViewUid = (TextView) view.findViewById(R.id.uidFriendChoice);
        TextView textViewName = (TextView) view.findViewById(R.id.nameFriendChoice);
        TextView textViewQuestion = (TextView) view.findViewById(R.id.questionFriendChoice);
        imageViewS2 = (ImageView) view.findViewById(R.id.star2);
        imageViewS3 = (ImageView) view.findViewById(R.id.star3);
        imageViewS4 = (ImageView) view.findViewById(R.id.star4);
        imageViewS5 = (ImageView) view.findViewById(R.id.star5);
        imageButtonScript = (ImageButton) view.findViewById(R.id.scriptFriendChoice);
        imageButtonHint = (ImageButton) view.findViewById(R.id.hintFriendChoice);
        imageButtonCheck = (Button) view.findViewById(R.id.checkFriendChoice);
        textViewAns1 = (TextView) view.findViewById(R.id.ans1);
        textViewAns2 = (TextView) view.findViewById(R.id.ans2);
        textViewAns3 = (TextView) view.findViewById(R.id.ans3);
        textViewAns4 = (TextView) view.findViewById(R.id.ans4);

        starFloat = Float.parseFloat(star);

        textViewUid.setText(uid + " 친구가 낸 질문");
        textViewName.setText(scriptnm);
        textViewQuestion.setText(question);
        textViewAns1.setText(answer1);
        textViewAns2.setText(answer2);
        textViewAns3.setText(answer3);
        textViewAns4.setText(answer4);

        textViewAns1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagA1 == 0) {
                    ans = answer1;
                    textViewAns1.setBackgroundResource(R.drawable.ic_icons_selector_correct);
                    textViewAns2.setBackgroundResource(R.drawable.ic_icons_selector_standard);
                    textViewAns3.setBackgroundResource(R.drawable.ic_icons_selector_standard);
                    textViewAns4.setBackgroundResource(R.drawable.ic_icons_selector_standard);
                    flagA1 = 1;
                    flagA2 = flagA3 = flagA4 = 0;
                } else {
                    ans = "";
                    textViewAns1.setBackgroundResource(R.drawable.ic_icons_selector_standard);
                    flagA1 = 0;
                }
            }
        });

        textViewAns2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagA2 == 0){
                    ans = answer2;
                    textViewAns2.setBackgroundResource(R.drawable.ic_icons_selector_correct);
                    textViewAns1.setBackgroundResource(R.drawable.ic_icons_selector_standard);
                    textViewAns3.setBackgroundResource(R.drawable.ic_icons_selector_standard);
                    textViewAns4.setBackgroundResource(R.drawable.ic_icons_selector_standard);
                    flagA2 = 1;
                    flagA1 = flagA3 = flagA4 = 0;
                } else {
                    ans = "";
                    textViewAns2.setBackgroundResource(R.drawable.ic_icons_selector_standard);
                    flagA2 = 0;
                }
            }
        });

        textViewAns3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagA3 == 0){
                    ans = answer3;
                    textViewAns3.setBackgroundResource(R.drawable.ic_icons_selector_correct);
                    textViewAns2.setBackgroundResource(R.drawable.ic_icons_selector_standard);
                    textViewAns1.setBackgroundResource(R.drawable.ic_icons_selector_standard);
                    textViewAns4.setBackgroundResource(R.drawable.ic_icons_selector_standard);
                    flagA3 = 1;
                    flagA2 = flagA1 = flagA4 = 0;
                } else {
                    ans = "";
                    textViewAns3.setBackgroundResource(R.drawable.ic_icons_selector_standard);
                    flagA3 = 0;
                }
            }
        });

        textViewAns4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagA4 == 0){
                    ans = answer4;
                    textViewAns4.setBackgroundResource(R.drawable.ic_icons_selector_correct);
                    textViewAns2.setBackgroundResource(R.drawable.ic_icons_selector_standard);
                    textViewAns3.setBackgroundResource(R.drawable.ic_icons_selector_standard);
                    textViewAns1.setBackgroundResource(R.drawable.ic_icons_selector_standard);
                    flagA4 = 1;
                    flagA2 = flagA3 = flagA1 = 0;
                } else {
                    ans = "";
                    textViewAns4.setBackgroundResource(R.drawable.ic_icons_selector_standard);
                    flagA4 = 0;
                }
            }
        });

        imageButtonCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ans.equals("")) {
                    Toast.makeText(context, "정답을 선택하세요.", Toast.LENGTH_SHORT).show();
                } else {
                    if(ans.equals(answer)) {
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.contentShowScript, ((ShowFriendQuizActivity)getActivity()).correctFriendQuizFragment);
                        Bundle bundle = new Bundle(7);
                        bundle.putString("id", id);
                        bundle.putString("scriptnm", scriptnm);
                        bundle.putString("uid", uid);
                        bundle.putString("star", star);
                        bundle.putString("like", like);
                        bundle.putString("key", key);
                        bundle.putInt("cnt", cnt);
                        ((ShowFriendQuizActivity)getActivity()).correctFriendQuizFragment.setArguments(bundle);
//                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    } else {
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.contentShowScript, ((ShowFriendQuizActivity)getActivity()).wrongFriendQuizFragment);
                        Bundle bundle = new Bundle(1);
                        bundle.putString("id", id);
                        ((ShowFriendQuizActivity)getActivity()).wrongFriendQuizFragment.setArguments(bundle);
//                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                }
            }
        });

        imageButtonHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.contentShowHint, ((ShowFriendQuizActivity)getActivity()).showHintFragment);
                Bundle bundle = new Bundle(1);
                bundle.putString("hint", hint);
                ((ShowFriendQuizActivity)getActivity()).showHintFragment.setArguments(bundle);
//                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        imageButtonScript.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.contentShowScript, ((ShowFriendQuizActivity)getActivity()).showScriptFragment);
                Bundle bundle = new Bundle(2);
                bundle.putString("scriptnm", scriptnm);
                bundle.putString("type", "friend");
                ((ShowFriendQuizActivity)getActivity()).showScriptFragment.setArguments(bundle);
//                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        if(starFloat >= 1.5) {
            imageViewS2.setImageResource(R.drawable.ic_icons_difficulty_star_full);
            if(starFloat >= 2.5) {
                imageViewS3.setImageResource(R.drawable.ic_icons_difficulty_star_full);
                if(starFloat >= 3.5) {
                    imageViewS4.setImageResource(R.drawable.ic_icons_difficulty_star_full);
                    if(starFloat >= 4.5) {
                        imageViewS5.setImageResource(R.drawable.ic_icons_difficulty_star_full);
                    }
                }
            }
        }

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
        if (context instanceof FriendChoiceQuizFragment.OnFragmentInteractionListener) {
            mListener = (FriendChoiceQuizFragment.OnFragmentInteractionListener) context;
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
