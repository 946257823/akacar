package cn.itaka.pojo.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonFormat;


/**
 * <p>
 * 司机对象
 * </p>
 *
 * @author xp
 * @since 2024-07-28
 */
@Data
@EqualsAndHashCode(callSuper = false) // 给当前类提供equals和hashcode
@Accessors(chain = true)
@TableName("t_driver") // 对应表名
@Schema(name = "Driver对象", description = "司机对象") // swagger注解，描述当前domain
public class Driver implements Serializable {

    private static final long serialVersionUID=1L;
    // swagger注解，描述当前字段
    @Schema(name = "id", description = "同loginId")
    // 防止前端接收null类型数据时，导致数据失真
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    // mybatis-plus注解，value：当前字段名称与数据库名称 type：id的自增类型
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(name = "phone", description = "电话")
    @TableField("phone")
    private String phone;

    @Schema(name = "homeAddress", description = "居住地址")
    @TableField("home_address")
    private String homeAddress;

    @Schema(name = "homeAddressLongitude", description = "居住地址精度")
    @TableField("home_address_longitude")
    private String homeAddressLongitude;

    @Schema(name = "homeAddressLatitude", description = "居住地址维度")
    @TableField("home_address_latitude")
    private String homeAddressLatitude;

    @Schema(name = "archive", description = "面部数据ID")
    @TableField("archive")
    private Boolean archive;

    @Schema(name = "createTime", description = "创建时间")
    @TableField("create_time")
    private Date createTime;

    @Schema(name = "updateTime", description = "修改时间")
    @TableField("update_time")
    private Date updateTime;

    @Schema(name = "deleted", description = "逻辑删除")
    @TableField("deleted")
    @TableLogic // 标识当前字段是逻辑删除字段
    private Boolean deleted;

    @Schema(name = "version", description = "乐观锁")
    @TableField("version")
    @Version // 标识当前字段是乐观锁字段
    private Integer version;

    @Schema(name = "openId", description = "微信ID")
    @TableField("open_id")
    private String openId;

    @Schema(name = "bitState", description = "位状态")
    @TableField("bit_state")
    private Long bitState;

}
