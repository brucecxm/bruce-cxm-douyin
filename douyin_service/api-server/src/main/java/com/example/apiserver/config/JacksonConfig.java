package com.example.apiserver.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.build();

        // 添加Hibernate5Module，专门处理Hibernate代理对象的序列化
        Hibernate5Module hibernate5Module = new Hibernate5Module();
        // 配置为忽略懒加载属性，只序列化已加载的属性
        hibernate5Module.configure(Hibernate5Module.Feature.SERIALIZE_IDENTIFIER_FOR_LAZY_NOT_LOADED_OBJECTS, false);
        hibernate5Module.configure(Hibernate5Module.Feature.REPLACE_PERSISTENT_COLLECTIONS, true);

        objectMapper.registerModule(hibernate5Module);

        // 禁用FAIL_ON_EMPTY_BEANS，防止空对象序列化失败
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        return objectMapper;
    }
}
