package org.apache.ambari.server.credentialapi;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.security.alias.CredentialProvider;
import org.apache.hadoop.security.alias.CredentialProviderFactory;
import org.apache.hadoop.security.alias.CredentialShell;
import org.apache.hadoop.security.alias.JavaKeyStoreProvider;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
public class CredentialUtil extends org.apache.hadoop.conf.Configured implements org.apache.hadoop.util.Tool {
    private static final java.lang.String COMMANDS = ((((((((((("   [--help]\n" + "   [") + org.apache.ambari.server.credentialapi.CredentialUtil.CreateCommand.USAGE) + "]\n") + "   [") + org.apache.ambari.server.credentialapi.CredentialUtil.DeleteCommand.USAGE) + "]\n") + "   [") + org.apache.ambari.server.credentialapi.CredentialUtil.ListCommand.USAGE) + "]\n") + "   [") + org.apache.ambari.server.credentialapi.CredentialUtil.GetCommand.USAGE) + "]\n";

    public static final java.lang.String jceksPrefix = org.apache.hadoop.security.alias.JavaKeyStoreProvider.SCHEME_NAME + "://file";

    public static final java.lang.String localJceksPrefix = "localjceks://file";

    private java.lang.String alias = null;

    private java.lang.String value = null;

    protected org.apache.hadoop.security.alias.CredentialProvider provider;

    private boolean overwrite = true;

    private boolean interactive = true;

    private org.apache.ambari.server.credentialapi.CredentialUtil.Command command = null;

    public static void main(java.lang.String[] args) throws java.lang.Exception {
        int res = org.apache.hadoop.util.ToolRunner.run(new org.apache.hadoop.conf.Configuration(), new org.apache.ambari.server.credentialapi.CredentialUtil(), args);
        java.lang.System.exit(res);
    }

    @java.lang.Override
    public int run(java.lang.String[] args) throws java.lang.Exception {
        int exitCode = 1;
        for (int i = 0; i < args.length; ++i) {
            if (args[i].equals("create")) {
                if (i == (args.length - 1)) {
                    return 1;
                }
                command = new org.apache.ambari.server.credentialapi.CredentialUtil.CreateCommand();
                alias = args[++i];
                if (alias.equals("-h") || alias.equals("-help")) {
                    printUsage();
                    return 0;
                }
            } else if (args[i].equals("get")) {
                if (i == (args.length - 1)) {
                    return 1;
                }
                command = new org.apache.ambari.server.credentialapi.CredentialUtil.GetCommand();
                alias = args[++i];
                if (alias.equals("-h") || alias.equals("-help")) {
                    printUsage();
                    return 0;
                }
            } else if (args[i].equals("delete")) {
                if (i == (args.length - 1)) {
                    printUsage();
                    return 1;
                }
                command = new org.apache.ambari.server.credentialapi.CredentialUtil.DeleteCommand();
                alias = args[++i];
                if (alias.equals("-help")) {
                    printUsage();
                    return 0;
                }
            } else if (args[i].equals("list")) {
                if (i < (args.length - 1)) {
                    alias = args[i + 1];
                }
                command = new org.apache.ambari.server.credentialapi.CredentialUtil.ListCommand();
                if (alias.equals("-h") || alias.equals("-help")) {
                    printUsage();
                    return 0;
                }
                alias = "not required";
            } else if (args[i].equals("-provider")) {
                if (i == (args.length - 1)) {
                    return 1;
                }
                java.lang.String providerPath = org.apache.ambari.server.credentialapi.CredentialUtil.getNormalizedPath(args[++i]);
                getConf().set(CredentialProviderFactory.CREDENTIAL_PROVIDER_PATH, providerPath);
                provider = getCredentialProvider();
            } else if (args[i].equals("-f") || args[i].equals("-force")) {
                interactive = false;
                overwrite = true;
            } else if (args[i].equals("-n")) {
                interactive = false;
                overwrite = false;
            } else if (args[i].equals("-v") || args[i].equals("-value")) {
                value = args[++i];
            } else if (args[i].equals("-h") || args[i].equals("-help")) {
                printUsage();
                return 0;
            } else {
                printUsage();
                org.apache.hadoop.util.ToolRunner.printGenericCommandUsage(java.lang.System.err);
                return 1;
            }
        }
        if (command == null) {
            printUsage();
        } else if (command.validate()) {
            exitCode = command.execute();
        }
        return exitCode;
    }

    protected void printUsage() {
        java.lang.System.out.println(getUsagePrefix() + org.apache.ambari.server.credentialapi.CredentialUtil.COMMANDS);
        if (command != null) {
            java.lang.System.out.println(command.getUsage());
        } else {
            java.lang.System.out.println("=========================================================" + "======");
            java.lang.System.out.println((org.apache.ambari.server.credentialapi.CredentialUtil.CreateCommand.USAGE + ":\n\n") + org.apache.ambari.server.credentialapi.CredentialUtil.CreateCommand.DESC);
            java.lang.System.out.println("=========================================================" + "======");
            java.lang.System.out.println((org.apache.ambari.server.credentialapi.CredentialUtil.DeleteCommand.USAGE + ":\n\n") + org.apache.ambari.server.credentialapi.CredentialUtil.DeleteCommand.DESC);
            java.lang.System.out.println("=========================================================" + "======");
            java.lang.System.out.println((org.apache.ambari.server.credentialapi.CredentialUtil.ListCommand.USAGE + ":\n\n") + org.apache.ambari.server.credentialapi.CredentialUtil.ListCommand.DESC);
            java.lang.System.out.println("=========================================================" + "======");
            java.lang.System.out.println((org.apache.ambari.server.credentialapi.CredentialUtil.GetCommand.USAGE + ":\n\n") + org.apache.ambari.server.credentialapi.CredentialUtil.GetCommand.DESC);
        }
    }

    protected java.lang.String getUsagePrefix() {
        return "Usage: ";
    }

    private static java.lang.String getNormalizedPath(java.lang.String providerPath) {
        if (providerPath != null) {
            java.lang.String jceksPath;
            if (providerPath.startsWith("/")) {
                providerPath = providerPath.substring(1);
            }
            jceksPath = org.apache.commons.lang.StringUtils.lowerCase(providerPath.trim());
            if ((!jceksPath.startsWith(org.apache.commons.lang.StringUtils.lowerCase(org.apache.ambari.server.credentialapi.CredentialUtil.jceksPrefix))) && (!jceksPath.startsWith(org.apache.ambari.server.credentialapi.CredentialUtil.localJceksPrefix))) {
                providerPath = (org.apache.ambari.server.credentialapi.CredentialUtil.jceksPrefix + "/") + providerPath;
            }
        }
        return providerPath;
    }

    private org.apache.hadoop.security.alias.CredentialProvider getCredentialProvider() {
        org.apache.hadoop.security.alias.CredentialProvider provider = null;
        java.util.List<org.apache.hadoop.security.alias.CredentialProvider> providers;
        try {
            providers = org.apache.hadoop.security.alias.CredentialProviderFactory.getProviders(getConf());
            provider = providers.get(0);
        } catch (java.io.IOException e) {
            e.printStackTrace(java.lang.System.err);
        }
        return provider;
    }

    private abstract class Command {
        public boolean validate() {
            boolean rc = true;
            if ((alias == null) || alias.isEmpty()) {
                java.lang.System.out.println("There is no alias specified. Please provide the" + "mandatory <alias>. See the usage description with -help.");
                rc = false;
            }
            if (provider == null) {
                java.lang.System.out.println("There are no valid CredentialProviders configured." + (("\nCredential will not be created.\n" + "Consider using the -provider option to indicate the provider") + " to use."));
                rc = false;
            }
            return rc;
        }

        public abstract java.lang.String getUsage();

        public abstract int execute() throws java.lang.Exception;
    }

    private class GetCommand extends org.apache.ambari.server.credentialapi.CredentialUtil.Command {
        public static final java.lang.String USAGE = "get <alias> [-provider provider-path]";

        public static final java.lang.String DESC = "The get subcommand gets the credential for the specified alias\n" + "from the provider specified through the -provider argument.\n";

        @java.lang.Override
        public int execute() throws java.io.IOException {
            int exitCode = 0;
            try {
                java.lang.String credential = getCredential();
                if (credential == null) {
                    exitCode = 1;
                } else {
                    java.lang.System.out.println(credential);
                }
            } catch (java.io.IOException ex) {
                java.lang.System.out.println(("Cannot get the credential for the specified alias." + ": ") + ex.getMessage());
                throw ex;
            }
            return exitCode;
        }

        private java.lang.String getCredential() throws java.io.IOException {
            java.lang.String credential = null;
            org.apache.hadoop.security.alias.CredentialProvider.CredentialEntry credEntry = provider.getCredentialEntry(alias);
            if (credEntry != null) {
                char[] password = credEntry.getCredential();
                if (password != null) {
                    credential = java.lang.String.valueOf(password);
                }
            }
            return credential;
        }

        @java.lang.Override
        public java.lang.String getUsage() {
            return (org.apache.ambari.server.credentialapi.CredentialUtil.GetCommand.USAGE + ":\n\n") + org.apache.ambari.server.credentialapi.CredentialUtil.GetCommand.DESC;
        }
    }

    private class CreateCommand extends org.apache.ambari.server.credentialapi.CredentialUtil.Command {
        public static final java.lang.String USAGE = "create <alias> [-value credential] [-provider provider-path] [-f | -n]";

        public static final java.lang.String DESC = "The create subcommand creates a new credential or overwrites\n" + (((("an existing credential for the name specified\n" + "as the <alias> argument within the provider indicated through\n") + "the -provider argument. The command asks for confirmation to\n") + "overwrite the existing credential unless the -f option is specified.\n") + "Specify -n to not overwrite if the credential exists.\nThe option specified last wins.");

        @java.lang.Override
        public int execute() throws java.lang.Exception {
            int exitCode = 0;
            org.apache.hadoop.security.alias.CredentialProvider.CredentialEntry credEntry = provider.getCredentialEntry(alias);
            if (credEntry != null) {
                if (interactive) {
                    overwrite = org.apache.hadoop.util.ToolRunner.confirmPrompt(((("You are about to OVERWRITE the credential " + alias) + " from CredentialProvider ") + provider) + ". Continue? ");
                }
                if (overwrite) {
                    org.apache.ambari.server.credentialapi.CredentialUtil.DeleteCommand deleteCommand = new org.apache.ambari.server.credentialapi.CredentialUtil.DeleteCommand();
                    exitCode = deleteCommand.execute();
                } else {
                    return 0;
                }
            }
            if (exitCode == 0) {
                exitCode = createCredential();
            }
            return exitCode;
        }

        @java.lang.Override
        public java.lang.String getUsage() {
            return (org.apache.ambari.server.credentialapi.CredentialUtil.CreateCommand.USAGE + ":\n\n") + org.apache.ambari.server.credentialapi.CredentialUtil.CreateCommand.DESC;
        }

        private int createCredential() throws java.lang.Exception {
            int exitCode;
            java.util.List<java.lang.String> args = new java.util.ArrayList<>();
            args.add("create");
            args.add(alias);
            if (value != null) {
                args.add("-value");
                args.add(value);
            }
            java.lang.String[] toolArgs = args.toArray(new java.lang.String[args.size()]);
            exitCode = org.apache.hadoop.util.ToolRunner.run(getConf(), new org.apache.hadoop.security.alias.CredentialShell(), toolArgs);
            return exitCode;
        }
    }

    private class DeleteCommand extends org.apache.ambari.server.credentialapi.CredentialUtil.Command {
        public static final java.lang.String USAGE = "delete <alias> [-f] [-provider provider-path]";

        public static final java.lang.String DESC = "The delete subcommand deletes the credential specified\n" + (("as the <alias> argument from within the provider indicated\n" + "through the -provider argument. The command asks for\n") + "confirmation unless the -f option is specified.");

        @java.lang.Override
        public int execute() throws java.lang.Exception {
            int exitCode;
            java.util.List<java.lang.String> args = new java.util.ArrayList<>();
            args.add("delete");
            args.add(alias);
            if (!interactive) {
                args.add("-f");
            }
            java.lang.String[] toolArgs = args.toArray(new java.lang.String[args.size()]);
            exitCode = org.apache.hadoop.util.ToolRunner.run(getConf(), new org.apache.hadoop.security.alias.CredentialShell(), toolArgs);
            return exitCode;
        }

        @java.lang.Override
        public java.lang.String getUsage() {
            return (org.apache.ambari.server.credentialapi.CredentialUtil.DeleteCommand.USAGE + ":\n\n") + org.apache.ambari.server.credentialapi.CredentialUtil.DeleteCommand.DESC;
        }
    }

    private class ListCommand extends org.apache.ambari.server.credentialapi.CredentialUtil.Command {
        public static final java.lang.String USAGE = "list [-provider provider-path]";

        public static final java.lang.String DESC = "The list subcommand displays the aliases contained within \n" + ("a particular provider - as configured in core-site.xml or\n " + "indicated through the -provider argument.");

        @java.lang.Override
        public int execute() throws java.lang.Exception {
            int exitCode;
            java.util.List<java.lang.String> args = new java.util.ArrayList<>();
            args.add("list");
            java.lang.String[] toolArgs = args.toArray(new java.lang.String[args.size()]);
            exitCode = org.apache.hadoop.util.ToolRunner.run(getConf(), new org.apache.hadoop.security.alias.CredentialShell(), toolArgs);
            return exitCode;
        }

        @java.lang.Override
        public java.lang.String getUsage() {
            return (org.apache.ambari.server.credentialapi.CredentialUtil.ListCommand.USAGE + ":\n\n") + org.apache.ambari.server.credentialapi.CredentialUtil.ListCommand.DESC;
        }
    }
}