package org.apache.ambari.server.cleanup;
import org.apache.commons.lang.ClassUtils;
public class ClasspathScannerUtils {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.cleanup.ClasspathScannerUtils.class);

    public static java.util.Set<java.lang.Class<?>> findOnClassPath(java.lang.String packageName, java.util.List<java.lang.Class<?>> exclusions, java.util.List<java.lang.Class<?>> selectors) {
        return org.apache.ambari.server.cleanup.ClasspathScannerUtils.findOnClassPath(org.apache.ambari.server.cleanup.ClasspathScannerUtils.class.getClassLoader(), packageName, exclusions, selectors);
    }

    public static java.util.Set<java.lang.Class<?>> findOnClassPath(java.lang.ClassLoader classLoader, java.lang.String packageName, java.util.List<java.lang.Class<?>> exclusions, java.util.List<java.lang.Class<?>> selectors) {
        java.util.Set<java.lang.Class<?>> bindingSet = new java.util.LinkedHashSet<>();
        try {
            com.google.common.reflect.ClassPath classpath = com.google.common.reflect.ClassPath.from(classLoader);
            org.apache.ambari.server.cleanup.ClasspathScannerUtils.LOGGER.info("Checking package [{}] for binding candidates.", packageName);
            for (com.google.common.reflect.ClassPath.ClassInfo classInfo : classpath.getTopLevelClassesRecursive(packageName)) {
                java.lang.Class<?> candidate = classInfo.load();
                if (exclusions.contains(candidate)) {
                    org.apache.ambari.server.cleanup.ClasspathScannerUtils.LOGGER.debug("Candidate [{}] is excluded excluded.", candidate);
                    continue;
                }
                if (org.apache.ambari.server.cleanup.ClasspathScannerUtils.isEligible(candidate, selectors)) {
                    org.apache.ambari.server.cleanup.ClasspathScannerUtils.LOGGER.debug("Found class [{}]", candidate);
                    bindingSet.add(candidate);
                } else {
                    org.apache.ambari.server.cleanup.ClasspathScannerUtils.LOGGER.debug("Candidate [{}] doesn't match.", candidate);
                }
            }
        } catch (java.io.IOException e) {
            org.apache.ambari.server.cleanup.ClasspathScannerUtils.LOGGER.error("Failure during configuring JUICE bindings.", e);
            throw new java.lang.IllegalArgumentException(e);
        }
        return bindingSet;
    }

    private static boolean isEligible(java.lang.Class<?> candidate, java.util.List<java.lang.Class<?>> selectors) {
        return org.apache.ambari.server.cleanup.ClasspathScannerUtils.checkSubClasses(candidate, selectors) || org.apache.ambari.server.cleanup.ClasspathScannerUtils.checkAnnotations(candidate, selectors);
    }

    private static boolean checkAnnotations(java.lang.Class<?> candidate, java.util.List<java.lang.Class<?>> selectors) {
        org.apache.ambari.server.cleanup.ClasspathScannerUtils.LOGGER.debug("Checking annotations for: [{}]", candidate);
        boolean ret = false;
        for (java.lang.annotation.Annotation candidateAnn : candidate.getDeclaredAnnotations()) {
            if (selectors.contains(candidateAnn.annotationType())) {
                ret = true;
                break;
            }
        }
        return ret;
    }

    private static boolean checkSubClasses(java.lang.Class<?> candidate, java.util.List<java.lang.Class<?>> selectors) {
        boolean ret = false;
        org.apache.ambari.server.cleanup.ClasspathScannerUtils.LOGGER.debug("Checking interfaces for: [{}]", candidate);
        java.util.List interfaces = org.apache.commons.lang.ClassUtils.getAllInterfaces(candidate);
        for (java.lang.Class selectorItf : selectors) {
            if (interfaces.contains(selectorItf)) {
                org.apache.ambari.server.cleanup.ClasspathScannerUtils.LOGGER.debug("Checking candidate for subclassing interface: ", selectorItf);
                if (selectorItf.getClass().isAssignableFrom(candidate.getClass())) {
                    ret = true;
                    break;
                }
            }
        }
        return ret;
    }
}