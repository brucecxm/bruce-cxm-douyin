package com.bruce.controller;

import com.bruce.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transfer")
public class TransferController {
@Autowired
    private  TransferService transferService;

    @PostMapping
    public ResponseEntity<?> transfer(@RequestParam Long fromUserId,
                                      @RequestParam Long toUserId,
                                      @RequestParam BigDecimal amount) {
        try {
            transferService.transfer(fromUserId, toUserId, amount);
            return ResponseEntity.ok("成功");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}