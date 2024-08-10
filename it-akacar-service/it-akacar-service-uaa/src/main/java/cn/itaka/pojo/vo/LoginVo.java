package cn.itaka.pojo.vo;

import cn.itaka.pojo.domain.Permission;
import cn.itaka.pojo.dto.LoginInfoDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginVo {
    private String tokenName;

    private String tokenValue;

    private List<String> permissions;

    private LoginInfoDto loginInfo;
}
