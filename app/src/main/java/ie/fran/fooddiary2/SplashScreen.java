package ie.fran.fooddiary2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Thread mtt = new Thread() {
            public void run() {
                try {
                    sleep(1000);
                    Intent loginFirst = new Intent(SplashScreen.this, Signup.class);
                    startActivity(loginFirst);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        mtt.start();
    }
}