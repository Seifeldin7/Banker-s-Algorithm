package banker;

import java.util.*;

class Resource {

    private String name;
    private int value;

    public Resource(String name, int value) {
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
    private Resource[] available;
    private Random rand = new Random();
    boolean[] flag;
    private int no_of_requests = rand.nextInt(61);
   // private int[][] request;
    private int[] requests;
    private boolean max_request = true;

    public Banker(int R, int P) {
        this.R = R;
        this.P = P;
    }

    private void input() {
        alloc = new int[P][R];
        max = new int[P][R];
        available = new Resource[R];
        requests =new int[R];
       // request = new int[this.no_of_requests][R];

        Scanner sc = new Scanner(System.in);
        int temp = R;
        System.out.println("Enter Name and Value of each Resource:");
        int k = 0;
        while (temp > 0) {
            String name = sc.next();
            int value = sc.nextInt();
            Resource r = new Resource(name, value);
            available[k] = r;
            temp--;
            k++;
        }
        for (int i = 0; i < P; i++) {
            for (int j = 0; j < R; j++) {
                max[i][j] = rand.nextInt(available[j].getValue() - 2 * alloc[i][j] + 1);
            }
        }

        /* for (int i = 0; i < this.no_of_requests; i++) {
            for (int j = 0; j < R; j++) {
                request[i][j] = rand.nextInt(10);
            }
        }*/
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

    private void request(int current_request, int p_no) {
        //int[] requests = new int[R];

        for (int j = 0; j < R; j++) {
            requests[j] = rand.nextInt(Need[p_no][j] + 2);
            if(requests[j] >available[j].getValue())
                j=0;
        }
         for (int k = 0; k < requests.length; k++) {
            System.out.print(requests[k] + " ");
        }
        System.out.println();
        this.max_request = true;
        boolean Safe = isSafe(current_request, p_no);
        if (this.max_request == false) {
            System.out.println("Request Denied! Recources requested are greater than the Need.");
        } else if (Safe) {
            for (int j = 0; j < R; j++) {
                alloc[p_no][j] += requests[j];
            }
            for (int i = 0; i < R; i++) {
                available[i].setValue(available[i].getValue() - requests[i]);
            }
            System.out.println("Request Accepted!");
        } else {
            System.out.println("Request Denied! System is unsafe");
        }
       
    }

    private void release(int p) {
        for (int i = 0; i < R; i++) {
            if (alloc[p][i] != 0) {
                break;
            }
            if (i == R - 1) {
                return;
            }
        }
        for (int i = 0; i < R; i++) {
            available[i].setValue(available[i].getValue() + alloc[p][i]);
        }
        for (int j = 0; j < R; j++) {
            alloc[p][j] = 0;
        }
        System.out.println("Process " + p + " has finished!");
    }

    private boolean isSafe(int current_request, int p_no) {
        boolean Safe = true;
        int[][] tempAlloc = new int[P][R];
        int[] tempAvailable = new int[R];
        flag = new boolean[P];
        for (int i = 0; i < flag.length; i++) {
            flag[i] = false;
        }
        for (int i = 0; i < P; i++) {
            for (int j = 0; j < R; j++) {
                tempAlloc[i][j] = alloc[i][j];
            }
        }

        for (int i = 0; i < R; i++) {
            tempAvailable[i] = available[i].getValue();
        }
        List<Integer> used_processes = new ArrayList<>();
        for (int i = 0; i < P; i++) {
            for (int j = 0; j < R; j++) {
                tempAlloc[i][j] += requests[j];
                tempAvailable[j] = tempAvailable[j] - requests[j];

            }
        }
        for (int i = 0; i < R; i++) {
            if (requests[i] > Need[p_no][i]) {
                this.max_request = false;
                return true;
            }
        }
        int tempNeed[][] = this.calcNeed(tempAlloc);
        
        for (int i = 0; i < P; i++) {
            if (used_processes.contains(i)) {
                continue;
            }
            boolean accepted = true;
            for (int j = 0; j < R; j++) {
                if (tempNeed[i][j] > available[j].getValue()) {
                    accepted = false;
                    break;
                }
            }
            if (accepted) {
                for (int j = 0; j < R; j++) {
                    tempAvailable[j] = tempAvailable[j] + tempAlloc[i][j];
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
        System.out.println("Enter the number of resources types and number of processes"
                + " respectively");
        R = sc.nextInt();
        P = sc.nextInt();
        Banker banker = new Banker(R, P);
        banker.input();
        int counter;
        for (int i = 0; i < banker.no_of_requests; i++) {
            int p = banker.rand.nextInt(P);
            banker.Need = banker.calcNeed(banker.alloc);
            System.out.print("Process " + p + " requested:");
            counter = banker.rand.nextInt(11);
            if (counter != p && (counter >= 0 && counter < P)) {
                banker.release(counter);
            }
            /*for (int k = 0; k < banker.request[i].length; k++) {
                System.out.print(banker.request[i][k] + " ");
            }
            System.out.println();*/
            banker.request(i, p);
            banker.Need = banker.calcNeed(banker.alloc);
            System.out.println("Maximum\t\t\tAllocation\t\t\tNeed\t\t\t\t\tAvailable");
            for (int k = 0; k < banker.P; k++) {
                for (int j = 0; j < banker.R; j++) {
                    System.out.print(banker.max[k][j] + " ");
                }

                System.out.print("\t\t");

                for (int j = 0; j < banker.R; j++) {
                    System.out.print(banker.alloc[k][j] + " ");
                }

                System.out.print("\t\t\t\t");

                for (int j = 0; j < banker.R; j++) {
                    System.out.print(banker.Need[k][j] + " ");
                }
                if (k == 0) {
                    System.out.print("\t\t\t\t");
                    for (int j = 0; j < banker.R; j++) {
                        System.out.print(banker.available[j].getValue() + " ");
                    }
                }
                System.out.println();
            }

        }

    }

}
