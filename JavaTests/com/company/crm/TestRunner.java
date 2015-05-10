package com.company.crm;

import org.junit.runner.JUnitCore;

import com.company.crm.controllers.servlets.restapi.ClientsControllerTest;
import com.company.crm.utils.ClientValidatorTest;

public class TestRunner {
    
    public static void main(String[] args) {
        JUnitCore.runClasses(ClientValidatorTest.class);
        JUnitCore.runClasses(ClientsControllerTest.class);
    }
    
}