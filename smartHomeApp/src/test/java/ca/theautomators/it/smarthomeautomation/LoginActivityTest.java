package ca.theautomators.it.smarthomeautomation;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class LoginActivityTest {
    LoginActivity loginActivity;
    @Before
    public void setUp() throws Exception {
        loginActivity = new LoginActivity();
    }

    @Test
    public void passwordCheck1(){
        assertTrue(loginActivity.checkPassword("Naveen@001"));

    }

    @Test
    public void passwordCheck2(){
        assertFalse(loginActivity.checkPassword("Naveen`001"));

    }

    @Test
    public void passwordCheck3(){
        assertFalse(loginActivity.checkPassword("naveengupta"));

    }

    @Test
    public void passwordCheck4(){
        assertFalse(loginActivity.checkPassword("123naveen"));

    }

    @Test
    public void emailCheck1(){
        assertTrue(loginActivity.emailCheck("naveenbt#i002gmail@abc.com"));

    }

    @Test
    public void emailCheck2(){
        assertTrue(loginActivity.emailCheck("naveenbti002@yahoo.com"));

    }
    @Test
    public void emailCheck3(){
        assertFalse(loginActivity.emailCheck("naveenabc.com@"));

    }

    @Test
    public void emailCheck4(){
        assertTrue(loginActivity.emailCheck("naveen@abc.com"));

    }
    @Test
    public void emailCheck5(){
        assertTrue(loginActivity.emailCheck("gaganajeet@gmail"));


    }




}