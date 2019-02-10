package com.nackademin.foureverhh.navandswipetabs;

import android.graphics.Bitmap;
import android.graphics.Matrix;
//To scale the image shown on the
public class ScalePhoto {

    public static Bitmap scaleDownPhoto(Bitmap sourceBitmap, int desiredPixels, boolean keepOriginBitmap){
        int width = sourceBitmap.getWidth();
        int height = sourceBitmap.getHeight();
        int newWidth = 0, newHeight = 0;
        float ratio = 0f;

        if(width > height && width > desiredPixels){
            newWidth = desiredPixels;
            ratio = (float) newWidth / width;
            newHeight = (int)(ratio * height);
        }

        if(width > height && width <= desiredPixels)
            return sourceBitmap;

        if(width < height && height > desiredPixels){
            newHeight = desiredPixels;
            ratio = (float) newHeight / height;
            newWidth = (int)(ratio * height);
        }

        if(width < height && height <= desiredPixels)
            return sourceBitmap;

        if(width == height && width > desiredPixels){
            newWidth = desiredPixels;
            newHeight = desiredPixels;
        }

        if(width == height && height <= desiredPixels)
            return sourceBitmap;

       return resizedBitmap(sourceBitmap,newWidth,newHeight,keepOriginBitmap);

    }

    public static Bitmap resizedBitmap(Bitmap sourceBitmap,
                                       int newWidth,
                                       int newHeight,
                                       boolean keepOriginBitmap){
        int width = sourceBitmap.getWidth();
        int height = sourceBitmap.getHeight();
        float scaleWidth = (float) newWidth / width;
        float scaleHeight = (float) newHeight / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth,scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(sourceBitmap,0,0,width,height,matrix,false);
        //if(!keepOriginBitmap)
        //    resizedBitmap.recycle();
        return resizedBitmap;
    }
}
