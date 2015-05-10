package com.company.crm;

import java.util.logging.Logger;

import org.junit.Rule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

public class CrmBaseTestClass {
    static final Logger logger = Logger.getLogger(CrmBaseTestClass.class.getName());

    @Rule
    public TestWatcher watchman = new TestWatcher() {
        @Override
        protected void failed(Throwable e, Description description) {
            logger.warning(description + " failed!\n");
        }

        @Override
        protected void succeeded(Description description) {
            logger.info(description + " success!\n");
        }
    };
}
