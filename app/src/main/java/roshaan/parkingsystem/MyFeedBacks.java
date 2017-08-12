package roshaan.parkingsystem;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyFeedBacks extends Fragment {

    RecyclerView recyclerView;
    DatabaseReference ref;
    ArrayList<String> data;
    ArrayList<MyFeedBacksStructure> feedBackCompleteData;
    Query query;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_feed_backs, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        data = new ArrayList<>();
        feedBackCompleteData = new ArrayList<>();
        recyclerView = (RecyclerView) getView().findViewById(R.id.MyFeedbacks);
        ref = FirebaseDatabase.getInstance().getReference().child("Feedbacks");


        if (getActivity().getClass().getName().equals("roshaan.parkingsystem.UsersFeed")) {

            query = ref.orderByChild("Uid").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
        } else if (getActivity().getClass().getName().equals("roshaan.parkingsystem.AdminsFeed")) {

            query = ref;
        }


        final MyFeedbacksAdapter adapter = new MyFeedbacksAdapter(getActivity(), data, feedBackCompleteData);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);


        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                data.clear();
                feedBackCompleteData.clear();

                Iterable<DataSnapshot> child = dataSnapshot.getChildren();

                for (DataSnapshot ch : child) {

                    MyFeedBacksStructure struct = ch.getValue(MyFeedBacksStructure.class);

                    data.add(struct.Title);
                    feedBackCompleteData.add(struct);
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    static MyFeedBackNoCommunicator communicator;

    interface MyFeedBackNoCommunicator {

        void sendFeedBackNo(String feedbackNo);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        communicator = (MyFeedBackNoCommunicator) context;
    }
}


class MyFeedbacksAdapter extends RecyclerView.Adapter<MyFeedbacksViewHolder> {
    LayoutInflater lf;
    Context context;
    ArrayList<String> data;
    ArrayList<MyFeedBacksStructure> feedBackCompleteData;

    public MyFeedbacksAdapter(Context context, ArrayList<String> data, ArrayList<MyFeedBacksStructure> studentCompleteData) {


        this.context = context;
        this.data = data;
        this.feedBackCompleteData = studentCompleteData;
        lf = LayoutInflater.from(context);
    }


    @Override
    public MyFeedbacksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View v = lf.inflate(R.layout.signle_row, parent, false);
        //sending inflatd view to ViewHolder so that it can find resource ids

        MyFeedbacksViewHolder holder = new MyFeedbacksViewHolder(v, data, feedBackCompleteData);

        //returning holder , holder now contains the ids of the layout
        //values will be assigned to these ids in onBindViewHolder
        return holder;
    }

    //Responsible to fill UI
    //The holder parameter here is from onCreateHolder's return statement so it contains ids
    @Override
    public void onBindViewHolder(MyFeedbacksViewHolder holder, int position) {


        //also capitalizi
        holder.name.setText(data.get(position).toString().substring(0, 1).toUpperCase() + data.get(position).toString().substring(1));

    }

    @Override
    public int getItemCount() {

        return data.size();
    }
}


class MyFeedbacksViewHolder extends RecyclerView.ViewHolder {

    DatabaseReference ref;
    TextView name;
    ArrayList<MyFeedBacksStructure> studentCompleteData;
    public ArrayList<String> data;

    //This will be called inside onCreateViewHolder
    //parameter interView contains the inflated single row view
    //here we will find resource ids
    public MyFeedbacksViewHolder(View itemView, ArrayList<String> data, final ArrayList<MyFeedBacksStructure> studentCompeteData) {
        super(itemView);
        name = (TextView) itemView.findViewById(R.id.singleRowText);

        this.data = data;
        this.studentCompleteData = studentCompeteData;

        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //student id on specified postition
                String no = studentCompleteData.get(getAdapterPosition()).getFeedBackNo();


                //to avoid nul pointer exception this if will run if selected user have not entered his more information
                if (no == null) {
                } else {


                    MyFeedBacks.communicator.sendFeedBackNo(no);
                }


            }
        });
    }


}

class MyFeedBacksStructure {
    String Description;
    String Title;
    String Uid;
    String FeedBackNo;

    MyFeedBacksStructure() {

    }

    public String getDescription() {
        return Description;
    }

    public String getFeedBackNo() {
        return FeedBackNo;
    }

    public void setFeedBackNo(String feedBackNo) {
        FeedBackNo = feedBackNo;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }
}

