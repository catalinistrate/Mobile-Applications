package com.example.otto.forum;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EditItem extends AppCompatActivity implements View.OnClickListener {
    private String itemValue;
    private Button update;
    private Button delete;
    private EditText t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        itemValue = getIntent().getExtras().getString("value");
        t = (EditText) findViewById(R.id.editText);
        t.setText(itemValue);
        update = (Button) findViewById(R.id.button2);
        delete = (Button) findViewById(R.id.button3);

        update.setOnClickListener(this);
        delete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==update.getId())
        {
            Intent intent=new Intent();
            intent.putExtra("value",t.getText().toString());
            setResult(1, intent);
            finish();
        }
        else
            if(v.getId()==delete.getId())
            {
                Intent intent=new Intent();
                setResult(0, intent);
                finish();
            }
    }
}
