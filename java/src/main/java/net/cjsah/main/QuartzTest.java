package net.cjsah.main;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DateBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import java.util.concurrent.TimeUnit;

@Slf4j
public class QuartzTest {
    public static void main(String[] args) throws SchedulerException, InterruptedException {
        SchedulerFactory factory = new StdSchedulerFactory();
        Scheduler scheduler = factory.getScheduler();
        scheduler.start();
        scheduler.scheduleJob(job(TestJob1.class, "j1"), trigger("t1", 1));
        scheduler.scheduleJob(job(TestJob2.class, "j2"), trigger("t2", 10));
        TimeUnit.MINUTES.sleep(1);
    }

    public static Trigger trigger(String name, int interval) {
        return TriggerBuilder.newTrigger()
                .withIdentity(name, "Main")
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(interval).repeatForever())
                .startAt(DateBuilder.futureDate(interval, DateBuilder.IntervalUnit.SECOND))
                .build();
    }

    public static <T extends Job> JobDetail job(Class<T> clazz, String name) {
        return JobBuilder.newJob(clazz)
                .withIdentity(name, "Main")
                .build();
    }

    public static class TestJob1 implements Job {

        @SneakyThrows
        @Override
        public void execute(JobExecutionContext context) {
            System.out.println("job1 run");
        }
    }

    public static class TestJob2 implements Job {

        @SneakyThrows
        @Override
        public void execute(JobExecutionContext context) {
            System.out.println("job2 run");
            throw new RuntimeException("err");
        }
    }


}
