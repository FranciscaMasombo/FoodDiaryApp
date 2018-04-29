package ie.fran.fooddiary2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

public class Profile extends AppCompatActivity {
    private EditText password;
    private EditText email;
    private StorageReference mStorageRef;
    private DatabaseReference mydatabaseref;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar myt = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myt);

        setContentView(R.layout.activity_profile);
        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        mydatabaseref = FirebaseDatabase.getInstance().getReference().child("users");

        Button buttonLogout = (Button) findViewById(R.id.bb);
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                SharedPreferences mPreferences = getSharedPreferences("CurrentUser", MODE_PRIVATE);
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.remove("UserName");
                editor.remove("PassWord");
                editor.commit();
                Message myMessage = new Message();
                myMessage.obj = "NOTSUCCESS";
                handler.sendMessage(myMessage);
                finish();
            }
        });


    }


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String loginmsg = (String) msg.obj;
            if (loginmsg.equals("NOTSUCCESS")) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                intent.putExtra("LoginMessage", "Logged Out");
                startActivity(intent);
                removeDialog(0);
            }
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mm = getMenuInflater();
        mm.inflate(R.menu.the_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            Intent intent = new Intent(this, AllRecipies.class);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.nav_add) {
            Intent intent = new Intent(this, Add.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        // ...
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
    }

    public void Logouut(View view) {

        mAuth.signOut();
    }
}
