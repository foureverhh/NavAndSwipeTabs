package com.nackademin.foureverhh.navandswipetabs;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.InputStream;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class ImageFragment extends Fragment {

    private ImageView photoFromGallery;
    private TextView textFromPhotoGallery;
    private Button buttonGetTextGallery;
    private static final int GET_PHOTO_FROM_GALLERY = 1;

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
        buttonGetTextGallery =(Button) rootView.findViewById(R.id.btn_get_text_photo_gallery);
        buttonGetTextGallery.setOnClickListener(new View.OnClickListener() {
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

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == GET_PHOTO_FROM_GALLERY && resultCode == RESULT_OK){
            Uri imageUri = data.getData();
            try {
                InputStream inputStream = getActivity()
                        .getContentResolver()
                        .openInputStream(imageUri);
                Bitmap originBitmap = BitmapFactory.decodeStream(inputStream);
                Bitmap resizedBitmap = ScalePhoto.scaleDownPhoto(originBitmap
                        , 500
                        , false);
                photoFromGallery.setImageBitmap(resizedBitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
