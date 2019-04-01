package io.jopen.web.api.service.impl;

import com.alibaba.fastjson.JSON;
import io.jopen.web.api.service.LikeEssayService;
import io.jopen.web.core.model.ErrorEnum;
import io.jopen.web.core.model.LikeModel;
import io.jopen.web.core.model.ResponseModel;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 *
 */
@Service
public class LikeEssayServiceImpl implements LikeEssayService {

    @Autowired
    private RedissonClient client;

    @Value("${redisson.like.collection1}")
    private String post_set;

    @Value("${redisson.like.collection2}")
    private String post_user_like_set_;

    @Value("${redisson.like.collection3}")
    private String post_user_like_;

    @Value("${redisson.like.colelction4}")
    private String post_counter_;

    @Value("${redisson.like.collection5}")
    private String post_user_collection_entity;

    @Value("${redisson.like.lock}")
    private String post_set_lock;

    @Autowired
    private RedisTemplate<String, Serializable> redisTemplate;


    /**
     * 下面的业务情况在单线程的情况下：
     * 补充1：需使用分布式锁进行操作
     * 补充2：下面的四种数据结构的创建在什么时候进行最为适宜？
     * TODO 当前还未使用分布式锁
     * <p>
     * <p>
     * <p>
     * 涉及到四个集合
     * 1：post_set :存储被点过赞的文章id
     * 2：post_user_like_set_{$post_id}:存储用户id  对应特定的文章id   有多少篇被点过赞的文章id  就有多少个用户集合Set
     * 3：post_user_like_{$post_id}_{$user_id}：将每个用户对每个post的点赞情况放到一个hash里面去，
     * hash的字段就随意跟进需求来处理就行了。用hash是因为完全可以用hash来存储一个点赞的对象， 对应数据库的一行记录。
     * 4:post_counter_{$post_id}:维护一个计数器
     *
     * @param uid
     * @param aid
     * @return
     */
    @Override
    public ResponseModel like(Integer uid, Integer aid) {

        /**
         * 1：判断当前文章是否被点过赞
         * 2：判断当前用户是否点赞过当前文章  ->{
         *                                 1:点过赞  不重复计算
         *                                 2：没有点过赞  叠加次数
         *                                }
         * 3：维护一个统计器  统计当前文章的点赞数量
         * 4：定时刷回数据库（MySQL）
         */
        RSet<Integer> set = client.getSet(post_set);

        if (set.contains(aid)) {
            //当前文章被点过赞
            String userSetName = this.post_user_like_set_ + aid;
            RSet<Integer> userSet = client.getSet(userSetName);
            if (userSet.contains(uid)) {
                //重复点赞后端不做计算   或者是再次点赞可以认为是用户取消点赞

            } else {

                //
                userSet.add(uid);
                String likeModelHash = this.post_user_like_ + aid + "_" + uid;

                //
                LikeModel likeModel = new LikeModel();
                likeModel.setAid(aid);
                likeModel.setUid(uid);
                likeModel.setCreateTime(new Date());

                // post_user_collection_entity 属于外部key
                RLocalCachedMap<String, Object> localCachedMap = client.getLocalCachedMap(this.post_user_collection_entity, LocalCachedMapOptions.defaults());
                localCachedMap.put(likeModelHash, likeModel);

                // 对当前文章维护一个计数器
                String essayLikeCount = this.post_counter_ + aid;
                RAtomicLong atomicLong = client.getAtomicLong(essayLikeCount);
                atomicLong.incrementAndGet();
            }

        } else {
            RLock lock = client.getLock(this.post_set_lock);
            try {

                // 这个地方需要注意的是分布式锁的问题  多个线程操作同一个Set会出现并发问题
                boolean tryLock = lock.tryLock(2, 1, TimeUnit.MILLISECONDS);

                //
                if (tryLock) {
                    set.add(aid);
                    String userSetName = this.post_user_like_set_ + aid;
                    RSet<Integer> userSet = client.getSet(userSetName);
                    userSet.add(uid);
                    String likeModelHash = this.post_user_like_ + aid + "_" + uid;
                    LikeModel likeModel = new LikeModel();
                    likeModel.setAid(aid);
                    likeModel.setUid(uid);
                    likeModel.setCreateTime(new Date());

                    //
                    RLocalCachedMap<String, Object> localCachedMap = client.getLocalCachedMap(this.post_user_collection_entity, LocalCachedMapOptions.defaults());
                    localCachedMap.put(likeModelHash, likeModel);

                    // 对当前文章维护一个计数器
                    String essayLikeCount = this.post_counter_ + aid;
                    RAtomicLong atomicLong = client.getAtomicLong(essayLikeCount);
                    atomicLong.incrementAndGet();
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseModel.error(ErrorEnum.BACKEND_4XX_5XX);
            } finally {
                lock.unlock();
            }
        }
        return ResponseModel.ok();

    }

    @Override
    public ResponseModel getLike(Integer aid) {

        // 获取文章点赞数据
        Map<String, Object> info = new HashMap<>();
        RSet<Integer> set = client.getSet(post_set);

        //
        if (set.contains(aid)) {
            String userSetName = this.post_user_like_set_ + aid;

            //存放的是用户ID
            RSet<Integer> userSet = this.client.getSet(userSetName);
            RLocalCachedMap<String, Object> localCachedMap = client.getLocalCachedMap(this.post_user_collection_entity, LocalCachedMapOptions.defaults());
            for (Integer v : userSet) {
                String likeModelHash = this.post_user_like_ + aid + "_" + v;
                LikeModel likeModel = (LikeModel) localCachedMap.get(likeModelHash);
                info.put("" + v, likeModel);
            }
            String s = this.post_counter_ + aid;
            RAtomicLong atomicLong = client.getAtomicLong(s);
            info.put("点赞量：", atomicLong.get());
        }
        return ResponseModel.ok(info);
    }
}
