package org.apache.ambari.server.state.kerberos;
import org.apache.commons.lang.StringUtils;
@com.google.inject.Singleton
public class VariableReplacementHelper {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.state.kerberos.VariableReplacementHelper.class);

    private static final java.util.regex.Pattern PATTERN_VARIABLE = java.util.regex.Pattern.compile("\\$\\{(?:([\\w\\-\\.]+)/)?([\\w\\-\\./]+)(?:\\s*\\|\\s*(.+?))?\\}");

    private static final java.util.regex.Pattern PATTERN_FUNCTION = java.util.regex.Pattern.compile("(\\w+)\\((.*?)\\)");

    private static final java.util.Map<java.lang.String, org.apache.ambari.server.state.kerberos.VariableReplacementHelper.Function> FUNCTIONS = new java.util.HashMap<java.lang.String, org.apache.ambari.server.state.kerberos.VariableReplacementHelper.Function>() {
        {
            put("each", new org.apache.ambari.server.state.kerberos.VariableReplacementHelper.EachFunction());
            put("toLower", new org.apache.ambari.server.state.kerberos.VariableReplacementHelper.ToLowerFunction());
            put("replace", new org.apache.ambari.server.state.kerberos.VariableReplacementHelper.ReplaceValue());
            put("append", new org.apache.ambari.server.state.kerberos.VariableReplacementHelper.AppendFunction());
            put("principalPrimary", new org.apache.ambari.server.state.kerberos.VariableReplacementHelper.PrincipalPrimary());
            put("stripPort", new org.apache.ambari.server.state.kerberos.VariableReplacementHelper.StripPort());
        }
    };

    public java.lang.String replaceVariables(java.lang.String value, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> replacementsMap) throws org.apache.ambari.server.AmbariRuntimeException {
        if (((value != null) && (replacementsMap != null)) && (!replacementsMap.isEmpty())) {
            int count = 0;
            boolean replacementPerformed;
            do {
                if ((++count) > 1000) {
                    throw new org.apache.ambari.server.AmbariRuntimeException(java.lang.String.format("Circular reference found while replacing variables in %s", value));
                }
                java.util.regex.Matcher matcher = org.apache.ambari.server.state.kerberos.VariableReplacementHelper.PATTERN_VARIABLE.matcher(value);
                java.lang.StringBuffer sb = new java.lang.StringBuffer();
                replacementPerformed = false;
                while (matcher.find()) {
                    java.lang.String type = matcher.group(1);
                    java.lang.String name = matcher.group(2);
                    java.lang.String function = matcher.group(3);
                    if ((name != null) && (!name.isEmpty())) {
                        java.util.Map<java.lang.String, java.lang.String> replacements = (type == null) ? replacementsMap.get("") : replacementsMap.get(type);
                        if ((replacements == null) || (replacements.get(name) == null)) {
                            continue;
                        }
                        java.lang.String replacement = replacements.get(name);
                        if (function != null) {
                            replacement = applyReplacementFunction(function, replacement, replacementsMap);
                        }
                        matcher.appendReplacement(sb, replacement.replace("\\", "\\\\").replace("$", "\\$"));
                        replacementPerformed = true;
                    }
                } 
                matcher.appendTail(sb);
                value = sb.toString();
            } while (replacementPerformed );
        }
        return value;
    }

    private java.lang.String applyReplacementFunction(java.lang.String function, java.lang.String replacement, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> replacementsMap) {
        if (function != null) {
            java.util.regex.Matcher matcher = org.apache.ambari.server.state.kerberos.VariableReplacementHelper.PATTERN_FUNCTION.matcher(function);
            if (matcher.matches()) {
                java.lang.String name = matcher.group(1);
                if (name != null) {
                    org.apache.ambari.server.state.kerberos.VariableReplacementHelper.Function f = org.apache.ambari.server.state.kerberos.VariableReplacementHelper.FUNCTIONS.get(name);
                    if (f != null) {
                        java.lang.String args = matcher.group(2);
                        java.lang.String[] argsList = args.split("(?<!\\\\),");
                        for (int i = 0; i < argsList.length; i++) {
                            argsList[i] = argsList[i].trim().replace("\\,", ",");
                        }
                        return f.perform(argsList, replacement, replacementsMap);
                    }
                }
            }
        }
        return replacement;
    }

    private interface Function {
        java.lang.String perform(java.lang.String[] args, java.lang.String data, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> replacementsMap);
    }

    private static class EachFunction implements org.apache.ambari.server.state.kerberos.VariableReplacementHelper.Function {
        @java.lang.Override
        public java.lang.String perform(java.lang.String[] args, java.lang.String data, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> replacementsMap) {
            if ((args == null) || (args.length != 3)) {
                throw new java.lang.IllegalArgumentException("Invalid number of arguments encountered");
            }
            if (data != null) {
                java.lang.StringBuilder builder = new java.lang.StringBuilder();
                java.lang.String pattern = args[0];
                java.lang.String concatDelimiter = args[1];
                java.lang.String dataDelimiter = args[2];
                java.lang.String[] items = data.split(dataDelimiter);
                for (java.lang.String item : items) {
                    if (builder.length() > 0) {
                        builder.append(concatDelimiter);
                    }
                    builder.append(java.lang.String.format(pattern, item));
                }
                return builder.toString();
            }
            return "";
        }
    }

    private static class ReplaceValue implements org.apache.ambari.server.state.kerberos.VariableReplacementHelper.Function {
        @java.lang.Override
        public java.lang.String perform(java.lang.String[] args, java.lang.String data, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> replacementsMap) {
            if ((args == null) || (args.length != 2)) {
                throw new java.lang.IllegalArgumentException("Invalid number of arguments encountered");
            }
            if (data != null) {
                java.lang.StringBuffer builder = new java.lang.StringBuffer();
                java.lang.String regex = args[0];
                java.lang.String replacement = args[1];
                java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
                java.util.regex.Matcher matcher = pattern.matcher(data);
                while (matcher.find()) {
                    matcher.appendReplacement(builder, replacement);
                } 
                matcher.appendTail(builder);
                return builder.toString();
            }
            return "";
        }
    }

    private static class ToLowerFunction implements org.apache.ambari.server.state.kerberos.VariableReplacementHelper.Function {
        @java.lang.Override
        public java.lang.String perform(java.lang.String[] args, java.lang.String data, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> replacementsMap) {
            if (data != null) {
                return data.toLowerCase();
            }
            return "";
        }
    }

    private static class AppendFunction implements org.apache.ambari.server.state.kerberos.VariableReplacementHelper.Function {
        @java.lang.Override
        public java.lang.String perform(java.lang.String[] args, java.lang.String data, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> replacementsMap) {
            if ((args == null) || (args.length != 3)) {
                java.lang.String message = "Invalid number of arguments encountered while processing the 'append' variable replacement function.  The following arguments are expected:" + (("\n\t- Configuration specification used to get the initial value" + "\n\t- Delimiter used for parsing the initial value and appending new values") + "\n\t- A flag to indicate whether values should be unique (\'true\') or not (\'false\')");
                org.apache.ambari.server.state.kerberos.VariableReplacementHelper.LOG.error(message);
                throw new java.lang.IllegalArgumentException(message);
            }
            java.lang.String configurationSpec = args[0];
            java.lang.String concatDelimiter = args[1];
            boolean uniqueOnly = java.lang.Boolean.parseBoolean(args[2]);
            java.lang.String sourceData = getSourceData(replacementsMap, configurationSpec);
            java.util.Collection<java.lang.String> sourceItems = parseItems(sourceData, concatDelimiter);
            java.util.Collection<java.lang.String> dataItems = parseItems(data, concatDelimiter);
            java.util.Collection<java.lang.String> items = new java.util.ArrayList<>();
            if (uniqueOnly) {
                for (java.lang.String item : sourceItems) {
                    if (!items.contains(item)) {
                        items.add(item);
                    }
                }
                for (java.lang.String item : dataItems) {
                    if (!items.contains(item)) {
                        items.add(item);
                    }
                }
            } else {
                items.addAll(sourceItems);
                items.addAll(dataItems);
            }
            return org.apache.commons.lang.StringUtils.join(items, concatDelimiter);
        }

        private java.util.Collection<java.lang.String> parseItems(java.lang.String delimitedString, java.lang.String concatDelimiter) {
            java.util.Collection<java.lang.String> items = new java.util.ArrayList<>();
            if (!org.apache.commons.lang.StringUtils.isEmpty(delimitedString)) {
                for (java.lang.String item : delimitedString.split(concatDelimiter)) {
                    item = item.trim();
                    if (!item.isEmpty()) {
                        items.add(item);
                    }
                }
            }
            return items;
        }

        private java.lang.String getSourceData(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> replacementsMap, java.lang.String configurationSpec) {
            java.lang.String sourceData = null;
            if (((replacementsMap != null) && (!replacementsMap.isEmpty())) && (!org.apache.commons.lang.StringUtils.isEmpty(configurationSpec))) {
                java.lang.String[] parts = configurationSpec.split("/");
                java.lang.String type = null;
                java.lang.String name = null;
                if (parts.length == 2) {
                    type = parts[0];
                    name = parts[1];
                } else if (parts.length == 1) {
                    name = parts[0];
                }
                if (!org.apache.commons.lang.StringUtils.isEmpty(name)) {
                    java.util.Map<java.lang.String, java.lang.String> replacements;
                    if (type == null) {
                        replacements = replacementsMap.get("");
                    } else {
                        replacements = replacementsMap.get(type);
                    }
                    if (replacements != null) {
                        sourceData = replacements.get(name);
                    }
                }
            }
            return sourceData;
        }
    }

    private static class PrincipalPrimary implements org.apache.ambari.server.state.kerberos.VariableReplacementHelper.Function {
        @java.lang.Override
        public java.lang.String perform(java.lang.String[] args, java.lang.String data, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> replacementsMap) {
            if (data == null) {
                return null;
            }
            if (data.contains("/")) {
                return data.split("/")[0];
            } else if (data.contains("@")) {
                return data.split("@")[0];
            } else {
                return data;
            }
        }
    }

    private static class StripPort implements org.apache.ambari.server.state.kerberos.VariableReplacementHelper.Function {
        @java.lang.Override
        public java.lang.String perform(java.lang.String[] args, java.lang.String data, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> replacementsMap) {
            if (data == null) {
                return null;
            }
            final int semicolonIndex = data.indexOf(":");
            return semicolonIndex == (-1) ? data : data.substring(0, semicolonIndex);
        }
    }
}