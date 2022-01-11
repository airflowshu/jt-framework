package io.github.hylexus.jt.jt808.support.codec;

import io.github.hylexus.jt.core.OrderedComponent;
import io.github.hylexus.jt.jt808.spec.Jt808SubPackageRequest;

/**
 * 分包消息到达服务端时候回调
 *
 * @author hylexus
 */
@FunctionalInterface
public interface Jt808RequestSubPackageEventListener extends OrderedComponent {

    /**
     * @param subPackageRequest 分包消息
     */
    void onSubPackage(Jt808SubPackageRequest subPackageRequest);

    Jt808RequestSubPackageEventListener NO_OPS_CONSUMER = subPackageRequest -> {
    };
}
