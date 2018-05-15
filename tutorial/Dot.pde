class Dot { 

  PVector pos; 
  PVector vel; 
  PVector acc; 
  Brain brain; 

  boolean dead = false;

  Dot() { 

    brain = new Brain(400);
    pos = new PVector( width/2, height /2);
    vel = new PVector ( 0, 0 ) ;
    acc = new PVector(0, 0);
  } 



  //---------------------------
  void show() { 
    fill(0); 
    ellipse(pos.x, pos.y, 4, 4) ;
  }



  //--------------------
  void move() { 
    if ( brain.directions.length > brain.step) { 
      acc = brain.directions[brain.step] ;
      brain.step++;
    } else { 
      dead = true;
    }

    vel.add(acc); 
    pos.add(vel);
  } 
  //----------------------------------------

  void update() { 

    if ( !dead) {
      move(); 
      if ( pos.x< 2|| pos.y<2|| pos.x>width-2 || pos.y> height -2) { 

        dead= true;
      }
    }
  }
}
