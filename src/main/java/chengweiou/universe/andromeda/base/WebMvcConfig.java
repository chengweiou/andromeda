package chengweiou.universe.andromeda.base;


import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import chengweiou.universe.andromeda.base.formatter.InstantFormatter;
import chengweiou.universe.andromeda.base.formatter.LocalDateFormatter;
import chengweiou.universe.andromeda.base.formatter.LocalDateTimeFormatter;
import chengweiou.universe.andromeda.interceptor.AuthInterceptorMe;
import chengweiou.universe.andromeda.interceptor.AuthInterceptorMg;
import chengweiou.universe.andromeda.interceptor.OptionsInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(new LocalDateFormatter());
        registry.addFormatter(new LocalDateTimeFormatter());
        registry.addFormatter(new InstantFormatter());
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new OptionsInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(new AuthInterceptorMg()).addPathPatterns("/mg/**");
        registry.addInterceptor(new AuthInterceptorMe()).addPathPatterns("/me/**");

    }
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedMethods("*");
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.stream()
                .filter(converter -> converter instanceof MappingJackson2HttpMessageConverter)
                .findFirst()
                .ifPresent(converter -> ((MappingJackson2HttpMessageConverter) converter).setDefaultCharset(StandardCharsets.UTF_8));
    }
}
