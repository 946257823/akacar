package cn.itaka.config;

import cn.dev33.satoken.stp.StpInterface;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import cn.itaka.properties.SaTokenSettingProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义权限加载接口实现类
 */

public class StpInterfaceImpl implements StpInterface{

    private RedisTemplate redisTemplate;
    private SaTokenSettingProperties saTokenSettingProperties;
    public StpInterfaceImpl(RedisTemplate redisTemplate, SaTokenSettingProperties saTokenSettingProperties) {
        this.redisTemplate = redisTemplate;
        this.saTokenSettingProperties = saTokenSettingProperties;
    }
    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String s) {
        // 1.从配置读取到key  permission-key:%s
        String key = String.format(saTokenSettingProperties.getPermissionKey(), loginId);
        // 2.从redis中读权限
        Object permission = redisTemplate.opsForValue().get(key);
        // 3. 把权限转为集合
        List<String> permssionList = JSONUtil.toList(JSONUtil.parseArray(permission), String.class);
        return permssionList;

    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        // 本 list 仅做模拟，实际项目中要根据具体业务逻辑来查询角色
        List<String> list = new ArrayList<String>();
        list.add("admin");
        list.add("super-admin");
        return list;
    }
}
