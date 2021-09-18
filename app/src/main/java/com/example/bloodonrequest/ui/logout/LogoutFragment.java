package com.example.bloodonrequest.ui.logout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.bloodonrequest.AuthActivity;
import com.example.bloodonrequest.R;
import com.example.bloodonrequest.backendUrl.BackendUrl;

public class LogoutFragment extends Fragment {

    private LogoutViewModel mViewModel;
    private Button btnLogout;

    public static LogoutFragment newInstance() {
        return new LogoutFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.logout_fragment, container, false);

        btnLogout = view.findViewById(R.id.btnLogoutLogout);
        btnLogout.setOnClickListener(v -> {
            BackendUrl.token = "Bearer ";

            SharedPreferences sharedPreferences = getContext().getSharedPreferences("token", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("myToken");
            editor.commit();

            Intent intent = new Intent(getContext(), AuthActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return;
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(LogoutViewModel.class);
        // TODO: Use the ViewModel
    }

}