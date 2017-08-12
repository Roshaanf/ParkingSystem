package roshaan.parkingsystem;


import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * A simple {@link Fragment} subclass.
 */
public class Slots extends Fragment implements View.OnClickListener {

  Spinner day;
    Spinner month;
    Spinner year;
    Spinner startingHour;
    Spinner startingMinute;
    Spinner noOfHours;
    Button slot1;
    Button slot2;
    Button slot3;
    Button slot4;
    Button slot5;
    Button slot6;
    Button slot7;
    Button slot8;
    Button slot9;
    Button slot10;
    Button set;
    String area;
    Date completeEndDateTime;
    Date completeStartDateTime;
    String completeStartDateTimeString;
    String completeEndDateTimeString;
    Boolean colorFlag=false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        area=getArguments().getString(UsersFeed.areaCommunicatorKey);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_slots, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //getting references for date
        day=(Spinner) getView().findViewById(R.id.dataDay);
        month=(Spinner) getView().findViewById(R.id.dayMonth);
        year=(Spinner) getView().findViewById(R.id.dayYear);

        //getting references for start time and no of hours
        startingHour=(Spinner) getView().findViewById(R.id.startingTimeHours);
        startingMinute=(Spinner) getView().findViewById(R.id.startingTimeMinutes);
        noOfHours=(Spinner) getView().findViewById(R.id.noOfHours);

        //setting adapter for date
        ArrayAdapter adapterDays = ArrayAdapter.createFromResource(getActivity(),R.array.Days,android.R.layout.simple_spinner_item);
        ArrayAdapter adapterMonths = ArrayAdapter.createFromResource(getActivity(),R.array.Months,android.R.layout.simple_spinner_item);
        ArrayAdapter adapterYears = ArrayAdapter.createFromResource(getActivity(),R.array.year,android.R.layout.simple_spinner_item);

        //setting adapter for  starting time and hours
        ArrayAdapter adapterStartingHour = ArrayAdapter.createFromResource(getActivity(),R.array.startingHours,android.R.layout.simple_spinner_item);
        ArrayAdapter adapterStartingMinute = ArrayAdapter.createFromResource(getActivity(),R.array.startingMinutes,android.R.layout.simple_spinner_item);
        ArrayAdapter adapterNoOfHours = ArrayAdapter.createFromResource(getActivity(),R.array.hours,android.R.layout.simple_spinner_item);

        set=(Button) getView().findViewById(R.id.slotsSet);

    //setting adapter for date
        day.setAdapter(adapterDays); month.setAdapter(adapterMonths); year.setAdapter(adapterYears);

        //setting adapter for start time
     startingHour.setAdapter(adapterStartingHour); startingMinute.setAdapter(adapterStartingMinute); noOfHours.setAdapter(adapterNoOfHours);


        slot1=(Button) getView().findViewById(R.id.slot1) ;
        slot2=(Button) getView().findViewById(R.id.slot2) ;
        slot3=(Button) getView().findViewById(R.id.slot3) ;
        slot4=(Button) getView().findViewById(R.id.slot4) ;
        slot5=(Button) getView().findViewById(R.id.slot5) ;
        slot6=(Button) getView().findViewById(R.id.slot6) ;
        slot7=(Button) getView().findViewById(R.id.slot7) ;
        slot8=(Button) getView().findViewById(R.id.slot8) ;
        slot9=(Button) getView().findViewById(R.id.slot9) ;
        slot10=(Button) getView().findViewById(R.id.slot10) ;

        //setting onClick to this activity so that they will be triggered inside freagment rather thn activity
        slot1.setOnClickListener(this);
        slot2.setOnClickListener(this);
        slot3.setOnClickListener(this);slot4.setOnClickListener(this);slot5.setOnClickListener(this);
        slot6.setOnClickListener(this);slot7.setOnClickListener(this);slot8.setOnClickListener(this);
        slot9.setOnClickListener(this);slot10.setOnClickListener(this);




       set.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               if(day.getSelectedItemId()!=0&&
                       month.getSelectedItemId()!=0&&
                       year.getSelectedItemId()!=0&&
                       noOfHours.getSelectedItemId()!=0&&
                       startingHour.getSelectedItemId()!=0&&
                       startingMinute.getSelectedItemId()!=0
                       ) {

                    //getting selected date only
                   String date=day.getSelectedItem()+"/"+month.getSelectedItemId()+"/"+year.getSelectedItem();




                   //checcking if the date is valid or invalid
                   Boolean flag=checkInvalidDate(date);
                   //if not valid
                   if(!flag){

                       Toast.makeText(getActivity(), "Invalid date", Toast.LENGTH_SHORT).show();
                        return;
                   }



                   final java.text.SimpleDateFormat completeSDF=new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm");
                    completeSDF.setLenient(true);

                   //making complete date with hours and minutes
                    completeStartDateTimeString =date+" "+startingHour.getSelectedItem()+":"+startingMinute.getSelectedItem();



                   completeStartDateTime= null;

                   completeStartDateTime=null;
                   try {
                       completeStartDateTime = completeSDF.parse(completeStartDateTimeString);
                   } catch (ParseException e) {
                       e.printStackTrace();

                       //Toast.makeText(getActivity(), "running", Toast.LENGTH_SHORT).show();
                   }


                   /////////////getting current date and time
                   Date current=getCurrentDate(completeSDF);


                   //comparing currendate with entered start date
                   //if entered start date is less than current date thn its an error

                   if(current.compareTo(completeStartDateTime)<=0){

                       slot1.setEnabled(true); slot2.setEnabled(true); slot3.setEnabled(true); slot4.setEnabled(true); slot5.setEnabled(true); slot6.setEnabled(true);
                       slot7.setEnabled(true); slot8.setEnabled(true); slot9.setEnabled(true); slot10.setEnabled(true);

                       slot1.setBackgroundColor(Color.parseColor("#F34363"));slot5.setBackgroundColor(Color.parseColor("#F34363"));slot9.setBackgroundColor(Color.parseColor("#F34363"));
                       slot2.setBackgroundColor(Color.parseColor("#F34363"));slot6.setBackgroundColor(Color.parseColor("#F34363"));slot10.setBackgroundColor(Color.parseColor("#F34363"));
                       slot3.setBackgroundColor(Color.parseColor("#F34363"));slot7.setBackgroundColor(Color.parseColor("#F34363"));
                       slot4.setBackgroundColor(Color.parseColor("#F34363"));slot8.setBackgroundColor(Color.parseColor("#F34363"));



//                      adding no of hours to initial date
                       completeEndDateTimeString=addHoursToDate(completeSDF,completeStartDateTime);

                        completeEndDateTime=null;

                       try {
                           completeEndDateTime=completeSDF.parse(completeEndDateTimeString);
                       } catch (ParseException e) {
                           e.printStackTrace();
                       }




                       Toast.makeText(getActivity(), "Sahi ja rhy ho", Toast.LENGTH_SHORT).show();



                       DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Bookings");



                       ref.orderByChild("area").equalTo(area).addValueEventListener(new ValueEventListener() {
                           @Override
                           public void onDataChange(DataSnapshot dataSnapshot) {

                               //now child contains push ids of bookings
                               Iterable<DataSnapshot> child=dataSnapshot.getChildren();

                               //for each booking accessing its values
                               for(DataSnapshot ch:child){

                                   //this object contains all values of single booking
                                   BookingStructure struct=ch.getValue(BookingStructure.class);

                                   String startTime=struct.startDate;
                                   String endTime=struct.endDate;

                                   //now parsing dates so that they will appeaar in the form of date
                                   Date startDateFromDb=null;
                                   Date endDateFromDb=null;

                                   try {
                                        startDateFromDb=completeSDF.parse(startTime);

                                       endDateFromDb=completeSDF.parse(endTime);
                                   } catch (ParseException e) {
                                       e.printStackTrace();
                                   }


                                 //compare returns 1 if the date in argument is less thn the date which is comparing
                                   //checks will check if slots are empty for the given date and time
                                if((       //checking if starting booking and ending booking is not between db starting and ending date
                                        ( completeStartDateTime.compareTo(startDateFromDb)>=0||completeStartDateTime.compareTo(endDateFromDb)==0)&&
                                        (completeStartDateTime.compareTo(endDateFromDb)<=0||completeStartDateTime.compareTo(startDateFromDb)==0))||
                                        (
                                        (  completeEndDateTime.compareTo(startDateFromDb)>=0||completeEndDateTime.compareTo(endDateFromDb)==0) &&
                                        (  completeEndDateTime.compareTo(endDateFromDb)<=0||completeEndDateTime.compareTo(startDateFromDb)==0)
                                        ) || //now checcking if db date is appearing between starting booking and ending bookind  time
                                        (
                                        ( startDateFromDb.compareTo(completeStartDateTime)>=0||startDateFromDb.compareTo(completeEndDateTime)==0)&&
                                         (startDateFromDb.compareTo(completeEndDateTime)<=0||startDateFromDb.compareTo(completeStartDateTime)==0))
                                        ||
                                        (  endDateFromDb.compareTo(completeStartDateTime)>=0||endDateFromDb.compareTo(completeEndDateTime)==0) &&
                                           (  endDateFromDb.compareTo(completeEndDateTime)<=0||endDateFromDb.compareTo(completeStartDateTime)==0)
                                        ){







                                    //disabling button and changing its background color because the slot is occupied

                                    String slot=struct.slot;

                                    //takay agr ye book hony k baad foran chalay listener to white color k bajaye yellow na krdy dubara chal k
                                    if(!colorFlag){
                                   switch(slot){

                                       case "Slot 1":

                                           slot1.setEnabled(false); slot1.setBackgroundColor(Color.parseColor("#FEC007"));
                                           break;
                                       case "Slot 2":

                                           slot2.setEnabled(false); slot2.setBackgroundColor(Color.parseColor("#FEC007"));
                                           break;
                                       case "Slot 3":
                                           slot3.setEnabled(false); slot3.setBackgroundColor(Color.parseColor("#FEC007"));
                                           break;
                                       case "Slot 4":
                                           slot4.setEnabled(false); slot4.setBackgroundColor(Color.parseColor("#FEC007"));
                                           break;
                                       case "Slot 5":
                                           slot5.setEnabled(false); slot5.setBackgroundColor(Color.parseColor("#FEC007"));
                                           break;
                                       case "Slot 6":
                                           slot6.setEnabled(false); slot6.setBackgroundColor(Color.parseColor("#FEC007"));
                                           break;
                                       case "Slot 7":
                                           slot7.setEnabled(false); slot7.setBackgroundColor(Color.parseColor("#FEC007"));
                                           break;
                                       case "Slot 8":
                                           slot8.setEnabled(false); slot8.setBackgroundColor(Color.parseColor("#FEC007"));
                                           break;
                                       case "Slot 9":
                                           slot9.setEnabled(false); slot9.setBackgroundColor(Color.parseColor("#FEC007"));
                                           break;
                                       case "Slot 10":
                                           slot10.setEnabled(false); slot10.setBackgroundColor(Color.parseColor("#FEC007"));
                                           break;
                                   }
                                    }

                                 //   colorFlag=false;
                                }



                               }
                           }

                           @Override
                           public void onCancelled(DatabaseError databaseError) {

                           }
                       });

                   } else{


                       Toast.makeText(getActivity(), "Start date should be equal to present date or of future", Toast.LENGTH_SHORT).show();

                       slot1.setEnabled(false); slot2.setEnabled(false); slot3.setEnabled(false); slot4.setEnabled(false); slot5.setEnabled(false); slot6.setEnabled(false);
                       slot7.setEnabled(false); slot8.setEnabled(false); slot9.setEnabled(false); slot10.setEnabled(false);

                       slot1.setBackgroundColor(Color.parseColor("#F34363"));slot5.setBackgroundColor(Color.parseColor("#F34363"));slot9.setBackgroundColor(Color.parseColor("#F34363"));
                       slot2.setBackgroundColor(Color.parseColor("#F34363"));slot6.setBackgroundColor(Color.parseColor("#F34363"));slot10.setBackgroundColor(Color.parseColor("#F34363"));
                       slot3.setBackgroundColor(Color.parseColor("#F34363"));slot7.setBackgroundColor(Color.parseColor("#F34363"));
                       slot4.setBackgroundColor(Color.parseColor("#F34363"));slot8.setBackgroundColor(Color.parseColor("#F34363"));
                   }



               }
                else{

                   slot1.setEnabled(false); slot2.setEnabled(false); slot3.setEnabled(false); slot4.setEnabled(false); slot5.setEnabled(false); slot6.setEnabled(false);
                   slot7.setEnabled(false); slot8.setEnabled(false); slot9.setEnabled(false); slot10.setEnabled(false);

                   slot1.setBackgroundColor(Color.parseColor("#F34363"));slot5.setBackgroundColor(Color.parseColor("#F34363"));slot9.setBackgroundColor(Color.parseColor("#F34363"));
                   slot2.setBackgroundColor(Color.parseColor("#F34363"));slot6.setBackgroundColor(Color.parseColor("#F34363"));slot10.setBackgroundColor(Color.parseColor("#F34363"));
                   slot3.setBackgroundColor(Color.parseColor("#F34363"));slot7.setBackgroundColor(Color.parseColor("#F34363"));
                   slot4.setBackgroundColor(Color.parseColor("#F34363"));slot8.setBackgroundColor(Color.parseColor("#F34363"));

                   Toast.makeText(getActivity(), "Select valid values from spinner", Toast.LENGTH_SHORT).show();
               }


           }
       });

    }


    @Override
    public void onClick(View view) {

        //Clicked slot refernce
        Button slot=(Button) getView().findViewById(view.getId());



        //now entering booking details in database

        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Bookings");

        //getting push key from database
        String id=ref.push().getKey();

        //making object of my booking
        BookingStructure bs=new BookingStructure(/*startDateTimeString*/completeStartDateTimeString,completeEndDateTimeString,id,
                FirebaseAuth.getInstance().getCurrentUser().getUid(),area,
               slot.getText().toString());

        ref.child(id).setValue(bs);


        //resetting spinners
        month.setSelection(0);day.setSelection(0);startingHour.setSelection(0);
        year.setSelection(0);noOfHours.setSelection(0);startingMinute.setSelection(0);


        ///disabling buttons
        slot1.setEnabled(false); slot2.setEnabled(false); slot3.setEnabled(false); slot4.setEnabled(false); slot5.setEnabled(false); slot6.setEnabled(false);
        slot7.setEnabled(false); slot8.setEnabled(false); slot9.setEnabled(false); slot10.setEnabled(false);

        //resetting colors
        slot1.setBackgroundColor(Color.parseColor("#F34363"));slot5.setBackgroundColor(Color.parseColor("#F34363"));slot9.setBackgroundColor(Color.parseColor("#F34363"));
        slot2.setBackgroundColor(Color.parseColor("#F34363"));slot6.setBackgroundColor(Color.parseColor("#F34363"));slot10.setBackgroundColor(Color.parseColor("#F34363"));
        slot3.setBackgroundColor(Color.parseColor("#F34363"));slot7.setBackgroundColor(Color.parseColor("#F34363"));
        slot4.setBackgroundColor(Color.parseColor("#F34363"));slot8.setBackgroundColor(Color.parseColor("#F34363"));




        //sending email;

      //  new SendMailTask(getActivity()).execute(/*fromEmail*/ "roshaanfa@gmail.com",
         //       "!@#$%^&*", /*toEmailList*/"roshaan.farrukh.rf@gmail.com", /*emailSubject*/ "hello", /*emailBody*/"jjjj");


        Toast.makeText(getActivity(), "Slot has been successfully booked , check details in my bookings tab", Toast.LENGTH_SHORT).show();
    }


    Date getCurrentDate(java.text.SimpleDateFormat completeSDF){

        String f=completeSDF.format(Calendar.getInstance().getTime());
        Date current=null;
        try {
            current=completeSDF.parse(f);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return current;
    }

    Boolean checkInvalidDate(String date){
        Boolean flag=true;
        java.text.SimpleDateFormat sdf=new java.text.SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);

        try {
            sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            flag=false;
        }

        return flag;
    }

    String addHoursToDate(java.text.SimpleDateFormat completeSDF,Date date){

        String newDate=null;

        ////GENERATING END DATE TIME BY ADDING NO OF HOURS TO INITLIAL DATE
        //using calender class to add noOfHours in initial date
        Calendar c=Calendar.getInstance();
        c.setTime(completeStartDateTime);

        c.set(Calendar.HOUR_OF_DAY, c.get(Calendar.HOUR_OF_DAY)+Integer.parseInt((String)noOfHours.getSelectedItem()));

        newDate=String.valueOf(completeSDF.format(c.getTime()));

        return newDate;
    }
}

class BookingStructure{

    String startDate;
    String endDate;
    String pushID;
    String uid;
    String area;
    String slot;

    public  BookingStructure(){}
    public BookingStructure(String startDate, String endDate, String pushID, String uid, String area, String slot) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.pushID = pushID;
        this.uid = uid;
        this.area = area;
        this.slot = slot;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getPushID() {
        return pushID;
    }

    public void setPushID(String pushID) {
        this.pushID = pushID;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }
}





class GMail {

    final String emailPort = "587";// gmail's smtp port
    final String smtpAuth = "true";
    final String starttls = "true";
    final String emailHost = "smtp.gmail.com";
//          mailhost = smtp.mail.yahoo.com for yahoo mails

    String fromEmail;
    String fromPassword;
    //List toEmailList;
    String toEmailList;
    String emailSubject;
    String emailBody;

    Properties emailProperties;
    Session mailSession;
    MimeMessage emailMessage;

    public GMail() {

    }

    public GMail(String fromEmail, String fromPassword,
                 String toEmailList, String emailSubject, String emailBody) {
        this.fromEmail = fromEmail;
        this.fromPassword = fromPassword;
        this.toEmailList = toEmailList;
        this.emailSubject = emailSubject;
        this.emailBody = emailBody;

        emailProperties = System.getProperties();
        emailProperties.put("mail.smtp.port", emailPort);
        emailProperties.put("mail.smtp.auth", smtpAuth);
        emailProperties.put("mail.smtp.starttls.enable", starttls);
        // emailProperties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        Log.i("GMail", "Mail server properties set.");


    }

    public MimeMessage createEmailMessage() throws AddressException,
            MessagingException, UnsupportedEncodingException {

        mailSession = Session.getDefaultInstance(emailProperties, null);
        emailMessage = new MimeMessage(mailSession);

        emailMessage.setFrom(new InternetAddress(fromEmail, fromEmail));
        // for (String toEmail : toEmailList) {
        //   Log.i("GMail","toEmail: "+toEmail);
        emailMessage.addRecipient(Message.RecipientType.TO,
                new InternetAddress(toEmailList));
        //}

        emailMessage.setSubject(emailSubject);
        emailMessage.setContent(emailBody, "text/html");// for a html email
        // emailMessage.setText(emailBody);// for a text email
        Log.i("GMail", "Email Message created.");
        return emailMessage;
    }

    public void sendEmail() throws AddressException, MessagingException {


        Transport transport = mailSession.getTransport("smtp");


        transport.connect(emailHost, fromEmail, fromPassword);

        Log.i("GMail","allrecipients: "+emailMessage.getAllRecipients());
        transport.sendMessage(emailMessage, emailMessage.getAllRecipients());
        transport.close();
        Log.i("GMail", "Email sent successfully.");
    }

}

class SendMailTask extends AsyncTask {

   // private ProgressDialog statusDialog;
    private Activity sendMailActivity;

    public SendMailTask(Activity activity) {
        sendMailActivity = activity;


    }

    protected void onPreExecute() {
//        statusDialog = new ProgressDialog(sendMailActivity);
//        statusDialog.setMessage("Getting ready...");
//        statusDialog.setIndeterminate(false);
//        statusDialog.setCancelable(false);
//        statusDialog.show();
    }

    @Override
    protected Object doInBackground(Object... args) {
        try {
            Log.i("SendMailTask", "About to instantiate GMail...");
            publishProgress("Processing input....");
            GMail androidEmail = new GMail(args[0].toString(),
                    args[1].toString(),  args[2].toString(), args[3].toString(),
                    args[4].toString());
            publishProgress("Preparing mail message....");

            androidEmail.createEmailMessage();

            publishProgress("Sending email....");
            androidEmail.sendEmail();

            publishProgress("Email Sent.");
            Log.i("SendMailTask", "Mail Sent.");
        } catch (Exception e) {
            publishProgress(e.getMessage());
            Log.e("SendMailTask", e.getMessage(), e);
        }
        return null;
    }

    @Override
    public void onProgressUpdate(Object... values) {
     //    statusDialog.setMessage(values[0].toString());

    }

    @Override
    public void onPostExecute(Object result) {
     //   statusDialog.dismiss();
    }

}