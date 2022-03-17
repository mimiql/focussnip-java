package org.oss.focussnip.api;

import org.oss.focussnip.common.BaseResponse;
import org.oss.focussnip.constant.OssConstant;
import org.oss.focussnip.service.OssService;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@RestController
public class OssContoller {
    @Autowired
    private OssService ossService;
    @Autowired
    private OssConstant ossConstant;

    @PostMapping("/files")
    public BaseResponse<String> uploadFiles(@RequestParam("file")List<MultipartFile> files){
        String resp = "";
        for(MultipartFile file : files){
            try {
                if(file != null){
                    String filename = file.getOriginalFilename();
                    if (!"".equals(filename.trim())){
                        String uploadUrl = ossConstant.getPREFIX() + ossService.uploadFile(file);
                        if(resp != null){
                            resp += uploadUrl;
                        }else resp = uploadUrl;
                    }
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
        return BaseResponse.getSuccessResponse(resp);

    }
}
