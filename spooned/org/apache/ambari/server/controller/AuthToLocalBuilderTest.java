package org.apache.ambari.server.controller;
public class AuthToLocalBuilderTest {
    @org.junit.Test
    public void testRuleGeneration() {
        org.apache.ambari.server.controller.AuthToLocalBuilder builder = new org.apache.ambari.server.controller.AuthToLocalBuilder("EXAMPLE.COM", java.util.Collections.emptyList(), false);
        builder.addRule("nn/_HOST@EXAMPLE.COM", "hdfs");
        builder.addRule("nn/_HOST@EXAMPLE.COM", "hdfs");
        builder.addRule("dn/_HOST@EXAMPLE.COM", "hdfs");
        builder.addRule("jn/_HOST@EXAMPLE.COM", "hdfs");
        builder.addRule("rm/_HOST@EXAMPLE.COM", "yarn");
        builder.addRule("jhs/_HOST@EXAMPLE.COM", "mapred");
        builder.addRule("hm/_HOST@EXAMPLE.COM", "hbase");
        builder.addRule("rs/_HOST@EXAMPLE.COM", "hbase");
        builder.addRule("foobar@EXAMPLE.COM", "hdfs");
        org.junit.Assert.assertEquals("RULE:[1:$1@$0](foobar@EXAMPLE.COM)s/.*/hdfs/\n" + (((((((("RULE:[1:$1@$0](.*@EXAMPLE.COM)s/@.*//\n" + "RULE:[2:$1@$0](dn@EXAMPLE.COM)s/.*/hdfs/\n") + "RULE:[2:$1@$0](hm@EXAMPLE.COM)s/.*/hbase/\n") + "RULE:[2:$1@$0](jhs@EXAMPLE.COM)s/.*/mapred/\n") + "RULE:[2:$1@$0](jn@EXAMPLE.COM)s/.*/hdfs/\n") + "RULE:[2:$1@$0](nn@EXAMPLE.COM)s/.*/hdfs/\n") + "RULE:[2:$1@$0](rm@EXAMPLE.COM)s/.*/yarn/\n") + "RULE:[2:$1@$0](rs@EXAMPLE.COM)s/.*/hbase/\n") + "DEFAULT"), builder.generate());
    }

    @org.junit.Test
    public void testRuleGeneration_caseInsensitiveSupport() {
        org.apache.ambari.server.controller.AuthToLocalBuilder builder = new org.apache.ambari.server.controller.AuthToLocalBuilder("EXAMPLE.COM", java.util.Collections.emptyList(), true);
        builder.addRule("nn/_HOST@EXAMPLE.COM", "hdfs");
        builder.addRule("nn/_HOST@EXAMPLE.COM", "hdfs");
        builder.addRule("dn/_HOST@EXAMPLE.COM", "hdfs");
        builder.addRule("jn/_HOST@EXAMPLE.COM", "hdfs");
        builder.addRule("rm/_HOST@EXAMPLE.COM", "yarn");
        builder.addRule("jhs/_HOST@EXAMPLE.COM", "mapred");
        builder.addRule("hm/_HOST@EXAMPLE.COM", "hbase");
        builder.addRule("rs/_HOST@EXAMPLE.COM", "hbase");
        builder.addRule("foobar@EXAMPLE.COM", "hdfs");
        org.junit.Assert.assertEquals("RULE:[1:$1@$0](foobar@EXAMPLE.COM)s/.*/hdfs/\n" + (((((((("RULE:[1:$1@$0](.*@EXAMPLE.COM)s/@.*///L\n" + "RULE:[2:$1@$0](dn@EXAMPLE.COM)s/.*/hdfs/\n") + "RULE:[2:$1@$0](hm@EXAMPLE.COM)s/.*/hbase/\n") + "RULE:[2:$1@$0](jhs@EXAMPLE.COM)s/.*/mapred/\n") + "RULE:[2:$1@$0](jn@EXAMPLE.COM)s/.*/hdfs/\n") + "RULE:[2:$1@$0](nn@EXAMPLE.COM)s/.*/hdfs/\n") + "RULE:[2:$1@$0](rm@EXAMPLE.COM)s/.*/yarn/\n") + "RULE:[2:$1@$0](rs@EXAMPLE.COM)s/.*/hbase/\n") + "DEFAULT"), builder.generate());
    }

    @org.junit.Test
    public void testRuleGeneration_changeToCaseInsensitiveSupport() {
        org.apache.ambari.server.controller.AuthToLocalBuilder builder = new org.apache.ambari.server.controller.AuthToLocalBuilder("EXAMPLE.COM", java.util.Collections.emptyList(), false);
        builder.addRule("nn/_HOST@EXAMPLE.COM", "hdfs");
        builder.addRule("nn/_HOST@EXAMPLE.COM", "hdfs");
        builder.addRule("dn/_HOST@EXAMPLE.COM", "hdfs");
        builder.addRule("jn/_HOST@EXAMPLE.COM", "hdfs");
        builder.addRule("rm/_HOST@EXAMPLE.COM", "yarn");
        builder.addRule("jhs/_HOST@EXAMPLE.COM", "mapred");
        builder.addRule("hm/_HOST@EXAMPLE.COM", "hbase");
        builder.addRule("rs/_HOST@EXAMPLE.COM", "hbase");
        java.lang.String existingRules = builder.generate();
        builder = new org.apache.ambari.server.controller.AuthToLocalBuilder("EXAMPLE.COM", java.util.Collections.emptyList(), true);
        builder.addRules(existingRules);
        builder.addRule("nn/_HOST@EXAMPLE.COM", "hdfs");
        builder.addRule("dn/_HOST@EXAMPLE.COM", "hdfs");
        builder.addRule("jn/_HOST@EXAMPLE.COM", "hdfs");
        builder.addRule("rm/_HOST@EXAMPLE.COM", "yarn");
        builder.addRule("jhs/_HOST@EXAMPLE.COM", "mapred");
        builder.addRule("hm/_HOST@EXAMPLE.COM", "hbase");
        builder.addRule("rs/_HOST@EXAMPLE.COM", "hbase");
        org.junit.Assert.assertEquals("RULE:[1:$1@$0](.*@EXAMPLE.COM)s/@.*///L\n" + ((((((("RULE:[2:$1@$0](dn@EXAMPLE.COM)s/.*/hdfs/\n" + "RULE:[2:$1@$0](hm@EXAMPLE.COM)s/.*/hbase/\n") + "RULE:[2:$1@$0](jhs@EXAMPLE.COM)s/.*/mapred/\n") + "RULE:[2:$1@$0](jn@EXAMPLE.COM)s/.*/hdfs/\n") + "RULE:[2:$1@$0](nn@EXAMPLE.COM)s/.*/hdfs/\n") + "RULE:[2:$1@$0](rm@EXAMPLE.COM)s/.*/yarn/\n") + "RULE:[2:$1@$0](rs@EXAMPLE.COM)s/.*/hbase/\n") + "DEFAULT"), builder.generate());
    }

    @org.junit.Test
    public void testRuleGeneration_changeToCaseSensitiveSupport() {
        org.apache.ambari.server.controller.AuthToLocalBuilder builder = new org.apache.ambari.server.controller.AuthToLocalBuilder("EXAMPLE.COM", java.util.Collections.emptyList(), true);
        builder.addRule("nn/_HOST@EXAMPLE.COM", "hdfs");
        builder.addRule("nn/_HOST@EXAMPLE.COM", "hdfs");
        builder.addRule("dn/_HOST@EXAMPLE.COM", "hdfs");
        builder.addRule("jn/_HOST@EXAMPLE.COM", "hdfs");
        builder.addRule("rm/_HOST@EXAMPLE.COM", "yarn");
        builder.addRule("jhs/_HOST@EXAMPLE.COM", "mapred");
        builder.addRule("hm/_HOST@EXAMPLE.COM", "hbase");
        builder.addRule("rs/_HOST@EXAMPLE.COM", "hbase");
        java.lang.String existingRules = builder.generate();
        builder = new org.apache.ambari.server.controller.AuthToLocalBuilder("EXAMPLE.COM", java.util.Collections.emptyList(), false);
        builder.addRules(existingRules);
        builder.addRule("nn/_HOST@EXAMPLE.COM", "hdfs");
        builder.addRule("dn/_HOST@EXAMPLE.COM", "hdfs");
        builder.addRule("jn/_HOST@EXAMPLE.COM", "hdfs");
        builder.addRule("rm/_HOST@EXAMPLE.COM", "yarn");
        builder.addRule("jhs/_HOST@EXAMPLE.COM", "mapred");
        builder.addRule("hm/_HOST@EXAMPLE.COM", "hbase");
        builder.addRule("rs/_HOST@EXAMPLE.COM", "hbase");
        org.junit.Assert.assertEquals("RULE:[1:$1@$0](.*@EXAMPLE.COM)s/@.*//\n" + ((((((("RULE:[2:$1@$0](dn@EXAMPLE.COM)s/.*/hdfs/\n" + "RULE:[2:$1@$0](hm@EXAMPLE.COM)s/.*/hbase/\n") + "RULE:[2:$1@$0](jhs@EXAMPLE.COM)s/.*/mapred/\n") + "RULE:[2:$1@$0](jn@EXAMPLE.COM)s/.*/hdfs/\n") + "RULE:[2:$1@$0](nn@EXAMPLE.COM)s/.*/hdfs/\n") + "RULE:[2:$1@$0](rm@EXAMPLE.COM)s/.*/yarn/\n") + "RULE:[2:$1@$0](rs@EXAMPLE.COM)s/.*/hbase/\n") + "DEFAULT"), builder.generate());
    }

    @org.junit.Test
    public void testRuleGeneration_ExistingRules() {
        org.apache.ambari.server.controller.AuthToLocalBuilder builder = new org.apache.ambari.server.controller.AuthToLocalBuilder("EXAMPLE.COM", java.util.Collections.emptyList(), false);
        builder.addRule("foobar@EXAMPLE.COM", "hdfs");
        builder.addRule("hm/_HOST@EXAMPLE.COM", "hbase");
        builder.addRule("nn/_HOST@EXAMPLE.COM", "hdfs");
        java.lang.String existingRules = builder.generate();
        builder = new org.apache.ambari.server.controller.AuthToLocalBuilder("EXAMPLE.COM", java.util.Collections.emptyList(), false);
        builder.addRules(existingRules);
        builder.addRule("dn/_HOST@EXAMPLE.COM", "hdfs");
        builder.addRule("nn/_HOST@EXAMPLE.COM", "hdfs");
        builder.addRule("nn/_HOST@EXAMPLE.COM", "hdfs");
        builder.addRule("jn/_HOST@EXAMPLE.COM", "hdfs");
        builder.addRule("rm/_HOST@EXAMPLE.COM", "yarn");
        builder.addRule("jhs/_HOST@EXAMPLE.COM", "mapred");
        builder.addRule("rs/_HOST@EXAMPLE.COM", "hbase");
        org.junit.Assert.assertEquals("RULE:[1:$1@$0](foobar@EXAMPLE.COM)s/.*/hdfs/\n" + (((((((("RULE:[1:$1@$0](.*@EXAMPLE.COM)s/@.*//\n" + "RULE:[2:$1@$0](dn@EXAMPLE.COM)s/.*/hdfs/\n") + "RULE:[2:$1@$0](hm@EXAMPLE.COM)s/.*/hbase/\n") + "RULE:[2:$1@$0](jhs@EXAMPLE.COM)s/.*/mapred/\n") + "RULE:[2:$1@$0](jn@EXAMPLE.COM)s/.*/hdfs/\n") + "RULE:[2:$1@$0](nn@EXAMPLE.COM)s/.*/hdfs/\n") + "RULE:[2:$1@$0](rm@EXAMPLE.COM)s/.*/yarn/\n") + "RULE:[2:$1@$0](rs@EXAMPLE.COM)s/.*/hbase/\n") + "DEFAULT"), builder.generate());
    }

    @org.junit.Test
    public void testRuleGeneration_ExistingRules_existingMoreSpecificRule() {
        org.apache.ambari.server.controller.AuthToLocalBuilder builder = new org.apache.ambari.server.controller.AuthToLocalBuilder("EXAMPLE.COM", java.util.Collections.emptyList(), false);
        builder.addRule("foobar@EXAMPLE.COM", "hdfs");
        builder.addRule("hm/_HOST@EXAMPLE.COM", "hbase");
        builder.addRule("jn/_HOST@EXAMPLE.COM", "hdfs");
        java.lang.String existingRules = builder.generate();
        existingRules = "RULE:[2:$1/$2@$0](dn/somehost.com@EXAMPLE.COM)s/.*/hdfs/\n" + existingRules;
        existingRules += "\nRULE:[1:$1@$0](.*@OTHER_REALM.COM)s/@.*//";
        builder = new org.apache.ambari.server.controller.AuthToLocalBuilder("EXAMPLE.COM", java.util.Collections.emptyList(), false);
        builder.addRules(existingRules);
        builder.addRule("dn/_HOST@EXAMPLE.COM", "hdfs");
        builder.addRule("nn/_HOST@EXAMPLE.COM", "hdfs");
        builder.addRule("nn/_HOST@EXAMPLE.COM", "hdfs");
        builder.addRule("jn/_HOST@EXAMPLE.COM", "hdfs");
        builder.addRule("rm/_HOST@EXAMPLE.COM", "yarn");
        builder.addRule("jhs/_HOST@EXAMPLE.COM", "mapred");
        builder.addRule("rs/_HOST@EXAMPLE.COM", "hbase");
        org.junit.Assert.assertEquals("RULE:[1:$1@$0](foobar@EXAMPLE.COM)s/.*/hdfs/\n" + (((((((((("RULE:[1:$1@$0](.*@EXAMPLE.COM)s/@.*//\n" + "RULE:[1:$1@$0](.*@OTHER_REALM.COM)s/@.*//\n") + "RULE:[2:$1/$2@$0](dn/somehost.com@EXAMPLE.COM)s/.*/hdfs/\n") + "RULE:[2:$1@$0](dn@EXAMPLE.COM)s/.*/hdfs/\n") + "RULE:[2:$1@$0](hm@EXAMPLE.COM)s/.*/hbase/\n") + "RULE:[2:$1@$0](jhs@EXAMPLE.COM)s/.*/mapred/\n") + "RULE:[2:$1@$0](jn@EXAMPLE.COM)s/.*/hdfs/\n") + "RULE:[2:$1@$0](nn@EXAMPLE.COM)s/.*/hdfs/\n") + "RULE:[2:$1@$0](rm@EXAMPLE.COM)s/.*/yarn/\n") + "RULE:[2:$1@$0](rs@EXAMPLE.COM)s/.*/hbase/\n") + "DEFAULT"), builder.generate());
    }

    @org.junit.Test
    public void testAddNullExistingRule() {
        org.apache.ambari.server.controller.AuthToLocalBuilder builder = new org.apache.ambari.server.controller.AuthToLocalBuilder("EXAMPLE.COM", java.util.Collections.emptyList(), false);
        builder.addRules(null);
        org.junit.Assert.assertEquals("RULE:[1:$1@$0](.*@EXAMPLE.COM)s/@.*//\n" + "DEFAULT", builder.generate());
    }

    @org.junit.Test
    public void testRuleRegexWithDifferentEnding() {
        java.lang.String rules = "RULE:[1:$1@$0](foobar@EXAMPLE.COM)s/.*/hdfs/\\\\\n" + (((("RULE:[1:$1@$0](.*@EXAMPLE.COM)s/@.*//\ntext\\\\" + "RULE:[2:$1@$0](dn@EXAMPLE.COM)s/.*/hdfs/\n") + "RULE:[2:$1@$0](hm@EXAMPLE.COM)s/.*/hbase/") + "RULE:[2:$1@$0](jhs@EXAMPLE.COM)s/.*/mapred/\\\\\\") + "RULE:[2:$1@$0](jn@EXAMPLE.COM)s/.*/hdfs/\\/\\");
        org.apache.ambari.server.controller.AuthToLocalBuilder builder = new org.apache.ambari.server.controller.AuthToLocalBuilder("EXAMPLE.COM", java.util.Collections.emptyList(), false);
        builder.addRules(rules);
        org.junit.Assert.assertEquals("RULE:[1:$1@$0](foobar@EXAMPLE.COM)s/.*/hdfs/\n" + ((((("RULE:[1:$1@$0](.*@EXAMPLE.COM)s/@.*//\n" + "RULE:[2:$1@$0](dn@EXAMPLE.COM)s/.*/hdfs/\n") + "RULE:[2:$1@$0](hm@EXAMPLE.COM)s/.*/hbase/\n") + "RULE:[2:$1@$0](jhs@EXAMPLE.COM)s/.*/mapred/\n") + "RULE:[2:$1@$0](jn@EXAMPLE.COM)s/.*/hdfs/\n") + "DEFAULT"), builder.generate());
    }

    @org.junit.Test
    public void testRuleRegexWithComplexReplacements() {
        java.lang.String rules = "RULE:[1:$1@$0](foobar@\\QEXAMPLE1.COM\\E$)s/.*@\\QEXAMPLE1.COM\\E$/hdfs/\n" + ("RULE:[1:$1@$0](.*@\\QEXAMPLE1.COM\\E)s/@\\QEXAMPLE1.COM\\E//\n" + "RULE:[2:$1@$0](.*@\\QEXAMPLE1.COM\\E)s/@\\QEXAMPLE1.COM\\E//");
        org.apache.ambari.server.controller.AuthToLocalBuilder builder = new org.apache.ambari.server.controller.AuthToLocalBuilder("EXAMPLE.COM", java.util.Collections.emptyList(), false);
        builder.addRules(rules);
        builder.addRule("nn/_HOST@EXAMPLE.COM", "hdfs");
        builder.addRule("dn/_HOST@EXAMPLE.COM", "hdfs");
        builder.addRule("jn/_HOST@EXAMPLE.COM", "hdfs");
        builder.addRule("rm/_HOST@EXAMPLE.COM", "yarn");
        builder.addRule("jhs/_HOST@EXAMPLE.COM", "mapred");
        builder.addRule("hm/_HOST@EXAMPLE.COM", "hbase");
        builder.addRule("rs/_HOST@EXAMPLE.COM", "hbase");
        builder.addRule("ambari-qa-c1@EXAMPLE.COM", "ambari-qa");
        org.junit.Assert.assertEquals("RULE:[1:$1@$0](ambari-qa-c1@EXAMPLE.COM)s/.*/ambari-qa/\n" + ((((((((((("RULE:[1:$1@$0](.*@EXAMPLE.COM)s/@.*//\n" + "RULE:[1:$1@$0](.*@\\QEXAMPLE1.COM\\E)s/@\\QEXAMPLE1.COM\\E//\n") + "RULE:[1:$1@$0](foobar@\\QEXAMPLE1.COM\\E$)s/.*@\\QEXAMPLE1.COM\\E$/hdfs/\n") + "RULE:[2:$1@$0](dn@EXAMPLE.COM)s/.*/hdfs/\n") + "RULE:[2:$1@$0](hm@EXAMPLE.COM)s/.*/hbase/\n") + "RULE:[2:$1@$0](jhs@EXAMPLE.COM)s/.*/mapred/\n") + "RULE:[2:$1@$0](jn@EXAMPLE.COM)s/.*/hdfs/\n") + "RULE:[2:$1@$0](nn@EXAMPLE.COM)s/.*/hdfs/\n") + "RULE:[2:$1@$0](rm@EXAMPLE.COM)s/.*/yarn/\n") + "RULE:[2:$1@$0](rs@EXAMPLE.COM)s/.*/hbase/\n") + "RULE:[2:$1@$0](.*@\\QEXAMPLE1.COM\\E)s/@\\QEXAMPLE1.COM\\E//\n") + "DEFAULT"), builder.generate());
    }

    @org.junit.Test
    public void testRulesWithWhitespace() {
        java.lang.String rulesWithWhitespace = "RULE:   [1:$1@$0](foobar@EXAMPLE.COM)s/.*/hdfs/\n" + (((("RULE:[  1:$1@$0](.*@EXAMPLE.COM)s/@.*//\n" + "RULE:[2:   $1@$0](dn@EXAMPLE.COM)s/.*/hdfs/\n") + "RULE:[2:$1@$0   ](hm@EXAMPLE.COM)s/.*/hbase/\n") + "RULE:[2:$1@$0]   (jhs@EXAMPLE.COM)s/.*/mapred/\n") + "RULE:[2:$1@$0](jn@EXAMPLE.COM)   s/.*/hdfs/\n");
        org.apache.ambari.server.controller.AuthToLocalBuilder builder = new org.apache.ambari.server.controller.AuthToLocalBuilder("EXAMPLE.COM", java.util.Collections.emptyList(), false);
        builder.addRules(rulesWithWhitespace);
        org.junit.Assert.assertEquals("RULE:[1:$1@$0](foobar@EXAMPLE.COM)s/.*/hdfs/\n" + ((((("RULE:[1:$1@$0](.*@EXAMPLE.COM)s/@.*//\n" + "RULE:[2:$1@$0](dn@EXAMPLE.COM)s/.*/hdfs/\n") + "RULE:[2:$1@$0](hm@EXAMPLE.COM)s/.*/hbase/\n") + "RULE:[2:$1@$0](jhs@EXAMPLE.COM)s/.*/mapred/\n") + "RULE:[2:$1@$0](jn@EXAMPLE.COM)s/.*/hdfs/\n") + "DEFAULT"), builder.generate());
    }

    @org.junit.Test
    public void testExistingRuleWithNoRealm() {
        org.apache.ambari.server.controller.AuthToLocalBuilder builder = new org.apache.ambari.server.controller.AuthToLocalBuilder("EXAMPLE.COM", java.util.Collections.emptyList(), false);
        builder.addRules("RULE:[1:$1](foobar)s/.*/hdfs/");
        org.junit.Assert.assertEquals("RULE:[1:$1](foobar)s/.*/hdfs/\n" + ("RULE:[1:$1@$0](.*@EXAMPLE.COM)s/@.*//\n" + "DEFAULT"), builder.generate());
    }

    @org.junit.Test
    public void testExistingRuleWithNoRealm2() {
        org.apache.ambari.server.controller.AuthToLocalBuilder builder = new org.apache.ambari.server.controller.AuthToLocalBuilder("EXAMPLE.COM", java.util.Collections.emptyList(), false);
        builder.addRules("RULE:[1:$1/$2](foobar/someHost)s/.*/hdfs/");
        org.junit.Assert.assertEquals("RULE:[1:$1/$2](foobar/someHost)s/.*/hdfs/\n" + ("RULE:[1:$1@$0](.*@EXAMPLE.COM)s/@.*//\n" + "DEFAULT"), builder.generate());
    }

    @org.junit.Test(expected = java.lang.IllegalArgumentException.class)
    public void testAddNewRuleWithNoRealm() {
        org.apache.ambari.server.controller.AuthToLocalBuilder builder = new org.apache.ambari.server.controller.AuthToLocalBuilder("EXAMPLE.COM", java.util.Collections.emptyList(), false);
        builder.addRule("someUser", "hdfs");
    }

    @org.junit.Test(expected = java.lang.IllegalArgumentException.class)
    public void testAddNewRuleWithNoRealm2() {
        org.apache.ambari.server.controller.AuthToLocalBuilder builder = new org.apache.ambari.server.controller.AuthToLocalBuilder("EXAMPLE.COM", java.util.Collections.emptyList(), false);
        builder.addRule("someUser/someHost", "hdfs");
    }

    @org.junit.Test
    public void testExistingWildcardRealm() {
        org.apache.ambari.server.controller.AuthToLocalBuilder builder = new org.apache.ambari.server.controller.AuthToLocalBuilder("EXAMPLE.COM", java.util.Collections.emptyList(), false);
        builder.addRules("RULE:[2:$1@$0]([rn]m@.*)s/.*/yarn/\n" + (("RULE:[2:$1@$0]([nd]n@.*)s/.*/hdfs/\n" + "RULE:[2:$1@$0](.*@EXAMPLE.COM)s/.*/yarn/\n") + "DEFAULT"));
        builder.addRule("nn/_HOST@EXAMPLE.COM", "hdfs");
        builder.addRule("jn/_HOST@EXAMPLE.COM", "hdfs");
        org.junit.Assert.assertEquals("RULE:[1:$1@$0](.*@EXAMPLE.COM)s/@.*//\n" + ((((("RULE:[2:$1@$0](jn@EXAMPLE.COM)s/.*/hdfs/\n" + "RULE:[2:$1@$0](nn@EXAMPLE.COM)s/.*/hdfs/\n") + "RULE:[2:$1@$0](.*@EXAMPLE.COM)s/.*/yarn/\n") + "RULE:[2:$1@$0]([nd]n@.*)s/.*/hdfs/\n") + "RULE:[2:$1@$0]([rn]m@.*)s/.*/yarn/\n") + "DEFAULT"), builder.generate());
    }

    @org.junit.Test
    public void testClone() throws java.lang.CloneNotSupportedException {
        org.apache.ambari.server.controller.AuthToLocalBuilder builder = new org.apache.ambari.server.controller.AuthToLocalBuilder("EXAMPLE.COM", java.util.Collections.emptyList(), false);
        builder.addRule("nn/_HOST@EXAMPLE.COM", "hdfs");
        builder.addRule("dn/_HOST@EXAMPLE.COM", "hdfs");
        builder.addRule("jn/_HOST@EXAMPLE.COM", "hdfs");
        builder.addRule("rm/_HOST@EXAMPLE.COM", "yarn");
        builder.addRule("jhs/_HOST@EXAMPLE.COM", "mapred");
        builder.addRule("hm/_HOST@EXAMPLE.COM", "hbase");
        builder.addRule("rs/_HOST@EXAMPLE.COM", "hbase");
        builder.addRule("foobar@EXAMPLE.COM", "hdfs");
        org.apache.ambari.server.controller.AuthToLocalBuilder copy = ((org.apache.ambari.server.controller.AuthToLocalBuilder) (builder.clone()));
        org.junit.Assert.assertNotSame(builder, copy);
        org.junit.Assert.assertEquals(builder.generate(), copy.generate());
        builder.addRule("user@EXAMPLE.COM", "hdfs");
        org.junit.Assert.assertTrue(!copy.generate().equals(builder.generate()));
    }

    @org.junit.Test
    public void testAdditionalRealms() {
        org.apache.ambari.server.controller.AuthToLocalBuilder builder = new org.apache.ambari.server.controller.AuthToLocalBuilder("EXAMPLE.COM", "REALM2,REALM3, REALM1  ", false);
        builder.addRules("RULE:[1:$1@$0](.*@FOOBAR.COM)s/@.*//\n" + "DEFAULT");
        builder.addRule("nn/_HOST@EXAMPLE.COM", "hdfs");
        builder.addRule("dn/_HOST@EXAMPLE.COM", "hdfs");
        builder.addRule("jn/_HOST@EXAMPLE.COM", "hdfs");
        builder.addRule("rm/_HOST@EXAMPLE.COM", "yarn");
        builder.addRule("jhs/_HOST@EXAMPLE.COM", "mapred");
        builder.addRule("hm/_HOST@EXAMPLE.COM", "hbase");
        builder.addRule("rs/_HOST@EXAMPLE.COM", "hbase");
        java.util.List<java.lang.String> rules = java.util.Arrays.asList("RULE:[1:$1@$0](.*@FOOBAR.COM)s/@.*//", "RULE:[1:$1@$0](.*@EXAMPLE.COM)s/@.*//", "RULE:[1:$1@$0](.*@REALM2)s/@.*//", "RULE:[1:$1@$0](.*@REALM1)s/@.*//", "RULE:[1:$1@$0](.*@REALM3)s/@.*//", "RULE:[2:$1@$0](dn@EXAMPLE.COM)s/.*/hdfs/", "RULE:[2:$1@$0](hm@EXAMPLE.COM)s/.*/hbase/", "RULE:[2:$1@$0](jhs@EXAMPLE.COM)s/.*/mapred/", "RULE:[2:$1@$0](jn@EXAMPLE.COM)s/.*/hdfs/", "RULE:[2:$1@$0](nn@EXAMPLE.COM)s/.*/hdfs/", "RULE:[2:$1@$0](rm@EXAMPLE.COM)s/.*/yarn/", "RULE:[2:$1@$0](rs@EXAMPLE.COM)s/.*/hbase/", "DEFAULT");
        org.junit.Assert.assertTrue(org.apache.ambari.server.utils.CollectionPresentationUtils.isStringPermutationOfCollection(builder.generate(), rules, "\n", 0, 0));
    }

    @org.junit.Test
    public void testAdditionalRealms_Null() {
        org.apache.ambari.server.controller.AuthToLocalBuilder builder = new org.apache.ambari.server.controller.AuthToLocalBuilder("EXAMPLE.COM", java.util.Collections.emptyList(), false);
        builder.addRule("nn/_HOST@EXAMPLE.COM", "hdfs");
        builder.addRule("dn/_HOST@EXAMPLE.COM", "hdfs");
        builder.addRule("jn/_HOST@EXAMPLE.COM", "hdfs");
        builder.addRule("rm/_HOST@EXAMPLE.COM", "yarn");
        builder.addRule("jhs/_HOST@EXAMPLE.COM", "mapred");
        builder.addRule("hm/_HOST@EXAMPLE.COM", "hbase");
        builder.addRule("rs/_HOST@EXAMPLE.COM", "hbase");
        org.junit.Assert.assertEquals("RULE:[1:$1@$0](.*@EXAMPLE.COM)s/@.*//\n" + ((((((("RULE:[2:$1@$0](dn@EXAMPLE.COM)s/.*/hdfs/\n" + "RULE:[2:$1@$0](hm@EXAMPLE.COM)s/.*/hbase/\n") + "RULE:[2:$1@$0](jhs@EXAMPLE.COM)s/.*/mapred/\n") + "RULE:[2:$1@$0](jn@EXAMPLE.COM)s/.*/hdfs/\n") + "RULE:[2:$1@$0](nn@EXAMPLE.COM)s/.*/hdfs/\n") + "RULE:[2:$1@$0](rm@EXAMPLE.COM)s/.*/yarn/\n") + "RULE:[2:$1@$0](rs@EXAMPLE.COM)s/.*/hbase/\n") + "DEFAULT"), builder.generate());
    }

    @org.junit.Test
    public void testAdditionalRealms_Empty() {
        org.apache.ambari.server.controller.AuthToLocalBuilder builder = new org.apache.ambari.server.controller.AuthToLocalBuilder("EXAMPLE.COM", "", false);
        builder.addRule("nn/_HOST@EXAMPLE.COM", "hdfs");
        builder.addRule("dn/_HOST@EXAMPLE.COM", "hdfs");
        builder.addRule("jn/_HOST@EXAMPLE.COM", "hdfs");
        builder.addRule("rm/_HOST@EXAMPLE.COM", "yarn");
        builder.addRule("jhs/_HOST@EXAMPLE.COM", "mapred");
        builder.addRule("hm/_HOST@EXAMPLE.COM", "hbase");
        builder.addRule("rs/_HOST@EXAMPLE.COM", "hbase");
        org.junit.Assert.assertEquals("RULE:[1:$1@$0](.*@EXAMPLE.COM)s/@.*//\n" + ((((((("RULE:[2:$1@$0](dn@EXAMPLE.COM)s/.*/hdfs/\n" + "RULE:[2:$1@$0](hm@EXAMPLE.COM)s/.*/hbase/\n") + "RULE:[2:$1@$0](jhs@EXAMPLE.COM)s/.*/mapred/\n") + "RULE:[2:$1@$0](jn@EXAMPLE.COM)s/.*/hdfs/\n") + "RULE:[2:$1@$0](nn@EXAMPLE.COM)s/.*/hdfs/\n") + "RULE:[2:$1@$0](rm@EXAMPLE.COM)s/.*/yarn/\n") + "RULE:[2:$1@$0](rs@EXAMPLE.COM)s/.*/hbase/\n") + "DEFAULT"), builder.generate());
    }

    @org.junit.Test
    public void testUseCase() {
        org.apache.ambari.server.controller.AuthToLocalBuilder builder = new org.apache.ambari.server.controller.AuthToLocalBuilder("EXAMPLE.COM", "FOOBAR.COM,HW.HDP,BAZ.NET", false);
        java.lang.String existingRules = "RULE:[1:$1@$0](.*@BAZ.NET)s/@.*//\n" + (((((((((((((((((((((("RULE:[1:$1@$0](accumulo-c1@EXAMPLE.COM)s/.*/accumulo/\n" + "RULE:[1:$1@$0](ambari-qa-c1@EXAMPLE.COM)s/.*/ambari-qa/\n") + "RULE:[1:$1@$0](hbase-c1@EXAMPLE.COM)s/.*/hbase/\n") + "RULE:[1:$1@$0](hdfs-c1@EXAMPLE.COM)s/.*/hdfs/\n") + "RULE:[1:$1@$0](spark-c1@EXAMPLE.COM)s/.*/spark/\n") + "RULE:[1:$1@$0](tracer-c1@EXAMPLE.COM)s/.*/accumulo/\n") + "RULE:[1:$1@$0](.*@EXAMPLE.COM)s/@.*//\n") + "RULE:[1:$1@$0](.*@FOOBAR.COM)s/@.*//\n") + "RULE:[1:$1@$0](.*@HW.HDP)s/@.*//\n") + "RULE:[2:$1@$0](accumulo@EXAMPLE.COM)s/.*/accumulo/\n") + "RULE:[2:$1@$0](amshbase@EXAMPLE.COM)s/.*/ams/\n") + "RULE:[2:$1@$0](amszk@EXAMPLE.COM)s/.*/ams/\n") + "RULE:[2:$1@$0](dn@EXAMPLE.COM)s/.*/hdfs/\n") + "RULE:[2:$1@$0](falcon@EXAMPLE.COM)s/.*/falcon/\n") + "RULE:[2:$1@$0](hbase@EXAMPLE.COM)s/.*/hbase/\n") + "RULE:[2:$1@$0](hive@EXAMPLE.COM)s/.*/hive/\n") + "RULE:[2:$1@$0](jhs@EXAMPLE.COM)s/.*/mapred/\n") + "RULE:[2:$1@$0](nm@EXAMPLE.COM)s/.*/yarn/\n") + "RULE:[2:$1@$0](nn@EXAMPLE.COM)s/.*/hdfs/\n") + "RULE:[2:$1@$0](oozie@EXAMPLE.COM)s/.*/oozie/\n") + "RULE:[2:$1@$0](rm@EXAMPLE.COM)s/.*/yarn/\n") + "RULE:[2:$1@$0](yarn@EXAMPLE.COM)s/.*/yarn/\n") + "DEFAULT");
        builder.addRules(existingRules);
        builder.addRule("nn/_HOST@EXAMPLE.COM", "hdfs");
        builder.addRule("dn/_HOST@EXAMPLE.COM", "hdfs");
        builder.addRule("rm/_HOST@EXAMPLE.COM", "yarn");
        builder.addRule("yarn/_HOST@EXAMPLE.COM", "yarn");
        builder.addRule("kafka/_HOST@EXAMPLE.COM", null);
        builder.addRule("hdfs-c1@EXAMPLE.COM", "hdfs");
        org.junit.Assert.assertEquals(existingRules, builder.generate());
    }

    @org.junit.Test
    public void testCustomRuleCanBeAddedWithCaseSensitivity() {
        org.apache.ambari.server.controller.AuthToLocalBuilder builder = new org.apache.ambari.server.controller.AuthToLocalBuilder("EXAMPLE.COM", java.util.Collections.emptyList(), false).addRule("yarn/_HOST@EXAMPLE.COM", "yarn").addRules("RULE:[1:$1@$0](.*@HDP01.LOCAL)s/.*/ambari-qa//L\n" + ("RULE:[2:$1@$0](yarn@EXAMPLE.COM)s/.*/yarn/\n" + "DEFAULT"));
        org.junit.Assert.assertEquals("RULE:[1:$1@$0](.*@EXAMPLE.COM)s/@.*//\n" + (("RULE:[1:$1@$0](.*@HDP01.LOCAL)s/.*/ambari-qa//L\n" + "RULE:[2:$1@$0](yarn@EXAMPLE.COM)s/.*/yarn/\n") + "DEFAULT"), builder.generate());
    }

    @org.junit.Test
    public void testCaseSensitivityFlagIsRemovedAfterItWasAddedToAmbariRule() {
        org.apache.ambari.server.controller.AuthToLocalBuilder builder = new org.apache.ambari.server.controller.AuthToLocalBuilder("EXAMPLE.COM", java.util.Collections.emptyList(), false).addRule("yarn/_HOST@EXAMPLE.COM", "yarn").addRules("RULE:[2:$1@$0](yarn@EXAMPLE.COM)s/.*/yarn//L\n" + "DEFAULT");
        org.junit.Assert.assertEquals("RULE:[1:$1@$0](.*@EXAMPLE.COM)s/@.*//\n" + ("RULE:[2:$1@$0](yarn@EXAMPLE.COM)s/.*/yarn/\n" + "DEFAULT"), builder.generate());
    }

    @org.junit.Test
    public void testCaseSensitivityFlagIsAddedAfterItWasFromAmbariRule() {
        org.apache.ambari.server.controller.AuthToLocalBuilder builder = new org.apache.ambari.server.controller.AuthToLocalBuilder("EXAMPLE.COM", java.util.Collections.emptyList(), true).addRule("yarn/_HOST@EXAMPLE.COM", "yarn").addRules("RULE:[1:$1@$0](.*@EXAMPLE.COM)s/@.*//\n" + ("RULE:[2:$1@$0](yarn@EXAMPLE.COM)s/.*/yarn/\n" + "DEFAULT"));
        org.junit.Assert.assertEquals("RULE:[1:$1@$0](.*@EXAMPLE.COM)s/@.*///L\n" + ("RULE:[2:$1@$0](yarn@EXAMPLE.COM)s/.*/yarn/\n" + "DEFAULT"), builder.generate());
    }
}