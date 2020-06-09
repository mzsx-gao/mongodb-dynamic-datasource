
package com.gao.mongodb.dynamicdatasource.config;

import com.gao.mongodb.dynamicdatasource.util.MongoDbFactoryUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * 公共配置库,即存放各个应用实际数据源信息的数据库连接配置
 * ILaTenantDao会使用这个配置
 */
@Configuration
@EnableMongoRepositories(basePackages = "com.gao.mongodb.dynamicdatasource.dao", mongoTemplateRef = "configMongoTemplate")
public class ConfigMongoConfig {


	@Bean(name = "configMongoFactory")
	public MongoDbFactory configMongoFactory(
	        @Value("${spring.data.common.uri}") String mongoUri,
			@Value("${spring.data.common.database}") String database,
			@Value("${spring.data.common.authdatabase}") String authDatabase,
			@Value("${spring.data.common.username}") String username,
			@Value("${spring.data.common.password}") String password){
		return MongoDbFactoryUtil.createMongoDbFactory(mongoUri, database, authDatabase, username, password);
	}

	@Bean(name = "configMongoTemplate")
	public MongoTemplate configMongoTemplate(@Qualifier("configMongoFactory") MongoDbFactory mongoDbFactory) {
		MappingMongoConverter converter = new MappingMongoConverter(new DefaultDbRefResolver(mongoDbFactory), new MongoMappingContext());
		converter.setTypeMapper(new DefaultMongoTypeMapper(null));
		return new MongoTemplate(mongoDbFactory, converter);
	}

	@Bean(name = "configGridFsTemplate")
	public GridFsTemplate configGridFsTemplate(@Qualifier("configMongoFactory") MongoDbFactory mongoDbFactory,
                                               @Qualifier("configMongoTemplate") MongoTemplate mongoTemplate) {
		return new GridFsTemplate(mongoDbFactory, mongoTemplate.getConverter());
	}
}
