package roshaan.parkingsystem;


import android.app.ProgressDialog;
import android.content.Intent;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.PasswordAuthentication;
import java.util.Properties;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    DatabaseReference ref;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        auth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        progressDialog.setMessage("Getting infromation please wait");
        progressDialog.show();

        //if no one is logged in
        if (auth.getCurrentUser() == null) {

            progressDialog.dismiss();
            finish();
            startActivity(new Intent(this, Login.class));
        }
        //if anyone is logged in
        else {

            // now we will check who is logged in user or admin and will show corresponding feeds

            ref = FirebaseDatabase.getInstance().getReference().child("Users").child(auth.getCurrentUser().getUid()).child("Type");

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {


                    String type = dataSnapshot.getValue(String.class);

                    //agr type user he to user feed kholdo
                    if (type != null && type.equals("User")) {

                        progressDialog.dismiss();
                        finish();
                        startActivity(new Intent(MainActivity.this, UsersFeed.class));
                    }
                    //agr type admin he to admin feed
                    else if (type != null) {

                        progressDialog.dismiss();
                        finish();
                        startActivity(new Intent(MainActivity.this, AdminsFeed.class));

                    } else {

                        Toast.makeText(MainActivity.this, "No User loggedd in", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                        finish();
                        startActivity(new Intent(MainActivity.this, Login.class));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }
}
