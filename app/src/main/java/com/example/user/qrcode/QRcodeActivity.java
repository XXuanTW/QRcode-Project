package com.example.user.qrcode;

import android.content.Intent;
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
import android.widget.Toast;

public class QRcodeActivity extends AppCompatActivity {

    Toolbar toolbar;
    DrawerLayout drawerlayout;
    NavigationView liftmenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        drawerlayout= (DrawerLayout)findViewById(R.id.drawerlayout_QRcode);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        liftmenu=(NavigationView)findViewById(R.id.liftbutton_QRcode);

        settoolbar();
        setliftmenu();
    }

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
