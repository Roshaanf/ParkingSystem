package roshaan.parkingsystem;


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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class AdminReply extends Fragment {

    EditText replyText;
    Button send;
    DatabaseReference ref;
    String feedbackNo;
    int replyNo = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        feedbackNo = getArguments().getString(UsersFeed.mySelectedFeedbackKey);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_reply, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        send = (Button) getView().findViewById(R.id.AdminReplySend);
        replyText = (EditText) getView().findViewById(R.id.AdminReplyEditText);

        if (feedbackNo != null) {

            //checking the number for next reply
            ref = FirebaseDatabase.getInstance().getReference().child("Feedbacks").child(feedbackNo).child("AdminReply");

            ref.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    String value = dataSnapshot.getKey();
                    if (value != null)
                        replyNo = Integer.parseInt(value);
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

        } else {

            Toast.makeText(getActivity(), "Feedback has been deleted", Toast.LENGTH_SHORT).show();
        }


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                replyNo++;
                String text = String.valueOf(replyText.getText());

                if (!TextUtils.isEmpty(replyText.getText())) {
                    ref = FirebaseDatabase.getInstance().getReference().child("Feedbacks").child(feedbackNo).child("AdminReply");
                    ref.child(String.valueOf(replyNo)).child("Reply").setValue(text);

                    Toast.makeText(getActivity(), "Your reply has been successfully sent", Toast.LENGTH_SHORT).show();
                    //resetting text
                    replyText.setText("");
                }

            }
        });
    }
}
