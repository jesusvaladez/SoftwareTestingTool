package org.apache.ambari.server.orm.helpers;
public class SQLOperations {
    public interface BatchOperation<T> {
        int chunk(java.util.Collection<T> chunk, int currentBatch, int totalBatches, int totalSize);
    }

    public static <T> int batch(java.util.Collection<T> collection, int batchSize, org.apache.ambari.server.orm.helpers.SQLOperations.BatchOperation<T> callback) {
        if (((collection == null) || collection.isEmpty()) || (batchSize == 0)) {
            return 0;
        }
        int totalSize = collection.size();
        int totalChunks = ((int) (java.lang.Math.ceil(((float) (totalSize)) / batchSize)));
        int currentChunk = 0;
        int resultSum = 0;
        for (java.util.Collection<T> chunk : com.google.common.collect.Iterables.partition(collection, batchSize)) {
            currentChunk += 1;
            resultSum += callback.chunk(chunk, currentChunk, totalChunks, totalSize);
        }
        return resultSum;
    }
}