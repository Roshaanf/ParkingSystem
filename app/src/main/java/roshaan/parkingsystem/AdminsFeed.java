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

public class AdminsFeed extends AppCompatActivity implements MyFeedBacks.MyFeedBackNoCommunicator, MySelectedFeedBack.FeedbackNumberCommunicator {

    FragmentManager mng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admins_feed);

        mng = getSupportFragmentManager();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater m = getMenuInflater();
        m.inflate(R.menu.admins_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.adminsViewFeedback) {

            MyFeedBacks sf = new MyFeedBacks();
            FragmentTransaction ftr = mng.beginTransaction();
            ftr.replace(R.id.forAdminsFragments, sf, "A");
            ftr.addToBackStack("A");
            ftr.commit();
        }

        if (item.getItemId() == R.id.adminsBookings) {

            MyBookings sf = new MyBookings();
            FragmentTransaction ftr = mng.beginTransaction();
            ftr.replace(R.id.forAdminsFragments, sf, "B");
            ftr.addToBackStack("B");
            ftr.commit();

        }

        if (item.getItemId() == R.id.adminsUsers) {

            Users sf = new Users();
            FragmentTransaction ftr = mng.beginTransaction();
            ftr.replace(R.id.forAdminsFragments, sf, "C");
            ftr.addToBackStack("C");
            ftr.commit();
        }
        if (item.getItemId() == R.id.adminsLogout) {

            FirebaseAuth.getInstance().signOut();

            finish();
            startActivity(new Intent(this, Login.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void sendFeedBackNo(String feedbackNo) {

        Bundle b = new Bundle();
        b.putString(UsersFeed.mySelectedFeedbackKey, feedbackNo);

        MySelectedFeedBack sc = new MySelectedFeedBack();
        sc.setArguments(b);

        FragmentTransaction ftr = mng.beginTransaction();
        ftr.replace(R.id.forAdminsFragments, sc, "K");
        ftr.addToBackStack("K");
        ftr.commit();
    }

    @Override
    public void sendFeedbackNo(String FeedbackNo) {

        Bundle b = new Bundle();
        b.putString(UsersFeed.mySelectedFeedbackKey, FeedbackNo);

        AdminReply sc = new AdminReply();
        sc.setArguments(b);

        FragmentTransaction ftr = mng.beginTransaction();
        ftr.replace(R.id.forAdminsFragments, sc, "L");
        ftr.addToBackStack("L");
        ftr.commit();
    }
}
