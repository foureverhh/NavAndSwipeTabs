package com.nackademin.foureverhh.navandswipetabs;


import android.Manifest;
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
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class CameraFragment extends Fragment implements CovertImageToBase64{

    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 2 ;
    private TextView textView;
    private static final int REQUEST_TAKE_PHOTO = 1;
    private ImageView imageView;
    private Button button;
    private String mCurrentPhotoPath;
    private String imageUrl;

    public CameraFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_camera, container, false);
        // Inflate the layout for this fragment
        textView = rootView.findViewById(R.id.camera_fragment_text);
        textView.setMovementMethod(new ScrollingMovementMethod());
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
        Context context = getActivity();
        if(context != null){
            if(takePictureIntent.resolveActivity(context.getPackageManager()) != null){
                File photoFile = createExternalStoragePublicPic();
                if(photoFile != null){
                    Uri photoUri = Uri.fromFile(createExternalStoragePublicPic());
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
                    startActivityForResult(Intent.createChooser(takePictureIntent, "Select a photo"),REQUEST_TAKE_PHOTO);
                }
            }
        }
    }

    private File createExternalStoragePublicPic(){
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).
                format(new Date());
        String imageFileName = timeStamp+".jpg" ;
        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        Log.e("show public storage","path is:"+path);
        File imageFile =  new File(path, imageFileName);
        mCurrentPhotoPath = imageFile.getAbsolutePath();
        imageUrl = convertImageToBase64();
        return imageFile;
    }
    @Override
    public String convertImageToBase64(){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Bitmap bitmap =  BitmapFactory.decodeFile(mCurrentPhotoPath);
        bitmap.compress(Bitmap.CompressFormat.JPEG,60,byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();

        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK){
            galleryAddImage();
            showPic();
        }
    }

    private void galleryAddImage(){
        Intent mediaScanIntent = new Intent();
        mediaScanIntent.setAction(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        if(getActivity() != null)
            getActivity().sendBroadcast(mediaScanIntent);
        Log.e("It runs here"," in gallery add");
        if(contentUri != null)
            Log.e("Photo sent to gallery"," is not empty");
    }

    private void setPic() {
        //Get the dimensions of the View
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();

        //Get the dimension of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        //Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        //Decode the image file into a Bitmap sized to fill the view
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        imageView.setImageBitmap(bitmap);
    }

    private void showPic() {
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            requestPermissions( new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            //setPic();
            Log.e("Permission","if");
        }else {
            setPic();
            Log.e("Permission", "else");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE) {
            if (permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setPic();
                Log.e("Permission","onRequestPermissionsResult");
            }
        }
    }
}
