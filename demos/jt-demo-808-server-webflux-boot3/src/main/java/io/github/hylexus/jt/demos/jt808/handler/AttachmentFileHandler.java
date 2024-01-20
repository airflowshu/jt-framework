package io.github.hylexus.jt.demos.jt808.handler;

import io.github.hylexus.jt.jt808.Jt808ProtocolVersion;
import io.github.hylexus.jt.jt808.spec.Jt808MsgBuilder;
import io.github.hylexus.jt.jt808.spec.Jt808Request;
import io.github.hylexus.jt.jt808.spec.builtin.msg.req.BuiltinMsg1210Alias;
import io.github.hylexus.jt.jt808.spec.builtin.msg.req.BuiltinMsg1211Alias;
import io.github.hylexus.jt.jt808.spec.builtin.msg.req.BuiltinMsg1212Alias;
import io.github.hylexus.jt.jt808.spec.builtin.msg.req.BuiltinMsg30316364Alias;
import io.github.hylexus.jt.jt808.spec.builtin.msg.resp.BuiltinMsg9212Alias;
import io.github.hylexus.jt.jt808.spec.builtin.msg.resp.BuiltinServerCommonReplyMsg;
import io.github.hylexus.jt.jt808.spec.session.Jt808Session;
import io.github.hylexus.jt.jt808.support.annotation.handler.Jt808RequestHandler;
import io.github.hylexus.jt.jt808.support.annotation.handler.Jt808RequestHandlerMapping;
import io.github.hylexus.jt.jt808.support.extension.attachment.AttachmentJt808SessionManager;
import io.netty.buffer.ByteBuf;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Jt808RequestHandler
public class AttachmentFileHandler {
    // !!!如果需要用 session 的话，附件相关的几个 808 指令对应的 session 应该从 AttachmentJt808SessionManager 中获取
    // !!!而不是从普通的 Jt808SessionManager 中获取
    // !!!因为附件上传和普通指令是不同的 TCP 连接
    private final AttachmentJt808SessionManager sessionManager;

    public AttachmentFileHandler(AttachmentJt808SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Jt808RequestHandlerMapping(msgType = 0x1210, versions = Jt808ProtocolVersion.AUTO_DETECTION)
    public BuiltinServerCommonReplyMsg processMsg0x1210(Jt808Request request, BuiltinMsg1210Alias body) {
        log.info("0x1210 ==> {}", body);
        warnLogIfNecessary(request, "0x1210 不应该由指令服务器对应的端口处理");
        return BuiltinServerCommonReplyMsg.success(request.header().msgId(), request.flowId());
    }

    @Jt808RequestHandlerMapping(msgType = 0x1211, versions = Jt808ProtocolVersion.AUTO_DETECTION)
    public BuiltinServerCommonReplyMsg processMsg0x1211(Jt808Request request, BuiltinMsg1211Alias body) {
        log.info("0x1211 ==> {}", body);
        warnLogIfNecessary(request, "0x1211 不应该由指令服务器对应的端口处理");
        return BuiltinServerCommonReplyMsg.success(request.header().msgId(), request.flowId());
    }

    @Jt808RequestHandlerMapping(msgType = 0x1212, versions = Jt808ProtocolVersion.AUTO_DETECTION)
    public BuiltinMsg9212Alias processMsg0x1212(Jt808Request request, BuiltinMsg1212Alias reqBody) {
        log.info("0x1211 ==> {}", reqBody);
        warnLogIfNecessary(request, "0x1212 不应该由指令服务器对应的端口处理");

        final BuiltinMsg9212Alias resp = new BuiltinMsg9212Alias();
        resp.setFileNameLength(reqBody.getFileNameLength());
        resp.setFileName(reqBody.getFileName());
        resp.setFileType(reqBody.getFileType());
        // 0x00：完成
        // 0x01：需要补传
        resp.setUploadResult((byte) 0x00);
        resp.setPackageCountToReTransmit((short) 0);
        resp.setRetransmitItemList(null);

        return resp;
    }

    @Jt808RequestHandlerMapping(msgType = 0x30316364)
    public void processMsg30316364(Jt808Request request, BuiltinMsg30316364Alias body, @Nullable Jt808Session session) {
        if (request.session() == null) {
            log.warn("session == null, 附件上传之前没有没有发送 0x1210,0x1211 消息???");
        }
        log.info("0x30316364 ==> session:{}, msg:{}", session, body);
    }

    private void warnLogIfNecessary(Jt808Request request, String msg) {
        if (request.session() == null) {
            return;
        }
        if (sessionManager.findByTerminalId(request.terminalId()).orElseThrow() != request.session()) {
            log.error("session invalid");
        }
        if (request.session().role() == Jt808Session.Role.INSTRUCTION) {
            log.warn(msg);
        }
    }

    public void sendMsg9212(String terminalId, BuiltinMsg9212Alias body) {
        final Jt808Session session = this.sessionManager.findByTerminalId(terminalId).orElseThrow();

        final ByteBuf byteBuf = Jt808MsgBuilder.newEntityBuilder(session)
                .version(session.protocolVersion())
                .terminalId(terminalId)
                .body(body)
                .build();
        session.sendMsgToClient(byteBuf);
    }
}
