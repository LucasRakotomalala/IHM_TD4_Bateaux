package com.ihm.seawatch.fragments;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;
import com.ihm.seawatch.R;

import static com.ihm.seawatch.fragments.Map.LOCATION_PERMS;
import static com.ihm.seawatch.fragments.Map.LOCATION_REQUEST;

public class HomePage extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_homepage, container, false);

        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("https://www.facebook.com/SeaWatch-101141728277356"))
                .setShareHashtag(new ShareHashtag.Builder()
                        .setHashtag("#JoinUsOnSeaWatch")
                        .build())
                .build();

        ShareButton shareButton = (ShareButton) rootView.findViewById(R.id.home_share);
        shareButton.setShareContent(content);

        return rootView;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Create Database in local to add points
        SQLiteDatabase sqLiteDatabase = requireContext().openOrCreateDatabase("geopoints.db", Context.MODE_PRIVATE, null);
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS Incidents(Latitude REAL, Longitude REAL, Details TEXT, Date TEXT,Temperature TEXT,Courant TEXT,Vent TEXT,DirectionVent TEXT,DirectionCourant TEXT,Precipitations TEXT);");
        sqLiteDatabase.close();

        final Switch swi = view.findViewById(R.id.gpsSwitch);
        boolean isChecked = ContextCompat.checkSelfPermission(this.requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;

        swi.setChecked(isChecked);
        // GPS Switch
        swi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                alert(isChecked);
            }
        });

        // Map function
        view.findViewById(R.id.button_map).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(HomePage.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
        view.findViewById(R.id.map_textView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(HomePage.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });

        // Post function
        view.findViewById(R.id.button_post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(HomePage.this)
                        .navigate(R.id.action_FirstFragment_to_ThirdFragment);
            }
        });
        view.findViewById(R.id.post_textView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(HomePage.this)
                        .navigate(R.id.action_FirstFragment_to_ThirdFragment);
            }
        });

        // Contact developers
        view.findViewById(R.id.contact_dev).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                composeEmail(new String[] { "dev@seawatch.com" }, "Email envoyé depuis l'application");
            }
        });
    }

    // Change switch value
    private void alert(final Boolean isChecked){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this.requireActivity());
        if (isChecked) {
            dialogBuilder.setMessage("Activer la localisation?")
                    .setCancelable(false)
                    .setNegativeButton("Non", new DialogInterface.OnClickListener(){
                        @Override public void onClick(DialogInterface dialog, int which){
                            dialog.cancel();
                            Switch swi = getView().findViewById(R.id.gpsSwitch);
                            swi.setChecked(false);
                        }
                    })
                    .setPositiveButton("Oui", new DialogInterface.OnClickListener(){
                        @Override public void onClick(DialogInterface dialog, int which){
                            requestPermissions(LOCATION_PERMS, LOCATION_REQUEST);
                            Switch swi = getView().findViewById(R.id.gpsSwitch);
                            swi.setChecked(true);
                        }
                    });
            AlertDialog alert = dialogBuilder.create();
            alert.setTitle("Localisation");
            alert.show();
        }
        else {
            Switch swi = getView().findViewById(R.id.gpsSwitch);
            Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.parse("package:" + this.requireActivity().getPackageName()));
            startActivity(intent);
            swi.setChecked(false);
        }
    }


    private void composeEmail(String[] addresses, String subject) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
