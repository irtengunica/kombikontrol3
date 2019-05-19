package com.example.okul.kombikontrol;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Switch;
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
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;



public class MainActivity extends ActionBarActivity {
    SharedPreferences preferences;
    public static String URL = "http://turulay.com/kombiisim42.php";//Bilgisayarýn IP adresi
    public static String URL2 = "http://turulay.com/kombiisim6.php";
    public static String URL3 = "http://turulay.com/kombikapat1.php";
    Boolean renkdegis=true;
    int sayac=0;
    Boolean msjdurum=true;
    String CihazID,CihazAdi;
    String durum="0";
    int degeral;
    int degeralond;
    String degeral2;
    SwipeRefreshLayout swipeLayout;
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
    public void mesajpencerefonk(String sdegeral,final int idegeral){
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View promptView = layoutInflater.inflate(R.layout.mesajfrm, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(promptView);
        final TextView mesajtxt=(TextView) promptView.findViewById(R.id.mesajtxt);
        mesajtxt.setText(sdegeral);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK", new
                        DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                .setNegativeButton("Ýptal", new
                        DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alertD = alertDialogBuilder.create();
        alertD.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        durum="0";
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swview);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeLayout.setRefreshing(false);
                        String durum="0";
                        threadcalistir();
                        /*LayoutInflater layoutInflater = LayoutInflater.from(context);
                        View promptView = layoutInflater.inflate(R.layout.numberpickeralert, null);
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                        alertDialogBuilder.setView(promptView);
                        AlertDialog alertD = alertDialogBuilder.create();

                        alertD.show();*/


                        // burada ise Swipe Reflesh olduðunda ne yapacaksanýz onu eklemeniz yeterlidir. Örneðin bir listeyi clear edebilir yada yeniden veri doldurabilirsiniz.
                    }
                }, 2000);
            }
        });

        swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        Button ayarlarbtn=(Button) findViewById(R.id.ayarlarbtn);
        Button anlikbt=(Button) findViewById(R.id.anlikbtn);
        Button guncelle=(Button) findViewById(R.id.guncelle);
        Button cihazID = (Button) findViewById(R.id.cihazID);
        Button cihazad = (Button) findViewById(R.id.cihazad);
        final Button simdikiderece = (Button) findViewById(R.id.simdikiderece);
        Button guntarih = (Button) findViewById(R.id.guntarih);
        final Button kombidurumu=(Button) findViewById(R.id.kombidurumu);
        final Switch sistemdurumusw=(Switch) findViewById(R.id.sistemdurumusw);
        TextView mesaj1=(TextView) findViewById(R.id.mesaj1);
        sistemdurumusw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    sistemdurumusw.setText("Sitem Açýk");
                    if(durum=="11"){
                        durum="12";
                        threadcalistir3();
                    }

                }else{
                    sistemdurumusw.setText("Sistem Kapalý");
                    durum="11";
                    threadcalistir3();
                }
            }

        });
        if(sistemdurumusw.isChecked()){
            sistemdurumusw.setText("Sitem Açýk");
        }else{
            sistemdurumusw.setText("Sistem Kapalý");
        }
        /*if (mesaj1.getText().toString()!="11"){
            sistemdurumusw.setChecked(true);
        }else{
            sistemdurumusw.setChecked(false);
        }*/
        //Toast.makeText(this,mesaj1.getText().toString(),Toast.LENGTH_SHORT).show();


        preferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        CihazID=preferences.getString("CihazID", "1001");
        CihazAdi=preferences.getString("CihazAdi", "Kombi");
        durum="0";

        threadcalistir();
        /*final android.os.Handler handler = new android.os.Handler();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        threadcalistir();
                    }
                });
            }
        };
        Timer timer = new Timer();
        timer.schedule(doAsynchronousTask, 15000, 15000);*/
        //
        simdikiderece.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                durum = "0";
                mesajpencerefonk("Anlýk Oda Sýcaklýðýnýzý verir."+simdikiderece.getText().toString()+" santigrat derecedir.",0);
                threadcalistir();
            }
        });
        kombidurumu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                durum = "0";
                mesajpencerefonk("Kombinizin Sizin Ayarladýðýnýz  Sýcaklýk Durumuna Göre Açýk/Kapalý Bilgisini verir."+kombidurumu.getText().toString(),0);
                threadcalistir();
            }
        });
        cihazID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                durum = "0";
                threadcalistir();
                Intent i=new Intent(getApplicationContext(),ayarlarfrm.class);
                startActivity(i);

            }
        });
        cihazad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                durum = "0";
                threadcalistir();
                Intent i=new Intent(getApplicationContext(),ayarlarfrm.class);
                startActivity(i);
            }
        });
        guncelle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                durum = "0";
                threadcalistir();
            }
        });
        ayarlarbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                durum = "0";
                threadcalistir();
                Intent i = new Intent(getApplicationContext(), kombiayarlarfrm.class);
                startActivity(i);

            }
        });
        anlikbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater = LayoutInflater.from(context);
                View promptView = layoutInflater.inflate(R.layout.numberpickeralert, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setView(promptView);
                final NumberPicker np,npondalik;
                npondalik = (NumberPicker) promptView.findViewById(R.id.sayi22);
                npondalik.setMinValue(0);
                npondalik.setMaxValue(9);
                np = (NumberPicker) promptView.findViewById(R.id.sayi11);
                np.setMinValue(10);
                np.setMaxValue(40);
                //degeral=Integer.parseInt(derecesec1txt.getText().toString());
                degeral=24;
                degeralond=0;
                np.setValue(degeral);
                np.setWrapSelectorWheel(false);
                npondalik.setValue(degeralond);
                npondalik.setWrapSelectorWheel(false);
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Ayarla", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result
                                durum = Integer.toString(np.getValue()) + Integer.toString(npondalik.getValue());

                                threadcalistir2();
                                //degeral2=Integer.toString(np.getValue());
                                //durum = "0";
                                //URL = "http://turulay.com/kombiisim4.php";
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
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id){
            case R.id.item1:
                Toast.makeText(this, "Program Ön Ayarlarý", Toast.LENGTH_SHORT).show();
                Intent i=new Intent(getApplicationContext(),ayarlarfrm.class);
                startActivity(i);
                break;
            case R.id.item2:
                Intent j = new Intent(getApplicationContext(), kombiayarlarfrm.class);
                startActivity(j);
                Toast.makeText(this,"Kombi Ayarlarý",Toast.LENGTH_SHORT).show();
                finish();
                break;

            case R.id.item3:
                Toast.makeText(this,"Tekrar Bekleriz.",Toast.LENGTH_SHORT).show();
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);


            //noinspection SimplifiableIfStatement
            //if (id == R.id.action_settings) {
            //return true;
        }

        //return super.onOptionsItemSelected(item);
        return true;
    }
    public static String connect(String url,String CihazID,String durum){
        HttpClient httpClient=new DefaultHttpClient();
        //HttpGet httpget = new HttpGet(url);
        HttpPost httppost = new HttpPost(url);
        httppost.addHeader("Content-Type", "application/x-www-form-urlencoded; text/html; charset=UTF-8");
        httppost.addHeader("User-Agent", "Mozilla/4.0");
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("cihazID", CihazID));
        params.add(new BasicNameValuePair("durum", durum));
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

                String ret = connect(params[0],CihazID,durum);
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
                /*TextView mesaj1 = (TextView) findViewById(R.id.mesaj1);
                mesaj1.setText("Kayýt Bulunamadý");
                mesaj1.setTextColor(Color.RED);*/
                Toast.makeText(getApplicationContext(), "Sistemden Herhangi bir bilgi gelmedi.",
                        Toast.LENGTH_LONG).show();
            }

        }
    }
    public void parseJson(JSONObject ogrenciJson) {
        //Button saatsec11txt=(Button)findViewById(R.id.saatsec11);
        String guncellemevarmi;
        int kars1,kars2,pildurum,nrf,modunuz;

        Button cihazID = (Button) findViewById(R.id.cihazID);
        Button cihazad = (Button) findViewById(R.id.cihazad);
        Button simdikiderece = (Button) findViewById(R.id.simdikiderece);
        Button guntarih = (Button) findViewById(R.id.guntarih);
        Button kombidurumu=(Button) findViewById(R.id.kombidurumu);
        TextView ayarlanansicaklik=(TextView) findViewById(R.id.ayarsicaklik);

        TextView simdikisaat = (TextView) findViewById(R.id.simdikisaat);
        ImageView kombiresim=(ImageView) findViewById(R.id.kombiresim);
        Integer kombidurums;
        float evsicakligi,ayarsicaklik;

        TextView cihazip = (TextView) findViewById(R.id.cihazip);
        TextView mesaj1 = (TextView) findViewById(R.id.mesaj1);
        Button anlikbtn=(Button) findViewById(R.id.anlikbtn);
        Button guncelle=(Button) findViewById(R.id.guncelle);
        Switch sistemdurumusw=(Switch) findViewById(R.id.sistemdurumusw);
        //mesaj1.setText("");
        System.out.println(ogrenciJson);
        try {
            //saatsec11txt.setText(ogrenciJson.getString("saatsec1"));
            cihazID.setText("CihazID: "+ogrenciJson.getString("cihazID"));
            cihazad.setText("Cihaz Adý: "+ogrenciJson.getString("cihazad"));
            evsicakligi=Float.parseFloat(ogrenciJson.getString("simdikiderece"));
            evsicakligi=evsicakligi/10;
            if(evsicakligi<=1){
                //mesaj1.setText("Sýcaklýk sensöründe bir sorun var. Lütfen Pilleri Kontrol ediniz.");
                //mesaj1.setTextColor(Color.RED);
                mesajpencerefonk("Sýcaklýk sensöründe bir sorun var. Lütfen Pilleri Kontrol ediniz.",0);
            }
            simdikiderece.setText("Ev Sýcaklýðý: " + String.valueOf(evsicakligi));

            //simdikiderece.setText("Ev Sýcaklýðý: "+ogrenciJson.getString("simdikiderece"));
            ayarsicaklik=Float.parseFloat(ogrenciJson.getString("ayarsicaklik"));
            ayarsicaklik=ayarsicaklik/10;
            ayarlanansicaklik.setText("Ayarlanan Sýcaklýk: "+String.valueOf(ayarsicaklik));
            anlikbtn.setText("Ayarlanan Sýcaklýk: "+String.valueOf(ayarsicaklik)+"C");
            kombidurums=Integer.parseInt(ogrenciJson.getString("kombidurum"));
            if(kombidurums==0)
            {
                kombiresim.setBackgroundResource(R.drawable.kapalianim);
                AnimationDrawable kapaliAnim=(AnimationDrawable) kombiresim.getBackground();
                kapaliAnim.start();
                //kombiresim.setImageResource(R.drawable.acik);
                kombidurumu.setText("Kombi Durumu: Kapalý");
            }
            else
            {
                kombiresim.setBackgroundResource(R.drawable.acikanim);
                AnimationDrawable acikAnim=(AnimationDrawable) kombiresim.getBackground();
                acikAnim.start();
                //kombiresim.setImageResource(R.drawable.kapali);
                kombidurumu.setText("Kombi Durumu: Açýk");
            }
            simdikisaat.setText("Cihaz Saati: "+ogrenciJson.getString("simdikisaat"));
            //cihazip.setText("Kombinin Að Adresi: "+ogrenciJson.getString("ipno")+ ogrenciJson.getString("modunuz"));
            guncellemevarmi="Güncelle: " + ogrenciJson.getString("tarih");
            sayac++;
            if(guntarih.getText().toString().length()>=29) {
                //mesaj1.setText(guncellemevarmi.substring(29, 31) + guntarih.getText().toString().substring(29, 31));
                kars1 =Integer.valueOf(guntarih.getText().toString().substring(27, 29));
                kars2 =Integer.valueOf(guncellemevarmi.substring(27, 29));

                if (kars1== kars2) {
                    if (sayac >= 10) {
                        //mesaj1.setText("Kombi Cihazýnýz Sisteme Bilgi Göndermiyor! Lütfen cihazý Kontrol ediniz.");
                        //mesaj1.setTextColor(Color.RED);
                        mesajpencerefonk("Kombi Cihazýnýz Sisteme Bilgi Göndermiyor! Lütfen cihazý Kontrol ediniz.",0);
                        sayac = 1;
                    }
                }else{
                    sayac=1;
                }

            }
            if(renkdegis) {
                guntarih.setTextColor(Color.RED);
                guncelle.setTextColor(Color.RED);
                renkdegis=false;
            }else{
                guntarih.setTextColor(Color.BLUE);
                guncelle.setTextColor(Color.BLUE);
                renkdegis = true;
            }
            guntarih.setText(guncellemevarmi);
            guncelle.setText(guncellemevarmi);

            pildurum=Integer.valueOf(ogrenciJson.getString("pil"));
            nrf=Integer.valueOf(ogrenciJson.getString("nrf"));
            modunuz=Integer.valueOf(ogrenciJson.getString("modunuz"));
            //mesaj1.setText(ogrenciJson.getString("pil")+ogrenciJson.getString("nrf")+ogrenciJson.getString("modunuz")+String.valueOf(msjdurum)+String.valueOf(sayac));
            mesaj1.setText(ogrenciJson.getString("modunuz"));

                if (modunuz == 0) {
                    ///fonk ekle
                    if (msjdurum) {
                        mesajpencerefonk("Sistem Sizin Ayarladýðýnýz Günlük Programda Çalýþmaktadýr.", 0);
                    }
                    durum="0";
                    cihazip.setText(ogrenciJson.getString("ipno")+ " nolu IP adresinde; GÜNLÜK çalýþma modundadýr.");
                }
                if (modunuz >= 1 && modunuz<=7 ) {
                    ///fonk ekle
                    if (msjdurum) {
                        mesajpencerefonk("Sistem Sizin Ayarladýðýnýz Haftalýk Programa Göre Çalýþmaktadýr.", 0);
                    }
                    cihazip.setText(ogrenciJson.getString("ipno")+ " nolu IP adresinde; HAFTALIK çalýþma modundadýr.");
                }

                if (modunuz == 8) {
                    ///fonk ekle
                    if (msjdurum) {
                        mesajpencerefonk("Sistem Sizin Ayarladýðýnýz Evde Yokum Modunda Çalýþmaktadýr.", 0);
                    }
                    cihazip.setText(ogrenciJson.getString("ipno")+ " nolu IP adresinde; EVDE YOKUM çalýþma modundadýr.");
                }
                if (modunuz == 9) {
                    ///fonk ekle
                    if (msjdurum) {
                        mesajpencerefonk("Sistem Sizin Ayarladýðýnýz Misafir Modunda Çalýþmaktadýr.", 0);
                    }
                    cihazip.setText(ogrenciJson.getString("ipno")+ " nolu IP adresinde; MÝSAFÝR çalýþma modundadýr.");
                }
                if (modunuz == 10) {
                    ///fonk ekle
                    if (msjdurum) {
                        mesajpencerefonk("Sistem Sizin Ayarladýðýnýz Hasta Var Modunda Çalýþmaktadýr.", 0);
                    }
                    cihazip.setText(ogrenciJson.getString("ipno")+ " nolu IP adresinde; HASTA VAR çalýþma modundadýr.");
                }
                if (modunuz == 11) {
                    ///fonk ekle
                    if (msjdurum) {
                        mesajpencerefonk("Sistem Sizin Kapalý Modunda Çalýþmaktadýr.", 0);
                    }
                        cihazip.setText(ogrenciJson.getString("ipno")+ " nolu IP adresinde; KAPALI çalýþma modundadýr.");
                }
            if (msjdurum) {
                if (nrf <= 50) {
                    ///fonk ekle
                    mesajpencerefonk("Sýcaklýk Sensörü kombiye çok uzakta. Lütfen Daha Yakýna Getiriniz", 0);
                }

                if (pildurum <= 30) {
                    ///fonk ekle
                    mesajpencerefonk("Sýcaklýk Sensörü Pilini Deðiþtirmeniz Gerekiyor. Pil Durumu %" + ogrenciJson.getString("pil"), 0);
                }
            }

            if(sayac%10==0){
                //sayac=1;
                msjdurum=true;
            }else{
                msjdurum=false;
            }
            if (modunuz==11){
                sistemdurumusw.setChecked(false);
            }else{
                sistemdurumusw.setChecked(true);
            }
            if(sistemdurumusw.isChecked()){
                sistemdurumusw.setText("Sitem Açýk");
            }else{
                sistemdurumusw.setText("Sistem Kapalý");
            }



        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public  void threadcalistir(){
        if (internetBaglantisiVarMi()) {
        //fetchJsonTask a = new fetchJsonTask();

        //a.execute(URL);
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
        Toast.makeText(getApplicationContext(), "Güncelleniyor. Ýþlemin Tamamlanmasý Ýçin 5 Saniye Bekleyiniz.", Toast.LENGTH_SHORT).show();
        t6.start();
        } else {
            Toast.makeText(getApplicationContext(), "Internet Baðlantýnýz yok", Toast.LENGTH_SHORT).show();
        }
    }
    public  void threadcalistir2(){
        if (internetBaglantisiVarMi()) {
        //fetchJsonTask a = new fetchJsonTask();

        //a.execute(URL);
        Thread t6 = new Thread() {
            public void run() {

                try {
                    //sleep(5000);
                    fetchJsonTask b = new fetchJsonTask();
                    Thread.sleep(1000);
                    b.execute(URL2);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    //finish();
                }

            }
            //public void finish(){

            //}


        };
        Toast.makeText(getApplicationContext(), "Güncelleniyor. Ýþlemin Tamamlanmasý Ýçin 5 Saniye Bekleyiniz.", Toast.LENGTH_SHORT).show();
        t6.start();
        } else {
            Toast.makeText(getApplicationContext(), "Internet Baðlantýnýz yok", Toast.LENGTH_SHORT).show();
        }
    }
    public  void threadcalistir3(){
        if (internetBaglantisiVarMi()) {
        //fetchJsonTask a = new fetchJsonTask();

        //a.execute(URL);
        Thread t6 = new Thread() {
            public void run() {

                try {
                    //sleep(5000);
                    fetchJsonTask b = new fetchJsonTask();
                    Thread.sleep(1000);
                    b.execute(URL3);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    //finish();
                }

            }
            //public void finish(){

            //}


        };
            Toast.makeText(getApplicationContext(), "Güncelleniyor. Ýþlemin Tamamlanmasý Ýçin 5 Saniye Bekleyiniz.", Toast.LENGTH_SHORT).show();
            t6.start();
        } else {

            Toast.makeText(getApplicationContext(), "Internet Baðlantýnýz yok", Toast.LENGTH_SHORT).show();

        }
    }
}
