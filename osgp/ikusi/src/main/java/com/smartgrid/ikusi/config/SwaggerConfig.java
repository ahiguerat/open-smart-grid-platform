package com.smartgrid.ikusi.config;

import java.util.ArrayList;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	public SwaggerConfig() {
		// TODO Auto-generated constructor stub
	}

//	@Bean
//	public Docket api() {
//	return new Docket(DocumentationType.SWAGGER_2)
//			.select()
//			.apis(RequestHandlerSelectors.any())
//			.paths(PathSelectors.any())
//			.build();			
//	}

	@Bean
    public Docket api() {
        //Adding Header
        ParameterBuilder aParameterBuilder = new ParameterBuilder();
        aParameterBuilder.name("Authorization")                 // name of header
                         .modelRef(new ModelRef("string"))
                         .parameterType("header")               // type - header
                         .defaultValue("Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqZXN1cy5ldGVsYmVydG8uZXh0IiwiZXhwIjoxNTc1ODQyNjY2LCJpYXQiOjE1NzU4MzkwNjZ9.XuYIZB299U455pMfeZzzuqiw-HbVsE61hcS8r0coPk67halXpXCoMvzj9OTrVxscOc0oTgS-ICkvOFHA_8ctqQ")        // based64 of - zone:mypassword
                         .required(true)                // for compulsory
                         .build();
        java.util.List<Parameter> aParameters = new ArrayList<>();
        aParameters.add(aParameterBuilder.build());             // add parameter
        return new Docket(DocumentationType.SWAGGER_2).select()
                                                      .apis(RequestHandlerSelectors.any())
                                                      .paths(PathSelectors.any())
                                                      .build().pathMapping("")
                                                      .globalOperationParameters(aParameters);
	}
}