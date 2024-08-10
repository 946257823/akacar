package cn.itaka.template;
import cn.hutool.core.lang.Assert;
import cn.itaka.properties.CosSetingProperties;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.model.ciModel.auditing.ImageAuditingRequest;
import com.qcloud.cos.model.ciModel.auditing.ImageAuditingResponse;

import java.io.InputStream;


public class CosTemplate {

    private CosSetingProperties cosSetingProperties;
    private COSClient cosClient;

    public CosTemplate(CosSetingProperties cosSetingProperties, COSClient cosClient) {
        this.cosSetingProperties = cosSetingProperties;
        this.cosClient = cosClient;
    }


    /**
     * 图片上传
     * @param inputStream
     * @param floder
     * @param imageName
     * @return
     */
    public String uploadFile(InputStream inputStream, String floder, String imageName) {

        String bucketName = cosSetingProperties.getBucketName();
// 指定文件上传到 COS 上的路径，即对象键。例如对象键为 folder/picture.jpg，则表示将文件 picture.jpg 上传到 folder 路径下
        String key = floder + "/" + imageName;
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, inputStream, new ObjectMetadata());
        PutObjectResult putObjectResult = this.cosClient.putObject(putObjectRequest);

        String etag = putObjectResult.getETag();
        if ("null".equals(etag) || null == etag) {
            throw new RuntimeException("上传图片失败");
        }

        Boolean aBoolean = this.imageAuditing(bucketName, key);
        Assert.isTrue(aBoolean, "图片审核失败");

        String filePath = String.format(cosSetingProperties.getImagePathTemplate(), bucketName, cosSetingProperties.getRegion(), imageName);
        return  filePath;
    }

    /**
     * 图片审核
     * @param bucketName
     * @param objctKey
     * @return
     */
    public Boolean imageAuditing(String bucketName, String objctKey) {
        //1.创建任务请求对象
        ImageAuditingRequest request = new ImageAuditingRequest();
        //2.添加请求参数 参数详情请见api接口文档
        //2.1设置请求bucket
        request.setBucketName(bucketName);
        //2.2设置审核类型
        request.setDetectType(cosSetingProperties.getDetectType());
        //2.3设置bucket中的图片位置
        request.setObjectKey(objctKey);
        //3.调用接口,获取任务响应对象
        ImageAuditingResponse response = this.cosClient.imageAuditing(request);
        return "0".equals(response.getResult());
    }
}
