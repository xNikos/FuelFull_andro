package com.rinworks.nikos.fuelfullpaliwoikoszty;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.design.internal.NavigationMenu;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
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
import android.text.InputFilter;
import android.text.Spanned;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    public static final int PICK_IMAGE = 1;
    SwitchCompat switchCompat;
    DataProccessor dataProccessor = new DataProccessor(this); //sharedPreferencesClass




    //Nav drawer selected fragments
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_all_expenses:
                Intent intent = getIntent();
                finish();
                startActivity(intent);
                //TODO: Ogarnąć to fragmentami + FAB jako osobna klasa?!
                break;
            case R.id.nav_fuel_expenses:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,
                        new tankowanieFragment()).commit();

                break;
            case R.id.nav_repair_expenses:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,
                        new naprawaFragment()).commit();
                break;
            case R.id.nav_notifications:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,
                        new przypomnienieFragment()).commit();
                break;
            case R.id.about_author:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,
                        new aboutApkFragment()).commit();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //white/black theme
        if (DataProccessor.getBool("Theme")) {
            setTheme(R.style.AppThemeDark);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //inicjalizacja apki

        switchCompat = findViewById(R.id.switcher);
        NavigationView navigationView = findViewById(R.id.NavigationView);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_all_expenses); //Domyślne podświetlone
        Menu menu = navigationView.getMenu();
        MenuItem item = menu.findItem(R.id.nav_theme);
        View header = navigationView.getHeaderView(0);
        Button addIMG = header.findViewById(R.id.nav_header_add_photo);
        View nav_header= header.findViewById(R.id.nav_header);
        View actionToogleView = MenuItemCompat.getActionView(item);

        switchCompat = actionToogleView.findViewById(R.id.switcher);
        switchCompat.setChecked(DataProccessor.getBool("Theme"));
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Toast.makeText(MainActivity.this, "Ciemny motyw: " + isChecked, Toast.LENGTH_SHORT).show();
                toogleTheme(isChecked);
            }
        });

        //Title Bar
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawerLayout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open_navigation_drawer, R.string.close_navigation_drawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

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


        //Add photo
        addIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");
                Intent chooserIntent = Intent.createChooser(pickIntent, "Wybierz aplikację " +
                        "galerii:");
                startActivityForResult(chooserIntent, PICK_IMAGE);
            }
        });

        //Check if foto
        if (DataProccessor.getStr("NavIMG")!="Empty") {
            Bitmap bitmap = Base64.decode(DataProccessor.getStr("NavIMG"));
            BitmapDrawable bg = new BitmapDrawable(getResources(), bitmap);
            nav_header.setBackground(bg);
            addIMG.setScaleX(10);
            addIMG.setScaleY(10);
            addIMG.setAlpha(0);

        }

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

        //starting popup
        //Dialog create
        if (!DataProccessor.getBool("CarAdded?")) {
        AlertDialog.Builder popupBuilder = new AlertDialog.Builder(MainActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.add_car_popup, null);
        TextView addCar_title = mView.findViewById(R.id.addCar_popupTitle);
        final TextView marka = mView.findViewById(R.id.marka);
        TextView model = mView.findViewById(R.id.model);
        TextView rocznik = mView.findViewById(R.id.rocznik);
        final EditText markaV = mView.findViewById(R.id.markaValue);
        final EditText modelV = mView.findViewById(R.id.modelValue);
        final EditText rocznikV = mView.findViewById(R.id.rocznikValue);
        rocznikV.setFilters(new InputFilter[]{new DecimalDigitsInputFilter
                (4, 0)});
        Button okbtn = mView.findViewById(R.id.tankowanie_ok_btn);

        popupBuilder.setView(mView);
        final AlertDialog dialog = popupBuilder.create();
        dialog.show();
        dialog.setCancelable(false);

        //dialog buttons on click
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!markaV.getText().toString().isEmpty() && !modelV.getText().toString
                        ().isEmpty() && !rocznikV.getText().toString().isEmpty()) {
                    //todo:Dodaj do bazy
                    if(rocznikV.getText().toString().length()<4)
                    {
                        Toast mToast = Toast.makeText(MainActivity.this, "Aż taki z niego " +
                                "staruszek?! :D", Toast
                                .LENGTH_SHORT);
                        mToast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL,
                                0, 0);
                        mToast.show();
                    }
                    else {
                    dialog.dismiss();
                    Snackbar mSnack = Snackbar.make(findViewById(R.id.card_recycler),
                            "Dodano do bazy! :)", Snackbar
                                    .LENGTH_LONG);
                    DataProccessor.setBool("CarAdded?",true);
                    mSnack.show();}
                } else {
                    Toast mToast = Toast.makeText(MainActivity.this, "Proszę wypełnij " +
                            "wszystkie pola!", Toast.LENGTH_SHORT);
                    mToast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL,
                            0, 0);
                    mToast.show();

                }
            }
        });}


        mFab.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    //Tankowanie
                    case R.id.fab_tankowanie:
                        //Dialog create
                        AlertDialog.Builder popupBuilder = new AlertDialog.Builder(MainActivity.this);
                        View mView = getLayoutInflater().inflate(R.layout.tankowanie_popup, null);
                        TextView tankowanie_title = mView.findViewById(R.id.tankowanie_popup_title);
                        TextView zatankowano = mView.findViewById(R.id.Zatankowano);
                        TextView cenaL = mView.findViewById(R.id.CenaL);
                        TextView przejechano = mView.findViewById(R.id.PrzejechanoT);
                        final EditText zatankowanoV = mView.findViewById(R.id.ZatankowanioVALUE);
                        zatankowanoV.setFilters(new InputFilter[]{new DecimalDigitsInputFilter
                                (2, 2)});
                        final EditText CenaLV = mView.findViewById(R.id.CenaLValue);
                        CenaLV.setFilters(new InputFilter[]{new DecimalDigitsInputFilter
                                (1, 2)});
                        final EditText przejechanoV = mView.findViewById(R.id.PrzejechanoTValue);
                        przejechanoV.setFilters(new InputFilter[]{new DecimalDigitsInputFilter
                                (3, 2)});
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
                    //Przypomnienie
                    case R.id.fab_przypomnienie:
                        //Dialog create
                        AlertDialog.Builder popupBuilderNotification = new AlertDialog.Builder
                                (MainActivity.this);
                        View mViewNotification = getLayoutInflater().inflate(R.layout.przypomnienie_popup,
                                null);
                        TextView przypomnienie_title = mViewNotification.findViewById(R.id.przypomnienie_popup_title);
                        TextView tytul = mViewNotification.findViewById(R.id.Tytuł);
                        TextView tresc = mViewNotification.findViewById(R.id.Tresc);
                        TextView kiedy = mViewNotification.findViewById(R.id.Kiedy);
                        final EditText tytulV = mViewNotification.findViewById(R.id.TytulValue);
                        final EditText trescV = mViewNotification.findViewById(R.id.TrescValue);
                        final EditText kiedyV = mViewNotification.findViewById(R.id.KiedyV);
                        okbtn = mViewNotification.findViewById(R.id.tankowanie_ok_btn);
                        cancelbtn = mViewNotification.findViewById(R.id.tankowanie_cancel_btn);

                        popupBuilderNotification.setView(mViewNotification);
                        final AlertDialog dialogNotification = popupBuilderNotification.create();
                        dialogNotification.show();

                        okbtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!trescV.getText().toString().isEmpty() && !tytulV.getText()
                                        .toString().isEmpty() && !kiedyV.getText().toString().isEmpty()) {
                                    //todo:Dodaj do bazy
                                    dialogNotification.dismiss();
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
                                dialogNotification.dismiss();
                                Snackbar mSnack = Snackbar.make(findViewById(R.id.card_recycler),
                                        "Anulowano!", Snackbar.LENGTH_LONG);
                                mSnack.show();
                            }
                        });
                        break;
                    //Naprawa
                    case R.id.fab_naprawa:
                        //Dialog create
                        AlertDialog.Builder popupBuilderRepair = new AlertDialog.Builder
                                (MainActivity.this);
                        View mViewRepair = getLayoutInflater().inflate(R.layout.naprawa_popup,
                                null);
                        TextView naprawa_title = mViewRepair.findViewById(R.id.naprawa_popup_title);
                        TextView Zaplacono = mViewRepair.findViewById(R.id.Zaplacono);
                        TextView Naprawiono = mViewRepair.findViewById(R.id.Naprawiono);
                        final EditText zaplaconoV = mViewRepair.findViewById(R.id.ZaplaconoValue);
                        zaplaconoV.setFilters(new InputFilter[]{new DecimalDigitsInputFilter
                                (4, 2)});
                        final EditText naprawionoV = mViewRepair.findViewById(R.id.naprawionoVALUE);
                        okbtn = mViewRepair.findViewById(R.id.tankowanie_ok_btn);
                        cancelbtn = mViewRepair.findViewById(R.id.tankowanie_cancel_btn);

                        popupBuilderRepair.setView(mViewRepair);
                        final AlertDialog dialogRepair = popupBuilderRepair.create();
                        dialogRepair.show();

                        okbtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!zaplaconoV.getText().toString().isEmpty() && !naprawionoV.getText()
                                        .toString
                                                ().isEmpty()) {
                                    //todo:Dodaj do bazy
                                    dialogRepair.dismiss();
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
                                dialogRepair.dismiss();
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

        //Image add override

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        NavigationView navigationView = findViewById(R.id.NavigationView);
        View header = navigationView.getHeaderView(0);
        View nav_header= header.findViewById(R.id.nav_header);
        Button addIMG = header.findViewById(R.id.nav_header_add_photo);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData
                () != null) {
            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                BitmapDrawable bg = new BitmapDrawable(getResources(),bitmap);
                nav_header.setBackground(bg);
                DataProccessor.setStr("NavIMG",Base64.encode(bitmap));
                addIMG.setScaleX(10);
                addIMG.setScaleY(10);
                addIMG.setAlpha(0);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void toogleTheme(boolean darkTheme) {
        DataProccessor.setBool("Theme", darkTheme);
        //restart activity
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

}
