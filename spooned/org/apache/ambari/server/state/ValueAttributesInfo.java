package org.apache.ambari.server.state;
import io.swagger.annotations.ApiModelProperty;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.type.TypeReference;
@javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.FIELD)
@org.codehaus.jackson.map.annotate.JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ValueAttributesInfo implements org.apache.ambari.server.controller.ApiModel {
    public static final java.lang.String EMPTY_VALUE_VALID = "empty_value_valid";

    public static final java.lang.String UI_ONLY_PROPERTY = "ui_only_property";

    public static final java.lang.String READ_ONLY = "read_only";

    public static final java.lang.String EDITABLE_ONLY_AT_INSTALL = "editable_only_at_install";

    public static final java.lang.String SHOW_PROPERTY_NAME = "show_property_name";

    public static final java.lang.String INCREMENT_STEP = "increment_step";

    public static final java.lang.String SELECTION_CARDINALITY = "selection_cardinality";

    public static final java.lang.String PROPERTY_FILE_NAME = "property-file-name";

    public static final java.lang.String PROPERTY_FILE_TYPE = "property-file-type";

    public static final java.lang.String ENTRIES = "entries";

    public static final java.lang.String HIDDEN = "hidden";

    public static final java.lang.String ENTRIES_EDITABLE = "entries_editable";

    public static final java.lang.String USER_GROUPS = "user-groups";

    public static final java.lang.String KEYSTORE = "keystore";

    private java.lang.String type;

    private java.lang.String maximum;

    private java.lang.String minimum;

    private java.lang.String unit;

    private java.lang.String delete;

    private java.lang.Boolean visible;

    private java.lang.Boolean overridable;

    private java.lang.String copy;

    @javax.xml.bind.annotation.XmlElement(name = "empty-value-valid")
    @org.codehaus.jackson.annotate.JsonProperty(org.apache.ambari.server.state.ValueAttributesInfo.EMPTY_VALUE_VALID)
    private java.lang.Boolean emptyValueValid;

    @javax.xml.bind.annotation.XmlElement(name = "ui-only-property")
    @org.codehaus.jackson.annotate.JsonProperty(org.apache.ambari.server.state.ValueAttributesInfo.UI_ONLY_PROPERTY)
    private java.lang.Boolean uiOnlyProperty;

    @javax.xml.bind.annotation.XmlElement(name = "read-only")
    @org.codehaus.jackson.annotate.JsonProperty(org.apache.ambari.server.state.ValueAttributesInfo.READ_ONLY)
    private java.lang.Boolean readOnly;

    @javax.xml.bind.annotation.XmlElement(name = "editable-only-at-install")
    @org.codehaus.jackson.annotate.JsonProperty(org.apache.ambari.server.state.ValueAttributesInfo.EDITABLE_ONLY_AT_INSTALL)
    private java.lang.Boolean editableOnlyAtInstall;

    @javax.xml.bind.annotation.XmlElement(name = "show-property-name")
    @org.codehaus.jackson.annotate.JsonProperty(org.apache.ambari.server.state.ValueAttributesInfo.SHOW_PROPERTY_NAME)
    private java.lang.Boolean showPropertyName;

    @javax.xml.bind.annotation.XmlElement(name = "increment-step")
    @org.codehaus.jackson.annotate.JsonProperty(org.apache.ambari.server.state.ValueAttributesInfo.INCREMENT_STEP)
    private java.lang.String incrementStep;

    @javax.xml.bind.annotation.XmlElementWrapper(name = org.apache.ambari.server.state.ValueAttributesInfo.ENTRIES)
    @javax.xml.bind.annotation.XmlElements(@javax.xml.bind.annotation.XmlElement(name = "entry"))
    private java.util.Collection<org.apache.ambari.server.state.ValueEntryInfo> entries;

    @javax.xml.bind.annotation.XmlElement(name = org.apache.ambari.server.state.ValueAttributesInfo.HIDDEN)
    private java.lang.String hidden;

    @javax.xml.bind.annotation.XmlElement(name = org.apache.ambari.server.state.ValueAttributesInfo.ENTRIES_EDITABLE)
    private java.lang.Boolean entriesEditable;

    @javax.xml.bind.annotation.XmlElement(name = "selection-cardinality")
    @org.codehaus.jackson.annotate.JsonProperty(org.apache.ambari.server.state.ValueAttributesInfo.SELECTION_CARDINALITY)
    private java.lang.String selectionCardinality;

    @javax.xml.bind.annotation.XmlElement(name = org.apache.ambari.server.state.ValueAttributesInfo.PROPERTY_FILE_NAME)
    @org.codehaus.jackson.annotate.JsonProperty(org.apache.ambari.server.state.ValueAttributesInfo.PROPERTY_FILE_NAME)
    private java.lang.String propertyFileName;

    @javax.xml.bind.annotation.XmlElement(name = org.apache.ambari.server.state.ValueAttributesInfo.PROPERTY_FILE_TYPE)
    @org.codehaus.jackson.annotate.JsonProperty(org.apache.ambari.server.state.ValueAttributesInfo.PROPERTY_FILE_TYPE)
    private java.lang.String propertyFileType;

    @javax.xml.bind.annotation.XmlElementWrapper(name = org.apache.ambari.server.state.ValueAttributesInfo.USER_GROUPS)
    @javax.xml.bind.annotation.XmlElements(@javax.xml.bind.annotation.XmlElement(name = "property"))
    private java.util.Collection<org.apache.ambari.server.state.UserGroupInfo> userGroupEntries;

    @javax.xml.bind.annotation.XmlElement(name = org.apache.ambari.server.state.ValueAttributesInfo.KEYSTORE)
    private boolean keyStore;

    public ValueAttributesInfo() {
    }

    @io.swagger.annotations.ApiModelProperty(name = "type")
    public java.lang.String getType() {
        return type;
    }

    public void setType(java.lang.String type) {
        this.type = type;
    }

    @io.swagger.annotations.ApiModelProperty(name = "maximum")
    public java.lang.String getMaximum() {
        return maximum;
    }

    public void setMaximum(java.lang.String maximum) {
        this.maximum = maximum;
    }

    @io.swagger.annotations.ApiModelProperty(name = "minimum")
    public java.lang.String getMinimum() {
        return minimum;
    }

    public void setMinimum(java.lang.String minimum) {
        this.minimum = minimum;
    }

    @io.swagger.annotations.ApiModelProperty(name = "unit")
    public java.lang.String getUnit() {
        return unit;
    }

    public void setUnit(java.lang.String unit) {
        this.unit = unit;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.state.ValueAttributesInfo.ENTRIES)
    public java.util.Collection<org.apache.ambari.server.state.ValueEntryInfo> getEntries() {
        return entries;
    }

    public void setEntries(java.util.Collection<org.apache.ambari.server.state.ValueEntryInfo> entries) {
        this.entries = entries;
    }

    @io.swagger.annotations.ApiModelProperty(name = "user-group-entries")
    public java.util.Collection<org.apache.ambari.server.state.UserGroupInfo> getUserGroupEntries() {
        return userGroupEntries;
    }

    public void setUserGroupEntries(java.util.Collection<org.apache.ambari.server.state.UserGroupInfo> userGroupEntries) {
        this.userGroupEntries = userGroupEntries;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.state.ValueAttributesInfo.HIDDEN)
    public java.lang.String getHidden() {
        return hidden;
    }

    public void setHidden(java.lang.String hidden) {
        this.hidden = hidden;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.state.ValueAttributesInfo.ENTRIES_EDITABLE)
    public java.lang.Boolean getEntriesEditable() {
        return entriesEditable;
    }

    public void setEntriesEditable(java.lang.Boolean entriesEditable) {
        this.entriesEditable = entriesEditable;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.state.ValueAttributesInfo.SELECTION_CARDINALITY)
    public java.lang.String getSelectionCardinality() {
        return selectionCardinality;
    }

    public void setSelectionCardinality(java.lang.String selectionCardinality) {
        this.selectionCardinality = selectionCardinality;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.state.ValueAttributesInfo.PROPERTY_FILE_NAME)
    public java.lang.String getPropertyFileName() {
        return propertyFileName;
    }

    public void setPropertyFileName(java.lang.String propertyFileName) {
        this.propertyFileName = propertyFileName;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.state.ValueAttributesInfo.PROPERTY_FILE_TYPE)
    public java.lang.String getPropertyFileType() {
        return propertyFileType;
    }

    public void setPropertyFileType(java.lang.String propertyFileType) {
        this.propertyFileType = propertyFileType;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.state.ValueAttributesInfo.INCREMENT_STEP)
    public java.lang.String getIncrementStep() {
        return incrementStep;
    }

    public void setIncrementStep(java.lang.String incrementStep) {
        this.incrementStep = incrementStep;
    }

    @io.swagger.annotations.ApiModelProperty(name = "delete")
    public java.lang.String getDelete() {
        return delete;
    }

    public void setDelete(java.lang.String delete) {
        this.delete = delete;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.state.ValueAttributesInfo.EMPTY_VALUE_VALID)
    public java.lang.Boolean getEmptyValueValid() {
        return emptyValueValid;
    }

    public void setEmptyValueValid(java.lang.Boolean isEmptyValueValid) {
        this.emptyValueValid = isEmptyValueValid;
    }

    @io.swagger.annotations.ApiModelProperty(name = "visible")
    public java.lang.Boolean getVisible() {
        return visible;
    }

    public void setVisible(java.lang.Boolean isVisible) {
        this.visible = isVisible;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.state.ValueAttributesInfo.READ_ONLY)
    public java.lang.Boolean getReadOnly() {
        return readOnly;
    }

    public void setReadOnly(java.lang.Boolean isReadOnly) {
        this.readOnly = isReadOnly;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.state.ValueAttributesInfo.EDITABLE_ONLY_AT_INSTALL)
    public java.lang.Boolean getEditableOnlyAtInstall() {
        return editableOnlyAtInstall;
    }

    public void setEditableOnlyAtInstall(java.lang.Boolean isEditableOnlyAtInstall) {
        this.editableOnlyAtInstall = isEditableOnlyAtInstall;
    }

    @io.swagger.annotations.ApiModelProperty(name = "overridable")
    public java.lang.Boolean getOverridable() {
        return overridable;
    }

    public void setOverridable(java.lang.Boolean isOverridable) {
        this.overridable = isOverridable;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.state.ValueAttributesInfo.SHOW_PROPERTY_NAME)
    public java.lang.Boolean getShowPropertyName() {
        return showPropertyName;
    }

    public void setShowPropertyName(java.lang.Boolean isPropertyNameVisible) {
        this.showPropertyName = isPropertyNameVisible;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.state.ValueAttributesInfo.UI_ONLY_PROPERTY)
    public java.lang.Boolean getUiOnlyProperty() {
        return uiOnlyProperty;
    }

    public void setUiOnlyProperty(java.lang.Boolean isUiOnlyProperty) {
        this.uiOnlyProperty = isUiOnlyProperty;
    }

    @io.swagger.annotations.ApiModelProperty(name = "copy")
    public java.lang.String getCopy() {
        return copy;
    }

    public void setCopy(java.lang.String copy) {
        this.copy = copy;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.state.ValueAttributesInfo.KEYSTORE)
    public boolean isKeyStore() {
        return keyStore;
    }

    public void setKeyStore(boolean keyStore) {
        this.keyStore = keyStore;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.state.ValueAttributesInfo that = ((org.apache.ambari.server.state.ValueAttributesInfo) (o));
        if (entries != null ? !entries.equals(that.entries) : that.entries != null)
            return false;

        if (entriesEditable != null ? !entriesEditable.equals(that.entriesEditable) : that.entriesEditable != null)
            return false;

        if (emptyValueValid != null ? !emptyValueValid.equals(that.emptyValueValid) : that.emptyValueValid != null)
            return false;

        if (visible != null ? !visible.equals(that.visible) : that.visible != null)
            return false;

        if (readOnly != null ? !readOnly.equals(that.readOnly) : that.readOnly != null)
            return false;

        if (editableOnlyAtInstall != null ? !editableOnlyAtInstall.equals(that.editableOnlyAtInstall) : that.editableOnlyAtInstall != null)
            return false;

        if (overridable != null ? !overridable.equals(that.overridable) : that.overridable != null)
            return false;

        if (hidden != null ? !hidden.equals(that.hidden) : that.hidden != null)
            return false;

        if (showPropertyName != null ? !showPropertyName.equals(that.showPropertyName) : that.showPropertyName != null)
            return false;

        if (uiOnlyProperty != null ? !uiOnlyProperty.equals(that.uiOnlyProperty) : that.uiOnlyProperty != null)
            return false;

        if (copy != null ? !copy.equals(that.copy) : that.copy != null)
            return false;

        if (maximum != null ? !maximum.equals(that.maximum) : that.maximum != null)
            return false;

        if (minimum != null ? !minimum.equals(that.minimum) : that.minimum != null)
            return false;

        if (selectionCardinality != null ? !selectionCardinality.equals(that.selectionCardinality) : that.selectionCardinality != null)
            return false;

        if (propertyFileName != null ? !propertyFileName.equals(that.propertyFileName) : that.propertyFileName != null)
            return false;

        if (propertyFileType != null ? !propertyFileType.equals(that.propertyFileType) : that.propertyFileType != null)
            return false;

        if (type != null ? !type.equals(that.type) : that.type != null)
            return false;

        if (unit != null ? !unit.equals(that.unit) : that.unit != null)
            return false;

        if (delete != null ? !delete.equals(that.delete) : that.delete != null)
            return false;

        if (incrementStep != null ? !incrementStep.equals(that.incrementStep) : that.incrementStep != null)
            return false;

        if (userGroupEntries != null ? !userGroupEntries.equals(that.userGroupEntries) : that.userGroupEntries != null)
            return false;

        return true;
    }

    @java.lang.Override
    public int hashCode() {
        int result = (type != null) ? type.hashCode() : 0;
        result = (31 * result) + (hidden != null ? hidden.hashCode() : 0);
        result = (31 * result) + (maximum != null ? maximum.hashCode() : 0);
        result = (31 * result) + (minimum != null ? minimum.hashCode() : 0);
        result = (31 * result) + (unit != null ? unit.hashCode() : 0);
        result = (31 * result) + (delete != null ? delete.hashCode() : 0);
        result = (31 * result) + (entries != null ? entries.hashCode() : 0);
        result = (31 * result) + (entriesEditable != null ? entriesEditable.hashCode() : 0);
        result = (31 * result) + (selectionCardinality != null ? selectionCardinality.hashCode() : 0);
        result = (31 * result) + (propertyFileName != null ? propertyFileName.hashCode() : 0);
        result = (31 * result) + (propertyFileType != null ? propertyFileType.hashCode() : 0);
        result = (31 * result) + (incrementStep != null ? incrementStep.hashCode() : 0);
        result = (31 * result) + (emptyValueValid != null ? emptyValueValid.hashCode() : 0);
        result = (31 * result) + (visible != null ? visible.hashCode() : 0);
        result = (31 * result) + (readOnly != null ? readOnly.hashCode() : 0);
        result = (31 * result) + (editableOnlyAtInstall != null ? editableOnlyAtInstall.hashCode() : 0);
        result = (31 * result) + (overridable != null ? overridable.hashCode() : 0);
        result = (31 * result) + (showPropertyName != null ? showPropertyName.hashCode() : 0);
        result = (31 * result) + (uiOnlyProperty != null ? uiOnlyProperty.hashCode() : 0);
        result = (31 * result) + (copy != null ? copy.hashCode() : 0);
        result = (31 * result) + (userGroupEntries != null ? userGroupEntries.hashCode() : 0);
        return result;
    }

    public java.util.Map<java.lang.String, java.lang.String> toMap(java.util.Optional<org.codehaus.jackson.map.ObjectMapper> mapper) {
        java.util.Map<java.lang.String, java.lang.String> map = mapper.orElseGet(org.codehaus.jackson.map.ObjectMapper::new).convertValue(this, new org.codehaus.jackson.type.TypeReference<java.util.Map<java.lang.String, java.lang.String>>() {});
        if (!java.lang.Boolean.parseBoolean(map.get("keyStore"))) {
            map.remove("keyStore");
        }
        return map;
    }

    public static org.apache.ambari.server.state.ValueAttributesInfo fromMap(java.util.Map<java.lang.String, java.lang.String> attributes, java.util.Optional<org.codehaus.jackson.map.ObjectMapper> mapper) {
        return mapper.orElseGet(org.codehaus.jackson.map.ObjectMapper::new).convertValue(attributes, org.apache.ambari.server.state.ValueAttributesInfo.class);
    }

    @java.lang.Override
    public java.lang.String toString() {
        return (((((((((((((((((((((((((((((((((((((((((((((((((((((((((("ValueAttributesInfo{" + "entries=") + entries) + ", type='") + type) + '\'') + ", maximum='") + maximum) + '\'') + ", minimum='") + minimum) + '\'') + ", unit='") + unit) + '\'') + ", delete='") + delete) + '\'') + ", emptyValueValid='") + emptyValueValid) + '\'') + ", visible='") + visible) + '\'') + ", readOnly='") + readOnly) + '\'') + ", editableOnlyAtInstall='") + editableOnlyAtInstall) + '\'') + ", overridable='") + overridable) + '\'') + ", showPropertyName='") + showPropertyName) + '\'') + ", uiOnlyProperty='") + uiOnlyProperty) + '\'') + ", incrementStep='") + incrementStep) + '\'') + ", entriesEditable=") + entriesEditable) + ", selectionCardinality='") + selectionCardinality) + '\'') + ", propertyFileName='") + propertyFileName) + '\'') + ", propertyFileType='") + propertyFileType) + '\'') + ", copy='") + copy) + '\'') + ", userGroupEntries='") + userGroupEntries) + '\'') + '}';
    }
}