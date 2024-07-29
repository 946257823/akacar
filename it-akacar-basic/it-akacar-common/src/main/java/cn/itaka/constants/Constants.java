package cn.itaka.constants;


import java.util.Arrays;
import java.util.List;

//常量维护
public class Constants {

    //手机号正则表达式
    public static final String CHINA_PHONE_REGEX =  "^((13[0-9])|(14[0,1,4-9])|(15[0-3,5-9])|(16[2,5,6,7])|(17[0-8])|(18[0-9])|(19[0-3,5-9]))\\d{8}$";

    //编码
    public static final String UTF_8 = "UTF-8";

    public static final Integer SEX_WOMAN = 0;
    public static final Integer SEX_MAN = 1;

    public class RealAuth {
        // 等级0青铜,1白银，2黄金,3白金,4大师,5宗师,6王者
        public static final Integer VERIFYING = 0;
        public static final Integer APPROVED = 1;
        public static final Integer VERIFY_FAIL = 2;
        public static final Integer REVOKE = 3;
    }

    public class Level {
        // 等级0青铜,1白银，2黄金,3白金,4大师,5宗师,6王者
        public static final String LEVEL_BRONZE = "0";
        public static final String LEVEL_SILVER = "1";
        public static final String LEVEL_GOLD = "2";
        public static final String LEVEL_PLATINUM = "3";
        public static final String LEVEL_MASTERS = "4";
        public static final String LEVEL_GRANDMASTERS = "5";
        public static final String LEVEL_KINGS = "6";
    }

    //登录模型
    public class Login {

        // 平台账户
        public static final int TYPE_ADMIN = 0;

        // 司机账户
        public static final int TYPE_DRIVER = 1;

        // 客户账户
        public static final int TYPE_CUSTOMER = 2;
    }

    //远程调用
    public class Remote {
        //服务名
        public static final String SERVICE_UAA = "it-akacar-service-uaa";

        public static final String PREFIX_LOGIN_REMOTE = "/login/remote";
    }

    public class Redis {
        // 司机上线标识
        public static final String DRIVER_ONLINE_KEY = "driver-online-%s";

        public static final String DRIVER_LOCATION_KEY = "driverLocationKey";

        public static final String ORDER_INFO_KEY = "order-info-key-%s";

        public static final String DRIVER_LOCATION_TO_REDIS_KEY = "driver-location-to-redis-key:%s";
    }

    public class Rule {
        public static final Integer ENABLE = 0;
        public static final Integer UN_ABLE = 0;
    }

    public class RocketMQ {
        public static final String DRIVER_PULL_ORDER_TOPIC = "driver-pull-order-topic:%s-tag";
        public static final String ORDER_TIMEOUT_CANCEL_TOPIC = "order-topic";
        public static final String ORDER_TIMEOUT_CANCEL_TAG = "timeout-cancel-tag";
    }

    public class Order {
        // 0.等待接单
        public static final Integer WAIT_ORDER = 0;
        // 1.已接单
        public static final Integer RECEIVED_ORDER = 1;
        // 2.司机已到达
        public static final Integer DRIVER_ARRIVED = 2;
        // 3.开始代驾
        public static final Integer START_DRIVING = 3;
        // 4.结束代驾
        public static final Integer END_DRIVING = 4;
        // 5.司机确认费用
        public static final Integer DRIVER_CONFIRMATION_FEE = 5;
        // 6.未付款
        public static final Integer UNPAID = 6;
        // 7.已付款
        public static final Integer PAID = 7;
        // 8.订单已结束
        public static final Integer ORDER_END = 8;
        // 9.客户撤单
        public static final Integer CUSTOMER_CANCEL = 9;
        // 10.司机撤单
        public static final Integer DRIVER_CALCEL = 10;
        // 11.事故关闭
        public static final Integer ACCIDENT_CLOSE = 11;
        // 12.其他
        public static final Integer OTHER = 12;

        // 乘客正在进行的订单状态
        public static final List<Integer> CUSTOMER_ORDER_IN_PROGRESS = Arrays.asList(
                WAIT_ORDER,RECEIVED_ORDER,DRIVER_ARRIVED,START_DRIVING,END_DRIVING,DRIVER_CONFIRMATION_FEE,UNPAID
        );

        // 司机正在进行的订单状态
        public static final List<Integer> DRIVER_ORDER_IN_PROGRESS = Arrays.asList(
                RECEIVED_ORDER,DRIVER_ARRIVED,START_DRIVING,END_DRIVING
        );

        // 可取消订单状态
        public static final List<Integer> ORDER_CANCELABLE = Arrays.asList(
                WAIT_ORDER,RECEIVED_ORDER,DRIVER_ARRIVED
        );
    }

}
