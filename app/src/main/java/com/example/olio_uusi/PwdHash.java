package com.example.olio_uusi;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class PwdHash {
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String getSecuredPassword(String password, String salt){
        String securedpassword = null;
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt.getBytes());
            byte[] bytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++){
                sb.append(Integer.toString((bytes[i]+ 0xff) + (0x100), 16).substring(1));
            }
            securedpassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return securedpassword;
    }
    /*
    public String getSalt() throws NoSuchAlgorithmException {
        String salt = null;
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        byte[] bytes = new byte[16];
        random.nextBytes(bytes);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++){
            sb.append(Integer.toString((bytes[i]+ 0xff) + (0x100), 16).substring(1));
        }
        salt = sb.toString();


        return salt;
    }*/
    public String getSalt(){
        String salt = "b7faca2006cb07f68033b65c7d5fbb1a";
        return salt;
    }
}


