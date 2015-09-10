package com.mityakoval.whereami;

import android.test.AndroidTestCase;

import com.mityakoval.whereami.containers.Settings;

/**
 * Created by mityakoval on 10/09/15.
 */
public class SettingsSavingTest extends AndroidTestCase{

    Settings settingsDefault;
    Settings settingsCustom;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        settingsCustom = new Settings();
        settingsCustom.setShowLatLong(false);
        settingsCustom.setShowCelsius(false);
        settingsCustom.setShowFahrenheit(true);

        settingsDefault = new Settings();
    }

    public void testSaveSettings() throws Exception {
        settingsCustom.saveSettings(mContext);
    }

    public void testLoadSettings() throws Exception {
        settingsCustom.loadSettings(mContext);
        assertNotSame(settingsDefault.isShowLatLong(), settingsCustom.isShowLatLong());
        assertNotSame(settingsDefault.isShowCelsius(), settingsCustom.isShowCelsius());
        assertNotSame(settingsDefault.isShowFahrenheit(), settingsCustom.isShowFahrenheit());
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        settingsCustom.deleteSettingsFile(mContext);
    }
}
