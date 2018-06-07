package com.example.ketki.notes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.HashSet;

public class CreateNoteActivity extends AppCompatActivity {

    int note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        EditText noteEditText = (EditText) findViewById(R.id.noteEditText);
        Intent intent = getIntent();
        note = intent.getIntExtra("note", -1);

        if(note != -1){
            noteEditText.setText(MainActivity.notes.get(note));
        }
        else {
            MainActivity.notes.add("");
            note = MainActivity.notes.size() - 1;
            MainActivity.arrayAdapter.notifyDataSetChanged();
        }
        noteEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                MainActivity.notes.set(note, String.valueOf(charSequence));
                MainActivity.arrayAdapter.notifyDataSetChanged();

                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.ruturaj.notes", Context.MODE_PRIVATE);

                HashSet<String> hashSet = new HashSet(MainActivity.notes);
                sharedPreferences.edit().putStringSet("noteSet", hashSet).apply();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}
