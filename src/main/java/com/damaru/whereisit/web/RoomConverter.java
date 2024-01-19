package com.damaru.whereisit.web;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import org.jboss.logging.Logger;

import com.damaru.whereisit.model.Room;
import com.damaru.whereisit.service.Repository;
import com.damaru.whereisit.util.Resources;

@FacesConverter(value="roomConvertor")
public class RoomConverter implements Converter<Room> {
    private static final long serialVersionUID = 1L;
    private static Logger logger = Logger.getLogger(RoomConverter.class.getName());
    
    @Override
    public Room getAsObject(FacesContext context, UIComponent component,
                                String value)
            throws ConverterException {
        
        logger.tracef("getAsObject: %s", value);
        
        Repository repository = Resources.lookup(Repository.class);

        if (value == null) {
            return null;
        }

        Room room = null;
        try {
            room = repository.findRoomByName(value);
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error occurred while converting a room.", e.getMessage()));
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return room;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component,
            Room room)
            throws ConverterException {
        
        logger.tracef("getAsString: %s", room);

        if (room == null) {
            return null;
        }

        return room.getName();
    }

}
