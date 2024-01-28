package io.github.hylexus.jt.jt808.spec.builtin.msg.extension.location;

import io.github.hylexus.jt.jt808.support.annotation.msg.req.RequestFieldAlias;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 苏标-表-4-17 驾驶状态监测系统报警信息数据格式
 *
 * @author hylexus
 */
@Data
public class BuiltinMsg65Alias {

    // offset[0,4) 报警 ID: 按照报警先后，从0开始循环累加，不区分报警类型。
    @RequestFieldAlias.Dword(order = 10)
    private long alarmId;

    // 0x00：不可用
    // 0x01：开始标志
    // 0x02：结束标志
    // 该字段仅适用于有开始和结束标志类型的报警或事件，报警类型或事件类型无开始和结束标志，则该位不可用，填入0x00即可
    @RequestFieldAlias.Byte(order = 20)
    private short status;

    // offset[5,6) 报警/事件类型
    @RequestFieldAlias.Byte(order = 30)
    private short alarmType;

    // offset[6,7) 报警级别
    @RequestFieldAlias.Byte(order = 40)
    private short alarmLevel;

    // offset[7,8) 疲劳程度 范围1~10。数值越大表示疲劳程度越严重，仅在报警类型为0x01时有效
    @RequestFieldAlias.Byte(order = 50)
    private short fatigueLevel;

    // offset[8,12) BYTE[4] 预留
    @RequestFieldAlias.Bytes(order = 60, length = 4)
    private byte[] reservedOffset8;

    // offset[12,13) BYTE 车速
    @RequestFieldAlias.Byte(order = 100)
    private short speed;

    // offset[13,15) WORD 高程
    @RequestFieldAlias.Word(order = 110)
    private int height;

    // offset[15,19) DWORD 纬度
    @RequestFieldAlias.Dword(order = 120)
    private long latitude;

    // offset[19,23) DWORD 经度
    @RequestFieldAlias.Dword(order = 130)
    private long longitude;

    // offset[23,29) BCD[6] 日期时间
    @RequestFieldAlias.BcdDateTime(order = 140)
    private LocalDateTime datetime;

    // offset[29,31] WORD 车辆状态
    @RequestFieldAlias.Word(order = 150)
    private int vehicleStatus;
    // offset[31,31+16) BYTE[16] 报警标识号
    // 报警识别号定义见表4-16
    @RequestFieldAlias.Object(order = 160, length = 16)
    private AlarmIdentifierAlias alarmIdentifier;
}
