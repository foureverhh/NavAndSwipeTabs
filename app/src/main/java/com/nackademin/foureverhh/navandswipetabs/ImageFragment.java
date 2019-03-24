package com.nackademin.foureverhh.navandswipetabs;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import org.apache.commons.io.IOUtils;


import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.Block;
import com.google.cloud.vision.v1.Image;

import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.cloud.vision.v1.Page;
import com.google.cloud.vision.v1.Paragraph;
import com.google.cloud.vision.v1.Symbol;
import com.google.cloud.vision.v1.TextAnnotation;
import com.google.cloud.vision.v1.Word;
import com.google.protobuf.ByteString;


import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
;
import java.util.List;
import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;

import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.protobuf.ByteString;


import org.apache.commons.codec.binary.Base64;


import javax.net.ssl.HttpsURLConnection;

import static android.app.Activity.RESULT_OK;
import static com.google.api.Page.newBuilder;


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
        textFromPhotoGallery.setMovementMethod(new ScrollingMovementMethod());
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

                Bitmap originBitmap = BitmapFactory.decodeStream(inputStream);
                Bitmap resizedBitmap = ScalePhoto.scaleDownPhoto(originBitmap
                        , 500
                        , false);
                photoFromGallery.setImageBitmap(resizedBitmap);
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run()
                    {
                        /*
                        //Create a post request by HttpsURLConnection
                        String url = "https//vision.googleapis.com/v1/images:annotate?" +
                                "key=AIzaSyDVm55Q1b9VB5ZqG-Hfd2WlbTtUmcmkVh8";
                        try {
                            URL postImageUrl = new URL(url);
                            HttpsURLConnection connection = (HttpsURLConnection)
                                    postImageUrl.openConnection();
                            connection.setRequestMethod("POST");
                            connection.setDoInput(true);
                            connection.setDoOutput(true);



                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        */

                        // Documentation tutorial
                        try {
                            //Create a vision client
                            ImageAnnotatorClient vision = ImageAnnotatorClient.create();
                            //Encode image to Base64 format
                            byte[] imageBytes = IOUtils.toByteArray(inputStream);
                            byte[] imageBase64 = Base64.encodeBase64(imageBytes);
                            ByteString imgBytes = ByteString.copyFrom(imageBase64);
                            //Create request
                            List<AnnotateImageRequest> requests = new ArrayList<>();
                            Image img = Image.newBuilder().setContent(imgBytes).build();
                            Feature feat = Feature.newBuilder().
                                    setType(Feature.Type.DOCUMENT_TEXT_DETECTION).build();
                            AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                                    .addFeatures(feat)
                                    .setImage(img)
                                    .build();
                            requests.add(request);
                            //parse response
                            BatchAnnotateImagesResponse response = vision
                                    .batchAnnotateImages(requests);
                            List<AnnotateImageResponse> responses = response.getResponsesList();
                            vision.close();

                            String textExtracted = "";
                            for(AnnotateImageResponse res: responses){
                                TextAnnotation annotation = res.getFullTextAnnotation();
                                for(Page page:annotation.getPagesList()){
                                    String pageText = "";
                                    for(Block block : page.getBlocksList()) {
                                        String blockText = "";
                                        for (Paragraph para : block.getParagraphsList()) {
                                            String paraText = "";
                                            for (Word word : para.getWordsList()) {
                                                String wordText = "";
                                                for (Symbol symbol : word.getSymbolsList()) {
                                                    wordText = wordText + symbol.getText();
                                                }
                                                paraText = pageText+wordText;
                                            }
                                            blockText=blockText+paraText;
                                        }
                                        pageText=pageText+blockText;
                                    }
                                }
                                textExtracted = textExtracted+annotation.getText();
                            }
                            final String finalTextExtracted = textExtracted;
                            //Show result on text view
                            textFromPhotoGallery.post(new Runnable() {
                              @Override
                              public void run() {
                                  textFromPhotoGallery.setText(finalTextExtracted);
                              }
                          });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
            super.onActivityResult(requestCode, resultCode, data);
    }

}
/*
    private void getText(final InputStream sourceInputStream){
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
                    //byte[] inputImage = org.apache.commons.codec.binary.Base64.encodeBase64(photoData);
                    Feature textFeature = new Feature();
                    textFeature.setType("TEXT_DETECTION");

                    AnnotateImageRequest request = new AnnotateImageRequest();
                    request.setImage(inputImage);
                    request.setFeatures(Arrays.asList(textFeature));

                    BatchAnnotateImagesRequest batchRequest =
                            new BatchAnnotateImagesRequest();
                    batchRequest.setRequests(Arrays.asList(request));


                    BatchAnnotateImagesResponse batchResponse = vision.images().annotate(batchRequest).execute();
                    //Vision.Images.Annotate batchRequest = vision.images().annotate(batchRequest);
                    //Use the response
                    TextAnnotation text = batchResponse.getResponses().get(0)
                            .getFullTextAnnotation();
                    textFromPhotoGallery.setText(text.getText()) ;


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
*/

/*
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
                            photoData = IOUtils.toByteArray(inputStream);
                            if(photoData == null)
                                Log.e("photoData"," is null");
                            Image inputImage = new Image();
                            inputImage.encodeContent(photoData);
                            //Make a Request and get Response
                            //byte[] inputImage = org.apache.commons.codec.binary.Base64.encodeBase64(photoData);
                            Feature textFeature = new Feature();
                            textFeature.setType("TEXT_DETECTION");

                            AnnotateImageRequest request = new AnnotateImageRequest();
                            request.setImage(inputImage);
                            request.setFeatures(Arrays.asList(textFeature));

                            BatchAnnotateImagesRequest batchRequest =
                                    new BatchAnnotateImagesRequest();
                            batchRequest.setRequests(Arrays.asList(request));


                            BatchAnnotateImagesResponse batchResponse = vision.images().annotate(batchRequest).execute();
                            //Vision.Images.Annotate batchRequest = vision.images().annotate(batchRequest);
                            //Use the response
                            TextAnnotation text = batchResponse.getResponses().get(0)
                                    .getFullTextAnnotation();
                            textFromPhotoGallery.setText(text.getText()) ;


                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
               */