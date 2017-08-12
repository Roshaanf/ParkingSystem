package roshaan.parkingsystem;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MySelectedFeedBack extends Fragment {

        String myFeedBackNo;
        TextView description;
        Button delete;
        DatabaseReference ref;
    DatabaseReference ref1;
        RecyclerView recyclerView;
        ArrayList<String> adminReply;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        myFeedBackNo=getArguments().getString(UsersFeed.mySelectedFeedbackKey);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_selected_feed_back, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        delete=(Button) getView().findViewById(R.id.MySelectedFeedbackDelete);
        description=(TextView) getView().findViewById(R.id.MySelectedFeedbackDescription);
        recyclerView=(RecyclerView) getView().findViewById(R.id.MyFeedbacksAdminReply);
        adminReply=new ArrayList<>();

        if(getActivity().getClass().getName().equals("roshaan.parkingsystem.AdminsFeed")){

            delete.setText("Reply");
        }

        if(myFeedBackNo==null){

            Toast.makeText(getActivity(),"Feedback has been deleted",Toast.LENGTH_LONG).show();

        }
        else{

            ref=FirebaseDatabase.getInstance().getReference().child("Feedbacks").child(myFeedBackNo).child("Description");

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String Description=dataSnapshot.getValue(String.class);

                    if(Description!=null){

                        description.setText(Description);
                    }
                    else{
                        description.setText("");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }


        ///Retrieving reply

        final MyFeedbacksAdminReplyAdapter adapter=new MyFeedbacksAdminReplyAdapter(getActivity(),adminReply);
       recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);


        ref1=FirebaseDatabase.getInstance().getReference().child("Feedbacks").child(myFeedBackNo);

        ref1.child("AdminReply").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                adminReply.clear();

                Iterable<DataSnapshot> child=dataSnapshot.getChildren();

                for(DataSnapshot ch:child){

                    adminsReplyStructure reply=ch.getValue(adminsReplyStructure.class);
                    adminReply.add(reply.Reply);

                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        ///DELETE BUTTON FUNCTIONALITY

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(getActivity().getClass().getName().equals("roshaan.parkingsystem.UsersFeed")) {
                    if (myFeedBackNo != null) {

                      DatabaseReference  ref = FirebaseDatabase.getInstance().getReference().child("Feedbacks").child(myFeedBackNo);
                        ref.removeValue();

                        Toast.makeText(getActivity(), "Feedback has been successfully deleted", Toast.LENGTH_SHORT).show();

                        getActivity().getSupportFragmentManager().popBackStack();
                    }
                }

                else if(getActivity().getClass().getName().equals("roshaan.parkingsystem.AdminsFeed")){

                        communicator.sendFeedbackNo(myFeedBackNo);

                }



            }
        });



    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(getActivity().getClass().getName().equals("roshaan.parkingsystem.AdminsFeed"))
        communicator= (FeedbackNumberCommunicator) context;
    }

    FeedbackNumberCommunicator communicator;
    interface FeedbackNumberCommunicator{

      void  sendFeedbackNo(String FeedbackNo);
    }
}



class MyFeedbacksAdminReplyAdapter extends RecyclerView.Adapter<MyFeedbacksAdminReplyViewHolder>
{
    LayoutInflater lf;
    Context context;
    ArrayList<String> data;


    public MyFeedbacksAdminReplyAdapter(Context context,ArrayList<String> data){


        this.context=context;
        this.data=data;
        lf=LayoutInflater.from(context);
    }

    @Override
    public MyFeedbacksAdminReplyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View v=lf.inflate(R.layout.signle_row,parent,false);

        MyFeedbacksAdminReplyViewHolder holder=new MyFeedbacksAdminReplyViewHolder(v,data);


        return holder;
    }


    @Override
    public void onBindViewHolder(MyFeedbacksAdminReplyViewHolder holder, int position) {


        //also capitalizi
        holder.name.setText(data.get(position).toString().substring(0,1).toUpperCase()+ data.get(position).toString().substring(1));

    }

    @Override
    public int getItemCount() {

        return data.size();
    }
}


class MyFeedbacksAdminReplyViewHolder extends RecyclerView.ViewHolder {

    DatabaseReference ref;
    TextView name;
    public  ArrayList<String> data;

    //This will be called inside onCreateViewHolder
    //parameter interView contains the inflated single row view
    //here we will find resource ids
    public MyFeedbacksAdminReplyViewHolder(View itemView, ArrayList<String> data) {
        super(itemView);
        name=(TextView) itemView.findViewById(R.id.singleRowText);
        this.data=data;


    }


}

class adminsReplyStructure{
    String Reply;

    public adminsReplyStructure() {
    }

    public String getReply() {
        return Reply;
    }

    public void setReply(String reply) {
        Reply = reply;
    }
}