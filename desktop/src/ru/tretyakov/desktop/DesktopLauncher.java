package ru.tretyakov.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import ru.tretyakov.StarGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		System.setProperty("user.name", "\\xD0\\x95\\xD0\\xB2\\xD0\\xB3\\xD0\\xB5\\xD0\\xBD");
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new StarGame(), config);
	}
}
