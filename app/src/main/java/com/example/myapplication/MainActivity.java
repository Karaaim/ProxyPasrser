package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    final String LOG_TAG = "myLogs";
    List<String> tabRowa = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(this);
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                MyTask mt = new MyTask();
                mt.execute();
                break;
        }
    }

    private void showResult() {
        int dataIndex = 0;
        TableLayout tableLayout = (TableLayout) findViewById(R.id.tbLayout);
        for (int i = 0; i < tabRowa.size(); i++) {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

            for (int j = 0, col = 0; j < 5; j++, dataIndex++) {
                TextView textView = new TextView(this);
                textView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT, 1f));

                if (j != 2) {
                    if (dataIndex < tabRowa.size()) {
                        textView.setText(tabRowa.get(dataIndex));
                        tableRow.addView(textView, col);
                        col++;
                    }
                }
            }
            tableLayout.addView(tableRow, i);
        }

    }


    public class MyTask extends AsyncTask<Void, Integer, Void> {


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Void doInBackground(Void... params) {
            Document doc = null;
            Elements tableResult = null;

            try {
                doc = Jsoup.connect("http://spys.one/").get();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            tableResult = doc.select("body > table:nth-child(2) > tbody >" +
                    " tr:nth-child(2) > td > table > tbody > tr > td > table:nth-child(1)")
                    .select("tr[class=spy1xx],tr[class=spy1x]");
            for (int trIndex = 0; trIndex < tableResult.size(); trIndex++) {
                for (int tdIndex = 0; tdIndex < 5; tdIndex++) {
                    System.out.println(trIndex + " " + tdIndex);
                    tabRowa.add(((Element) tableResult.tagName("tr").get(trIndex).childNodes()
                            .get(tdIndex)).text());
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Button button = findViewById(R.id.button);
            button.setVisibility(View.INVISIBLE);
            showResult();
        }
    }
}


