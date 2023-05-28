package io.github.hylexus.jt.jt1078.spec;

import reactor.core.publisher.Flux;

import java.time.Duration;

/**
 * @author hylexus
 */
public interface Jt1078Publisher {

    default Flux<Jt1078Subscription> subscribe() {
        return this.doSubscribe().dataStream();
    }

    default Flux<Jt1078Subscription> subscribe(Duration timeout) {
        return this.doSubscribe(timeout).dataStream();
    }

    default Flux<Jt1078Subscription> subscribe(String sim, short channelNumber, Duration timeout) {
        return this.doSubscribe(sim, channelNumber, timeout).dataStream();
    }

    Jt1078Subscriber doSubscribe();

    Jt1078Subscriber doSubscribe(Duration timeout);

    Jt1078Subscriber doSubscribe(String sim, short channelNumber, Duration timeout);

    void unsubscribe(String id);
}
