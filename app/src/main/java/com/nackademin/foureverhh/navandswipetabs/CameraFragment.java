package com.nackademin.foureverhh.navandswipetabs;


import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class CameraFragment extends Fragment {

    private TextView textView;
    private static final int REQUEST_TAKE_PHOTO = 1;
    private ImageView imageView;
    private Button button;
    private String mCurrentPhotoPath;

    public CameraFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_camera, container, false);
        // Inflate the layout for this fragment
        textView = rootView.findViewById(R.id.camera_fragment_text);
        imageView = rootView.findViewById(R.id.camera_image);
        button = rootView.findViewById(R.id.btn_text_from_camera);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
        return rootView;
    }

    private void dispatchTakePictureIntent(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //Ensure that there's a camera activity to handle the intent
        Context context = getActivity();
        if(context != null){
            if(takePictureIntent.resolveActivity(context.getPackageManager()) != null){
                //Create the File where the photo should go
                File photoFile = null;

                try {
                    photoFile = createImageFile();
                } catch (IOException e) {
            Toast.makeText(context,"Photo File failed",Toast.LENGTH_SHORT).show();
                }
                //Continue only if the File was successfully created
                if(photoFile != null){
                    Uri photoUri = FileProvider.getUriForFile(getActivity(),
                            "com.nackademin.foureverhh.navandswipetabs.fileprovider",
                photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
                    startActivityForResult(takePictureIntent,REQUEST_TAKE_PHOTO);
                }
            }
        }
    }

    private File createImageFile() throws IOException{
        //Create an image file name
        Context context = getActivity();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = null;

        if(context != null)
            storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

            File image = File.createTempFile(
                    imageFileName,
                    ".jpg",
                    storageDir
            );
        //Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        Log.e("Path is ",mCurrentPhotoPath);
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK){
            galleryAddImage();
            setPic();
        }
    }

    private void galleryAddImage(){
        Intent mediaScanIntent = new Intent();
        mediaScanIntent.setAction(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);
        Log.e("It runs here"," in gallery add");
    }

    private void setPic(){
        //Get dimension Of the View
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();

        //Get dimension of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath,bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;
        int scaleFactor = Math.min(photoW/targetW,photoH/targetH);
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath,bmOptions);
        imageView.setImageBitmap(bitmap);
        Log.e("It runs here"," in setPic");
    }
}
