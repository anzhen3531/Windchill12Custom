package ext.ziang.doc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/doc")
public class UploadFileController {

    @RequestMapping(method = RequestMethod.POST, path = "/uploadFile")
    @ResponseBody
    public void uploadFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.getWriter().println("上传完成");
    }
}
