package com.gao.mongodb.dynamicdatasource.dao;

import java.util.List;

import com.gao.mongodb.dynamicdatasource.entity.LaTenant;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository("tenantDao")
public interface ILaTenantDao extends MongoRepository<LaTenant, String> {

    List<LaTenant> findByTenantCode(String tenantCode);
    
    List<LaTenant> findByTenantCodeAndTenantStatus(String tenantCode,int status);
    
    List<LaTenant> findByTenantCodeAndTenantStatusAndApplication(String tenantCode,int status,String application);
}