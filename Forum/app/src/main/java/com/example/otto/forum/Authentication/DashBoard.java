package com.example.otto.forum.Authentication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.otto.forum.EditItem;
import com.example.otto.forum.GraphDialog;
import com.example.otto.forum.Item;
import com.example.otto.forum.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class DashBoard extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private HashMap<String, List<Item>> messages;
    private LinkedList<String> topics;
    private LinearLayout linearLayout;
    private Spinner spinner;
    private Button addButton;
    private Button deleteButton;
    private ArrayAdapter<String> spinnerArrayAdapter;
    private String selectedTopic;
    private Button addItem;
    private Button stats;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        initializeComments();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        messages = new HashMap<>();
        spinner = (Spinner) findViewById(R.id.category_spinner);
        addButton = (Button) findViewById(R.id.add);
        addButton.setOnClickListener(this);
        deleteButton = (Button) findViewById(R.id.delete);
        deleteButton.setOnClickListener(this);
        addItem = (Button) findViewById(R.id.addItem);
        addItem.setOnClickListener(this);
        stats = (Button) findViewById(R.id.stats);
        stats.setOnClickListener(this);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        topics = new LinkedList<>();
        // Initializing an ArrayAdapter
        spinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.spinner_item, topics);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setOnItemSelectedListener(this);
    }

    private void initializeComments() {
        databaseTopics();
        databaseMessages();
    }

    private void databaseTopics() {
        FirebaseDatabase.getInstance().getReference("/topics").addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                topics.clear();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    topics.addLast((String) d.getKey());
                    if (!messages.containsKey(d.getKey()))
                        messages.put(d.getKey(), new ArrayList<Item>());
                }
                selectedTopic = topics.get(0);
                spinnerArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void databaseMessages() {
        FirebaseDatabase.getInstance().getReference("/messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null)
                    for (final DataSnapshot d : dataSnapshot.getChildren()) {
                        FirebaseDatabase.getInstance().getReference("/messages/" + d.getKey()).addValueEventListener(
                                new ValueEventListener() {
                                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            ArrayList<Item> array = new ArrayList<Item>();
                                            for (DataSnapshot d : dataSnapshot.getChildren()) {
                                                array.add(d.getValue(Item.class));
                                            }
                                            messages.put(dataSnapshot.getKey(), array);
                                        } else
                                            messages.remove(dataSnapshot.getKey());
                                        refreshItemsInLayout();
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                }
                        );
                    }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private RelativeLayout getItemContainer(String textValue, int id) {
        TextView tv = new TextView(this);
        tv.setText(textValue);
        RelativeLayout rel = new RelativeLayout(this);
        rel.addView(tv);
        RelativeLayout.LayoutParams lay = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100);
        lay.setMargins(20, 20, 20, 20);
        rel.setLayoutParams(lay);
        rel.setId(id);
        rel.setOnClickListener(this);
        return rel;
    }

    @Override
    public void onBackPressed() {
        FirebaseAuth.getInstance().signOut();
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Topic name");

            final EditText input = new EditText(this);

            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    FirebaseDatabase.getInstance().getReference("topics").child(input.getText().toString()).setValue(true);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
        } else if (v.getId() == R.id.delete) {
            FirebaseDatabase.getInstance().getReference("/topics").child(selectedTopic).removeValue();
            FirebaseDatabase.getInstance().getReference("/messages").child(selectedTopic).removeValue();
        } else if (v.getId() == R.id.addItem) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Item Value");

            final EditText input = new EditText(this);

            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    messages.get(selectedTopic).add(new Item(System.currentTimeMillis(), input.getText().toString()));
                    FirebaseDatabase.getInstance().getReference("messages").child(selectedTopic).setValue(messages.get(selectedTopic));
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
        } else if (v.getId() == R.id.stats) {
            GraphDialog cdd = new GraphDialog(this);
            cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            cdd.show();
        } else
            for (Item r : messages.get(selectedTopic)) {
                if (v.getId() == messages.get(selectedTopic).indexOf(r)) {
                    Intent intent = new Intent(this, EditItem.class);
                    intent.putExtra("value", r.value);
                    startActivityForResult(intent, messages.get(selectedTopic).indexOf(r));
                }
            }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedTopic = (String) parent.getItemAtPosition(position);
        refreshItemsInLayout();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void refreshItemsInLayout() {
        linearLayout.removeAllViews();
        if (messages.get(selectedTopic) != null)
            for (Item i : messages.get(selectedTopic)) {
                linearLayout.addView(getItemContainer(i.value, messages.get(selectedTopic).indexOf(i)));
            }
    }

    // Call Back method  to get the Message form other Activity
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Item i : messages.get(selectedTopic)) {

            if (requestCode == messages.get(selectedTopic).indexOf(i)) {
                if (resultCode == 0) {
                    FirebaseDatabase.getInstance().getReference("/messages/" + selectedTopic).child(String.valueOf(requestCode)).removeValue();
                } else {
                    FirebaseDatabase.getInstance().getReference("/messages/" + selectedTopic).child(String.valueOf(requestCode)).setValue(new Item(System.currentTimeMillis(), data.getStringExtra("value")));
                }
                break;
            }
        }
        refreshItemsInLayout();
    }

    public HashMap<String, List<Item>> getMessages() {
        return messages;
    }

    public String getSelectedTopic() {
        return selectedTopic;
    }
}
