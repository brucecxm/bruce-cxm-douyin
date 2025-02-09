package com.bruce.entity;

import lombok.Data;
import nonapi.io.github.classgraph.json.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Document(indexName = "schedule")
public class ScheduleES {
    @Id
    private Integer id;

    private String name;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
