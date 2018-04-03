package com.example.user.qrcode;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Looper;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    SurfaceView QRcord;
    TextView textView;
    TextView textQRcodeRead;
    BarcodeDetector barcodeDetector;
    CameraSource cameraSource;
    Toolbar toolbar;
    DrawerLayout drawerlayout;
    NavigationView liftmenu;
    Button QRbutton;
    String url;
//    String url="http://10.0.2.2/testget.php?age=";
    final int CameraID = 1001;



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        QRcord = (SurfaceView) findViewById(R.id.QRcode);
        textView = (TextView) findViewById(R.id.textview1);
        liftmenu = (NavigationView) findViewById(R.id.liftbutton_main);
        drawerlayout = (DrawerLayout) findViewById(R.id.drawerlayout_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        QRbutton = (Button) findViewById(R.id.QRbutton);
        textQRcodeRead = (TextView) findViewById(R.id.textview2);
        settoolbar();
        setliftmenu();


        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();
        cameraSource = new CameraSource
                .Builder(this, barcodeDetector)
                .setRequestedPreviewSize(640, 480)
                .setAutoFocusEnabled(true)
                .build();

        QRcord.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.CAMERA}, CameraID);
                    return;
                }
                try {
                    cameraSource.start(QRcord.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrcodes = detections.getDetectedItems();
                if (qrcodes.size() != 0)
                    textView.post(new Runnable() {
                        @Override
                        public void run() {
                            Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                            vibrator.vibrate(500);
                            textView.setText(qrcodes.valueAt(0).displayValue);
                            runCode();

                        }

                        private void runCode() {

                        }
                    });
            }
        });

        QRbutton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
//              String url="http://10.0.2.2/testget.php?age=20";
//                url =  url+textQRcodeRead.getText();
                url ="http://10.0.2.2/QRcodeTest.php?qrcodeid=";
                url =  url+textView.getText().toString();
                new Thread(new Runnable(){

                    @Override
                    public void run() {
                        Looper.prepare();
                         //TODO Auto-generated method stub
                        HttpClient client = new DefaultHttpClient();
                        HttpGet myget = new HttpGet(url);
                        try {
                            HttpResponse response = client.execute(myget);
//                            textQRcodeRead.setText(String.valueOf(response));
                        } catch (Exception e) {

                            e.printStackTrace();
                        }
                        Looper.loop();
                    }}).start();

                new TransTask().execute(url);
//                textQRcodeRead.setText("");
            }
        });
    }
    //Json 解析
    class TransTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... params) {
            StringBuilder sb = new StringBuilder();
            try {
                URL url = new URL(params[0]);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(url.openStream()));
                String line = in.readLine();
                while (line != null) {
                    Log.d("HTTP", line);
                    sb.append(line);
                    line = in.readLine();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return sb.toString();
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("JSON", s);
            parseJSON(s);
        }

        private void parseJSON(String s) {
            ArrayList<Json> trans = new ArrayList<>();

            try {
                JSONArray array = new JSONArray(s);
                for (int i=0; i<array.length(); i++){
                    JSONObject obj = array.getJSONObject(i);
                    int id = obj.getInt("id");
                    int age = obj.getInt("age");
                    int checkid = obj.getInt("checkid");
                    String time = obj.getString("time");
                    String username = obj.getString("username");
                    Json t = new Json(id, age, checkid, username,time);
                    textQRcodeRead.setText(String.valueOf(checkid));
                    trans.add(t);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    //左側表單menu
    private void setliftmenu() {
        liftmenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // 點選時收起選單
                drawerlayout.closeDrawer(GravityCompat.START);
                // 取得選項id
                int id = item.getItemId();
                // 依照id判斷點了哪個項目並做相應事件
                if (id == R.id.action_camera) {
                    // 按下「剪票模式」要做的事
                    Toast.makeText(MainActivity.this, "目前顯示頁面：剪票模式", Toast.LENGTH_SHORT).show();
                }
                else if (id == R.id.action_qrcode) {
                    // 按下「QRcode模式」要做的事
                    //傳送數值
                    // Bundle
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, QRcodeActivity.class);
                    MainActivity.this.finish();
                    Bundle bundle = new Bundle();
                    bundle.putString("QRcodeID",textView.getText().toString());
                    intent.putExtras(bundle);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
    }

    //toolbar 設定
    private void settoolbar() {
        toolbar.setTitle("QR Code");

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return true;
            }
        });
        toolbar.inflateMenu(R.menu.menu);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);//toolbar 上面


        ActionBarDrawerToggle mActionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerlayout, toolbar, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mActionBarDrawerToggle.syncState();
    }


    //權限
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CameraID: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }try{
                        cameraSource.start(QRcord.getHolder());
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }
            break;
        }
    }

}
