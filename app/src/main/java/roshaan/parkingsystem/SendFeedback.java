package roshaan.parkingsystem;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class SendFeedback extends Fragment {

    EditText title;
    EditText description;
    Button send;
    DatabaseReference ref;
    int FeedbackNo = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_send_feedback, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        title = (EditText) getView().findViewById(R.id.sendFeedbackTitle);
        description = (EditText) getView().findViewById(R.id.sendFeedbackDescription);
        send = (Button) getView().findViewById(R.id.feedbackSendButton);
        ref = FirebaseDatabase.getInstance().getReference().child("Feedbacks");


        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String value = dataSnapshot.getKey().toString();
                FeedbackNo = Integer.parseInt(value);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Title = String.valueOf(title.getText());
                String Description = String.valueOf(description.getText());

                FeedbackNo++;
                System.out.println("kkk2" + FeedbackNo);
                //setting reference for this feedback
                ref = FirebaseDatabase.getInstance().getReference().child("Feedbacks")
                        .child(String.valueOf(FeedbackNo));

                if (!TextUtils.isEmpty(title.getText()) &&
                        !TextUtils.isEmpty(description.getText())) {

                    ref.child("Title").setValue(Title);
                    ref.child("Description").setValue(Description);
                    ref.child("Uid").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    ref.child("FeedBackNo").setValue(String.valueOf(FeedbackNo));

                    Toast.makeText(getActivity(), "Feedback successfully send", Toast.LENGTH_SHORT).show();
                    //resetting text
                    title.setText("");
                    description.setText("");

                } else {

                    Toast.makeText(getActivity(), "Fill all fields", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
