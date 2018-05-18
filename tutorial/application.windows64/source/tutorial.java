import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class tutorial extends PApplet {

Population test;

PVector goal = new PVector(400, 10);

public void setup() { 
   
  test = new Population(1000);
} 

public void draw() { 
  background(255); 
  fill(255, 0, 0);
  ellipse(goal.x, goal.y, 10, 10);
  
  fill(0,0,255);
  rect(100,300,600,20);

  if ( test.allDotsDead()) {
    //genetic algorithm
    test.calculateFitness(); 
    test.naturalSelection(); 
    test.mutate();
    
    
  } else {

    test.update(); 
    test.show();
  }
}
class Brain { 

  PVector[] directions; 
  int step = 0; 


  Brain( int size) { 
    directions = new PVector[size]; 
    randomize();
  }


  //---------------------


  public void randomize() { 

    for ( int i = 0; i < directions.length; i++) { 

      float randomAngle = random(2*PI); 
      directions[i] = PVector.fromAngle(randomAngle);
    }
  }

  public Brain clone() { 

    Brain clone = new Brain(directions.length); 

    for ( int i = 0; i < directions.length; i++) { 
      clone.directions[i] = directions[i];
    }
    
    return clone;
  }
  
  public void mutate(){ 
    
     float mutationRate = 0.01f;
     for(int i = 0; i < directions.length; i++){ 
       float rand = random(1); 
       
       if( rand< mutationRate){ 
         float randomAngle = random(2*PI);
         directions[i] = PVector.fromAngle(randomAngle);
         
       }
     }
    
  }
}
class Dot { 

  PVector pos; 
  PVector vel; 
  PVector acc; 
  Brain brain; 

  boolean dead = false;
  boolean reachedGoal = false;
  boolean isBest = false; 
  
  float fitness = 0;

  Dot() { 

    brain = new Brain(400);
    pos = new PVector( width/2, height -10);
    vel = new PVector ( 0, 0 ) ;
    acc = new PVector(0, 0);
  } 



  //---------------------------
  public void show() { 
    
    if( isBest){ 
      fill( 0,255,0);
       ellipse(pos.x, pos.y, 8, 8) ;
    
    }else{
    fill(0); 
    ellipse(pos.x, pos.y, 4, 4) ;
    }
  }



  //--------------------
  public void move() { 
    if ( brain.directions.length > brain.step) { 
      acc = brain.directions[brain.step] ;
      brain.step++;
    } else { 
      dead = true;
    }

    vel.add(acc); 
    vel.limit(5);
    pos.add(vel);
  } 
  //----------------------------------------

  public void update() { 

    if ( !dead && !reachedGoal) {
      move(); 
      if ( pos.x< 2|| pos.y<2|| pos.x>width-2 || pos.y> height -2) { 

        dead= true;
      }else if( dist( pos.x, pos.y, goal.x,goal.y) < 5){ 
        
        reachedGoal = true;
        
      }else if(pos.x<700 && pos.y <320 && pos.x >100 && pos.y>300){ 
        dead=true;
          
      }
      
    }
  }

  public void calculateFitness() { 
    if( reachedGoal){
      fitness = 1.0f/16.0f + 10000.0f/(float)(brain.step*brain.step);
    }else{
      
      float distanceToGoal = dist(pos.x, pos.y, goal.x, goal.y); 
      fitness = 1.0f/(distanceToGoal * distanceToGoal);
    }
  }
  public Dot getBaby(){ 
    Dot baby = new Dot();
    baby.brain = brain.clone();
    return baby;
  }
}
class Population { 

  Dot[] dots; 

  float fitnessSum ;
  int gen = 1;
  int bestDot = 0 ;
  int minStep = 400;

  Population ( int size) { 

    dots = new Dot[size]; 
    for ( int i = 0; i < size; i ++) { 

      dots[i] = new Dot();
    }
  }


  public void show() { 
    for ( int i = 1; i < dots.length; i ++) { 
      dots[i].show();
    }
    dots[0].show();
  }

  public void update() {
    for ( int i = 0; i < dots.length; i ++) { 
      if ( dots[i].brain.step > minStep) { 
        dots[i].dead = true;
      } else {

        dots[i].update();
      }
    }
  }

  public void calculateFitness() { 
    for ( int i = 0; i < dots.length; i ++) { 
      dots[i].calculateFitness();
    }
  }

  public boolean allDotsDead() { 
    for ( int i = 0; i < dots.length; i ++) { 
      if (!dots[i].dead && !dots[i].reachedGoal) {
        return false;
      }
    }
    return true;
  }

  //--------------------------------------------------------------------------------
  public void calculateFitnessSum() { 
    fitnessSum = 0 ; 

    for (int i = 0; i < dots.length; i++) { 
      fitnessSum+=dots[i].fitness;
    }
  }


  public void naturalSelection() { 
    Dot[] newDots = new Dot[dots.length];
    setBestDot();
    calculateFitnessSum(); 

    newDots[0] = dots[bestDot].getBaby();
    newDots[0].isBest = true; 

    for ( int i = 1; i < newDots.length; i ++) { 
      //select parent based on fitness
      Dot parent= selectParent(); 
      //get baby
      newDots[i] = parent.getBaby();
    }
    dots = newDots.clone();
    gen++;
  }



  public Dot selectParent() {

    float rand = random(fitnessSum);

    float runningSum = 0; 

    for ( int i = 0; i < dots.length; i++) { 

      runningSum+= dots[i].fitness; 
      if ( runningSum > rand) { 
        return dots[i];
      }
    }

    return null; //shouldnt be called
  }


  public void mutate() { 
    for ( int i = 1; i < dots.length; i++) { 
      dots[i].brain.mutate();
    }
  }

  public void setBestDot() { 

    float max = 0; 
    int maxIndex = 0;

    for ( int i = 0; i < dots.length; i ++) { 
      if (dots[i].fitness > max) { 
        max = dots[i].fitness; 
        maxIndex = i;
      }
    }

    bestDot = maxIndex;

    if ( dots[bestDot].reachedGoal) { 
      minStep = dots[bestDot].brain.step;
      println("step: ", minStep);
    }
  }
}

  public void settings() {  size(800, 800); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "tutorial" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
