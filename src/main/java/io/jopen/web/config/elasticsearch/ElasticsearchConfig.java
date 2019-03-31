package io.jopen.web.config.elasticsearch;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;

import java.net.InetAddress;

/**
 * 描述：
 * 作者：MaXFeng
 * 时间：2018/10/1
 */
//@Configuration
public class ElasticsearchConfig {

    @Value("${elasticsearch.cluster.name}")
    private String clusterName;

    @Value("${elasticsearch.pool}")
    private Integer poolSize;

    @Value("${elasticsearch.host}")
    private String host;

    @Value("${elasticsearch.port}")
    private Integer port;


    //    @Bean
    public TransportClient init() {
        try {
            Settings settings = Settings.builder().put("cluster.name", clusterName)
                    .put("client.transport.sniff", true)
                    .put("thread_pool.search.size", poolSize)
                    .build();
            TransportClient transportClient = new PreBuiltTransportClient(settings);
            InetSocketTransportAddress address = new InetSocketTransportAddress(InetAddress.getByName(host), port);
            transportClient.addTransportAddress(address);
            return transportClient;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
