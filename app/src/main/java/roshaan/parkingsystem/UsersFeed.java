package roshaan.parkingsystem;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

public class UsersFeed extends AppCompatActivity implements MyFeedBacks.MyFeedBackNoCommunicator, NewBooking.AreaCommunication{

    public static final String mySelectedFeedbackKey="Roshaan.MySelectedFeedback key";
    public static  final String areaCommunicatorKey="Roshaan..AreaComunicator";

    static FragmentManager mng;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_feed);
        mng=getSupportFragmentManager();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater m=getMenuInflater();
        m.inflate(R.menu.users_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {



        if(item.getItemId()==R.id.usersSendFeedback){

                SendFeedback sf=new SendFeedback();
            FragmentTransaction ftr=mng.beginTransaction();
            ftr.replace(R.id.forUsersFragments,sf,"A");
            ftr.addToBackStack("A");
            ftr.commit();
        }


        if(item.getItemId()==R.id.usersFeedback){

            MyFeedBacks sf=new MyFeedBacks();
            FragmentTransaction ftr=mng.beginTransaction();
            ftr.replace(R.id.forUsersFragments,sf,"B");
            ftr.addToBackStack("B");
            ftr.commit();
        }

        if(item.getItemId()==R.id.usersNewBooking){

            NewBooking sf=new NewBooking();
            FragmentTransaction ftr=mng.beginTransaction();
            ftr.replace(R.id.forUsersFragments,sf,"C");
            ftr.addToBackStack("C");
            ftr.commit();
        }
        if(item.getItemId()==R.id.usersMyBookings){

            MyBookings sf=new MyBookings();
            FragmentTransaction ftr=mng.beginTransaction();
            ftr.replace(R.id.forUsersFragments,sf,"D");
            ftr.addToBackStack("D");
            ftr.commit();
        }


        if(item.getItemId()==R.id.usersLogout){

            FirebaseAuth.getInstance().signOut();

            finish();
            startActivity(new Intent(this, Login.class));
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void sendFeedBackNo(String feedbackNo) {

        Bundle b=new Bundle();
        b.putString(mySelectedFeedbackKey,feedbackNo);

        MySelectedFeedBack sc=new MySelectedFeedBack();
        sc.setArguments(b);

        FragmentTransaction ftr=mng.beginTransaction();
        ftr.replace(R.id.forUsersFragments,sc,"E");
        ftr.addToBackStack("E");
        ftr.commit();
    }

    @Override
    public void areaCommunicator(String area) {


        Bundle b=new Bundle();
        b.putString(areaCommunicatorKey,area);

        Slots sc=new Slots();
        sc.setArguments(b);

        FragmentTransaction ftr=mng.beginTransaction();
        ftr.replace(R.id.forUsersFragments,sc,"F");
        ftr.addToBackStack("F");
        ftr.commit();
    }
}
