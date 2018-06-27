package com.rinworks.nikos.fuelfullpaliwoikoszty;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.constraint.ConstraintLayout;
import android.support.design.internal.NavigationMenu;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.ArrayList;

import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;

public class MainActivity extends AppCompatActivity {
    //private DrawerLayout drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        drawer = findViewById(R.id.drawerLayout);
//
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open_navigation_drawer, R.string.close_navigation_drawer);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();


        final FabSpeedDial mFab = findViewById(R.id.extendedFab); //extended fab init
        final RecyclerView mRecyclerView = findViewById(R.id.card_recycler); //recycle_view init
        mRecyclerView.setHasFixedSize(true); //optymalizacja
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this)); //layout w recycle
        mRecyclerView.setItemAnimator(new DefaultItemAnimator()); //dodaj animacje


        //temp array list
        ArrayList<SingleCard> cards = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            cards.add(new SingleCard());
        }
        mRecyclerView.setAdapter(new RecycleAdapter(cards, mRecyclerView));

        //Fab hide show
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 50 && mFab.isShown() || dy < -50 && mFab.isShown())
                    mFab.hide();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) mFab.show();
                super.onScrollStateChanged(mRecyclerView, newState);
            }
        });
        mFab.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.fab_tankowanie:
                        //Dialog create
                        AlertDialog.Builder popupBuilder = new AlertDialog.Builder(MainActivity.this);
                        View mView = getLayoutInflater().inflate(R.layout.tankowanie_popup, null);
                        TextView tankowanie_title = mView.findViewById(R.id.tankowanie_popup_title);
                        TextView zatankowano = mView.findViewById(R.id.Zatankowano);
                        TextView cenaL = mView.findViewById(R.id.CenaL);
                        TextView przejechano = mView.findViewById(R.id.PrzejechanoT);
                        final EditText zatankowanoV = mView.findViewById(R.id.ZatankowanioVALUE);
                        final EditText CenaLV = mView.findViewById(R.id.CenaLValue);
                        final EditText przejechanoV = mView.findViewById(R.id.PrzejechanoTValue);
                        Button okbtn = mView.findViewById(R.id.tankowanie_ok_btn);
                        Button cancelbtn = mView.findViewById(R.id.tankowanie_cancel_btn);

                        popupBuilder.setView(mView);
                        final AlertDialog dialog = popupBuilder.create();
                        dialog.show();

                        //dialog buttons on click
                        okbtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!zatankowanoV.getText().toString().isEmpty() && !CenaLV.getText().toString
                                        ().isEmpty() && !przejechanoV.getText().toString().isEmpty()) {
                                    //todo:Dodaj do bazy
                                    dialog.dismiss();
                                    Snackbar mSnack = Snackbar.make(findViewById(R.id.card_recycler),
                                            "Dodano do bazy! :)", Snackbar
                                                    .LENGTH_LONG);
                                    mSnack.show();

                                } else {
                                    Toast mToast = Toast.makeText(MainActivity.this, "Proszę wypełnij " +
                                            "wszystkie pola!", Toast.LENGTH_SHORT);
                                    mToast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL,
                                            0, 0);
                                    mToast.show();

                                }
                            }
                        });

                        cancelbtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                Snackbar mSnack = Snackbar.make(findViewById(R.id.card_recycler),
                                        "Anulowano!", Snackbar.LENGTH_LONG);
                                mSnack.show();
                            }
                        });
                        break;
                    //Default
                    default:
                        Toast.makeText(MainActivity.this, "Coś", Toast.LENGTH_SHORT).show();
                }

                return true;
            }

        });


    }
}