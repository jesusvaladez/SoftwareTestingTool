package org.apache.ambari.server.orm.helpers;
public class SQLOperationsTest {
    @org.junit.Before
    public void setup() throws java.lang.Exception {
    }

    @org.junit.Test
    public void testBatchOperation() {
        final java.util.List<java.lang.Integer> testCollection = new java.util.ArrayList<>();
        final int collectionSize = 150;
        final int batchSize = 10;
        final int totalExpectedBatches = ((int) (java.lang.Math.ceil(((float) (collectionSize)) / batchSize)));
        for (int i = 0; i < collectionSize; i++) {
            testCollection.add(i);
        }
        int processedItems = org.apache.ambari.server.orm.helpers.SQLOperations.batch(testCollection, batchSize, (chunk, currentBatch, totalBatches, totalSize) -> {
            junit.framework.Assert.assertTrue(chunk.size() <= batchSize);
            junit.framework.Assert.assertEquals(totalExpectedBatches, totalBatches);
            return chunk.size();
        });
        junit.framework.Assert.assertEquals(collectionSize, processedItems);
    }
}