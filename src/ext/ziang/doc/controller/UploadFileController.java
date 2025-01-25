package ext.ziang.doc.controller;

import ext.common.thread.SessionThreadPoolExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import wt.method.MethodContext;
import wt.method.jmx.MethodServer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/doc")
public class UploadFileController {

    public static final Logger logger = LoggerFactory.getLogger(SessionThreadPoolExecutor.class);

    /**
     * 上传文件
     * 
     * @param request 请求
     * @return
     * @throws IllegalStateException
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value = "uploadFile", method = RequestMethod.POST)
    public String springUpload(HttpServletRequest request) throws IllegalStateException, IOException {
        long startTime = System.currentTimeMillis();
        // 将当前上下文初始化给 CommonsMultipartResolver （多部分解析器）
        CommonsMultipartResolver multipartResolver =
            new CommonsMultipartResolver(request.getSession().getServletContext());
        // 检查form中是否有enctype="multipart/form-data"
        if (multipartResolver.isMultipart(request)) {
            // 将request变成文件request
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;
            // 获取multiRequest 中所有的文件 以及 流
            MultiValueMap<String, MultipartFile> multiFileMap = multiRequest.getMultiFileMap();
            for (Map.Entry<String, List<MultipartFile>> entry : multiFileMap.entrySet()) {
                List<MultipartFile> multipartFiles = entry.getValue();
                for (MultipartFile multipartFile : multipartFiles) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("multipartFile = " + multipartFile.getOriginalFilename());
                        logger.debug("multipartFile = " + multipartFile.getInputStream().available());
                    }
                }
            }
        }
        long endTime = System.currentTimeMillis();
        logger.info("方法三的运行时间：" + String.valueOf(endTime - startTime) + "ms");
        return "上传完成";
    }
}
