package com.damaru.whereisit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.damaru.whereisit.model.Container;
import com.damaru.whereisit.model.Item;
import com.damaru.whereisit.model.Room;
import com.damaru.whereisit.model.Saveable;
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
                .addClasses(
                        Container.class,
                        Item.class,
                        Repository.class,
                        Resources.class,
                        Room.class,
                        Saveable.class)
                .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
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
    public void testFindRoomByName() {
        String name = "Living Room";
        Room room = new Room();
        room.setName(name);
        repository.save(room);
        assertNotNull(room.getId());
        log.info(room.getName() + " was persisted with id " + room.getId());
        
        room = repository.findRoomByName(name);
        assertEquals(name, room.getName());
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
    
    
    // This also tests that we can have different containers with the same name,
    // as long as they're in different rooms.
    @Test
    public void testFetchContainersByRoom() {
        Room livingRoom = new Room();
        livingRoom.setName("Living Room");
        repository.save(livingRoom);
        
        Container desk = new Container();
        desk.setName("Desk");
        desk.setRoom(livingRoom);
        repository.save(desk);
        
        Container livingRoomCabinet = new Container();
        livingRoomCabinet.setName("Cabinet");
        livingRoomCabinet.setRoom(livingRoom);
        repository.save(livingRoomCabinet);
        
        List<Container> containers = repository.findContainersByRoom(livingRoom);
        assertEquals(2, containers.size());
        assertEquals(livingRoomCabinet, containers.get(0));
        assertEquals(desk, containers.get(1));
        
        Room bedroom = new Room();
        bedroom.setName("Bedroom");
        repository.save(bedroom);
        
        Container bedroomCabinet = new Container();
        bedroomCabinet.setName("Cabinet");
        bedroomCabinet.setRoom(bedroom);
        repository.save(bedroomCabinet);
        
        containers = repository.findContainersByRoom(bedroom);
        assertEquals(1, containers.size());
        assertEquals(bedroomCabinet, containers.get(0));
    }
    
    @Test
    public void testCreateItemsAndSearch() {
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
        
        Item pen = new Item();
        pen.setName("Fountain Pen");
        pen.setContainer(desk);
        repository.save(pen);
        
        List<Item> items = repository.searchItems("pen");
        assertEquals(1, items.size());
        Item item = items.get(0);
        assertEquals(pen, item);
        assertEquals(desk, item.getContainer());
    }
    

    // This was written to test the item.name index.
    @Ignore
    @Test
    public void makeLotsOfRecords() {
        Room room = new Room();
        room.setName(nextName());
        repository.save(room);
        
        Container desk = new Container();
        desk.setName(nextName());
        desk.setRoom(room);
        repository.save(desk);
        repository.flush();
        
        for (int i = 0; i < 1000; i++) {
            Item pen = new Item();
            pen.setName(nextName());
            pen.setContainer(desk);
            repository.save(pen);
        }
        List<Item> items = repository.searchItems("abc");
        items.stream().close();
        log.info("Results: " + items);
    }
    
    private String nextName() {
        String chars = "abcdefghjklmnpqrstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ23456789";

        Random r = new Random();
        StringBuilder st = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            st.append(chars.charAt(r.nextInt(chars.length())));
        }
        return st.toString();
    }

}
