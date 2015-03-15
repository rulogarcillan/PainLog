package com.pain.log.painlog.negocio;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pain.log.painlog.R;


public class SplashFragment extends Fragment {


    private int pos;
    ImageView image;
    TextView text;



    public static SplashFragment newInstance() {
        SplashFragment fragment = new SplashFragment();
        return fragment;
    }

    public SplashFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.splash_fragment, container, false);
        image = (ImageView) rootView.findViewById(R.id.imageSplash);
        text = (TextView) rootView.findViewById(R.id.textplash);

        switch (pos){
            case 1:
                image.setImageResource(R.drawable.frame1);
                text.setText(R.string.splash1);
                break;
            case 2:
                image.setImageResource(R.drawable.frame2);
                text.setText(R.string.splash2);
                break;
            case 3:
                image.setImageResource(R.drawable.frame3);
                text.setText(R.string.splash3);
                break;
            case 4:
                image.setImageResource(R.drawable.frame2);
                text.setText(R.string.splash4);

                text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(),MainActivity.class);
                        getActivity().startActivity(intent);
                        getActivity().finish();
                    }
                });
                break;
            default:
                break;
        }

        return rootView;
    }

    public void setPos(int pos){
        this.pos = pos;
    }


}
