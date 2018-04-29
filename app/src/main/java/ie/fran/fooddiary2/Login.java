package ie.fran.fooddiary2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class Login extends AppCompatActivity {
    private EditText password;
    private EditText email;
    private StorageReference mStorageRef;
    private DatabaseReference mydatabaseref;
    private FirebaseAuth mAuth;
    //Give your SharedPreferences file a name and save it to a static variable
    public static final String PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();
        mydatabaseref = FirebaseDatabase.getInstance().getReference().child("users");
    }


    public void login(View view) {

        final String emailentered = email.getText().toString().trim();
        String passwordentered = password.getText().toString().trim();
        if (!emailentered.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")) {
            email.setError("Invalid Email Address");
        } else {
            if (!TextUtils.isEmpty(emailentered) && !TextUtils.isEmpty(passwordentered)) {
                mAuth.signInWithEmailAndPassword(emailentered, passwordentered).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //check if they are already a user
                            alreadyExists();
                            finish();
                        } else {
                            //start new intent so they can use the app
                            Toast.makeText(getApplicationContext(), "You Need to Signup", Toast.LENGTH_SHORT).show();

                            SharedPreferences settings = getSharedPreferences(Login.PREFS_NAME, 0);
                           //Get "hasLoggedIn" value. If the value doesn't exist yet false is returned
                            boolean hasLoggedIn = settings.getBoolean("hasLoggedIn", false);


                        }
                    }
                });
            }
        }

    }

    public void alreadyExists() {

        final String userid = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        mydatabaseref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(userid)) {
//User has successfully logged in, save this information
// We need an Editor object to make preference changes.
                    SharedPreferences settings = getSharedPreferences(Login.PREFS_NAME, 0); // 0 - for private mode
                    SharedPreferences.Editor editor = settings.edit();

//Set "hasLoggedIn" to true
                    editor.putBoolean("hasLoggedIn", true);
// Commit the edits!
                    editor.commit();
                    //start new intent so they can use the app
                    Toast.makeText(getApplicationContext(), "Welcome Back. ", Toast.LENGTH_SHORT).show();

                    Intent home = new Intent(Login.this, AllRecipies.class);
                    startActivity(home);
//                    finish();
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void signupbutton(View view) {
        Intent loginFirst = new Intent(Login.this,Signup.class);
        startActivity(loginFirst);
        finish();
    }
}
