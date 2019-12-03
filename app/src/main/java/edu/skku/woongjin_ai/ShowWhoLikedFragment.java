package edu.skku.woongjin_ai;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kakao.usermgmt.response.model.User;

import java.util.ArrayList;

public class ShowWhoLikedFragment extends Fragment {
    //마이페이지 - 내가 만든 문제함 - 문제 클릭 - 내 문제 상세정보 프래그먼트 - "n명이 좋아했어요!" 클릭 시 또 다른 프래그먼트로 좋아요 누른 사람 리스트 띄우는 프래그먼트
    //미완성임..

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    ListView likedfriends;
    ArrayList<UserInfo> Uinfos;

    DatabaseReference mPostReference;

    MyQuizActivity activity;

    ShowFriendListAdapter showFriendListAdapter;

    ImageButton close;

    private ShowWhoLikedFragment.OnFragmentInteractionListener mListener;

    public ShowWhoLikedFragment(){

    }

    public static ShowWhoLikedFragment newInstance(String param1, String param2) {
        ShowWhoLikedFragment fragment = new ShowWhoLikedFragment();
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
        final View view = inflater.inflate(R.layout.fragment_wholiked, container, false);
        final Context context = container.getContext();

        close=(ImageButton)view.findViewById(R.id.close);

        mPostReference=FirebaseDatabase.getInstance().getReference();

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.remove(((MyQuizActivity)getActivity()).showWhoLikedFragment);
                fragmentTransaction.commit();
                //activity.onFragmentChange(backType, "noKey", 0);
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
        activity=(MyQuizActivity)getActivity();
        if (context instanceof ShowWhoLikedFragment.OnFragmentInteractionListener) {
            mListener = (ShowWhoLikedFragment.OnFragmentInteractionListener) context;
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
