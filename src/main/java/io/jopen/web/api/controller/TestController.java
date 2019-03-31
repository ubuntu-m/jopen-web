package io.jopen.web.api.controller;

import io.jopen.web.api.mongo.MongoInterface;
import io.jopen.web.core.annotation.RateLimit;
import io.jopen.web.core.model.MongoDBModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

import static org.springframework.http.ResponseEntity.ok;

@RestController
public class TestController {

    @Autowired
    private MongoInterface mongoInterface;

    @RateLimit
    @GetMapping(value = "/test")
    public Object test() {
        System.out.println("hello world");
        return "hello world";
    }

    @GetMapping(value = "/query")
    public ResponseEntity<?> getOne() {
        return ok(this.mongoInterface.queryByContent("hello world"));
    }

    @RateLimit
    @GetMapping(value = "/test1")
    public Object test1() {
        return "hello world  ========";
    }


    @GetMapping(value = "/testMongo")
    public Object testMongo() {
        MongoDBModel model = new MongoDBModel();
        model.setId(UUID.randomUUID().toString().replace("-", ""));
        model.setDescription("税务部门依法查处范冰冰“阴阳合同”等偷逃税问题");
        model.setTitle("税务部门依法查处范冰冰“阴阳合同”等偷逃税问题");
        model.setKeys("逃税，阴阳合同");
        model.setPublishDate(new Date());
        model.setSubscribe(1000L);
        return this.mongoInterface.insert(model);
    }

    @GetMapping(value = "/mongo/{_id}")
    public Object queryEntity(@PathVariable String _id) {
        return this.mongoInterface.queryById(_id);
    }
}
