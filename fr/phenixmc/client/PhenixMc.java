package fr.phenixmc.client;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

/**
 * @author DreaMzy
 * @category Main
 * 
 * Toutes les nouvelles classes créées pour le projet PhenixMc sont créées spécialement pour une utilisation commerciale.
 * L'utilisation de code "Open Source" permet de ne pas lier les projets personnels et les demandes commerciales.
 * Merci de votre compréhension.
 * 
 */
public class PhenixMc {

	/**
	 * Instance PhenixMc
	 */
	private static PhenixMc phenix;
	
	/**
	 * Datas
	 */
	private String prefix = "[PhenixMc]";
	private String resourcePath = "textures/phenixmc/";
	private int defaultTextColor = 14857473; //Orange
	/**
	 * Constructeur
	 */
	public PhenixMc() {
		this.phenix = this;
	}
	
	/**
	 * @return Récupérer l'instance de PhenixMc
	 */
	public static PhenixMc getPhenix() {
		return phenix;
		
	}
	
	/**
	 * @return Récupérer l'instance de PhenixMc
	 */
	public String getPrefix() {
		return this.prefix;
		
	}

	/**
	 * @return Récuper le chemin d'accès des textures
	 */
	public String getResourcePath() {
		return this.resourcePath;
	}

	/**
	 * @return Couleur par défaut d'un texte
	 */
	public int getDefaultTextColor() {
		return this.defaultTextColor;
	}
	
	/**
	 * @param adress
	 * @param port
	 * @param username
	 */
	public void createNewTeamSpeakConnection(String adress, String port, String username) {

		if (port == null) {
			port = "9987";
		}

		URI connection = URI.create("ts3server://" + adress + "?port=" + port + "&nickname=" + username);

		try {
			Desktop.getDesktop().browse(connection);
		} catch (IOException exception) {
			exception.printStackTrace();
			this.sendDebugIntoConsole(exception.getMessage(), true);

		}
	}

	/**
	 * @param adress
	 */
	public void createNewBrowserConnection(String adress) {

		URI connection = URI.create(adress);

		try {
			Desktop.getDesktop().browse(connection);
		} catch (IOException exception) {
			exception.printStackTrace();
			this.sendDebugIntoConsole(exception.getMessage(), true);

		}
	}

	/**
	 * @param exception
	 * @param prefix
	 */
	public void sendDebugIntoConsole(String exception, boolean prefix) {
		System.out.println("#---------------------------------------------#");
		System.out.println(prefix ? this.prefix : "" + exception);
		System.out.println("#---------------------------------------------#");
	}
	
	 /**
	 * @param url
	 * @return Contenu que retourne l'url
	 */
	public static String getContent(String url) {
		 try {
		  URL website = new URL(url);
		  HttpURLConnection connection = (HttpURLConnection) website.openConnection();
		  BufferedReader br1 = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		  String retu = br1.readLine();
		  br1.close();
		  return retu;
		 } catch(Exception e) {
		  e.printStackTrace();
		  return null;
		 }
		}
}
