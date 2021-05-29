package TradingPlatform;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256 {

    // Source https://www.baeldung.com/sha-256-hashing-java
    public static String hashPassword(String password) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(password.getBytes());
            String stringHash = new String(messageDigest.digest());
            return stringHash;
        }
        catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
            return password;
        }
    }

    public static void main(String[] args) {
        String testEncode = "toor";
        System.out.println(hashPassword(testEncode));
    }
}