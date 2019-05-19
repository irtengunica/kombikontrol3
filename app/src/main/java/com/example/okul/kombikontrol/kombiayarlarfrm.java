package com.example.okul.kombikontrol;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Handler;


public class kombiayarlarfrm extends ActionBarActivity {
    SharedPreferences preferences;
    public static String URL = "http://turulay.com/kombiisim553.php";//Bilgisayarýn IP adresi
    String CihazID;
    String saatsec1;
    String derecesec1;
    String saatsec2;
    String derecesec2;
    String saatsec3;
    String derecesec3;
    String saatsec4;
    String derecesec4;
    String saatsec5;
    String derecesec5;
    String saatsec6;
    String derecesec6;
    String tolarans;
    String durum="0";
    String saat="00";
    String dakika="00";
    String deger222;
    int degeral;
    int degeralond;
    String saataldeger;
    int saatkarsilastir;
    String calismamodu;
    String gunyaziyla;
    final Context context = this;
    boolean internetBaglantisiVarMi() {

        ConnectivityManager conMgr = (ConnectivityManager) getSystemService (Context.CONNECTIVITY_SERVICE);

        if (conMgr.getActiveNetworkInfo() != null

                && conMgr.getActiveNetworkInfo().isAvailable()

                && conMgr.getActiveNetworkInfo().isConnected()) {

            return true;

        } else {

            return false;

        }

    }
    public void derecealfonk(String derecegonder, final int txtsec){

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View promptView = layoutInflater.inflate(R.layout.numberpickeralert, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(promptView);
        final NumberPicker np,npondalik;
        np = (NumberPicker) promptView.findViewById(R.id.sayi11);
        npondalik = (NumberPicker) promptView.findViewById(R.id.sayi22);
        npondalik.setMinValue(0);
        npondalik.setMaxValue(9);
        np.setMinValue(10);
        np.setMaxValue(40);
        if(derecegonder.toString().length()==4){
            degeral=Integer.parseInt(derecegonder.toString().substring(0,2));
            //degeralond=0;
            degeralond=Integer.parseInt(derecegonder.toString().substring(3,4));
        }else
        {
            degeral=24;
            degeralond=0;
        }
        np.setValue(degeral);
        np.setWrapSelectorWheel(false);
        npondalik.setValue(degeralond);
        npondalik.setWrapSelectorWheel(false);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Ayarla", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // get user input and set it to result
                        if(txtsec==1) {
                            final Button derecesec1txt = (Button) findViewById(R.id.derecesec1);
                            derecesec1txt.setText(Integer.toString(np.getValue()) + "." + Integer.toString(npondalik.getValue()));
                        }
                        if(txtsec==2) {
                            final Button derecesec2txt = (Button) findViewById(R.id.derecesec2);
                            derecesec2txt.setText(Integer.toString(np.getValue()) + "." + Integer.toString(npondalik.getValue()));
                        }
                        if(txtsec==3) {
                            final Button derecesec3txt = (Button) findViewById(R.id.derecesec3);
                            derecesec3txt.setText(Integer.toString(np.getValue()) + "." + Integer.toString(npondalik.getValue()));
                        }
                        if(txtsec==4) {
                            final Button derecesec4txt = (Button) findViewById(R.id.derecesec4);
                            derecesec4txt.setText(Integer.toString(np.getValue()) + "." + Integer.toString(npondalik.getValue()));
                        }
                        if(txtsec==5) {
                            final Button derecesec5txt = (Button) findViewById(R.id.derecesec5);
                            derecesec5txt.setText(Integer.toString(np.getValue()) + "." + Integer.toString(npondalik.getValue()));
                        }
                        if(txtsec==6) {
                            final Button derecesec6txt = (Button) findViewById(R.id.derecesec6);
                            derecesec6txt.setText(Integer.toString(np.getValue()) + "." + Integer.toString(npondalik.getValue()));
                        }
                    }
                })
                .setNegativeButton("Ýptal",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,	int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alertD = alertDialogBuilder.create();

        alertD.show();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kombiayarlarfrm);
        Button gonder1=(Button) findViewById(R.id.gonder1);
        Button iptal=(Button) findViewById(R.id.iptal);
        preferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final View gunlerview=(View) findViewById(R.id.gunlerview);
        CihazID=preferences.getString("CihazID", "Cihaz ID Gir");
        //Button saatsec11txt=(Button)findViewById(R.id.saatsec11);
        final Button saatsec1txt = (Button) findViewById(R.id.saatsec1);
        final Button derecesec1txt = (Button) findViewById(R.id.derecesec1);
        final Button saatsec2txt = (Button) findViewById(R.id.saatsec2);
        final Button derecesec2txt = (Button) findViewById(R.id.derecesec2);
        final Button saatsec3txt = (Button) findViewById(R.id.saatsec3);
        final Button derecesec3txt = (Button) findViewById(R.id.derecesec3);
        final Button saatsec4txt = (Button) findViewById(R.id.saatsec4);
        final Button derecesec4txt = (Button) findViewById(R.id.derecesec4);
        final Button saatsec5txt = (Button) findViewById(R.id.saatsec5);
        final Button derecesec5txt = (Button) findViewById(R.id.derecesec5);
        final Button saatsec6txt = (Button) findViewById(R.id.saatsec6);
        final Button derecesec6txt = (Button) findViewById(R.id.derecesec6);
        final Button tolaranstxt = (Button) findViewById(R.id.tolarans);
        //final Button programbtn = (Button) findViewById(R.id.Programbtn);
        final RadioGroup modradioGroup=(RadioGroup) findViewById(R.id.modradioGroup);
        final RadioButton modsecbuton1=(RadioButton) findViewById(R.id.stdradio);
        final RadioButton modsecbuton2=(RadioButton) findViewById(R.id.hftradio);
        final RadioButton modsecbuton3=(RadioButton) findViewById(R.id.yazradio);
        final RadioButton modsecbuton4=(RadioButton) findViewById(R.id.krmradio);
        final RadioButton ptesiradio=(RadioButton) findViewById(R.id.ptesiradio);
        final RadioGroup modradioGroupgun=(RadioGroup) findViewById(R.id.modradioGroupgun);
        final RadioButton salradio=(RadioButton) findViewById(R.id.salradio);
        final RadioButton carradio=(RadioButton) findViewById(R.id.carradio);
        final RadioButton perradio=(RadioButton) findViewById(R.id.perradio);
        final RadioButton cumradio=(RadioButton) findViewById(R.id.cumradio);
        final RadioButton ctesiradio=(RadioButton) findViewById(R.id.ctesiradio);
        final RadioButton pazradio=(RadioButton) findViewById(R.id.pazradio);
        final TextView modsectxt=(TextView) findViewById(R.id.modsectxt);
        durum="0";
        threadcalistir();
        modradioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                modsectxt.setText(String.valueOf(modradioGroup.indexOfChild(findViewById(checkedId))));
                int secenek=modradioGroup.indexOfChild(findViewById(checkedId));
                durum="2";
                gunlerview.setVisibility(View.INVISIBLE);
                if(secenek==0){
                    modsectxt.setText("GENEL GÜNLÜK MOD:");
                    calismamodu="0";
                }
                if(secenek==1){

                    Date simdikiZaman = new Date();
                    Calendar gunbul=Calendar.getInstance();
                    gunbul.setTime(simdikiZaman);
                    int gunbul2=gunbul.get(gunbul.DAY_OF_WEEK);
                    gunlerview.setVisibility(View.VISIBLE);

                    if (gunbul2==1){
                        gunyaziyla="Pazar";
                        //pazradio.setChecked(true);
                        modsectxt.setText("HAFTALIK MOD:("+gunyaziyla+")");
                        calismamodu="1";
                    }
                    if (gunbul2==2){
                        gunyaziyla="Pazartesi";
                        //ptesiradio.setChecked(true);
                        modsectxt.setText("HAFTALIK MOD:("+gunyaziyla+")");
                        calismamodu="2";
                    }
                    if (gunbul2==3){
                        gunyaziyla="Salý";
                        //salradio.setChecked(true);
                        modsectxt.setText("HAFTALIK MOD:("+gunyaziyla+")");
                        calismamodu="3";
                    }
                    if (gunbul2==4){
                        gunyaziyla="Çarþamba";
                        //carradio.setChecked(true);
                        modsectxt.setText("HAFTALIK MOD:("+gunyaziyla+")");
                        calismamodu="4";
                    }
                    if (gunbul2==5){
                        gunyaziyla="Perþembe";
                        //perradio.setChecked(true);
                        modsectxt.setText("HAFTALIK MOD:("+gunyaziyla+")");
                        calismamodu="5";
                    }
                    if (gunbul2==6){
                        gunyaziyla="Cuma";
                        //cumradio.setChecked(true);
                        modsectxt.setText("HAFTALIK MOD:("+gunyaziyla+")");
                        calismamodu="6";
                    }
                    if (gunbul2==7){
                        gunyaziyla="Cumartesi";
                        //ctesiradio.setChecked(true);
                        modsectxt.setText("HAFTALIK MOD:("+gunyaziyla+")");
                        calismamodu="7";
                    }
                }
                if(secenek==2){
                    modsectxt.setText("EVDE YOKUM MODU:");
                    calismamodu="8";
                }
                if(secenek==3){
                    modsectxt.setText("MÝSAFÝR MODU:");
                    calismamodu="9";
                }
                if(secenek==4){
                    modsectxt.setText("HASTA VAR MODU:");
                    calismamodu="10";
                }
                if(secenek==5){
                    modsectxt.setText("KOMBÝ KAPAT MODU:");
                    calismamodu="11";
                }
                threadcalistir();

            }

        });
        modradioGroupgun.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int secenek2=modradioGroupgun.indexOfChild(findViewById(checkedId));
                durum="2";
                if (secenek2==6){
                    gunyaziyla="Pazar";
                    //pazradio.setChecked(true);
                    modsectxt.setText("HAFTALIK MOD:("+gunyaziyla+")");
                    calismamodu="1";

                }
                if (secenek2==0){
                    gunyaziyla="Pazartesi";
                    //ptesiradio.setChecked(true);
                    modsectxt.setText("HAFTALIK MOD:("+gunyaziyla+")");
                    calismamodu="2";
                }
                if (secenek2==1){
                    gunyaziyla="Salý";
                    //salradio.setChecked(true);
                    modsectxt.setText("HAFTALIK MOD:("+gunyaziyla+")");
                    calismamodu="3";
                }
                if (secenek2==2){
                    gunyaziyla="Çarþamba";
                    //carradio.setChecked(true);
                    modsectxt.setText("HAFTALIK MOD:("+gunyaziyla+")");
                    calismamodu="4";
                }
                if (secenek2==3){
                    gunyaziyla="Perþembe";
                    //perradio.setChecked(true);
                    modsectxt.setText("HAFTALIK MOD:("+gunyaziyla+")");
                    calismamodu="5";
                }
                if (secenek2==4){
                    gunyaziyla="Cuma";
                    //cumradio.setChecked(true);
                    modsectxt.setText("HAFTALIK MOD:("+gunyaziyla+")");
                    calismamodu="6";
                }
                if (secenek2==5) {
                    gunyaziyla = "Cumartesi";
                    //ctesiradio.setChecked(true);
                    modsectxt.setText("HAFTALIK MOD:(" + gunyaziyla + ")");
                    calismamodu = "7";
                }
                threadcalistir();
            }

        });

        /*programbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), programfrm.class);
                startActivity(i);
            }
        });*/
        iptal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        gonder1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                durum = "1";
                //Button saatsec11txt=(Button)findViewById(R.id.saatsec11);
                //final Button saatsec1txt = (Button) findViewById(R.id.saatsec1);

                saatsec1 = saatsec1txt.getText().toString();
                //final Button derecesec1txt = (Button) findViewById(R.id.derecesec1);
                //derecesec1 = derecesec1txt.getText().toString();
                derecesec1 = String.valueOf(Math.round(Float.valueOf(derecesec1txt.getText().toString())*10));
                //final Button saatsec2txt = (Button) findViewById(R.id.saatsec2);
                saatsec2 = saatsec2txt.getText().toString();
                //final Button derecesec2txt = (Button) findViewById(R.id.derecesec2);
                //derecesec2 = derecesec2txt.getText().toString();
                derecesec2 = String.valueOf(Math.round(Float.valueOf(derecesec2txt.getText().toString())*10));
                //final Button saatsec3txt = (Button) findViewById(R.id.saatsec3);
                saatsec3 = saatsec3txt.getText().toString();
                //final Button derecesec3txt = (Button) findViewById(R.id.derecesec3);
                //derecesec3 = derecesec3txt.getText().toString();
                derecesec3 = String.valueOf(Math.round(Float.valueOf(derecesec3txt.getText().toString())*10));
                //final Button saatsec4txt = (Button) findViewById(R.id.saatsec4);
                saatsec4 = saatsec4txt.getText().toString();
                //final Button derecesec4txt = (Button) findViewById(R.id.derecesec4);
                //derecesec4 = derecesec4txt.getText().toString();
                derecesec4 = String.valueOf(Math.round(Float.valueOf(derecesec4txt.getText().toString())*10));
                //final Button saatsec5txt = (Button) findViewById(R.id.saatsec5);
                saatsec5 = saatsec5txt.getText().toString();
                //final Button derecesec5txt = (Button) findViewById(R.id.derecesec5);
                //derecesec5 = derecesec5txt.getText().toString();
                derecesec5 = String.valueOf(Math.round(Float.valueOf(derecesec5txt.getText().toString())*10));
                //final Button saatsec6txt = (Button) findViewById(R.id.saatsec6);
                saatsec6 = saatsec6txt.getText().toString();
                //final Button derecesec6txt = (Button) findViewById(R.id.derecesec6);
                //derecesec6 = derecesec6txt.getText().toString();
                derecesec6 = String.valueOf(Math.round(Float.valueOf(derecesec6txt.getText().toString())*10));
                //final Button tolaranstxt = (Button) findViewById(R.id.tolarans);
                tolarans = tolaranstxt.getText().toString();

                threadcalistir();
                finish();

            }
        });
        derecesec1txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                derecealfonk(derecesec1txt.getText().toString(),1);
            }
        });
        derecesec2txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                derecealfonk(derecesec2txt.getText().toString(),2);
            }
        });
        derecesec3txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                derecealfonk(derecesec3txt.getText().toString(),3);
            }
        });
        derecesec4txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                derecealfonk(derecesec4txt.getText().toString(),4);
            }
        });
        derecesec5txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                derecealfonk(derecesec5txt.getText().toString(),5);
            }
        });
        derecesec6txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                derecealfonk(derecesec6txt.getText().toString(),6);
            }
        });
        tolaranstxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater = LayoutInflater.from(context);
                View promptView = layoutInflater.inflate(R.layout.numberpickeralert, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setView(promptView);
                final NumberPicker np;
                np = (NumberPicker) promptView.findViewById(R.id.sayi22);
                np.setMinValue(0);
                np.setMaxValue(4);
                degeral=Integer.parseInt(tolaranstxt.getText().toString());
                np.setValue(degeral);
                np.setWrapSelectorWheel(false);
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Ayarla", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result

                                tolaranstxt.setText(Integer.toString(np.getValue()));
                            }
                        })
                        .setNegativeButton("Ýptal",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alertD = alertDialogBuilder.create();

                alertD.show();
            }
        });
        saatsec2txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar mcurrentTime = Calendar.getInstance();//
                int hour;
                int minute;
                saatkarsilastir=Integer.parseInt(saatsec1txt.getText().toString().substring(0,2));
                //Integer.parseInt(saatkarsilastir.substring(0,2);
                saataldeger=saatsec2txt.getText().toString();
                if(saataldeger.length()==5){
                    hour=Integer.parseInt(saataldeger.substring(0,2));
                    //minute=Integer.parseInt(saataldeger.substring(4,5));
                    minute=0;
                }
                else{
                    hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);//Güncel saati aldýk
                    //minute = mcurrentTime.get(Calendar.MINUTE);//Güncel dakikayý aldýk
                    minute=0;
                     }
                TimePickerDialog timePicker; //Time Picker referansýmýzý oluþturduk
                  //TimePicker objemizi oluþturuyor ve click listener ekliyoruz
                timePicker = new TimePickerDialog(kombiayarlarfrm.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        if (selectedHour<=saatkarsilastir){
                            saat=Integer.toString(saatkarsilastir);
                        }else
                        {
                            saat=Integer.toString(selectedHour);
                        }
                        dakika=Integer.toString(selectedMinute);
                        if(saat.length()==1){
                            saat="0"+saat;
                        }
                        if(dakika.length()==1){
                            dakika="0"+dakika;
                        }
                        saatsec2txt.setText(saat + ":" + dakika);//Ayarla butonu týklandýðýnda textview'a yazdýrýyoruz
                    }
                }, hour, minute, true);//true 24 saatli sistem için
                timePicker.setTitle("Saat Seçiniz");
                timePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, "Ayarla", timePicker);
                //timePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE, "Ýptal", timePicker);

                timePicker.show();
            }
        });
        saatsec3txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();//
                int hour;
                int minute;
                saatkarsilastir=Integer.parseInt(saatsec2txt.getText().toString().substring(0,2));
                //Integer.parseInt(saatkarsilastir.substring(0,2);
                saataldeger=saatsec3txt.getText().toString();
                if(saataldeger.length()==5){
                    hour=Integer.parseInt(saataldeger.substring(0,2));
                    //minute=Integer.parseInt(saataldeger.substring(4,5));
                    minute=0;
                }
                else{
                    hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);//Güncel saati aldýk
                    //minute = mcurrentTime.get(Calendar.MINUTE);//Güncel dakikayý aldýk
                    minute=0;
                }
                TimePickerDialog timePicker; //Time Picker referansýmýzý oluþturduk

                //TimePicker objemizi oluþturuyor ve click listener ekliyoruz
                timePicker = new TimePickerDialog(kombiayarlarfrm.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        if (selectedHour<=saatkarsilastir){
                            saat=Integer.toString(saatkarsilastir);
                        }else
                        {
                            saat=Integer.toString(selectedHour);
                        }
                        dakika=Integer.toString(selectedMinute);
                        if(saat.length()==1){
                            saat="0"+saat;
                        }
                        if(dakika.length()==1){
                            dakika="0"+dakika;
                        }
                        saatsec3txt.setText(saat + ":" + dakika);//Ayarla butonu týklandýðýnda textview'a yazdýrýyoruz

                    }
                }, hour, minute, true);//true 24 saatli sistem için
                timePicker.setTitle("Saat Seçiniz");
                timePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, "Ayarla", timePicker);
                //timePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE, "Ýptal", timePicker);

                timePicker.show();
            }

        });
        saatsec4txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();//
                int hour;
                int minute;
                saatkarsilastir=Integer.parseInt(saatsec3txt.getText().toString().substring(0,2));
                //Integer.parseInt(saatkarsilastir.substring(0,2);
                saataldeger=saatsec4txt.getText().toString();
                if(saataldeger.length()==5){
                    hour=Integer.parseInt(saataldeger.substring(0,2));
                    //minute=Integer.parseInt(saataldeger.substring(4,5));
                    minute=0;
                }
                else{
                    hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);//Güncel saati aldýk
                    //minute = mcurrentTime.get(Calendar.MINUTE);//Güncel dakikayý aldýk
                    minute=0;
                }
                TimePickerDialog timePicker; //Time Picker referansýmýzý oluþturduk

                //TimePicker objemizi oluþturuyor ve click listener ekliyoruz
                timePicker = new TimePickerDialog(kombiayarlarfrm.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        if (selectedHour<=saatkarsilastir){
                            saat=Integer.toString(saatkarsilastir);
                        }else
                        {
                            saat=Integer.toString(selectedHour);
                        }
                        dakika=Integer.toString(selectedMinute);
                        if(saat.length()==1){
                            saat="0"+saat;
                        }
                        if(dakika.length()==1){
                            dakika="0"+dakika;
                        }
                        saatsec4txt.setText(saat + ":" + dakika);//Ayarla butonu týklandýðýnda textview'a yazdýrýyoruz
                    }
                }, hour, minute, true);//true 24 saatli sistem için
                timePicker.setTitle("Saat Seçiniz");
                timePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, "Ayarla", timePicker);
                //timePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE, "Ýptal", timePicker);

                timePicker.show();
            }
        });
        saatsec5txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();//
                int hour;
                int minute;
                saatkarsilastir=Integer.parseInt(saatsec4txt.getText().toString().substring(0,2));
                //Integer.parseInt(saatkarsilastir.substring(0,2);
                saataldeger=saatsec5txt.getText().toString();
                if(saataldeger.length()==5){
                    hour=Integer.parseInt(saataldeger.substring(0,2));
                    //minute=Integer.parseInt(saataldeger.substring(4,5));
                    minute=0;
                }
                else{
                    hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);//Güncel saati aldýk
                    //minute = mcurrentTime.get(Calendar.MINUTE);//Güncel dakikayý aldýk
                    minute=0;
                }
                TimePickerDialog timePicker; //Time Picker referansýmýzý oluþturduk

                //TimePicker objemizi oluþturuyor ve click listener ekliyoruz
                timePicker = new TimePickerDialog(kombiayarlarfrm.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        if (selectedHour<=saatkarsilastir){
                            saat=Integer.toString(saatkarsilastir);
                        }else
                        {
                            saat=Integer.toString(selectedHour);
                        }
                        dakika=Integer.toString(selectedMinute);
                        if(saat.length()==1){
                            saat="0"+saat;
                        }
                        if(dakika.length()==1){
                            dakika="0"+dakika;
                        }
                        saatsec5txt.setText(saat + ":" + dakika);//Ayarla butonu týklandýðýnda textview'a yazdýrýyoruz
                    }
                }, hour, minute, true);//true 24 saatli sistem için
                timePicker.setTitle("Saat Seçiniz");
                timePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, "Ayarla", timePicker);
                //timePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE, "Ýptal", timePicker);

                timePicker.show();
            }
        });
        saatsec6txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();//
                int hour;
                int minute;
                saatkarsilastir=Integer.parseInt(saatsec5txt.getText().toString().substring(0,2));
                //Integer.parseInt(saatkarsilastir.substring(0,2);
                saataldeger=saatsec6txt.getText().toString();
                if(saataldeger.length()==5){
                    hour=Integer.parseInt(saataldeger.substring(0,2));
                    //minute=Integer.parseInt(saataldeger.substring(4,5));
                    minute=0;
                }
                else{
                    hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);//Güncel saati aldýk
                    //minute = mcurrentTime.get(Calendar.MINUTE);//Güncel dakikayý aldýk
                    minute=0;
                }
                TimePickerDialog timePicker; //Time Picker referansýmýzý oluþturduk

                //TimePicker objemizi oluþturuyor ve click listener ekliyoruz
                timePicker = new TimePickerDialog(kombiayarlarfrm.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        if (selectedHour<=saatkarsilastir){
                            saat=Integer.toString(saatkarsilastir);
                        }else
                        {
                            saat=Integer.toString(selectedHour);
                        }
                        dakika=Integer.toString(selectedMinute);
                        if(saat.length()==1){
                            saat="0"+saat;
                        }
                        if(dakika.length()==1){
                            dakika="0"+dakika;
                        }
                        saatsec6txt.setText(saat + ":" + dakika);//Ayarla butonu týklandýðýnda textview'a yazdýrýyoruz
                    }
                }, hour, minute, true);//true 24 saatli sistem için
                timePicker.setTitle("Saat Seçiniz");
                timePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, "Ayarla", timePicker);
                //timePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE, "Ýptal", timePicker);

                timePicker.show();
            }
        });



    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_kombiayarlarfrm, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public static String connect(String url,String CihazID,String saatsec1,String derecesec1,String saatsec2,String derecesec2,String saatsec3,String derecesec3,String saatsec4,String derecesec4,String saatsec5,String derecesec5,String saatsec6,String derecesec6,String tolarans,String durum,String modunuz){
        HttpClient httpClient=new DefaultHttpClient();
        //HttpGet httpget = new HttpGet(url);
        HttpPost httppost = new HttpPost(url);
        httppost.addHeader("Content-Type", "application/x-www-form-urlencoded; text/html; charset=UTF-8");
        httppost.addHeader("User-Agent", "Mozilla/4.0");
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("cihazID", CihazID));
        params.add(new BasicNameValuePair("saatsec1", saatsec1));
        params.add(new BasicNameValuePair("derecesec1", derecesec1));
        params.add(new BasicNameValuePair("saatsec2", saatsec2));
        params.add(new BasicNameValuePair("derecesec2", derecesec2));
        params.add(new BasicNameValuePair("saatsec3", saatsec3));
        params.add(new BasicNameValuePair("derecesec3", derecesec3));
        params.add(new BasicNameValuePair("saatsec4", saatsec4));
        params.add(new BasicNameValuePair("derecesec4", derecesec4));
        params.add(new BasicNameValuePair("saatsec5", saatsec5));
        params.add(new BasicNameValuePair("derecesec5", derecesec5));
        params.add(new BasicNameValuePair("saatsec6", saatsec6));
        params.add(new BasicNameValuePair("derecesec6", derecesec6));
        params.add(new BasicNameValuePair("tolarans", tolarans));
        params.add(new BasicNameValuePair("durum", durum));
        params.add(new BasicNameValuePair("modunuz",modunuz));
        try {
            httppost.setEntity(new UrlEncodedFormEntity(params));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        HttpResponse response;
        try {
            response=httpClient.execute(httppost);
            HttpEntity entity=response.getEntity();
            if(entity!=null){
                InputStream instream=entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(instream));
                StringBuilder sb = new StringBuilder();
                String line = null;
                try {
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        instream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return sb.toString();
            }
        } catch (Exception e) {
        }
        return null;
    }
    class fetchJsonTask extends AsyncTask<String, Integer, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                String ret = connect(params[0],CihazID,saatsec1,derecesec1,saatsec2,derecesec2,saatsec3,derecesec3,saatsec4,derecesec4,saatsec5,derecesec5,saatsec6,derecesec6,tolarans,durum,calismamodu);
                ret = ret.trim();
                JSONObject jsonObj = new JSONObject(ret);
                return jsonObj;
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(JSONObject result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (result != null) {
                parseJson(result);
                /*TextView mesaj1 = (TextView) findViewById(R.id.mesaj1);
                mesaj1.setText(result.toString());
                mesaj1.setTextColor(Color.RED);*/
            } else {
                TextView mesaj1 = (TextView) findViewById(R.id.mesaj1);
                mesaj1.setText("Kayýt Bulunamadý");
                mesaj1.setTextColor(Color.RED);
                Toast.makeText(getApplicationContext(), "Sistemden Herhangi bir bilgi gelmedi.",
                        Toast.LENGTH_SHORT).show();
            }

        }
    }
    public void parseJson(JSONObject ogrenciJson) {
        //int gunbul2;
        //Button saatsec11txt=(Button)findViewById(R.id.saatsec11);
        Button saatsec1txt = (Button) findViewById(R.id.saatsec1);
        Button derecesec1txt = (Button) findViewById(R.id.derecesec1);
        Button saatsec2txt = (Button) findViewById(R.id.saatsec2);
        Button derecesec2txt = (Button) findViewById(R.id.derecesec2);
        Button saatsec3txt = (Button) findViewById(R.id.saatsec3);
        Button derecesec3txt = (Button) findViewById(R.id.derecesec3);
        Button saatsec4txt = (Button) findViewById(R.id.saatsec4);
        Button derecesec4txt = (Button) findViewById(R.id.derecesec4);
        Button saatsec5txt = (Button) findViewById(R.id.saatsec5);
        Button derecesec5txt = (Button) findViewById(R.id.derecesec5);
        Button saatsec6txt = (Button) findViewById(R.id.saatsec6);
        Button derecesec6txt = (Button) findViewById(R.id.derecesec6);
        Button tolaranstxt = (Button) findViewById(R.id.tolarans);
        final RadioGroup modradioGroup=(RadioGroup) findViewById(R.id.modradioGroup);
        RadioButton modsecbuton1=(RadioButton) findViewById(R.id.stdradio);
        RadioButton modsecbuton2=(RadioButton) findViewById(R.id.hftradio);
        RadioButton modsecbuton3=(RadioButton) findViewById(R.id.yazradio);
        RadioButton modsecbuton4=(RadioButton) findViewById(R.id.krmradio);
        RadioButton modsecbuton5=(RadioButton) findViewById(R.id.hstvradio);
        RadioButton modsecbuton6=(RadioButton) findViewById(R.id.kptradio);
        RadioButton ptesiradio=(RadioButton) findViewById(R.id.ptesiradio);
        RadioButton salradio=(RadioButton) findViewById(R.id.salradio);
        RadioButton carradio=(RadioButton) findViewById(R.id.carradio);
        RadioButton perradio=(RadioButton) findViewById(R.id.perradio);
        RadioButton cumradio=(RadioButton) findViewById(R.id.cumradio);
        RadioButton ctesiradio=(RadioButton) findViewById(R.id.ctesiradio);
        RadioButton pazradio=(RadioButton) findViewById(R.id.pazradio);
        View gunlerview=(View) findViewById(R.id.gunlerview);
        TextView modsectxt=(TextView) findViewById(R.id.modsectxt);
        int moddegeral;

        System.out.println(ogrenciJson);
        try {
            //saatsec11txt.setText(ogrenciJson.getString("saatsec1"));
            saatsec1txt.setText(ogrenciJson.getString("saatsec1"));
            derecesec1txt.setText(ogrenciJson.getString("derecesec1").substring(0,2)+"."+ogrenciJson.getString("derecesec1").substring(2,3));
            saatsec2txt.setText(ogrenciJson.getString("saatsec2"));
            derecesec2txt.setText(ogrenciJson.getString("derecesec2").substring(0,2)+"."+ogrenciJson.getString("derecesec2").substring(2,3));
            saatsec3txt.setText(ogrenciJson.getString("saatsec3"));
            derecesec3txt.setText(ogrenciJson.getString("derecesec3").substring(0,2)+"."+ogrenciJson.getString("derecesec3").substring(2,3));
            saatsec4txt.setText(ogrenciJson.getString("saatsec4"));
            derecesec4txt.setText(ogrenciJson.getString("derecesec4").substring(0,2)+"."+ogrenciJson.getString("derecesec4").substring(2,3));
            saatsec5txt.setText(ogrenciJson.getString("saatsec5"));
            derecesec5txt.setText(ogrenciJson.getString("derecesec5").substring(0,2)+"."+ogrenciJson.getString("derecesec5").substring(2,3));
            saatsec6txt.setText(ogrenciJson.getString("saatsec6"));
            derecesec6txt.setText(ogrenciJson.getString("derecesec6").substring(0,2)+"."+ogrenciJson.getString("derecesec6").substring(2,3));
            tolaranstxt.setText(ogrenciJson.getString("tolarans"));
            moddegeral=Integer.valueOf(ogrenciJson.getString("modunuz"));
            //if(Integer.valueOf(durum)!=2){
            if(moddegeral==0){
                modsecbuton1.setChecked(true);
                gunlerview.setVisibility(View.INVISIBLE);
                calismamodu="0";
                /*Toast.makeText(getApplicationContext(), ogrenciJson.getString("modunuz")+"+++"+durum+"+++"+ogrenciJson.getString("durum"),
                        Toast.LENGTH_LONG).show();*/
            }

                if (moddegeral==1){
                    //gunyaziyla="Pazar";
                    gunlerview.setVisibility(View.VISIBLE);
                    pazradio.setChecked(true);
                    modsecbuton2.setChecked(true);
                    calismamodu="1";
                    //modsectxt.setText("HAFTALIK MOD:("+gunyaziyla+")");
                    //calismamodu="1";
                }
                if (moddegeral==2){
                    //gunyaziyla="Pazartesi";
                    gunlerview.setVisibility(View.VISIBLE);
                    ptesiradio.setChecked(true);
                    modsecbuton2.setChecked(true);
                    calismamodu="2";
                    //modsectxt.setText("HAFTALIK MOD:("+gunyaziyla+")");
                    //calismamodu="2";
                }
                if (moddegeral==3){
                    //gunyaziyla="Salý";
                    gunlerview.setVisibility(View.VISIBLE);
                    salradio.setChecked(true);
                    modsecbuton2.setChecked(true);
                    calismamodu="3";
                    //modsectxt.setText("HAFTALIK MOD:("+gunyaziyla+")");
                    //calismamodu="3";
                }
                if (moddegeral==4){
                    //gunyaziyla="Çarþamba";
                    gunlerview.setVisibility(View.VISIBLE);
                    carradio.setChecked(true);
                    modsecbuton2.setChecked(true);
                    calismamodu="4";
                    //modsectxt.setText("HAFTALIK MOD:("+gunyaziyla+")");
                    //calismamodu="4";
                }
                if (moddegeral==5){
                    //gunyaziyla="Perþembe";
                    gunlerview.setVisibility(View.VISIBLE);
                    perradio.setChecked(true);
                    modsecbuton2.setChecked(true);
                    calismamodu="5";
                    //modsectxt.setText("HAFTALIK MOD:("+gunyaziyla+")");
                    //calismamodu="5";
                }
                if (moddegeral==6){
                    //gunyaziyla="Cuma";
                    gunlerview.setVisibility(View.VISIBLE);
                    cumradio.setChecked(true);
                    modsecbuton2.setChecked(true);
                    calismamodu="6";
                    //modsectxt.setText("HAFTALIK MOD:("+gunyaziyla+")");
                    //calismamodu="6";
                }
                if (moddegeral==7){
                    //gunyaziyla="Cumartesi";
                    gunlerview.setVisibility(View.VISIBLE);
                    ctesiradio.setChecked(true);
                    modsecbuton2.setChecked(true);
                    calismamodu="7";
                    //modsectxt.setText("HAFTALIK MOD:("+gunyaziyla+")");
                    //calismamodu="7";
                }

            if(moddegeral==8){
                gunlerview.setVisibility(View.INVISIBLE);
                modsecbuton3.setChecked(true);
                calismamodu="8";
            }
            if(moddegeral==9){
                gunlerview.setVisibility(View.INVISIBLE);
                modsecbuton4.setChecked(true);
                calismamodu="9";
            }
            if(moddegeral==10){
                gunlerview.setVisibility(View.INVISIBLE);
                modsecbuton5.setChecked(true);
                calismamodu="10";
            }
            if(moddegeral==11){
                gunlerview.setVisibility(View.INVISIBLE);
                modsecbuton6.setChecked(true);
                calismamodu="11";
            }
            //}


            /*Toast.makeText(getApplicationContext(), ogrenciJson.getString("modunuz")+"+++"+durum+"+++"+ogrenciJson.getString("durum"),
                   Toast.LENGTH_LONG).show();*/



        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public  void threadcalistir(){
        //fetchJsonTask a = new fetchJsonTask();

        //a.execute(URL);
        if (internetBaglantisiVarMi()) {
        Thread t6 = new Thread() {
            public void run() {

                try {
                    //sleep(5000);
                    fetchJsonTask b = new fetchJsonTask();
                    Thread.sleep(1000);
                    b.execute(URL);


                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    //finish();
                }

            }
            //public void finish(){

            //}
        };
        Toast.makeText(getApplicationContext(), "Internet Baðlantýnýz var. Ýþlemin Tamamlanmasý Ýçin 5 Saniye Bekleyiniz.", Toast.LENGTH_SHORT).show();
        t6.start();
        } else {
            Toast.makeText(getApplicationContext(), "Internet Baðlantýnýz yok", Toast.LENGTH_LONG).show();
        }
    }

}
