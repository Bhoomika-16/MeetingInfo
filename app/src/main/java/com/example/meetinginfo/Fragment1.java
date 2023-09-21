package com.example.meetinginfo;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static androidx.core.content.ContextCompat.getSystemService;

public class Fragment1 extends Fragment {
    EditText date,time,agenda;
    DataBaseConn dbc;
    CalendarView calendarView;
    Button btn;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_layout1,container,false);
        date=view.findViewById(R.id.txtDate);
        time=view.findViewById(R.id.txtTime);
        agenda=view.findViewById(R.id.txtAgenda);
        btn=view.findViewById(R.id.btn1);
        calendarView=view.findViewById(R.id.mCal);
        dbc=new DataBaseConn(getActivity());    //need to initialize here only
        calendarView.setVisibility(View.INVISIBLE);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyBoard();
                calendarView.setVisibility(View.VISIBLE);
                calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                        String d=dayOfMonth+"/"+(month+1)+"/"+year;
                        date.setText(d);
                        calendarView.setVisibility(View.INVISIBLE);
                    }
                });
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mdate,mTime,mAgenda;
                mdate=date.getText().toString();
                mTime=time.getText().toString();
                mAgenda=agenda.getText().toString();
               Boolean insert=dbc.insertvalue(mdate,mTime,mAgenda);
                if(insert==true){
                    Toast.makeText(getActivity(),"Data Inserted",Toast.LENGTH_SHORT).show();

                }
                else
                    Toast.makeText(getActivity(),"Data NOT Inserted",Toast.LENGTH_SHORT).show();
                //txt.setText("NOT INSERTED");


            }


        });




        return view;
    }
    private void closeKeyBoard(){
        View  view = getActivity().getCurrentFocus();
         if (view != null) {
             InputMethodManager imm = (InputMethodManager)
                     getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
             imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
         }

    }

}
