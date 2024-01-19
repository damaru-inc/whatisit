package com.damaru.whereisit.web;

import com.damaru.whereisit.model.EditAction;

public class Controller {

    private EditAction editAction = EditAction.none;
    
    private String message = "";
    
    public boolean getEditable() {
        return editAction == EditAction.create || editAction == EditAction.edit;
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
