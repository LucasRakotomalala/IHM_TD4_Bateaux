package com.ihm.seawatch.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.content.FileProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.myapplication.R;

import java.io.File;
import java.io.IOException;

public class Post extends Fragment{
    private final int REQUEST_CODE = 42;
    private final String FILE_NAME = "photo.jpg";
    private File photoFile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_post, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Return to menu
        view.findViewById(R.id.button_toHome).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(Post.this)
                        .navigate(R.id.action_ThirdFragment_to_FirstFragment);
            }
        });

        view.findViewById(R.id.cameraBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* The idea of taking pictures here:
                    Click the button to create an Intent and pass a filePath.
                    After opening the camera, take a picture and store the image file in the specified filePath.
                    Then the imageView here reads the image from the filePath. */
                // The reason for using filePath instead of IntentBundle: if only intent bundle is used, the file size is limited to 1MB, and HD images cannot be transferred)
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    photoFile = getPhotoFile(FILE_NAME);
                } catch (IOException e) {
                    Toast.makeText(requireActivity(),"Erreur : " + e, Toast.LENGTH_SHORT).show();
                }
                // Wrap file object as content provider using FileProvider class (higher security, prevent external read file path)
                Uri fileProvider = FileProvider.getUriForFile(requireContext(), "com.ihm.seawatch.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);
                // Check if camera app is available
                if (takePictureIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_CODE);
                } else {
                    Toast.makeText(requireActivity(),"Impossible d'ouvrir l'appareil photo", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Read the saved photos from the path
            Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
            TextView tv = getView().findViewById(R.id.post_picName);
            tv.setText(FILE_NAME);
            ImageView imageView = getView().findViewById(R.id.bitMap);
            imageView.setImageBitmap(takenImage);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private File getPhotoFile(String fileName) throws IOException {
        File storageDirectory = this.requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(fileName, ".jpg", storageDirectory);
    }

}
