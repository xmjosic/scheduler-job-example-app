package hr.xmjosic.schedulerjobexampleapp.service.impl;

import hr.xmjosic.schedulerjobexampleapp.service.SchedulerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import net.javacrumbs.shedlock.support.KeepAliveLockProvider;
import net.javacrumbs.shedlock.support.StorageBasedLockProvider;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class SchedulerServiceImpl implements SchedulerService {

    private final LockProvider lockProvider;

    @Scheduled(cron = "${scheduler.job.cron}")
    @SchedulerLock(name = "NiceAndEasy", lockAtLeastFor = "PT20S", lockAtMostFor = "PT20S")
    @Override
    public void doEveryFiveMinutes() {
        log.info("Scheduler job started at: " + LocalDateTime.now().toString());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            log.error("Exception occurred.");
        }
        this.clearCache();
        log.info("Scheduler job finish at " + LocalDateTime.now().toString());
    }

    private void clearCache() {
        log.info("clear cache start");
        ((StorageBasedLockProvider) lockProvider).clearCache();
        log.info("cache clear end");
    }
}
