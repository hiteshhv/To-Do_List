package com.hitesh.todolist;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashSet;


public class MainActivity extends AppCompatActivity {

    static ArrayList<String> notes = new ArrayList<>();
    static ArrayAdapter<String> arrayAdapter;
    SharedPreferences sharedPreferences;
    FloatingActionButton fab, fab1;
    boolean isFABOpen;
    LinearLayout fabLayout1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        fabLayout1 = (LinearLayout) findViewById(R.id.fabLayout1);
        fab = findViewById(R.id.fab);
        fab1 = findViewById(R.id.fab1);

        sharedPreferences = getApplicationContext().getSharedPreferences("com.hitesh.todolist", Context.MODE_PRIVATE);
        ListView listView = findViewById(R.id.listView);

        HashSet<String> set = (HashSet<String>) sharedPreferences.getStringSet("notes", null);

        if (set == null) {
            notes.add("Example Note");
        } else {
            notes = new ArrayList<>(set);
        }

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notes);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), editNoteActivity.class);
                intent.putExtra("noteId", position);
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(MainActivity.this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Do you want to delete the selecter item")
                        .setMessage("").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        notes.remove(position);
                        arrayAdapter.notifyDataSetChanged();

                        HashSet<String> set = new HashSet<>(MainActivity.notes);
                        sharedPreferences.edit().putStringSet("notes", set).apply();
                    }
                }).setNegativeButton("No", null).show();
                return true;
            }
        });
     fab.setOnClickListener(new View.OnClickListener() {
         @Override

         public void onClick(View v) {
             if(!isFABOpen){
                 open();
                 fabLayout1.setVisibility(View.VISIBLE);
                 isFABOpen = true;
             } else {
                 close();
                 isFABOpen = false;
             }
         }
     });

     fab1.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             Intent intent = new Intent(getApplicationContext(), editNoteActivity.class);
             startActivity(intent);
         }
     });
    }
    public void open(){
        fabLayout1.animate().translationY(-50);
        fab.animate().rotationBy(135);
        fabLayout1.animate().alpha(1).setDuration(500);
    }
    public void close(){
        fabLayout1.animate().translationY(10);
        fab.animate().rotationBy(-135);
        fabLayout1.animate().alpha(0.0f).setDuration(500);
    }
}