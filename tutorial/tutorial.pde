Population test;

PVector goal = new PVector(400,10);

void setup() { 
  size(800, 800); 
  test = new Population(1000);
} 

void draw() { 
  background(255); 
  fill(255,0,0);
  ellipse(goal.x, goal.y,10,10);
  test.update(); 
  test.show();
}
