package com.example.sshimizu_subbook;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

public class MainActivity extends AppCompatActivity {
    private static final String FILENAME = "file.sav";
    static final int RESULT_SUBACTIVITY = 1;
    private static final String TAG = "MyActivity";

    private MainActivity activity = this;

    private EditText SubNames;
    private EditText TextView;
    private EditText Charges;
    private EditText comments;

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

        /*CalenderDate.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                CreateCalender datePicker = new CreateCalender();
                datePicker.show(getFragmentManager(), "datePicker");
                @Override
                public void onDateSet(DatePicker view , int year , int monthOfYear , int dayOfMonth){
                    editText.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
                }
            }
        });*/

        varDateSetListener = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view , int year , int monthOfYear , int dayOfMonth){
                NumberFormat f = new DecimalFormat("00");
                String Month = f.format(monthOfYear +1);
                String Day = f.format(dayOfMonth);
                String date = (year + "-" + Month + "-" + Day);
                SubDates.setText(date);
            }
        };

        //can be changed SubDates
        SubDates.setOnClickListener(new View.OnClickListener() {
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

            public void onClick(View v) {
                setResult(RESULT_OK);
                if ((SubDates.getText().toString().matches("yyyy-mm-dd")) ||
                        (SubNames.getText().toString().matches("")) ||
                        (Charges.getText().toString().matches(""))) {
                    SubNames.setText("Please fill in all place");
                }
                else{
                    String Date = SubDates.getText().toString();
                    String name = SubNames.getText().toString();
                    String comment = comments.getText().toString();
                    float charges = Float.parseFloat( Charges.getText().toString() );
                    String StringCharges = String.format("%.2f", charges);
                    if ((name.length() <= 20) && (comment.length() <= 30)){
                        Subscript.add(new NewSubscription(name, Date, StringCharges, comment));
                        adapter.notifyDataSetChanged();

                        SubNames.setText("");
                        SubDates.setText("yyyy-mm-dd");
                        Charges.setText("");
                        comments.setText("");
                        saveInFile();
                    }
                    else {
                        SubNames.setText("name max = 20, comment max = 30");
                    }
                }

            }
        });

        getCurrentSubList().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                view.setSelected(true);
                Position = position;
            }
        });


        deleteButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                setResult(RESULT_OK);
                //Subscript.clear();
                Subscript.remove(Position);
                adapter.notifyDataSetChanged();
                v.setSelected(false);

                saveInFile();
            }
        });

        CurrentSubList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
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

                /*Intent intents = getIntent();
                String name = intents.getStringExtra("RESULT_NAME");
                String date = intents.getStringExtra("RESULT_DATE");
                String charge = intents.getStringExtra("RESULT_CHARGES");
                String comments = intents.getStringExtra("RESULT_COMMENTS");
                Integer j = intents.getIntExtra("RESULT_POS",0);

                Subscript.get(j).setSubName(name);
                Subscript.get(j).setSubDate(date);
                Subscript.get(j).setSubCharge(charge);
                Subscript.get(j).setSubComments(comments);

                saveInFile();*/

                return false;
            }
        });
    }

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

    protected void onStart() {
        super.onStart();
        loadFromFile();
        adapter = new ArrayAdapter<Subscription>(this,
                R.layout.subsclist, Subscript);
        CurrentSubList.setAdapter(adapter);

    }

    private void loadFromFile() {
        try {
            FileInputStream fis = openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));

            Gson gson = new Gson();

            // Taken https://stackoverflow.com/questions/12384064/gson-convert-from-json-to-a-typed-arraylistt
            // 2018-01-23
            Type listType = new TypeToken<ArrayList<Subscription>>(){}.getType();
            Subscript = gson.fromJson(in, listType);

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
