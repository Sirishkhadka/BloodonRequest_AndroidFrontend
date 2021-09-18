package com.example.bloodonrequest.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bloodonrequest.AuthActivity;
import com.example.bloodonrequest.R;
import com.example.bloodonrequest.api.BloodRequest;
import com.example.bloodonrequest.api.UserApi;
import com.example.bloodonrequest.backendUrl.BackendUrl;
import com.example.bloodonrequest.model.RequestBlood;
import com.example.bloodonrequest.model.User;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DonationAdapter extends RecyclerView.Adapter<DonationAdapter.MyViewHolder> {

    Context context;
    List<RequestBlood> requestBloodList;

    public DonationAdapter(Context context, List<RequestBlood> requestBloodList) {
        this.context = context;
        this.requestBloodList = requestBloodList;
    }

    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.donation_layout, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull @NotNull DonationAdapter.MyViewHolder holder, int position) {
        RequestBlood requestBlood = requestBloodList.get(position);

        holder.tvDonationFullName.setText("For: " + requestBlood.getFullName());
        holder.tvDonationDateRequest.setText(requestBlood.getDateRequest());
        holder.tvDonationBloodAmount.setText("Amount :" + requestBlood.getBloodAmount());
        holder.tvDonationContactNumber.setText(requestBlood.getContact());
        holder.tvDonationLocation.setText("Location: " + requestBlood.getLocation());
        holder.tvDonationReason.setText("Reason: " + requestBlood.getReason());
        holder.tvDonationBloodGroup.setText(requestBlood.getBloodGroup());


        if (requestBlood.getBloodGroup().equals("0+")) {
            holder.llBloodGroup.setBackgroundColor(Color.rgb(239, 83, 80));
        }
        if (requestBlood.getBloodGroup().equals("O-")) {
            holder.llBloodGroup.setBackgroundColor(Color.rgb(180, 178, 182));
        }
        if (requestBlood.getBloodGroup().equals("A+")) {
            holder.llBloodGroup.setBackgroundColor(Color.rgb(23, 119, 243));
        }

        if (requestBlood.getBloodGroup().equals("A-")) {
            holder.llBloodGroup.setBackgroundColor(Color.rgb(80, 80, 194));
        }

        if (requestBlood.getBloodGroup().equals("B+")) {
            holder.llBloodGroup.setBackgroundColor(Color.rgb(24, 118, 242));
        }
        if (requestBlood.getBloodGroup().equals("B-")) {
            holder.llBloodGroup.setBackgroundColor(Color.rgb(174, 89, 189));
        }

        if (requestBlood.getBloodGroup().equals("AB-")) {
            holder.llBloodGroup.setBackgroundColor(Color.rgb(126, 223, 208));
        }

        holder.imageViewCallBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:+977" + requestBlood.getContact()));
            context.startActivity(intent);
        });

        holder.imageViewDeleteBtn.setOnClickListener(v -> {

            if (requestBlood.getFullName() == "Baxan Acharya") {
                deleteDonation(requestBlood.get_id(), position);
            } else {
                Toast.makeText(context, "You do not have access to delete this resource", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void deleteDonation(String id, int position) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("token", Context.MODE_PRIVATE);
        String token;
        if (sharedPreferences.getString("myToken", "").equals("")) {
            token = BackendUrl.token;
        } else {
            token = sharedPreferences.getString("myToken", "");
        }

        BloodRequest bloodRequest = BackendUrl.getInstance().create(BloodRequest.class);
        Call<RequestBlood> requestDeleteBloodCall = bloodRequest.deleteRequest(token, id);

        requestDeleteBloodCall.enqueue(new Callback<RequestBlood>() {
            @Override
            public void onResponse(Call<RequestBlood> call, Response<RequestBlood> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(context, "Code: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                requestBloodList.remove(position);
                notifyItemRemoved(position);
                notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<RequestBlood> call, Throwable t) {
                Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return requestBloodList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDonationFullName, tvDonationDateRequest,
                tvDonationBloodAmount, tvDonationContactNumber,
                tvDonationLocation, tvDonationReason, tvDonationBloodGroup;
        private ImageView imageViewCallBtn, imageViewDeleteBtn;
        private LinearLayout llBloodGroup;

        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvDonationFullName = itemView.findViewById(R.id.tvDonationFullName);
            tvDonationDateRequest = itemView.findViewById(R.id.tvDonationDateRequest);
            tvDonationBloodAmount = itemView.findViewById(R.id.tvDonationBloodAmount);
            tvDonationContactNumber = itemView.findViewById(R.id.tvDonationContactNumber);
            tvDonationLocation = itemView.findViewById(R.id.tvDonationLocation);
            tvDonationReason = itemView.findViewById(R.id.tvDonationReason);
            tvDonationBloodGroup = itemView.findViewById(R.id.tvDonationBloodGroup);
            imageViewCallBtn = itemView.findViewById(R.id.imageViewCallBtn);
            imageViewDeleteBtn = itemView.findViewById(R.id.imageViewDeleteBtn);
            llBloodGroup = itemView.findViewById(R.id.llBloodGroup);

        }
    }
}
