package edu.skku.woongjin_ai;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class HintWritingFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private HintWritingFragment.OnFragmentInteractionListener mListener;

    EditText editTextHint;
    String desc = "", type;

    public HintWritingFragment() {

    }

    public static HintWritingFragment newInstance(String param1, String param2) {
        HintWritingFragment fragment = new HintWritingFragment();
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
        final View view = inflater.inflate(R.layout.fragment_hintwriting, container, false);
        final Context context = container.getContext();

        type = getArguments().getString("type");

        editTextHint = (EditText) view.findViewById(R.id.hint);
        ImageButton goBackButton = (ImageButton) view.findViewById(R.id.goBack);

        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                if(type.equals("ox")) {
                    ((OXTypeActivity)getActivity()).checkButton.setImageResource(R.drawable.ic_icons_quiz_complete_inactivate);
                    ((OXTypeActivity)getActivity()).flagD = 0;
                    fragmentTransaction.remove(((OXTypeActivity)getActivity()).hintWritingFragment);
                    fragmentTransaction.commit();
                } else if(type.equals("choice")) {
//                    fragmentTransaction.remove(((ChoiceTypeActivity)getActivity()).hintVideoFragment);
//                    fragmentTransaction.commit();
                } else if(type.equals("shortword")) {
//                    fragmentTransaction.remove(((ShortwordTypeActivity)getActivity()).hintVideoFragment);
//                    fragmentTransaction.commit();
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
        if (context instanceof HintWritingFragment.OnFragmentInteractionListener) {
            mListener = (HintWritingFragment.OnFragmentInteractionListener) context;
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
