package io.jopen.web.api.service;


import io.jopen.web.core.model.ResponseModel;

/**
 * 描述：
 * 作者：MaXFeng
 * 时间：2018/10/2
 */
public interface LikeEssayService {
    ResponseModel like(Integer uid, Integer aid);

    ResponseModel getLike(Integer aid);
}
