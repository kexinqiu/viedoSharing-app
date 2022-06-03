package com.pilipili.app.dao.repository;

import com.pilipili.app.domain.Video;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


//内置了增删改查的基础操作
public interface VideoRepository extends ElasticsearchRepository<Video, Long> {

    //spring data会将其进行拆分 find by， title（字段）， like模糊查询
    Video findByTitleLike(String keyword);



}
