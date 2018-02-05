package fr.phenixmc.client.shield;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AntiAutoThread implements ActionListener {
	
        @Override
        public void actionPerformed(ActionEvent arg0) {
         System.err.println("AntiAuto du serveur PhenixMc.");
         AntiAuto.clics = 0;
        }
}