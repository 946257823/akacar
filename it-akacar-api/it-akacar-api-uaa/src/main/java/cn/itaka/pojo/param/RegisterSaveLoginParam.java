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
    private String nick_name;
    private String open_id;
    private String name;
    private String phone;
}
