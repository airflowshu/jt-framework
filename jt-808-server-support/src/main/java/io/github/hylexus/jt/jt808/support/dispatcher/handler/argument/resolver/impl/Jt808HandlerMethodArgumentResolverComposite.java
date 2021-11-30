package io.github.hylexus.jt.jt808.support.dispatcher.handler.argument.resolver.impl;

import io.github.hylexus.jt.jt808.support.dispatcher.handler.argument.resolver.ArgumentContext;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.argument.resolver.HandlerMethodArgumentResolver;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.reflection.MethodParameter;
import io.github.hylexus.jt.jt808.support.exception.Jt808ArgumentResolveException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Jt808HandlerMethodArgumentResolverComposite implements HandlerMethodArgumentResolver {

    private final List<HandlerMethodArgumentResolver> resolvers = new ArrayList<>();
    private final ConcurrentMap<MethodParameter, HandlerMethodArgumentResolver> argumentResolverCache = new ConcurrentHashMap<>();

    public Jt808HandlerMethodArgumentResolverComposite() {
        addDefaultHandlerMethodArgumentResolver(this);
    }

    static void addDefaultHandlerMethodArgumentResolver(Jt808HandlerMethodArgumentResolverComposite resolvers) {
        resolvers.addResolver(new Jt808RequestMsgBodyHandlerMethodArgumentResolver());
        resolvers.addResolver(new Jt808RequestArgumentResolver());
        resolvers.addResolver(new Jt808SessionArgumentResolver());
        resolvers.addResolver(new Jt808RequestHeaderArgumentResolver());
        resolvers.addResolver(new Jt808ExceptionArgumentResolver());
    }

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        final HandlerMethodArgumentResolver resolver = getResolver(methodParameter);
        return resolver != null;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ArgumentContext context) {
        final HandlerMethodArgumentResolver resolver = this.getResolver(parameter);
        if (resolver != null) {
            return resolver.resolveArgument(parameter, context);
        }
        throw new Jt808ArgumentResolveException("Can not resolve argument [ " + parameter.getParameterType() + " ]", context);
    }

    private HandlerMethodArgumentResolver getResolver(MethodParameter methodParameter) {
        final HandlerMethodArgumentResolver resolver = this.argumentResolverCache.get(methodParameter);
        if (resolver != null) {
            return resolver;
        }

        for (HandlerMethodArgumentResolver argumentResolver : this.resolvers) {
            if (argumentResolver.supportsParameter(methodParameter)) {
                this.argumentResolverCache.put(methodParameter, argumentResolver);
                return argumentResolver;
            }
        }

        return null;
    }

    public Jt808HandlerMethodArgumentResolverComposite addResolver(HandlerMethodArgumentResolver resolver) {
        this.resolvers.add(resolver);
        return this;
    }
}