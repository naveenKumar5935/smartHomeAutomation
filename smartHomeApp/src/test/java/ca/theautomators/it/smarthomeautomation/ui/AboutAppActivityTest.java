package ca.theautomators.it.smarthomeautomation.ui;

import android.os.Build;
import android.widget.ImageView;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import ca.theautomators.it.smarthomeautomation.R;
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

        String Text = (String) result.getText();
        assertNotNull(Text);
        assertNotEquals(Text,"Version1.0Copyright 2021");
    }

    @Test
    public void validateTextViewContent2(){
        TextView result = (TextView) activity.findViewById(R.id.textView2);

        String Text = (String) result.getText();
        assertNotNull(Text);
        assertEquals(Text,"Version1.0 Copyright 2021");
    }

    @Test
    public void validateTextViewContent3(){
        TextView result = (TextView) activity.findViewById(R.id.textView3);

        String Text = (String) result.getText();
        assertNotNull(Text);
        assertEquals(Text,"Who We Are? We are a Home Automation Company Specializing in Custom Smart Home Solutions. We can't wait to be part of your project - reach out today and see how we can make your dreams a reality!");
    }



}