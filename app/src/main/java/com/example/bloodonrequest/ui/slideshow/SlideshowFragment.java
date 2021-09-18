package com.example.bloodonrequest.ui.slideshow;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bloodonrequest.R;
import com.example.bloodonrequest.adapter.DonationAdapter;
import com.example.bloodonrequest.api.BloodRequest;
import com.example.bloodonrequest.backendUrl.BackendUrl;
import com.example.bloodonrequest.databinding.FragmentSlideshowBinding;
import com.example.bloodonrequest.model.RequestBlood;
import com.example.bloodonrequest.ui.gallery.GalleryViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;
    private RecyclerView recyclerView;
    private EditText etBloodGroup;
    private ImageView imgSearchBlood;

    private String token;
    List<RequestBlood> requestBloodList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);

        recyclerView = root.findViewById(R.id.bloodGroupRecyclerView);
        etBloodGroup = root.findViewById(R.id.etBloodGroupSearch);
        imgSearchBlood = root.findViewById(R.id.imgSearchBlood);

        imgSearchBlood.setOnClickListener(v -> {
            searchBlood();
        });

        getAllDonations();

        return root;

    }

    private void getAllDonations() {
        requestBloodList = new ArrayList<>();
        BloodRequest bloodRequest = BackendUrl.getInstance().create(BloodRequest.class);
        Call<List<RequestBlood>> listCallDonation = bloodRequest.getAllBloodDonations();

        listCallDonation.enqueue(new Callback<List<RequestBlood>>() {
            @Override
            public void onResponse(Call<List<RequestBlood>> call, Response<List<RequestBlood>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getActivity(), "Code: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                List<RequestBlood> bloodList = response.body();
                DonationAdapter donationAdapter = new DonationAdapter(getActivity(), bloodList);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.hasFixedSize();
                recyclerView.setAdapter(donationAdapter);
                donationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<RequestBlood>> call, Throwable t) {
                Toast.makeText(getActivity(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void searchBlood() {
        if (etBloodGroup.getText().toString().trim().isEmpty()) {
            return;
        }
        requestBloodList = new ArrayList<>();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("token", Context.MODE_PRIVATE);
        if (sharedPreferences.getString("myToken", "").equals("")) {
            token = BackendUrl.token;
        } else {
            token = sharedPreferences.getString("myToken", "");
        }

        BloodRequest bloodRequest = BackendUrl.getInstance().create(BloodRequest.class);

        Call<List<RequestBlood>> listCallDonation = bloodRequest.searchBlood(token, etBloodGroup.getText().toString().trim());

        listCallDonation.enqueue(new Callback<List<RequestBlood>>() {
            @Override
            public void onResponse(Call<List<RequestBlood>> call, Response<List<RequestBlood>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getActivity(), "" + response.code(), Toast.LENGTH_LONG).show();
                    return;
                }

                List<RequestBlood> requestBloods = response.body();

                if (requestBloods.size() == 0) {
                    Toast.makeText(getContext(), "The blood group cannot be found", Toast.LENGTH_SHORT).show();
                    return;
                }
                DonationAdapter donationAdapter = new DonationAdapter(getContext(), requestBloods);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.hasFixedSize();
                recyclerView.setAdapter(donationAdapter);
                donationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<RequestBlood>> call, Throwable t) {
                Toast.makeText(getActivity(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


}