package com.nackademin.foureverhh.navandswipetabs;


import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class CameraFragment extends Fragment {

    public CameraFragment() {
        // Required empty public constructor
    }

    TextView textView;
    static final int REQUEST_TAKE_PHOTO = 1;
    ImageView imageView;
    Button btn;
    Uri photoUri;

    private void dispatchTakePictureIntent(){
        Intent takePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photo = new File(Environment.getExternalStorageDirectory(),"Pic.jpg");
        takePhoto.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(photo));
        photoUri = Uri.fromFile(photo);
        if(photoUri == null)
             Toast.makeText(getActivity(),"photoUri is not empty",Toast.LENGTH_LONG).show();
        getActivity().startActivityForResult(takePhoto,REQUEST_TAKE_PHOTO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK){
            Uri selectedImage = photoUri;
            getActivity().getContentResolver().notifyChange(selectedImage,null);
            ContentResolver contentResolver = getActivity().getContentResolver();
            Bitmap bitmap;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(contentResolver,selectedImage);
                if(bitmap == null)
                    Toast.makeText(getActivity(),"bitmap is not empty",Toast.LENGTH_LONG).show();
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                Toast.makeText(getActivity(), "Failed to load", Toast.LENGTH_SHORT)
                                           .show();
                Log.e("Camera",e.toString());
            }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_camera, container, false);
        // Inflate the layout for this fragment
        textView = rootView.findViewById(R.id.camera_fragment_text);
        imageView = rootView.findViewById(R.id.camera_image);
        btn = rootView.findViewById(R.id.get_text_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 dispatchTakePictureIntent();
            }
        });
        return rootView;
    }

}
