package io.jopen.web.config.elasticsearch;

import io.jopen.web.core.context.ProjectHolder;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.get.GetRequestBuilder;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.json.JSONObject;

import java.util.Map;

/**
 * 描述：用于操作es数据库
 * 作者：MaXFeng
 * 时间：2018/10/1
 */
public class ElasticsearchUtils {

    private static TransportClient client = ProjectHolder.getBean(TransportClient.class);

    /**
     * 创建索引
     *
     * @param indexStr
     * @return
     */
    public static boolean createIndex(String indexStr) {
        CreateIndexRequestBuilder prepareCreate = client.admin().indices().prepareCreate(indexStr);
        CreateIndexResponse response = prepareCreate.execute().actionGet();
        return response.isAcknowledged();
    }


    /**
     * 索引删除
     *
     * @param indexStr
     * @return
     */
    public static boolean deleteIndex(String indexStr) {
        DeleteIndexResponse response = client.admin().indices().prepareDelete(indexStr).execute().actionGet();
        return response.isAcknowledged();
    }

    /**
     * 判断索引是否存在
     *
     * @param indexStr
     * @return
     */
    public static boolean isExistIndex(String indexStr) {
        IndicesExistsResponse response =
                client.admin().indices().exists(new IndicesExistsRequest(indexStr)).actionGet();
        return response.isExists();
    }


    /**
     * 向索引中添加数据
     *
     * @param jsonObject 要添加的数据
     * @param index      索引名称
     * @param type       类型名称
     * @param id         数据id   指定ID添加
     * @return
     */
    public static String addData(JSONObject jsonObject, String index, String type, String id) {
        IndexResponse indexResponse = client.prepareIndex(index, type, id).setSource(jsonObject).get();
        return indexResponse.getId();
    }


    /**
     * 不指定ID进行数据添加
     *
     * @param jsonObject
     * @param index
     * @param type
     * @return
     */
    public static String addData(JSONObject jsonObject, String index, String type) {
        IndexResponse indexResponse = client.prepareIndex(index, type).setSource(jsonObject).get();
        return indexResponse.getId();
    }


    /**
     * 数据删除
     *
     * @param type
     * @param index
     * @param id
     */
    public static void deleteData(String type, String index, String id) {
        client.prepareDelete(index, type, id).get();
    }


    /**
     * 根据id修改数据
     *
     * @param jsonObject
     * @param index
     * @param type
     * @param id
     */
    public static void updateDataById(JSONObject jsonObject, String index, String type, String id) {
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.doc(jsonObject).index(index).type(type).id(id);
        client.update(updateRequest);
    }


    /**
     * 根据id查询数据
     *
     * @param index
     * @param type
     * @param id
     * @param fields 要显示的字段    用逗号隔开
     * @return
     */
    public static Map<String, Object> searchDataById(String index, String type, String id, String fields) {
        GetRequestBuilder prepareGet = client.prepareGet(index, type, id);
        if (StringUtils.isNotEmpty(fields)) {
            /**
             * 传入要显示的字段
             */
            prepareGet.setFetchSource(fields.split(","), null);
        }
        GetResponse response = prepareGet.execute().actionGet();
        return response.getSource();
    }
}
