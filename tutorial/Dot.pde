class Dot{ 
  
  PVector pos; 
  PVector vel; 
  PVector acc; 
  Brain brain; 
  
  Dot(){ 
    
    brain = new Brain(400)
    pos = new PVector( width/2, height /2) ;
    vel = new PVector ( 0 ,0 ) ;
    acc = new PVector(0,0); 
    
    
  } 
  
  
  
  //---------------------------
  void show(0{ 
    fill(0); 
    ellipse(pos.x, pos.y,4,4) ; 
    
    
    
  }
  
  
  
  //--------------------
  void move(){ 
    if( brain.directions.length > barin.step){ 
      acc = brain.directions[brain.step] 
      brain.step++; 
      
    }
    
    vel.add(acc); 
    pos.add(vel);
    
  } 
    
  
  
  
  
    
