module Binance {
	exports pl.edu.pw.mini.zpoif.project.binance.dataprocessing.tables.services;
	exports pl.edu.pw.mini.zpoif.project.binance.controllers.services;
	exports pl.edu.pw.mini.zpoif.project.binance.controllers.celltypes;
	exports pl.edu.pw.mini.zpoif.project.binance.currency;
	exports pl.edu.pw.mini.zpoif.project.binance.dataprocessing.chartcontent;
	exports pl.edu.pw.mini.zpoif.project.binance.dataprocessing.chartanalysiscontent;
	exports pl.edu.pw.mini.zpoif.project.binance.main;
	exports pl.edu.pw.mini.zpoif.project.binance.controllers;
	exports pl.edu.pw.mini.zpoif.project.binance.dataprocessing.tables;

	opens pl.edu.pw.mini.zpoif.project.binance.main to javafx.fxml, javafx.graphics, javafx.controls;
	opens pl.edu.pw.mini.zpoif.project.binance.controllers to javafx.fxml, javafx.controls, javafx.graphics;
	
	requires binance.connector.java;
	requires com.fasterxml.jackson.core;
	requires com.fasterxml.jackson.databind;
	requires java.logging;
	requires javafx.base;
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.graphics;
	requires org.controlsfx.controls;
}