package com.example.bloodonrequest.ui.home;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.example.bloodonrequest.R;
import com.example.bloodonrequest.adapter.SliderImageAdapter;
import com.example.bloodonrequest.databinding.FragmentSlideshowBinding;
import com.example.bloodonrequest.model.Image;
import com.example.bloodonrequest.ui.bloodBank.BloodBankFragment;
import com.example.bloodonrequest.ui.gallery.GalleryFragment;
import com.example.bloodonrequest.ui.hospitals.HosptialFragment;
import com.example.bloodonrequest.ui.logout.LogoutFragment;
import com.example.bloodonrequest.ui.profile.ProfileFragment;
import com.example.bloodonrequest.ui.slideshow.SlideshowFragment;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment {
    private HomeViewModel homeViewModel;
    private FragmentSlideshowBinding binding;

    private static ViewPager mPager;
    private static int currentPages = 0;
    private static int NUM_PAGES = 0;
    private ArrayList<Image> imageModelArrayList;
    private CardView homeFirstCardView, homeSecondCardView, homeThirdCardView, homeFourthCardView, homeFifthCardView, homeSixthCardView;


    private int[] myImageList = new int[]{R.drawable.first, R.drawable.second,
            R.drawable.third, R.drawable.fourth
            , R.drawable.fiveslide, R.drawable.sixslide, R.drawable.sevenslide, R.drawable.eightslide};

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        homeFirstCardView = root.findViewById(R.id.homeFirstCardView);
        homeSecondCardView = root.findViewById(R.id.homeSecondCardView);
        homeThirdCardView = root.findViewById(R.id.homeThirdCardView);
        homeFourthCardView = root.findViewById(R.id.homeFourthCardView);
        homeFifthCardView = root.findViewById(R.id.homeFifthCardView);
        homeSixthCardView = root.findViewById(R.id.homeSixthCardView);

        homeFirstCardView.setOnLongClickListener(v -> {
            Toast.makeText(getContext(), "Request Blood", Toast.LENGTH_SHORT).show();
            return false;
        });

        homeFirstCardView.setOnClickListener(v -> {
            GalleryFragment galleryFragment = new GalleryFragment();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.nav_host_fragment_content_dashboard, galleryFragment);
            ft.addToBackStack(null);
            ft.commit();
        });

        homeSecondCardView.setOnLongClickListener(v -> {
            Toast.makeText(getContext(), "Blood Donations", Toast.LENGTH_SHORT).show();
            return false;
        });

        homeSecondCardView.setOnClickListener(v -> {
            SlideshowFragment slideshowFragment = new SlideshowFragment();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.nav_host_fragment_content_dashboard, slideshowFragment);
            ft.addToBackStack(null);
            ft.commit();
        });

        homeThirdCardView.setOnLongClickListener(v -> {
            Toast.makeText(getContext(), "Profile", Toast.LENGTH_SHORT).show();
            return false;
        });

        homeThirdCardView.setOnClickListener(v -> {
            ProfileFragment profileFragment = new ProfileFragment();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.nav_host_fragment_content_dashboard, profileFragment);
            ft.addToBackStack(null);
            ft.commit();
        });

        homeFourthCardView.setOnLongClickListener(v -> {
            Toast.makeText(getContext(), "Logout", Toast.LENGTH_SHORT).show();
            return false;
        });

        homeFourthCardView.setOnClickListener(v -> {
            LogoutFragment logoutFragment = new LogoutFragment();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.nav_host_fragment_content_dashboard, logoutFragment);
            ft.addToBackStack(null);
            ft.commit();
        });

        homeFifthCardView.setOnLongClickListener(v -> {
            Toast.makeText(getContext(), "Hospitals", Toast.LENGTH_SHORT).show();
            return false;
        });

        homeFifthCardView.setOnClickListener(v -> {
            HosptialFragment hosptialFragment = new HosptialFragment();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.nav_host_fragment_content_dashboard, hosptialFragment);
            ft.addToBackStack(null);
            ft.commit();
        });

        homeSixthCardView.setOnLongClickListener(v -> {
            Toast.makeText(getContext(), "Blood Banks", Toast.LENGTH_SHORT).show();
            return false;
        });

        homeSixthCardView.setOnClickListener(v -> {
            BloodBankFragment bloodBankFragment = new BloodBankFragment();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.nav_host_fragment_content_dashboard, bloodBankFragment);
            ft.addToBackStack(null);
            ft.commit();
        });


        mPager = root.findViewById(R.id.homeViewPager);
        imageModelArrayList = new ArrayList<>();
        imageModelArrayList = addToList();
        mPager.setAdapter(new SliderImageAdapter(getActivity(), imageModelArrayList));


        addSlider();


        return root;
    }

    private void addSlider() {
        NUM_PAGES = imageModelArrayList.size();

        Handler handler = new Handler();
        Runnable updateSlider = () -> {
            if (currentPages == NUM_PAGES) {
                currentPages = 0;
            }
            mPager.setCurrentItem(currentPages++, true);
        };

        Timer slideTimer = new Timer();
        slideTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(updateSlider);
            }
        }, 4000, 4000);

    }

    private ArrayList<Image> addToList() {
        ArrayList<Image> list = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            Image imageModel = new Image();
            imageModel.setImage_drawable(myImageList[i]);
            list.add(imageModel);
        }

        return list;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}