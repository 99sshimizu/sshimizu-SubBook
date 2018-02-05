/*
 * MainActivity (sshimizu_SubBook)
 *
 * Version 1.0
 *
 * February 5, 2018
 *
 * Copyright (c) 2018 sshimizu CMPUT 301. University of Alberta - All Rights Reserved. You may use distribute or
 * modify this code under terms and condition of the Code of Student Behaviour at University of Alberta.You can
 * find a copy of licence in this project. Otherwise please contact contact @abc.ca.
 */

package com.example.sshimizu_subbook;

/**
 * Created by SarahS on 2018/01/23.
 */

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.DialogFragment;
import android.app.Fragment;
import android.widget.Toast;

/**
 * Represents a MainActivity
 *
 * @author sshimizu
 * @Version 1.0
 * @see Subscription
 * @see NewSubscription
 * @see SubscriptionEditView
 */
public class MainActivity extends AppCompatActivity {
    private static final String FILENAME = "file.sav";
    static final int RESULT_SUBACTIVITY = 1;
    private static final String TAG = "MyActivity";

    private MainActivity activity = this;

    private EditText SubNames;
    private EditText Charges;
    private EditText comments;

    /**Called to return Subscription list
     *
     * @return CurrentSubList
     */
    public ListView getCurrentSubList() {return CurrentSubList;}
    private ListView CurrentSubList;

    private TextView SubDates;
    private TextView TTlCharges;

    private ArrayList<Subscription> Subscript;
    private ArrayAdapter<Subscription> adapter;

    private DatePickerDialog.OnDateSetListener varDateSetListener;

    private int Position;
    private float TotalCharges;
    private float sum;

    /**Called when activity is first created.
     *
     * @param savedInstanceState onCreate savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TotalCharges = 0;

        SubNames = (EditText) findViewById(R.id.editText1);
        Button saveButton = (Button) findViewById(R.id.button);
        Button deleteButton = (Button) findViewById(R.id.button2);
        Charges = (EditText) findViewById(R.id.editText4);
        CurrentSubList = (ListView) findViewById(R.id.SubscriptionLists);
        SubDates = (TextView) findViewById(R.id.TextView);
        comments = (EditText) findViewById(R.id.comments);

        varDateSetListener = new DatePickerDialog.OnDateSetListener(){
            /**Sets date in TextView
             *
             * @param view view
             * @param year year
             * @param monthOfYear monthOfYear
             * @param dayOfMonth dayOfMonth
             */
            @Override
            public void onDateSet(DatePicker view , int year , int monthOfYear , int dayOfMonth){
                NumberFormat f = new DecimalFormat("00");
                String Month = f.format(monthOfYear +1);
                String Day = f.format(dayOfMonth);
                String date = (year + "-" + Month + "-" + Day);
                SubDates.setText(date);
            }
        };

        SubDates.setOnClickListener(new View.OnClickListener() {
            /**Called when clicked on TextView of date calls for calender dialog
             *
             * @param view view
             */
            public void onClick(View view){
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog dateDialog = new DatePickerDialog(
                        MainActivity.this,
                        varDateSetListener,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                );
                dateDialog.show();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {

            /**Called when clicked save button
             *
             * @param v view
             */
            public void onClick(View v) {
                setResult(RESULT_OK);
                //Checks for input not filled in (order of date, name, charges)
                //Except for the comments
                if (SubDates.getText().toString().matches("yyyy-mm-dd")) {
                    toastMake("Please fill in date of subscription", 0, -200);
                }
                else if (SubNames.getText().toString().matches("")){
                    toastMake("Please fill in name of subscription", 0, -200);
                }
                else if (Charges.getText().toString().matches("")){
                    toastMake("Please fill in charges of subscription", 0, -200);
                }
                //If input all filled in get each text
                else{
                    //Checks for the sizes if name and comments <=20 and <=30
                    if (SubNames.getText().toString().length() > 20){
                        toastMake("Subscription name too long", 0, -200);
                    }
                    else if (comments.getText().toString().length() > 30){
                        toastMake("Comments too long", 0, -200);
                    }
                    //No problem then saves to lists
                    else {
                        String Date = SubDates.getText().toString();
                        String name = SubNames.getText().toString();
                        String comment = comments.getText().toString();
                        float charges = Float.parseFloat( Charges.getText().toString() );
                        String StringCharges = String.format("%.2f", charges);
                        Subscript.add(new NewSubscription(name, Date, StringCharges, comment));
                        adapter.notifyDataSetChanged();

                        SubNames.setText("");
                        SubDates.setText("yyyy-mm-dd");
                        Charges.setText("");
                        comments.setText("");
                        saveInFile();
                    }
                }

            }
        });

        getCurrentSubList().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /**When item in list clicked it returns the position
             *
             * @param adapterView adapterView
             * @param view view
             * @param position position
             * @param id id
             */
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                view.setSelected(true);
                Position = position;
            }
        });


        deleteButton.setOnClickListener(new View.OnClickListener() {

            /**Called when delete button clicked.
             * It deletes one selected
             *
             * @param v view
             */
            public void onClick(View v) {
                setResult(RESULT_OK);
                //Subscript.clear();
                Subscript.remove(Position);
                CurrentSubList.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                //Saves file
                saveInFile();
            }
        });

        CurrentSubList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            /**When item in list is long clicked we get new activity for both EDIT and VIEW
             *
             * @param adapterView adapterView
             * @param view view
             * @param i position
             * @param l l
             * @return false
             */
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(activity, SubscriptionEditView.class);
                Position = i;
                String pos = Integer.toString(i);
                intent.putExtra("POS", pos);
                intent.putExtra("NAME", Subscript.get(i).getSubName());
                intent.putExtra("DATE", Subscript.get(i).getSubDates());
                intent.putExtra("CHARGES", Subscript.get(i).getSubCharges());
                intent.putExtra("COMMENTS", Subscript.get(i).getSubComments());
                //startActivity(intent);
                startActivityForResult(intent, RESULT_SUBACTIVITY);

                return false;
            }
        });
    }

    /**Creates pop up to tell what is missing or the errors when inputting.
     * Ex... Date left empty
     *
     * @param message string
     * @param x width
     * @param y height
     */
    private void toastMake(String message, int x, int y){
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, x, y);
        toast.show();
    }

    /**Gains changed value when it was Edited in activity from long clicked.
     *
     * @param requestCode RESULT_SUBACTIVITY (1)
     * @param resultCode RESULT_OK
     * @param intent intent
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Log.d(TAG, "ON ACTIVITY RESULT");
        if (requestCode == RESULT_SUBACTIVITY) {
            if (resultCode == RESULT_OK) {
                Log.d(TAG, "RESULT OK");
                String name = intent.getStringExtra("RESULT_NAME");
                String date = intent.getStringExtra("RESULT_DATE");
                String charge = intent.getStringExtra("RESULT_CHARGE");
                String comments = intent.getStringExtra("RESULT_COMMENTS");
                String j = intent.getStringExtra("RESULT_POS");

                int position = Integer.parseInt(j);
                Subscript.get(position).setSubName(name);
                Subscript.get(position).setSubDate(date);
                Subscript.get(position).setSubCharge(charge);
                Subscript.get(position).setSubComments(comments);

                saveInFile();
            }
        }
    }

    /**Called when app first loaded.
     *
     */
    protected void onStart() {
        super.onStart();
        loadFromFile();
        adapter = new ArrayAdapter<Subscription>(this,
                R.layout.subsclist, Subscript);
        CurrentSubList.setAdapter(adapter);

    }

    /**Called when app calls for subscription lists
     *
     */
    private void loadFromFile() {
        try {
            FileInputStream fis = openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));

            Gson gson = new Gson();

            // Taken https://stackoverflow.com/questions/12384064/gson-convert-from-json-to-a-typed-arraylistt
            // 2018-01-23
            Type listType = new TypeToken<ArrayList<Subscription>>(){}.getType();
            Subscript = gson.fromJson(in, listType);

            //Calculates total sum of charges when file is newly loaded (when opens)
            sum = 0;
            for (int i = 0; i < Subscript.size(); i++) {
                sum = sum + Float.parseFloat(Subscript.get(i).getSubCharges());;
            }
            TotalCharges = sum;
            String StCharges = String.format("%.2f", TotalCharges);
            TTlCharges = (TextView) findViewById(R.id.ttlcharges);
            TTlCharges.setText("Total Fee: \n$" + StCharges);

        } catch (FileNotFoundException e) {
            Subscript = new ArrayList<Subscription>();
        }

    }

    /**Called when any changes occurred in lists.
     * Ex...Edit, create new subscription, delete...etc
     *
     */
    private void saveInFile() {
        try{
            FileOutputStream fos = openFileOutput(FILENAME,
                    Context.MODE_PRIVATE);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));

            Gson gson = new Gson();
            gson.toJson(Subscript, out);
            out.flush();

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        }

        //Calculate total sum of charges
        sum = 0;
        for (int i = 0; i < Subscript.size(); i++) {
            sum = sum + Float.parseFloat(Subscript.get(i).getSubCharges());;
        }
        TotalCharges = sum;
        String StCharges = String.format("%.2f", TotalCharges);
        TTlCharges = (TextView) findViewById(R.id.ttlcharges);
        TTlCharges.setText("Total Fee: \n$" + StCharges);
    }

}
