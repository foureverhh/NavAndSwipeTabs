package com.nackademin.foureverhh.navandswipetabs;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import android.os.Bundle;
import android.provider.MediaStore;

import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import static android.app.Activity.RESULT_OK;



/**
 * A simple {@link Fragment} subclass.
 */
public class ImageFragment extends Fragment implements CovertImageToBase64,IOCRCallBack {

    private ImageView photoFromGallery;
    private TextView textFromPhotoGallery;
    private Button buttonGetPhotoGallery,buttonGetTextGallery;
    private static final int GET_PHOTO_FROM_GALLERY = 1;
    private Bitmap bitmapGallery;


    private String mApiKey;
    private boolean isOverlayRequired;
    private String mLanguage;
    private IOCRCallBack mIOCRCallBack;
    private String imageUrl;

    public ImageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_image, container, false);
        photoFromGallery = (ImageView) rootView.findViewById(R.id.get_photo_from_gallery);
        textFromPhotoGallery =(TextView) rootView.findViewById(R.id.text_from_photo_gallery);
        textFromPhotoGallery.setMovementMethod(new ScrollingMovementMethod());
        buttonGetPhotoGallery =(Button) rootView.findViewById(R.id.btn_get_photo_gallery);
        buttonGetTextGallery =(Button) rootView.findViewById(R.id.btn_get_text_gallery);

        mApiKey = "792e611e6f88957";
        isOverlayRequired = true;
        mLanguage = "eng";
        mIOCRCallBack = this;

        buttonGetPhotoGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getPhotoFromGallery = new Intent();
                getPhotoFromGallery.setAction(Intent.ACTION_GET_CONTENT);
                getPhotoFromGallery.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        ,"image/*");
                startActivityForResult(
                        getPhotoFromGallery.createChooser(getPhotoFromGallery,"Select file")
                        ,GET_PHOTO_FROM_GALLERY);

            }
        });

        startOCRTask();
        return rootView;
    }

    private void startOCRTask() {

        buttonGetTextGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OCRAsyncTask ocrAsyncTask = new OCRAsyncTask(getActivity(),
                        mApiKey,
                        isOverlayRequired,
                        imageUrl,
                        mLanguage,
                        mIOCRCallBack);

                ocrAsyncTask.execute();
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == GET_PHOTO_FROM_GALLERY && resultCode == RESULT_OK)
        {
            Uri imageUri = data.getData();
            try
            {
                final InputStream inputStream = getActivity()
                        .getContentResolver()
                        .openInputStream(imageUri);

                bitmapGallery = BitmapFactory.decodeStream(inputStream);
                Bitmap resizedBitmap = ScalePhoto.scaleDownPhoto(bitmapGallery
                        , 500
                        , false);
                imageUrl = convertImageToBase64();
                photoFromGallery.setImageBitmap(resizedBitmap);

            }catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
            super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public String convertImageToBase64() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmapGallery.compress(Bitmap.CompressFormat.JPEG,60,byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        return android.util.Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    @Override
    public void getOCRCallBackResult(String response) {
        StringBuilder ocrResult = new StringBuilder();
        try {
            JSONObject object = new JSONObject(response);
            JSONArray parsedResult = object.getJSONArray("ParsedResults");
            for(int i=0; i< parsedResult.length();i++){
                JSONObject subObject = parsedResult.getJSONObject(i);
                String parsedText = subObject.getString("ParsedText");
                ocrResult.append(parsedText);
                ocrResult.append("\n");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("ocrResult",ocrResult.toString());
        textFromPhotoGallery.setText(ocrResult.toString());
    }
}