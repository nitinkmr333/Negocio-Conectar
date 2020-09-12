package com.nc.rockstar.negocioconectar;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ViewFlipper;

public class homeActivity extends Fragment {
    View myView;

    ViewFlipper v_flipper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.layout_home, container, false);
        return myView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        int images[] = {R.drawable.download1,R.drawable.download2,R.drawable.download4};
        v_flipper = getActivity().findViewById(R.id.v_flipper);

        for(int image:images){
            flipperImages(image);
        }
    }

    public void flipperImages(int image){
        ImageView imageView = new ImageView(getActivity());
        imageView.setBackgroundResource(image);
        v_flipper.addView(imageView);
        v_flipper.setFlipInterval(4000);
        v_flipper.setAutoStart(true);
        //animation
        v_flipper.setInAnimation(getActivity(),android.R.anim.slide_in_left);
        v_flipper.setOutAnimation(getActivity(),android.R.anim.slide_out_right);
    }


}
