package de.lebenshilfe_muenster.uk_gebaerden_muensterland.sample;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.test.ApplicationTestCase;
import android.test.MoreAsserts;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {

    private  Application applicationUnderTest;

    public ApplicationTest() {
        super(Application.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        createApplication();
        this.applicationUnderTest = getApplication();

    }

    public void testCorrectVersion() throws Exception {
        PackageInfo info = applicationUnderTest.getPackageManager().getPackageInfo(applicationUnderTest.getPackageName(), 0);
        assertNotNull(info);
        MoreAsserts.assertMatchesRegex("\\d\\.\\d", info.versionName);
    }
}