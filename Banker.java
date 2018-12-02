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

public class Banker {

    public int R, P;
    private int[][] alloc;
    private int[][] max;
    private int[][] Need;
    private resource[] available;
    private Random rand = new Random();
    boolean[] flag;
    private int no_of_requests = rand.nextInt(31);
    private int[][] request;

    public Banker(int R, int P) {
        this.R = R;
        this.P = P;
    }

    private void input() {
        alloc = new int[P][R];
        max = new int[P][R];
        available = new resource[R];
        flag = new boolean[P];
        request = new int[this.no_of_requests][R];

        Scanner sc = new Scanner(System.in);
        int temp = R;
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

    private int[][] calcNeed(int[][] alloc) {
        Need = new int[P][R];
        for (int i = 0; i < P; i++) {
            for (int j = 0; j < R; j++) {
                Need[i][j] = max[i][j] - alloc[i][j];
            }
        }
        return Need;

    }

    private void request(int current_request) {
        if (isSafe(current_request)) {
            for (int i = 0; i < P; i++) {
                for (int j = 0; j < R; j++) {
                    alloc[i][j] += request[current_request][j];
                }
            }
            for (int i = 0; i < R; i++) {
                available[i].setValue(available[i].getValue() - request[current_request][i]);
            }
            System.out.println("Request Accepted!");
        } else {
            System.out.println("Request Denied!");
        }
    }

    private boolean isSafe(int current_request) {
        int temp2 = R;
        boolean Safe = true;
        int[][] tempAlloc = new int[P][R];
        resource[] tempAvailable = new resource[R];
        for (int i = 0; i < P; i++) {
            for (int j = 0; j < R; j++) {
                tempAlloc[i][j] = alloc[i][j];
            }
        }
        int tempNeed[][] = this.calcNeed(tempAlloc);
        for (int i = 0; i < R; i++) {
            if (request[current_request][i] > tempNeed[current_request % P][i]) {
                return false;
            }
        }
        for (int i = 0; i < R; i++) {
            tempAvailable[i] = available[i];
        }
        List<Integer> used_processes = new ArrayList<Integer>();
        for (int i = 0; i < P; i++) {
            for (int j = 0; j < R; j++) {
                tempAlloc[i][j] += request[current_request][j];
                tempAvailable[j].setValue(tempAvailable[j].getValue() - request[current_request][j]);

            }
        }
        tempNeed = this.calcNeed(tempAlloc);

        for (int i = 0; i < P; i++) {
            if (used_processes.contains(i)) {
                continue;
            }
            boolean accepted = true;
            int[] temp = new int[R];
            for (int j = 0; j < R; j++) {
                temp[j] = tempAvailable[j].getValue() + tempAlloc[i][j];
                if (temp[j] < max[i][j]) {
                    accepted = false;
                    break;
                }
            }
            if (accepted) {
                for (int j = 0; j < R; j++) {
                    tempAvailable[j].setValue(tempAvailable[j].getValue() + tempAlloc[i][j]);
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
        int R, P;
        Scanner sc = new Scanner(System.in);
        R = sc.nextInt();
        P = sc.nextInt();
        Banker banker = new Banker(R, P);
        banker.input();

        for (int i = 0; i < banker.no_of_requests; i++) {
            banker.Need = banker.calcNeed(banker.alloc);
            for (int k = 0; k < banker.P; k++) {
                for (int j = 0; j < banker.R; j++) {
                    System.out.print(banker.Need[k][j] + " ");
                }

                System.out.print("   ");

                for (int j = 0; j < banker.R; j++) {
                    System.out.print(banker.max[k][j] + " ");
                }

                System.out.print("   ");

                for (int j = 0; j < banker.R; j++) {
                    System.out.print(banker.alloc[k][j] + " ");
                }
                System.out.println();
            }

            banker.request(i);
        }

    }

}
