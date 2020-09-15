package com.smartgrid.ikusi.config;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;

@Configuration
public class JacksonConfig {

	@Bean
	public Jackson2ObjectMapperBuilderCustomizer addCustomBigDecimalDeserialization() {
		return new Jackson2ObjectMapperBuilderCustomizer() {
			@Override
			public void customize(Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder) {
				jacksonObjectMapperBuilder.featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
				jacksonObjectMapperBuilder.modules(new Hibernate5Module());
			}

		};
	}

	@Bean
	public ObjectMapper includeTransientObjectMapper() {
		Hibernate5Module hibernate5Module = new Hibernate5Module();
		hibernate5Module.disable(Hibernate5Module.Feature.USE_TRANSIENT_ANNOTATION);
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(hibernate5Module);
		return mapper;
	}
}
