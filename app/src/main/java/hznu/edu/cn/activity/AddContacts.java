package hznu.edu.cn.activity;

/**
 * Created by Ssumday on 2017/11/26.
 */

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.administrator.myapplication.R;

public class AddContacts extends AppCompatActivity{
    private Button Add;
    private EditText Name;
    private EditText MobilePhone;
    private MyDatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add);
        dbHelper = new MyDatabaseHelper(this,"Phone.db",null,1);
        Add =(Button)findViewById(R.id.ADD);
        Name =(EditText)findViewById(R.id.name);
        MobilePhone=(EditText)findViewById(R.id.phone);
        Add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();

                values.put("name",Name.getText().toString());
                values.put("phone",MobilePhone.getText().toString());
                db.insert("phone",null,values);
                Intent intent=new Intent(AddContacts.this,CcrActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}

