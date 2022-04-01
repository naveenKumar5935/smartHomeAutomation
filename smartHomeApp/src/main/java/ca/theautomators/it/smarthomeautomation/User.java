/*
 Naveen Kumar N01355935 Section-RNA
 Gaganajeet Hanny N01350705 Section-RNA
 Sukhmanpreet Kaur N01355022 Section-RNA
 James Ricci N00411900 Section-RNA
 */

package ca.theautomators.it.smarthomeautomation;

public class User {

    String email, password, fullName, phone, code, devices;

    public User(String email, String password, String fullName, String phone, String code){
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.phone = phone;
        this.code=code;
        this.devices="";
    }

    public String getCode() {
        return code;
    }
    public void setCode(String code){
        this.code = code;
    }

    public String getDevices() {
        return devices;
    }


    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
