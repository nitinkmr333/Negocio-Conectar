package com.nc.rockstar.negocioconectar;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class chatAdapter extends RecyclerView.Adapter<chatAdapter.MyChatHolder> {

    private ChatMessage model;
    private List<ads> moviesList;

    public class MyChatHolder extends RecyclerView.ViewHolder {

        TextView leftText,rightText;

        public MyChatHolder(View itemView) {
            super(itemView);

            leftText = (TextView) itemView.findViewById(R.id.leftText);
            rightText = (TextView) itemView.findViewById(R.id.rightText);
        }
    }

    public chatAdapter(ChatMessage model) {
        this.model = model;
    }


    @NonNull
    @Override
    public MyChatHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.msglist, viewGroup, false);

        return new MyChatHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyChatHolder holder, int i) {
        if (model.getMsgUser().equals("user")) {
            holder.rightText.setText(model.getMsgText());

            holder.rightText.setVisibility(View.VISIBLE);
            holder.leftText.setVisibility(View.GONE);
        }
        else {
            holder.leftText.setText(model.getMsgText());

            holder.rightText.setVisibility(View.GONE);
            holder.leftText.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
