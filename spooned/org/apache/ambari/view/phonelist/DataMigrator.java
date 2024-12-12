package org.apache.ambari.view.phonelist;
public class DataMigrator implements org.apache.ambari.view.migration.ViewDataMigrator {
    @javax.inject.Inject
    private org.apache.ambari.view.ViewContext viewContext;

    @javax.inject.Inject
    private org.apache.ambari.view.migration.ViewDataMigrationContext migrationContext;

    @java.lang.Override
    public boolean beforeMigration() {
        return migrationContext.getOriginDataVersion() == 0;
    }

    @java.lang.Override
    public void afterMigration() {
    }

    @java.lang.Override
    public void migrateEntity(java.lang.Class originEntityClass, java.lang.Class currentEntityClass) throws org.apache.ambari.view.migration.ViewDataMigrationException {
        if (currentEntityClass == org.apache.ambari.view.phonelist.PhoneUser.class) {
            migrationContext.copyAllObjects(originEntityClass, currentEntityClass, new org.apache.ambari.view.phonelist.DataMigrator.PhoneUserConverter());
        } else {
            migrationContext.copyAllObjects(originEntityClass, currentEntityClass);
        }
    }

    @java.lang.Override
    public void migrateInstanceData() {
        for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> userData : migrationContext.getOriginInstanceDataByUser().entrySet()) {
            for (java.util.Map.Entry<java.lang.String, java.lang.String> entry : userData.getValue().entrySet()) {
                java.lang.String newValue = java.lang.String.format("<no surname>;%s", entry.getValue());
                migrationContext.putCurrentInstanceData(userData.getKey(), entry.getKey(), newValue);
            }
        }
    }

    private static class PhoneUserConverter implements org.apache.ambari.view.migration.EntityConverter {
        @java.lang.Override
        public void convert(java.lang.Object orig, java.lang.Object dest) {
            org.apache.ambari.view.phonelist.PhoneUser destPhone = ((org.apache.ambari.view.phonelist.PhoneUser) (dest));
            org.springframework.beans.BeanUtils.copyProperties(orig, dest);
            if (destPhone.getName() == null) {
                destPhone.setSurname("<no surname>");
            } else {
                java.lang.String[] parts = destPhone.getName().split(" ");
                if (parts.length > 1) {
                    destPhone.setSurname(parts[parts.length - 1]);
                } else {
                    destPhone.setSurname("<no surname>");
                }
            }
        }
    }
}