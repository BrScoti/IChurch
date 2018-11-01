package com.example.aluno.ichurch.Model;

public class Church_Hours {
    String user;
    String schedules_mass;
    String schedules_confession;
    String schedules_other;
    String user_name;

    public String getUser_name() {
        return user_name;
    }


    public String getUser() {
        return user;
    }


    public String getSchedules_mass() {
        return schedules_mass;
    }

    public void setSchedules_mass(String schedules_mass) {
        this.schedules_mass = schedules_mass;
    }

    public String getSchedules_confession() {
        return schedules_confession;
    }

    public void setSchedules_confession(String schedules_confession) {
        this.schedules_confession = schedules_confession;
    }

    public String getSchedules_other() {
        return schedules_other;
    }

    public void setSchedules_other(String schedules_other) {
        this.schedules_other = schedules_other;
    }
}
