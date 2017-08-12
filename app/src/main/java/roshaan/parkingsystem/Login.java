package roshaan.parkingsystem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class Login extends AppCompatActivity {

    EditText email;
    EditText password;
    Button loginButton;
    FirebaseAuth auth;
    DatabaseReference ref;
    TextView goForSignup;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText) findViewById(R.id.loginEmail);
        password = (EditText) findViewById(R.id.loginPassword);
        loginButton = (Button) findViewById(R.id.loginButton);
        goForSignup = (TextView) findViewById(R.id.loginDontHaveAccount);
        auth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);


    }

    public void loginClicked(View v) {


        if (!TextUtils.isEmpty(email.getText()) &&
                !TextUtils.isEmpty(password.getText())) {

            progressDialog.setMessage("Loggin in please wait");
            progressDialog.show();

            String Email = email.getText().toString();
            String Password = password.getText().toString();

            auth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    //chcking if login is succesfull
                    if (task.isSuccessful()) {


                        ref = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Type");

                        ref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                String type = dataSnapshot.getValue(String.class);

                                //agr type user he to user feed kholdo
                                if (type != null && type.equals("User")) {

                                    progressDialog.dismiss();
                                    finish();
                                    startActivity(new Intent(Login.this, UsersFeed.class));
                                }
                                //agr type admin he to admin feed
                                else if (type != null) {

                                    progressDialog.dismiss();
                                    finish();
                                    startActivity(new Intent(Login.this, AdminsFeed.class));

                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(Login.this, "Incorrect user name or password", Toast.LENGTH_SHORT).show();


                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                    }
                    //means email and password is wrong
                    else {
                        progressDialog.dismiss();
                        Toast.makeText(Login.this, "Incorrect Email or Password", Toast.LENGTH_LONG).show();
                    }


                }
            });

        } else {

            Toast.makeText(this, "Fill all fields", Toast.LENGTH_LONG).show();
        }

    }

    public void openSignup(View v) {

        finish();

        startActivity(new Intent(this, Signup.class));
    }
}
