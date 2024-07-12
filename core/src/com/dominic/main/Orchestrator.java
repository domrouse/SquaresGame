package com.dominic.main;

import com.badlogic.gdx.Game;

public class Orchestrator extends Game {

	private LoadingScreen loadingScreen;
	private PreferencesScreen preferencesScreen;
	private MenuScreen menuScreen;
	private MainScreen mainScreen;
	private EndScreen endScreen;
	private appPreferences preferences;
	private MainScreen2 mainScreen2;

	public final static int MENU = 0;
	public final static int PREFERENCES = 1;
	public final static int APPLICATION = 2;
	public final static int ENDGAME = 3;
	public final static int APPLICATION2 = 4;



	@Override
	public void create() {
		//set initial screen
		loadingScreen = new LoadingScreen(this);
		setScreen(loadingScreen);
		preferences = new appPreferences();

	}

	// screen changer
	public void changeScreen(int screen){
		switch(screen){
			case MENU:
				if(menuScreen == null) menuScreen = new MenuScreen(this);
				this.setScreen(menuScreen);
				break;
			case PREFERENCES:
				if(preferencesScreen == null) preferencesScreen = new PreferencesScreen(this);
				this.setScreen(preferencesScreen);
				break;
			case APPLICATION:
				if(mainScreen == null) mainScreen = new MainScreen(this);
				this.setScreen(mainScreen);
				break;
			case ENDGAME:
				if(endScreen == null) endScreen = new EndScreen(this);
				this.setScreen(endScreen);
				break;
			case APPLICATION2:
				if(mainScreen2 == null) mainScreen2 = new MainScreen2(this);
				this.setScreen(mainScreen2);
				break;
		}
	}

	public appPreferences getPreferences() {
		return this.preferences;
	}


}
