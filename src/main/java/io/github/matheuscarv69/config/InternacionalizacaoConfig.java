package io.github.matheuscarv69.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.Locale;

@Configuration
public class InternacionalizacaoConfig {

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages"); // local do arquivo messages.properties; classpath pq o arquivo está na raiz do projeto
        messageSource.setDefaultEncoding("ISO-8859-1"); // encoding (ex: utf-8)
        messageSource.setDefaultLocale(Locale.getDefault()); // setando localização de acordo com o sistema operacional
        return messageSource;
    }

    @Bean // metodo responsavel por pegar as chaves e converte-las nas strings
    public LocalValidatorFactoryBean validatorFactoryBean() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource());
        return bean;
    }
}
