module main {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.base;
    requires java.sql;
    requires java.net.http;
    requires jdk.httpserver;

    exports main;
    exports engine;
    exports world;
    exports entities;
    exports ai;
    exports utils;
}
