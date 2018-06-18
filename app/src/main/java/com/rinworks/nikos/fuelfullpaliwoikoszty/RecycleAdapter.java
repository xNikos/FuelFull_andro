package com.rinworks.nikos.fuelfullpaliwoikoszty;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class RecycleAdapter extends RecyclerView.Adapter {

    @NonNull
    private ArrayList<SingleCard> mSingleCard = new ArrayList<>(); //żródło danych
    private RecyclerView mRecycleView;
    private class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView vZatankowano;
        public TextView vZaplacono;
        public TextView vPrzejechano;
        public TextView vSpalanie;

        public MyViewHolder(View pItem) {
            super(pItem);
            vZatankowano = pItem.findViewById(R.id.cardOption1Value);
            vZaplacono = pItem.findViewById(R.id.cardOption2Value);
            vPrzejechano = pItem.findViewById(R.id.cardOption3Value);
            vSpalanie = pItem.findViewById(R.id.cardOption4Value);
        }
    }

    public RecycleAdapter(ArrayList<SingleCard> pCards, RecyclerView pRecycleView) {
        mSingleCard = pCards;
        mRecycleView = pRecycleView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycle_view_card, viewGroup, false);
                return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int i) {
        SingleCard card = mSingleCard.get(i);
        ((MyViewHolder) viewHolder).vZatankowano.setText(card.getvZatankowano());
        ((MyViewHolder) viewHolder).vZaplacono.setText(card.getvZaplacono());
        ((MyViewHolder) viewHolder).vPrzejechano.setText(card.getvPrzejechano());
        ((MyViewHolder) viewHolder).vSpalanie.setText(card.getvSpalanie());
    }

    @Override
    public int getItemCount() {
        return mSingleCard.size();
    }
}
