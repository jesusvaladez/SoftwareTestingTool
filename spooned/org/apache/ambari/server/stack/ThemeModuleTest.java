package org.apache.ambari.server.stack;
public class ThemeModuleTest {
    @org.junit.Test
    public void testResolve() throws java.lang.Exception {
        java.io.File parentThemeFile = new java.io.File(this.getClass().getClassLoader().getResource("parent-theme.json").getFile());
        java.io.File childThemeFile = new java.io.File(this.getClass().getClassLoader().getResource("child-theme.json").getFile());
        org.apache.ambari.server.stack.ThemeModule parentModule = new org.apache.ambari.server.stack.ThemeModule(parentThemeFile);
        org.apache.ambari.server.stack.ThemeModule childModule = new org.apache.ambari.server.stack.ThemeModule(childThemeFile);
        childModule.resolve(parentModule, null, null, null);
        org.apache.ambari.server.state.theme.Theme childTheme = childModule.getModuleInfo().getThemeMap().get(org.apache.ambari.server.stack.ThemeModule.THEME_KEY);
        org.apache.ambari.server.state.theme.Theme parentTheme = parentModule.getModuleInfo().getThemeMap().get(org.apache.ambari.server.stack.ThemeModule.THEME_KEY);
        org.junit.Assert.assertNotNull(childTheme.getThemeConfiguration().getLayouts());
        org.junit.Assert.assertEquals(10, parentTheme.getThemeConfiguration().getPlacement().getConfigs().size());
        org.junit.Assert.assertEquals(12, childTheme.getThemeConfiguration().getPlacement().getConfigs().size());
        org.junit.Assert.assertEquals(10, parentTheme.getThemeConfiguration().getWidgets().size());
        org.junit.Assert.assertEquals(12, childTheme.getThemeConfiguration().getWidgets().size());
    }

    @org.junit.Test
    public void testAddErrors() {
        java.util.Set<java.lang.String> errors = com.google.common.collect.ImmutableSet.of("one error", "two errors");
        org.apache.ambari.server.stack.ThemeModule module = new org.apache.ambari.server.stack.ThemeModule(((java.io.File) (null)));
        module.addErrors(errors);
        org.junit.Assert.assertEquals(errors, com.google.common.collect.ImmutableSet.copyOf(module.getErrors()));
    }
}