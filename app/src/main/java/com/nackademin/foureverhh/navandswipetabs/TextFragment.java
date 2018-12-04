package com.nackademin.foureverhh.navandswipetabs;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


/**
 * A simple {@link Fragment} subclass.
 */
public class TextFragment extends Fragment {

    private EditText editText;
    private TextView textView;
    private Button button;

    public TextFragment() {
        // Required empty public constructor
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
                searchResultFromWiki();
            }
        });
        return rootView;
    }


    private void searchResultFromWiki() {

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                String text = editText.getText().toString();
                String url = "https://zh.wikipedia.org/w/api.php?action=parse&page="+
                        text+"&prop=wikitext&utf8&&format=json";
                //String url = "https://zh.wikipedia.org/w/api.php?action=opensearch&search="+
                //        text+"&utf8&format=json";

                URL endPoint = null;
                try {
                    endPoint = new URL(url);
                    HttpsURLConnection connection = (HttpsURLConnection)endPoint.openConnection();
                    connection.setRequestMethod("GET");
                    //Parse response from connection
                    int responseCode = connection.getResponseCode();
                    BufferedReader in = new BufferedReader
                            (new InputStreamReader(connection.getInputStream()));
                    String inputLine;
                    StringBuilder response = new StringBuilder();
                    while((inputLine = in.readLine()) !=null)
                        response.append(inputLine);
                    in.close();

                    JSONObject myResponse = new JSONObject(response.toString());
                    JSONObject parseObject = myResponse.getJSONObject("parse");
                    JSONObject wikiTextObject = parseObject.getJSONObject("wikitext");
                    final String textToShow = wikiTextObject.getString("*");
                    textView.post(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText(textToShow);
                        }
                    });
                } catch (MalformedURLException e) {
                    e.printStackTrace();

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                    textView.post(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText("查无结果！");
                        }
                    });
                }


            }
        });
    }
}
//class ParseWikiTask extends AsyncTask< >{ }
