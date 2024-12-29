package com.example.gallery.util;

import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.example.demo_full.R;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.UUID;


/* loaded from: classes3.dex */
public class BaseSettings {
    public static final String EMAIL_SUPPORT = "sonbui.bichdao2650@gmail.com";
    private static BaseSettings instance;
    private static SharedPreferences sharedPreferences;
    private final String KEY_TIME_START_APP = "pref_key_time_start_app";
    private final String KEY_DATE_INSTALL = "pref_key_date_install";
    private final String KEY_TIME_SHOW_UPDATE = "pref_key_time_show_update";
    private final String KEY_ADS_TIME_CLICK_POPUP = "pref_key_ads_time_click_popup";
    private final String KEY_ADS_TIME_SHOW_POPUP = "pref_key_ads_time_show_popup";
    private final String KEY_PURCHASE_SKU_ = "pref_key_purchase_sku_";
    private final String KEY_FIRST_SHOW_POLICY = "pref_key_first_show_policy";
    private final String KEY_COUNTRY_IP_INFO = "pref_key_country_ip_info";
    private final String KEY_DEVICE_ID_AUTO_GEN = "pref_key_device_id_auto_gen";
    private final String KEY_RATE_APP = "pref_key_rate_app";
    private final String KEY_TIME_DIALOG_RATE = "pref_key_time_dialog_rate";
    private final String KEY_INSET_BOTTOM = "pref_key_inset_bottom";
    private final String KEY_INSET_TOP = "pref_key_inset_top";

    public static BaseSettings getInstance() {
        if (instance == null) {
            instance = new BaseSettings();
        }
        return instance;
    }

    public BaseSettings() {
        if (sharedPreferences == null) {
            sharedPreferences = BaseApplication.getInstance().getSharedPreferences("settings_app", 0);
        }
//        setDateInstall();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <T> T get(String str, T t) {
        try {
            if (t.getClass() == String.class) {
                return (T) sharedPreferences.getString(str, (String) t);
            }
            if (t.getClass() == Boolean.class) {
                return (T) Boolean.valueOf(sharedPreferences.getBoolean(str, ((Boolean) t).booleanValue()));
            }
            if (t.getClass() == Float.class) {
                return (T) Float.valueOf(sharedPreferences.getFloat(str, ((Float) t).floatValue()));
            }
            if (t.getClass() == Integer.class) {
                return (T) Integer.valueOf(sharedPreferences.getInt(str, ((Integer) t).intValue()));
            }
            if (t.getClass() == Long.class) {
                return (T) Long.valueOf(sharedPreferences.getLong(str, ((Long) t).longValue()));
            }
            T t2 = (T) new Gson().fromJson(sharedPreferences.getString(str, ""), (Class) t.getClass());
            return t2 == null ? t : t2;
        } catch (Exception unused) {
            return t;
        }
    }

    public <T> T get(int i, T t) {
        return (T) get(BaseApplication.getInstance().getString(i), (String) t);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <T> void put(String str, T t) {
        try {
            SharedPreferences.Editor edit = sharedPreferences.edit();
            if (t instanceof String) {
                edit.putString(str, (String) t);
            } else if (t instanceof Boolean) {
                edit.putBoolean(str, ((Boolean) t).booleanValue());
            } else if (t instanceof Integer) {
                edit.putInt(str, ((Integer) t).intValue());
            } else if (t instanceof Long) {
                edit.putLong(str, ((Long) t).longValue());
            } else if (t instanceof Float) {
                edit.putFloat(str, ((Float) t).floatValue());
            } else {
                edit.putString(str, new Gson().toJson(t));
            }
            edit.apply();
        } catch (Exception unused) {
        }
    }

    public <T> void put(int i, T t) {
        put(BaseApplication.getInstance().getString(i), (String) t);
    }

    public void remove(String str) {
        sharedPreferences.edit().remove(str).apply();
    }

    public void clear() {
        sharedPreferences.edit().clear().apply();
    }

    public void startApp() {
        setTimeStartApp(System.currentTimeMillis());
        setTimeShowPopup(0L);
        setTimeClickPopup(0L);
    }

    public long getTimeStartApp() {
        return ((Long) get("pref_key_time_start_app",  0L)).longValue();
    }

    public void setTimeStartApp(long j) {
        put("pref_key_time_start_app",  Long.valueOf(j));
    }

//    public void setDateInstall() {
//        if (((String) get("pref_key_date_install", "")).equals("")) {
//            Calendar calendar = Calendar.getInstance();
//            put("pref_key_date_install", calendar.get(1) + "-" + BaseUtils.standardNumber(calendar.get(2) + 1) + "-" + BaseUtils.standardNumber(calendar.get(5)));
//        }
//    }

//    public String getDateInstall() {
//        if (((String) get("pref_key_date_install", "")).equals("")) {
//            setDateInstall();
//        }
//        return (String) get("pref_key_date_install", "");
//    }

    public long getTimeShowUpdate() {
        return (Long) get("pref_key_time_show_update",  0L);
    }

    public void setTimeShowUpdate(long j) {
        put("pref_key_time_show_update",  Long.valueOf(j));
    }

    public long getTimeClickPopup() {
        return ((Long) get("pref_key_ads_time_click_popup",  0L)).longValue();
    }

    public void setTimeClickPopup(long j) {
        put("pref_key_ads_time_click_popup",  Long.valueOf(j));
    }

    public long getTimeShowPopup() {
        return ((Long) get("pref_key_ads_time_show_popup",  0L)).longValue();
    }

    public void setTimeShowPopup(long j) {
        put("pref_key_ads_time_show_popup",  Long.valueOf(j));
    }

    public boolean isPurChase(String str) {
        return ((Boolean) get("pref_key_purchase_sku_" + str,  false)).booleanValue();
    }

    public void setPurchase(String str, boolean z) {
        put("pref_key_purchase_sku_" + str,  Boolean.valueOf(z));
    }

    public boolean isFirstShowPolicy() {
        return ((Boolean) get("pref_key_first_show_policy",  true)).booleanValue();
    }

    public void checkFirstShowPolicy() {
        put("pref_key_first_show_policy",  false);
    }

    public void countryIpInfo(String str) {
        put("pref_key_country_ip_info", str);
    }

    public String countryIpInfo() {
        return (String) get("pref_key_country_ip_info", "");
    }

    public String deviceId() {
        if (TextUtils.isEmpty((CharSequence) get("pref_key_device_id_auto_gen", ""))) {
            put("pref_key_device_id_auto_gen", UUID.randomUUID().toString());
        }
        return (String) get("pref_key_device_id_auto_gen", "");
    }

//    public boolean checkIS() {
//        if (!BaseConfig.getInstance().getConfig_ads().getAds_network().equals(AppLovinMediationProvider.MAX)) {
//            return true;
//        }
//        Log.d("check IS false step 0");
//        return false;
//    }

    public boolean pin(long j) {
        return ((Boolean) get("pref_key_pin_" + j,  false)).booleanValue();
    }

    public void pin(long j, boolean z) {
        put("pref_key_pin_" + j,  Boolean.valueOf(z));
    }

    public boolean hideAlerts(long j) {
        return ((Boolean) get("pref_key_hide_alerts_" + j,  false)).booleanValue();
    }

    public void hideAlerts(long j, boolean z) {
        put("pref_key_hide_alerts_" + j,  Boolean.valueOf(z));
    }

    public boolean darkMode() {
        int darkModeValue = darkModeValue();
        if (darkModeValue == 2) {
            int i = BaseApplication.getInstance().getResources().getConfiguration().uiMode & 48;
            if (i == 16) {
                return false;
            }
            if (i == 32) {
                return true;
            }
        }
        return darkModeValue == 1;
    }

    public int darkModeValue() {
        return ((Integer) get(R.string.pref_key_dark_mode, 2)).intValue();
    }

    public void setDarkMode(int i) {
        put(R.string.pref_key_dark_mode, (int) Integer.valueOf(i));
    }

    public void setDarkMode(boolean z) {
        setDarkMode(z ? 1 : 0);
    }

    public int statusRateApp() {
        return ((Integer) get("pref_key_rate_app", 0)).intValue();
    }

    public void setStatusRateApp(int i) {
        put("pref_key_rate_app",  Integer.valueOf(i));
    }

    public long getTimeDialogRate() {
        return ((Long) get("pref_key_time_dialog_rate",  0L)).longValue();
    }

    public void setTimeDialogRate(boolean z) {
        if (!z || getTimeDialogRate() == 0) {
            put("pref_key_time_dialog_rate",  Long.valueOf(System.currentTimeMillis()));
        }
    }

    public int insetBottom() {
        return ((Integer) get("pref_key_inset_bottom", 0)).intValue();
    }

    public void insetBottom(int i) {
        put("pref_key_inset_bottom",  Integer.valueOf(i));
    }

    public int insetTop() {
        return ((Integer) get("pref_key_inset_top", 0)).intValue();
    }

    public void insetTop(int i) {
        put("pref_key_inset_top", Integer.valueOf(i));
    }
}
