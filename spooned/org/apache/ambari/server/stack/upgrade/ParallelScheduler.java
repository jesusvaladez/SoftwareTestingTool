package org.apache.ambari.server.stack.upgrade;
import javax.xml.bind.annotation.XmlElement;
public class ParallelScheduler {
    public static final int DEFAULT_MAX_DEGREE_OF_PARALLELISM = java.lang.Integer.MAX_VALUE;

    @javax.xml.bind.annotation.XmlElement(name = "max-degree-of-parallelism")
    public int maxDegreeOfParallelism = org.apache.ambari.server.stack.upgrade.ParallelScheduler.DEFAULT_MAX_DEGREE_OF_PARALLELISM;
}