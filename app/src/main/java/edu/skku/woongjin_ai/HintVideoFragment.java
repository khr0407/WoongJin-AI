package edu.skku.woongjin_ai;

import android.content.Context;
import android.graphics.Camera;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.io.IOException;

public class HintVideoFragment extends Fragment implements SurfaceHolder.Callback {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    String type, path;
    Camera camera;
    boolean isRecording = false, isPlaying = false, hasVideo = false;
    MediaRecorder mediaRecorder = null;
    MediaPlayer mediaPlayer = null;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder = null;
    ImageButton recordButton, recordAgainButton;

    private HintVideoFragment.OnFragmentInteractionListener mListener;

    public HintVideoFragment() {

    }

    public static HintVideoFragment newInstance(String param1, String param2) {
        HintVideoFragment fragment = new HintVideoFragment();
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
        final View view = inflater.inflate(R.layout.fragment_hintvideo, container, false);
        final Context context = container.getContext();

        type = getArguments().getString("type");

        recordButton = (ImageButton) view.findViewById(R.id.record);
        recordAgainButton = (ImageButton) view.findViewById(R.id.watchVideo);
        ImageButton goBackButton = (ImageButton) view.findViewById(R.id.goBack);
        mediaRecorder = new MediaRecorder();
        mediaPlayer = new MediaPlayer();
        surfaceView = (SurfaceView) view.findViewById(R.id.sv);
        camera = new Camera();

        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hasVideo = false;
                if(isRecording == false) {
                    initAudioRecorder();
                    mediaRecorder.start();
                    isRecording = true;
                    recordButton.setImageResource(R.drawable.ic_icons_recording_video);
                    Log.d("hereeeeeeeeee", "stop recording");
                } else {
                    mediaRecorder.stop();
                    isRecording = false;
                    recordButton.setImageResource(R.drawable.ic_icons_take_video);
                    Log.d("hereeeeeeeeee", "stop recording");
                }
                hasVideo = true;
                initVideoRecorder();
                startVideoRecorder();
            }
        });

        recordAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPlaying == false) {
                    try {
                        mediaPlayer.setDataSource(path);
                        if(hasVideo == true) {
                            mediaPlayer.setDisplay(surfaceHolder);
                            mediaPlayer.setOnCompletionListener(myListener);
                        }
                        mediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mediaPlayer.start();
                    isPlaying = true;
                    Log.d("hereeeeeeeeee", "stop playing");
                } else {
                    mediaPlayer.stop();
                    isPlaying = false;
                    Log.d("hereeeeeeeeee", "start playing1");
                }
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                isPlaying = false;
                Log.d("hereeeeeeeeee", "start playing2");
            }
        });

        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                if(type.equals("ox")) {
                    fragmentTransaction.remove(((OXTypeActivity)getActivity()).hintVideoFragment);
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

    MediaPlayer.OnCompletionListener myListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            Log.d("hereeeeeeeee", "start playing3");
        }
    };

    void initVideoRecorder() {
//        camera = Camera.open();

    }

    void initAudioRecorder() {

    }

    void startVideoRecorder() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof HintVideoFragment.OnFragmentInteractionListener) {
            mListener = (HintVideoFragment.OnFragmentInteractionListener) context;
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

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
