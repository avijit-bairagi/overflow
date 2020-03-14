package com.mrrobot.overflow.common.utils;

public enum UserLevel {

    BEGINNER(1, "BEGINNER", 100, 1.0),
    MASTER(2, "MASTER", 1000, 1.5),
    PRO(3, "PRO", 2000, 1.8);


    private final Integer level;
    private final String name;
    private final Integer levelPoint;
    private final Double value;


    UserLevel(int level, String name, int levelPoint, Double value) {
        this.level = level;
        this.name = name;
        this.levelPoint = levelPoint;
        this.value = value;
    }

    public static UserLevel findByLevel(Integer level) {
        for (UserLevel ul : values()) {
            if (ul.getLevel() == level) {
                return ul;
            }
        }
        return BEGINNER;
    }

    public int getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }

    public int getLevelPoint() {
        return levelPoint;
    }

    public Double getValue() {
        return value;
    }
}
