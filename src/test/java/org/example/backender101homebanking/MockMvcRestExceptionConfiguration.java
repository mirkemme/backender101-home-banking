package org.example.backender101homebanking;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.util.WebUtils;

@TestConfiguration
public class MockMvcRestExceptionConfiguration implements WebMvcConfigurer {
    private final BasicErrorController errorController;

    public MockMvcRestExceptionConfiguration(final BasicErrorController basicErrorController) {
        this.errorController = basicErrorController;
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptor() {
            @Override
            public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response,
                                        final Object handler, final Exception ex) throws Exception {

                final int status = response.getStatus();

                if (status >= 400) {
                    response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

                    request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, status);
                    request.setAttribute(WebUtils.ERROR_STATUS_CODE_ATTRIBUTE, status);
                    request.setAttribute(WebUtils.ERROR_REQUEST_URI_ATTRIBUTE, request.getRequestURI().toString());
                    // The original exception is already saved as an attribute request
                    Exception exception = (Exception) request.getAttribute(DispatcherServlet.EXCEPTION_ATTRIBUTE);
                    request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, exception);
                    request.setAttribute(WebUtils.ERROR_MESSAGE_ATTRIBUTE, exception.getMessage());

                    if (exception != null) {
                        request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, exception);
                        request.setAttribute(WebUtils.ERROR_MESSAGE_ATTRIBUTE, exception.getMessage());
                    }
                    new ObjectMapper().writeValue(response.getOutputStream(),
                            MockMvcRestExceptionConfiguration.this.errorController.error(request).getBody());

                }
            }
        });
    }
}
