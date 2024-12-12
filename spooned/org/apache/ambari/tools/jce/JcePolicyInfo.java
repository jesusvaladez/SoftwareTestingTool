package org.apache.ambari.tools.jce;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.UnrecognizedOptionException;
public class JcePolicyInfo {
    public static void main(java.lang.String[] args) throws java.lang.Exception {
        try {
            boolean showHelp = true;
            org.apache.commons.cli.CommandLine cli = new org.apache.commons.cli.DefaultParser().parse(org.apache.ambari.tools.jce.JcePolicyInfo.options(), args);
            if (cli.hasOption("lc")) {
                org.apache.ambari.tools.jce.JcePolicyInfo.listCiphers();
                showHelp = false;
            }
            if (cli.hasOption("tu")) {
                org.apache.ambari.tools.jce.JcePolicyInfo.testUnlimitedKeyJCEPolicy();
                showHelp = false;
            }
            if (showHelp) {
                org.apache.ambari.tools.jce.JcePolicyInfo.printHelp(null);
            }
        } catch (org.apache.commons.cli.UnrecognizedOptionException e) {
            org.apache.ambari.tools.jce.JcePolicyInfo.printHelp(e);
        }
    }

    private static void printHelp(org.apache.commons.cli.UnrecognizedOptionException exception) {
        org.apache.commons.cli.HelpFormatter helpFormatter = new org.apache.commons.cli.HelpFormatter();
        if (exception == null) {
            helpFormatter.printHelp("jcepolicyinfo [options]", org.apache.ambari.tools.jce.JcePolicyInfo.options());
        } else {
            helpFormatter.printHelp("jcepolicyinfo [options]", exception.getLocalizedMessage(), org.apache.ambari.tools.jce.JcePolicyInfo.options(), null);
        }
        java.lang.System.exit(1);
    }

    private static org.apache.commons.cli.Options options() {
        return new org.apache.commons.cli.Options().addOption(org.apache.commons.cli.Option.builder("h").longOpt("help").desc("print help").build()).addOption(org.apache.commons.cli.Option.builder("tu").longOpt("test_unlimited").desc("Test's the policy for unlimited key encryption").hasArg(false).argName("tu").build()).addOption(org.apache.commons.cli.Option.builder("lc").longOpt("list_ciphers").desc("List the ciphers allowed by the policy").hasArg(false).argName("lc").build());
    }

    private static void testUnlimitedKeyJCEPolicy() {
        java.lang.System.out.print("Unlimited Key JCE Policy: ");
        try {
            boolean unlimited = javax.crypto.Cipher.getMaxAllowedKeyLength("RC5") >= 256;
            java.lang.System.out.println(unlimited);
            java.lang.System.exit(unlimited ? 0 : 1);
        } catch (java.security.NoSuchAlgorithmException e) {
            java.lang.System.out.println("unknown [error]");
            java.lang.System.exit(-1);
        }
    }

    private static void listCiphers() {
        java.lang.System.out.println("Available ciphers:");
        for (java.security.Provider provider : java.security.Security.getProviders()) {
            java.lang.String providerName = provider.getName();
            for (java.security.Provider.Service service : provider.getServices()) {
                java.lang.String algorithmName = service.getAlgorithm();
                if ("Cipher".equalsIgnoreCase(service.getType())) {
                    try {
                        long keylength = javax.crypto.Cipher.getMaxAllowedKeyLength(algorithmName);
                        java.lang.System.out.print('\t');
                        java.lang.System.out.print(providerName.toLowerCase());
                        java.lang.System.out.print('.');
                        java.lang.System.out.print(algorithmName.toLowerCase());
                        java.lang.System.out.print(": ");
                        java.lang.System.out.println(keylength);
                    } catch (java.security.NoSuchAlgorithmException e) {
                    }
                }
            }
        }
    }
}