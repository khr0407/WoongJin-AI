package edu.skku.woongjin_ai;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class FriendOXQuizFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private FriendOXQuizFragment.OnFragmentInteractionListener mListener;

    String id, scriptnm;
    ImageView imageViewO, imageViewX, imageViewS1, imageViewS2, imageViewS3, imageViewS4, imageViewS5;

    public FriendOXQuizFragment() {

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

        TextView textViewUid = view.findViewById(R.id.uidFriendOX);
        TextView textViewName = view.findViewById(R.id.nameFriendOX);
        TextView textViewQuestion = view.findViewById(R.id.questionFriendOX);


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
