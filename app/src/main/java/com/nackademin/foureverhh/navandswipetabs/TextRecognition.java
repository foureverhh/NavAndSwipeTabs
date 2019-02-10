package com.nackademin.foureverhh.navandswipetabs;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.api.client.http.javanet.NetHttpTransport;
/*
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;
import com.google.api.services.vision.v1.model.TextAnnotation;
 */
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;

import javax.net.ssl.HttpsURLConnection;

//Followed this: https://code.tutsplus.com/tutorials/how-to-use-the-google-cloud-vision-api-in-android-apps--cms-29009

public class TextRecognition extends AsyncTask{

    private static final String TAG = TextRecognition.class.getName();
    String url = "https://api.ocr.space/parse/image";

    private String mApiKey;
    private boolean isOverlayRequired = false;
    private String mImageUrl;
    private String mLanguage;
    private Activity mActivity;
    private ProgressDialog mProgressDialog;
    private IOCRCallBack mIOCRCallBack;

    public TextRecognition (Activity activity,String apiKey, boolean isOverlayRequired,
                             String imageUrl,String language, IOCRCallBack iocrCallBack) {
        this.mActivity = activity;
        this.mApiKey = apiKey;
        this.isOverlayRequired = isOverlayRequired;
        this.mImageUrl = imageUrl;
        this.mIOCRCallBack = iocrCallBack;
    }

    @Override
    protected void onPreExecute() {
        mProgressDialog = new ProgressDialog(mActivity);
        mProgressDialog.setTitle("Wait while processing");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Object[] objects) {
        try{
            return sendPost(mApiKey,isOverlayRequired,mImageUrl, mLanguage);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private String sendPost(String apiKey, boolean isOverlayRequired, String imageUrl,
                            String language) throws Exception{
        URL obj = new URL(url);
        HttpsURLConnection connection = (HttpsURLConnection) obj.openConnection();

        //Add request header
        connection.setRequestMethod("POST");
        connection.setRequestProperty("User-Agent","Mozilla/5.0");
        connection.setRequestProperty("Accept-Language","en-US,en;q=0.5");

        JSONObject postDataParams = new JSONObject();

        postDataParams.put("apikey",apiKey);//Add API key
        postDataParams.put("isOverLayRequired",isOverlayRequired);
        postDataParams.put("url",imageUrl);
        postDataParams.put("language",language);

        //Send post request
        connection.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
        wr.writeBytes(getPostDataString(postDataParams));
        wr.flush();
        wr.close();

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while((inputLine = in.readLine()) != null){
            response.append(inputLine);
        }
        in.close();

        //return result
        return String.valueOf(response);
    }

    @Override
    protected void onPostExecute(Object result) {
        super.onPostExecute(result);
        if(mProgressDialog != null && mProgressDialog.isShowing()){
            mProgressDialog.dismiss();
        }
        String response = (String) result;
        mIOCRCallBack.getOCRCallBackResult(response);
        Log.d(TAG,response.toString());
    }

    public String getPostDataString(JSONObject params) throws Exception {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> iterator = params.keys();
        while(iterator.hasNext()){
            String key = iterator.next();
            Object value = params.get(key);

            if(first){
                first =false;
            }else
                result.append("&");

            result.append(URLEncoder.encode(key,"UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(),"UTF-8"));
        }
        return result.toString();
    }
    //Followed by https://github.com/bsuhas/OCRTextRecognitionAndroidApp/blob/be7bb24a0e880cf174de9f16047fcb1b8c7447c6/app/src/main/java/com/ocrtextrecognitionapp/OCRAsyncTask.java
//Free ORC API


/*
    public static void textRecognize(final TextView textView,final InputStream sourceInputStream){
        if(sourceInputStream == null)
            Log.e("InputStream"," is null");
        //Initialize an instance of Vision client
        Vision.Builder visionBuilder = new Vision.Builder(
                new NetHttpTransport(),
                new AndroidJsonFactory(),
                null
        );

        visionBuilder.setVisionRequestInitializer(
                new VisionRequestInitializer("AIzaSyDVm55Q1b9VB5ZqG-Hfd2WlbTtUmcmkVh8"));

        final Vision vision = visionBuilder.build();
        final String[] textInImage = {};
        //Create new thread to handle text recognition
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                //Convert image inputStream to Base64 string
                byte[] photoData = {};
                try {
                    photoData = IOUtils.toByteArray(sourceInputStream);
                    if(photoData == null)
                        Log.e("photoData"," is null");
                    Image inputImage = new Image();
                    inputImage.encodeContent(photoData);
                    //Make a Request and get Response
                    Feature textFeature = new Feature();
                    textFeature.setType("TEXT_DETECTION");

                    AnnotateImageRequest request = new AnnotateImageRequest();
                    request.setImage(inputImage);
                    request.setFeatures(Arrays.asList(textFeature));

                    BatchAnnotateImagesRequest batchRequest =
                            new BatchAnnotateImagesRequest();
                    batchRequest.setRequests(Arrays.asList(request));


                    BatchAnnotateImagesResponse batchResponse =
                            vision.images().annotate(batchRequest).execute();

                    //Use the response
                    final TextAnnotation text = batchResponse.getResponses().get(0)
                            .getFullTextAnnotation();
                    textView.setText(text.getText()) ;


                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });

*/

    /*
        TextRecognizer textRecognizer = new TextRecognizer.Builder(context).build();
        StringBuilder stringBuilder = new StringBuilder(" ");
        if(!textRecognizer.isOperational())
            Log.w("Error:"," Detector dependencies are not yet available");
        else{
            Frame frame = new Frame.Builder().setBitmap(sourceBitmap).build();
            SparseArray<TextBlock> items = textRecognizer.detect(frame);
            Log.e("items size is"," "+items.size());
            for(int i = 0; i< items.size(); i++){
                TextBlock item = items.valueAt(i);
                stringBuilder.append(item.getValue());
                stringBuilder.append("\n");
            }
        }
        Log.e("It runs ","into text recognize");
        Log.e("Text is: ",stringBuilder.toString());
       return stringBuilder.toString();
      */
//    }
}
