package com.gao.mongodb.dynamicdatasource.config;

import com.gao.mongodb.dynamicdatasource.TenantGridFsTemplate;
import com.gao.mongodb.dynamicdatasource.TenantMongoTemplate;
import com.gao.mongodb.dynamicdatasource.util.MongoDbFactoryUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

/**
 * 默认数据源配置
 */
@Configuration
public class DefaultMongoConfig {

    @Bean(name = "defaultMongoFactory")
    public MongoDbFactory defaultMongoFactory(
            @Value("${spring.data.default.uri}") String mongoUri,
            @Value("${spring.data.default.database}") String database,
            @Value("${spring.data.default.authdatabase:}") String authDatabase,
            @Value("${spring.data.default.username:}") String username,
            @Value("${spring.data.default.password:}") String password) {
        return MongoDbFactoryUtil.createMongoDbFactory(mongoUri, database, authDatabase, username, password);
    }

    @Bean(name = "defaultMongoTemplate")
    public MongoTemplate defaultMongoTemplate(@Qualifier("defaultMongoFactory") MongoDbFactory mongoDbFactory) {
        return new MongoTemplate(mongoDbFactory);
    }

    @Bean(name = "defaultGridFsTemplate")
    public GridFsTemplate defaultGridFsTemplate(@Qualifier("defaultMongoFactory") MongoDbFactory mongoDbFactory,
                                                @Qualifier("defaultMongoTemplate") MongoTemplate mongoTemplate) {
        return new GridFsTemplate(mongoDbFactory,mongoTemplate.getConverter());
    }

    @Bean
    public TenantMongoTemplate tenantProxyMongoTemplate() {
        return new TenantMongoTemplate();
    }

    @Bean
    public TenantGridFsTemplate tenantProxyGridFsTemplate() {
        return new TenantGridFsTemplate();
    }

    @Bean(name = "mongoTemplate")
    public MongoTemplate tenantMongoTemplate(
            @Qualifier("defaultMongoFactory")MongoDbFactory mongoDbFactory,
            @Qualifier("defaultMongoTemplate") MongoTemplate mongoTemplate,
            TenantMongoTemplate template) {
        return (MongoTemplate) template.getInstance(mongoTemplate, new Class[]{MongoDbFactory.class},
                new Object[]{mongoDbFactory});
    }

    @Bean(name = "gridFsTemplate")
    public GridFsTemplate tenantGridFsTemplate(
            @Qualifier("defaultMongoFactory")MongoDbFactory mongoDbFactory,
            @Qualifier("defaultGridFsTemplate") GridFsTemplate gridFsTemplate,
            @Qualifier("defaultMongoTemplate") MongoTemplate mongoTemplate,
            TenantGridFsTemplate template) {
        return (GridFsTemplate) template.getInstance(gridFsTemplate, new Class[]{MongoDbFactory.class,
                MongoConverter.class}, new Object[]{mongoDbFactory,mongoTemplate.getConverter()});
    }
}
