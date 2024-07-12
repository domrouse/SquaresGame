package com.dominic.main;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MenuScreen implements Screen {

    private Stage stage;
    private Orchestrator parent; // a field to store our orchestrator




    public MenuScreen(Orchestrator orchestrator){
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
        TextButton newGame = new TextButton("Single player", skin);
        TextButton newGame2 = new TextButton("Two player", skin);
        TextButton preferences = new TextButton("Preferences", skin);
        TextButton exit = new TextButton("Exit", skin);
        TextButton clearHighScore = new TextButton("Clear score", skin);
        TextButton clearHighScore2 = new TextButton("Clear score", skin);

        // get 'highscore' from preferences
        int highscore = parent.getPreferences().getHighScore();
        int highscore2 = parent.getPreferences().getHighScore2();

        // add buttons to the table
        table.add(newGame).colspan(2).fillX().uniformX();
        table.row().pad(10, 0, 0, 0);
        table.add(newGame2).colspan(2).fillX().uniformX();
        table.row().pad(10, 0, 0, 0);
        table.add(preferences).colspan(2).fillX().uniformX();
        table.row().pad(10, 0, 0, 0);
        table.add(exit).colspan(2).fillX().uniformX();
        table.row().pad(10, 0, 10, 0);
        table.add(new Label("1P high score = "+Integer.toString(highscore)+"   ",skin)).left();
        table.add(clearHighScore).right();
        table.row().pad(10, 0, 10, 0);
        table.add(new Label("2P high score = "+Integer.toString(highscore2)+"   ",skin)).left();
        table.add(clearHighScore2).right();

        // create button listeners
        // exit method
        exit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        // start a new single player game
        newGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.changeScreen(Orchestrator.APPLICATION);
            }
        });

        // start a new two player game
        newGame2.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.changeScreen(Orchestrator.APPLICATION2);
            }
        });

        // go to preferences screen
        preferences.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.changeScreen(Orchestrator.PREFERENCES);
            }
        });

        // clear high score saved in preferences, then return to main menu
        clearHighScore.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.getPreferences().setHighScore(0);
                parent.changeScreen(Orchestrator.MENU);
            }
        });

        // clear high score player 2 saved in preferences, then return to main menu
        clearHighScore2.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.getPreferences().setHighScore2(0);
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