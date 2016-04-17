package com.serpare.serpare_beta;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.serpare.serpare_beta.database.noteDatabase.DBOpenHelper;
import com.serpare.serpare_beta.database.noteDatabase.NoteProvider;

public class EditorActivity extends AppCompatActivity {

    private String action;
    private EditText editor;
    private String noteFilter;
    private String oldText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        editor = (EditText) findViewById(R.id.editText);
        Intent intent = getIntent();

        Uri uri = intent.getParcelableExtra(NoteProvider.CONTENT_ITEM_TYPE);

        if (uri == null){
            action = intent.ACTION_INSERT;
            setTitle(getString(R.string.new_note));
        }else{
            action = Intent.ACTION_EDIT;
            noteFilter = DBOpenHelper.NOTE_ID + "=" + uri.getLastPathSegment();

            Cursor cursor = getContentResolver().query(
                    uri,DBOpenHelper.ALL_COLUMNS,noteFilter, null, null
            );
            cursor.moveToFirst();
            oldText = cursor.getString(cursor.getColumnIndex(DBOpenHelper.NOTE_TEXT));
            editor.setText(oldText);
            editor.requestFocus();


        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (action.equals(Intent.ACTION_EDIT)) {
            getMenuInflater().inflate(R.menu.menu_editor, menu);

        }




        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (item.getItemId()){
            case android.R.id.home:
                finishEditing();
                finish();
                break;
            case R.id.action_delete:
                deleteNote();
                break;
        }

        return super.onOptionsItemSelected(item);}

    private void deleteNote() {
        getContentResolver().delete(NoteProvider.CONTENT_URI, noteFilter, null);
        Toast.makeText(this, R.string.noteDeleted, Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    private void finishEditing() {
        String newText = editor.getText().toString().trim();

        switch (action) {
            case Intent.ACTION_INSERT:
                if (newText.length() == 0) {
                    setResult(RESULT_CANCELED);
                } else {
                    InsertNote(newText);
                }
                break;
            case Intent.ACTION_EDIT:
                if (newText.length()==0) {
                    deleteNote();
                }else if (oldText.equals(newText)){
                    setResult(RESULT_CANCELED);
                }else{
                    updateNote(newText);

                }


        }
        finish();
    }

    private void updateNote(String notetext) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.NOTE_TEXT, notetext);
        getContentResolver().update(NoteProvider.CONTENT_URI, values, noteFilter, null);
        Toast.makeText(this, R.string.updateMessage, Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
    }

    private void InsertNote(String notetext) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.NOTE_TEXT, notetext);
        getContentResolver().insert(NoteProvider.CONTENT_URI, values);
        setResult(RESULT_OK);
    }

    @Override
    public void onBackPressed() {
        finishEditing();
    }
}


