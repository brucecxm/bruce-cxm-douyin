package com.bruce.service;
import com.bruce.entity.ScheduleES;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ScheduleESRepository extends ElasticsearchRepository<ScheduleES, Long> {
}
