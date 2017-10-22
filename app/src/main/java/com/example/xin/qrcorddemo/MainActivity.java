package com.example.xin.qrcorddemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {

    Button scanBt;
    TextView showResultTxv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView(){
        //获取xml布局中的组件实例
        scanBt = (Button) findViewById(R.id.scanBt);
        showResultTxv = (TextView) findViewById(R.id.showResultTxv);

        //为button设置监听点击
        scanBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showResultTxv.setText("Test Message For onClick");
                customScan();
            }
        });
    }
    //自定义扫描界面
    //设置自定义的activity：setCaptureActivity(CustomScanActivity.class)
    public void customScan(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // 申请权限
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, 100);
            return;
        }
        new IntentIntegrator(this).setOrientationLocked(false)
                .setPrompt("")
                .setCaptureActivity(CustomScanActivity.class)
                .initiateScan();//开始扫描
    }

    //通过onActivityResult回调方法获取扫描返回值

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult intentResult = IntentIntegrator.
                parseActivityResult(requestCode,resultCode,data);
        if (intentResult != null){
            if (intentResult.getContents() == null){
                Toast.makeText(MainActivity.this,"内容为空",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(MainActivity.this,"扫描成功",Toast.LENGTH_SHORT).show();
                //ScanResult为获取到的字符串
                String ScanResult = intentResult.getContents();
                showResultTxv.setText(ScanResult);
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //获得授权
                customScan();
            }else {
                // 被禁止授权
                Toast.makeText(MainActivity.this, "请至权限中心打开本应用的相机访问权限", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
