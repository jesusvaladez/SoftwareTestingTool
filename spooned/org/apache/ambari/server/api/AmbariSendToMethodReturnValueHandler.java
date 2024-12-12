package org.apache.ambari.server.api;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.handler.annotation.support.DestinationVariableMethodArgumentResolver;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.support.MissingSessionUserException;
import org.springframework.messaging.simp.annotation.support.SendToMethodReturnValueHandler;
public class AmbariSendToMethodReturnValueHandler extends org.springframework.messaging.simp.annotation.support.SendToMethodReturnValueHandler {
    private final org.springframework.messaging.simp.SimpMessageSendingOperations messagingTemplate;

    private final boolean annotationRequired;

    private org.springframework.util.PropertyPlaceholderHelper placeholderHelper = new org.springframework.util.PropertyPlaceholderHelper("{", "}", null, false);

    private java.lang.String defaultUserDestinationPrefix = "/queue";

    private java.lang.String defaultDestinationPrefix = "/topic";

    public static final java.lang.String CORRELATION_ID_HEADER = "correlationId";

    public static final java.lang.String NATIVE_HEADERS = "nativeHeaders";

    public AmbariSendToMethodReturnValueHandler(org.springframework.messaging.simp.SimpMessageSendingOperations messagingTemplate, boolean annotationRequired) {
        super(messagingTemplate, annotationRequired);
        this.messagingTemplate = messagingTemplate;
        this.annotationRequired = annotationRequired;
    }

    @java.lang.Override
    public void handleReturnValue(java.lang.Object returnValue, org.springframework.core.MethodParameter returnType, org.springframework.messaging.Message<?> message) throws java.lang.Exception {
        if (returnValue == null) {
            return;
        }
        org.springframework.messaging.MessageHeaders headers = message.getHeaders();
        java.lang.String sessionId = org.springframework.messaging.simp.SimpMessageHeaderAccessor.getSessionId(headers);
        org.springframework.util.PropertyPlaceholderHelper.PlaceholderResolver varResolver = initVarResolver(headers);
        java.lang.Object annotation = findAnnotation(returnType);
        java.lang.String correlationId = getCorrelationId(message);
        if ((annotation != null) && (annotation instanceof org.springframework.messaging.simp.annotation.SendToUser)) {
            org.springframework.messaging.simp.annotation.SendToUser sendToUser = ((org.springframework.messaging.simp.annotation.SendToUser) (annotation));
            boolean broadcast = sendToUser.broadcast();
            java.lang.String user = getUserName(message, headers);
            if (user == null) {
                if (sessionId == null) {
                    throw new org.springframework.messaging.simp.annotation.support.MissingSessionUserException(message);
                }
                user = sessionId;
                broadcast = false;
            }
            java.lang.String[] destinations = getTargetDestinations(sendToUser, message, this.defaultUserDestinationPrefix);
            for (java.lang.String destination : destinations) {
                destination = this.placeholderHelper.replacePlaceholders(destination, varResolver);
                if (broadcast) {
                    this.messagingTemplate.convertAndSendToUser(user, destination, returnValue, createHeaders(null, returnType, correlationId));
                } else {
                    this.messagingTemplate.convertAndSendToUser(user, destination, returnValue, createHeaders(sessionId, returnType, correlationId));
                }
            }
        } else {
            org.springframework.messaging.handler.annotation.SendTo sendTo = ((org.springframework.messaging.handler.annotation.SendTo) (annotation));
            java.lang.String[] destinations = getTargetDestinations(sendTo, message, this.defaultDestinationPrefix);
            for (java.lang.String destination : destinations) {
                destination = this.placeholderHelper.replacePlaceholders(destination, varResolver);
                this.messagingTemplate.convertAndSend(destination, returnValue, createHeaders(sessionId, returnType, correlationId));
            }
        }
    }

    private java.lang.String getCorrelationId(org.springframework.messaging.Message<?> message) {
        org.springframework.messaging.simp.SimpMessageHeaderAccessor headerAccessor = org.springframework.messaging.simp.SimpMessageHeaderAccessor.wrap(message);
        return headerAccessor.getFirstNativeHeader(org.apache.ambari.server.api.AmbariSendToMethodReturnValueHandler.CORRELATION_ID_HEADER);
    }

    private org.springframework.messaging.MessageHeaders createHeaders(java.lang.String sessionId, org.springframework.core.MethodParameter returnType, java.lang.String correlationId) {
        org.springframework.messaging.simp.SimpMessageHeaderAccessor headerAccessor = org.springframework.messaging.simp.SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        if (getHeaderInitializer() != null) {
            getHeaderInitializer().initHeaders(headerAccessor);
        }
        if (sessionId != null) {
            headerAccessor.setSessionId(sessionId);
        }
        headerAccessor.setHeader(SimpMessagingTemplate.CONVERSION_HINT_HEADER, returnType);
        headerAccessor.setLeaveMutable(true);
        headerAccessor.addNativeHeader(org.apache.ambari.server.api.AmbariSendToMethodReturnValueHandler.CORRELATION_ID_HEADER, correlationId);
        return headerAccessor.getMessageHeaders();
    }

    private java.lang.Object findAnnotation(org.springframework.core.MethodParameter returnType) {
        java.lang.annotation.Annotation[] anns = new java.lang.annotation.Annotation[4];
        anns[0] = org.springframework.core.annotation.AnnotatedElementUtils.findMergedAnnotation(returnType.getMethod(), org.springframework.messaging.simp.annotation.SendToUser.class);
        anns[1] = org.springframework.core.annotation.AnnotatedElementUtils.findMergedAnnotation(returnType.getMethod(), org.springframework.messaging.handler.annotation.SendTo.class);
        anns[2] = org.springframework.core.annotation.AnnotatedElementUtils.findMergedAnnotation(returnType.getDeclaringClass(), org.springframework.messaging.simp.annotation.SendToUser.class);
        anns[3] = org.springframework.core.annotation.AnnotatedElementUtils.findMergedAnnotation(returnType.getDeclaringClass(), org.springframework.messaging.handler.annotation.SendTo.class);
        if ((anns[0] != null) && (!org.springframework.util.ObjectUtils.isEmpty(((org.springframework.messaging.simp.annotation.SendToUser) (anns[0])).value()))) {
            return anns[0];
        }
        if ((anns[1] != null) && (!org.springframework.util.ObjectUtils.isEmpty(((org.springframework.messaging.handler.annotation.SendTo) (anns[1])).value()))) {
            return anns[1];
        }
        if ((anns[2] != null) && (!org.springframework.util.ObjectUtils.isEmpty(((org.springframework.messaging.simp.annotation.SendToUser) (anns[2])).value()))) {
            return anns[2];
        }
        if ((anns[3] != null) && (!org.springframework.util.ObjectUtils.isEmpty(((org.springframework.messaging.handler.annotation.SendTo) (anns[3])).value()))) {
            return anns[3];
        }
        for (int i = 0; i < 4; i++) {
            if (anns[i] != null) {
                return anns[i];
            }
        }
        return null;
    }

    private org.springframework.util.PropertyPlaceholderHelper.PlaceholderResolver initVarResolver(org.springframework.messaging.MessageHeaders headers) {
        java.lang.String name = org.springframework.messaging.handler.annotation.support.DestinationVariableMethodArgumentResolver.DESTINATION_TEMPLATE_VARIABLES_HEADER;
        java.util.Map<java.lang.String, java.lang.String> vars = ((java.util.Map<java.lang.String, java.lang.String>) (headers.get(name)));
        return new org.apache.ambari.server.api.AmbariSendToMethodReturnValueHandler.DestinationVariablePlaceholderResolver(vars);
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ("AmbariSendToMethodReturnValueHandler [annotationRequired=" + annotationRequired) + "]";
    }

    private static class DestinationVariablePlaceholderResolver implements org.springframework.util.PropertyPlaceholderHelper.PlaceholderResolver {
        private final java.util.Map<java.lang.String, java.lang.String> vars;

        public DestinationVariablePlaceholderResolver(java.util.Map<java.lang.String, java.lang.String> vars) {
            this.vars = vars;
        }

        @java.lang.Override
        public java.lang.String resolvePlaceholder(java.lang.String placeholderName) {
            return this.vars != null ? this.vars.get(placeholderName) : null;
        }
    }
}