package com.blackducksoftware.soleng.idcopier.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import com.blackducksoftware.soleng.idcopier.model.IDCConfig;
import com.blackducksoftware.soleng.idcopier.model.UserServiceModel;
import com.blackducksoftware.soleng.idcopier.service.LoginService;
import com.blackducksoftware.soleng.idcopier.service.ProjectService;

//Marks this class as configuration
@Configuration
// Specifies which package to scan
@ComponentScan({"com.blackducksoftware.soleng.idcopier.controller","com.blackducksoftware.soleng.idcopier.model"})

// Enables Spring's annotations
@EnableWebMvc
public class IDCWebConfig extends WebMvcConfigurerAdapter
{

    
    @Bean
    public UrlBasedViewResolver setupViewResolver()
    {
	UrlBasedViewResolver resolver = new UrlBasedViewResolver();
	resolver.setPrefix("/WEB-INF/views/");
	resolver.setSuffix(".jsp");
	resolver.setViewClass(JstlView.class);
	return resolver;
    }
    
    @Override
    public void configureDefaultServletHandling(
	    DefaultServletHandlerConfigurer configurer)
    {
	configurer.enable();
    }
    
    @Bean  
    @Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)  
    public UserServiceModel loginServiceModel() {  
        return new UserServiceModel();  
    }  
    
    @Bean  
    @Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)  
    public ProjectService projectService() {  
        return new ProjectService();  
    }  

}
