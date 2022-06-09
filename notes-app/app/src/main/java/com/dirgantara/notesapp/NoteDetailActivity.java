package com.dirgantara.notesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Date;

public class NoteDetailActivity extends AppCompatActivity
{
    private EditText titleEditText, descEditText;
    private Button deleteButton;
    private Note selectedNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) //отрисовка экрана редактирования заметки
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);
        initWidgets();
        checkForEditNote();
    }

    private void initWidgets() //инициализация полей и кнопок
    {
        titleEditText = findViewById(R.id.titleEditText);
        descEditText = findViewById(R.id.descriptionEditText);
        deleteButton = findViewById(R.id.deleteNoteButton);
    }

    private void checkForEditNote() //проверка на то, не была ли редактирована заметка
    {
        Intent previousIntent = getIntent();

        int passedNoteID = previousIntent.getIntExtra(Note.NOTE_EDIT_EXTRA, -1);
        selectedNote = Note.getNoteForID(passedNoteID);

        if (selectedNote != null)
        {
            titleEditText.setText(selectedNote.getTitle());
            descEditText.setText(selectedNote.getDescription());
        }
        else
        {
            deleteButton.setVisibility(View.INVISIBLE);
        }
    }

    public void saveNote(View view) //сохранения заметки
    {
        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
        String title = String.valueOf(titleEditText.getText());
        String desc = String.valueOf(descEditText.getText());

        if(selectedNote == null)
        {
            int id = Note.noteArrayList.size();
            Note newNote = new Note(id, title, desc);
            Note.noteArrayList.add(newNote);
            sqLiteManager.addNoteToDatabase(newNote);
        }
        else
        {
            selectedNote.setTitle(title);
            selectedNote.setDescription(desc);
            sqLiteManager.updateNoteInDB(selectedNote);
        }

        finish();
    }

    public void deleteNote(View view) //удаление заметки
    {
        selectedNote.setDeleted(new Date());
        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
        sqLiteManager.updateNoteInDB(selectedNote);
        finish();
    }
}