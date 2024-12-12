package org.apache.ambari.server.stack;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
public class HostsTypeTest {
    @org.junit.Test
    public void testGuessMasterFrom1() {
        org.apache.ambari.server.stack.HostsType hosts = org.apache.ambari.server.stack.HostsType.guessHighAvailability(com.google.common.collect.Sets.newLinkedHashSet(java.util.Arrays.asList("c6401")));
        org.junit.Assert.assertThat(hosts.getMasters(), org.hamcrest.core.Is.is(java.util.Collections.singleton("c6401")));
        org.junit.Assert.assertThat(hosts.getSecondaries(), IsCollectionWithSize.hasSize(0));
    }

    @org.junit.Test
    public void testGuessMasterFrom3() {
        org.apache.ambari.server.stack.HostsType hosts = org.apache.ambari.server.stack.HostsType.guessHighAvailability(com.google.common.collect.Sets.newLinkedHashSet(java.util.Arrays.asList("c6401", "c6402", "c6403")));
        org.junit.Assert.assertThat(hosts.getMasters(), org.hamcrest.core.Is.is(java.util.Collections.singleton("c6401")));
        org.junit.Assert.assertThat(hosts.getSecondaries(), org.hamcrest.core.Is.is(com.google.common.collect.Sets.newLinkedHashSet(java.util.Arrays.asList("c6402", "c6403"))));
    }

    @org.junit.Test(expected = java.lang.IllegalArgumentException.class)
    public void testGuessMasterFromEmptyList() {
        org.apache.ambari.server.stack.HostsType.guessHighAvailability(new java.util.LinkedHashSet<>(java.util.Collections.emptySet()));
    }

    @org.junit.Test(expected = java.lang.IllegalArgumentException.class)
    public void testMasterIsMandatory() {
        new org.apache.ambari.server.stack.HostsType.HighAvailabilityHosts(null, java.util.Collections.emptyList());
    }

    @org.junit.Test
    public void testFederatedMastersAndSecondaries() {
        org.apache.ambari.server.stack.HostsType federated = org.apache.ambari.server.stack.HostsType.federated(java.util.Arrays.asList(new org.apache.ambari.server.stack.HostsType.HighAvailabilityHosts("master1", java.util.Arrays.asList("sec1", "sec2")), new org.apache.ambari.server.stack.HostsType.HighAvailabilityHosts("master2", java.util.Arrays.asList("sec3", "sec4"))), new java.util.LinkedHashSet<>(java.util.Collections.emptySet()));
        org.junit.Assert.assertThat(federated.getMasters(), org.hamcrest.core.Is.is(com.google.common.collect.Sets.newHashSet("master1", "master2")));
        org.junit.Assert.assertThat(federated.getSecondaries(), org.hamcrest.core.Is.is(com.google.common.collect.Sets.newHashSet("sec1", "sec2", "sec3", "sec4")));
    }

    @org.junit.Test
    public void testArrangeHosts() {
        org.apache.ambari.server.stack.HostsType federated = org.apache.ambari.server.stack.HostsType.federated(java.util.Arrays.asList(new org.apache.ambari.server.stack.HostsType.HighAvailabilityHosts("master1", java.util.Arrays.asList("sec1", "sec2")), new org.apache.ambari.server.stack.HostsType.HighAvailabilityHosts("master2", java.util.Arrays.asList("sec3", "sec4"))), new java.util.LinkedHashSet<>(java.util.Collections.emptySet()));
        federated.arrangeHostSecondariesFirst();
        org.junit.Assert.assertThat(federated.getHosts(), org.hamcrest.core.Is.is(com.google.common.collect.Sets.newLinkedHashSet(java.util.Arrays.asList("sec1", "sec2", "master1", "sec3", "sec4", "master2"))));
    }
}