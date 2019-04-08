package io.jopen.web.api.controller;

import io.jopen.web.api.service.LikeEssayService;
import io.jopen.web.core.annotation.RateLimit;
import io.jopen.web.core.model.ResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;

/**
 * 描述： 利用redis实现点赞功能实现
 * 此处作为redis案例来实现   不适用关系型数据库
 * 作者：MaXFeng
 * 时间：2018/10/2
 */
@RestController
public class Like4EssayController {

    @Autowired
    private LikeEssayService likeEssayService;

    /**
     * @param uid 用户id
     * @param aid 文章ID
     * @return
     */
    @RateLimit
    @GetMapping(value = "/like/{uid}/{aid}")
    public ResponseEntity<?> like(@PathVariable Integer uid, @PathVariable Integer aid) {
        ResponseModel r = this.likeEssayService.like(uid, aid);
        return ok(r);
    }

    @RateLimit
    @GetMapping(value = "/like/{aid}")
    public ResponseEntity<?> getLike(@PathVariable Integer aid){
        ResponseModel r = this.likeEssayService.getLike(aid);
        return ok(r);
    }
}
