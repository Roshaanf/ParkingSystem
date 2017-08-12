package roshaan.parkingsystem;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewBooking extends Fragment {


    Button Gulshan;
    Button Gulistan;
    Button Sadar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_booking, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Gulshan=(Button) getView().findViewById(R.id.areaGulshan);
        Gulistan=(Button) getView().findViewById(R.id.areaGuistan);
        Sadar=(Button) getView().findViewById(R.id.areaSadar);

        Gulshan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                communicator.areaCommunicator("Gulshan");
            }
        });
        Gulistan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                communicator.areaCommunicator("Gulistan");
            }
        });

        Sadar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                communicator.areaCommunicator("Sadar");
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        communicator= (AreaCommunication) context;
    }

    AreaCommunication communicator;
    interface AreaCommunication{

      void  areaCommunicator(String area);
    }
}
