class Point {
    int x;
    double y;

    public Point(int x, double y) {
        this.x = x;
        this.y = y;
    try {
        double z = this.x / 0;

    }
        catch(Exception e){
            System.out.println("Coś jest nie tak");
        }
    }


    public double distance(Point other) {
        return Math.sqrt(Math.pow((other.x - this.x), 2) + Math.pow((other.y - this.y), 2));
    }


    public double distanceX(Point other) {
        return Math.abs(other.x - this.x);
    }


    public double distanceY(Point other) {
        return Math.abs(other.y - this.y);
    }
}

class Circle extends Shape {
    private Point Point;
    Point center;
    double radius;

    public double Circle(double radius) {
        this.center = center;
        this.radius = radius;
        try {
            if (radius * 2 < 1) {
                throw new BadShapeTwoException("Średnica musi być większa lub równa 1.");
            }
        } catch (BadShapeTwoException e) {
            System.err.println("Błąd: " + e.getMessage());
        }
        finally {
            System.out.println("Praca zakończona.");
        }
        double area;
        {
            return Math.PI * radius * radius;
        }
    }

    @Override
    public double area() {
        return 0;
    }
}