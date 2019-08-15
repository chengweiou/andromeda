package chengweiou.universe.andromeda.base;


import chengweiou.universe.andromeda.base.formatter.LocalDateFormatter;
import chengweiou.universe.andromeda.base.formatter.LocalDateTimeFormatter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(new LocalDateFormatter());
        registry.addFormatter(new LocalDateTimeFormatter());
    }
//    todo tip if use wormhole, cors in this project must trun off
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedMethods("*");
    }
}
