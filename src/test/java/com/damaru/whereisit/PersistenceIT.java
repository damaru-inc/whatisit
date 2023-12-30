package com.damaru.whereisit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.damaru.whereisit.model.Container;
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
                .addClasses(Container.class, Room.class, Repository.class, Resources.class)
                .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                // Deploy our test datasource
                .addAsWebInfResource("test-ds.xml");
        System.out.println(archive.toString(true));
        return archive;
    }
    
    
    @Before
    public void reset() {
        log.info("Resetting.");
        repository.cleanDb();
    }

    @Test
    public void testSaveRoom() {
        Room room = new Room();
        room.setName("Living Room");
        repository.save(room);
        assertNotNull(room.getId());
        log.info(room.getName() + " was persisted with id " + room.getId());
        
        List<Room> rooms = repository.findAllRooms();
        
        for (Room r : rooms) {
            log.info(r.toString());
        }
    }

    @Test
    public void testSaveContainer() {
        Room room = new Room();
        room.setName("Living Room");
        repository.save(room);
        Container container = new Container();
        container.setName("Desk");
        container.setRoom(room);
        repository.save(container);
        assertNotNull(container.getId());
        log.info(container.getName() + " was persisted with id " + container.getId());
        
        
        List<Container> containers = repository.findAllContainers();
        
        for (Container c : containers) {
            log.info(c.toString());
        }
    }
    
    
    @Test
    public void testFetchContainersByRoom() {
        Room room = new Room();
        room.setName("Living Room");
        repository.save(room);
        
        Container desk = new Container();
        desk.setName("Desk");
        desk.setRoom(room);
        repository.save(desk);
        
        Container cabinet = new Container();
        cabinet.setName("Cabinet");
        cabinet.setRoom(room);
        repository.save(cabinet);
        
        List<Container> containers = repository.findContainersByRoom(room);
        assertEquals(2, containers.size());
        assertEquals(cabinet, containers.get(0));
        assertEquals(desk, containers.get(1));
        
    }

}
