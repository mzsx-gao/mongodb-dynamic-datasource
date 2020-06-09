package com.gao.mongodb.dynamicdatasource.util;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.util.StringUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * 名称: MongoDbFactoryUtil
 * 描述: 创建MongoDbFactory工具类
 *
 * @author gaoshudian
 * @date 2020-06-08 18:13
 */
public class MongoDbFactoryUtil {

    public static MongoDbFactory createMongoDbFactory(String mongoUri, String database, String authDatabase,
                                                  String username, String password) {
        List<ServerAddress> serverAddressList = new ArrayList();
        String[] addresses = StringUtils.commaDelimitedListToStringArray(mongoUri);
        for (String address : addresses) {
            String[] arr = StringUtils.split(address, ":");
            serverAddressList.add(new ServerAddress(arr[0], Integer.valueOf(arr[1])));
        }
        MongoClient mongoClient;
        if (!StringUtils.isEmpty(username)) {
            List<MongoCredential> mongoCredentialList = new ArrayList();
            mongoCredentialList.add(MongoCredential.createCredential(username, authDatabase, password.toCharArray()));
            mongoClient = new MongoClient(serverAddressList, mongoCredentialList);
        } else {
            mongoClient = new MongoClient(serverAddressList);
        }
        return new SimpleMongoDbFactory(mongoClient, database);
    }
}
