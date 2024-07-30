package cn.itaka.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CodeToOpenIdDto {
    private String sessionKey;
    private String unionid;
    private String errmsg;
    private String openid;
    private Integer errcode;
}
