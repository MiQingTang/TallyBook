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
import android.widget.Toast;

import com.example.administrator.myapplication.R;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.UpdateListener;
import hznu.edu.cn.entity.User;

public class CcrActivity extends AppCompatActivity {

    private List<ContactsNumber> phonedata = new ArrayList<>();
    private ContactsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ccr);

    }

    @Override
    protected void onResume() {
        super.onResume();
        initphone();
        adapter = new ContactsAdapter(CcrActivity.this, R.layout.contacts_number, phonedata);
        ListView listview = (ListView) findViewById(R.id.list_phone);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ContactsNumber getpn = phonedata.get(i);
                String Pnumber = getpn.getContactsNumber();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + Pnumber));
                startActivity(intent);
            }
        });
    }

    private void initphone() {
        List<ContactsNumber> cc = User.getCurrentUser(User.class).getCcr();
        phonedata.clear();
        phonedata.addAll(cc);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.add_item:
                Intent intent = new Intent(CcrActivity.this, AddContacts.class);
                startActivity(intent);
                break;
            case R.id.delete_item:
                for (int i = 0; i < phonedata.size(); i++) {
                    if (phonedata.get(i).getChecked()) {
                        phonedata.remove(i);
                    }
                }
                adapter.notifyDataSetChanged();
                User user = User.getCurrentUser(User.class);
                user.getCcr().clear();
                user.getCcr().addAll(phonedata);
                user.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Toast.makeText(CcrActivity.this, "更新信息成功", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(CcrActivity.this, "更新信息失败", Toast.LENGTH_SHORT).show();

                    }
                });

                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

}
