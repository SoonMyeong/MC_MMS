package kr.co.nexsys.mcp.homemanager.exception;

import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.LinkedHashMap;
import java.util.Map;

/*
    Controller 모든 Exception의 경우 AllControllerAdvice.class 에서 담당 하고 있다.(DispatcherServlet)
    그 외에 Exception 나오는 곳은 RequestDispatcher를 사용하는 Filter 부분 밖에 없으므로
    DefaultErrorAttributes를 'HM10001R' Exception에 맞게 Custom 하여 사용

    All Controller's Exception used in AllControllerAdvice.class.
    DefaultErrorAttributes used customizing that Exception used in Filter using RequestDispatcher.

 */
@Component
public class FilterErrorAttributes implements ErrorAttributes {
    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {
        Map<String, Object> errorAttributes = new LinkedHashMap<>();
        errorAttributes.put("code","HM10001R");
        errorAttributes.put("message","An authentication error has occurred.");

        return errorAttributes;
    }

    @Override
    public Throwable getError(WebRequest webRequest) {
        return null;
    }
}
