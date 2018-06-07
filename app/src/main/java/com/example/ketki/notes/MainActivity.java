package com.example.ketki.notes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

    static ArrayList<String> notes = new ArrayList<>();
    static ArrayAdapter<String> arrayAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId() == R.id.addNote){
            Intent intent = new Intent(getApplicationContext(), CreateNoteActivity.class);
            startActivity(intent);
            return true;
        }
        else {
            new AlertDialog.Builder(MainActivity.this)
                    .setIcon(android.R.drawable.ic_menu_delete)
                    .setTitle("Are you sure?")
                    .setMessage("Do you want to clear everything?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            notes.removeAll(notes);
                            arrayAdapter.notifyDataSetChanged();

                            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.ruturaj.notes", Context.MODE_PRIVATE);

                            HashSet<String> hashSet = new HashSet<String>(MainActivity.notes);
                            sharedPreferences.edit().putStringSet("noteSet", hashSet).apply();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView notesListView = (ListView) findViewById(R.id.notesListVIew);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.ruturaj.notes", Context.MODE_PRIVATE);
        HashSet<String> hashSet = (HashSet<String>) sharedPreferences.getStringSet("noteSet", null);

        if(hashSet == null) {
            notes.add("Add a note...");
        }
        else{
            notes = new ArrayList(hashSet);
        }

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, notes);
        notesListView.setAdapter(arrayAdapter);

        notesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), CreateNoteActivity.class);
                intent.putExtra("note", i);
                startActivity(intent);
            }
        });

        notesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int itemClicked = i;
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_menu_delete)
                        .setTitle("Are you sure?")
                        .setMessage("Do you want to delete this note?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                notes.remove(itemClicked);
                                arrayAdapter.notifyDataSetChanged();

                                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.ruturaj.notes", Context.MODE_PRIVATE);

                                HashSet<String> hashSet = new HashSet<String>(MainActivity.notes);
                                sharedPreferences.edit().putStringSet("noteSet", hashSet).apply();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
                return true;
            }
        });
    }
}
