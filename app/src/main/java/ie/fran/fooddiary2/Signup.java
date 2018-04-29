package ie.fran.fooddiary2;

import android.content.Intent;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.regex.Pattern;

public class Signup extends AppCompatActivity {
    private EditText email, password;
    private StorageReference mStorageRef;
    private DatabaseReference mydatabaseref;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        email= findViewById(R.id.email);
        password= findViewById(R.id. password);
        mAuth = FirebaseAuth.getInstance();
        mydatabaseref = FirebaseDatabase.getInstance().getReference().child("users");

    }


    public void signupbutton(View view) {


        final String emailentered = email.getText().toString().trim();
        String passwordentered = password.getText().toString().trim();


        if (!emailentered.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")) {
            email.setError("A valid email is required.");
        }
        else {
            if (!TextUtils.isEmpty(emailentered) && !TextUtils.isEmpty(passwordentered)) {

                mAuth.createUserWithEmailAndPassword(emailentered, passwordentered).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            String userid = mAuth.getCurrentUser().getUid();
                            DatabaseReference currentuid = mydatabaseref.child(userid);
                            currentuid.child("Name").setValue(emailentered);
                            Toast.makeText(getApplicationContext(), "You are now signed up.", Toast.LENGTH_SHORT).show();
                            Intent loginFirst = new Intent(Signup.this,Login.class);
                            startActivity(loginFirst);
                            finish();
                        }
                    }

                });

            }
        }
    }

    public void loginbutton(View view) {
        Intent loginFirst = new Intent(Signup.this,Login.class);
        startActivity(loginFirst);
        finish();
    }

}
