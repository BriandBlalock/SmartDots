Dot test;

void setup(){ 
  size(800,800); 
  test = new Dot(); 
  
} 

void draw(){ 
  background(255); 
  test.move(); 
  test.show(); 
  
}
