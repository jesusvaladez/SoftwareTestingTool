package org.apache.ambari.server.agent.stomp;
import org.springframework.expression.spel.support.SimpleEvaluationContext;
import org.springframework.lang.Nullable;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.broker.AbstractSubscriptionRegistry;
import org.springframework.messaging.simp.broker.SubscriptionRegistry;
import org.springframework.messaging.support.MessageHeaderAccessor;
public class AmbariSubscriptionRegistry extends org.springframework.messaging.simp.broker.AbstractSubscriptionRegistry {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.agent.stomp.AmbariSubscriptionRegistry.class);

    private static final org.springframework.expression.EvaluationContext messageEvalContext = org.springframework.expression.spel.support.SimpleEvaluationContext.forPropertyAccessors(new org.apache.ambari.server.agent.stomp.AmbariSubscriptionRegistry.SimpMessageHeaderPropertyAccessor()).build();

    private org.springframework.util.PathMatcher pathMatcher = new org.springframework.util.AntPathMatcher();

    private volatile int cacheLimit;

    private java.lang.String selectorHeaderName = "selector";

    private volatile boolean selectorHeaderInUse = false;

    private final org.springframework.expression.ExpressionParser expressionParser = new org.springframework.expression.spel.standard.SpelExpressionParser();

    private final org.apache.ambari.server.agent.stomp.AmbariSubscriptionRegistry.DestinationCache destinationCache;

    private final org.apache.ambari.server.agent.stomp.AmbariSubscriptionRegistry.SessionSubscriptionRegistry subscriptionRegistry = new org.apache.ambari.server.agent.stomp.AmbariSubscriptionRegistry.SessionSubscriptionRegistry();

    public AmbariSubscriptionRegistry(int cacheLimit) {
        this.cacheLimit = cacheLimit;
        destinationCache = new org.apache.ambari.server.agent.stomp.AmbariSubscriptionRegistry.DestinationCache();
    }

    public void setPathMatcher(org.springframework.util.PathMatcher pathMatcher) {
        this.pathMatcher = pathMatcher;
    }

    public org.springframework.util.PathMatcher getPathMatcher() {
        return this.pathMatcher;
    }

    public void setCacheLimit(int cacheLimit) {
        this.cacheLimit = cacheLimit;
    }

    public int getCacheLimit() {
        return this.cacheLimit;
    }

    public void setSelectorHeaderName(java.lang.String selectorHeaderName) {
        org.springframework.util.Assert.notNull(selectorHeaderName, "'selectorHeaderName' must not be null");
        this.selectorHeaderName = selectorHeaderName;
    }

    public java.lang.String getSelectorHeaderName() {
        return this.selectorHeaderName;
    }

    @java.lang.Override
    protected void addSubscriptionInternal(java.lang.String sessionId, java.lang.String subsId, java.lang.String destination, org.springframework.messaging.Message<?> message) {
        org.springframework.expression.Expression expression = getSelectorExpression(message.getHeaders());
        this.subscriptionRegistry.addSubscription(sessionId, subsId, destination, expression);
        this.destinationCache.updateAfterNewSubscription(destination, sessionId, subsId);
    }

    @org.springframework.lang.Nullable
    private org.springframework.expression.Expression getSelectorExpression(org.springframework.messaging.MessageHeaders headers) {
        org.springframework.expression.Expression expression = null;
        if (getSelectorHeaderName() != null) {
            java.lang.String selector = org.springframework.messaging.simp.SimpMessageHeaderAccessor.getFirstNativeHeader(getSelectorHeaderName(), headers);
            if (selector != null) {
                try {
                    expression = this.expressionParser.parseExpression(selector);
                    this.selectorHeaderInUse = true;
                    if (logger.isTraceEnabled()) {
                        logger.trace(("Subscription selector: [" + selector) + "]");
                    }
                } catch (java.lang.Throwable ex) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Failed to parse selector: " + selector, ex);
                    }
                }
            }
        }
        return expression;
    }

    @java.lang.Override
    protected void removeSubscriptionInternal(java.lang.String sessionId, java.lang.String subsId, org.springframework.messaging.Message<?> message) {
        org.apache.ambari.server.agent.stomp.AmbariSubscriptionRegistry.SessionSubscriptionInfo info = this.subscriptionRegistry.getSubscriptions(sessionId);
        if (info != null) {
            java.lang.String destination = info.removeSubscription(subsId);
            if (destination != null) {
                this.destinationCache.updateAfterRemovedSubscription(sessionId, subsId);
            }
        }
    }

    @java.lang.Override
    public void unregisterAllSubscriptions(java.lang.String sessionId) {
        org.apache.ambari.server.agent.stomp.AmbariSubscriptionRegistry.SessionSubscriptionInfo info = this.subscriptionRegistry.removeSubscriptions(sessionId);
        if (info != null) {
            this.destinationCache.updateAfterRemovedSession(info);
        }
    }

    @java.lang.Override
    protected org.springframework.util.MultiValueMap<java.lang.String, java.lang.String> findSubscriptionsInternal(java.lang.String destination, org.springframework.messaging.Message<?> message) {
        org.springframework.util.MultiValueMap<java.lang.String, java.lang.String> result = this.destinationCache.getSubscriptions(destination, message);
        return filterSubscriptions(result, message);
    }

    private org.springframework.util.MultiValueMap<java.lang.String, java.lang.String> filterSubscriptions(org.springframework.util.MultiValueMap<java.lang.String, java.lang.String> allMatches, org.springframework.messaging.Message<?> message) {
        if (!this.selectorHeaderInUse) {
            return allMatches;
        }
        org.springframework.util.MultiValueMap<java.lang.String, java.lang.String> result = new org.springframework.util.LinkedMultiValueMap<>(allMatches.size());
        allMatches.forEach((sessionId, subIds) -> {
            subIds.forEach(subId -> {
                org.apache.ambari.server.agent.stomp.AmbariSubscriptionRegistry.SessionSubscriptionInfo info = this.subscriptionRegistry.getSubscriptions(sessionId);
                if (info == null) {
                    return;
                }
                org.apache.ambari.server.agent.stomp.AmbariSubscriptionRegistry.Subscription sub = info.getSubscription(subId);
                if (sub == null) {
                    return;
                }
                org.springframework.expression.Expression expression = sub.getSelectorExpression();
                if (expression == null) {
                    result.add(sessionId, subId);
                    return;
                }
                try {
                    if (java.lang.Boolean.TRUE.equals(expression.getValue(org.apache.ambari.server.agent.stomp.AmbariSubscriptionRegistry.messageEvalContext, message, java.lang.Boolean.class))) {
                        result.add(sessionId, subId);
                    }
                } catch (org.springframework.expression.spel.SpelEvaluationException ex) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Failed to evaluate selector: " + ex.getMessage());
                    }
                } catch (java.lang.Throwable ex) {
                    logger.debug("Failed to evaluate selector", ex);
                }
            });
        });
        return result;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ((("DefaultSubscriptionRegistry[" + this.destinationCache) + ", ") + this.subscriptionRegistry) + "]";
    }

    private class DestinationCache {
        private final java.util.Map<java.lang.String, org.springframework.util.LinkedMultiValueMap<java.lang.String, java.lang.String>> accessCache = new java.util.concurrent.ConcurrentHashMap<>(cacheLimit);

        private final com.google.common.cache.Cache<java.lang.String, java.lang.String> notSubscriptionCache = com.google.common.cache.CacheBuilder.newBuilder().maximumSize(cacheLimit).build();

        public org.springframework.util.LinkedMultiValueMap<java.lang.String, java.lang.String> getSubscriptions(java.lang.String destination, org.springframework.messaging.Message<?> message) {
            org.springframework.util.LinkedMultiValueMap<java.lang.String, java.lang.String> copiedSubscriptions = new org.springframework.util.LinkedMultiValueMap<>();
            if (notSubscriptionCache.asMap().keySet().contains(destination)) {
                return copiedSubscriptions;
            }
            this.accessCache.compute(destination, (key, value) -> {
                if (value == null) {
                    org.springframework.util.LinkedMultiValueMap<java.lang.String, java.lang.String> result = new org.springframework.util.LinkedMultiValueMap<>();
                    subscriptionRegistry.getAllSubscriptions().forEach(info -> {
                        info.getDestinations().forEach(destinationPattern -> {
                            if (destinationPattern.equals(destination)) {
                                info.getSubscriptions(destinationPattern).forEach(subscription -> {
                                    result.add(info.sessionId, subscription.getId());
                                });
                            }
                        });
                    });
                    if (!result.isEmpty()) {
                        copiedSubscriptions.addAll(result.deepCopy());
                        return result;
                    } else {
                        notSubscriptionCache.put(destination, "");
                        return null;
                    }
                } else {
                    copiedSubscriptions.addAll(value.deepCopy());
                    return value;
                }
            });
            return copiedSubscriptions;
        }

        public void updateAfterNewSubscription(java.lang.String destination, java.lang.String sessionId, java.lang.String subsId) {
            org.springframework.util.LinkedMultiValueMap<java.lang.String, java.lang.String> updatedMap = this.accessCache.computeIfPresent(destination, (key, value) -> {
                if (getPathMatcher().match(destination, key)) {
                    org.springframework.util.LinkedMultiValueMap<java.lang.String, java.lang.String> subs = value;
                    subs.add(sessionId, subsId);
                    return subs;
                }
                return value;
            });
            if (updatedMap == null) {
                this.notSubscriptionCache.invalidate(destination);
            }
        }

        public void updateAfterRemovedSubscription(java.lang.String sessionId, java.lang.String subsId) {
            for (java.util.Iterator<java.util.Map.Entry<java.lang.String, org.springframework.util.LinkedMultiValueMap<java.lang.String, java.lang.String>>> iterator = this.accessCache.entrySet().iterator(); iterator.hasNext();) {
                java.util.Map.Entry<java.lang.String, org.springframework.util.LinkedMultiValueMap<java.lang.String, java.lang.String>> entry = iterator.next();
                java.lang.String destination = entry.getKey();
                this.accessCache.compute(destination, (key, value) -> {
                    if (value != null) {
                        java.util.List<java.lang.String> subscriptions = value.get(sessionId);
                        if (subscriptions != null) {
                            subscriptions.remove(subsId);
                            if (subscriptions.isEmpty()) {
                                value.remove(sessionId);
                            }
                            if (value.isEmpty()) {
                                return null;
                            } else {
                                this.notSubscriptionCache.invalidate(destination);
                            }
                        }
                    }
                    return value;
                });
            }
        }

        public void updateAfterRemovedSession(org.apache.ambari.server.agent.stomp.AmbariSubscriptionRegistry.SessionSubscriptionInfo info) {
            for (java.util.Iterator<java.util.Map.Entry<java.lang.String, org.springframework.util.LinkedMultiValueMap<java.lang.String, java.lang.String>>> iterator = this.accessCache.entrySet().iterator(); iterator.hasNext();) {
                java.util.Map.Entry<java.lang.String, org.springframework.util.LinkedMultiValueMap<java.lang.String, java.lang.String>> entry = iterator.next();
                java.lang.String destination = entry.getKey();
                this.accessCache.compute(destination, (key, value) -> {
                    if (value != null) {
                        if (value.remove(info.getSessionId()) != null) {
                            if (value.isEmpty()) {
                                return null;
                            } else {
                                this.notSubscriptionCache.invalidate(destination);
                            }
                        }
                    }
                    return value;
                });
            }
        }

        @java.lang.Override
        public java.lang.String toString() {
            return ("cache[" + this.accessCache.size()) + " destination(s)]";
        }
    }

    private static class SessionSubscriptionRegistry {
        private final java.util.concurrent.ConcurrentMap<java.lang.String, org.apache.ambari.server.agent.stomp.AmbariSubscriptionRegistry.SessionSubscriptionInfo> sessions = new java.util.concurrent.ConcurrentHashMap<>();

        @org.springframework.lang.Nullable
        public org.apache.ambari.server.agent.stomp.AmbariSubscriptionRegistry.SessionSubscriptionInfo getSubscriptions(java.lang.String sessionId) {
            return this.sessions.get(sessionId);
        }

        public java.util.Collection<org.apache.ambari.server.agent.stomp.AmbariSubscriptionRegistry.SessionSubscriptionInfo> getAllSubscriptions() {
            return this.sessions.values();
        }

        public org.apache.ambari.server.agent.stomp.AmbariSubscriptionRegistry.SessionSubscriptionInfo addSubscription(java.lang.String sessionId, java.lang.String subscriptionId, java.lang.String destination, @org.springframework.lang.Nullable
        org.springframework.expression.Expression selectorExpression) {
            org.apache.ambari.server.agent.stomp.AmbariSubscriptionRegistry.SessionSubscriptionInfo info = this.sessions.get(sessionId);
            if (info == null) {
                info = new org.apache.ambari.server.agent.stomp.AmbariSubscriptionRegistry.SessionSubscriptionInfo(sessionId);
                org.apache.ambari.server.agent.stomp.AmbariSubscriptionRegistry.SessionSubscriptionInfo value = this.sessions.putIfAbsent(sessionId, info);
                if (value != null) {
                    info = value;
                }
            }
            info.addSubscription(destination, subscriptionId, selectorExpression);
            return info;
        }

        @org.springframework.lang.Nullable
        public org.apache.ambari.server.agent.stomp.AmbariSubscriptionRegistry.SessionSubscriptionInfo removeSubscriptions(java.lang.String sessionId) {
            return this.sessions.remove(sessionId);
        }

        @java.lang.Override
        public java.lang.String toString() {
            return ("registry[" + this.sessions.size()) + " sessions]";
        }
    }

    private static class SessionSubscriptionInfo {
        private final java.lang.String sessionId;

        private final java.util.Map<java.lang.String, java.util.Set<org.apache.ambari.server.agent.stomp.AmbariSubscriptionRegistry.Subscription>> destinationLookup = new java.util.concurrent.ConcurrentHashMap<>(4);

        public SessionSubscriptionInfo(java.lang.String sessionId) {
            org.springframework.util.Assert.notNull(sessionId, "'sessionId' must not be null");
            this.sessionId = sessionId;
        }

        public java.lang.String getSessionId() {
            return this.sessionId;
        }

        public java.util.Set<java.lang.String> getDestinations() {
            return this.destinationLookup.keySet();
        }

        public java.util.Set<org.apache.ambari.server.agent.stomp.AmbariSubscriptionRegistry.Subscription> getSubscriptions(java.lang.String destination) {
            return this.destinationLookup.get(destination);
        }

        @org.springframework.lang.Nullable
        public org.apache.ambari.server.agent.stomp.AmbariSubscriptionRegistry.Subscription getSubscription(java.lang.String subscriptionId) {
            for (java.util.Map.Entry<java.lang.String, java.util.Set<org.apache.ambari.server.agent.stomp.AmbariSubscriptionRegistry.Subscription>> destinationEntry : this.destinationLookup.entrySet()) {
                for (org.apache.ambari.server.agent.stomp.AmbariSubscriptionRegistry.Subscription sub : destinationEntry.getValue()) {
                    if (sub.getId().equalsIgnoreCase(subscriptionId)) {
                        return sub;
                    }
                }
            }
            return null;
        }

        public void addSubscription(java.lang.String destination, java.lang.String subscriptionId, @org.springframework.lang.Nullable
        org.springframework.expression.Expression selectorExpression) {
            java.util.Set<org.apache.ambari.server.agent.stomp.AmbariSubscriptionRegistry.Subscription> subs = this.destinationLookup.get(destination);
            if (subs == null) {
                synchronized(this.destinationLookup) {
                    subs = this.destinationLookup.get(destination);
                    if (subs == null) {
                        subs = new java.util.concurrent.CopyOnWriteArraySet<>();
                        this.destinationLookup.put(destination, subs);
                    }
                }
            }
            subs.add(new org.apache.ambari.server.agent.stomp.AmbariSubscriptionRegistry.Subscription(subscriptionId, selectorExpression));
        }

        @org.springframework.lang.Nullable
        public java.lang.String removeSubscription(java.lang.String subscriptionId) {
            for (java.util.Map.Entry<java.lang.String, java.util.Set<org.apache.ambari.server.agent.stomp.AmbariSubscriptionRegistry.Subscription>> destinationEntry : this.destinationLookup.entrySet()) {
                java.util.Set<org.apache.ambari.server.agent.stomp.AmbariSubscriptionRegistry.Subscription> subs = destinationEntry.getValue();
                if (subs != null) {
                    for (org.apache.ambari.server.agent.stomp.AmbariSubscriptionRegistry.Subscription sub : subs) {
                        if (sub.getId().equals(subscriptionId) && subs.remove(sub)) {
                            synchronized(this.destinationLookup) {
                                if (subs.isEmpty()) {
                                    this.destinationLookup.remove(destinationEntry.getKey());
                                }
                            }
                            return destinationEntry.getKey();
                        }
                    }
                }
            }
            return null;
        }

        @java.lang.Override
        public java.lang.String toString() {
            return ((("[sessionId=" + this.sessionId) + ", subscriptions=") + this.destinationLookup) + "]";
        }
    }

    private static final class Subscription {
        private final java.lang.String id;

        @org.springframework.lang.Nullable
        private final org.springframework.expression.Expression selectorExpression;

        public Subscription(java.lang.String id, @org.springframework.lang.Nullable
        org.springframework.expression.Expression selector) {
            org.springframework.util.Assert.notNull(id, "Subscription id must not be null");
            this.id = id;
            this.selectorExpression = selector;
        }

        public java.lang.String getId() {
            return this.id;
        }

        @org.springframework.lang.Nullable
        public org.springframework.expression.Expression getSelectorExpression() {
            return this.selectorExpression;
        }

        @java.lang.Override
        public boolean equals(java.lang.Object other) {
            return (this == other) || ((other instanceof org.apache.ambari.server.agent.stomp.AmbariSubscriptionRegistry.Subscription) && this.id.equals(((org.apache.ambari.server.agent.stomp.AmbariSubscriptionRegistry.Subscription) (other)).id));
        }

        @java.lang.Override
        public int hashCode() {
            return this.id.hashCode();
        }

        @java.lang.Override
        public java.lang.String toString() {
            return ("subscription(id=" + this.id) + ")";
        }
    }

    private static class SimpMessageHeaderPropertyAccessor implements org.springframework.expression.PropertyAccessor {
        @java.lang.Override
        public java.lang.Class<?>[] getSpecificTargetClasses() {
            return new java.lang.Class<?>[]{ org.springframework.messaging.Message.class, org.springframework.messaging.MessageHeaders.class };
        }

        @java.lang.Override
        public boolean canRead(org.springframework.expression.EvaluationContext context, @org.springframework.lang.Nullable
        java.lang.Object target, java.lang.String name) {
            return true;
        }

        @java.lang.Override
        public org.springframework.expression.TypedValue read(org.springframework.expression.EvaluationContext context, @org.springframework.lang.Nullable
        java.lang.Object target, java.lang.String name) {
            java.lang.Object value;
            if (target instanceof org.springframework.messaging.Message) {
                value = (name.equals("headers")) ? ((org.springframework.messaging.Message) (target)).getHeaders() : null;
            } else if (target instanceof org.springframework.messaging.MessageHeaders) {
                org.springframework.messaging.MessageHeaders headers = ((org.springframework.messaging.MessageHeaders) (target));
                org.springframework.messaging.simp.SimpMessageHeaderAccessor accessor = org.springframework.messaging.support.MessageHeaderAccessor.getAccessor(headers, org.springframework.messaging.simp.SimpMessageHeaderAccessor.class);
                org.springframework.util.Assert.state(accessor != null, "No SimpMessageHeaderAccessor");
                if ("destination".equalsIgnoreCase(name)) {
                    value = accessor.getDestination();
                } else {
                    value = accessor.getFirstNativeHeader(name);
                    if (value == null) {
                        value = headers.get(name);
                    }
                }
            } else {
                throw new java.lang.IllegalStateException("Expected Message or MessageHeaders.");
            }
            return new org.springframework.expression.TypedValue(value);
        }

        @java.lang.Override
        public boolean canWrite(org.springframework.expression.EvaluationContext context, @org.springframework.lang.Nullable
        java.lang.Object target, java.lang.String name) {
            return false;
        }

        @java.lang.Override
        public void write(org.springframework.expression.EvaluationContext context, @org.springframework.lang.Nullable
        java.lang.Object target, java.lang.String name, @org.springframework.lang.Nullable
        java.lang.Object value) {
        }
    }
}