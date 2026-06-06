package com.shea.ioc.aiapplicationcreator.controller;

import com.shea.ioc.aiapplicationcreator.common.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 健康检查控制器
 * @author : Shea.
 * @since : 2026/6/6 14:21
 */
@RestController
@RequestMapping("/health")
public class HealthController {

    @GetMapping
    public Result<String> health() {
        return Result.success("OK");
    }
}
