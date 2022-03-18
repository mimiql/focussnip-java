package org.oss.focussnip.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.oss.focussnip.common.BaseResponse;
import org.oss.focussnip.constant.OssConstant;
import org.oss.focussnip.service.OssService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@RestController
public class OssController {
    @Autowired
    private OssService ossService;
    @Autowired
    private OssConstant ossConstant;

    Logger logger = LoggerFactory.getLogger(OssController.class);

    @PostMapping("/files")
    public BaseResponse<List<String>> uploadFiles(@RequestParam("files")List<MultipartFile> files) throws IOException {
        List<String> resp = new ArrayList<>();
        for(MultipartFile file : files){
            try {
                if(file != null){
                    String filename = file.getOriginalFilename();
                    if (StringUtils.isNotBlank(filename)){
                        String uploadUrl = ossConstant.getPREFIX() + ossService.uploadFile(file);
                        resp.add(uploadUrl);
                    }
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
        ObjectMapper mapper = new ObjectMapper();
        logger.debug(mapper.writeValueAsString(resp));
        return BaseResponse.getSuccessResponse(resp);
    }
}
