package com.example.socks.model;

public enum Color {
    RED("Красный"), WHITE("Белый"), BLUE("Синий"), GREEN("Зеленый"), BLACK("Черный"), YELLOW("Желтый"), ORANGE("Оранжевый");
    private String nameColor;

    Color(String nameColor) {
        this.nameColor = nameColor;
    }

    public String getNameColor() {
        return nameColor;
    }

}
