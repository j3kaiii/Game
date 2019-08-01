package ru.tretyakov;

import com.badlogic.gdx.Game;

import ru.tretyakov.screen.MenuScreen;

public class StarGame extends Game {

	@Override
	public void create() {
		setScreen(new MenuScreen(this));
	}
}
