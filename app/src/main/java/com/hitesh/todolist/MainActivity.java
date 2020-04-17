package com.hitesh.todolist;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.BoringLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashSet;


public class MainActivity extends AppCompatActivity {

    static ArrayList<String> notes = new ArrayList<>();
    static ArrayAdapter arrayAdapter;
    SharedPreferences sharedPreferences;
    FloatingActionButton fab, fab1;
    boolean isFABOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getApplicationContext().getSharedPreferences("com.hitesh.todolist", Context.MODE_PRIVATE);
        ListView listView = findViewById(R.id.listView);

        HashSet<String> set = (HashSet<String>) sharedPreferences.getStringSet("notes", null);

        if (set == null) {
            notes.add("Example Note");
        } else {
            notes = new ArrayList(set);
        }

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, notes);
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
       fab = findViewById(R.id.fab);
       fab1 = findViewById(R.id.fab1);
     fab.setOnClickListener(new View.OnClickListener() {
         @Override

         public void onClick(View v) {
           //fab1.animate().translationYBy(155);
             if(!isFABOpen){
                 open();
                 isFABOpen = true;
             } else {
                 close();
                 fab1.setVisibility(0);
                 isFABOpen = false;
             }
         }
     });

    }
    public void open(){

        fab1.animate().translationY(-120);
    }
    public void close(){

        fab1.animate().translationY(10);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.top_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.add_note) {
            Intent intent = new Intent(getApplicationContext(), editNoteActivity.class);
            startActivity(intent);

            return true;
        }

        return false;
    }
}