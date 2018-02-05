package com.example.sshimizu_subbook;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;

public class SubscriptionEditView extends AppCompatActivity {
    private SubscriptionEditView activity = this;

    private EditText subName;
    private TextView subDate;
    private EditText subCharge;
    private EditText subComments;
    private DatePickerDialog.OnDateSetListener varDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription_edit_view);

        Intent intent = getIntent();
        String name = intent.getStringExtra("NAME");
        String date = intent.getStringExtra("DATE");
        String charge = intent.getStringExtra("CHARGES");
        String comments = intent.getStringExtra("COMMENTS");
        final String position = intent.getStringExtra("POS");

        subName = (EditText) findViewById(R.id.editText);
        subDate = (TextView) findViewById(R.id.textView);
        subCharge = (EditText) findViewById(R.id.editText2);
        subComments = (EditText) findViewById(R.id.editText3);
        Button subButton = (Button) findViewById(R.id.button3);

        subName.setText(name);
        subDate.setText(date);
        subCharge.setText(charge);
        subComments.setText(comments);

        varDateSetListener = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view , int year , int monthOfYear , int dayOfMonth){
                NumberFormat f = new DecimalFormat("00");
                String Month = f.format(monthOfYear +1);
                String Day = f.format(dayOfMonth);
                String date = (year + "-" + Month + "-" + Day);
                subDate.setText(date);
            }
        };

        //can be changed SubDates
        subDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog dateDialog = new DatePickerDialog(
                        SubscriptionEditView.this,
                        varDateSetListener,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                );
                dateDialog.show();
            }
        });

        subButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                setResult(RESULT_OK);
                if ((subDate.getText().toString().matches("yyyy-mm-dd")) ||
                        (subName.getText().toString().matches("")) ||
                        (subCharge.getText().toString().matches(""))) {
                    subName.setText("Please fill in all place");
                }
                else{
                    String Date = subDate.getText().toString();
                    String name = subName.getText().toString();
                    String comment = subComments.getText().toString();
                    //String StringCharges = subCharge.getText().toString();
                    float charges = Float.parseFloat( subCharge.getText().toString() );
                    String StringCharges = String.format("%.2f", charges);

                    if ((name.length() <= 20) && (comment.length() <= 30)){
                        Intent intent = new Intent();
                        intent.putExtra("RESULT_NAME", name);
                        intent.putExtra("RESULT_DATE", Date);
                        intent.putExtra("RESULT_CHARGE", StringCharges);
                        intent.putExtra("RESULT_COMMENTS", comment);
                        intent.putExtra("RESULT_POS", position);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                    else {
                        subName.setText("name max = 20, comment max = 30");
                    }
                }

            }
        });

    }
}
