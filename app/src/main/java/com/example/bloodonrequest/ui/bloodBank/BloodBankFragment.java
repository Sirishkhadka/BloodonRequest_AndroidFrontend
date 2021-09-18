package com.example.bloodonrequest.ui.bloodBank;

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
import com.example.bloodonrequest.model.LatLng;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class BloodBankFragment extends Fragment implements OnMapReadyCallback {

    private BloodBankViewModel mViewModel;

    private AutoCompleteTextView actvBloodBank;
    private ImageView searchBloodBank;

    private GoogleMap bloodBankMap;
    private List<LatLng> latitudeLongitudeList;
    private Marker markerName;
    private CameraUpdate center, zoom;


    public static BloodBankFragment newInstance() {
        return new BloodBankFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.blood_bank_fragment, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.bloodBankMap);  //use SuppoprtMapFragment for using in fragment instead of activity  MapFragment = activity   SupportMapFragment = fragment
        mapFragment.getMapAsync(this);

        actvBloodBank = view.findViewById(R.id.actvBloodBankName);
        searchBloodBank = view.findViewById(R.id.imgSearchBloodBank);


        addDataToActv();

        searchBloodBank.setOnClickListener(v -> {
            if (actvBloodBank.getText().toString().trim().isEmpty()) {
                actvBloodBank.setError("Please enter blood bank name");
                actvBloodBank.requestFocus();
                return;
            }
            //get the current location of the place
            int position = searchList(actvBloodBank.getText().toString());
            if (position > -1) {
                loadMap(position);
            } else {
                Toast.makeText(getContext(), "Location not found by name!.." + actvBloodBank.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

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

        center = CameraUpdateFactory.newLatLng(new com.google.android.gms.maps.model.LatLng(latitude, longitude));

        zoom = CameraUpdateFactory.zoomTo(17);
        markerName = bloodBankMap.addMarker(new MarkerOptions().position(new com.google.android.gms.maps.model.LatLng(latitude, longitude)).title(marker));

        bloodBankMap.moveCamera(center);
        bloodBankMap.animateCamera(zoom);
        bloodBankMap.getUiSettings().setZoomControlsEnabled(true);
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

        latitudeLongitudeList.add(new LatLng(27.6994312, 85.2901898, "Nepal Red Cross Society Blood Bank, Soalteemod, Red Cross Rd, Kathmandu 44600"));
        latitudeLongitudeList.add(new LatLng(27.6733233, 85.3356681, "Emergency Blood Transfusion service Center, Bhagwan Marga, Lalitpur 44600"));
        latitudeLongitudeList.add(new LatLng(27.7358156, 85.3306925, "Tribhuwan University Teaching Hospital Blood Center, Kathmandu 44600"));
        latitudeLongitudeList.add(new LatLng(27.6733387, 85.3355802, "Lalitpur Blood Bank, Lalitpur 44600"));
        latitudeLongitudeList.add(new LatLng(27.6766582, 85.288778, "TU Lions Blood Transfusion & Research Center (#TULBTRC), Kirtipur 44600"));
        latitudeLongitudeList.add(new LatLng(27.6984173, 85.3483995, "Nobel Blood Transfusion Centre, Sinamangal Rd, Kathmandu 44600"));
        latitudeLongitudeList.add(new LatLng(27.7098166, 85.3280037, "Himal Blood Transfusion Center, Thirbum Marg, Kathmandu 44600"));
        latitudeLongitudeList.add(new LatLng(27.6698424, 85.3213712, "Nepal Bangladesh Bank Ltd. Lalitpur Branch, Lalitpur 44600"));

        String[] data = new String[latitudeLongitudeList.size()];

        for (int i = 0; i < data.length; i++) {
            data[i] = latitudeLongitudeList.get(i).getMarker();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, data);

        actvBloodBank.setAdapter(adapter);
        actvBloodBank.setThreshold(1);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(BloodBankViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onMapReady(@NonNull @NotNull GoogleMap googleMap) {
        bloodBankMap = googleMap;
        if (actvBloodBank.isPerformingCompletion()) {
            // Add a marker in Sydney and move the camera
            center = CameraUpdateFactory.newLatLng(new com.google.android.gms.maps.model.LatLng(27.7107553, 83.4679022));
            zoom = CameraUpdateFactory.zoomTo(20);
            bloodBankMap.moveCamera(center);
            bloodBankMap.animateCamera(zoom);
            bloodBankMap.getUiSettings().setZoomControlsEnabled(true);
        } else {
            List<LatLng> latLngs = new ArrayList<>();

            latLngs.add(new LatLng(27.6994312, 85.2901898, "Nepal Red Cross Society Blood Bank, Soalteemod, Red Cross Rd, Kathmandu 44600"));
            latLngs.add(new LatLng(27.6733233, 85.3356681, "Emergency Blood Transfusion service Center, Bhagwan Marga, Lalitpur 44600"));
            latLngs.add(new LatLng(27.7358156, 85.3306925, "Tribhuwan University Teaching Hospital Blood Center, Kathmandu 44600"));
            latLngs.add(new LatLng(27.6733387, 85.3355802, "Lalitpur Blood Bank, Lalitpur 44600"));
            latLngs.add(new LatLng(27.6766582, 85.288778, "TU Lions Blood Transfusion & Research Center (#TULBTRC), Kirtipur 44600"));
            latLngs.add(new LatLng(27.6984173, 85.3483995, "Nobel Blood Transfusion Centre, Sinamangal Rd, Kathmandu 44600"));
            latLngs.add(new LatLng(27.7098166, 85.3280037, "Himal Blood Transfusion Center, Thirbum Marg, Kathmandu 44600"));
            latLngs.add(new LatLng(27.6698424, 85.3213712, "Nepal Bangladesh Bank Ltd. Lalitpur Branch, Lalitpur 44600"));

            CameraUpdate center, zoom;

            for (int i = 0; i < latLngs.size(); i++) {
                center = CameraUpdateFactory.newLatLng(new com.google.android.gms.maps.model.LatLng(latLngs.get(i).getLat(),
                        latLngs.get(i).getLon()));

                zoom = CameraUpdateFactory.zoomTo(12);
                bloodBankMap.addMarker(new MarkerOptions().position(new com.google.android.gms.maps.model.LatLng(latLngs.get(i).getLat(),
                        latLngs.get(i).getLon())).title(latLngs.get(i).getMarker()));

                bloodBankMap.moveCamera(center);
                bloodBankMap.animateCamera(zoom);
                bloodBankMap.getUiSettings().setZoomControlsEnabled(true);

            }
        }
    }
}