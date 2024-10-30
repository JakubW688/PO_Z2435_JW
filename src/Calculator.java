public class Calculator {
    public double calculateAreaSum(Shape shape1, Shape shape2) {
        return shape1.area() + shape2.area();
    }
    public static void main(String[] args){
        Circle circle = new Circle();
        Square square = new Square((double) 4.0);
        Triangle triangle = new Triangle(3, 4, 6, 7);
        Calculator calculator = new Calculator();
        System.out.println("suma powierzchni koło - kwadrat: " + calculator.calculateAreaSum(circle, square));
        System.out.println("suma powierzchni koło - trójkąt: "+ calculator.calculateAreaSum(circle, triangle));
    }
}