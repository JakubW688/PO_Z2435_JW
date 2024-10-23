public class Square extends Shape {
    private double side;
    public Square(Double side){
        this.side = side;
    }
    @Override
    public double area() {
        return side * side;
    }
    @Override
    public double circumference() {
        return 4 * side;
    }
}
