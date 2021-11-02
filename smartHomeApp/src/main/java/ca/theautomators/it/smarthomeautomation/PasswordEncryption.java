package ca.theautomators.it.smarthomeautomation;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordEncryption {

    String password;

    public PasswordEncryption(String password) {
        this.password = password;
    }

    public String getHashedPassword(){

        String salt = "BF7Y4BWJWUWU3BS";
        String hashedPassword = null;

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(salt.getBytes(StandardCharsets.UTF_8));
            byte[] bytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++){
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            hashedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hashedPassword;



    }
}
