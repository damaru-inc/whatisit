package com.damaru.whereisit;

import static org.junit.Assert.assertNotNull;

import java.util.logging.Logger;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.damaru.whereisit.model.Room;
import com.damaru.whereisit.service.Repository;
import com.damaru.whereisit.util.Resources;

@RunWith(Arquillian.class)
public class PersistenceIT {

    @Inject
    Repository repository;

    @Inject
    Logger log;

    @Deployment
    public static Archive<?> createTestArchive() {
        WebArchive archive = ShrinkWrap.create(WebArchive.class, "test.war")
                .addClasses(Room.class, Repository.class, Resources.class)
                .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                // Deploy our test datasource
                .addAsWebInfResource("test-ds.xml");
        System.out.println(archive.toString(true));
        return archive;
    }

    @Test
    public void testSaveRoom() {
        Room room = new Room();
        room.setName("Living Room");
        repository.save(room);
        assertNotNull(room.getId());
        log.info(room.getName() + " was persisted with id " + room.getId());

    }
    
}
