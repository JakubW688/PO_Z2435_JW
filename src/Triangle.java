public class Triangle extends Shape{
    private double base;
    private double height;
    private double sideA;
    private double sideB;
    private double sideC;

    public Triangle(double base, double height, double sideA, double sideB){
        this.base = base;
        this.height = height;
        this.sideA = sideA;
        this.sideB = sideB;
        this.sideC = sideC;
    }
    @Override
    public double area() {
        return (base * height) / 2;
    }
    @Override
    public double circumference() {
        return sideA + sideB + sideC;
    }
}
