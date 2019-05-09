package com.nackademin.foureverhh.navandswipetabs;


import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;
import com.nackademin.foureverhh.navandswipetabs.HistoryContract.*;


/**
 * A simple {@link Fragment} subclass.
 */
public class TextFragment extends Fragment {

    private EditText editText;
    private TextView textView;
    private Button button;
    private SQLiteDatabase historyDatabase;

    public TextFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Create database
        HistoryDBHelper historyDBHelper = new HistoryDBHelper(getActivity());
        historyDatabase = historyDBHelper.getWritableDatabase();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_text, container, false);
        editText = rootView.findViewById(R.id.edit_query_textPage);
        textView = rootView.findViewById(R.id.text_result_textFragment);
        textView.setMovementMethod(new ScrollingMovementMethod());
        button = rootView.findViewById(R.id.btn_textFragment);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String text = editText.getText().toString().trim();
                if(text.isEmpty())
                    return;
                searchResultFromWiki("en",text);
            }
        });

        return rootView;
    }


    private void searchResultFromWiki(final String language,final String text) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

                String url ="https://"+language+".wikipedia.org/w/api.php" +
                        "?format=json&action=query&prop=extracts&exintro&explaintext&redirects=1" +
                        "&titles="+text;

                URL endPoint = null;
                try {
                    endPoint = new URL(url);
                    HttpsURLConnection connection = (HttpsURLConnection)endPoint.openConnection();
                    connection.setRequestMethod("GET");
                    //Parse response from connection
                    //int responseCode = connection.getResponseCode();
                    BufferedReader in = new BufferedReader
                            (new InputStreamReader(connection.getInputStream()));
                    String inputLine;
                    StringBuilder response = new StringBuilder();
                    while((inputLine = in.readLine()) !=null)
                        response.append(inputLine);
                    in.close();

                    JSONObject myResponse = new JSONObject(response.toString());
                    JSONObject queryObject = myResponse.getJSONObject("query");
                    JSONObject pagesObject = queryObject.getJSONObject("pages");
                    Iterator<?> keys = pagesObject.keys();
                    while (keys.hasNext()){
                        String key = (String) keys.next();
                        if(key.equals("-1"))
                            searchResultFromWiki("sv",text);
                        else if(pagesObject.get(key) instanceof JSONObject){
                            final String textToShow = pagesObject.getJSONObject(key)
                                        .optString("extract");
                            if(textToShow.length() != 0) {
                                textView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        textView.setText(textToShow);
                                        addItemToDataBase(text, textToShow);
                                    }
                                });
                            }
                            else {
                                textView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        textView.setText(getString(R.string.no_result_wikepedia));
                                        addItemToDataBase(text,
                                                getString(R.string.no_result_wikepedia));
                                    }
                                });
                            }
                        }
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void addItemToDataBase(String keyword, String result) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(HistoryEntry.COLUMN_NAME_KEYWORD,keyword);
        contentValues.put(HistoryEntry.COLUMN_NAME_RESULT,result);
        long rowId = historyDatabase.insert(HistoryEntry.TABLE_NAME,null,contentValues);
        Toast.makeText(getContext(),"data save to database"+rowId,Toast.LENGTH_SHORT).show();
        editText.getText().clear();
    }
}
