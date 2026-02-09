package org.example;


public class Main {
    public static void main(String[] args) {
        VetCare controller = VetCare.getInstance();
        Menu menu = new Menu(controller);
        menu.start();
    }
}
