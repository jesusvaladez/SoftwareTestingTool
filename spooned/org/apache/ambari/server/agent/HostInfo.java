package org.apache.ambari.server.agent;
import org.codehaus.jackson.annotate.JsonProperty;
public class HostInfo {
    private java.lang.String architecture;

    private java.lang.String domain;

    private java.lang.String fqdn;

    private java.lang.String hardwareisa;

    private java.lang.String hardwaremodel;

    private java.lang.String hostname;

    private java.lang.String id;

    private java.lang.String interfaces;

    private java.lang.String ipaddress;

    private java.lang.String kernel;

    private java.lang.String kernelmajversion;

    private java.lang.String kernelrelease;

    private java.lang.String kernelversion;

    private java.lang.String macaddress;

    private long memoryfree;

    private long memorysize;

    private java.util.List<org.apache.ambari.server.agent.DiskInfo> mounts = new java.util.ArrayList<>();

    private long memorytotal;

    private java.lang.String netmask;

    private java.lang.String operatingsystem;

    private java.lang.String operatingsystemrelease;

    private java.lang.String osfamily;

    private int physicalprocessorcount;

    private int processorcount;

    private boolean selinux;

    private java.lang.String swapfree;

    private java.lang.String swapsize;

    private java.lang.String timezone;

    private java.lang.String uptime;

    private long uptime_days;

    private long uptime_hours;

    @org.codehaus.jackson.annotate.JsonProperty("architecture")
    @com.fasterxml.jackson.annotation.JsonProperty("architecture")
    public java.lang.String getArchitecture() {
        return this.architecture;
    }

    @org.codehaus.jackson.annotate.JsonProperty("architecture")
    @com.fasterxml.jackson.annotation.JsonProperty("architecture")
    public void setArchitecture(java.lang.String architecture) {
        this.architecture = architecture;
    }

    @org.codehaus.jackson.annotate.JsonProperty("domain")
    @com.fasterxml.jackson.annotation.JsonProperty("domain")
    public java.lang.String getDomain() {
        return this.domain;
    }

    @org.codehaus.jackson.annotate.JsonProperty("domain")
    @com.fasterxml.jackson.annotation.JsonProperty("domain")
    public void setDomain(java.lang.String domain) {
        this.domain = domain;
    }

    @org.codehaus.jackson.annotate.JsonProperty("fqdn")
    @com.fasterxml.jackson.annotation.JsonProperty("fqdn")
    public java.lang.String getFQDN() {
        return this.fqdn;
    }

    @org.codehaus.jackson.annotate.JsonProperty("fqdn")
    @com.fasterxml.jackson.annotation.JsonProperty("fqdn")
    public void setFQDN(java.lang.String fqdn) {
        this.fqdn = fqdn;
    }

    @org.codehaus.jackson.annotate.JsonProperty("hardwareisa")
    @com.fasterxml.jackson.annotation.JsonProperty("hardwareisa")
    public java.lang.String getHardwareIsa() {
        return hardwareisa;
    }

    @org.codehaus.jackson.annotate.JsonProperty("hardwareisa")
    @com.fasterxml.jackson.annotation.JsonProperty("hardwareisa")
    public void setHardwareIsa(java.lang.String hardwareisa) {
        this.hardwareisa = hardwareisa;
    }

    @org.codehaus.jackson.annotate.JsonProperty("hardwaremodel")
    @com.fasterxml.jackson.annotation.JsonProperty("hardwaremodel")
    public java.lang.String getHardwareModel() {
        return this.hardwaremodel;
    }

    @org.codehaus.jackson.annotate.JsonProperty("hardwaremodel")
    @com.fasterxml.jackson.annotation.JsonProperty("hardwaremodel")
    public void setHardwareModel(java.lang.String hardwaremodel) {
        this.hardwaremodel = hardwaremodel;
    }

    @org.codehaus.jackson.annotate.JsonProperty("hostname")
    @com.fasterxml.jackson.annotation.JsonProperty("hostname")
    public java.lang.String getHostName() {
        return this.hostname;
    }

    @org.codehaus.jackson.annotate.JsonProperty("hostname")
    @com.fasterxml.jackson.annotation.JsonProperty("hostname")
    public void setHostName(java.lang.String hostname) {
        this.hostname = hostname;
    }

    @org.codehaus.jackson.annotate.JsonProperty("id")
    @com.fasterxml.jackson.annotation.JsonProperty("id")
    public java.lang.String getAgentUserId() {
        return id;
    }

    @org.codehaus.jackson.annotate.JsonProperty("id")
    @com.fasterxml.jackson.annotation.JsonProperty("id")
    public void setAgentUserId(java.lang.String id) {
        this.id = id;
    }

    @org.codehaus.jackson.annotate.JsonProperty("interfaces")
    @com.fasterxml.jackson.annotation.JsonProperty("interfaces")
    public java.lang.String getInterfaces() {
        return this.interfaces;
    }

    @org.codehaus.jackson.annotate.JsonProperty("interfaces")
    @com.fasterxml.jackson.annotation.JsonProperty("interfaces")
    public void setInterfaces(java.lang.String interfaces) {
        this.interfaces = interfaces;
    }

    @org.codehaus.jackson.annotate.JsonProperty("ipaddress")
    @com.fasterxml.jackson.annotation.JsonProperty("ipaddress")
    public java.lang.String getIPAddress() {
        return this.ipaddress;
    }

    @org.codehaus.jackson.annotate.JsonProperty("ipaddress")
    @com.fasterxml.jackson.annotation.JsonProperty("ipaddress")
    public void setIPAddress(java.lang.String ipaddress) {
        this.ipaddress = ipaddress;
    }

    @org.codehaus.jackson.annotate.JsonProperty("kernel")
    @com.fasterxml.jackson.annotation.JsonProperty("kernel")
    public java.lang.String getKernel() {
        return this.kernel;
    }

    @org.codehaus.jackson.annotate.JsonProperty("kernel")
    @com.fasterxml.jackson.annotation.JsonProperty("kernel")
    public void setKernel(java.lang.String kernel) {
        this.kernel = kernel;
    }

    @org.codehaus.jackson.annotate.JsonProperty("kernelmajversion")
    @com.fasterxml.jackson.annotation.JsonProperty("kernelmajversion")
    public java.lang.String getKernelMajVersion() {
        return this.kernelmajversion;
    }

    @org.codehaus.jackson.annotate.JsonProperty("kernelmajversion")
    @com.fasterxml.jackson.annotation.JsonProperty("kernelmajversion")
    public void setKernelMajVersion(java.lang.String kernelmajversion) {
        this.kernelmajversion = kernelmajversion;
    }

    @org.codehaus.jackson.annotate.JsonProperty("kernelrelease")
    @com.fasterxml.jackson.annotation.JsonProperty("kernelrelease")
    public java.lang.String getKernelRelease() {
        return this.kernelrelease;
    }

    @org.codehaus.jackson.annotate.JsonProperty("kernelrelease")
    @com.fasterxml.jackson.annotation.JsonProperty("kernelrelease")
    public void setKernelRelease(java.lang.String kernelrelease) {
        this.kernelrelease = kernelrelease;
    }

    @org.codehaus.jackson.annotate.JsonProperty("kernelversion")
    @com.fasterxml.jackson.annotation.JsonProperty("kernelversion")
    public java.lang.String getKernelVersion() {
        return this.kernelversion;
    }

    @org.codehaus.jackson.annotate.JsonProperty("kernelversion")
    @com.fasterxml.jackson.annotation.JsonProperty("kernelversion")
    public void setKernelVersion(java.lang.String kernelversion) {
        this.kernelversion = kernelversion;
    }

    @org.codehaus.jackson.annotate.JsonProperty("macaddress")
    @com.fasterxml.jackson.annotation.JsonProperty("macaddress")
    public java.lang.String getMacAddress() {
        return this.macaddress;
    }

    @org.codehaus.jackson.annotate.JsonProperty("macaddress")
    @com.fasterxml.jackson.annotation.JsonProperty("macaddress")
    public void setMacAddress(java.lang.String macaddress) {
        this.macaddress = macaddress;
    }

    @org.codehaus.jackson.annotate.JsonProperty("memoryfree")
    @com.fasterxml.jackson.annotation.JsonProperty("memoryfree")
    public long getFreeMemory() {
        return this.memoryfree;
    }

    @org.codehaus.jackson.annotate.JsonProperty("memoryfree")
    @com.fasterxml.jackson.annotation.JsonProperty("memoryfree")
    public void setFreeMemory(long memoryfree) {
        this.memoryfree = memoryfree;
    }

    @org.codehaus.jackson.annotate.JsonProperty("memorysize")
    @com.fasterxml.jackson.annotation.JsonProperty("memorysize")
    public long getMemorySize() {
        return this.memorysize;
    }

    @org.codehaus.jackson.annotate.JsonProperty("memorysize")
    @com.fasterxml.jackson.annotation.JsonProperty("memorysize")
    public void setMemorySize(long memorysize) {
        this.memorysize = memorysize;
    }

    @org.codehaus.jackson.annotate.JsonProperty("mounts")
    @com.fasterxml.jackson.annotation.JsonProperty("mounts")
    public java.util.List<org.apache.ambari.server.agent.DiskInfo> getMounts() {
        return this.mounts;
    }

    @org.codehaus.jackson.annotate.JsonProperty("mounts")
    @com.fasterxml.jackson.annotation.JsonProperty("mounts")
    public void setMounts(java.util.List<org.apache.ambari.server.agent.DiskInfo> mounts) {
        this.mounts = mounts;
    }

    @org.codehaus.jackson.annotate.JsonProperty("memorytotal")
    @com.fasterxml.jackson.annotation.JsonProperty("memorytotal")
    public long getMemoryTotal() {
        return this.memorytotal;
    }

    @org.codehaus.jackson.annotate.JsonProperty("memorytotal")
    @com.fasterxml.jackson.annotation.JsonProperty("memorytotal")
    public void setMemoryTotal(long memorytotal) {
        this.memorytotal = memorytotal;
    }

    @org.codehaus.jackson.annotate.JsonProperty("netmask")
    @com.fasterxml.jackson.annotation.JsonProperty("netmask")
    public java.lang.String getNetMask() {
        return this.netmask;
    }

    @org.codehaus.jackson.annotate.JsonProperty("netmask")
    @com.fasterxml.jackson.annotation.JsonProperty("netmask")
    public void setNetMask(java.lang.String netmask) {
        this.netmask = netmask;
    }

    @org.codehaus.jackson.annotate.JsonProperty("operatingsystem")
    @com.fasterxml.jackson.annotation.JsonProperty("operatingsystem")
    public java.lang.String getOS() {
        return this.operatingsystem;
    }

    @org.codehaus.jackson.annotate.JsonProperty("operatingsystem")
    @com.fasterxml.jackson.annotation.JsonProperty("operatingsystem")
    public void setOS(java.lang.String operatingsystem) {
        this.operatingsystem = operatingsystem;
    }

    @org.codehaus.jackson.annotate.JsonProperty("operatingsystemrelease")
    @com.fasterxml.jackson.annotation.JsonProperty("operatingsystemrelease")
    public java.lang.String getOSRelease() {
        return this.operatingsystemrelease;
    }

    @org.codehaus.jackson.annotate.JsonProperty("operatingsystemrelease")
    @com.fasterxml.jackson.annotation.JsonProperty("operatingsystemrelease")
    public void setOSRelease(java.lang.String operatingsystemrelease) {
        this.operatingsystemrelease = operatingsystemrelease;
    }

    @org.codehaus.jackson.annotate.JsonProperty("osfamily")
    @com.fasterxml.jackson.annotation.JsonProperty("osfamily")
    public java.lang.String getOSFamily() {
        return this.osfamily;
    }

    @org.codehaus.jackson.annotate.JsonProperty("osfamily")
    @com.fasterxml.jackson.annotation.JsonProperty("osfamily")
    public void setOSFamily(java.lang.String osfamily) {
        this.osfamily = osfamily;
    }

    @org.codehaus.jackson.annotate.JsonProperty("physicalprocessorcount")
    @com.fasterxml.jackson.annotation.JsonProperty("physicalprocessorcount")
    public int getPhysicalProcessorCount() {
        return this.physicalprocessorcount;
    }

    @org.codehaus.jackson.annotate.JsonProperty("physicalprocessorcount")
    @com.fasterxml.jackson.annotation.JsonProperty("physicalprocessorcount")
    public void setPhysicalProcessorCount(int physicalprocessorcount) {
        this.physicalprocessorcount = physicalprocessorcount;
    }

    @org.codehaus.jackson.annotate.JsonProperty("processorcount")
    @com.fasterxml.jackson.annotation.JsonProperty("processorcount")
    public int getProcessorCount() {
        return this.processorcount;
    }

    @org.codehaus.jackson.annotate.JsonProperty("processorcount")
    @com.fasterxml.jackson.annotation.JsonProperty("processorcount")
    public void setProcessorCount(int processorcount) {
        this.processorcount = processorcount;
    }

    @org.codehaus.jackson.annotate.JsonProperty("selinux")
    @com.fasterxml.jackson.annotation.JsonProperty("selinux")
    public boolean getSeLinux() {
        return selinux;
    }

    @org.codehaus.jackson.annotate.JsonProperty("selinux")
    @com.fasterxml.jackson.annotation.JsonProperty("selinux")
    public void setSeLinux(boolean selinux) {
        this.selinux = selinux;
    }

    @org.codehaus.jackson.annotate.JsonProperty("swapfree")
    @com.fasterxml.jackson.annotation.JsonProperty("swapfree")
    public java.lang.String getSwapFree() {
        return this.swapfree;
    }

    @org.codehaus.jackson.annotate.JsonProperty("swapfree")
    @com.fasterxml.jackson.annotation.JsonProperty("swapfree")
    public void setSwapFree(java.lang.String swapfree) {
        this.swapfree = swapfree;
    }

    @org.codehaus.jackson.annotate.JsonProperty("swapsize")
    @com.fasterxml.jackson.annotation.JsonProperty("swapsize")
    public java.lang.String getSwapSize() {
        return swapsize;
    }

    @org.codehaus.jackson.annotate.JsonProperty("swapsize")
    @com.fasterxml.jackson.annotation.JsonProperty("swapsize")
    public void setSwapSize(java.lang.String swapsize) {
        this.swapsize = swapsize;
    }

    @org.codehaus.jackson.annotate.JsonProperty("timezone")
    @com.fasterxml.jackson.annotation.JsonProperty("timezone")
    public java.lang.String getTimeZone() {
        return this.timezone;
    }

    @org.codehaus.jackson.annotate.JsonProperty("timezone")
    @com.fasterxml.jackson.annotation.JsonProperty("timezone")
    public void setTimeZone(java.lang.String timezone) {
        this.timezone = timezone;
    }

    @org.codehaus.jackson.annotate.JsonProperty("uptime")
    @com.fasterxml.jackson.annotation.JsonProperty("uptime")
    public java.lang.String getUptime() {
        return this.uptime;
    }

    @org.codehaus.jackson.annotate.JsonProperty("uptime")
    @com.fasterxml.jackson.annotation.JsonProperty("uptime")
    public void setUpTime(java.lang.String uptime) {
        this.uptime = uptime;
    }

    @org.codehaus.jackson.annotate.JsonProperty("uptime_hours")
    @com.fasterxml.jackson.annotation.JsonProperty("uptime_hours")
    public long getUptimeHours() {
        return this.uptime_hours;
    }

    @org.codehaus.jackson.annotate.JsonProperty("uptime_hours")
    @com.fasterxml.jackson.annotation.JsonProperty("uptime_hours")
    public void setUpTimeHours(long uptime_hours) {
        this.uptime_hours = uptime_hours;
    }

    @org.codehaus.jackson.annotate.JsonProperty("uptime_days")
    @com.fasterxml.jackson.annotation.JsonProperty("uptime_days")
    public long getUpTimeDays() {
        return this.uptime_days;
    }

    @org.codehaus.jackson.annotate.JsonProperty("uptime_days")
    @com.fasterxml.jackson.annotation.JsonProperty("uptime_days")
    public void setUpTimeDays(long uptime_days) {
        this.uptime_days = uptime_days;
    }

    private java.lang.String getDiskString() {
        if (mounts == null) {
            return null;
        }
        java.lang.StringBuilder ret = new java.lang.StringBuilder();
        for (org.apache.ambari.server.agent.DiskInfo diskInfo : mounts) {
            ret.append("(").append(diskInfo).append(")");
        }
        return ret.toString();
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ((((((((((((((((((((((((((((((((((("[" + "hostname=") + this.hostname) + ",") + "fqdn=") + this.fqdn) + ",") + "domain=") + this.domain) + ",") + "architecture=") + this.architecture) + ",") + "processorcount=") + this.processorcount) + ",") + "physicalprocessorcount=") + this.physicalprocessorcount) + ",") + "osname=") + this.operatingsystem) + ",") + "osversion=") + this.operatingsystemrelease) + ",") + "osfamily=") + this.osfamily) + ",") + "memory=") + this.memorytotal) + ",") + "uptime_hours=") + this.uptime_hours) + ",") + "mounts=") + getDiskString()) + "]\n";
    }
}