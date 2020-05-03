package com.ihm.seawatch.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;

import com.example.myapplication.R;

public class HomePage extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_homepage, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Switch swi = view.findViewById(R.id.gpsSwitch);
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

        // Post function
        view.findViewById(R.id.button_post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(HomePage.this)
                        .navigate(R.id.action_FirstFragment_to_ThirdFragment);
            }
        });

    }

    // Change switch value
    private void alert(final Boolean isChecked){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this.requireActivity());
        if(isChecked){
            dialogBuilder.setMessage("Activer la localisation ?")
                    .setCancelable(false)
                    .setNegativeButton("Oui", new DialogInterface.OnClickListener() {
                        @Override public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            Switch swi = getView().findViewById(R.id.gpsSwitch);
                            swi.setChecked(true);
                        }
                    })
                    .setPositiveButton("Non", new DialogInterface.OnClickListener() {
                        @Override public void onClick(DialogInterface dialog, int which) {
                            Switch swi = getView().findViewById(R.id.gpsSwitch);
                            swi.setChecked(false);
                        }
                    });
            AlertDialog alert = dialogBuilder.create();
            alert.setTitle("Localisation");
            alert.show();
        }
    }
}
