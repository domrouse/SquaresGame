package com.dominic.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class EndScreen implements Screen {

    private Orchestrator parent; // a field to store our orchestrator
    private Stage stage;

    public EndScreen(Orchestrator orchestrator){
        parent = orchestrator;
        stage = new Stage(new ScreenViewport());

    }

    @Override
    public void show() {
        stage.clear();
        Gdx.input.setInputProcessor(stage);

        // Create a table that fills the screen. Everything else will go inside this table.
        Table table = new Table();
        table.setFillParent(true);
//        table.setDebug(true);
        stage.addActor(table);

        // get skins
        Skin skin = new Skin(Gdx.files.internal("skin/glassy-ui.json"));

        // create buttons
        TextButton newGame = new TextButton("Retry", skin);
        TextButton mainMenu = new TextButton("Main menu", skin);
        TextButton exit = new TextButton("Exit", skin);

        // get 'score' integer from game, and 'highscore' from preferences
        int score = MainScreen.getScore();
        int score2 = MainScreen2.getScore();
        int highscore = parent.getPreferences().getHighScore();
        int highscore2 = parent.getPreferences().getHighScore2();

        // add buttons to the table
        table.add(new Label("1P score = "+Integer.toString(score)+"    1P high score = "+Integer.toString(highscore),skin)).left();
        table.row();
        table.add(new Label("2P score = "+Integer.toString(score2)+"    2P high score = "+Integer.toString(highscore2),skin)).left();
        table.row();
        table.add(newGame).fillX().uniformX();
        table.row().pad(10, 0, 10, 0);
        table.add(mainMenu).fillX().uniformX();
        table.row();
        table.add(exit).fillX().uniformX();

        // create button listeners
        // exit method
        exit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        // start a new game
        newGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.changeScreen(Orchestrator.APPLICATION);
            }
        });

        // go to the Main menu
        mainMenu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.changeScreen(Orchestrator.MENU);
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub
    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub
    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}