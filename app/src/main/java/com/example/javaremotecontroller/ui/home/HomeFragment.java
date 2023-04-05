package com.example.javaremotecontroller.ui.home;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.javaremotecontroller.MainActivity;
import com.example.javaremotecontroller.R;
import com.example.javaremotecontroller.communication.BlueToothHelper;
import com.example.javaremotecontroller.communication.InfraredHelper;
import com.example.javaremotecontroller.databinding.FragmentHomeBinding;

import static android.content.Context.NOTIFICATION_SERVICE;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private InfraredHelper infraredHelper = new InfraredHelper();
    private BlueToothHelper blueToothHelper;
    private NotificationManager notificationManager;
    private Notification notification;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        blueToothHelper = new BlueToothHelper(getActivity());

        final TextView textView = binding.textHome;
        final Button wifiButton = root.findViewById(R.id.button_wifi);
        final Button infraredButton = root.findViewById(R.id.button_infrared);
        final Button blueToothButton = root.findViewById(R.id.button_bluetooth);
        final ImageView imageView = root.findViewById(R.id.image_view);

        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        // 給 wifi button 綁定事件
        wifiButton.setOnClickListener(onWiFiClick());
        // 紅外
        infraredButton.setOnClickListener(onInfraredClick());
        // 蓝牙
        blueToothButton.setOnClickListener(onBlueToothClick());
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 加载 animation xml
                Animation animation = AnimationUtils.loadAnimation(getActivity(),R.anim.alpha);
                imageView.startAnimation(animation);
            }
        });

        notificationManager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);

        // 大于 安卓 8
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new  NotificationChannel("cid","通知",NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        Intent intent = new Intent(getActivity(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(),0, intent,PendingIntent.FLAG_IMMUTABLE);

        notification = new NotificationCompat.Builder(getActivity(), "cid")
                .setContentTitle("标题")
                .setContentText("这是内容")
                .setSmallIcon(R.drawable.ic_baseline_settings_24)
                .setContentIntent(pendingIntent)
                .build();

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

    public View.OnClickListener onInfraredClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infraredHelper.sendSignal(getActivity());
            }
        };
    }

    public View.OnClickListener onBlueToothClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                infraredHelper.sendSignal(getActivity());
                notificationManager.notify(1, notification);
            }
        };
    }
}