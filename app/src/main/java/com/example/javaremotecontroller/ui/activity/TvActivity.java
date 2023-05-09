package com.example.javaremotecontroller.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.javaremotecontroller.R;
import com.example.javaremotecontroller.util.ColorUtils;
import com.example.javaremotecontroller.util.ImageUtils;
import com.example.javaremotecontroller.util.ToastUtils;
import com.example.javaremotecontroller.view.RoundMenuView;

public class TvActivity extends AppCompatActivity {

    private RoundMenuView roundMenuView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv);
    }

    private void init() {

        roundMenuView = findViewById(R.id.round_menu_view);

        RoundMenuView.RoundMenu roundMenu = new RoundMenuView.RoundMenu();
        roundMenu.selectSolidColor = ColorUtils.getColor(this, R.color.black);
        roundMenu.strokeColor = ColorUtils.getColor(this, R.color.black);
        roundMenu.icon= ImageUtils.drawable2Bitmap(this,R.drawable.ic_baseline_keyboard_arrow_up_24);
        roundMenu.onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtils.showToast(view.getContext(),"点击了1");
            }
        };
        roundMenuView.addRoundMenu(roundMenu);

        roundMenu = new RoundMenuView.RoundMenu();
        roundMenu.selectSolidColor = ColorUtils.getColor(this, R.color.black);
        roundMenu.strokeColor = ColorUtils.getColor(this, R.color.black);
        roundMenu.icon=ImageUtils.drawable2Bitmap(this,R.drawable.ic_baseline_keyboard_arrow_right_24);
        roundMenu.onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtils.showToast(view.getContext(),"点击了2");
            }
        };
        roundMenuView.addRoundMenu(roundMenu);

        roundMenu = new RoundMenuView.RoundMenu();
        roundMenu.selectSolidColor = ColorUtils.getColor(this, R.color.black);
        roundMenu.strokeColor = ColorUtils.getColor(this, R.color.black);
        roundMenu.icon = ImageUtils.drawable2Bitmap(this,R.drawable.ic_baseline_keyboard_arrow_down_24);
        roundMenu.onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtils.showToast(view.getContext(),"点击了3");
            }
        };
        roundMenuView.addRoundMenu(roundMenu);

        roundMenu = new RoundMenuView.RoundMenu();
        roundMenu.selectSolidColor = ColorUtils.getColor(this, R.color.black);
        roundMenu.strokeColor = ColorUtils.getColor(this, R.color.black);
        roundMenu.icon=ImageUtils.drawable2Bitmap(this,R.drawable.ic_baseline_keyboard_arrow_left_24);
        roundMenu.onClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtils.showToast(view.getContext(),"点击了4");
            }
        };
        roundMenuView.addRoundMenu(roundMenu);

        roundMenuView.setCoreMenu(ColorUtils.getColor(this, R.color.gray_f2f2),
                ColorUtils.getColor(this, R.color.black), ColorUtils.getColor(this, R.color.black)
                , 1, 0.43,ImageUtils.drawable2Bitmap(this,R.drawable.ic_baseline_ok_24),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ToastUtils.showToast(view.getContext(),"点击了中心圆圈");
                    }
                });
    }
}