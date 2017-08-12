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
public class MyBookings extends Fragment {


    RecyclerView recyclerView;
    DatabaseReference ref;
    ArrayList<BookingStructure> bookingsCompleteData;
    Query query;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_bookings, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        bookingsCompleteData = new ArrayList<>();
        recyclerView = (RecyclerView) getView().findViewById(R.id.MyBookings);
        ref = FirebaseDatabase.getInstance().getReference().child("Bookings");

        final MyBookingsAdapter adapter = new MyBookingsAdapter(getActivity(), bookingsCompleteData);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        if (getActivity().getClass().getName().equals("roshaan.parkingsystem.UsersFeed")) {
            query = ref.orderByChild("uid").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
        } else if (getActivity().getClass().getName().equals("roshaan.parkingsystem.AdminsFeed")) {
            query = ref;
        }

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                bookingsCompleteData.clear();

                Iterable<DataSnapshot> child = dataSnapshot.getChildren();

                for (DataSnapshot ch : child) {

                    BookingStructure struct = ch.getValue(BookingStructure.class);


                    bookingsCompleteData.add(struct);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}


class MyBookingsAdapter extends RecyclerView.Adapter<MyBookingsViewHolder> {
    LayoutInflater lf;
    Context context;

    ArrayList<BookingStructure> bookingsCompleteData;

    public MyBookingsAdapter(Context context, ArrayList<BookingStructure> bookingsCompleteData) {


        this.context = context;

        this.bookingsCompleteData = bookingsCompleteData;
        lf = LayoutInflater.from(context);
    }

    //inflation will take place here
    //this method will be called if new viewHolder rewuired otherwise recycling will do the job
    @Override
    public MyBookingsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View v = lf.inflate(R.layout.single_booking, parent, false);
        //sending inflatd view to ViewHolder so that it can find resource ids

        MyBookingsViewHolder holder = new MyBookingsViewHolder(v, bookingsCompleteData);


        return holder;
    }

    //Responsible to fill UI
    //The holder parameter here is from onCreateHolder's return statement so it contains ids
    @Override
    public void onBindViewHolder(MyBookingsViewHolder holder, int position) {


        //also capitalizi
        holder.area.setText(bookingsCompleteData.get(position).area.toString().substring(0, 1).toUpperCase() + bookingsCompleteData.get(position).area.toString().substring(1));
        holder.slot.setText(bookingsCompleteData.get(position).slot.toString().substring(0, 1).toUpperCase() + bookingsCompleteData.get(position).slot.toString().substring(1));
        holder.startDate.setText(bookingsCompleteData.get(position).startDate.toString().substring(0, 1).toUpperCase() + bookingsCompleteData.get(position).startDate.toString().substring(1));
        holder.endDate.setText(bookingsCompleteData.get(position).endDate.toString().substring(0, 1).toUpperCase() + bookingsCompleteData.get(position).endDate.toString().substring(1));

    }

    @Override
    public int getItemCount() {

        return bookingsCompleteData.size();
    }
}


class MyBookingsViewHolder extends RecyclerView.ViewHolder {

    DatabaseReference ref;
    TextView area;
    TextView slot;
    TextView startDate;
    TextView endDate;
    Button cancel;
    ArrayList<BookingStructure> bookingsCompleteData;


    //This will be called inside onCreateViewHolder
    //parameter interView contains the inflated single row view
    //here we will find resource ids
    public MyBookingsViewHolder(View itemView, final ArrayList<BookingStructure> bookingsCompleteData) {
        super(itemView);

        area = (TextView) itemView.findViewById(R.id.signleBkngArea);
        slot = (TextView) itemView.findViewById(R.id.signleBkngSlot);
        startDate = (TextView) itemView.findViewById(R.id.signleBkngStrtTime);
        endDate = (TextView) itemView.findViewById(R.id.signleBkngEndTime);
        cancel = (Button) itemView.findViewById(R.id.signleBkngCancel);


        this.bookingsCompleteData = bookingsCompleteData;

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Bookings");
                ref.child(bookingsCompleteData.get(getAdapterPosition()).pushID).removeValue();


            }
        });
    }


}