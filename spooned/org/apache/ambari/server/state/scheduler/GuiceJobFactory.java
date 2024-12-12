package org.apache.ambari.server.state.scheduler;
import org.quartz.Job;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;
public class GuiceJobFactory implements org.quartz.spi.JobFactory {
    private final com.google.inject.Injector injector;

    @com.google.inject.Inject
    public GuiceJobFactory(com.google.inject.Injector injector) {
        this.injector = injector;
    }

    @java.lang.Override
    public org.quartz.Job newJob(org.quartz.spi.TriggerFiredBundle bundle, org.quartz.Scheduler scheduler) throws org.quartz.SchedulerException {
        return injector.getInstance(bundle.getJobDetail().getJobClass());
    }
}