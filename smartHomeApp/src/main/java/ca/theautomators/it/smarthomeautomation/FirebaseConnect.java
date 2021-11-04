/*
 Naveen Kumar N01355935 Section-RNA
 Gaganajeet Hanny N01350705 Section-RNA
 Sukhmanpreet Kaur N01355022 Section-RNA
 James Ricci N00411900 Section-RNA
 */

package ca.theautomators.it.smarthomeautomation;

public class FirebaseConnect {

    private static FirebaseConnect INSTANCE= null;

    private FirebaseConnect(){}

    public static FirebaseConnect getInstance(){

        if(INSTANCE == null){

            synchronized (FirebaseConnect.class){

                INSTANCE = new FirebaseConnect();
            }
        }

        return(INSTANCE);
    }

    public User getUserInfo(String email, String password){

        //TODO add get username and get password from database functionality

        User user = new User("email retrieved from firebase", "password retrieved from database");

        return user;
    }

    //TODO add getters for devices and device data


}
