package cn.itaka.pojo.domain;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;


/**
 * <p>
 * 司机实名资料
 * </p>
 *
 * @author xp
 * @since 2024-07-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_driver_auth_material")
@Schema(name = "DriverAuthMaterial对象", description = "司机实名资料")
public class DriverAuthMaterial implements Serializable {

    private static final long serialVersionUID=1L;

    @Schema(name = "id", description = "同DriverID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(name = "name", description = "姓名")
    @TableField("name")
    private String name;

    @Schema(name = "gender", description = "女,男")
    @TableField("gender")
    private String gender;

    @Schema(name = "idNumber", description = "身份证号")
    @TableField("id_number")
    private String idNumber;

    @Schema(name = "birthday", description = "生日")
    @TableField("birthday")
    @JsonFormat(pattern = "YYYY-MM-dd", timezone = "GTM+8")
    private Date birthday;

    @Schema(name = "realAuthStatus", description = "认证状态: 0-审核中 1-审核成功 2-审核失败 3-认证撤销")
    @TableField("real_auth_status")
    private Integer realAuthStatus;

    @Schema(name = "idcardAddress", description = "身份证地址")
    @TableField("idcard_address")
    private String idcardAddress;

    @Schema(name = "idcardExpire", description = "身份证过期时间")
    @TableField("idcard_expire")
    @JsonFormat(pattern = "YYYY-MM-dd", timezone = "GTM+8")
    private Date idcardExpire;

    @Schema(name = "idcardFront", description = "身份证正面")
    @TableField("idcard_front")
    private String idcardFront;

    @Schema(name = "idcardBack", description = "身份证背面")
    @TableField("idcard_back")
    private String idcardBack;

    @Schema(name = "idcardHolding", description = "手持身份证")
    @TableField("idcard_holding")
    private String idcardHolding;

    @Schema(name = "carClass", description = "驾驶证类型")
    @TableField("car_class")
    private String carClass;

    @Schema(name = "drcardExpire", description = "驾驶证过期")
    @TableField("drcard_expire")
    @JsonFormat(pattern = "YYYY-MM-dd", timezone = "GTM+8")
    private Date drcardExpire;

    @Schema(name = "drcardIssueDate", description = "驾驶证领取日期")
    @TableField("drcard_issue_date")
    @JsonFormat(pattern = "YYYY-MM-dd", timezone = "GTM+8")
    private Date drcardIssueDate;

    @Schema(name = "drcardFront", description = "驾驶证正面")
    @TableField("drcard_front")
    private String drcardFront;

    @Schema(name = "drcardBack", description = "驾驶证背面")
    @TableField("drcard_back")
    private String drcardBack;

    @Schema(name = "drcardHolding", description = "手持驾驶证")
    @TableField("drcard_holding")
    private String drcardHolding;

    @Schema(name = "createTime", description = "创建时间")
    @TableField("create_time")
    @JsonFormat(pattern = "YYYY-MM-dd", timezone = "GTM+8")
    private Date createTime;

    @Schema(name = "updateTime", description = "修改时间")
    @TableField("update_time")
    @JsonFormat(pattern = "YYYY-MM-dd", timezone = "GTM+8")
    private Date updateTime;

    @Schema(name = "deleted", description = "逻辑删除")
    @TableField("deleted")
    @TableLogic
    private Boolean deleted;

    @Schema(name = "version", description = "乐观锁")
    @TableField("version")
    @Version
    private Integer version;

    @Schema(name = "auditTime", description = "审核时间")
    @TableField("audit_time")
    @JsonFormat(pattern = "YYYY-MM-dd", timezone = "GTM+8")
    private Date auditTime;

    @Schema(name = "auditUserId", description = "审核人")
    @TableField("audit_user_id")
    private Long auditUserId;

    @Schema(name = "auditRemark", description = "审核备注")
    @TableField("audit_remark")
    private String auditRemark;

    @Schema(name = "phone", description = "电话")
    @TableField("phone")
    private String phone;

    @Schema(name = "email", description = "邮箱")
    @TableField("email")
    private String email;

    @Schema(name = "mailAddress", description = "收信地址")
    @TableField("mail_address")
    private String mailAddress;

    @Schema(name = "contactName", description = "应急联系人")
    @TableField("contact_name")
    private String contactName;

    @Schema(name = "contactPhone", description = "应急联系人电话")
    @TableField("contact_phone")
    private String contactPhone;

    @Schema(name = "driverId", description = "司机ID")
    @TableField("driver_id")
    private Long driverId;

    /**
     * 获取司机驾龄
     * @return
     */
    public Long getYear() { // 前端胡须表达式的row.year可以自动调此方法
        return DateUtil.betweenYear(this.drcardIssueDate, new Date(), false);
    }

}
