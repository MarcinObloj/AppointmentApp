package com.financial.experts.appplication.config;

import com.financial.experts.database.postgres.entity.Question;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.PagedResourcesAssembler;

@Configuration
public class HateoasConfig {

    @Bean
    public PagedResourcesAssembler<Question> questionPagedAssembler() {
        return new PagedResourcesAssembler<>(null, null);
    }
}