package ca.theautomators.it.smarthomeautomation;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ReportIssueTest{
    ReportIssue reportIssue;
    @Before
    public void setUp() throws Exception {
        reportIssue = new ReportIssue();
    }

    @Test
    public void emailCheck(){
        assertTrue(reportIssue.emailCheck("kahlonsukhman18@gmail.com"));
    }

    @Test
    public void emailCheck2(){
        assertFalse(reportIssue.emailCheck("k@'ursukhman18@gmail.com"));
    }
}