package com.dangkhoa.socialnetwork.configuration

import com.google.common.base.CaseFormat
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.filter.OncePerRequestFilter

import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletRequestWrapper
import javax.servlet.http.HttpServletResponse
import java.util.concurrent.ConcurrentHashMap

@Configuration
class AppConfig {

    @Bean
    Filter snakeConverter() {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                    throws ServletException, IOException {
                final Map<String, String[]> formattedParams = new ConcurrentHashMap<>();

                for (String param : request.getParameterMap().keySet()) {
                    String formattedParam = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, param);
                    formattedParams.put(formattedParam, request.getParameterValues(param));
                }

                filterChain.doFilter(new HttpServletRequestWrapper(request) {
                    @Override
                    String getParameter(String name) {
                        return formattedParams.containsKey(name) ? formattedParams.get(name)[0] : null
                    }

                    @Override
                    Enumeration<String> getParameterNames() {
                        return Collections.enumeration(formattedParams.keySet())
                    }

                    @Override
                    String[] getParameterValues(String name) {
                        return formattedParams.get(name)
                    }

                    @Override
                    Map<String, String[]> getParameterMap() {
                        return formattedParams
                    }
                }, response)
            }
        };
    }
}
