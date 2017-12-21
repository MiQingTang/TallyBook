package hznu.edu.cn.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.administrator.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class CcrActivity extends AppCompatActivity {
    private MyDatabaseHelper dbHelper;
    SQLiteDatabase db;
    private ContactsAdapter adapter;
    private List<ContactsNumber> phonedata=new ArrayList<>() ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new MyDatabaseHelper(this,"Phone.db",null,1);
        db=dbHelper.getWritableDatabase();

        initphone();
        adapter=new ContactsAdapter(CcrActivity.this,R.layout.contacts_number,phonedata);
        ListView listview=(ListView) findViewById(R.id.list_phone);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ContactsNumber getpn=phonedata.get(i);
                String Pnumber=getpn.getContactsNumber();
                Intent intent=new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+Pnumber));
                startActivity(intent);
            }
        });
    }
    private void initphone(){
        Cursor cursor=db.query("phone",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                String name=cursor.getString(cursor.getColumnIndex("name"));
                String phonenumber=cursor.getString(cursor.getColumnIndex("phone"));
                ContactsNumber ph=new ContactsNumber(name,phonenumber);
                phonedata.add(ph);
            }while(cursor.moveToNext());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId){
            case R.id.add_item:
                Intent intent=new Intent(CcrActivity.this,AddContacts.class);
                startActivity(intent);
                break;
            case R.id.delete_item:
                for(int i=0;i<phonedata.size();i++){
                    if(phonedata.get(i).getChecked()){
                        // dbHelper=new MyDatabaseHelper(this,"Phone.db",null,1);
                        //SQLiteDatabase db= dbHelper.getWritableDatabase();
                        db.delete("phone","name=?",new String[]{phonedata.get(i).getName()});
                    }
                }
                phonedata.clear();
                initphone();
                adapter.notifyDataSetChanged();
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

}
