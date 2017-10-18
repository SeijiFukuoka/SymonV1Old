package br.com.symon.tracking;

import android.app.Application;
import android.content.Context;
import br.com.symon.R;
import android.util.Log;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.text.Normalizer;
import java.util.Locale;
import java.util.Map;


public class Tracking {

    public static final boolean TRIM_VALUES = false;
    public static final boolean HAS_DEBUGGER = true;

    private static Tracker trackerGA;

    private static boolean hasNotInitializedYet = true;

    private static boolean hasGoogleAnalytics;

    private static FirebaseAnalytics mFirebaseAnalytics;
    private static Context context;

    /**
     * Load default settings
     */
    public static void loadData() {
        hasGoogleAnalytics = true;
    }

    /**
     * Initialize TRACKING
     */
    public static void initialize(Application application) {
        hasNotInitializedYet = false;
        loadData();

        context = application.getApplicationContext();

        if (hasGoogleAnalytics) {
            if (trackerGA == null) {
                GoogleAnalytics analytics = GoogleAnalytics.getInstance(context);
                trackerGA = analytics.newTracker(R.xml.global_tracker);
                trackerGA.enableAdvertisingIdCollection(true);
            }
        }
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }

    /**
     * TAG PageView
     */
    public static void tagPageView(String viewName) {
        if (hasNotInitializedYet) return;
//        if (!checkWifiOnAndConnected()) return;

        String viewNameWithoutAccents = viewName;
        if (TRIM_VALUES) {
            viewNameWithoutAccents = normalizeGA(viewName);
        }

        if (hasGoogleAnalytics) {
            trackerGA.setScreenName(viewNameWithoutAccents);
            trackerGA.send(new HitBuilders.ScreenViewBuilder().build());
        }
        if (HAS_DEBUGGER) Log.i("Tracking:pageView: ", viewNameWithoutAccents);
    }

    /**
     * TAG Events
     */
    public static void tagEventWithCategory(String category, String action, String label, String value) {
//        if (!checkWifiOnAndConnected()) return;

        String trimCategoryWithoutAccents = category;
        if (TRIM_VALUES) {
            trimCategoryWithoutAccents = normalizeGA(category);
        }

        String trimLabelWithoutAccents = label;
        if (!label.equals("") && TRIM_VALUES) {
            trimLabelWithoutAccents = normalizeGA(label);
        }

        if (hasGoogleAnalytics) {
            if (value.equals("")) {
                value = "0";
            }
            Map<String, String> params = new HitBuilders.EventBuilder().setCategory(trimCategoryWithoutAccents).setAction(action).setLabel(trimLabelWithoutAccents).setValue(Long.valueOf(value)).build();
            trackerGA.send(params);
        }

        if (HAS_DEBUGGER)
            Log.i("Tracking:eventView: ", trimCategoryWithoutAccents + "," + action + "," + trimLabelWithoutAccents + "," + value);
    }

    /**
     * TAG Events / value optional
     */
    public static void tagEventWithCategory(String category, String action, String label) {
        tagEventWithCategory(category, action, label, "");
    }

    /**
     * TAG Events / label & value optionals
     */
    public static void tagEventWithCategory(String category, String action) {
        tagEventWithCategory(category, action, "", "");
    }
    /**
     * Normalize String
     *
     * @return (string) without spaces & accents
     */
    private static String normalizeGA(String value) {
        //trim
        String trimValue = value.replace(" ", "-").toLowerCase(Locale.getDefault());

        //normalize
        String trimValueWithoutAccents = Normalizer.normalize(trimValue, Normalizer.Form.NFD);
        trimValueWithoutAccents = trimValueWithoutAccents.replaceAll("[^\\p{ASCII}]", "");
        return trimValueWithoutAccents;
    }

    /**
     * Urban - Custom Events Name / value optional
     */
//
//    public static void sendUrbanCustomEvent(CustomEvent customEvent) {
//        if (!checkWifiOnAndConnected()) return;
//        UAirship.shared().getAnalytics().addEvent(customEvent);
//    }
//
//    public static void tagUrbanCustomEvent(String name) {
//        if (!checkWifiOnAndConnected()) return;
//        CustomEvent customEvent = new CustomEvent.Builder(name).create();
//        sendUrbanCustomEvent(customEvent);
//    }
//
//
//    public static void setUrbanAirshipPush(boolean status) {
//        if (!checkWifiOnAndConnected()) return;
//        UAirship.shared().getPushManager().setUserNotificationsEnabled(status);
//    }
//
//    public static void setUrbanTag(String tag) {
//        if (!checkWifiOnAndConnected()) return;
//        UAirship.shared().getPushManager().editTags().addTag(tag).apply();
//    }
//
//    public static void setUserUrbanTag(Set<String> addTags, Set<String> removeTags) {
//        if (!checkWifiOnAndConnected()) return;
//        UAirship.shared().getPushManager().editTags().addTags(addTags).removeTags(removeTags).apply();
//    }
//
//    public static void removeUserUrbanTag(Set<String> removeTags) {
//        if (!checkWifiOnAndConnected()) return;
//        UAirship.shared().getPushManager().editTags().removeTags(removeTags).apply();
//    }
//
//    public static void setUrbanUserTags(boolean isLogged) {
//        if (!checkWifiOnAndConnected()) return;
//
//        Set<String> addTags = new ArraySet<>();
//        Set<String> removeTags = new ArraySet<>();
//
//        if (isLogged) {
//            addTags.add(TrackingConstants.URBAN_EVENT.TAG.LOGGED_USER);
//            removeTags.add(TrackingConstants.URBAN_EVENT.TAG.NOT_LOGGED_USER);
//        } else {
//            addTags.add(TrackingConstants.URBAN_EVENT.TAG.NOT_LOGGED_USER);
//            removeTags.add(TrackingConstants.URBAN_EVENT.TAG.LOGGED_USER);
//        }
//        removeTags.add(TrackingConstants.URBAN_EVENT.TAG.UNSUBSCRIBED_USER);
//        Tracking.setUserUrbanTag(addTags, removeTags);
//    }
//
//    private static boolean checkWifiOnAndConnected() {
//        WifiManager wifiMgr = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//
//        if (wifiMgr.isWifiEnabled()) {
//
//            WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
//
//            return wifiInfo.getNetworkId() != -1;
//        }
//        else {
//            return false;
//        }
//    }
}