package com.example.javaremotecontroller.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.javaremotecontroller.NavPageActivity;
import com.example.javaremotecontroller.R;
import com.example.javaremotecontroller.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        final Button wifiButton = root.findViewById(R.id.button_wifi);

        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        // 給 wifi button 綁定事件
        wifiButton.setOnClickListener(onWiFiClick());

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public View.OnClickListener onWiFiClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("打印：","look");
                Toast toast = Toast.makeText(getActivity(), R.string.toast_message,Toast.LENGTH_SHORT);
                toast.show();
            }
        };
    }
}