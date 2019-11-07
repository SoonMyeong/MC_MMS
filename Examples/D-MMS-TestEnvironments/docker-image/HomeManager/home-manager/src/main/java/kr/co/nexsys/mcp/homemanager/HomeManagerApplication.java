package kr.co.nexsys.mcp.homemanager;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@ServletComponentScan
@SpringBootApplication
public class HomeManagerApplication {

    public static void main(String[] args) {

        SpringApplication.run(HomeManagerApplication.class, args);
    }

    @Bean
    public CommonsRequestLoggingFilter commonsRequestLoggingFilter() {
        CommonsRequestLoggingFilter loggingFilter = new CommonsRequestLoggingFilter();
        loggingFilter.setIncludeHeaders(true);
        loggingFilter.setIncludeQueryString(true);
        loggingFilter.setIncludePayload(true);
        loggingFilter.setIncludeClientInfo(true);

        loggingFilter.setBeforeMessagePrefix("Before : ");
        loggingFilter.setBeforeMessageSuffix("");
        loggingFilter.setAfterMessagePrefix("After : ");
        loggingFilter.setAfterMessageSuffix("");

        return loggingFilter;
    }

}
