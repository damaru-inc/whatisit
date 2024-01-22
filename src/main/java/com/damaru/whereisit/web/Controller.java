package com.damaru.whereisit.web;

import org.jboss.logging.Logger;

import com.damaru.whereisit.model.EditAction;

public class Controller {
    
    private static Logger logger = Logger.getLogger(Controller.class.getName());

    private EditAction editAction = EditAction.none;
    
    private String message = "";
    
    public boolean getEditable() {
        boolean ret = editAction == EditAction.create || editAction == EditAction.edit;
        logger.tracef("getEditable: action is %s, ret is %s", editAction, ret);
        //return editAction == EditAction.create || editAction == EditAction.edit;
        return ret;
    }

    public EditAction getEditAction() {
        return editAction;
    }

    public void setEditAction(EditAction editAction) {
        this.editAction = editAction;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
