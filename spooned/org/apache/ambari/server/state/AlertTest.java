package org.apache.ambari.server.state;
public class AlertTest extends org.apache.ambari.server.state.Alert {
    @org.junit.Test
    public void testSetTextMiddleEllipsizing() throws java.lang.Exception {
        org.apache.ambari.server.state.Alert alert = new org.apache.ambari.server.state.Alert();
        java.lang.String shortText = "Short alert text";
        java.lang.String longText = ("Not so short" + new java.lang.String(new char[org.apache.ambari.server.state.Alert.MAX_ALERT_TEXT_SIZE])) + "alert text";
        alert.setText(shortText);
        org.junit.Assert.assertEquals(shortText.length(), alert.getText().length());
        org.junit.Assert.assertEquals(shortText, alert.getText());
        alert.setText(longText);
        org.junit.Assert.assertFalse(shortText.length() == alert.getText().length());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.Alert.MAX_ALERT_TEXT_SIZE, alert.getText().length());
        org.junit.Assert.assertTrue(alert.getText().startsWith("Not so short"));
        org.junit.Assert.assertTrue(alert.getText().endsWith("alert text"));
        org.junit.Assert.assertTrue(alert.getText().contains("…"));
    }
}