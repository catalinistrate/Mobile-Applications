package com.example.otto.forum.Authentication;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.otto.forum.EditItem;
import com.example.otto.forum.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.LinkedList;

public class DashBoard extends AppCompatActivity implements View.OnClickListener {
    private LinkedList<String> items;
    private LinearLayout linearLayout;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);

        items = new LinkedList<String>();
        for (int i=0;i<10;i++)
            items.add("Item"+String.valueOf(i));
        refreshItemsInLayout();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private RelativeLayout getItemContainer(String textValue, int id)
    {
        TextView tv = new TextView(this);
        tv.setText(textValue);
        RelativeLayout rel = new RelativeLayout(this);
        rel.addView(tv);
        RelativeLayout.LayoutParams lay = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,100);
        lay.setMargins(20,20,20,20);
        rel.setLayoutParams(lay);
        rel.setOnClickListener(this);
        rel.setId(id);
        return rel;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void refreshItemsInLayout() {
        linearLayout.removeAllViews();
        for (String r : items)
        {
            linearLayout.addView(getItemContainer(r,items.indexOf(r)));
        }
    }

    @Override
    public void onClick(View v) {
        for(String r : items)
        {
            if(v.getId()==items.indexOf(r))
            {
                Intent intent = new Intent(this, EditItem.class);
                intent.putExtra("value", r);
                startActivityForResult(intent,items.indexOf(r));
            }
        }
    }
    // Call Back method  to get the Message form other Activity
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        for(String r : items) {

            if (requestCode == items.indexOf(r)) {
                if (resultCode == 0){
                    items.remove(r);
                }
                else {
                    items.remove(r);
                    items.add(requestCode,data.getStringExtra("value"));
                }
                break;
            }
        }
        refreshItemsInLayout();
    }

    @Override
    public void onBackPressed() {
        FirebaseAuth.getInstance().signOut();
        super.onBackPressed();
    }
}
