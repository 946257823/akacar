package cn.itaka.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginInfoDto {
    private Long id;
    private String username;
    private Integer type;
    private String avatar;
    private String nickName;
    private String openId;
    private String name;
    private String phone;
    private Boolean admin;
}
