package com.example.bloodonrequest.ui.gallery;

import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.bloodonrequest.AuthActivity;
import com.example.bloodonrequest.DashboardActivity;
import com.example.bloodonrequest.R;
import com.example.bloodonrequest.api.BloodRequest;
import com.example.bloodonrequest.api.UserApi;
import com.example.bloodonrequest.backendUrl.BackendUrl;
import com.example.bloodonrequest.createChannel.CreateChannel;
import com.example.bloodonrequest.databinding.FragmentGalleryBinding;
import com.example.bloodonrequest.model.RequestBlood;
import com.example.bloodonrequest.model.User;
import com.example.bloodonrequest.ui.home.HomeViewModel;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;
    private TextView textView;
    private EditText etName, etBloodGroup, etAmountOfBlood, etContactNumber, etLocation, etNeededTillDate, etWhyNeedBlood;
    private Button btnRequest;
    private String pattern = "^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$";
    private Matcher m;


    public static final String NOTIFICATION_REPLY = "NotificationReply";
    public static final String CHANNNEL_ID = "SimplifiedCodingChannel";
    public static final String CHANNEL_NAME = "SimplifiedCodingChannel";
    public static final String CHANNEL_DESC = "This is a channel for Simplified Coding Notifications";

    public static final String KEY_INTENT_MORE = "keyintentmore";
    public static final String KEY_INTENT_HELP = "keyintenthelp";

    public static final int REQUEST_CODE_MORE = 100;
    public static final int REQUEST_CODE_HELP = 101;
    public static final int NOTIFICATION_ID = 200;

    private NotificationManagerCompat notificationManagerCompat;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        etName = root.findViewById(R.id.etRequestBloodName);
        etBloodGroup = root.findViewById(R.id.etRequestBloodGroup);
        etAmountOfBlood = root.findViewById(R.id.etRequestAmountBlood);
        etContactNumber = root.findViewById(R.id.etRequestContact);
        etLocation = root.findViewById(R.id.etRequestAddress);
        etNeededTillDate = root.findViewById(R.id.etRequestNeededTillDate);
        etWhyNeedBlood = root.findViewById(R.id.etRequestWhyNeedBlood);
        btnRequest = root.findViewById(R.id.btnRequest);
        textView = root.findViewById(R.id.textViewRequestBlood);

        notificationManagerCompat = NotificationManagerCompat.from(getContext());
        CreateChannel channel = new CreateChannel(getContext());
        channel.createChannel();


        getUserProfile();
        etName.requestFocus();

        etNeededTillDate.setOnClickListener(v -> {
            pickDate();
        });

        btnRequest.setOnClickListener(v -> {

            if (etName.getText().toString().trim().isEmpty()) {
                etName.setError("Please enter the full name");
                etName.requestFocus();
                return;
            }
            if (etBloodGroup.getText().toString().trim().isEmpty()) {
                etBloodGroup.setError("Please enter the blood group");
                etBloodGroup.requestFocus();
                return;
            }

            if (etAmountOfBlood.getText().toString().trim().isEmpty()) {
                etAmountOfBlood.setError("Please enter amount of blood you need");
                etAmountOfBlood.requestFocus();
                return;
            }
            Pattern r = Pattern.compile(pattern);
            if (!etContactNumber.getText().toString().trim().isEmpty()) {
                m = r.matcher(etContactNumber.getText().toString().trim());
            } else {
                etContactNumber.setError("Please enter your mobile number");
                etContactNumber.requestFocus();
                return;
            }
            if (!m.find()) {
                etContactNumber.setError("Please enter a valid mobile number");
                etContactNumber.requestFocus();
                return;
            }
            if (etLocation.getText().toString().trim().isEmpty()) {
                etLocation.setError("Please enter location details");
                etLocation.requestFocus();
                return;
            }

            if (etNeededTillDate.getText().toString().trim().isEmpty()) {
                etNeededTillDate.setError("Please enter the needed till date");
                etNeededTillDate.requestFocus();
                return;
            }

            if (etWhyNeedBlood.getText().toString().trim().isEmpty()) {
                etWhyNeedBlood.setError("Please enter why do you need blood");
                etWhyNeedBlood.requestFocus();
                return;
            }

            addBloodRequest();
        });

        return root;
    }

    private void addBloodRequest() {
        textView.setVisibility(View.VISIBLE);
        textView.setText("Processing request......");
        String fullName = etName.getText().toString().trim();
        String bloodGroup = etBloodGroup.getText().toString().trim();
        String bloodAmount = etAmountOfBlood.getText().toString().trim();
        String contact = etContactNumber.getText().toString().trim();
        String location = etLocation.getText().toString().trim();
        String dateRequest = etNeededTillDate.getText().toString().trim();
        String reason = etWhyNeedBlood.getText().toString().trim();

        RequestBlood requestBlood = new RequestBlood(fullName, bloodGroup, bloodAmount, contact, location, dateRequest, reason);
        BloodRequest bloodRequest = BackendUrl.getInstance().create(BloodRequest.class);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("token", Context.MODE_PRIVATE);
        String token;
        if (sharedPreferences.getString("myToken", "").equals("")) {
            token = BackendUrl.token;
        } else {
            token = sharedPreferences.getString("myToken", "");
        }

        Call<RequestBlood> requestBloodCall = bloodRequest.requestBlood(token, requestBlood);
        requestBloodCall.enqueue(new Callback<RequestBlood>() {
            @Override
            public void onResponse(Call<RequestBlood> call, Response<RequestBlood> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getActivity(), "Code: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                builder1.setMessage("Blood request is successfully posted!!, You will get donor soon!! ");
                builder1.setCancelable(true);
                AlertDialog alert11 = builder1.create();
                alert11.show();

                Toast.makeText(getContext(), "Request posted", Toast.LENGTH_LONG).show();
                displayNotification();
            }

            @Override
            public void onFailure(Call<RequestBlood> call, Throwable t) {
                Toast.makeText(getActivity(), "" + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        textView.setVisibility(View.INVISIBLE);
    }

    private void displayNotification() {
        //Pending intent for a notification button named More
        PendingIntent morePendingIntent = PendingIntent.getBroadcast(
                getContext(),
                REQUEST_CODE_MORE,
                new Intent(getContext(), GalleryFragment.class)
                        .putExtra(KEY_INTENT_MORE, REQUEST_CODE_MORE),
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        //Pending intent for a notification button help
        PendingIntent helpPendingIntent = PendingIntent.getBroadcast(
                getContext(),
                REQUEST_CODE_HELP,
                new Intent(getContext(), GalleryFragment.class)
                        .putExtra(KEY_INTENT_HELP, REQUEST_CODE_HELP),
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        final String KEY_QUICK_REPLY_TEXT = "quick_reply";

        String replyLabel = getResources().getString(R.string.reply_label);

        Notification.Action action = new Notification.Action.Builder(android.R.drawable.stat_notify_chat,
                "Reply Now", helpPendingIntent)
                .addRemoteInput(new RemoteInput.Builder(KEY_QUICK_REPLY_TEXT)
                        .setLabel(replyLabel).build())
                .build();
        String notifyMessage = etName.getText().toString() + " needs " + etBloodGroup.getText().toString() + " blood urgently at " + etLocation.getText().toString();
        Notification notification = new Notification.Builder(getContext(), CreateChannel.CHANNEL_1)
                .setSmallIcon(R.drawable.ic_blood_donation)
                .setContentTitle("Blood Request")
                .setContentText(etName.getText().toString() + " needs " + etBloodGroup.getText().toString() + " blood urgently at " + etLocation.getText().toString())
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setColor(Color.RED)
                .setAutoCancel(true)
                .setContentIntent(helpPendingIntent)
                .setStyle(new Notification.BigTextStyle().bigText(etName.getText().toString() + " needs " + etBloodGroup.getText().toString() + " blood urgently at " + etLocation.getText().toString()))
                .addAction(action)
                .addAction(android.R.drawable.ic_menu_compass, "More", morePendingIntent)
                .addAction(android.R.drawable.ic_menu_directions, "Help", helpPendingIntent)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE))
                .build();
        notificationManagerCompat.notify(1, notification);
    }

    private void pickDate() {
        //use the current date as the default date in the picker
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date = month + "/" + dayOfMonth + "/" + year;
                etNeededTillDate.setText(date);
            }
        }, year, month, day);
        datePickerDialog.show();
    }


    private void getUserProfile() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("token", Context.MODE_PRIVATE);
        String token;
        if (sharedPreferences.getString("myToken", "").equals("")) {
            token = BackendUrl.token;
        } else {
            token = sharedPreferences.getString("myToken", "");
        }


        UserApi usersAPI = BackendUrl.getInstance().create(UserApi.class);
        Call<User> userCall = usersAPI.getUserDetails(token);

        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 401 || response.code() == 402) {
                    Intent intent = new Intent(getActivity(), AuthActivity.class);
                    startActivity(intent);
                    return;
                }
                etName.setText(response.body().getFullName());
                etContactNumber.setText(response.body().getContactNumber());
                etLocation.setText(response.body().getCountry() + " " + response.body().getAddress());

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getActivity(), "" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}