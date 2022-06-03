package com.pilipili.app.api;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.pilipili.app.domain.JsonResponse;
import com.pilipili.app.domain.Video;
import com.pilipili.app.service.DemoService;
import com.pilipili.app.service.ElasticSearchService;
import com.pilipili.app.service.feign.MsDeclareService;
import com.pilipili.app.service.util.FastDFSUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
public class DemoApi {
    @Autowired
    private DemoService demoService;
    
    @Autowired
    private FastDFSUtil fastDFSUtil;

    @Autowired
    private ElasticSearchService elasticSearchService;

    @Autowired
    private MsDeclareService msDeclareService;

    @GetMapping("/query")
    public Long query(Long id){
        return demoService.query(id);
    }

    @GetMapping("/slices")
    public void slices(MultipartFile file) throws Exception{
        fastDFSUtil.convertFileToSlices(file);
    }

    @GetMapping("/es-videos")
    public JsonResponse<Video> getEsVideos(@RequestParam String keyword){
        Video video = elasticSearchService.getVideos(keyword);
        return new JsonResponse<>(video);
    }

    @GetMapping("/demos")
    public Long msget(@RequestParam Long id){
        return msDeclareService.msget(id);
    }

    @PostMapping("/demos")
    public Map<String, Object> mspost(@RequestBody Map<String, Object> params){
        return msDeclareService.mspost(params);
    }

    //使用断路器
    @HystrixCommand(fallbackMethod = "error",
            commandProperties = {
                    //超时时间
                    @HystrixProperty(
                            name = "execution.isolation.thread.timeoutInMilliseconds",
                            value = "2000"
                    )
            }
    )
    @GetMapping("/timeout")
    public String circuitBreakerWithHystrix(@RequestParam Long time){
        return msDeclareService.timeout(time);
    }

    //断路器的降级方法
    public String error(Long time){
        return "超时出错！";
    }

}
