package com.dominic.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.Random;
public class MainScreen2 implements Screen {

    private static int score=0, highscore2;
    private Stage stage;
    private Orchestrator parent; // a field to store our orchestrator
    private TextureAtlas textureAtlas;
    private FitViewport viewport;
    private OrthographicCamera cam;
    private ShapeRenderer sr;
    private SpriteBatch batch;

    //score and screen decls
    private BitmapFont font, scorefont;
    private Vector3 touchPoint;
    private float width, height, screenL, screenR, screenT, screenB;
    private int sm=1;
    private Rectangle screen;

    //boost decls
    private Sprite boost;
    private Vector3 posB;
    private float RB=50f, vB=0.01f, vBfrailMulti=20f, vBfrailMult=vBfrailMulti, dvBfrailMult=1.5f, dSturdyTimeB=0.3f;
    private Rectangle boostRectangle;

    // player1 decls
    private BitmapFont player1font;
    private Sprite player1, player1hurt, player1protect;
    private Vector3 pos1;
    private int lives1i=3, lives1=lives1i, isImmortal1=0, player1powers=100, isProtected=0;
    private float player1L, player1R, player1T, player1B, xi1, yi1, v1 = 0.35f, R1=60f;
    private float dRclick=0.17f, timeUntilMortalMax1=5f, timeUntilMortal1=0f;
    private Rectangle player1Rectangle;

    // player2 decls
    private BitmapFont player2font;
    private Sprite player2, player2hurt, player2speedy;
    private Vector3 pos2;
    private int lives2i=6, lives2=lives2i, isImmortal2=0, player2powers=100, isSpeedy=0;
    private float player2L, player2R, player2T, player2B, xi2, yi2, v2i = 0.45f, R2i=120f, v2=v2i, R2=R2i;
    private float timeUntilMortalMax2=1f, timeUntilMortal2=0f, ImmortalPower=0.01f;
    private Rectangle player2Rectangle;

    // enemy decls
    private Sprite enemy, enemyfrail;
    private Vector3 posE, posEOLD;
    private int isFrail=0, firstEnemyGrowth=0;
    private float enemyL, enemyR, enemyT, enemyB;
    private float vEi = 0.3f, vE=vEi, dvE = 0.05f, ddvE=0.015f, vEmax=1f, REi=100f, RE=REi, Rrprint=REi, dRE=5f, ddRE=1f, vEStore;
    private float timeUntilFrail=0f, timeUntilFrailMin=5f,timeUntilFrailMax=10f, timeUntilSturdy=0f, timeUntilSturdyMin=2f, timeUntilSturdyMax=4f;
    private Rectangle enemyRectangle;

    Random rand = new Random();

    public static int getScore() {
        return score;
    }

    public MainScreen2(Orchestrator orchestrator){
        stage = new Stage(new ScreenViewport());
        parent = orchestrator;
        sr = new ShapeRenderer();

        //screen properties
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        screen = new Rectangle(0, 0, width, height);
        screenL = screen.getX();
        screenB = screen.getY();
        screenR = screenL + width;
        screenT = screenB + height;

        cam = new OrthographicCamera();
        cam.setToOrtho(false,width, height);
        viewport = new FitViewport(width,height,cam);
        // sprites
        textureAtlas = new TextureAtlas("sprites.txt");
        batch = new SpriteBatch();
        player1 = textureAtlas.createSprite("player1_2P");
        player1hurt = textureAtlas.createSprite("player1hurt_2P");
        player1protect = textureAtlas.createSprite("player1_2P_protect");
        player2 = textureAtlas.createSprite("player2");
        player2hurt = textureAtlas.createSprite("player2hurt");
        player2speedy = textureAtlas.createSprite("player2_speedy");
        enemy = textureAtlas.createSprite("enemy");
        enemyfrail = textureAtlas.createSprite("enemyfrail");
        boost = textureAtlas.createSprite("hourglass");

        // start player1
        xi1 = 3f*width / 4;
        yi1 = height / 6;
        pos1 = new Vector3(xi1, yi1, 0);

        // start player2
        xi2 = width / 4;
        yi2 = height / 6;
        pos2 = new Vector3(xi2, yi2, 0);

        // start enemy at top 1/5, middle third
        posE = new Vector3((rand.nextFloat() * 0.33f+0.33f)* width, (rand.nextFloat() * 0.2f + 0.8f) * height, 0);

        // start boost in middle two thirds
        posB = new Vector3(rand.nextFloat() * width, (rand.nextFloat() * 0.33f + 0.33f) * height, 0);

        // vector for mouse click
        touchPoint = new Vector3();

        // font
        font = new BitmapFont();
        font.setColor(Color.RED);
        player1font = new BitmapFont();
        player1font.setColor(Color.GREEN);
        player2font = new BitmapFont();
        player2font.setColor(Color.BLUE);
        scorefont = new BitmapFont();
        scorefont.setColor(Color.BLACK);
    }

    @Override
    public void show() {
        stage.clear();
        Gdx.input.setInputProcessor(stage);
    }

    //TODO: add instructions
    //TODO: double click for some reason still presses buttons on menu screen, so can exit
    @Override
    public void render(float delta) {
        //`~~~~~~~~~~~reset screen with white background~~~~~~~~~~~~~~~~~~~
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        cam.update();
//        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
//        stage.draw();

        //~~~~~~~~~~~~~~~~~~~display data~~~~~~~~~~~~~~~~~~~~
        batch.begin();
        font.draw(batch, "Speed: " + String.format("%.3f", vE / vEmax), 0.75f * width, 0.9f * height);
        font.draw(batch, "Size: " + String.format("%.3f", RE / Rrprint), 0.75f * width, 0.85f * height);
        font.draw(batch, "Time until Frail: " + String.format("%.2f", timeUntilFrail), 0.75f * width, 0.8f * height);
        font.draw(batch, "Time until Sturdy: " + String.format("%.2f", timeUntilSturdy), 0.75f * width, 0.75f * height);
        player1font.draw(batch, "LIVES: " + lives1, 0.05f * width, 0.9f * height);
        player1font.draw(batch, "Immortality: " + String.format("%.2f", timeUntilMortal1), 0.05f * width, 0.85f * height);
        player1font.draw(batch, "Power: " + player1powers, 0.05f * width, 0.8f * height);
        scorefont.draw(batch, "Score: " + score + "    (Multiplier: " + sm + ")", 0.05f * width, 0.95f * height);
        scorefont.draw(batch, "Frail multiplier: " + String.format("%.2f", vBfrailMult), 0.3f * width, 0.95f * height);
        player2font.draw(batch, "LIVES: " + lives2, 0.3f * width, 0.9f * height);
        player2font.draw(batch, "Immortality: " + String.format("%.2f", timeUntilMortal2), 0.3f * width, 0.85f * height);
        player2font.draw(batch, "Powers: " + player2powers, 0.3f * width, 0.8f * height);
        batch.end();

        //~~~~~~~~~~~~~~~~~~draw sprites~~~~~~~~~~~~~~~~~~~~~~~~~~~
        //remove player sprites if they have lives <= 0
        batch.begin();
        if(lives1>0) {
            player1.setPosition(pos1.x, pos1.y);
            player1.setSize(R1, R1);
            player1hurt.setPosition(pos1.x, pos1.y);
            player1hurt.setSize(R1, R1);
            player1protect.setPosition(pos1.x, pos1.y);
            player1protect.setSize(R1, R1);
        }else{
            player1.setPosition(-width, -height);
            player1hurt.setPosition(-width, -height);
            player1protect.setPosition(-width, -height);
        }
        if (lives2 > 0) {
            player2.setPosition(pos2.x, pos2.y);
            player2.setSize(R2, R2);
            player2hurt.setPosition(pos2.x, pos2.y);
            player2hurt.setSize(R2, R2);
            player2speedy.setPosition(pos2.x, pos2.y);
            player2speedy.setSize(R2, R2);
        }else{
            player2.setPosition(-width, -height);
            player2hurt.setPosition(-width, -height);
        }

        enemy.setPosition(posE.x, posE.y);
        enemy.setSize(RE, RE);
        enemyfrail.setPosition(posE.x, posE.y);
        enemyfrail.setSize(RE, RE);
        if (isFrail == 0) {//draw sturdy enemy or frail enemy depending on timer
            enemy.draw(batch);
        } else {
            enemyfrail.draw(batch);
        }
        if (lives1 > 0) {
            if (isImmortal1 == 0) {//draw mortal or immortal player depending on timer
                player1.draw(batch);
            } else {
                if(isProtected == 0){//draw protected or immortal
                    player1hurt.draw(batch);
                }else{
                    player1protect.draw(batch);
                }

            }
        }
        if (lives2 > 0) {
            if (isImmortal2 == 0) {//draw mortal or immortal player depending on timer
                if(isSpeedy == 0){ //draw speedy or non-speedy
                    player2.draw(batch);
                }else{
                    player2speedy.draw(batch);
                }

            } else {
                player2hurt.draw(batch);
            }
        }
        boost.setPosition(posB.x, posB.y);
        boost.setSize(RB, RB);
        boost.draw(batch);
        batch.end();

        //~~~~~~~~~~~~~~~~~get boundaries~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        if (isImmortal1 == 0) {//draw mortal or immortal player 1 depending on timer
            player1Rectangle = player1.getBoundingRectangle();
        } else {
            if(isProtected == 0){//draw protected or immortal
                player1Rectangle = player1hurt.getBoundingRectangle();
            }else{
                player1Rectangle = player1protect.getBoundingRectangle();
            }
        }

        if(isImmortal2==0) {//draw mortal or immortal player 2 depending on timer
            if(isSpeedy == 0){ //draw speedy or non-speedy
                player2Rectangle = player2.getBoundingRectangle();
            }else{
                player2Rectangle = player2speedy.getBoundingRectangle();
            }
        }else{
            player2Rectangle = player2hurt.getBoundingRectangle();
        }
        if(isFrail==0){
            enemyRectangle = enemy.getBoundingRectangle();
        }else{
            enemyRectangle = enemyfrail.getBoundingRectangle();
        }
        boostRectangle = boost.getBoundingRectangle();

        //~~~~~~~~~~~~~~~~~ move player1 with arrow keys~~~~~~~~~~~~~~~~~~`
        if (Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT) )
            pos1.x -= Gdx.graphics.getDeltaTime() * v1 * width;
        if (Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT) )
            pos1.x += Gdx.graphics.getDeltaTime() * v1 * width;
        if (Gdx.input.isKeyPressed(Input.Keys.DPAD_UP))
            pos1.y += Gdx.graphics.getDeltaTime() * v1 * width;
        if (Gdx.input.isKeyPressed(Input.Keys.DPAD_DOWN))
            pos1.y -= Gdx.graphics.getDeltaTime() * v1 * width;

        //~~~~~~~~~~~~~~~~~ move player2 with WASD~~~~~~~~~~~~~~~~~~`
        if (Gdx.input.isKeyPressed(Input.Keys.A) )
            pos2.x -= Gdx.graphics.getDeltaTime() * v2 * width;
        if (Gdx.input.isKeyPressed(Input.Keys.D) )
            pos2.x += Gdx.graphics.getDeltaTime() * v2 * width;
        if (Gdx.input.isKeyPressed(Input.Keys.W) )
            pos2.y += Gdx.graphics.getDeltaTime() * v2 * width;
        if (Gdx.input.isKeyPressed(Input.Keys.S) )
            pos2.y -= Gdx.graphics.getDeltaTime() * v2 * width;

        //~~~~~~~~~~~~~~~~~ Add player 1 special power ~~~~~~~~~~~~~~~~~~`
        //player 1 can increase the speed and decrease radius of player 2
        if (Gdx.input.isKeyPressed(Input.Keys.SLASH) && player1powers>0 ) {
            player1powers -= 1;
            v2=2f*v2i;
            R2=0.5f*R2i;
            isSpeedy=1;
        }else{
            v2=v2i;
            R2=R2i;
            isSpeedy=0;
        }

        //~~~~~~~~~~~~~~~~~ Add player 2 special power ~~~~~~~~~~~~~~~~~~`
        //player 2 can give player 1 immortality (PROTECT)
        if ((Gdx.input.isKeyPressed(Input.Keys.C) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) && player2powers>0 ) {
            player2powers -= 1;
            isImmortal1=1;
            isProtected=1;
            timeUntilMortal1+=ImmortalPower;
        }else{
            isProtected=0;
        }

        //~~~~~~~~~~~~~~~~~~~~move enemy downwards at speed v~~~~~~~~~~~~~~~~~~~~
        posEOLD = posE;
        posE.set(posEOLD.x, posEOLD.y - Gdx.graphics.getDeltaTime() * vE * height, 0);

        // if RED circle falls off screen then respawn
        if (posE.y < 0) {
            posE.x = rand.nextFloat() * (width-enemyRectangle.getWidth());
            posE.y = (rand.nextFloat() * 0.2f + 0.8f) * height;

            if (isFrail==0) {
                // iff sturdy increase speed increment up to maximum
                if (vE < vEmax) vE += dvE;

                // iff sturdy and speed is maximum then increase radius
                if (vE >= vEmax || firstEnemyGrowth == 1){
                    RE += dRE;
                }

            }

            if (vE > vEmax) {
                vE = vEmax;
            }

        }

        //~~~~~~~~~~~~~~~~~~~~~~~ if player 1 or 2 collides with boost ~~~~~~~~~~~~~~~~~~~
        boolean isOverlapingBoost1 = player1Rectangle.overlaps(boostRectangle);
        boolean isOverlapingBoost2 = player2Rectangle.overlaps(boostRectangle);
        if (isOverlapingBoost1 || isOverlapingBoost2) {
            // increase score by 5
            score+=sm*5;

            if(isFrail==0){
                //if enemy is sturdy, reduce the speed of the enemy by the boost amount
                if(vE>=vEi){
                    vE -= vB*(1+(vE-vEi)/(vEmax-vEi));
                }else{
                    vE -=vB;
                }
            }else{ //otherwise enemy is frail
                // increse time until sturdy by 0.3 (smaller than single player)
                timeUntilSturdy += dSturdyTimeB;

                //decrease the speed of the sturdy enemy
                vEStore -= vBfrailMult*vB;

                if(vEStore<0){
                    vEStore=0;
                }
            }

            // reposition the boost
            posB.x = rand.nextFloat()*(width-boostRectangle.getWidth());
            posB.y = (rand.nextFloat() * 0.33f + 0.33f) * height;

            // redraw the boost sprite
            batch.begin();
            boost.setPosition(posB.x, posB.y);
            boost.setSize(RB, RB);
            boost.draw(batch);
            batch.end();

            // if enemy speed reduces to 0 or less than
            if (vE <= 0){
                //if either player had zero lives before, need to reset their positions
                if(lives1==0){
                    pos1.x=xi1;
                    pos1.y=yi1;
                    lives1+=1;
                }
                if(lives2==0){
                    pos2.x=xi2;
                    pos2.y=yi2;
                    lives2+=1;
                }

                //gain powers
                player1powers+=50;
                player2powers+=50;

                //gain score and score multiplier increases
                score+=300*sm;
                sm+=1;

                //enemy grows faster and speeds up faster
                dvE+=ddvE;
                dRE+=ddRE;

                //reset positions
                posE.x = rand.nextFloat() * (width-enemyRectangle.getWidth());
                posE.y = (rand.nextFloat() * 0.2f + 0.8f) * height;

                //reset enemy properties
                isFrail=0;
                timeUntilFrail=0;
                timeUntilSturdy=0;
                firstEnemyGrowth=0;
                REi += 2f*dRE;
                if(vBfrailMult>10+dvBfrailMult) {
                    vBfrailMult -= dvBfrailMult;
                }
                RE=REi;
                vE = vEi;


            }
        }

        //~~~~~~~~~~~~~~~~~~if enemy has grown, allow to become frail~~~~~~~~~~
        if (RE>REi || firstEnemyGrowth == 1) { //enemy starts to grow if Rr>Rri
            // First time around need to set time.
            if(firstEnemyGrowth==0){
                timeUntilFrail = (rand.nextFloat() * (timeUntilFrailMax - timeUntilFrailMin) + timeUntilFrailMin) * 1f;
                firstEnemyGrowth=1;
            }
            if(isFrail == 0) { // enemy is sturdy
                if (timeUntilFrail <= 0) { // frail timer has ended, enemy becomes frail
                    isFrail = 1; // turn frail checker on
                    timeUntilFrail=0;

                    //start the timer until sturdy
                    timeUntilSturdy = (rand.nextFloat() * (timeUntilSturdyMax - timeUntilSturdyMin) + timeUntilSturdyMin) * 1f;

                    vEStore = vE; // save current speed

                    vE = vE / 3; // decrease enemy speed
                } else { // otherwise decrease timer until frail begins
                    timeUntilFrail -= Gdx.graphics.getDeltaTime();
                }
            }else {
                if (timeUntilSturdy <= 0) { // sturdy timer has ended, enemy becomes sturdy
                    isFrail = 0; // turn frail checker off
                    timeUntilSturdy=0;

                    //start the timer until frail
                    timeUntilFrail = (rand.nextFloat() * (timeUntilFrailMax - timeUntilFrailMin) + timeUntilFrailMin) * 1f;

                    vE = vEStore; // return to speed saved before turning frail

                } else { // otherwise decrease timer until sturdy begins
                    timeUntilSturdy -= Gdx.graphics.getDeltaTime();
                }
            }

        }

        //~~~~~~~~~~~~~~~~~~ decrease frail enemy size by clicking on it ~~~~~~~~~~~~~~~~
        // when the cursor is pressed
        if(Gdx.input.isTouched()) {
            cam.unproject(touchPoint.set(Gdx.input.getX(),Gdx.input.getY(),0));
            if(isFrail==1 && enemyRectangle.contains(touchPoint.x,touchPoint.y)) {
                // have clicked on the frail enemy so decrease radius of dRclick
                RE-=dRclick;
                if(RE<REi){
                    RE=REi;
                }
            }
        }

        //~~~~~~~~~~~~~~~~~~player1 collides with enemy~~~~~~~~~~~~~~~~~~~~~
        boolean isOverlaping1 = player1Rectangle.overlaps(enemyRectangle);
        if (isOverlaping1) {

            if (isImmortal1==0) { // if not immortal
                //reduce number of lives by 1
                lives1 -= 1;

                //start immortality timer
                isImmortal1=1;
                timeUntilMortal1=timeUntilMortalMax1;

            }


        }
        //check if timer has ran out
        if (timeUntilMortal1<=0){
            //turn off immortality
            isImmortal1=0;
            timeUntilMortal1=0;
        }else{// if immortal
            //reduce immortality timer
            timeUntilMortal1-=Gdx.graphics.getDeltaTime();
        }

        //~~~~~~~~~~~~~~~~~~player2 collides with enemy~~~~~~~~~~~~~~~~~~~~~
        boolean isOverlaping2 = player2Rectangle.overlaps(enemyRectangle);
        if (isOverlaping2) {

            if (isImmortal2==0) { // if not immortal
                //reduce number of lives by 1
                lives2 -= 1;

                //start immortality timer
                isImmortal2=1;
                timeUntilMortal2=timeUntilMortalMax2;

            }


        }
        //check if timer has ran out
        if (timeUntilMortal2<=0){
            //turn off immortality
            isImmortal2=0;
            timeUntilMortal2=0;
        }else{// if immortal
            //reduce immortality timer
            timeUntilMortal2-=Gdx.graphics.getDeltaTime();
        }

        //~~~~~~~~~~~~~~~~~~if both players runs out of lives~~~~~~~~~~~~~~~~~~~~~~~
        if (lives1==0 && lives2==0){
            // set high score in preferences if it is larger than current high score
            highscore2 = parent.getPreferences().getHighScore2();
            if(score>highscore2){
                parent.getPreferences().setHighScore2(score);
            }

            // change screen to exit screen
            parent.changeScreen(Orchestrator.ENDGAME);

            // reset the score, lives, velocity, size, timerFrail and timeUntilFrail
            score = 0;
            lives1=lives1i;
            lives2=lives2i;
            vE = vEi;
            RE = REi;
            vBfrailMult=vBfrailMulti;
            isFrail=0;
            isImmortal1=0;
            isImmortal2=0;
            timeUntilMortal1=0;
            timeUntilFrail=0;
            timeUntilSturdy=0;
            firstEnemyGrowth=0;

            //reset positions
            pos1.x = xi1;
            pos1.y = yi1;
            pos2.x = xi2;
            pos2.y = yi2;
            posE.x = rand.nextFloat() * (width-enemyRectangle.getWidth());
            posE.y = (rand.nextFloat() * 0.2f + 0.8f) * height;
        }


        //~~~~~~~~~~~~~~~~~~prevent player1 walking off screen~~~~~~~~~~~~~~~~~~~~~
        //player bounds
        player1L = player1Rectangle.getX();
        player1B = player1Rectangle.getY();
        player1R = player1Rectangle.getX() + player1Rectangle.getWidth();
        player1T = player1Rectangle.getY() + player1Rectangle.getHeight();

        // Correct horizontal axis
        if (player1L < screenL) {
            // Clamp to left
            pos1.x = screenL + 0.1f*(player1R - player1L);
        } else if (player1R > screenR) {
            // Clamp to right
            pos1.x = screenR - (player1R - player1L);
        }

        // Correct vertical axis
        if (player1B < screenB) {
            // Clamp to bottom
            pos1.y = screenB + 0.1f*(player1T - player1B);
        } else if (player1T > screenT) {
            // Clamp to top
            pos1.y = screenT - (player1T - player1B);
        }


        //~~~~~~~~~~~~~~~~~~prevent player2 walking off screen~~~~~~~~~~~~~~~~~~~~~
        //player bounds
        player2L = player2Rectangle.getX();
        player2B = player2Rectangle.getY();
        player2R = player2Rectangle.getX() + player2Rectangle.getWidth();
        player2T = player2Rectangle.getY() + player2Rectangle.getHeight();

        // Correct horizontal axis
        if (player2L < screenL) {
            // Clamp to left
            pos2.x = screenL + 0.1f*(player2R - player2L);
        } else if (player2R > screenR) {
            // Clamp to right
            pos2.x = screenR - (player2R - player2L);
        }

        // Correct vertical axis
        if (player2B < screenB) {
            // Clamp to bottom
            pos2.y = screenB + 0.1f*(player2T - player2B);
        } else if (player1T > screenT) {
            // Clamp to top
            pos2.y = screenT - (player2T - player2B);
        }


        // TODO: add background


    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        batch.setProjectionMatrix(cam.combined);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        sr.dispose();
        batch.dispose();
        font.dispose();
        player1font.dispose();
        player2font.dispose();
        scorefont.dispose();
        textureAtlas.dispose();
        stage.dispose();
    }
}