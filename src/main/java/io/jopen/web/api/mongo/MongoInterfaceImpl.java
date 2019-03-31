package io.jopen.web.api.mongo;

import io.jopen.web.core.model.ErrorEnum;
import io.jopen.web.core.model.MongoDBModel;
import io.jopen.web.core.model.ResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import static io.jopen.web.core.model.ResponseModel.error;
import static io.jopen.web.core.model.ResponseModel.ok;


/**
 * 描述：
 * 作者：MaXFeng
 * 时间：2018/10/3
 */
@Repository
public class MongoInterfaceImpl implements MongoInterface {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public ResponseModel insert(MongoDBModel mongoDBModel) {
        try {
            this.mongoTemplate.insert(mongoDBModel);
            return ok();
        } catch (Exception e) {
            e.printStackTrace();
            return error(ErrorEnum.BACKEND_TIMEOUT);
        }
    }


    @Override
    public Object queryById(String id) {
        try {
            MongoDBModel model = this.mongoTemplate.findById(id, MongoDBModel.class);
            return ok(model);
        } catch (Exception e) {
            return error(ErrorEnum.BACKEND_4XX_5XX);
        }
    }

    public Object queryByContent(String content) {
        Criteria criteria = Criteria.where("title").regex("10").size(1);
        Query query = new Query(criteria);
        List<MongoDBModel> mongoDBModels = this.mongoTemplate.find(query, MongoDBModel.class);
        return mongoDBModels;
    }
}
