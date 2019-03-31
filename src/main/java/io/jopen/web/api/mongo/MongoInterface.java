package io.jopen.web.api.mongo;

import io.jopen.web.core.model.MongoDBModel;
import io.jopen.web.core.model.ResponseModel;

/**
 * 描述：
 * 作者：MaXFeng
 * 时间：2018/10/3
 */

public interface MongoInterface {

    default void test1() {
        System.out.println("hello world");
    }

    ResponseModel insert(MongoDBModel mongoDBModel);

    Object queryById(String id);

    Object queryByContent(String content);
}
