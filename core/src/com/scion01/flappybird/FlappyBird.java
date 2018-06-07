package com.scion01.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] bird;
	Texture[] tubes;
	Texture gameOver;
	int flapState=0;
	float accumulator =0f;
	BitmapFont font;
	float dt;
	float posY=0;
	float speedY=0;
	int gameState=0;
	float gravity=1f;
	float maxTubeOffset;
	Random random;

	float tubeVelocity=4;
	int numberOfTubes=4;
	float[] tubeX = new float[numberOfTubes];
	float[] tubeOffset = new float[numberOfTubes];
	float[] gap= new float[numberOfTubes];
	float distancBetweenTubes;
	Circle birdCircle;
	Rectangle[] rectTopTubes;
	Rectangle[] rectBottomTubes;
	Integer score;
	int scoringTube=0;
	//ShapeRenderer shapeRenderer;
	//this draws shapes, we need shapes for collision detection
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		//batch is 2d rectangular object
		background = new Texture("bg.png");

		bird = new Texture[2];
		tubes = new Texture[2];

		bird[0] = new Texture("bird.png");
		bird[1] = new Texture("bird2.png");

		tubes[0] = new Texture("toptube.png");
		tubes[1] = new Texture("bottomtube.png");



		//maxTubeOffset = Gdx.graphics.getHeight()/2 - gap/2 - 100;
		random = new Random();

		distancBetweenTubes = Gdx.graphics.getWidth()/2+150;

		posY=Gdx.graphics.getHeight()/2-bird[0].getHeight()/2;
		for(int i=0;i<numberOfTubes;i++){
			tubeOffset[i] = (random.nextFloat()-0.5f)*(Gdx.graphics.getHeight()/2-80);
			tubeX[i]= Gdx.graphics.getWidth()+Gdx.graphics.getWidth()/2-tubes[0].getWidth()/2 +i*distancBetweenTubes;
			gap[i]=600;
		}

		birdCircle = new Circle();
		//shapeRenderer =new ShapeRenderer();
		rectTopTubes = new Rectangle[numberOfTubes];
		rectBottomTubes = new Rectangle[numberOfTubes];

		for(int i=0;i<4;i++) {
			rectTopTubes[i] = new Rectangle();
			rectBottomTubes[i] =new Rectangle();
		}
		score=0;
		scoringTube=0;
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);//size
		gameOver = new Texture("gameOver.png");
	}
	void updateInfo(int i){

			gap[i] = random.nextFloat()*100+500;
			tubeOffset[i] = (random.nextFloat()-0.5f)*(Gdx.graphics.getHeight()/2-80);


	}

	void init(){
		posY=Gdx.graphics.getHeight()/2-bird[0].getHeight()/2;
		for(int i=0;i<numberOfTubes;i++){
			tubeOffset[i] = (random.nextFloat()-0.5f)*(Gdx.graphics.getHeight()/2-80);
			tubeX[i]= Gdx.graphics.getWidth()+Gdx.graphics.getWidth()/2-tubes[0].getWidth()/2 +i*distancBetweenTubes;
			gap[i]=600;
		}
        for(int i=0;i<4;i++) {
            rectTopTubes[i] = new Rectangle();
            rectBottomTubes[i] =new Rectangle();
        }
		Gdx.app.log("state",String.valueOf(gameState));
	}

	@Override
	public void render () {

		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		if (gameState == 1) {
			Gdx.app.log("state","came back");
			if(tubeX[scoringTube]<Gdx.graphics.getWidth()/2){
				score++;
				scoringTube++;
				scoringTube=scoringTube%numberOfTubes;
				Gdx.app.log("Score",String.valueOf(score));
			}

			if (Gdx.input.justTouched()) {//every time screen is tapped
				speedY=-25;
				Gdx.app.log("state","Here 3");
			}

			for(int i=0;i<numberOfTubes;i++) {

				if(tubeX[i]<-tubes[0].getWidth()){
					tubeX[i]+=numberOfTubes*distancBetweenTubes;
					updateInfo(i);
				}else {
					tubeX[i] -= tubeVelocity;

				}
				batch.draw(tubes[0],tubeX[i],Gdx.graphics.getHeight()/2+ gap[i]/2 +tubeOffset[i]);
				batch.draw(tubes[1],tubeX[i],Gdx.graphics.getHeight()/2 - gap[i]/2 - tubes[1].getHeight()+tubeOffset[i]);
				rectTopTubes[i] = new Rectangle(tubeX[i],Gdx.graphics.getHeight()/2+ gap[i]/2 +tubeOffset[i],tubes[0].getWidth(),tubes[0].getHeight());
				rectBottomTubes[i] =  new Rectangle(tubeX[i],Gdx.graphics.getHeight()/2 - gap[i]/2 - tubes[1].getHeight()+tubeOffset[i],tubes[0].getWidth(),tubes[0].getHeight());
			}





			if(posY>0){
				speedY = speedY +gravity;
				posY -= speedY;
			}
			else{
				gameState=2;
			}

			dt = Gdx.graphics.getDeltaTime();
			//the time from the last passed f
			accumulator += dt;


			if (accumulator > 0.1) {
				if (flapState == 0)
					flapState = 1;
				else
					flapState = 0;
				accumulator = 0;
			}



		}
		else if(gameState==0) {
			if (Gdx.input.justTouched()) {//every time screen is tapped
				Gdx.app.log("state","Here 1");
				gameState=1;

			}
		}
		else if(gameState==2){
			batch.draw(gameOver,Gdx.graphics.getWidth()/2-gameOver.getWidth()/2,Gdx.graphics.getHeight()/2-gameOver.getHeight()/2);
			if(Gdx.input.justTouched()){
				Gdx.app.log("state","Here 2");
				gameState=1;
				score=0;
				scoringTube=0;
				speedY=0;
				init();
			}
		}

		//bottom left is start, the next ones give the height

		batch.draw(bird[flapState], Gdx.graphics.getWidth() / 2 - bird[flapState].getWidth() / 2, posY);

		font.draw(batch, String.valueOf(score),100,200);
		//for rendering text

		batch.end();

		birdCircle.set(Gdx.graphics.getWidth()/2,posY+bird[flapState].getHeight()/2,bird[flapState].getHeight()/2);
		//x,y,radius




		//shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		//shapeRenderer.setColor(Color.RED);


		//shapeRenderer.circle(birdCircle.x,birdCircle.y,birdCircle.radius);
		for(int i=0;i<numberOfTubes;i++){
			//shapeRenderer.rect(rectTopTubes[i].x,rectTopTubes[i].y,rectTopTubes[i].width,rectTopTubes[i].height);
			//shapeRenderer.rect(rectBottomTubes[i].x,rectBottomTubes[i].y,rectBottomTubes[i].width,rectBottomTubes[i].height);
			//for checking the linking
			if(Intersector.overlaps(birdCircle,rectTopTubes[i])){
				//Gdx.app.log("Dead","dead");
				Gdx.app.log("state","Still here");
				gameState=2;
			}
			else if(Intersector.overlaps(birdCircle,rectBottomTubes[i])){
				//Gdx.app.log("Dead","dead");
				Gdx.app.log("state","still here 2");
				gameState=2;
			}
		}

		//shapeRenderer.end();
	}
	@Override
	public void dispose () {
		batch.dispose();
		background.dispose();
	}
}

