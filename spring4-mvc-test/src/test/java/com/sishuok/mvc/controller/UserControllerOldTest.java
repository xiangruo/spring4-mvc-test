package com.sishuok.mvc.controller;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.web.servlet.ModelAndView;

import com.hhb.mvc.controller.UserController;


public class UserControllerOldTest {

    private UserController userController;

    @Before
    public void setUp() {
        userController = new UserController();
        //安装userCtroller依赖 比如userService
    }

    @Test
    public void testView() {
        MockHttpServletRequest req = new MockHttpServletRequest();
        ModelAndView mv = userController.view(1L, req);

        ModelAndViewAssert.assertViewName(mv, "users/view");
        ModelAndViewAssert.assertModelAttributeAvailable(mv, "user");

    }
    
    

}
