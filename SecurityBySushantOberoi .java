package com.example.sushantoberoi.catchyourtrain;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by sushant oberoi on 21-03-2017.
 */
public class Security extends android.support.v4.app.Fragment implements AdapterView.OnItemClickListener{
    ListView lv;
    Button btn;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.security, container, false);
        ArrayAdapter adapter=ArrayAdapter.createFromResource(getActivity(),R.array.items,android.R.layout.simple_list_item_1);
        lv= (ListView) v.findViewById(R.id.listView);
        btn=(Button)v.findViewById(R.id.btn);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Thread t=  new Thread(new Runnable() {
                  @Override
                  public void run() {
                      try{
                          SQLiteDatabase data = getActivity().openOrCreateDatabase("hint", android.content.Context.MODE_PRIVATE, null); //nobody other can access
                          data.execSQL("create table if not exists contacts(name varchar, contact varchar);");
                          String query = "select * from contacts";
                          Cursor c = data.rawQuery(query, null);
                          int cnt = c.getCount();
                          String[] array_name=new String[cnt];
                          if(cnt>0) {
                              c.moveToFirst();
                              array_name = new String[cnt];
                              int k = 0;
                              while (!c.isAfterLast()) {
                                  String s2 = c.getString(c.getColumnIndex("contact"));
                                  array_name[k++] = s2;
                                  c.moveToNext();
                              }
                          }

                          GetLocation loc=new GetLocation();
                          loc.getAddress();
                          for(int j=0;j<cnt;j++){
                              String msg="help me: my location is: "+loc.abc;
                              String num=array_name[j];
                              SmsManager s=SmsManager.getDefault();
                              s.sendTextMessage(num,null,msg,null,null);
                          }
                      }
                      catch(Exception e)
                      {
                        e.printStackTrace();
                      }
                  }
              });
                t.start();
                try {
                    t.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    Intent j = new Intent(getActivity(), TakeAutoPicture.class);
                    startActivity(j);
                }
                catch(Exception e){
                    Toast.makeText(getActivity(),""+e,Toast.LENGTH_LONG).show();
                }
            }
        });


        return v;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if(i==0)
        {
            Intent intent=new Intent(getActivity(),SaveContacts.class);
            startActivity(intent);
        }
        else if(i==1){
            try{
            SQLiteDatabase data = getActivity().openOrCreateDatabase("hint", android.content.Context.MODE_PRIVATE, null); //nobody other can access
            data.execSQL("create table if not exists contacts(name varchar, contact varchar);");
            String query = "select * from contacts";
            Cursor c = data.rawQuery(query, null);
            int cnt = c.getCount();
            String[] array_name=new String[cnt];
            if(cnt>0) {
                c.moveToFirst();
                array_name = new String[cnt];
                int k = 0;
                while (!c.isAfterLast()) {
                    String s2 = c.getString(c.getColumnIndex("contact"));
                    array_name[k++] = s2;
                    c.moveToNext();
                }
            }

            GetLocation loc=new GetLocation();
            loc.getAddress();
            for(int j=0;j<cnt;j++){
                String msg="help me: my location is: "+loc.abc;
                Log.d(",oc",loc.abc);
                String num=array_name[j];
                SmsManager s=SmsManager.getDefault();
                s.sendTextMessage(num,null,msg,null,null);
                Toast.makeText(getActivity(),"message has been sent to"+num,Toast.LENGTH_LONG).show();
            }
            }
            catch(Exception e)
            {
                Toast.makeText(getActivity(),""+e,Toast.LENGTH_LONG).show();
            }
        }
        else if(i==2){
            try {
                Intent j = new Intent(getActivity(), TakeAutoPicture.class);
                startActivity(j);
            }
            catch(Exception e){
                Toast.makeText(getActivity(),""+e,Toast.LENGTH_LONG).show();
            }
        }
    }
}
