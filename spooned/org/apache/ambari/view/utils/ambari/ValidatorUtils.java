package org.apache.ambari.view.utils.ambari;
import org.apache.commons.validator.routines.RegexValidator;
import org.apache.commons.validator.routines.UrlValidator;
public class ValidatorUtils {
    public static boolean validateHdfsURL(java.lang.String webhdfsUrl) {
        java.lang.String[] schemes = new java.lang.String[]{ "webhdfs", "hdfs", "s3", "wasb", "swebhdfs", "adl" };
        return org.apache.ambari.view.utils.ambari.ValidatorUtils.validateURL(webhdfsUrl, schemes);
    }

    public static boolean validateHttpURL(java.lang.String webhdfsUrl) {
        java.lang.String[] schemes = new java.lang.String[]{ "http", "https" };
        return org.apache.ambari.view.utils.ambari.ValidatorUtils.validateURL(webhdfsUrl, schemes);
    }

    public static boolean validateURL(java.lang.String webhdfsUrl, java.lang.String[] schemes) {
        org.apache.commons.validator.routines.RegexValidator authority = new org.apache.commons.validator.routines.RegexValidator(".*");
        org.apache.commons.validator.routines.UrlValidator urlValidator = new org.apache.commons.validator.routines.UrlValidator(schemes, authority, org.apache.commons.validator.routines.UrlValidator.ALLOW_LOCAL_URLS);
        return urlValidator.isValid(webhdfsUrl);
    }
}