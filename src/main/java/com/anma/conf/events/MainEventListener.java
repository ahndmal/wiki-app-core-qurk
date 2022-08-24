package com.anma.conf.events;

import com.anma.models.db.Page;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import io.quarkus.runtime.StartupEvent;
import io.smallrye.mutiny.Uni;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import java.util.Random;

@ApplicationScoped
public class MainEventListener {

    Logger LOG = LoggerFactory.getLogger(this.getClass().getName());

//    void onMessage(@Observes Event msg) {
//        msg.fireAsync("").ac
//    }

    void startup(@Observes StartupEvent event) {
        LOG.info(">>> Startup :: UP");
        Page page = new Page();
        page.setAuthorId(new Random().nextInt(1, 5));
        Uni<PanacheEntityBase> savedPage = page.persist();
        savedPage.subscribe();
        LOG.info(">>> page saved " + page.id);

        Page.count().subscribe().with(
                it -> System.out.println(it),
                failure -> System.out.println("Failed with " + failure));
    }
}
