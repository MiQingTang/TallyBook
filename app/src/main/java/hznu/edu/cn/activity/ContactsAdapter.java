package hznu.edu.cn.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.administrator.myapplication.R;

import java.util.List;

public class ContactsAdapter extends ArrayAdapter<ContactsNumber> {
    private int resourId;
    public ContactsAdapter(Context context, int textViewResourceId, List<ContactsNumber> objects){
        super(context, textViewResourceId ,objects);
        resourId=textViewResourceId;
    }
    @Override
    public View getView(int position, View conertView, ViewGroup parent){
        final ContactsNumber p = getItem(position);
        View view= LayoutInflater.from(getContext()).inflate(resourId,parent,false);
        final CheckBox check=(CheckBox) view.findViewById(R.id.phone_checkbox);
        TextView phonename=(TextView)view.findViewById(R.id.phone_name);
        TextView phonenumber=(TextView)view.findViewById(R.id.phone_number);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p.setChecked(check.isChecked());
            }
        });

        phonename.setText(p.getName());
        phonenumber.setText(p.getContactsNumber());
        return view;
    }
}

