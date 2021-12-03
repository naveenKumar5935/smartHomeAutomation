package ca.theautomators.it.smarthomeautomation.ui;

import android.app.Activity;
import android.os.Build;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.w3c.dom.Text;

import ca.theautomators.it.smarthomeautomation.MainActivity;
import ca.theautomators.it.smarthomeautomation.R;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.P)
public class AboutAppActivityTest {

    private AboutAppActivity activity;


    @Before
    public void setup(){
        activity = Robolectric.setupActivity(AboutAppActivity.class);
    }

    @Test
    public void validateTextViewContent(){
        TextView result = (TextView) activity.findViewById(R.id.textView2);
        assertNotNull("TextView could not be found", result);
        assertTrue("It's true","version1_0_copyright_2021".equals(result.getText().toString()));
    }


}