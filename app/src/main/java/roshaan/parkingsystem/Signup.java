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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Signup extends AppCompatActivity {

    EditText email;
    EditText password;
    Button signupButton;
    FirebaseAuth auth;
    DatabaseReference ref;
    EditText fullName;
    EditText mobileNo;
    RadioGroup rGroup;
    TextView goForLogin;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        auth=FirebaseAuth.getInstance();
        email=(EditText) findViewById(R.id.signupEmail);
        password =(EditText) findViewById(R.id.signupPassword);
        signupButton=(Button) findViewById(R.id.signupButton);
        fullName=(EditText) findViewById(R.id.signupFullName);
        rGroup=(RadioGroup) findViewById(R.id.signUpRadioGroup);
        mobileNo=(EditText) findViewById(R.id.signUpMobileNo);
        progressDialog=new ProgressDialog(this);
        goForLogin=(TextView) findViewById(R.id.alreadyHavAccnt);
    }



    public void signupClicked(View v){


        if(!TextUtils.isEmpty(email.getText())&&
            !    TextUtils.isEmpty(password.getText())&&
             !   TextUtils.isEmpty(fullName.getText())&&
                !TextUtils.isEmpty(mobileNo.getText())){

            progressDialog.setMessage("Signing up please wait");
            progressDialog.show();

            String Email= email.getText().toString();
            String Password=password.getText().toString();

            auth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    //if signup occur succesfully
                    if(task.isSuccessful()){

                        RadioButton radio=(RadioButton) findViewById(rGroup.getCheckedRadioButtonId());
                        String gender=radio.getText().toString();
                        String FullName=fullName.getText().toString();
                        String MobileNo=mobileNo.getText().toString();

                        ref= FirebaseDatabase.getInstance().getReference().child("Users").child(auth.getCurrentUser().getUid());
                        ref.child("FullName").setValue(FullName);
                        ref.child("Uid").setValue(auth.getCurrentUser().getUid());
                        ref.child("Type").setValue("User");
                        ref.child("MobileNo").setValue(MobileNo);
                        ref.child("Gender").setValue(gender);

                        progressDialog.dismiss();

                        //closing this activity and opening user info activity
                        finish();

                        startActivity(new Intent(Signup.this,UsersFeed.class));

                    }
                    //catching all exceptions tthat can occur is sigunup is not done correctly
                    else{

                        try {
                            throw task.getException();
                        } catch(FirebaseAuthWeakPasswordException e) {

                            progressDialog.dismiss();
                            Toast.makeText(Signup.this,"Signup failed password must be 6 characters long",Toast.LENGTH_LONG).show();

                        } catch(FirebaseAuthInvalidCredentialsException e) {
                            progressDialog.dismiss();
                            Toast.makeText(Signup.this,"Signup failed! Bad structure of email",Toast.LENGTH_LONG).show();
                        } catch(FirebaseAuthUserCollisionException e) {

                            progressDialog.dismiss();
                            Toast.makeText(Signup.this,"Signup failed id already in use choose another one",Toast.LENGTH_LONG).show();
                        } catch(Exception e) {

                        }
                    }


                }
            });

        }
        else{

            Toast.makeText(this, "Fill all fields",Toast.LENGTH_LONG).show();

        }
    }

    public void goForLogin(View v) {

        finish();

        startActivity(new Intent(this,Login.class));
    }
}
