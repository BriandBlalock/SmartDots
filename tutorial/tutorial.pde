Population test;

void setup() { 
  size(800, 800); 
  test = new Population(100);
} 

void draw() { 
  background(255); 
  test.update(); 
  test.show();
}
