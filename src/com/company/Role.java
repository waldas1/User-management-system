package com.company;

public enum Role {
    SIMPLE(true),
    ADMIN(false);

    private boolean isDeletable;

    Role(boolean isDeletable){
        this.isDeletable = isDeletable;
    }

    public boolean isDeletable(){
        return isDeletable;
    }
}
