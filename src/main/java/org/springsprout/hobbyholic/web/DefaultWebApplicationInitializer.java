package org.springsprout.hobbyholic.web;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.DispatcherType;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import java.util.EnumSet;

/**
 * 웹 애플리케이션 구동시 사용할 설정 메타정보(web.xml) 
 * 
 * @author YongKwon Park
 */
public class DefaultWebApplicationInitializer implements WebApplicationInitializer {

    private final static String DISPATCHER_SERVLET_NAME = "dispatcher";
    
    @Override
    public void onStartup(ServletContext context) throws ServletException {
        AnnotationConfigWebApplicationContext dispatcherContext = new AnnotationConfigWebApplicationContext();
        dispatcherContext.register(HobbyholicWebAppConfig.class);

        ServletRegistration.Dynamic dispatcher = context.addServlet(DISPATCHER_SERVLET_NAME, new DispatcherServlet(dispatcherContext));
        dispatcher.addMapping("/");
        dispatcher.setLoadOnStartup(1);


        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);

        context.addFilter(characterEncodingFilter.getClass().getSimpleName(), characterEncodingFilter)
               .addMappingForServletNames(EnumSet.allOf(DispatcherType.class), false, DISPATCHER_SERVLET_NAME);


        HiddenHttpMethodFilter hiddenHttpMethodFilter = new HiddenHttpMethodFilter();

        context.addFilter(hiddenHttpMethodFilter.getClass().getSimpleName(), hiddenHttpMethodFilter)
               .addMappingForServletNames(EnumSet.allOf(DispatcherType.class), false, DISPATCHER_SERVLET_NAME);
    }

}
