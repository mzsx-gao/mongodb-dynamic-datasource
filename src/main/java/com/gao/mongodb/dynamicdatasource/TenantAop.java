package com.gao.mongodb.dynamicdatasource;

import java.text.MessageFormat;
import java.util.List;

import com.gao.mongodb.dynamicdatasource.dao.ILaTenantDao;
import com.gao.mongodb.dynamicdatasource.util.EnableStatusEnum;
import com.gao.mongodb.dynamicdatasource.entity.LaTenant;
import com.gao.mongodb.dynamicdatasource.exception.DynamicDatasourceException;
import com.gao.mongodb.dynamicdatasource.exception.ExceptionCode;
import com.gao.mongodb.dynamicdatasource.util.MongoDbFactoryUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Component;
import com.gao.mongodb.dynamicdatasource.entity.TenantVo;

@Aspect
@Order(0)
@Component
public class TenantAop {

    @Autowired
    private TenantMongoTemplate mongoTemplate;

    @Autowired
    private TenantGridFsTemplate gridFsTemplate;

    @Value("${spring.application.name}")
    private String applicationName;

    @Autowired
    private ILaTenantDao tenantDao;

    //标识商户数据源的唯一标识
    public static final String MONGODB_LIST_ITEM = "mongo-datasource:{0}:{1}";

    //切面拦截第一个参数是TenantVo的方法
    @Around(value = "execution(* com.gao.mongodb..*.*(com.gao.mongodb.dynamicdatasource.entity.TenantVo,..)) " +
            "and !execution(* com.gao.mongodb.dynamicdatasource..*.*(..))")
    public Object tenant(ProceedingJoinPoint point) throws Throwable {

        Object[] args = point.getArgs();
        TenantVo tenantVo = (TenantVo) args[0];

        String mongoSourcesKey = MessageFormat.format(MONGODB_LIST_ITEM, tenantVo.getTenantCode(), TemplateHolder.MONGO_TEMPLATE_SUFFIX);
        String fsSourcesKey = MessageFormat.format(MONGODB_LIST_ITEM, tenantVo.getTenantCode(), TemplateHolder.GRIDFS_TEMPLATE_SUFFIX);
        TemplateHolder tenantMongoConfig = TemplateHolder.getInstance();
        if (tenantMongoConfig.getMongoTemplateMap().get(mongoSourcesKey) == null) {
            //从数据库里查询数据源
            List<LaTenant> tenants = tenantDao.findByTenantCodeAndTenantStatusAndApplication(tenantVo.getTenantCode(),
                    EnableStatusEnum.ENABLE.status(), applicationName);
            if(tenants != null){
                if(tenants.size()>1){
                    throw new DynamicDatasourceException(ExceptionCode.EXCEPTION_CODE.getCode(),"一个商户只能配置一个数据源");
                }else if(tenants.size() == 1){
                    LaTenant tenant = tenants.get(0);
                    MongoDbFactory mongoDbFactory = MongoDbFactoryUtil.createMongoDbFactory(tenant.getUri(),
                            tenant.getDatabase(),
                            tenant.getAuthdatabase(),
                            tenant.getUsername(),
                            tenant.getPassword());
                    MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory);
                    GridFsTemplate gridFsTemplate = new GridFsTemplate(mongoDbFactory, mongoTemplate.getConverter());
                    tenantMongoConfig.getMongoTemplateMap().put(mongoSourcesKey,mongoTemplate);
                    tenantMongoConfig.getFsTemplateMap().put(fsSourcesKey,gridFsTemplate);
                }
            }
        }
        mongoTemplate.setTemplate(tenantMongoConfig.getMongoTemplateMap().get(mongoSourcesKey));
        gridFsTemplate.setTemplate(tenantMongoConfig.getFsTemplateMap().get(fsSourcesKey));
        return point.proceed(args);
    }

}