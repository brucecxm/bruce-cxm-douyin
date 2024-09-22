package com.bruce.controller;


import com.bruce.annotation.RateLimit;
import com.bruce.config.RabbitMQConfig;
import com.bruce.filter.GlobalFilterooo;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/introduce")
public class introduceController {

    @Autowired
private GlobalFilterooo globalFilterooo;

    @GetMapping("/limited")
    @RateLimit(value = 10, period = 60)
    public String limitedApi() {
        globalFilterooo.testFilter();
        return "API 访问成功";
    }



    @Autowired
    private RabbitTemplate rabbitTemplate;
    @GetMapping("/yanshi")
    public void sendDelayedMessage(String message) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.NORMAL_EXCHANGE, "normal", message);

        System.out.println("Delayed message sent: " + message);
    }



//    前端会传来用户的id
//    然后根据用户的id查找出用户最喜欢看的类型 就是class字段里面哪个标签最多
    //然后根据这个标签
//    到视频库表里面随机查找五个值

@RequestMapping("/test")
    public String[] test(){
        System.out.println("coming");

        String[] VideoArr= {"https://www.douyin.com/aweme/v1/play/?video_id=v0d00fg10000cj1lq4jc77u0ng6s1gt0&line=0&file_id=bed51c00899b458cbc5d8280147c22a1&sign=7749aec7bd62a3760065f60e40fc1867&is_play_url=1&source=PackSourceEnum_PUBLISH",
                "https://www.douyin.com/aweme/v1/play/?video_id=v0d00fg10000cj1lq4jc77u0ng6s1gt0&line=0&file_id=bed51c00899b458cbc5d8280147c22a1&sign=7749aec7bd62a3760065f60e40fc1867&is_play_url=1&source=PackSourceEnum_PUBLISH",
                "https://www.douyin.com/aweme/v1/play/?video_id=v0d00fg10000cj1lq4jc77u0ng6s1gt0&line=0&file_id=bed51c00899b458cbc5d8280147c22a1&sign=7749aec7bd62a3760065f60e40fc1867&is_play_url=1&source=PackSourceEnum_PUBLISH",
                "https://www.douyin.com/aweme/v1/play/?video_id=v0200fg10000cnm4kq3c77ubn9efls50&line=0&file_id=57f15d4da3274950aec2af12bbe0514c&sign=23f56dac8fb96a21c4bdc2b4e3bedb62&is_play_url=1&source=PackSourceEnum_PUBLISH",
                "https://www.douyin.com/aweme/v1/play/?video_id=v0200f2c0000bj63fuv3cp5a1sbmujc0&line=0&file_id=fad24ab3e1ab4efa90440244d5268bd9&sign=f33a08757b00e73f6a75ab6a3c5d751b&is_play_url=1&source=PackSourceEnum_PUBLISH",
        "https://www.douyin.com/aweme/v1/play/?video_id=v0200f9e0000bldvt42gd9fqp49ecpag&line=0&file_id=f87d7ac5d5a24b19b3f33f1b31ba02e7&sign=c872722944d05b0ccd56ac9f5fb8a51b&is_play_url=1&source=PackSourceEnum_PUBLISH",
        "https://www.douyin.com/aweme/v1/play/?video_id=v0300fg10000choacujc77ufs55f0oq0&line=0&file_id=7b9fdf3117bc4ab49b58c956a70bd2e7&sign=e116a1e5b4269edca98db07a22142fbd&is_play_url=1&source=PackSourceEnum_PUBLISH",
        "https://www.douyin.com/aweme/v1/play/?video_id=v0300fg10000choacujc77ufs55f0oq0&line=0&file_id=7b9fdf3117bc4ab49b58c956a70bd2e7&sign=e116a1e5b4269edca98db07a22142fbd&is_play_url=1&source=PackSourceEnum_PUBLISH",
        "https://www.douyin.com/aweme/v1/play/?video_id=v0200fg10000cjdk9q3c77ud1oonqoqg&line=0&file_id=b636323c468d42df8b21695bb124d9ee&sign=5786da50100031a108d24a9ef596de33&is_play_url=1&source=PackSourceEnum_PUBLISH"};

        return VideoArr;
    }


    @RequestMapping("/testmore")
    public String[] testmore(){
        System.out.println("comingmore");

        String[] VideoArr= {"https://www.douyin.com/aweme/v1/play/?video_id=v0d00fg10000cj1lq4jc77u0ng6s1gt0&line=0&file_id=bed51c00899b458cbc5d8280147c22a1&sign=7749aec7bd62a3760065f60e40fc1867&is_play_url=1&source=PackSourceEnum_PUBLISH",
                "https://www.douyin.com/aweme/v1/play/?video_id=v0d00fg10000cj1lq4jc77u0ng6s1gt0&line=0&file_id=bed51c00899b458cbc5d8280147c22a1&sign=7749aec7bd62a3760065f60e40fc1867&is_play_url=1&source=PackSourceEnum_PUBLISH",
                "https://www.douyin.com/aweme/v1/play/?video_id=v0d00fg10000cj1lq4jc77u0ng6s1gt0&line=0&file_id=bed51c00899b458cbc5d8280147c22a1&sign=7749aec7bd62a3760065f60e40fc1867&is_play_url=1&source=PackSourceEnum_PUBLISH",
                "https://www.douyin.com/aweme/v1/play/?video_id=v0d00fg10000cj1lq4jc77u0ng6s1gt0&line=0&file_id=bed51c00899b458cbc5d8280147c22a1&sign=7749aec7bd62a3760065f60e40fc1867&is_play_url=1&source=PackSourceEnum_PUBLISH",
                "https://www.douyin.com/aweme/v1/play/?video_id=v0200f2c0000bj63fuv3cp5a1sbmujc0&line=0&file_id=fad24ab3e1ab4efa90440244d5268bd9&sign=f33a08757b00e73f6a75ab6a3c5d751b&is_play_url=1&source=PackSourceEnum_PUBLISH",
        "https://www.douyin.com/aweme/v1/play/?video_id=v0200fg10000cjis5nbc77u6pe2n4s20&line=0&file_id=90751c32a5f34edd98bfd1f4a4b87556&sign=61b2b338c6921f78ad9cc287db2dea4e&is_play_url=1&source=PackSourceEnum_PUBLISH",
        "https://www.douyin.com/aweme/v1/play/?video_id=v0200fg10000cjis5nbc77u6pe2n4s20&line=0&file_id=90751c32a5f34edd98bfd1f4a4b87556&sign=61b2b338c6921f78ad9cc287db2dea4e&is_play_url=1&source=PackSourceEnum_PUBLISH"};

        return VideoArr;
    }

    @RequestMapping("/testcontent")
    public String[] testcontent(){
        System.out.println("comingcontent");

        String[] VideoArr= {"https://www.dg6s1gt0&line=0&file_id=bed51c00899b458cbc5d8280147c22a1&sign=7749aec7bd62a3760065f60e40fc1867&is_play_url=1&source=PackSourceEnum_PUBLISH",
                "https://www.douyin.com/aweme/v1/play/?video_id=v0d00fg10000cj1lq4jc77u0ng6s1gt0&line=0&file_id=bed51c00899b458cbc5d8280147c22a1&sign=7749aec7bd62a3760065f60e40fc1867&is_play_url=1&source=PackSourceEnum_PUBLISH",
       "https://www.douyin.com/aweme/v1/play/?video_id=v0200fg10000cjdk9q3c77ud1oonqoqg&line=0&file_id=b636323c468d42df8b21695bb124d9ee&sign=5786da50100031a108d24a9ef596de33&is_play_url=1&source=PackSourceEnum_PUBLISH",
       "https://www.douyin.com/aweme/v1/play/?video_id=v0200fg10000cjdk9q3c77ud1oonqoqg&line=0&file_id=b636323c468d42df8b21695bb124d9ee&sign=5786da50100031a108d24a9ef596de33&is_play_url=1&source=PackSourceEnum_PUBLISH",
        "https://www.douyin.com/aweme/v1/play/?video_id=v0200fa50000bqv2ovedm15352jvv5vg&line=0&file_id=efac24de9d2548228975fc8429e5bdcb&sign=3b7c4acc3b831e92448d6909510074c0&is_play_url=1&source=PackSourceEnum_PUBLISH",
        "https://www.douyin.com/aweme/v1/play/?video_id=v0200fa50000bqv2ovedm15352jvv5vg&line=0&file_id=efac24de9d2548228975fc8429e5bdcb&sign=3b7c4acc3b831e92448d6909510074c0&is_play_url=1&source=PackSourceEnum_PUBLISH"};

        return VideoArr;
    }

    @GetMapping("/hong")
    public void testcontentone(){
        System.out.println("comingcontent");


    }

}
