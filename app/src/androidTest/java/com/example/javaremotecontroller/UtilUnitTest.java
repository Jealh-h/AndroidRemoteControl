package com.example.javaremotecontroller;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.javaremotecontroller.util.util;


import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UtilUnitTest {

    Context ctx;

    @Before
    public void init() {
        System.out.println("工具类 Util 测试");
    }

    @Test
    public void testContext() {
        // Context of the app under test.
        ctx = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.javaremotecontroller", ctx.getPackageName());
    }

    /**
     * 静态变量测试
     */
    @Test
    public void testStaticMemberVariable() {
        assertEquals(util.BLUE_TOOTH_DEVICE_CARRY_DATA_KEY,"ADDRESS_BLUE_DEVICE");
        assertEquals(util.BRAND_LIST_TO_OPERATION_PANEL_KEY,"BRAND_MODEL");
        assertEquals(util.CATEGORY_ID_AIR_CONDITION,1);
        assertEquals(util.CATEGORY_ID_FAN,7);
        assertEquals(util.CATEGORY_ID_LAMP,10);
        assertEquals(util.CATEGORY_ID_SOUND,9);
        assertEquals(util.CATEGORY_ID_SWEEP,12);
        assertEquals(util.CATEGORY_ID_TV,2);
        assertEquals(util.DASHBOARD_TO_BRAND_LIST_KEY,"CATEGORY_MODEL");
    }

    @Test
    public void testInt2Decimal() {
        long address = 3232235776L;
        assertEquals(util.int2Decimal(address), "192.168.1.0");
    }

    @Test
    public void testDpToPx() {
        // 预期被 catch 捕获
        assertEquals(util.dpToPx(16,ctx), 0);
    }

    @Test
    public void testPxToDp() {
        // 预期被 catch 捕获
        assertEquals(util.pxToDp(16, ctx), 0);
    }
}
