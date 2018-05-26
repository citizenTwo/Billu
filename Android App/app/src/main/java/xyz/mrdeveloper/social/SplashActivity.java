package xyz.mrdeveloper.social;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        FirstActivityLauncher logoLauncher = new FirstActivityLauncher();
        logoLauncher.start();
    }

    private class FirstActivityLauncher extends Thread {
        public void run() {
            try {
                sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Intent i = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(i);
        }
    }
}