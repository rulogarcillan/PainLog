package com.pain.log.painlog.negocio;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.pain.log.painlog.R;


public class SplashFragment extends Fragment {


    private int pos;
    ImageView image;
    Button boton;
    TextView text;
    Animation animation;



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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }



    @Override
    public void onResume() {
        super.onResume();
        animation.cancel();
        if (pos== 4) {
            animation.setDuration(1000);
            animation.start();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        animation = AnimationUtils.loadAnimation(getActivity(), R.anim.abc_fade_in);
        View rootView = inflater.inflate(R.layout.splash_fragment, container, false);
        text = (TextView) rootView.findViewById(R.id.textplash);

        if (pos== 4){
            rootView = inflater.inflate(R.layout.splash_fragment2, container, false);
            boton = (Button) rootView.findViewById(R.id.textplash);
        }

        image = (ImageView) rootView.findViewById(R.id.imageSplash);


        switch (pos){
            case 1:
                image.setImageResource(R.drawable.frame11);
                text.setText(R.string.splash1);
                break;
            case 2:
                image.setImageResource(R.drawable.frame22);
                text.setText(R.string.splash2);
                break;
            case 3:
                image.setImageResource(R.drawable.frame33);
                text.setText(R.string.splash3);
                break;
            case 4:


                boton.startAnimation(animation);



                image.setImageResource(R.drawable.frame44);
                boton.setText(R.string.splash4);

                boton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(),MainActivity.class);
                        getActivity().startActivity(intent);
                        getActivity().finish();
                        getActivity().overridePendingTransition(R.anim.fadein,R.anim.fadeout);
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
