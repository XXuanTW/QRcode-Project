package com.example.user.qrcode;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.EnumMap;
import java.util.Map;

public class QRcodeActivity extends AppCompatActivity {

    Toolbar toolbar;
    DrawerLayout drawerlayout;
    NavigationView liftmenu;
    TextView textView;
    ImageView imgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        drawerlayout= (DrawerLayout)findViewById(R.id.drawerlayout_QRcode);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        liftmenu=(NavigationView)findViewById(R.id.liftbutton_QRcode);
        textView=(TextView)findViewById(R.id.QRcodeIDRead);
        imgView = (ImageView) findViewById(R.id.QRcodeImage);
        settoolbar();
        setliftmenu();

        Bundle bundle = this.getIntent().getExtras();
        String QRcodeRead = bundle.getString("QRcodeID");
        textView.setText(QRcodeRead);
        QRcodeImageView();

    }

    private void QRcodeImageView() {
        // QR code 的內容
        Bundle bundle = this.getIntent().getExtras();
        String QRcodeRead = bundle.getString("QRcodeID");
        // QR code 寬度
        int QRCodeWidth = 200;
        // QR code 高度
        int QRCodeHeight = 200;
        // QR code 內容編碼
        Map<EncodeHintType, Object> hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        MultiFormatWriter writer = new MultiFormatWriter();
        try
        {
            // 容錯率姑且可以將它想像成解析度，分為 4 級：L(7%)，M(15%)，Q(25%)，H(30%)
            // 設定 QR code 容錯率為 H
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

            // 建立 QR code 的資料矩陣
            BitMatrix result = writer.encode(QRcodeRead, BarcodeFormat.QR_CODE, QRCodeWidth, QRCodeHeight, hints);
            // ZXing 還可以生成其他形式條碼，如：BarcodeFormat.CODE_39、BarcodeFormat.CODE_93、BarcodeFormat.CODE_128、BarcodeFormat.EAN_8、BarcodeFormat.EAN_13...

            //建立點陣圖
            Bitmap bitmap = Bitmap.createBitmap(QRCodeWidth, QRCodeHeight, Bitmap.Config.ARGB_8888);
            // 將 QR code 資料矩陣繪製到點陣圖上
            for (int y = 0; y<QRCodeHeight; y++)
            {
                for (int x = 0;x<QRCodeWidth; x++)
                {
                    bitmap.setPixel(x, y, result.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }


            // 設定為 QR code 影像
            imgView.setImageBitmap(bitmap);
        }
        catch (WriterException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    //左側表單
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
                    Intent intent = new Intent();
                    intent.setClass(QRcodeActivity.this, MainActivity.class);
                    startActivity(intent);
                    QRcodeActivity.this.finish();
                    return true;
                }
                else if (id == R.id.action_qrcode) {
                    // 按下「QRcode模式」要做的事
                    Toast.makeText(QRcodeActivity.this, "目前顯示頁面：QRcode模式", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });

    }
    //toolbar設定
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
        getSupportActionBar().setHomeButtonEnabled(true);


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
}
