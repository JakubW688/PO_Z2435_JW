public class Main {
    public static void main(String[] args) {
        Point p1 = new Point(9, 6);
        Point p2 = new Point(5, 4);


        System.out.println("Odległość między punktami: " + p1.distance(p2));
        System.out.println("Odległość wzdłuż osi X: " + p1.distanceX(p2));
        System.out.println("Odległość wzdłuż osi Y: " + p1.distanceY(p2));


        Circle circle = new Circle();
        System.out.println("Pole koła: " + circle.area());
        System.out.println("Obwód koła: " + circle.circumference());
    }
}
