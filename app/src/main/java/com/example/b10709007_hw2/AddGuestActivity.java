package com.example.b10709007_hw2;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.b10709007_hw2.data.WaitlistContract;
import com.example.b10709007_hw2.data.WaitlistDbHelper;

public class AddGuestActivity extends AppCompatActivity {

    private static final String LOG_TAG =AddGuestActivity.class.getSimpleName();

    private EditText personName;
    private EditText partySize;
    private Button add_info;
    private Button clear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_guest);

        personName=(EditText)findViewById(R.id.person_name_edit_text);
        partySize=(EditText)findViewById(R.id.party_count_edit_text);
        add_info=(Button)findViewById(R.id.add_to_waitlist_button);
        clear=(Button)findViewById(R.id.cancel_to_waitlist_button);

        WaitlistDbHelper dbHelper = new WaitlistDbHelper(this);//因為會動到資料庫所以要

        ActionBar actionBar=this.getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        clear.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {//當點下取消
                partySize.clearFocus();
                personName.getText().clear();
                partySize.getText().clear();
            }
        });

        add_info.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {//當點下新增
                addToWaitlist();//要進行字元處理 然後 新增至資料庫

                final Intent intent = new Intent();
                intent.setClass(AddGuestActivity.this  , MainActivity.class);
                startActivity(intent);//回到主畫面
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {//回去main
        int id=item.getItemId();
        if(id==android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }

    public void addToWaitlist() {//進行字元的處理
        if (personName.getText().toString().length() == 0 || partySize.getText().toString().length() == 0) {
            return;
        }
        int Size = 1;

        try {
            Size = Integer.parseInt(String.valueOf(partySize.getText()));
        } catch (NumberFormatException ex) {
            Log.e(LOG_TAG, "Failed to parse party size text to number: " + ex.getMessage());
        }

        addNewGuest(personName.getText().toString(), Size);

    }

    private long addNewGuest(String name, int partySize) {//新增至資料庫內
        ContentValues cv = new ContentValues();

        cv.put(WaitlistContract.WaitlistEntry.COLUMN_GUEST_NAME, name);
        cv.put(WaitlistContract.WaitlistEntry.COLUMN_PARTY_SIZE, partySize);

        return MainActivity.mDb.insert(WaitlistContract.WaitlistEntry.TABLE_NAME, null, cv);
    }
}

