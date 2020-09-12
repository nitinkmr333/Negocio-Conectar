package com.nc.rockstar.negocioconectar;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

public class infoActivity extends Fragment {
    View myView;

    private TextView infoText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.layout_info, container, false);
        return myView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        //infoText = (TextView) getActivity().findViewById(R.id.info);
        //infoText.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
    }

}
