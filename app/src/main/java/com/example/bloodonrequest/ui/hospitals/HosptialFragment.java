package com.example.bloodonrequest.ui.hospitals;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.bloodonrequest.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HosptialFragment extends Fragment implements OnMapReadyCallback {

    private HosptialViewModel mViewModel;
    private AutoCompleteTextView actvHospitals;
    private ImageView searchHospitals;

    private GoogleMap hospitalMap;
    private List<com.example.bloodonrequest.model.LatLng> latitudeLongitudeList;
    private Marker markerName;
    private CameraUpdate center, zoom;


    public static HosptialFragment newInstance() {
        return new HosptialFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.hosptial_fragment, container, false);
        actvHospitals = view.findViewById(R.id.actvHospitalName);
        searchHospitals = view.findViewById(R.id.imgSearchHospitals);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.hosptialMap);
        mapFragment.getMapAsync(this);


        searchHospitals.setOnClickListener(v -> {
            if (actvHospitals.getText().toString().trim().isEmpty()) {
                actvHospitals.setError("Please enter the hospital name");
                actvHospitals.requestFocus();
                return;
            }

            //get the current location of the place
            int position = searchList(actvHospitals.getText().toString());

            if (position > -1) {
                loadMap(position);
            } else {
                Toast.makeText(getContext(), "Location not found by name!.." + actvHospitals.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        addDataToActv();


        return view;
    }

    private void loadMap(int position) {
        //removes old marker from map
        if (markerName != null) {
            markerName.remove();
        }

        double latitude = latitudeLongitudeList.get(position).getLat();
        double longitude = latitudeLongitudeList.get(position).getLon();
        String marker = latitudeLongitudeList.get(position).getMarker();

        center = CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude));

        zoom = CameraUpdateFactory.zoomTo(17);
        markerName = hospitalMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(marker));

        hospitalMap.moveCamera(center);
        hospitalMap.animateCamera(zoom);
        hospitalMap.getUiSettings().setZoomControlsEnabled(true);

    }

    private int searchList(String name) {
        for (int i = 0; i < latitudeLongitudeList.size(); i++) {
            if (latitudeLongitudeList.get(i).getMarker().contains(name)) {
                return i;
            }
        }
        return -1;
    }

    private void addDataToActv() {

        latitudeLongitudeList = new ArrayList<>();

        latitudeLongitudeList.add(new com.example.bloodonrequest.model.LatLng(27.7080332, 85.3353207, "Welcare Hospital & Research Centre P Ltd, Pashupati Rd, Kathmandu 44600"));
        latitudeLongitudeList.add(new com.example.bloodonrequest.model.LatLng(27.7048249, 85.3136514, "Bir Hospital, Kanti Path, Kathmandu 44600"));
        latitudeLongitudeList.add(new com.example.bloodonrequest.model.LatLng(27.6841753, 85.298451, "Vayodha Hospitals, Kathmandu 44600"));
        latitudeLongitudeList.add(new com.example.bloodonrequest.model.LatLng(27.7025456, 85.3201618, "Kathmandu Model Hospital, Adwait Marg, Kathmandu 44600"));
        latitudeLongitudeList.add(new com.example.bloodonrequest.model.LatLng(27.6899912, 85.3190591, "Norvic International Hospital, Kathmandu 44617"));
        latitudeLongitudeList.add(new com.example.bloodonrequest.model.LatLng(27.7183852, 85.3464575, "Medicare National Hospital & Research Center, Ring Road, Kathmandu 44600"));
        latitudeLongitudeList.add(new com.example.bloodonrequest.model.LatLng(27.7359917, 85.3302595, "T. U. Teaching Hospital, Maharajgunj Rd, Kathmandu 44600"));
        latitudeLongitudeList.add(new com.example.bloodonrequest.model.LatLng(27.752876, 85.325889, "Grande International Hospital, Tokha Rd, Kathmandu 44600"));
        latitudeLongitudeList.add(new com.example.bloodonrequest.model.LatLng(27.7022232, 85.3088042, "Nidan Hospital, Lalitpur 44600"));

        String[] data = new String[latitudeLongitudeList.size()];

        for (int i = 0; i < data.length; i++) {
            data[i] = latitudeLongitudeList.get(i).getMarker();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, data);

        actvHospitals.setAdapter(adapter);
        actvHospitals.setThreshold(1);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(HosptialViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onMapReady(@NonNull @NotNull GoogleMap googleMap) {
        hospitalMap = googleMap;

        if (actvHospitals.isPerformingCompletion()) {
            // Add a marker in Sydney and move the camera
            center = CameraUpdateFactory.newLatLng(new LatLng(27.7107553, 83.4679022));
            zoom = CameraUpdateFactory.zoomTo(20);
            hospitalMap.moveCamera(center);
            hospitalMap.animateCamera(zoom);
            hospitalMap.getUiSettings().setZoomControlsEnabled(true);
        } else {

            List<com.example.bloodonrequest.model.LatLng> latLngs = new ArrayList<>();
            latLngs.add(new com.example.bloodonrequest.model.LatLng(27.7080332, 85.3353207, "Welcare Hospital & Research Centre P Ltd, Pashupati Rd, Kathmandu 44600"));
            latLngs.add(new com.example.bloodonrequest.model.LatLng(27.7048249, 85.3136514, "Bir Hospital, Kanti Path, Kathmandu 44600"));
            latLngs.add(new com.example.bloodonrequest.model.LatLng(27.6841753, 85.298451, "Vayodha Hospitals, Kathmandu 44600"));
            latLngs.add(new com.example.bloodonrequest.model.LatLng(27.7025456, 85.3201618, "Kathmandu Model Hospital, Adwait Marg, Kathmandu 44600"));
            latLngs.add(new com.example.bloodonrequest.model.LatLng(27.6899912, 85.3190591, "Norvic International Hospital, Kathmandu 44617"));
            latLngs.add(new com.example.bloodonrequest.model.LatLng(27.7183852, 85.3464575, "Medicare National Hospital & Research Center, Ring Road, Kathmandu 44600"));
            latLngs.add(new com.example.bloodonrequest.model.LatLng(27.7359917, 85.3302595, "T. U. Teaching Hospital, Maharajgunj Rd, Kathmandu 44600"));
            latLngs.add(new com.example.bloodonrequest.model.LatLng(27.752876, 85.325889, "Grande International Hospital, Tokha Rd, Kathmandu 44600"));
            latLngs.add(new com.example.bloodonrequest.model.LatLng(27.7022232, 85.3088042, "Nidan Hospital, Lalitpur 44600"));

            CameraUpdate center, zoom;

            for (int i = 0; i < latLngs.size(); i++) {
                center = CameraUpdateFactory.newLatLng(new LatLng(latLngs.get(i).getLat(),
                        latLngs.get(i).getLon()));

                zoom = CameraUpdateFactory.zoomTo(12);
                hospitalMap.addMarker(new MarkerOptions().position(new LatLng(latLngs.get(i).getLat(),
                        latLngs.get(i).getLon())).title(latLngs.get(i).getMarker()));

                hospitalMap.moveCamera(center);
                hospitalMap.animateCamera(zoom);
                hospitalMap.getUiSettings().setZoomControlsEnabled(true);

            }
        }
    }
}