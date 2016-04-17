package com.serpare.serpare_beta;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.serpare.serpare_beta.database.UserInputSuggestions;
import com.serpare.serpare_beta.database.XmlDatabse;

public class SearchActivity extends AppCompatActivity {

    protected String UserIntentText;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        UserIntentText = getIntent().getStringExtra(MainActivity.USER_INPUT);

        TextView tv = (TextView) findViewById(R.id.passedIntentString);
        tv.setText(UserIntentText);

        PieChart lc = (PieChart) findViewById(R.id.pieChartTest);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        return super.onOptionsItemSelected(item);
    }
}
