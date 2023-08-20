package io.github.hylexus.jt.demos.common.configuration;

import io.github.hylexus.jt.demos.common.web.reactive.SamplesResponseWrapperResultHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.accept.RequestedContentTypeResolver;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityResultHandler;

import static org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type.REACTIVE;

@ConditionalOnWebApplication(type = REACTIVE)
public class DemosReactiveAutoConfiguration {

    @Bean
    public SamplesResponseWrapperResultHandler responseWrapperResultHandler(
            ServerCodecConfigurer serverCodecConfigurer,
            RequestedContentTypeResolver requestedContentTypeResolver,
            ResponseEntityResultHandler responseEntityResultHandler) {

        return new SamplesResponseWrapperResultHandler(
                serverCodecConfigurer.getWriters(),
                requestedContentTypeResolver,
                responseEntityResultHandler
        );
    }
}