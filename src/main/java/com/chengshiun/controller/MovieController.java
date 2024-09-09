package com.chengshiun.controller;

import com.chengshiun.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MovieController {

    @Autowired
    private MemberService memberService;

    @GetMapping("/getMovies")
    public String getMovies() {
        return "取得電影列表";
    }

    @PostMapping("/watchFreeMovie")
    public String watchFreeMovie() {
        return "觀看免費電影";
    }

    @PostMapping("/watchVipMovie")
    public String watchVipMovie() {
        return "觀看VIP電影";
    }

    @PostMapping("/uploadMovie")
    public String uploadMovie() {
        return "上傳新電影";
    }

    @DeleteMapping("/deleteMovie")
    public String deleteMovie() {
        return "刪除電影";
    }
}
