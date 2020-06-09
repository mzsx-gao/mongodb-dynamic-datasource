package com.gao.mongodb.dynamicdatasource;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

/**
 * 单例类，全局只有唯一一个
 * 该类保存了所有商户的mongoTemplate和gridFsTemplate
 */
@Data
public class TemplateHolder {

  public static final String MONGO_TEMPLATE_SUFFIX = "MongoTemplate";
  public static final String GRIDFS_TEMPLATE_SUFFIX = "GridFsTemplate";

  private Map<String, MongoTemplate> mongoTemplateMap = new HashMap<>();
  private Map<String, GridFsTemplate> fsTemplateMap = new HashMap<>();

  private static class TenantMongoConfigHolder{
    private static TemplateHolder instance=new TemplateHolder();
  }
  private TemplateHolder(){

  }
  public static TemplateHolder getInstance(){
    return TenantMongoConfigHolder.instance;
  }

}
