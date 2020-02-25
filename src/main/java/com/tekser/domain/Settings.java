package com.tekser.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Settings {

    @Id
    private Long id = 0L;

    private int firstRun = 1;

    public Settings() {
    }

    public Settings(Long id, int firstRun) {
        this.id = id;
        this.firstRun = firstRun;
    }

    public int getFirstRun() {
        return firstRun;
    }

    public void setFirstRun(int firstRun) {
        this.firstRun = firstRun;
    }
}
