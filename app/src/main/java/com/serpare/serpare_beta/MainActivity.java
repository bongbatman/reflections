package com.serpare.serpare_beta;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.serpare.serpare_beta.database.DataModel;
import com.serpare.serpare_beta.database.UserInputSuggestions;
import com.serpare.serpare_beta.database.XmlDatabse;
import com.serpare.serpare_beta.database.noteDatabase.DBOpenHelper;
import com.serpare.serpare_beta.database.noteDatabase.NoteProvider;
import com.serpare.serpare_beta.xmlParseData.xmlParser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity
implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final int EDITOR_REQUEST = 1005;
    CursorAdapter cursorAdapter;
    ProgressBar pb;
    List<xmlParser> searchList;

    public static final String USER_INPUT = "User Input";


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.logo_actionbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        try {
            XmlDatabse.PullData(this);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        pb = (ProgressBar) findViewById(R.id.progressBar);
        pb.setVisibility(View.INVISIBLE);

//        ImageView notesIcon = (ImageView) findViewById(R.id.imageDocIcon);
//        notesIcon.setImageResource(R.mipmap.ic_note);

        UserInputSuggestions.setSuggestionList(XmlDatabse.list);
        AutoCompleteTextView userInput = (AutoCompleteTextView) findViewById(R.id.searchText);
//        String[] inputSuggestions = getResources().getStringArray(R.array.completion_hints);
        ArrayAdapter<String> mainInoutArrayAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, UserInputSuggestions.getSuggestionList());

        userInput.setAdapter(mainInoutArrayAdapter);

        cursorAdapter = new NotesCursorAdapter(this, null, 0);


        ListView list = (ListView) findViewById(android.R.id.list);
        list.setAdapter(cursorAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                Uri uri = Uri.parse(NoteProvider.CONTENT_URI + "/" + id);
                intent.putExtra(NoteProvider.CONTENT_ITEM_TYPE, uri);
                startActivityForResult(intent, EDITOR_REQUEST);
            }
        });

        getLoaderManager().initLoader(0,null,this);



    }

    private void insertNote(String notetext) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.NOTE_TEXT, notetext);
        Uri noteUri = getContentResolver().insert(NoteProvider.CONTENT_URI,values);
        Log.d("MAIN ACTIVITY", "Inserted Note" + noteUri.getLastPathSegment());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_create_sample:
                insertSampleData();
                if (isOnline()){
                    requestData("https://91ac7a6688151f94eddd1abc6d153048cb27ba88.googledrive.com/host/0BwYrL5UHVGjWfmJBdnlnY3E0REVNYzZ4TXhrVXJmSVNxSGVON2hOeWczMTkyaHZOLWVtQmc/SearchItems.xml");
                }else{
                    Toast.makeText(this, "No Network",Toast.LENGTH_SHORT).show();
                }




                break;
            case R.id.action_delete_all:
                deleteAllNotes();
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    private void requestData(String uri) {
        xmlTask task1 = new xmlTask();
        task1.execute(uri);
    }

    private void deleteAllNotes() {
        DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    getContentResolver().delete(
                            NoteProvider.CONTENT_URI, null, null
                    );
                    restartLoader();
                    Toast.makeText(MainActivity.this, getString(R.string.all_deleted),
                            Toast.LENGTH_SHORT).show();
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.are_you_sure))
                .setPositiveButton(getString(android.R.string.yes), dialogListener)
                .setNegativeButton(getString(android.R.string.no), dialogListener).show();
    }

    private void insertSampleData() {
        insertNote("Simple Note");
        insertNote("Multiline\nnote");
        insertNote("Very long note fuck fuckfuckfuckfuckfuckfuckfuckfuckfuckfuckfuckfuckfuckfuckfuckfuckfuckfuckfuckfuckfuckfuckfuckfuckfuckfuckfuckfuckz");
        restartLoader();
    }

    private void restartLoader() {
        getLoaderManager().restartLoader(0, null, this);
    }

    public void searchButtonClickHandler(View view) {
        Toast.makeText(this, "Search Button Test Successful", Toast.LENGTH_SHORT).show();
        ImageButton searchBtn = (ImageButton) findViewById(R.id.sbutton);
        searchBtn.setImageResource(R.drawable.search_button_clicked);

        AutoCompleteTextView userInput = (AutoCompleteTextView) findViewById(R.id.searchText);
        Editable searchIntentExtra = userInput.getText();
        String finalTextForIntent = searchIntentExtra.toString();

        Intent searchActivity = new Intent(this, SearchActivity.class);
        searchActivity.putExtra(USER_INPUT, finalTextForIntent);
        startActivity(searchActivity);


    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, NoteProvider.CONTENT_URI,
                null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }

    public void openEditorForNewNote(View view) {
        Intent intent = new Intent(this, EditorActivity.class);
        startActivityForResult(intent, EDITOR_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDITOR_REQUEST && resultCode == RESULT_OK){
            restartLoader();
        }
    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netIfo = cm.getActiveNetworkInfo();
        if (netIfo != null && netIfo.isConnectedOrConnecting()){
            return true;
        }else{
            return false;
        }
    }

    private class xmlTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            Toast.makeText(MainActivity.this, "Task start", Toast.LENGTH_LONG).show();
            pb.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
//            for (int i = 0; i < params.length ; i++) {
//                publishProgress("working with" + params[i]);
//                try {
//                    Thread.sleep(1002);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//            return "Async Task";
            String content = NetworkReqFirst.getData(params[0]);
            return content;

        }

        @Override
        protected void onPostExecute(String s) {

            Toast.makeText(MainActivity.this, s, Toast.LENGTH_LONG).show();
            pb.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            Toast.makeText(MainActivity.this, values[0], Toast.LENGTH_SHORT).show();

        }
    }
}
