package org.apache.ambari.server.orm.db;
public class DDLTestUtils {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.orm.db.DDLTestUtils.class);

    private static final java.util.regex.Pattern CommentLine = java.util.regex.Pattern.compile("^\\s*--.*");

    private static final java.util.regex.Pattern EmptyLine = java.util.regex.Pattern.compile("^\\s*$");

    private static final java.util.regex.Pattern CommitLine = java.util.regex.Pattern.compile("^\\s*(go|commit).*$");

    private static final java.util.regex.Pattern CreateTable = java.util.regex.Pattern.compile("^\\s*create\\s+table.*$");

    private static final java.util.regex.Pattern AlterTable = java.util.regex.Pattern.compile("^\\s*alter\\s+table.*$");

    private static final java.util.regex.Pattern CreateIndex = java.util.regex.Pattern.compile("^\\s*create\\s+index.*$");

    private static final java.util.regex.Pattern EndStatement = java.util.regex.Pattern.compile("(.*\\;)\\s*$");

    private static final java.util.regex.Pattern TableName = java.util.regex.Pattern.compile("^\\s*create table\\s+([\\`\\w\\.\\_\\`]+).*$");

    private static final java.util.regex.Pattern PK = java.util.regex.Pattern.compile("^.*constraint\\s+([\\w\\.\\_]+)\\s+primary\\s+key\\s*\\(([^\\)]+)\\).*$");

    private static final java.util.regex.Pattern PKClustered = java.util.regex.Pattern.compile("^.*constraint\\s+([\\w\\.\\_]+)\\s+primary\\s+key\\s+clustered\\s*\\(([^\\)]+)\\).*$");

    private static final java.util.regex.Pattern UQ = java.util.regex.Pattern.compile("^.*constraint\\s+([\\w\\.\\_]+)\\s+unique\\s*\\(([^\\)]+)\\).*$");

    private static final java.util.regex.Pattern FK = java.util.regex.Pattern.compile("^.*constraint\\s+([\\w\\.\\_]+)\\s+foreign\\s+key\\s*\\(([^\\)]*)\\)\\s*references\\s+([\\`\\w\\_\\.\\`]+)\\s*\\(([^\\)]+)\\).*$");

    private static final java.util.regex.Pattern Col = java.util.regex.Pattern.compile("^\\s*([\\`\\\"\\[\\]\\w\\.\\_]+)\\s+.*$");

    private static final java.util.regex.Pattern InnerList = java.util.regex.Pattern.compile("(\\([^\\(^\\)]+\\))");

    private static final java.util.regex.Pattern UnnamedPK = java.util.regex.Pattern.compile("^\\s*primary\\s+key[\\sclustered]*\\(([^\\)]+)\\).*$");

    private static final java.util.regex.Pattern UnnamedUQ = java.util.regex.Pattern.compile("^\\s*unique\\s*\\(([^\\)]+)\\).*$");

    private static final java.util.regex.Pattern UnnamedFK = java.util.regex.Pattern.compile("^\\s*foreign\\s+key\\s*\\(([^\\)]+)\\)\\s*references\\s+([\\w\\_\\.]+)\\s*\\(([^\\)]+)\\).*$");

    private static final java.util.regex.Pattern PKColumn = java.util.regex.Pattern.compile("^.*[\\w\\.\\_]+\\s.*primary\\s+key[\\sclustered\\,\\;\\)]*$");

    private static final java.util.regex.Pattern UQColumn = java.util.regex.Pattern.compile("^\\s*[\\w\\.\\_]+\\s.*unique[\\s\\;\\,\\)]*$");

    private static final java.util.List<java.util.regex.Pattern> CheckedUnnamedConstraints = com.google.common.collect.ImmutableList.of(org.apache.ambari.server.orm.db.DDLTestUtils.UnnamedPK);

    private static final java.util.List<java.util.regex.Pattern> UncheckedUnnamedConstraints = com.google.common.collect.ImmutableList.of(org.apache.ambari.server.orm.db.DDLTestUtils.UnnamedUQ, org.apache.ambari.server.orm.db.DDLTestUtils.UnnamedFK, org.apache.ambari.server.orm.db.DDLTestUtils.PKColumn, org.apache.ambari.server.orm.db.DDLTestUtils.UQColumn);

    private static final com.google.common.cache.LoadingCache<java.lang.String, org.apache.ambari.server.orm.db.DDL> ddlCache = com.google.common.cache.CacheBuilder.newBuilder().build(new com.google.common.cache.CacheLoader<java.lang.String, org.apache.ambari.server.orm.db.DDL>() {
        @java.lang.Override
        public org.apache.ambari.server.orm.db.DDL load(java.lang.String key) throws java.lang.Exception {
            return org.apache.ambari.server.orm.db.DDLTestUtils.loadDdl(key);
        }
    });

    public static final java.util.List<java.lang.String> DATABASES = com.google.common.collect.ImmutableList.of("Derby", "MySQL", "Oracle", "Postgres", "Postgres-EMBEDDED", "SQLAnywhere", "SQLServer");

    public static org.apache.ambari.server.orm.db.DDL getDdl(java.lang.String dbType) throws java.lang.Exception {
        return org.apache.ambari.server.orm.db.DDLTestUtils.ddlCache.get(dbType);
    }

    private static java.net.URL getDdlUrl(java.lang.String dbType) {
        return com.google.common.io.Resources.getResource(("Ambari-DDL-" + dbType) + "-CREATE.sql");
    }

    private static java.util.List<java.lang.String> loadFile(java.lang.String dbType) throws java.lang.Exception {
        java.util.List<java.lang.String> lines = com.google.common.io.Resources.readLines(org.apache.ambari.server.orm.db.DDLTestUtils.getDdlUrl(dbType), com.google.common.base.Charsets.UTF_8);
        java.util.List<java.lang.String> replaced = new java.util.ArrayList<>(lines.size());
        for (java.lang.String line : lines) {
            replaced.add(line.toLowerCase());
        }
        return replaced;
    }

    private static java.util.List<java.lang.String> groupStatements(java.util.List<java.lang.String> ddlFile) {
        java.util.List<java.lang.String> statements = new java.util.ArrayList<>();
        com.google.common.base.Optional<java.util.ArrayList<java.lang.String>> currentStmt = com.google.common.base.Optional.absent();
        for (java.lang.String line : ddlFile) {
            if ((org.apache.ambari.server.orm.db.DDLTestUtils.CommentLine.matcher(line).matches() || org.apache.ambari.server.orm.db.DDLTestUtils.EmptyLine.matcher(line).matches()) || org.apache.ambari.server.orm.db.DDLTestUtils.CommitLine.matcher(line).matches());else if ((org.apache.ambari.server.orm.db.DDLTestUtils.CreateTable.matcher(line).matches() || org.apache.ambari.server.orm.db.DDLTestUtils.AlterTable.matcher(line).matches()) || org.apache.ambari.server.orm.db.DDLTestUtils.CreateIndex.matcher(line).matches()) {
                if (currentStmt.isPresent())
                    throw new java.lang.IllegalStateException((("Unfinished statement: " + currentStmt.get()) + "\nnew statement: ") + line);

                currentStmt = com.google.common.base.Optional.of(new java.util.ArrayList<java.lang.String>());
                currentStmt.get().add(org.apache.ambari.server.orm.db.DDLTestUtils.stripComment(line));
                if (line.contains(";")) {
                    statements.add(com.google.common.base.Joiner.on(' ').join(currentStmt.get()));
                    currentStmt = com.google.common.base.Optional.absent();
                }
            } else if (currentStmt.isPresent() && org.apache.ambari.server.orm.db.DDLTestUtils.EndStatement.matcher(line).matches()) {
                currentStmt.get().add(org.apache.ambari.server.orm.db.DDLTestUtils.stripComment(line));
                statements.add(com.google.common.base.Joiner.on(' ').join(currentStmt.get()));
                currentStmt = com.google.common.base.Optional.absent();
            } else if (currentStmt.isPresent()) {
                currentStmt.get().add(org.apache.ambari.server.orm.db.DDLTestUtils.stripComment(line));
            }
        }
        return statements;
    }

    private static java.lang.String stripComment(java.lang.String line) {
        return line.contains("--") ? line.substring(0, line.indexOf("--")) : line;
    }

    private static java.util.Collection<java.lang.String> toColumns(java.lang.String cols) {
        java.util.List<java.lang.String> columns = new java.util.ArrayList<>();
        for (java.lang.String col : com.google.common.base.Splitter.on('|').split(cols)) {
            columns.add(org.apache.ambari.server.orm.db.DDLTestUtils.stripPrefixQuotationAndBrackets(col.trim()));
        }
        return columns;
    }

    private static java.lang.String stripPrefixQuotationAndBrackets(java.lang.String input) {
        java.lang.String output = input.replaceAll("[\\`\\\"\\[\\]]", "").replaceAll("[^\\.]*\\.", "");
        return output;
    }

    private static com.google.common.base.Optional<java.lang.String> firstMatchingGroup(java.util.regex.Pattern p, java.lang.String s) {
        java.util.regex.Matcher m = p.matcher(s);
        if (m.matches()) {
            return com.google.common.base.Optional.of(m.group(1));
        } else {
            return com.google.common.base.Optional.absent();
        }
    }

    private static java.util.Map<java.lang.String, org.apache.ambari.server.orm.db.Table> parseTableDefs(java.util.List<java.lang.String> statements) {
        java.util.List<java.lang.String> createTables = new java.util.ArrayList<>();
        for (java.lang.String stmt : statements) {
            if (stmt.matches(".*create\\s+table.*")) {
                java.lang.String content = stmt.substring(stmt.indexOf('(') + 1, stmt.lastIndexOf(')'));
                java.util.regex.Matcher m = org.apache.ambari.server.orm.db.DDLTestUtils.InnerList.matcher(content);
                while (m.find()) {
                    java.lang.String innerList = m.group();
                    stmt = stmt.replace(innerList, innerList.replaceAll("\\,", "|"));
                } 
                createTables.add(stmt);
            }
        }
        java.util.List<org.apache.ambari.server.orm.db.Table> tables = new java.util.ArrayList<>();
        for (java.lang.String ct : createTables) {
            java.lang.String tableName = org.apache.ambari.server.orm.db.DDLTestUtils.stripPrefixQuotationAndBrackets(org.apache.ambari.server.orm.db.DDLTestUtils.firstMatchingGroup(org.apache.ambari.server.orm.db.DDLTestUtils.TableName, ct).get());
            java.util.List<java.lang.String> columns = new java.util.ArrayList<>();
            com.google.common.base.Optional<org.apache.ambari.server.orm.db.SimpleConstraint> pk = com.google.common.base.Optional.absent();
            java.util.List<org.apache.ambari.server.orm.db.FKConstraint> fks = new java.util.ArrayList<>();
            java.util.List<org.apache.ambari.server.orm.db.SimpleConstraint> uqs = new java.util.ArrayList<>();
            final java.lang.String innerPart = ct.substring(ct.indexOf('(') + 1, ct.lastIndexOf(')'));
            for (java.lang.String definition : com.google.common.base.Splitter.on(',').split(innerPart)) {
                definition = definition.trim();
                org.apache.ambari.server.orm.db.DDLTestUtils.assertNounnamedConstraint(tableName, definition);
                java.util.regex.Matcher pkMatcher = org.apache.ambari.server.orm.db.DDLTestUtils.PK.matcher(definition);
                java.util.regex.Matcher pkClustMatcher = org.apache.ambari.server.orm.db.DDLTestUtils.PKClustered.matcher(definition);
                java.util.regex.Matcher unnamedPkMatcher = org.apache.ambari.server.orm.db.DDLTestUtils.UnnamedPK.matcher(definition);
                java.util.regex.Matcher pkColumnMatcher = org.apache.ambari.server.orm.db.DDLTestUtils.PKColumn.matcher(definition);
                java.util.regex.Matcher fkMatcher = org.apache.ambari.server.orm.db.DDLTestUtils.FK.matcher(definition);
                java.util.regex.Matcher uqMatcher = org.apache.ambari.server.orm.db.DDLTestUtils.UQ.matcher(definition);
                java.util.regex.Matcher unnamedFkMatcher = org.apache.ambari.server.orm.db.DDLTestUtils.UnnamedFK.matcher(definition);
                java.util.regex.Matcher unnamedUqMatcher = org.apache.ambari.server.orm.db.DDLTestUtils.UnnamedUQ.matcher(definition);
                java.util.regex.Matcher uqColumnMatcher = org.apache.ambari.server.orm.db.DDLTestUtils.UQColumn.matcher(definition);
                java.util.regex.Matcher colMatcher = org.apache.ambari.server.orm.db.DDLTestUtils.Col.matcher(definition);
                if (pkMatcher.matches()) {
                    pk = com.google.common.base.Optional.of(org.apache.ambari.server.orm.db.Constraint.pk(pkMatcher.group(1), org.apache.ambari.server.orm.db.DDLTestUtils.toColumns(pkMatcher.group(2))));
                } else if (pkMatcher.matches()) {
                    pk = com.google.common.base.Optional.of(org.apache.ambari.server.orm.db.Constraint.pk(org.apache.ambari.server.orm.db.DDLTestUtils.stripPrefixQuotationAndBrackets(pkMatcher.group(1)), org.apache.ambari.server.orm.db.DDLTestUtils.toColumns(pkMatcher.group(2))));
                } else if (pkClustMatcher.matches()) {
                    pk = com.google.common.base.Optional.of(org.apache.ambari.server.orm.db.Constraint.pk(org.apache.ambari.server.orm.db.DDLTestUtils.stripPrefixQuotationAndBrackets(pkClustMatcher.group(1)), org.apache.ambari.server.orm.db.DDLTestUtils.toColumns(pkClustMatcher.group(2))));
                } else if (unnamedPkMatcher.matches()) {
                    pk = com.google.common.base.Optional.of(org.apache.ambari.server.orm.db.Constraint.pk("<default>", org.apache.ambari.server.orm.db.DDLTestUtils.toColumns(unnamedPkMatcher.group(1))));
                } else if (fkMatcher.matches()) {
                    fks.add(org.apache.ambari.server.orm.db.Constraint.fk(fkMatcher.group(1), org.apache.ambari.server.orm.db.DDLTestUtils.toColumns(fkMatcher.group(2)), org.apache.ambari.server.orm.db.DDLTestUtils.stripPrefixQuotationAndBrackets(fkMatcher.group(3)), org.apache.ambari.server.orm.db.DDLTestUtils.toColumns(fkMatcher.group(4))));
                } else if (unnamedFkMatcher.matches()) {
                    fks.add(org.apache.ambari.server.orm.db.Constraint.fk("<default>", org.apache.ambari.server.orm.db.DDLTestUtils.toColumns(unnamedFkMatcher.group(1)), org.apache.ambari.server.orm.db.DDLTestUtils.stripPrefixQuotationAndBrackets(unnamedFkMatcher.group(2)), org.apache.ambari.server.orm.db.DDLTestUtils.toColumns(unnamedFkMatcher.group(3))));
                } else if (uqMatcher.matches()) {
                    uqs.add(org.apache.ambari.server.orm.db.Constraint.uq(org.apache.ambari.server.orm.db.DDLTestUtils.stripPrefixQuotationAndBrackets(uqMatcher.group(1)), org.apache.ambari.server.orm.db.DDLTestUtils.toColumns(uqMatcher.group(2))));
                } else if (unnamedUqMatcher.matches()) {
                    uqs.add(org.apache.ambari.server.orm.db.Constraint.uq("<default>", org.apache.ambari.server.orm.db.DDLTestUtils.toColumns(unnamedUqMatcher.group(1))));
                } else if (colMatcher.matches()) {
                    java.lang.String colName = org.apache.ambari.server.orm.db.DDLTestUtils.stripPrefixQuotationAndBrackets(colMatcher.group(1));
                    columns.add(colName);
                    if (pkColumnMatcher.matches()) {
                        pk = com.google.common.base.Optional.of(org.apache.ambari.server.orm.db.Constraint.pk("<default>", java.util.Collections.singleton(colName)));
                    } else if (uqColumnMatcher.matches()) {
                        uqs.add(org.apache.ambari.server.orm.db.Constraint.uq("<default>", java.util.Collections.singleton(colName)));
                    }
                } else {
                    org.apache.ambari.server.orm.db.DDLTestUtils.LOG.warn("Unexpected definition: {}, context: {}", definition, ct);
                }
            }
            if (columns.isEmpty()) {
                throw new java.lang.IllegalStateException("No columns found in table " + tableName);
            }
            org.apache.ambari.server.orm.db.DDLTestUtils.checkDupes("columns of table " + tableName, columns);
            tables.add(new org.apache.ambari.server.orm.db.Table(tableName, com.google.common.collect.ImmutableSet.copyOf(columns), pk, com.google.common.collect.ImmutableSet.copyOf(fks), com.google.common.collect.ImmutableSet.copyOf(uqs)));
        }
        java.util.Map<java.lang.String, org.apache.ambari.server.orm.db.Table> tableMap = com.google.common.collect.Maps.newHashMap();
        for (org.apache.ambari.server.orm.db.Table t : tables) {
            if (tableMap.containsKey(t.name))
                throw new java.lang.IllegalStateException("Duplicate table definition: " + t.name);

            tableMap.put(t.name, t);
        }
        return tableMap;
    }

    private static void checkDupes(java.lang.String objectName, java.util.List<? extends java.lang.Object> items) {
        java.util.Set<java.lang.Object> set = com.google.common.collect.Sets.newHashSet(items);
        if (set.size() < items.size()) {
            throw new java.lang.IllegalStateException(java.lang.String.format("Duplicates found in %s: %s", objectName, com.google.common.collect.Iterables.toString(items)));
        }
    }

    private static void assertNounnamedConstraint(java.lang.String tableName, java.lang.String definition) {
        if (tableName.contains("qrtz")) {
            org.apache.ambari.server.orm.db.DDLTestUtils.LOG.debug("Skipp checking quartz table: {}", tableName);
        } else {
            for (java.util.regex.Pattern unnamedConstraint : org.apache.ambari.server.orm.db.DDLTestUtils.CheckedUnnamedConstraints) {
                if (unnamedConstraint.matcher(definition).matches()) {
                    throw new java.lang.IllegalStateException(java.lang.String.format("Found invalid (unnamed) constraint in table %s: %s", tableName, definition));
                }
            }
            for (java.util.regex.Pattern unnamedConstraint : org.apache.ambari.server.orm.db.DDLTestUtils.UncheckedUnnamedConstraints) {
                if (unnamedConstraint.matcher(definition).matches()) {
                    org.apache.ambari.server.orm.db.DDLTestUtils.LOG.info("Found unnamed constraint in table {}: {}", tableName, definition);
                }
            }
        }
    }

    private static org.apache.ambari.server.orm.db.DDL loadDdl(java.lang.String dbType) throws java.lang.Exception {
        java.util.List<java.lang.String> lines = org.apache.ambari.server.orm.db.DDLTestUtils.loadFile(dbType);
        java.util.List<java.lang.String> statements = org.apache.ambari.server.orm.db.DDLTestUtils.groupStatements(lines);
        java.util.Map<java.lang.String, org.apache.ambari.server.orm.db.Table> tables = org.apache.ambari.server.orm.db.DDLTestUtils.parseTableDefs(statements);
        java.util.List<java.lang.String> alterTables = new java.util.ArrayList<>();
        for (java.lang.String stmt : statements) {
            if (stmt.matches(".*alter\\s+table.*"))
                alterTables.add(stmt);

        }
        return new org.apache.ambari.server.orm.db.DDL(dbType, tables, alterTables);
    }
}