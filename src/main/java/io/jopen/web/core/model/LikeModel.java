package io.jopen.web.core.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 描述：
 * 作者：MaXFeng
 * 时间：2018/10/2
 */
public class LikeModel implements Serializable {

    private Integer uid;


    private Integer aid;


    private Date createTime;

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getAid() {
        return aid;
    }

    public void setAid(Integer aid) {
        this.aid = aid;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
