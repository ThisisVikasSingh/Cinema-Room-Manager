package cinema;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class Cinema {

    public static void main(String[] args) {
        int numRows = getNumberOfRows();
        int numRowSeats = getNumberOfSeatsInEachRow();

        ArrayList<ArrayList<Character>> seatingArrangement = getInitialSeatingArrangement(numRows, numRowSeats);

        System.out.println();

        printMenu(numRows, numRowSeats, seatingArrangement);

    }

    private static ArrayList<ArrayList<Character>> getInitialSeatingArrangement(int numRows, int numRowSeats) {

        ArrayList<ArrayList<Character>> seatingArrangement = new ArrayList<>();

        for (int i = 0; i < numRows + 1; i++) {
            ArrayList<Character> row = new ArrayList<>();
            for (int j = 0; j < numRowSeats + 1; j++) {
                if (i == 0 && j == 0) row.add(' ');
                else if (i == 0) {
                    row.add((char) (j + '0'));
                } else if (j == 0) {
                    row.add((char) (i + '0'));
                } else row.add('S');
            }
            seatingArrangement.add(row);
        }

        return seatingArrangement;
    }

    private static void printMenu(int numRows, int numRowSeats, ArrayList<ArrayList<Character>> seatingArrangement) {

        int choice;
        do {
            System.out.println("1. Show the seats");
            System.out.println("2. Buy a ticket");
            System.out.println("3. Statistics");
            System.out.println("0. Exit");

            Scanner scanner = new Scanner(System.in);

            choice = scanner.nextInt();

            System.out.println();

            switch (choice) {
                case 1:
                    printSeatingArrangement(seatingArrangement);
                    System.out.println();
                    break;
                case 2:
                    buyTicketProcess(numRows, numRowSeats, seatingArrangement);
                    break;

                case 3:
                    statistics(seatingArrangement);
                    System.out.println();

            }

        } while (choice != 0);

    }

    private static void buyTicketProcess(int numRows, int numRowSeats, ArrayList<ArrayList<Character>> seatingArrangement) {
        int rowNum = getRowNum(numRows);
        int seatNum = getSeatNum(numRowSeats);

        if (rowNum==-1 || seatNum==-1){
            System.out.println("Wrong input!");
            buyTicketProcess(numRows, numRowSeats, seatingArrangement);
            return;
        }
        boolean isSeatAlreadyTaken = isSeatAlreadyTaken(rowNum, seatNum, seatingArrangement);

        if (isSeatAlreadyTaken) {
            System.out.println("That ticket has already been purchased!");
            buyTicketProcess(numRows,numRowSeats,seatingArrangement);
            return;
        } else {
            updateSeatingArrangementMatrix(seatingArrangement, rowNum, seatNum);

            boolean numSeatsMoreThan60 = checkNumberOfSeatsMoreThan60(numRows, numRowSeats);

            int ticketPrice = calcTicketPrice(numSeatsMoreThan60, rowNum, numRows);
            printTicketPrice(ticketPrice);
        }
        System.out.println();
    }

    private static int getSeatNum(int numRowSeats) {
        int seatNum;

        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter a seat number in that row:");
        seatNum = scanner.nextInt();

        return (seatNum<=numRowSeats && seatNum>=1)?seatNum:-1;
    }

    private static int getRowNum(int numRows) {
        int rowNum;

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a row number:");
        rowNum = scanner.nextInt();

        return (rowNum<=numRows && rowNum>=1)?rowNum:-1;
    }

    private static void statistics(ArrayList<ArrayList<Character>> seatingArrangement) {
        int numPurchasedTickets = 0;
        float percentageOfPurchasedTickets;
        int currentIncome = 0;
        int totalIncome = 0;

        for (int i = 1; i < seatingArrangement.size(); i++) {
            for (int j = 1; j < seatingArrangement.get(0).size(); j++) {
                int numRows = seatingArrangement.size()-1;
                int numSeatsInEachRow = seatingArrangement.get(0).size()-1;
                boolean isNumSeatsMoreThan60 = checkNumberOfSeatsMoreThan60(numRows, numSeatsInEachRow);

                if (seatingArrangement.get(i).get(j).equals('B')) {
                    numPurchasedTickets++;

                    int currentTicketPrice = calcTicketPrice(isNumSeatsMoreThan60, i, numRows);

                    currentIncome += currentTicketPrice;
                }

                int currentTicketPrice = calcTicketPrice(isNumSeatsMoreThan60, i, numRows);
                totalIncome += currentTicketPrice;
            }

        }

        int totalNumberOfSeats = seatingArrangement.size() * seatingArrangement.get(0).size() - (seatingArrangement.size()+ seatingArrangement.get(0).size()) + 1 ;
        percentageOfPurchasedTickets = ((float) numPurchasedTickets /(float) totalNumberOfSeats);
        DecimalFormat df = new DecimalFormat("0.00%");
        String formattedPercentage = df.format(percentageOfPurchasedTickets);

        System.out.println("Number of purchased tickets: " + numPurchasedTickets);
        System.out.println("Percentage: " + formattedPercentage);
        System.out.println("Current Income: $" + currentIncome);
        System.out.println("Total Income: $" + totalIncome);
    }

    private static boolean isSeatAlreadyTaken(int rowNum, int seatNum, ArrayList<ArrayList<Character>> seatingArrangement) {
        return seatingArrangement.get(rowNum).get(seatNum).equals('B');
    }

    private static void printSeatingArrangement(ArrayList<ArrayList<Character>> seatingArrangement) {
        System.out.println("Cinema:");

        for (ArrayList<Character> row : seatingArrangement) {
            for (char c : row) {
                System.out.print(c + " ");
            }
            System.out.println();
        }
    }

    private static void updateSeatingArrangementMatrix(ArrayList<ArrayList<Character>> seatingArrangement, int rowNum, int seatNum) {

        for (int i = 0; i < seatingArrangement.size(); i++) {
            for (int j = 0; j < seatingArrangement.get(0).size(); j++) {
                if (i == rowNum && j == seatNum) {
                    seatingArrangement.get(i).set(j, 'B');
                }

            }

        }

    }

    private static int getNumberOfSeatsInEachRow() {
        int numSeatsInEachRow;

        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the number of seats in each row:");
        numSeatsInEachRow = scanner.nextInt();

        return numSeatsInEachRow;
    }

    private static int getNumberOfRows() {
        int numRows;

        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the number of rows:");
        numRows = scanner.nextInt();

        return numRows;
    }

    private static void printTicketPrice(int ticketPrice) {
        System.out.println("Ticket price: $" + ticketPrice);
    }

    private static int calcTicketPrice(boolean numSeatsMoreThan60, Integer rowNum, int numRows) {
        if (!numSeatsMoreThan60) {
            return 10;
        }

        int upperBoundFrontHalfRows = numRows / 2;

        if (rowNum <= upperBoundFrontHalfRows) {
            return 10;
        } else return 8;

    }

    private static boolean checkNumberOfSeatsMoreThan60(int numRows, int numRowSeats) {
        return numRows * numRowSeats > 60;
    }
}