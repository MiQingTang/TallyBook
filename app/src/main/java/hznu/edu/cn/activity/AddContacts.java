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
import android.widget.Toast;

import com.example.administrator.myapplication.R;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import hznu.edu.cn.entity.User;

public class AddContacts extends AppCompatActivity {
    private Button Add;
    private EditText Name;
    private EditText MobilePhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add);
        Add = (Button) findViewById(R.id.ADD);
        Name = (EditText) findViewById(R.id.name);
        MobilePhone = (EditText) findViewById(R.id.phone);
        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                User bmobUser = User.getCurrentUser(User.class);
                ContactsNumber ccr = new ContactsNumber(Name.getText().toString(), MobilePhone.getText().toString());
                bmobUser.getCcr().add(ccr);
                bmobUser.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e != null) {
                            Toast.makeText(AddContacts.this, "添加失败，检查网络！" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            finish();
                        }
                    }
                });
            }
        });
    }
}

