package com.ismyself.canal.listener;


import com.alibaba.fastjson.JSON;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.ismyself.canal.mq.queue.TopicQueue;
import com.ismyself.canal.mq.send.TopicMessageSender;
import com.ismyself.feign.ContentFeign;
import com.ismyself.pojo.Content;
import com.xpand.starter.canal.annotation.*;
import entity.Message;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;


import java.util.List;


/**
 * package com.ismyself.canal.queue;
 *
 * @auther txw
 * @create 2019-08-29  11:13
 * @description：
 */
@CanalEventListener
public class CanalDataEventListener {

    @Autowired
    private ContentFeign contentFeign;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private TopicMessageSender topicMessageSender;

    /**
     * 增加数据监听
     *
     * @param eventType
     * @param rowData
     */
    @InsertListenPoint()
    public void onEventInsert(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
        rowData.getAfterColumnsList().forEach(c -> System.out.println("By--Annotation:" + c.getName() + "::" + c.getValue()));
    }

    /**
     * 修改数据监听
     *
     * @param rowData
     */
    @UpdateListenPoint
    public void onEventUpdate(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
        System.out.println("UpdateListenPoint");
        rowData.getAfterColumnsList().forEach(c -> System.out.println("By--Annotation:" + c.getName() + "::" + c.getValue()));
    }

    /**
     * 删除数据监听
     *
     * @param entryType
     */
    @DeleteListenPoint
    public void onEventDelete(CanalEntry.EntryType entryType) {
        System.out.println("DeleteListenPoint");
    }

    /**
     * 自定义数据修改监听
     *
     * @param entryType
     * @param rowData
     */
/*    @ListenPoint(destination = "example",schema = "changgou_content",table = {"tb_content_category","tb_content"},eventType = CanalEntry.EventType.UPDATE)
    public void onEventCustomUpdate(CanalEntry.EntryType entryType,CanalEntry.RowData rowData){
        System.out.println("DeleteListenPoint");
        rowData.getAfterColumnsList().forEach(c-> System.out.println("By--Annotation:"+c.getName()+"::"+c.getValue()));
    }*/


    /***
     * 修改广告数据修改监听
     * 同步数据到Redis
     * @param eventType
     * @param rowData
     */
//    @ListenPoint(destination = "example", schema = "changgou_content", table = {"tb_content"}, eventType = {CanalEntry.EventType.UPDATE,CanalEntry.EventType.INSERT,CanalEntry.EventType.DELETE})
    public void onEventCustomUpdate(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
        //获取广告分类的ID
        String categoryId = getColumn(rowData, "category_id");
        //根据广告分类ID获取所有广告
        Result<List<Content>> result = contentFeign.findByCategoryId(Long.valueOf(categoryId));
        //将广告数据存入到Redis缓存
        List<Content> contents = result.getData();
        stringRedisTemplate.boundValueOps("content_"+categoryId).set(JSON.toJSONString(contents));
    }


    /***
     * 获取指定列的值
     * @param rowData
     * @param columnName
     * @return
     */
    public String getColumn(CanalEntry.RowData rowData,String columnName){
        //有可能是增加
        for (CanalEntry.Column column : rowData.getAfterColumnsList()) {
            if(column.getName().equals(columnName)){
                return column.getValue();
            }
        }

        //有可能是删除操作
        for (CanalEntry.Column column : rowData.getBeforeColumnsList()) {
            if(column.getName().equals(columnName)){
                return column.getValue();
            }
        }
        return null;
    }


    /**
     * 用来监听tb_spu商品的变化
     * @param eventType
     * @param rowData
     */
    @ListenPoint(destination = "example",   //canal的名称
            schema = "changgou_goods",      //数据库名
            table = {"tb_spu"},             //表名
            eventType = {CanalEntry.EventType.UPDATE, CanalEntry.EventType.DELETE}//监听的类型
    )
    public void onEventCustomSpu(CanalEntry.EventType eventType, CanalEntry.RowData rowData){
        //获取那个商品变化的id
        int number = eventType.getNumber();
        String id = getColumn(rowData,"id");
        //新建一个变化后的消息队列
        Message message = new Message(number, id, TopicQueue.TOPIC_QUEUE_SPU, TopicQueue.TOPIC_EXCHANGE_SPU);
        //发送消息
        topicMessageSender.sendMessage(message);
    }

}


