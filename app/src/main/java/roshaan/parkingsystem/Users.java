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
public class Users extends Fragment {

    RecyclerView recyclerView;
    DatabaseReference ref;
    ArrayList<UsersStructure> usersCompleteData;
    Query query;

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_users, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        usersCompleteData = new ArrayList<>();
        recyclerView = (RecyclerView) getView().findViewById(R.id.Users);
        ref = FirebaseDatabase.getInstance().getReference().child("Users");

        final UsersAdapter adapter = new UsersAdapter(getActivity(), usersCompleteData);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);


        ref.orderByChild("Type").equalTo("User").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                usersCompleteData.clear();

                Iterable<DataSnapshot> child = dataSnapshot.getChildren();

                for (DataSnapshot ch : child) {

                    UsersStructure struct = ch.getValue(UsersStructure.class);


                    usersCompleteData.add(struct);
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}


class UsersAdapter extends RecyclerView.Adapter<UsersViewHolder> {
    LayoutInflater lf;
    Context context;
    ArrayList<UsersStructure> usersCompleteData;

    public UsersAdapter(Context context, ArrayList<UsersStructure> usersCompleteData) {


        this.context = context;

        this.usersCompleteData = usersCompleteData;
        lf = LayoutInflater.from(context);
    }

    //inflation will take place here
    //this method will be called if new viewHolder rewuired otherwise recycling will do the job
    @Override
    public UsersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View v = lf.inflate(R.layout.single_booking, parent, false);
        //sending inflatd view to ViewHolder so that it can find resource ids
        //extra parameter of data myny apni asani ke liye dala he takay click p data le skun
        UsersViewHolder holder = new UsersViewHolder(v, usersCompleteData);

        //returning holder , holder now contains the ids of the layout
        //values will be assigned to these ids in onBindViewHolder
        return holder;
    }

    //Responsible to fill UI
    //The holder parameter here is from onCreateHolder's return statement so it contains ids
    @Override
    public void onBindViewHolder(UsersViewHolder holder, int position) {


        //also capitalizi
        holder.fullName.setText(usersCompleteData.get(position).FullName.toString().substring(0, 1).toUpperCase() + usersCompleteData.get(position).FullName.toString().substring(1));
        holder.mobileNo.setText(usersCompleteData.get(position).MobileNo.toString().substring(0, 1).toUpperCase() + usersCompleteData.get(position).MobileNo.toString().substring(1));
        holder.gender.setText(usersCompleteData.get(position).Gender.toString().substring(0, 1).toUpperCase() + usersCompleteData.get(position).Gender.toString().substring(1));
        holder.hideMe.setVisibility(View.GONE);
        holder.tV1.setText("Full Name");
        holder.tV3.setText("Mobile No");
        holder.tV2.setText("Gender");
        holder.tV4.setVisibility(View.GONE);
        holder.delete.setText("Delete");
    }

    @Override
    public int getItemCount() {

        return usersCompleteData.size();
    }
}


class UsersViewHolder extends RecyclerView.ViewHolder {

    DatabaseReference ref;
    TextView fullName;
    TextView gender;
    TextView mobileNo;
    TextView hideMe;
    TextView tV1;
    TextView tV2;
    TextView tV3;
    TextView tV4;
    Button delete;
    ArrayList<UsersStructure> usersCompleteData;

    //This will be called inside onCreateViewHolder
    //parameter interView contains the inflated single row view
    //here we will find resource ids
    public UsersViewHolder(View itemView, final ArrayList<UsersStructure> usersCompleteData) {
        super(itemView);
        this.usersCompleteData = usersCompleteData;

        fullName = (TextView) itemView.findViewById(R.id.signleBkngArea);
        gender = (TextView) itemView.findViewById(R.id.signleBkngSlot);
        mobileNo = (TextView) itemView.findViewById(R.id.signleBkngStrtTime);
        hideMe = (TextView) itemView.findViewById(R.id.signleBkngEndTime);
        delete = (Button) itemView.findViewById(R.id.signleBkngCancel);
        tV1 = (TextView) itemView.findViewById(R.id.TV1);
        tV2 = (TextView) itemView.findViewById(R.id.TV2);
        tV3 = (TextView) itemView.findViewById(R.id.TV3);
        tV4 = (TextView) itemView.findViewById(R.id.TV4);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //deleteing fromm users
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");
                ref.child(usersCompleteData.get(getAdapterPosition()).Uid).removeValue();


                //remobing feedbacks
                DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference().child("Feedbacks");
                Query q = ref1.orderByChild("Uid").equalTo(usersCompleteData.get(getAdapterPosition()).Uid);

                q.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        Iterable<DataSnapshot> child = dataSnapshot.getChildren();

                        for (DataSnapshot ch : child) {

                            MyFeedBacksStructure struct = ch.getValue(MyFeedBacksStructure.class);
                            ;

                            if (struct != null) {

                                FirebaseDatabase.getInstance().getReference().child("Feedbacks").child(struct.FeedBackNo).removeValue();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                //removing bookings
                DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference().child("Bookings");
                Query q1 = ref2.orderByChild("uid").equalTo(usersCompleteData.get(getAdapterPosition()).Uid);

                q1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Iterable<DataSnapshot> child = dataSnapshot.getChildren();

                        for (DataSnapshot ch : child) {

                            BookingStructure struct = ch.getValue(BookingStructure.class);
                            ;

                            if (struct != null) {

                                FirebaseDatabase.getInstance().getReference().child("Bookings").child(struct.pushID).removeValue();
                            }
                        }
                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        });
    }


}

class UsersStructure {

    String FullName;
    String Gender;
    String MobileNo;
    String Type;
    String Uid;

    UsersStructure() {

    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }
}

