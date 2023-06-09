import com.qqwing.Difficulty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class geneticAlgorithm {

    List<Integer> parent1 = new ArrayList<>();
    List<Integer> parent2 = new ArrayList<>();
    public List<Integer> chromosome = new ArrayList<>();
    public List<Integer> pointCross = new ArrayList<>();

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

    public List<Integer> generateChromosome (int[] Sudoku) {
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
                for (int j = f; j < l; j++)
                    newPop.add(first.get(j));


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
        int i2 = 3;
        int j1 = 0;
        int j2 = 3;

        frecVect = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        for (int z = 0; z < 9; z++) {
            for (int i = i1; i < i2; i++) {
                for (int j = j1; j < j2; j++) {
                    frecVect[sudokuFull.get(j * 9 + i)]++;
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
        return sumFinal;
    }

    public List<Integer> mutation(List<Integer> child, List<Integer> posCross) {
        int randIndex = (int) (Math.random() * (posCross.size() ));

        int randFirst = 0;
        int randSecond = 0;
        if (randIndex == 0) {

            int infLim = 0;
            int supLim = posCross.get(randIndex);

            randFirst = (int) (Math.random() * (supLim - infLim ) + infLim);
            randSecond = (int) (Math.random() * (supLim - infLim) + infLim);

            while(randFirst == randSecond)
                randSecond = (int) (Math.random() * (supLim - infLim) + infLim);


            int aux = child.get(randFirst);
            child.set(randFirst, child.get(randSecond));
            child.set(randSecond, aux);

        } else {
            int supLim = posCross.get(randIndex);
            int infLim = posCross.get(randIndex - 1);

            randFirst = (int) (Math.random() * (supLim - infLim ) + infLim);///mod1
            randSecond = (int) (Math.random() * (supLim - infLim) + infLim);

            while(randFirst == randSecond)
                randSecond = (int) (Math.random() * (supLim - infLim) + infLim);


            int aux = child.get(randFirst);
            child.set(randFirst, child.get(randSecond));
            child.set(randSecond, aux);

        }
        return child;
    }

    public List<Integer> weightedBy(List<List<Integer>> population, int[] sudoku) {

        geneticAlgorithm  people = new geneticAlgorithm ();
        List<Integer> weights = new ArrayList<>();

        for (List<Integer> people1 : population)
            weights.add(people.fitnessFunction(people.reconstructFunction(people1, sudoku)));

        return weights;
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
