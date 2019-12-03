package edu.skku.woongjin_ai;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/*
from MainActivity
메인페이지에서 게임나라 선택 후 폭탄 게임 / 퀴즈 버킷 챌린지 선택하기
 */

public class MainGameTypeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    Button bombgameButton, quizbucketButton;
    Intent intentBombGame, intentQuizbucket;
    String id, nickname, thisWeek;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    private MainGameTypeFragment.OnFragmentInteractionListener mListener;

    public MainGameTypeFragment() {

    }

    public static MainGameTypeFragment newInstance(String param1, String param2) {
        MainGameTypeFragment fragment = new MainGameTypeFragment();
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
        final View view = inflater.inflate(R.layout.fragment_maingametype, container, false);
        final Context context = container.getContext();

        id = getArguments().getString("id");
        nickname = getArguments().getString("nickname");
        thisWeek = getArguments().getString("thisWeek");

        bombgameButton = (Button) view.findViewById(R.id.me);
        quizbucketButton = (Button) view.findViewById(R.id.friend);

        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        // 폭탄 게임 버튼 이벤트
        bombgameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentBombGame = new Intent(getActivity(), NationGameActivity.class);
                intentBombGame.putExtra("id", id);
                intentBombGame.putExtra("nickname", nickname);

                fragmentTransaction.remove(((MainActivity)getActivity()).mainGameTypeFragment);
                fragmentTransaction.commit();
                startActivity(intentBombGame);
            }
        });

        // 퀴즈 버킷 챌린지 버튼 이벤트
        quizbucketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                intentQuizbucket = new Intent(getActivity(), QuizbucketMain.class);
                intentQuizbucket.putExtra("id", id);
                intentQuizbucket.putExtra("nickname", nickname);
                intentQuizbucket.putExtra("thisWeek", thisWeek);

                fragmentTransaction.remove(((MainActivity)getActivity()).mainGameTypeFragment);
                fragmentTransaction.commit();
                startActivity(intentQuizbucket);
                */
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
        if (context instanceof MainGameTypeFragment.OnFragmentInteractionListener) {
            mListener = (MainGameTypeFragment.OnFragmentInteractionListener) context;
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
