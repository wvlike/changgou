package com.ismyself.canal.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.ismyself.feign.ContentFeign;
import com.ismyself.pojo.Content;
import com.xpand.starter.canal.annotation.CanalEventListener;
import com.xpand.starter.canal.annotation.InsertListenPoint;
import com.xpand.starter.canal.annotation.ListenPoint;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;

/**
 * package com.ismyself.canal.queue;
 *
 * @auther txw
 * @create 2019-08-30  14:42
 * @description：
 */
@CanalEventListener
public class CanalListener {

    @Autowired
    private ContentFeign contentFeign;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @InsertListenPoint(table = "tb_content", schema = "changgou_content", destination = "example")
    public void onInsertListen(CanalEntry.EntryType entryType, CanalEntry.RowData rowData) {
        rowData.getAfterColumnsList().forEach(c -> System.out.println("自定义的增加:" + c.getName() + "----" + c.getValue()));
    }

    @ListenPoint(
            destination = "example",
            schema = "changgou_content",
            table = {"tb_content_category", "tb_content"},
            eventType = {CanalEntry.EventType.UPDATE}
    )
    public void onContentListen(CanalEntry.EntryType entryType, CanalEntry.RowData rowData) {
        for (CanalEntry.Column column : rowData.getBeforeColumnsList()) {
            System.out.println("自定义修改前：" + column.getName() + ":" + column.getValue());
        }
        rowData.getBeforeColumnsList().forEach(c -> System.out.println("自定义修改后" + c.getName() + ":" + c.getValue()));
    }


    /**
     * 对content数据库的content表表进行增改查监听
     */
    @ListenPoint(destination = "example",
            schema = {"changgou_content"},
            table = {"tb_content"},
            eventType = {CanalEntry.EventType.INSERT, CanalEntry.EventType.UPDATE, CanalEntry.EventType.DELETE})
    public void onContentListenCUD(CanalEntry.EntryType entryType, CanalEntry.RowData rowData) {

        //根据数据获取对应的行的id
        Long categoryId = Long.valueOf(getColumnId(rowData, "category_id"));
        //根据contentFeign获取所有category_id的数据
        Result<List<Content>> result = contentFeign.findByCategoryId(categoryId);
        List<Content> contentList = result.getData();
        //将数据同步到redis
        String key = "content_"+categoryId;
        stringRedisTemplate.boundValueOps(key).set(JSON.toJSONString(contentList));
    }

    /**
     * 根据columnName获取所有组的id
     * @param rowData
     * @param columnName
     * @return
     */
    private String getColumnId(CanalEntry.RowData rowData, String columnName) {

        String columnId = null;

        for (CanalEntry.Column column : rowData.getAfterColumnsList()) {
            if (column.getName().equals(columnName)) {
                columnId = column.getValue();
            }
        }
        for (CanalEntry.Column column : rowData.getBeforeColumnsList()) {
            if (column.getName().equals(columnName)) {
                columnId = column.getValue();
            }
        }
        return columnId;
    }

}