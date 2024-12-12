package org.apache.ambari.server.orm.db;
import org.apache.commons.lang.StringUtils;
public class DDLTests {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.orm.db.DDLTestUtils.class);

    private static final int EXPECTED_ALTER_TABLE_COUNT = 1;

    @org.junit.Test
    public void testVerifyDerby() throws java.lang.Exception {
        verifyDDL("Derby");
    }

    @org.junit.Test
    public void testVerifyPostgres() throws java.lang.Exception {
        verifyDDL("Postgres");
    }

    @org.junit.Test
    public void testVerifyMySQL() throws java.lang.Exception {
        verifyDDL("MySQL");
    }

    @org.junit.Test
    public void testVerifyOracle() throws java.lang.Exception {
        verifyDDL("Oracle");
    }

    @org.junit.Test
    public void testVerifySqlAnywhere() throws java.lang.Exception {
        verifyDDL("SQLAnywhere");
    }

    @org.junit.Test
    public void testVerifyMsSqlServer() throws java.lang.Exception {
        verifyDDL("SQLServer");
    }

    private void verifyDDL(java.lang.String dbType) throws java.lang.Exception {
        org.apache.ambari.server.orm.db.DDLTests.LOG.info("Checking DDL for {}", dbType);
        org.apache.ambari.server.orm.db.DDL ddl = org.apache.ambari.server.orm.db.DDLTestUtils.getDdl(dbType);
        org.apache.ambari.server.orm.db.DDLTests.printDDLMetrics(ddl);
        org.junit.Assert.assertEquals("Expected count of alter tables mismatch. Please include all constraint definitions in " + (("the create table statement, only use alter table in exceptional cases, such as to work around a circular " + "FK dependency. Would another such case occur, please document it in the DDL's and adjust the ") + "EXPECTED_ALTER_TABLE_COUNT in this test."), org.apache.ambari.server.orm.db.DDLTests.EXPECTED_ALTER_TABLE_COUNT, ddl.alterTables.size());
        for (java.lang.String tableName : ddl.tableNames()) {
            org.junit.Assert.assertTrue("Table name exceeds the 30 character limit: " + tableName, tableName.length() <= 30);
        }
        for (org.apache.ambari.server.orm.db.Table table : ddl.tables.values()) {
            org.junit.Assert.assertTrue("PK name exceeds the 30 character limit: " + table.primaryKey, (!table.primaryKey.isPresent()) || (table.primaryKey.get().name().length() <= 30));
            for (org.apache.ambari.server.orm.db.Constraint constr : com.google.common.collect.Sets.union(table.foreignKeys, table.uniqueConstraints)) {
                org.junit.Assert.assertTrue("Constraint name exceeds the 30 character limit: " + constr, constr.name().length() <= 30);
            }
        }
        for (org.apache.ambari.server.orm.db.Table table : ddl.tables.values()) {
            org.junit.Assert.assertFalse("Unnamed PK exists for table: " + table.name, ((!table.name.startsWith("qrtz")) && table.primaryKey.isPresent()) && table.primaryKey.get().name().equals("<default>"));
            for (org.apache.ambari.server.orm.db.Constraint constr : com.google.common.collect.Sets.union(table.foreignKeys, table.uniqueConstraints)) {
                org.junit.Assert.assertTrue("Constraint name exceeds the 30 character limit: " + constr, constr.name().length() <= 30);
            }
        }
    }

    @org.junit.Test
    public void testCompareDerby() throws java.lang.Exception {
        org.apache.ambari.server.orm.db.DDLTests.compareAgainstPostgres("Derby");
    }

    @org.junit.Test
    public void testCompareOracle() throws java.lang.Exception {
        org.apache.ambari.server.orm.db.DDLTests.compareAgainstPostgres("Oracle");
    }

    @org.junit.Test
    public void testCompareMySQL() throws java.lang.Exception {
        org.apache.ambari.server.orm.db.DDLTests.compareAgainstPostgres("MySQL");
    }

    @org.junit.Test
    public void testCompareSQLAnywhere() throws java.lang.Exception {
        org.apache.ambari.server.orm.db.DDLTests.compareAgainstPostgres("SQLAnywhere");
    }

    @org.junit.Test
    public void testCompareSQLServer() throws java.lang.Exception {
        org.apache.ambari.server.orm.db.DDLTests.compareAgainstPostgres("SQLServer");
    }

    static void compareAgainstPostgres(java.lang.String dbType) throws java.lang.Exception {
        org.apache.ambari.server.orm.db.DDLTests.LOG.info("Comparing {} against Postgres", dbType);
        org.apache.ambari.server.orm.db.DDL postgres = org.apache.ambari.server.orm.db.DDLTestUtils.getDdl("Postgres");
        org.apache.ambari.server.orm.db.DDL other = org.apache.ambari.server.orm.db.DDLTestUtils.getDdl(dbType);
        java.util.List<java.lang.String> diffs = org.apache.ambari.server.orm.db.DDLTests.compareDdls(postgres, other);
        if (diffs.isEmpty()) {
            org.apache.ambari.server.orm.db.DDLTests.LOG.info("Compare OK.");
        } else {
            org.apache.ambari.server.orm.db.DDLTests.LOG.info("{} differences found:", diffs.size());
            for (java.lang.String diff : diffs) {
                org.apache.ambari.server.orm.db.DDLTests.LOG.info(diff);
            }
            java.lang.StringBuilder buffer = new java.lang.StringBuilder();
            buffer.append("Found ").append(diffs.size()).append(" differences when comparing ").append(other).append(" against Postgres:").append(java.lang.System.lineSeparator()).append(org.apache.commons.lang.StringUtils.join(diffs, java.lang.System.lineSeparator()));
            org.junit.Assert.fail(buffer.toString());
        }
    }

    static void printDDLMetrics(org.apache.ambari.server.orm.db.DDL ddl) {
        org.apache.ambari.server.orm.db.DDLTests.LOG.info("DDL metrics for {}", ddl.dbType);
        int colCount = 0;
        int pkCount = 0;
        int fkCount = 0;
        int uqCount = 0;
        for (org.apache.ambari.server.orm.db.Table t : ddl.tables.values()) {
            colCount += t.columns.size();
            if (t.primaryKey.isPresent()) {
                pkCount++;
            }
            fkCount += t.foreignKeys.size();
            uqCount += t.uniqueConstraints.size();
        }
        org.apache.ambari.server.orm.db.DDLTests.LOG.info("Found {} tables", ddl.tables.size());
        java.util.List<java.lang.String> tableNames = new java.util.ArrayList<>();
        tableNames.addAll(ddl.tableNames());
        java.util.Collections.sort(tableNames);
        org.apache.ambari.server.orm.db.DDLTests.LOG.info("Table names: {}", com.google.common.base.Joiner.on(',').join(tableNames));
        org.apache.ambari.server.orm.db.DDLTests.LOG.info("Total number of Columns: {}", colCount);
        org.apache.ambari.server.orm.db.DDLTests.LOG.info("Total number of PK's: {}", pkCount);
        org.apache.ambari.server.orm.db.DDLTests.LOG.info("Total number of FK's: {}", fkCount);
        org.apache.ambari.server.orm.db.DDLTests.LOG.info("Total number of UQ's: {}", uqCount);
        org.apache.ambari.server.orm.db.DDLTests.LOG.info("Number of Alter table statements: {}", ddl.alterTables.size());
    }

    static java.util.List<java.lang.String> compareDdls(org.apache.ambari.server.orm.db.DDL base, org.apache.ambari.server.orm.db.DDL other) {
        java.util.List<java.lang.String> diffs = new java.util.ArrayList<>();
        if (!base.tableNames().equals(other.tableNames())) {
            java.util.Set<java.lang.String> missingTables = com.google.common.collect.Sets.difference(base.tableNames(), other.tableNames());
            if (!missingTables.isEmpty()) {
                diffs.add("Missing tables: " + com.google.common.base.Joiner.on(", ").join(missingTables));
            }
            java.util.Set<java.lang.String> extraTables = com.google.common.collect.Sets.difference(other.tableNames(), base.tableNames());
            if (!extraTables.isEmpty()) {
                diffs.add("Extra tables: " + com.google.common.base.Joiner.on(", ").join(extraTables));
            }
        }
        java.util.Set<java.lang.String> commonTables = com.google.common.collect.Sets.intersection(base.tableNames(), other.tableNames());
        for (java.lang.String tableName : commonTables) {
            org.apache.ambari.server.orm.db.Table baseTable = base.tables.get(tableName);
            org.apache.ambari.server.orm.db.Table otherTable = other.tables.get(tableName);
            diffs.addAll(org.apache.ambari.server.orm.db.DDLTests.compareSets(java.lang.String.format("Comparing columns of table %s.", tableName), baseTable.columns, otherTable.columns));
            diffs.addAll(org.apache.ambari.server.orm.db.DDLTests.compareConstraints(tableName, "FK", baseTable.foreignKeys, otherTable.foreignKeys, false));
            diffs.addAll(org.apache.ambari.server.orm.db.DDLTests.compareConstraints(tableName, "UQ", baseTable.uniqueConstraints, otherTable.uniqueConstraints, false));
            boolean comparePKName = !tableName.contains("qrtz");
            diffs.addAll(org.apache.ambari.server.orm.db.DDLTests.compareConstraints(tableName, "PK", org.apache.ambari.server.orm.db.DDLTests.toSet(baseTable.primaryKey), org.apache.ambari.server.orm.db.DDLTests.toSet(otherTable.primaryKey), comparePKName));
        }
        return diffs;
    }

    static <T> java.util.Set<T> toSet(com.google.common.base.Optional<T> arg) {
        return arg.isPresent() ? com.google.common.collect.ImmutableSet.of(arg.get()) : com.google.common.collect.ImmutableSet.of();
    }

    static <ContentType> java.util.List<java.lang.String> compareSets(java.lang.String message, java.util.Set<ContentType> base, java.util.Set<ContentType> other) {
        java.util.List<java.lang.String> diffs = new java.util.ArrayList<>(2);
        java.util.Set<ContentType> missingItems = com.google.common.collect.Sets.difference(base, other);
        if (!missingItems.isEmpty()) {
            diffs.add((message + " Missing items: ") + com.google.common.base.Joiner.on(", ").join(missingItems));
        }
        java.util.Set<ContentType> extraItems = com.google.common.collect.Sets.difference(other, base);
        if (!extraItems.isEmpty()) {
            diffs.add((message + " Extra items: ") + com.google.common.base.Joiner.on(", ").join(extraItems));
        }
        return diffs;
    }

    static <ContentType> java.util.List<java.lang.String> compareConstraints(java.lang.String tableName, java.lang.String constraintType, java.util.Set<? extends org.apache.ambari.server.orm.db.Constraint<ContentType>> base, java.util.Set<? extends org.apache.ambari.server.orm.db.Constraint<ContentType>> other, boolean compareConstraintNames) {
        java.util.List<java.lang.String> diffs = new java.util.ArrayList<>();
        java.util.Map<ContentType, org.apache.ambari.server.orm.db.Constraint<ContentType>> baseByContent = com.google.common.collect.Maps.newHashMap();
        java.util.Map<ContentType, org.apache.ambari.server.orm.db.Constraint<ContentType>> otherByContent = com.google.common.collect.Maps.newHashMap();
        for (org.apache.ambari.server.orm.db.Constraint<ContentType> c : base) {
            baseByContent.put(c.content(), c);
        }
        for (org.apache.ambari.server.orm.db.Constraint<ContentType> c : other) {
            otherByContent.put(c.content(), c);
        }
        diffs.addAll(org.apache.ambari.server.orm.db.DDLTests.compareSets(java.lang.String.format("Comparing %ss of table %s.", constraintType, tableName), baseByContent.keySet(), otherByContent.keySet()));
        java.util.Set<ContentType> common = com.google.common.collect.Sets.intersection(baseByContent.keySet(), otherByContent.keySet());
        for (ContentType constrContent : common) {
            org.apache.ambari.server.orm.db.Constraint b = baseByContent.get(constrContent);
            org.apache.ambari.server.orm.db.Constraint o = otherByContent.get(constrContent);
            if (!b.name().equals(o.name())) {
                if (compareConstraintNames) {
                    diffs.add(java.lang.String.format("Constraint name mismatch for table %s: %s vs. %s", tableName, b, o));
                } else {
                    org.apache.ambari.server.orm.db.DDLTests.LOG.info("Ignoring constraint name mismatch for table {}: {} vs. {}", tableName, b, o);
                }
            }
        }
        return diffs;
    }
}