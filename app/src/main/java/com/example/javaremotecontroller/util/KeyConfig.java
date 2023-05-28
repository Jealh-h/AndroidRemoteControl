package com.example.javaremotecontroller.util;

import com.example.javaremotecontroller.R;
import com.example.javaremotecontroller.model.KeyModel;

import java.util.ArrayList;
import java.util.List;

public class KeyConfig {
    public static List<KeyModel> getMediaKeys() {
        List<KeyModel> keys = new ArrayList<>();

        keys.add(new KeyModel(R.id.hid_volume_incs, "233")); // e9
        keys.add(new KeyModel(R.id.hid_volume_decs, "234")); // ea
        keys.add(new KeyModel(R.id.brightness_incs, "111")); // 6f
        keys.add(new KeyModel(R.id.brightness_decs, "112")); // 70

        return keys;
    }

    public static List<KeyModel> getModifierKeys() {
        List<KeyModel> keys = new ArrayList<>();

        keys.add(new KeyModel(R.id.kbd_gui, "8"));
        keys.add(new KeyModel(R.id.kbd_left_alt, "4"));
        keys.add(new KeyModel(R.id.kbd_left_ctrl, "1"));
        keys.add(new KeyModel(R.id.kbd_left_shift, "2"));
        keys.add(new KeyModel(R.id.kbd_right_shift, "32"));
        keys.add(new KeyModel(R.id.kbd_right_alt, "64"));
        keys.add(new KeyModel(R.id.kbd_right_ctrl, "16"));
        return keys;
    }

    public static List<KeyModel> getCommonKeys() {
        List<KeyModel> keys = new ArrayList<>();
        keys.add(new KeyModel(R.id.kbd_a, "4"));
        keys.add(new KeyModel(R.id.kbd_b, "5"));
        keys.add(new KeyModel(R.id.kbd_c, "6"));
        keys.add(new KeyModel(R.id.kbd_d, "7"));
        keys.add(new KeyModel(R.id.kbd_e, "8"));
        keys.add(new KeyModel(R.id.kbd_f, "9"));
        keys.add(new KeyModel(R.id.kbd_g, "10"));
        keys.add(new KeyModel(R.id.kbd_h, "11"));
        keys.add(new KeyModel(R.id.kbd_i, "12"));
        keys.add(new KeyModel(R.id.kbd_j, "13"));
        keys.add(new KeyModel(R.id.kbd_k, "14"));
        keys.add(new KeyModel(R.id.kbd_l, "15"));
        keys.add(new KeyModel(R.id.kbd_m, "16"));
        keys.add(new KeyModel(R.id.kbd_n, "17"));
        keys.add(new KeyModel(R.id.kbd_o, "18"));
        keys.add(new KeyModel(R.id.kbd_p, "19"));
        keys.add(new KeyModel(R.id.kbd_q, "20"));
        keys.add(new KeyModel(R.id.kbd_r, "21"));
        keys.add(new KeyModel(R.id.kbd_s, "22"));
        keys.add(new KeyModel(R.id.kbd_t, "23"));
        keys.add(new KeyModel(R.id.kbd_u, "24"));
        keys.add(new KeyModel(R.id.kbd_v, "25"));
        keys.add(new KeyModel(R.id.kbd_w, "26"));
        keys.add(new KeyModel(R.id.kbd_x, "27"));
        keys.add(new KeyModel(R.id.kbd_y, "28"));
        keys.add(new KeyModel(R.id.kbd_z, "29"));
        keys.add(new KeyModel(R.id.kbd_1, "30"));
        keys.add(new KeyModel(R.id.kbd_2, "31"));
        keys.add(new KeyModel(R.id.kbd_3, "32"));
        keys.add(new KeyModel(R.id.kbd_4, "33"));
        keys.add(new KeyModel(R.id.kbd_5, "34"));
        keys.add(new KeyModel(R.id.kbd_6, "35"));
        keys.add(new KeyModel(R.id.kbd_7, "36"));
        keys.add(new KeyModel(R.id.kbd_8, "37"));
        keys.add(new KeyModel(R.id.kbd_9, "38"));
        keys.add(new KeyModel(R.id.kbd_0, "39"));
        keys.add(new KeyModel(R.id.kbd_enter, "40"));
        keys.add(new KeyModel(R.id.kbd_esc, "41"));
        keys.add(new KeyModel(R.id.kbd_backspace, "42"));
        keys.add(new KeyModel(R.id.kbd_tab, "43"));
        keys.add(new KeyModel(R.id.kbd_space, "44"));
        keys.add(new KeyModel(R.id.kbd_decrement, "45"));
        keys.add(new KeyModel(R.id.kbd_increment, "46"));
        keys.add(new KeyModel(R.id.kbd_left_brace, "47"));
        keys.add(new KeyModel(R.id.kbd_right_brace, "48"));
        keys.add(new KeyModel(R.id.kbd_segment, "49"));

        keys.add(new KeyModel(R.id.kbd_colon, "51"));
        keys.add(new KeyModel(R.id.kbd_quotation, "52"));
        keys.add(new KeyModel(R.id.kbd_bullet, "53"));
        keys.add(new KeyModel(R.id.kbd_less_than, "54"));
        keys.add(new KeyModel(R.id.kbd_great_than, "55"));
        keys.add(new KeyModel(R.id.kbd_question, "56"));
        keys.add(new KeyModel(R.id.kbd_caps, "57"));
        keys.add(new KeyModel(R.id.kbd_f1, "58"));
        keys.add(new KeyModel(R.id.kbd_f2, "59"));
        keys.add(new KeyModel(R.id.kbd_f3, "60"));
        keys.add(new KeyModel(R.id.kbd_f4, "61"));
        keys.add(new KeyModel(R.id.kbd_f5, "62"));
        keys.add(new KeyModel(R.id.kbd_f6, "63"));
        keys.add(new KeyModel(R.id.kbd_f7, "64"));
        keys.add(new KeyModel(R.id.kbd_f8, "65"));
        keys.add(new KeyModel(R.id.kbd_f9, "66"));
        keys.add(new KeyModel(R.id.kbd_f10, "67"));
        keys.add(new KeyModel(R.id.kbd_f11, "68"));
        keys.add(new KeyModel(R.id.kbd_f12, "69"));

        keys.add(new KeyModel(R.id.kbd_dir_right, "79"));
        keys.add(new KeyModel(R.id.kbd_dir_left, "80"));
        keys.add(new KeyModel(R.id.kbd_dir_down, "81"));
        keys.add(new KeyModel(R.id.kbd_dir_up, "82"));

        return keys;
    }

}
