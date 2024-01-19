package com.damaru.whereisit.web;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import org.jboss.logging.Logger;

import com.damaru.whereisit.model.Container;
import com.damaru.whereisit.model.Room;
import com.damaru.whereisit.service.Repository;
import com.damaru.whereisit.util.Resources;

@FacesConverter(value="containerConvertor")
public class ContainerConverter implements Converter<Container> {
    private static final long serialVersionUID = 1L;
    private static Logger logger = Logger.getLogger(ContainerConverter.class.getName());
    
    @Override
    public Container getAsObject(FacesContext context, UIComponent component,
                                String value)
            throws ConverterException {
        
        logger.tracef("getAsObject: %s", value);
        
        Repository repository = Resources.lookup(Repository.class);

        if (value == null) {
            return null;
        }

        Container container = null;
        try {
            container = repository.findContainerByName(value);
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error occurred while converting a room.", e.getMessage()));
            e.printStackTrace();
        }
        
        return container;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component,
            Container container)
            throws ConverterException {
        
        logger.tracef("getAsString: %s", container);

        if (container == null) {
            return null;
        }

        return container.getName();
    }

}
