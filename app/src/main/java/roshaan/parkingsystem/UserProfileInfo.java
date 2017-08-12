package roshaan.parkingsystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserProfileInfo extends AppCompatActivity {

    RadioButton radio;
    RadioGroup rGroup;
    EditText mobilNo;
    Button submit;
    DatabaseReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_info);

        rGroup=(RadioGroup) findViewById(R.id.radioGroup);
        mobilNo=(EditText) findViewById(R.id.userProfileMobileNo);
        submit=(Button) findViewById(R.id.userProfileSubmitBtn);

    }

    public void submit(View v){

        if(!TextUtils.isEmpty(mobilNo.getText())){

            radio= (RadioButton) findViewById(rGroup.getCheckedRadioButtonId());
            String Gender=radio.getText().toString();
            String MobileNo=mobilNo.getText().toString();

            ref= FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            ref.child("MobileNo").setValue(MobileNo);
            ref.child("Gender").setValue(Gender);

            finish();
            startActivity(new Intent(this,UsersFeed.class));

        }
        else{

            Toast.makeText(this,"Fill all fields",Toast.LENGTH_LONG).show();
        }
    }
}
