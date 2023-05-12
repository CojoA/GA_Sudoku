import com.qqwing.Difficulty;

import java.text.CollationElementIterator;
import java.util.*;
import java.util.stream.Stream;


public class QQWing_Sudoku {

    List<Integer> parent1 = new ArrayList<>();
    List<Integer> parent2 = new ArrayList<>();
    public int[] vecFrec = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    public List<Integer> chromosome = new ArrayList<>();
    public List<Integer> pointCross = new ArrayList<>();



    //    public List<Integer> pupulation(int[] Sudoku){
//        for(int i = 0; i < 81;i+=9){
//            int[] vecFrec = {0,0,0,0,0,0,0,0,0,0};
//            for(int j = i; j < i+9;j++){ // iterate on the line
//                if(Sudoku[i] != 0)
//                    vecFrec[j]++;
//            }
//            for (int j = 1; j <= 9; j++) {
//                if(vecFrec[j] == 0)
//                    chromosome.add(j);
//            }
////            pointCross.add(chromosome.size());
//
//        }
//        return chromosome;
//    }
    public int[] generateSudoku(int dif) {
        int[] puzzle = new int[0];
        switch (dif) {
            case 1:
                //Extremely easy puzzle, should be solvable without tuning the parameters of the genetic algorithm
                puzzle = ExampleQQWing.computePuzzleWithNHolesPerRow(9);
                System.out.println(Arrays.toString(puzzle));
                for (int i = 0; i < puzzle.length; i++) {
                    System.out.print(puzzle[i]+" ");
                    if ((i + 1) % 3 == 0 && (i+1) %9!=0 ) {
                        System.out.printf("| ");
                    }
                    if (i == 26 || i == 53){
                        System.out.println();
                        for (int j = 0; j < 11; j++) {
                            System.out.printf("- ");
                        }
                    }
                    if ((i+1) % 9 == 0)
                        System.out.println();
                }
                break;
            case 2:
                //Puzzle with difficulty SIMPLE as assessed by QQWing.
                //Should require just minimal tuning of the parameters of the genetic algorithm
                puzzle = ExampleQQWing.computePuzzleByDifficulty(Difficulty.SIMPLE);
//                for(int i = 0; i < 9; i++){
//                    for (int j = 0; j < 9; j++) {
//                        System.out.print(puzzle[i]);
//                    }
//                    System.out.println();
//                }
                break;
            case 3:
                //Puzzle with difficulty EASY as assessed by QQWing.
                //Should require some tuning of the parameters of the genetic algorithm
                puzzle = ExampleQQWing.computePuzzleByDifficulty(Difficulty.EASY);
                break;
            case 4:
                //Puzzle with difficulty INTERMEDIATE as assessed by QQWing.
                //Should require serious effort tuning the parameters of the genetic algorithm
                puzzle = ExampleQQWing.computePuzzleByDifficulty(Difficulty.INTERMEDIATE);
                break;
            case 5:
                //Puzzle with difficulty EXPERT as assessed by QQWing.
                //Should require great effort tuning the parameters of the genetic algorithm
                puzzle = ExampleQQWing.computePuzzleByDifficulty(Difficulty.EXPERT);
                break;
        }
        return puzzle;
    }

    public List<Integer> population(int[] Sudoku) {
        for (int i = 0; i < 81; i += 9) {
            int[] vecFrec = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

            for (int j = i; j < i + 9; j++) { // iterate on the line
                if (Sudoku[j] != 0)
                    vecFrec[Sudoku[j]]++;

            }
            for (int j = 1; j <= 9; j++) {
                if (vecFrec[j] == 0)
                    chromosome.add(j);
            }
            pointCross.add(chromosome.size());

        }
        chromosome.addAll(pointCross);
        return chromosome;
    }

    public List<List<Integer>> generatePopulation(List<Integer> first, List<Integer> posCross) {
        List<List<Integer>> populations = new ArrayList<>();

        int f = 0;
        int index = 0;
        int l = posCross.get(index);


        for (int i = 0; i < 1000000; i++) {
            List<Integer> newPop = new ArrayList<>();
            List<Integer> newPopFinal = new ArrayList<>();
            index = 0;
            f = 0;
            l = posCross.get(index);
            for (int k = 0; k < posCross.size(); k++) {

//                System.out.printf("|"+f+ " "+l+"|   ");
                for (int j = f; j < l; j++) {

                    newPop.add(first.get(j));
                }

                Collections.shuffle(newPop);
                newPopFinal.addAll(newPop);
                newPop = new ArrayList<>();
                f = l;
                index++;
                if (index < 9)
                    l = posCross.get(index);

            }
            populations.add(newPopFinal);
        }
        return populations;
    }

    public List<Integer> reconstructFunction(List<Integer> chromo, int[] sudokuGame) {
        List<Integer> sudokuFull = new ArrayList<>();

        int index = 0;
        for (int i = 0; i < sudokuGame.length; i++) {
            if (sudokuGame[i] == 0) {
                sudokuFull.add(chromo.get(index));
                index++;
            } else {
                sudokuFull.add(sudokuGame[i]);
            }
        }

//            for (Integer i : sudokuFull){
//                System.out.printf(i+" ");
//            }

        return sudokuFull;
    }

    public int fitnessFunction(List<Integer> sudokuFull) {
        int sumFinal = 0;
        int[] frecVect = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                frecVect[sudokuFull.get(j * 9 + i)]++;
            }
            for (int j = 1; j <= 9; j++) {
                if (frecVect[j] == 1) {
                    sumFinal++;
                }
            }
            frecVect = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        }
        int i1 = 0;
        int i2 = 3;////verif
        int j1 = 0;
        int j2 = 3;
        frecVect = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
//        System.out.println("sudokuFullPractic");
        for (int z = 0; z < 9; z++) {
            for (int i = i1; i < i2; i++) {
                for (int j = j1; j < j2; j++) {
                    frecVect[sudokuFull.get(j * 9 + i)]++;
//                    System.out.printf(j*9+i+" ");
                }
            }
            for (int j = 1; j <= 9; j++) {
                if (frecVect[j] == 1) {
                    sumFinal++;
                }
            }
            frecVect = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

            if (i2 + 3 <= 9) {
                i1 += 3;
                i2 += 3;
            }
            if ((z + 1) % 3 == 0) {

                j1 += 3;
                j2 += 3;
                i1 = 0;
                i2 = 3;
            }
        }
//        System.out.println("suma = " + sumFinal);
        return sumFinal;
    }


    public List<List<Integer>> crossover(List<List<Integer>> population, List<Integer> posCross) {//out children
        Collections.shuffle(population);
        List<List<Integer>> newPopulation = new ArrayList<>();
        List<Integer> people1 = new ArrayList<>();
        List<Integer> people2 = new ArrayList<>();

        int randomIndex;
        int div;
        for (int i = 0; i < population.size(); i += 2) {
            randomIndex = (int) (Math.random() * (posCross.size() - 1));
            div = posCross.get(randomIndex);
            for (int j = 0; j < div; j++) {
                people1.add(population.get(i).get(j));
                people2.add(population.get(i + 1).get(j));
            }
            for (int j = div; j < population.get(i).size(); j++) {
                people1.add(population.get(i + 1).get(j));
                people2.add(population.get(i).get(j));
            }
            newPopulation.add(people1);
            newPopulation.add(people2);


        }
        return newPopulation;
    }

    public List<Integer> mutation(List<Integer> child, List<Integer> posCross) {
        int randIndex = (int) (Math.random() * (posCross.size() ));
//        System.out.printf("child1: "+child+" ");
        int randFirst =0;
        int randSecond = 0;
        if (randIndex == 0) {

            int infLim = 0;//[0,]
            int supLim = posCross.get(randIndex);

            randFirst = (int) (Math.random() * (supLim - infLim ) + infLim);///mod1
            randSecond = (int) (Math.random() * (supLim - infLim) + infLim);
            while(randFirst == randSecond){
                randSecond = (int) (Math.random() * (supLim - infLim) + infLim);
            }
            int aux = child.get(randFirst);
            child.set(randFirst, child.get(randSecond));
            child.set(randSecond, aux);
//            System.out.println("->"+randFirst+" "+ randSecond+ " cu 0");


        } else {
            int supLim = posCross.get(randIndex);
            int infLim = posCross.get(randIndex - 1);
            randFirst = (int) (Math.random() * (supLim - infLim ) + infLim);///mod1
            randSecond = (int) (Math.random() * (supLim - infLim) + infLim);
            while(randFirst == randSecond){
                randSecond = (int) (Math.random() * (supLim - infLim) + infLim);
            }

            int aux = child.get(randFirst);
            child.set(randFirst, child.get(randSecond));
            child.set(randSecond, aux);

//            System.out.println("->"+randFirst+" "+ randSecond);
        }
//        System.out.printf("child2: "+child);
//        System.out.println("->"+randFirst+" "+ randSecond);
        return child;
    }

    public List<Integer> weightedBy(List<List<Integer>> population, int[] sudoku) {
        QQWing_Sudoku people = new QQWing_Sudoku();
        List<Integer> weights = new ArrayList<>();
        for (List<Integer> people1 : population) {
            weights.add(people.fitnessFunction(people.reconstructFunction(people1, sudoku)));
        }
        return weights;
    }

    public List<Integer> weithed_random_choices(List<List<Integer>> population, List<Integer> weights) {
        int sum = 0;

        for (int i : weights) {
            sum += i;
        }
        int averageWeight = sum / weights.size();
        int counter = 0;
        Collections.shuffle(population);

        for (int i = 0; i < population.size(); i++) {
            if (weights.get(i) > averageWeight) {
                return population.get(i);
            }

        }

        return population.get(0);
    }

    public List<Integer> reproduce (List<Integer> parent1,List<Integer> parent2,List<Integer>posCross){
        List<Integer> child = new ArrayList<>();

        int randomIndex = (int) (Math.random() * (posCross.size() - 1));
        int div = posCross.get(randomIndex);

        for (int j = 0; j < div; j++) {
            child.add(parent1.get(j));
        }
        for (int j = div; j < parent1.size(); j++) {
            child.add(parent2.get(j));
        }
        return child;
    }

}
