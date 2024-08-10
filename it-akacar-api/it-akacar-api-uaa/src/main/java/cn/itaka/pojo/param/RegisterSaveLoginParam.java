package cn.itaka.pojo.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterSaveLoginParam {

    private String username;
    private String password;
    private Integer type;
    private Boolean enabled;
    private String avatar;
    private Boolean admin;
    private String nickName;
    private String openId;
    private String name;
    private String phone;
    private Long id;
}
