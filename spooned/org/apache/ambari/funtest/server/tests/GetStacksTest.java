package org.apache.ambari.funtest.server.tests;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
public class GetStacksTest extends org.apache.ambari.funtest.server.tests.ServerTestBase {
    @org.junit.Test
    public void testServerStatus() throws java.io.IOException {
        java.lang.String stacksPath = "/api/v1/stacks";
        java.lang.String stacksUrl = java.lang.String.format(org.apache.ambari.funtest.server.tests.ServerTestBase.SERVER_URL_FORMAT, org.apache.ambari.funtest.server.tests.ServerTestBase.serverPort) + stacksPath;
        org.apache.http.impl.client.CloseableHttpClient httpClient = org.apache.http.impl.client.HttpClients.createDefault();
        org.apache.http.client.methods.HttpGet httpGet = new org.apache.http.client.methods.HttpGet(stacksUrl);
        httpGet.addHeader("Authorization", org.apache.ambari.funtest.server.tests.ServerTestBase.getBasicAdminAuthentication());
        httpGet.addHeader("X-Requested-By", "ambari");
        try {
            org.apache.http.HttpResponse httpResponse = httpClient.execute(httpGet);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            org.junit.Assert.assertEquals(HttpStatus.SC_OK, statusCode);
            org.apache.http.HttpEntity entity = httpResponse.getEntity();
            java.lang.String responseBody = (entity != null) ? org.apache.http.util.EntityUtils.toString(entity) : null;
            org.junit.Assert.assertTrue(responseBody != null);
            com.google.gson.JsonElement jsonElement = new com.google.gson.JsonParser().parse(new com.google.gson.stream.JsonReader(new java.io.StringReader(responseBody)));
            org.junit.Assert.assertTrue(jsonElement != null);
            com.google.gson.JsonObject jsonObject = jsonElement.getAsJsonObject();
            org.junit.Assert.assertTrue(jsonObject.has("items"));
            com.google.gson.JsonArray stacksArray = jsonObject.get("items").getAsJsonArray();
            org.junit.Assert.assertTrue(stacksArray.size() > 0);
        } finally {
            httpClient.close();
        }
    }
}