package banker;

import java.util.*;

class resource {

    private String name;
    private int value;

    public resource(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return this.name;
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}

class x {

    int x;
}

public class Banker {

    private int R, P;
    private int[][] alloc = new int[P][R];
    private int[][] max = new int[P][R];
    private int[][] Need = new int[P][R];
    private resource[] available = new resource[R];
    private Random rand = new Random();
    boolean[] flag = new boolean[P];
    private int no_of_requests = rand.nextInt(31);
    private int[][] request = new int[this.no_of_requests][R];

    private void input() {
        Scanner sc = new Scanner(System.in);
        R = sc.nextInt();
        int temp = R;
        P = sc.nextInt();
        System.out.println("Enter Name and Value of each Resource:");
        int k = 0;
        while (temp > 0) {
            String name = sc.next();
            int value = sc.nextInt();
            resource r = new resource(name, value);
            available[k] = r;
            temp--;
            k++;
        }
        for (int i = 0; i < P; i++) {
            for (int j = 0; j < R; j++) {
                max[i][j] = rand.nextInt(41);
            }
        }

        for (int i = 0; i < this.no_of_requests; i++) {
            for (int j = 0; j < R; j++) {
                request[i][j] = rand.nextInt(31);
            }
        }
    }

    private void calcNeed() {
        for (int i = 0; i < P; i++) {
            for (int j = 0; j < R; j++) {
                Need[i][j] = max[i][j] - alloc[i][j];
            }
        }
    }

    private void request(int current_request) {
        if (isSafe()) {
            for (int i = 0; i < P; i++) {
                for (int j = 0; j < R; j++) {
                    alloc[i][j] += request[current_request][j];
                }
            }
        } else {
            System.out.println("Request Denied!");
        }
    }

    private boolean isSafe(int current_request) {
        int temp2 = R;
        boolean Safe = true;
        List<Integer> used_processes = new ArrayList<Integer>();
        for (int i = 0; i < P; i++) {

            for (int j = 0; j < R; j++) {
                alloc[i][j] += request[current_request][j];
            }
        }
        for (int i = 0; i < P; i++) {
            if (used_processes.contains(i)) {
                continue;
            }

            boolean accepted = true;
            int[] temp = new int[R];
            for (int j = 0; j < R; j++) {
                temp[j] = available[j].getValue() + alloc[i][j];
                if (temp[j] < Need[i][j]) {
                    accepted = false;
                    break;
                }
            }
            if (accepted) {
                for (int j = 0; j < R; j++) {
                    available[j].setValue(available[j].getValue() + alloc[i][j]);
                    alloc[i][j]=0;
                }
                this.flag[i] = true;
                used_processes.add(i);
                i = 0;
            }

        }
        for (int i = 0; i < this.flag.length; i++) {
            if (flag[i] == false) {
                Safe = false;
                break;
            }
        }
        return Safe;
    }

    public static void main(String[] args) {
        Banker banker = new Banker();
        banker.input();
        int no_of_requests;
        Scanner sc = new Scanner(System.in);
        no_of_requests = sc.nextInt();
        for (int i = 0; i < no_of_requests; i++) {

        }

    }

}
