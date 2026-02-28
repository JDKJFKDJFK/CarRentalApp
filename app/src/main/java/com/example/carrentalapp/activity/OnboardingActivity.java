package com.example.carrentalapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.example.carrentalapp.MainActivity;
import com.example.carrentalapp.R;
import com.example.carrentalapp.adapter.OnboardingAdapter;
import com.example.carrentalapp.databinding.ActivityOnboardingBinding;
import com.example.carrentalapp.utils.SessionManager;

public class OnboardingActivity extends BaseActivity {

    private ActivityOnboardingBinding binding;
    private OnboardingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityOnboardingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        adapter = new OnboardingAdapter();
        binding.vpOnboarding.setAdapter(adapter);

        binding.dots.attachTo(binding.vpOnboarding);

        binding.btnSkip.setOnClickListener(v -> finishOnboarding());

        binding.btnNext.setOnClickListener(v -> {
            int pos = binding.vpOnboarding.getCurrentItem();
            if (pos < 2) {
                binding.vpOnboarding.setCurrentItem(pos + 1, true);
            } else {
                finishOnboarding();
            }
        });

        binding.vpOnboarding.registerOnPageChangeCallback(new androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                binding.btnNext.setText(position == 2 ? getString(R.string.start) : getString(R.string.next));
            }
        });
    }

    private void finishOnboarding() {
        SharedPreferences pref = getSharedPreferences("settings", MODE_PRIVATE);
        pref.edit().putBoolean("onboarding_done", true).apply();
        SessionManager session = new SessionManager(this);
        boolean isLogin = session.getUserId() != -1;

        if (isLogin) startActivity(new Intent(this, MainActivity.class));
        else startActivity(new Intent(this, LoginActivity.class));

        finish();
    }
}