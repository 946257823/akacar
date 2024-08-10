package cn.itaka.controller.manager;

import cn.itaka.result.R;
import cn.itaka.template.CosTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.Name;
import java.io.IOException;
import java.io.InputStream;

@Tag(name = "Cos文件上传",description = "Cos文件上传控制器类")
@RestController
@RequestMapping("/cos")
public class CosContoller {

    @Autowired
    private CosTemplate cosTemplate;

    @Operation( summary= "文件上传",description = "文件上传接口")
    @Parameter(name = "file",description = "文件对象",required = true)
    @PostMapping("/uploadFile/{floder}")
    public R<String> uploadFile(@PathVariable("floder") String floder, MultipartFile file) {
        try(    // try-with-resources: 使用 try-with-resources 语法可以自动关闭 InputStream，避免资源泄漏问题。
                // 1.获取文件本身
                InputStream inputStream = file.getInputStream()
        ) {
            // 2.文件名
            String fileName = System.currentTimeMillis() + file.getOriginalFilename();
            String filePath = cosTemplate.uploadFile(inputStream, floder, fileName);
            return R.success(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            return R.error("文件上传失败");
        }
    }
}
