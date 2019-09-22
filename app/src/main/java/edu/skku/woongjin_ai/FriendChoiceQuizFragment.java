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

public class FriendChoiceQuizFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private FriendOXQuizFragment.OnFragmentInteractionListener mListener;

    String id, scriptnm, question, answer, uid, star, like, hint, key, ans = "";
    int cnt, flagAO = 0, flagAX = 0;
    float starFloat;
    ImageView imageO, imageX, imageViewS2, imageViewS3, imageViewS4, imageViewS5;
    ImageButton imageButtonScript, imageButtonHint;
    Button imageButtonCheck;

    public FriendChoiceQuizFragment() {

    }

    public static FriendOXQuizFragment newInstance(String param1, String param2) {
        FriendOXQuizFragment fragment = new FriendOXQuizFragment();
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
        final View view = inflater.inflate(R.layout.fragment_friendoxquiz, container, false);
        final Context context = container.getContext();

        id = getArguments().getString("id");
        scriptnm = getArguments().getString("scriptnm");
        question = getArguments().getString("question");
        answer = getArguments().getString("answer");
        uid = getArguments().getString("uid");
        star = getArguments().getString("star");
        like = getArguments().getString("like");
        hint = getArguments().getString("desc");
        key = getArguments().getString("key");
        cnt = getArguments().getInt("cnt");

        TextView textViewUid = view.findViewById(R.id.uidFriendOX);
        TextView textViewName = view.findViewById(R.id.nameFriendOX);
        TextView textViewQuestion = view.findViewById(R.id.questionFriendOX);
        imageO = (ImageView) view.findViewById(R.id.oFriendOX);
        imageX = (ImageView) view.findViewById(R.id.xFriendOX);
        imageViewS2 = (ImageView) view.findViewById(R.id.star2);
        imageViewS3 = (ImageView) view.findViewById(R.id.star3);
        imageViewS4 = (ImageView) view.findViewById(R.id.star4);
        imageViewS5 = (ImageView) view.findViewById(R.id.star5);
        imageButtonScript = (ImageButton) view.findViewById(R.id.scriptFriendOX);
        imageButtonHint = (ImageButton) view.findViewById(R.id.hintFriendOX);
        imageButtonCheck = (Button) view.findViewById(R.id.checkFriendOX);

        starFloat = Float.parseFloat(star);

        textViewUid.setText(uid + " 친구가 낸 질문");
        textViewName.setText(scriptnm);
        textViewQuestion.setText(question);

        imageButtonCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ans.equals("")) {
                    Toast.makeText(context, "정답을 입력하세요.", Toast.LENGTH_SHORT).show();
                } else {
                    if(ans.equals(answer)) {
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.contentShowScriptOX, ((ShowFriendQuizActivity)getActivity()).correctFriendQuizFragment);
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
                        fragmentTransaction.replace(R.id.contentShowScriptOX, ((ShowFriendQuizActivity)getActivity()).wrongFriendQuizFragment);
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
                fragmentTransaction.replace(R.id.contentShowFriendQuiz, ((ShowFriendQuizActivity)getActivity()).showHintFragment);
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
                fragmentTransaction.replace(R.id.contentShowScriptOX, ((ShowFriendQuizActivity)getActivity()).showScriptFragment);
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

        imageO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagAO == 0) {
                    if(flagAX == 1) {
                        imageX.setImageResource(R.drawable.x_white);
                        flagAX = 0;
                    }
                    ans = "o";
                    imageO.setImageResource(R.drawable.o_orange);
                    flagAO = 1;
                } else {
                    ans = "";
                    imageO.setImageResource(R.drawable.o_white);
                    flagAO = 0;
                }
            }
        });

        imageX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagAX == 0) {
                    if(flagAO == 1) {
                        imageO.setImageResource(R.drawable.o_white);
                        flagAO = 0;
                    }
                    ans = "x";
                    imageX.setImageResource(R.drawable.x_orange);
                    flagAX = 1;
                } else {
                    ans = "";
                    imageX.setImageResource(R.drawable.x_white);
                    flagAX = 0;
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
        if (context instanceof FriendOXQuizFragment.OnFragmentInteractionListener) {
            mListener = (FriendOXQuizFragment.OnFragmentInteractionListener) context;
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
