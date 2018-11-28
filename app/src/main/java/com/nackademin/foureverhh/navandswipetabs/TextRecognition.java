package com.nackademin.foureverhh.navandswipetabs;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

public class TextRecognition {

    public static String textRecognize(Context context, Bitmap sourceBitmap){

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
    }

}
