package ca.theautomators.it.smarthomeautomation;

import android.os.StrictMode;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class sendSMS {
    public sendSMS() {
    }

    public String sendSms() {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            // Construct data
            String apiKey = "apikey=" + "Mzg0MTY0NzE2MjM1NzczNzYxNmI3MjM2NGY2NzRlNDY=";
            String message = "&message=" + "This is your message";
            String sender = "&sender=" + "SmartHome";
            String numbers = "&numbers=" + "+16478096396";

            // Send data
            HttpURLConnection conn = (HttpURLConnection) new URL("https://api.txtlocal.com/send/?").openConnection();
            String data = apiKey + numbers + message + sender;
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
            conn.getOutputStream().write(data.getBytes("UTF-8"));
            final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            final StringBuffer stringBuffer = new StringBuffer();
            String line;
            while ((line = rd.readLine()) != null) {
                stringBuffer.append(line);
            }
            rd.close();

            Log.e("otp","sent successfully");


            return stringBuffer.toString();
        } catch (Exception e) {
            Log.e("Error SMS ","otp declined");
            return "Error "+e;
        }
    }
}