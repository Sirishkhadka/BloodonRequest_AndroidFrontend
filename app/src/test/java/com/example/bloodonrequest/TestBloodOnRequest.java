package com.example.bloodonrequest;


import com.example.bloodonrequest.bll.LoginBll;
import com.example.bloodonrequest.bll.RegisterBll;
import com.example.bloodonrequest.bll.UpdateProfileBll;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class TestBloodOnRequest {

    //login pass test
    @Test
    public void login() {
        //enter right email and password then only test will pass
        LoginBll loginBll = new LoginBll();
        boolean result = loginBll.checkUser("sirishk90@gmail.com", "898989");
        assertEquals(true, result);
    }

    //login fail test
    @Test
    public void failLogin() {
        //give wrong email or password
        LoginBll loginBll = new LoginBll();
        boolean result = loginBll.checkUser("sirishjungkhadka@gmaill.com", "Baxan12#");
        assertEquals(false, result);
    }

    //registration test
    @Test
    public void register() {
        //keep the email unique then only the test will pass
        RegisterBll registerBll = new RegisterBll(
                "hari", "Male", "O+",
                "Nepal", "9851123654",
                "kathmandu", "hlo@gmail.com",
                "12345678", "imageFile-1622446162157.jpg"
        );
        boolean result = registerBll.registerUser();
        assertEquals(true, result);
    }

    //register fail
    @Test
    public void registerFail() {
        //keep the email repeated to pass the test
        RegisterBll registerBll = new RegisterBll(
                "sirish khadka", "Male", "O+",
                "Nepal", "9851123654",
                "kathmandu", "sirishk90@gmail.com",
                "12345678", "imageFile-1622446162157.jpg"
        );
        boolean result = registerBll.registerUser();
        assertEquals(false, result);
    }

    //update profile test
    @Test
    public void updateProfileTest() {
        //give valid id of user
        UpdateProfileBll updateProfileBll = new UpdateProfileBll("sirish khadka ", "Male",
                "A+", "9860643623", "Nepal",
                "Bhatakpur", "sirishk90@gmail.com");
        boolean result = updateProfileBll.updateUser("60ca4920b42d002f5843e20e");
        assertEquals(true, result);
    }


}
