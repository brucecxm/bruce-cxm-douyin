package com.bruce.dto;

import lombok.Data;

@Data
public class HandleRequestDTO {
    private Long requestId;
    private String action; // "accept" or "reject"
}