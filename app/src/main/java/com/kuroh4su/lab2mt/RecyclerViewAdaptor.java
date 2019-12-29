package com.kuroh4su.lab2mt;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerViewAdaptor extends RecyclerView.Adapter<RecyclerViewAdaptor.RecyclerViewHolder> {

    private ArrayList<InformationList.Player> mPlayersList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView mPlayerNickname;
        TextView mPlayerSkill;

        RecyclerViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            mPlayerNickname = itemView.findViewById(R.id.playerNickname);
            mPlayerSkill = itemView.findViewById(R.id.playerSkill);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    RecyclerViewAdaptor(ArrayList<InformationList.Player> playersList) {
        mPlayersList = playersList;
    }

    @Override
    public int getItemCount() {
        return mPlayersList.size();
    }

    @Override
    @NonNull
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_view, viewGroup, false);
        return new RecyclerViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder RecyclerViewHolder, int position) {
        RecyclerViewHolder.mPlayerNickname.setText(mPlayersList.get(position).playerNickname);
        RecyclerViewHolder.mPlayerSkill.setText(String.valueOf(mPlayersList.get(position).playerSkill));
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}