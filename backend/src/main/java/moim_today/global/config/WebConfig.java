package moim_today.global.config;

import moim_today.global.argumentresolver.MemberLoginArgumentResolver;
import moim_today.global.interceptor.MemberLoginInterceptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final ObjectMapper objectMapper;

    public WebConfig(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(new MemberLoginInterceptor(objectMapper))
                .order(1)
                .addPathPatterns("*")
                .excludePathPatterns("/api/login");
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new MemberLoginArgumentResolver());
    }
}