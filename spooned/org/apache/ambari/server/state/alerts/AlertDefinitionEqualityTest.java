package org.apache.ambari.server.state.alerts;
@org.junit.experimental.categories.Category({ category.AlertTest.class })
public class AlertDefinitionEqualityTest extends junit.framework.TestCase {
    @org.junit.Test
    public void testAlertDefinitionEquality() {
        org.apache.ambari.server.state.alert.AlertDefinition ad1 = getAlertDefinition(org.apache.ambari.server.state.alert.SourceType.PORT);
        org.apache.ambari.server.state.alert.AlertDefinition ad2 = getAlertDefinition(org.apache.ambari.server.state.alert.SourceType.PORT);
        junit.framework.Assert.assertTrue(ad1.equals(ad2));
        junit.framework.Assert.assertTrue(ad1.deeplyEquals(ad2));
        ad2.setInterval(2);
        junit.framework.Assert.assertTrue(ad1.equals(ad2));
        junit.framework.Assert.assertFalse(ad1.deeplyEquals(ad2));
        ad2.setName(getName() + " foo");
        junit.framework.Assert.assertFalse(ad1.equals(ad2));
        junit.framework.Assert.assertFalse(ad1.deeplyEquals(ad2));
        ad2 = getAlertDefinition(org.apache.ambari.server.state.alert.SourceType.AGGREGATE);
        junit.framework.Assert.assertFalse(ad1.deeplyEquals(ad2));
        ad2 = getAlertDefinition(org.apache.ambari.server.state.alert.SourceType.PORT);
        junit.framework.Assert.assertTrue(ad1.deeplyEquals(ad2));
        ad2.getSource().getReporting().getOk().setText("foo");
        junit.framework.Assert.assertFalse(ad1.deeplyEquals(ad2));
    }

    private org.apache.ambari.server.state.alert.AlertDefinition getAlertDefinition(org.apache.ambari.server.state.alert.SourceType sourceType) {
        org.apache.ambari.server.state.alert.AlertDefinition definition = new org.apache.ambari.server.state.alert.AlertDefinition();
        definition.setClusterId(1);
        definition.setComponentName("component");
        definition.setEnabled(true);
        definition.setInterval(1);
        definition.setName("Name");
        definition.setScope(org.apache.ambari.server.state.alert.Scope.ANY);
        definition.setServiceName("ServiceName");
        definition.setLabel("Label");
        definition.setDescription("Description");
        definition.setSource(getSource(sourceType));
        return definition;
    }

    private org.apache.ambari.server.state.alert.Source getSource(org.apache.ambari.server.state.alert.SourceType type) {
        org.apache.ambari.server.state.alert.Source source = null;
        switch (type) {
            case AGGREGATE :
                source = new org.apache.ambari.server.state.alert.AggregateSource();
                ((org.apache.ambari.server.state.alert.AggregateSource) (source)).setAlertName("hdfs-foo");
                break;
            case METRIC :
                source = new org.apache.ambari.server.state.alert.MetricSource();
                break;
            case PERCENT :
                source = new org.apache.ambari.server.state.alert.PercentSource();
                break;
            case PORT :
                source = new org.apache.ambari.server.state.alert.PortSource();
                ((org.apache.ambari.server.state.alert.PortSource) (source)).setPort(80);
                ((org.apache.ambari.server.state.alert.PortSource) (source)).setUri("uri://foo");
                break;
            case SCRIPT :
                source = new org.apache.ambari.server.state.alert.ScriptSource();
                break;
            default :
                break;
        }
        source.setReporting(getReporting());
        return source;
    }

    private org.apache.ambari.server.state.alert.Reporting getReporting() {
        org.apache.ambari.server.state.alert.Reporting reporting = new org.apache.ambari.server.state.alert.Reporting();
        reporting.setCritical(getReportingTemplate(org.apache.ambari.server.state.AlertState.CRITICAL));
        reporting.setWarning(getReportingTemplate(org.apache.ambari.server.state.AlertState.WARNING));
        reporting.setOk(getReportingTemplate(org.apache.ambari.server.state.AlertState.OK));
        return reporting;
    }

    private org.apache.ambari.server.state.alert.Reporting.ReportTemplate getReportingTemplate(org.apache.ambari.server.state.AlertState state) {
        org.apache.ambari.server.state.alert.Reporting.ReportTemplate template = new org.apache.ambari.server.state.alert.Reporting.ReportTemplate();
        switch (state) {
            case CRITICAL :
                template.setText("OH NO!");
                template.setValue(80.0);
                break;
            case OK :
                template.setText("No worries.");
                break;
            case UNKNOWN :
                break;
            case WARNING :
                template.setText("Getting nervous...");
                template.setValue(50.0);
                break;
            default :
                break;
        }
        return template;
    }
}