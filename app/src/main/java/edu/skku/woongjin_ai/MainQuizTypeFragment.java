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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainQuizTypeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    Button meButton, friendButton;
    Intent intent;
    String id, nickname, thisWeek;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    private MainQuizTypeFragment.OnFragmentInteractionListener mListener;

    public MainQuizTypeFragment() {

    }

    public static MainQuizTypeFragment newInstance(String param1, String param2) {
        MainQuizTypeFragment fragment = new MainQuizTypeFragment();
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
        final View view = inflater.inflate(R.layout.fragment_mainquiztype, container, false);
        final Context context = container.getContext();

        id = getArguments().getString("id");
        nickname = getArguments().getString("nickname");
        thisWeek = getArguments().getString("thisWeek");

        intent = new Intent(getActivity(), NationQuizActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("nickname", nickname);
        intent.putExtra("thisWeek", thisWeek);

        meButton = (Button) view.findViewById(R.id.me);
        friendButton = (Button) view.findViewById(R.id.friend);

        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        meButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("quizType", "me");
                fragmentTransaction.remove(((MainActivity)getActivity()).mainQuizTypeFragment);
                fragmentTransaction.commit();
                startActivity(intent);
            }
        });

        friendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("quizType", "friend");
                fragmentTransaction.remove(((MainActivity)getActivity()).mainQuizTypeFragment);
                fragmentTransaction.commit();
                startActivity(intent);
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
        if (context instanceof MainQuizTypeFragment.OnFragmentInteractionListener) {
            mListener = (MainQuizTypeFragment.OnFragmentInteractionListener) context;
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
