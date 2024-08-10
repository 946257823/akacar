package cn.itaka.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverDaySummaryDto {

    private Integer driverDuration;
    private Integer todayIncome;
    private Integer todayTradeOrders;


}
