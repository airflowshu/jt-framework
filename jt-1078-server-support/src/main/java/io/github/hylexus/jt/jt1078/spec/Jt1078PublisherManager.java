package io.github.hylexus.jt.jt1078.spec;

import java.util.function.Predicate;
import java.util.stream.Stream;

public interface Jt1078PublisherManager {

    long count(Predicate<Jt1078SubscriberDescriptor> predicate);

    Stream<Jt1078SubscriberDescriptor> list();

    default Stream<Jt1078SubscriberDescriptor> list(String sim) {
        return list().filter(it -> it.getSim().equals(sim));
    }

    default Stream<Jt1078SubscriberDescriptor> list(String sim, short channel) {
        return this.list(sim).filter(it -> it.getChannel() == channel);
    }

    void closeSubscriber(String id);
}
