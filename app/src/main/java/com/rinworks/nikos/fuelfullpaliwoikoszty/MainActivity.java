package com.rinworks.nikos.fuelfullpaliwoikoszty;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    //TODO: Check Dialogs :D
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final FloatingActionButton mFab = findViewById(R.id.fab_main);
        final RecyclerView mRecyclerView = findViewById(R.id.card_recycler);
        mRecyclerView.setHasFixedSize(true); //optymalizacja
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this)); //layout w recycle
        mRecyclerView.setItemAnimator(new DefaultItemAnimator()); //dodaj animacje

        ArrayList<SingleCard> cards = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            cards.add(new SingleCard());
        }
        mRecyclerView.setAdapter(new RecycleAdapter(cards, mRecyclerView));

        //Fab hide show
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy>20 && mFab.isShown() || dy<-20 && mFab.isShown())
                    mFab.hide();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState==mRecyclerView.SCROLL_STATE_IDLE){
                    mFab.show();
                }
                super.onScrollStateChanged(mRecyclerView,newState);
            }
        });

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar snackbar;
                Snackbar.make(mRecyclerView,"Lets test that shit",Snackbar
                        .LENGTH_LONG).show();
            }
        });



    }
}
