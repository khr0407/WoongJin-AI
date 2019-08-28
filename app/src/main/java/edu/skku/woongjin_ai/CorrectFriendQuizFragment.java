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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CorrectFriendQuizFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private CorrectFriendQuizFragment.OnFragmentInteractionListener mListener;

    String id;
    Button buttonSubmit;
    ImageView imageViewS1, imageViewS2, imageViewS3, imageViewS4, imageViewS5, imageViewThumb;
    int star = 0, flagS1 = 0, flagS2 = 0, flagS3 = 0, flagS4 = 0, flagS5 = 0, flagT = 0;

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

        TextView textViewID = (TextView) view.findViewById(R.id.textCheckCorrect);
        buttonSubmit = (Button) view.findViewById(R.id.submitCheckCorrect);
        imageViewS1 = (ImageView) view.findViewById(R.id.star1);
        imageViewS2 = (ImageView) view.findViewById(R.id.star2);
        imageViewS3 = (ImageView) view.findViewById(R.id.star3);
        imageViewS4 = (ImageView) view.findViewById(R.id.star4);
        imageViewS5 = (ImageView) view.findViewById(R.id.star5);
        imageViewThumb = (ImageView) view.findViewById(R.id.thumbCheckCorrect);

        textViewID.setText("정답이야 " + id + "! 수고했어^^");

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(star == 0) {
                    Toast.makeText(context, "난이도를 선택해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    //TODO 난이도 계산

                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.remove(((ShowFriendQuizActivity)getActivity()).correctFriendQuizFragment);
                    fragmentTransaction.commit();
                }
            }
        });

        imageViewThumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagT == 0) {
                    Toast.makeText(context, "좋아요를 눌렀습니다", Toast.LENGTH_SHORT).show();
                    //TODO 좋아요 계산


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
                    star = 1;
                    imageViewS1.setImageResource(R.drawable.ic_icons_difficulty_star_full);
                    flagS1 = 1;
                } else {
                    star = 0;
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
                    star = 2;
                    imageViewS1.setImageResource(R.drawable.ic_icons_difficulty_star_full);
                    imageViewS2.setImageResource(R.drawable.ic_icons_difficulty_star_full);
                    flagS1 = 1;
                    flagS2 = 1;
                } else {
                    star = 0;
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
                    star = 3;
                    imageViewS1.setImageResource(R.drawable.ic_icons_difficulty_star_full);
                    imageViewS2.setImageResource(R.drawable.ic_icons_difficulty_star_full);
                    imageViewS3.setImageResource(R.drawable.ic_icons_difficulty_star_full);
                    flagS1 = 1;
                    flagS2 = 1;
                    flagS3 = 1;
                } else {
                    star = 0;
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
                    star = 4;
                    imageViewS1.setImageResource(R.drawable.ic_icons_difficulty_star_full);
                    imageViewS2.setImageResource(R.drawable.ic_icons_difficulty_star_full);
                    imageViewS3.setImageResource(R.drawable.ic_icons_difficulty_star_full);
                    imageViewS4.setImageResource(R.drawable.ic_icons_difficulty_star_full);
                    flagS1 = 1;
                    flagS2 = 1;
                    flagS3 = 1;
                    flagS4 = 1;
                } else {
                    star = 0;
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
                    star = 5;
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
                    star = 0;
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
