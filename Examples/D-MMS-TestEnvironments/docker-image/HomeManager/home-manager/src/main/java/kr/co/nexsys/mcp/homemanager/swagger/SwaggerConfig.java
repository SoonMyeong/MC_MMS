package kr.co.nexsys.mcp.homemanager.swagger;

import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket homeMmsApi(){

        /* 글로벌옵션을 통해 모든 파라미터에 Header값 추가 하기 */
        ParameterBuilder srcMRNBuilder = new ParameterBuilder();
        srcMRNBuilder.name("MMS-MRN") //헤더 이름
                .description("MMS's MRN") //설명
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(false)
                .build();

        ParameterBuilder certificateBuilder = new ParameterBuilder();
        certificateBuilder.name("certificate") //헤더 이름
                .description("MMS certificate") //설명
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(false)
                .build();


        List<Parameter> aParameters = new ArrayList<>();
        aParameters.add(srcMRNBuilder.build());
        aParameters.add(certificateBuilder.build());

        return new Docket(DocumentationType.SWAGGER_2)
                .globalOperationParameters(aParameters)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(Predicates.not(PathSelectors.regex("/error.*")))
                .build();
    }
}
