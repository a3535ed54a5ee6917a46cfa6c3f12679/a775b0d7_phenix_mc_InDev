package fr.phenixmc.client.shield;

import java.awt.Frame;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;
import javax.swing.Timer;

import net.minecraft.client.Minecraft;

public class AntiAuto extends Thread {
        protected Minecraft mc;

        public static int clics = 0;
        public static int maxclicks = 85;

        public AntiAuto(Minecraft client, String name) {
        (new Timer(5000, new AntiAutoThread())).start();
        }
}