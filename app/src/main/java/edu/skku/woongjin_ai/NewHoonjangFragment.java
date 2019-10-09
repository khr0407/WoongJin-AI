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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NewHoonjangFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    String what;
    int level;

    ImageView hoonjang;
    TextView congratulation;
    DatabaseReference mPostReference;

    private NewHoonjangFragment.OnFragmentInteractionListener mListener;

    public NewHoonjangFragment() {

    }

    public static NewHoonjangFragment newInstance(String param1, String param2) {
        NewHoonjangFragment fragment = new NewHoonjangFragment();
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
        final View view = inflater.inflate(R.layout.fragment_newhoonjang, container, false);
        final Context context = container.getContext();

        congratulation=(TextView)view.findViewById(R.id.congratulation);
        hoonjang=(ImageView)view.findViewById(R.id.hoonjangImage);

        what= getArguments().getString("what");
        level=getArguments().getInt("level");

        congratulation.setText(what+" 레벨 "+level+"달성!");

        String path=what+"_"+level;


        if(what.equals("attend")){
            if(level==1)
                hoonjang.setImageResource(R.drawable.attend_1);
            else if(level==2)
                hoonjang.setImageResource(R.drawable.attend_2);
            else
                hoonjang.setImageResource(R.drawable.attend_3);
        }else if(what.equals("bombmaster")){
            if(level==1)
                hoonjang.setImageResource(R.drawable.bombmaster_1);
            else if(level==2)
                hoonjang.setImageResource(R.drawable.bombmaster_2);
            else
                hoonjang.setImageResource(R.drawable.bombmaster_3);
        }else if(what.equals("bucket")){
            if(level==1)
                hoonjang.setImageResource(R.drawable.bucket_1);
            else if(level==2)
                hoonjang.setImageResource(R.drawable.bucket_2);
            else
                hoonjang.setImageResource(R.drawable.bucket_3);
        }else if(what.equals("quiz")){
            if(level==1)
                hoonjang.setImageResource(R.drawable.quiz_1);
            else if(level==2)
                hoonjang.setImageResource(R.drawable.quiz_2);
            else
                hoonjang.setImageResource(R.drawable.quiz_3);
        }else if(what.equals("quizhunter")){
            if(level==1)
                hoonjang.setImageResource(R.drawable.quizhunter_1);
            else if(level==2)
                hoonjang.setImageResource(R.drawable.quizhunter_2);
            else
                hoonjang.setImageResource(R.drawable.quizhunter_3);
        }else{
            if(level==1)
                hoonjang.setImageResource(R.drawable.read_1);
            else if(level==2)
                hoonjang.setImageResource(R.drawable.read_2);
            else
                hoonjang.setImageResource(R.drawable.read_3);
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
        if (context instanceof NewHoonjangFragment.OnFragmentInteractionListener) {
            mListener = (NewHoonjangFragment.OnFragmentInteractionListener) context;
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
