package com.example.meetinginfo;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.sql.Time;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class Fragment2 extends Fragment {
    EditText date;
    EditText eId;
    CalendarView cal;
    Button btn1, remBtn;
    DataBaseConn dbc;

    //String med="";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment2_layout, container, false);
        date = view.findViewById(R.id.editTextDate);
        cal = view.findViewById(R.id.calendarView);
        btn1 = view.findViewById(R.id.btn2);
        remBtn = view.findViewById(R.id.remBtn);
        eId = view.findViewById(R.id.emailId);
        dbc = new DataBaseConn(getActivity());
        //t=()

        cal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String d = dayOfMonth + "/" + (month + 1) + "/" + year;
                date.setText(d);
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
                                    @SuppressLint("Range")
                                    @Override
                                    public void onClick(View v) {
                                        String d1 = date.getText().toString();
                                        StringBuffer res = new StringBuffer();
                                        StringBuilder msg = new StringBuilder();
                                        Cursor c = dbc.fetch(d1);
                                        int count = c.getCount();
                                        c.moveToFirst();
                                        if (count > 0) {
                                            do {
                                                res.append(c.getString(c.getColumnIndex("agenda"))).append(" at ").append(c.getString(c.getColumnIndex("time")));
                                                res.append("\n");

                                                //med = (String.valueOf(c.getString(c.getColumnIndex("agenda"))));
                                            } while (c.moveToNext());
                                            Toast.makeText(getActivity(), res, Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(getActivity(), "No Meeting on This Day....", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                remBtn.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("Range")
                    @Override
                    public void onClick(View view) {


                        try {
                            String stringSenderEmail = "bbhoomi1671@gmail.com";
                            String stringReceiverEmail = (String) eId.getText().toString();
                            String stringPasswordSenderEmail = "pmyynruktnoayrrh";
                            String stringHost = "smtp.gmail.com";
                            Properties properties = System.getProperties();
                            properties.put("mail.smtp.host", stringHost);
                            properties.put("mail.smtp.port", "465");
                            properties.put("mail.smtp.ssl.enable", "true");
                            properties.put("mail.smtp.auth", "true");

                            Session session = Session.getInstance(properties, new Authenticator() {
                                @Override
                                protected PasswordAuthentication getPasswordAuthentication() {
                                    return new PasswordAuthentication(stringSenderEmail, stringPasswordSenderEmail);
                                }
                            });
                            MimeMessage mimeMessage = new MimeMessage(session);
                            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(stringReceiverEmail));
                            mimeMessage.setSubject("Subject: Meeting Scheduled today");
                            String d1 = date.getText().toString();
                            StringBuilder msg = new StringBuilder();
                            Cursor c = dbc.fetch(d1);
                            int count = c.getCount();
                            c.moveToFirst();
                            if (count > 0) {
                                do {
                                    msg.append(c.getString(c.getColumnIndex("agenda"))).append(" at ").append(c.getString(c.getColumnIndex("time")));
                                    msg.append("\n");

                                    //med = (String.valueOf(c.getString(c.getColumnIndex("agenda"))));
                                } while (c.moveToNext());
                            }
                                mimeMessage.setText("Hello,\n\n" + "A meeting is scheduled by you as " + msg + "\n\nThank You");
                                Thread thread = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Transport.send(mimeMessage);
                                        } catch (MessagingException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                thread.start();
                        }catch (AddressException e) {
                            e.printStackTrace();
                        }catch (MessagingException e) {
                            e.printStackTrace();
                        }
                    }
                });
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
