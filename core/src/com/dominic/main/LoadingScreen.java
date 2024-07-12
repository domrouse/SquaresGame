package com.dominic.main;
import com.badlogic.gdx.Screen;


public class LoadingScreen implements Screen {

    private Orchestrator parent; // a field to store our orchestrator


    public LoadingScreen(Orchestrator orchestrator){
        parent = orchestrator;
    }

    @Override
    public void show() {
        // TODO Auto-generated method stub
    }

    @Override
    public void render(float delta) {
        parent.changeScreen(Orchestrator.MENU);

    }

    @Override
    public void resize(int width, int height) {

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
        // TODO Auto-generated method stub
    }
}


