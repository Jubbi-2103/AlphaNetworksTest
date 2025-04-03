package fizzbuzz;

public class FizzBuzz {

    public String pickOne(int input) {
        if (input % 3 == 0 && input % 5 == 0) {
            return "fizzbuzz";
        }
        else if (input % 3 == 0) {
            return "fizz";
        }
        else if (input % 5 == 0) {
            return "buzz";
        }
        return String.valueOf(input);
    }

}
