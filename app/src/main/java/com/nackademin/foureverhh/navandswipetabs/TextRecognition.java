package com.nackademin.foureverhh.navandswipetabs;

import android.content.Context;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;
import com.google.api.services.vision.v1.model.TextAnnotation;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;


public class TextRecognition {

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
    }
}
