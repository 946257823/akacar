package cn.itaka.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverRealAuthAuditDto {

    private Long id;
    private String remark;
    private Boolean approve;
}
