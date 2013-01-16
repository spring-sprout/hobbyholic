package org.springsprout.hobbyholic.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;

/**
 * @author Keesun Baik
 */
@Configuration
@ComponentScan(basePackages = "org.springsprout.hobbyholic",
        excludeFilters = {@ComponentScan.Filter(Controller.class), @ComponentScan.Filter(Configuration.class)})
public class AppConfig {


}
