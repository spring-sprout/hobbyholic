package org.springsprout.hobbyholic.bootstrap;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.DispatcherServlet;
import org.springsprout.hobbyholic.config.AppConfig;
import org.springsprout.hobbyholic.config.WebAppConfig;

import javax.servlet.DispatcherType;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import java.util.EnumSet;

/**
 * 웹 애플리케이션 구동시 사용할 설정 메타정보(web.xml) 
 * 
 * @author YongKwon Park
 * @author Keesun Baik
 */
public class DefaultWebApplicationInitializer implements WebApplicationInitializer {

    private final static String DISPATCHER_SERVLET_NAME = "dispatcher";
    
    @Override
    public void onStartup(ServletContext sc) throws ServletException {
        // Create the 'root' Spring application context
        AnnotationConfigWebApplicationContext root = new AnnotationConfigWebApplicationContext();
        root.register(AppConfig.class);

        // Manages the lifecycle of the root application context
        sc.addListener(new ContextLoaderListener(root));

        // Child Context
        AnnotationConfigWebApplicationContext dispatcherContext = new AnnotationConfigWebApplicationContext();
        dispatcherContext.register(WebAppConfig.class);

        // Dispatcher Servlet
        ServletRegistration.Dynamic dispatcher = sc.addServlet(DISPATCHER_SERVLET_NAME, new DispatcherServlet(dispatcherContext));
        dispatcher.addMapping("/");
        dispatcher.setLoadOnStartup(1);

        // Character Encoding Filter
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);
        sc.addFilter(characterEncodingFilter.getClass().getSimpleName(), characterEncodingFilter)
               .addMappingForServletNames(EnumSet.allOf(DispatcherType.class), false, DISPATCHER_SERVLET_NAME);

        // Enables support for DELETE and PUT request methods with web browser clients
        HiddenHttpMethodFilter hiddenHttpMethodFilter = new HiddenHttpMethodFilter();
        sc.addFilter(hiddenHttpMethodFilter.getClass().getSimpleName(), hiddenHttpMethodFilter)
               .addMappingForServletNames(EnumSet.allOf(DispatcherType.class), false, DISPATCHER_SERVLET_NAME);
    }

}
