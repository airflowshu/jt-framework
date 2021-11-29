package io.github.hylexus.jt.jt808.session;

import io.github.hylexus.jt.config.Jt808ProtocolVersion;
import io.github.hylexus.jt.exception.JtCommunicationException;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;

/**
 * Created At 2020-06-20 15:58
 *
 * @author hylexus
 */
public interface Jt808Session {

    int MAX_FLOW_ID = 0xffff;

    /**
     * @param bytes 待发送给客户端的数据
     * @throws InterruptedException     InterruptedException
     * @see #sendMsgToClient(ByteBuf)
     */
    default void sendMsgToClient(byte[] bytes) throws InterruptedException, JtCommunicationException {
        this.sendMsgToClient(Unpooled.wrappedBuffer(bytes));
    }

    /**
     * @param byteBuf 待发送给客户端的数据
     * @throws InterruptedException     InterruptedException
     * @throws JtCommunicationException JtCommunicationException
     */
    void sendMsgToClient(ByteBuf byteBuf) throws InterruptedException, JtCommunicationException;

    /**
     * @return 当前流水号，并自增
     * @see #getCurrentFlowId(boolean)
     */
    default int getCurrentFlowId() {
        return getCurrentFlowId(true);
    }

    /**
     * @param autoIncrement 是否自增
     * @return 当前流水号
     * @see #getCurrentFlowId()
     */
    int getCurrentFlowId(boolean autoIncrement);

    String getId();

    default String getSessionId() {
        return getId();
    }

    Channel getChannel();

    Jt808Session setChannel(Channel channel);

    String getTerminalId();

    /**
     * @return 当前终端的协议版本号
     * @author hylexus
     * @since 2.0.0
     */
    Jt808ProtocolVersion getProtocolVersion();

    /**
     * @return 上次通信时间
     */
    long getLastCommunicateTimestamp();

    Jt808Session setLastCommunicateTimestamp(long lastCommunicateTimestamp);

    String toString();
}