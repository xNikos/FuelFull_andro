package com.rinworks.nikos.fuelfullpaliwoikoszty;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView mRecyclerView = findViewById(R.id.card_recycler);
        mRecyclerView.setHasFixedSize(true); //optymalizacja
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this)); //layout w recycle
        mRecyclerView.setItemAnimator(new DefaultItemAnimator()); //dodaj animacje

        ArrayList<SingleCard> cards = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            cards.add(new SingleCard());
        }
        mRecyclerView.setAdapter(new RecycleAdapter(cards, mRecyclerView));
    }
}
