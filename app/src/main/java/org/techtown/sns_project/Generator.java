package org.techtown.sns_project;

import java.util.ArrayList;

public abstract class Generator {

    private ArrayList<org.techtown.sns_project.Observer> observers = new ArrayList<org.techtown.sns_project.Observer>();

    public void AddObserver(Observer observer) {
        observers.add(observer);
    }
    public void NotifyObservers() {
        for(int i=0; i<observers.size(); i++) {
            observers.get(i).update(this);
        }
    }
    public abstract int getInt();
    public abstract void execute();
}
