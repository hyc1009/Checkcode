package com.example.he.alltest;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    private boolean rootCommand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        String apkRoot = "chmod 777 " + getPackageCodePath();
//        rootCommand = RootCommand(apkRoot);
        initView();
    }

    private void initView() {
        CheckCodeView checkCodeView = (CheckCodeView) findViewById(R.id.checkCodeView);
        TextView tvStart =  findViewById(R.id.start);

        final TextView textView = (TextView) findViewById(R.id.text);

        CountDownTimer timer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long l) {
                textView.setText(l/1000 + "秒后可以重发");
            }

            @Override
            public void onFinish() {
                textView.setText("获取验证码");
            }
        };

        checkCodeView.setOnCheckcodeCompleteListener(new CheckCodeView.OnInputCompleteListener() {
            @Override
            public void setInputCompleteListener(StringBuilder s) {
                Log.e("得到的验证码==", s.toString());
            }
        });
        timer.start();
    }

    public void go(View view) {
        if (rootCommand) {

            Intent intent = this.getPackageManager().getLaunchIntentForPackage("com.UCMobile");
            startActivity(intent);
            try {
                Process process = Runtime.getRuntime().exec("adb shell input tap 362 353");
            } catch (IOException e) {
                e.printStackTrace();
            }
//            Intent intent1 = new Intent(MainActivity.this, AdbService.class);
//            startService(intent1);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    public boolean RootCommand(String command) {
        Process process = null;
        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(command + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (Exception e) {
            Log.d("*** DEBUG ***", "ROOT REE" + e.getMessage());
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception e) {
            }
        }
        Log.d("*** DEBUG ***", "Root SUC ");
        return true;
    }

}
