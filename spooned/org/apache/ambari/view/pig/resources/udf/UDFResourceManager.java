package org.apache.ambari.view.pig.resources.udf;
public class UDFResourceManager extends org.apache.ambari.view.pig.resources.PersonalCRUDResourceManager<org.apache.ambari.view.pig.resources.udf.models.UDF> {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.view.pig.resources.udf.UDFResourceManager.class);

    public UDFResourceManager(org.apache.ambari.view.ViewContext context) {
        super(org.apache.ambari.view.pig.resources.udf.models.UDF.class, context);
    }
}