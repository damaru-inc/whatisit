package com.damaru.whereisit.web;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import org.jboss.logging.Logger;

import com.damaru.whereisit.model.Item;
import com.damaru.whereisit.service.Repository;
import com.damaru.whereisit.util.Resources;

@FacesConverter(value="itemConvertor")
public class ItemConverter implements Converter<Item> {
    private static final long serialVersionUID = 1L;
    private static Logger logger = Logger.getLogger(ItemConverter.class.getName());
    
    @Override
    public Item getAsObject(FacesContext context, UIComponent component,
                                String value)
            throws ConverterException {
        
        logger.tracef("getAsObject: %s", value);
        
        Repository repository = Resources.lookup(Repository.class);

        if (value == null) {
            return null;
        }

        Item item = null;
        try {
            item = repository.findItemByName(value);
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error occurred while converting a item.", e.getMessage()));
            e.printStackTrace();
        }
        
        return item;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component,
            Item item)
            throws ConverterException {
        
        logger.tracef("getAsString: %s", item);

        if (item == null) {
            return null;
        }

        return item.getName();
    }

}
