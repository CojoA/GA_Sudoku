import java.util.HashSet;
import java.util.Random;
import com.qqwing.Difficulty;
import com.qqwing.QQWing;
import java.util.*;
import java.util.random.RandomGenerator;

public class Main {

    public static void main(String[] args) {
        geneticAlgorithm newSudoku = new geneticAlgorithm();

        //select difficulty
        int[] Sudoku = newSudoku.generateSudoku(5);

        List <Integer> first;

        //generate the initial chromosome
        first =  newSudoku.generateChromosome(Sudoku);

        //generate the crossover points array
        List<Integer>posCross = new ArrayList<>();
        
        
        for (int i = first.size()-9; i < first.size(); i++)
            posCross.add(first.get(i));
        
        for (int i=0 ; i<9 ;i++)
            first.remove(first.size()-1);

        
        List<List<Integer>> populations;

        // generate the population
        populations = newSudoku.generatePopulation(first,posCross);

        // reconstruct the empty sudoku game with the chromosome in order to calculate the fitness function 
        List<Integer> sudoku = newSudoku.reconstructFunction(first,Sudoku);
        
        // calculate the initial fitness function
        newSudoku.fitnessFunction(sudoku);


        List<List<Integer>> population2;
        List<Integer> parent1;
        List<Integer> parent2;
        List<Integer> child;
        List<Integer> weights;
        
        int bestFitnessScore = 0;
        int max = 0;
        double weight1 = 1;
        int last = 0;
        while (bestFitnessScore != 162) {

            population2 = new ArrayList<>();
            
            Collections.shuffle(populations);
            
            weights = newSudoku.weightedBy(populations,Sudoku);

            List<Integer> wonderChild = new ArrayList<>();

            int max1 = 0;
            int max2 = 0;
            int index2 = 0;
            int index1 = 0;

            // initiate the tournament selection
            for(int i = 0; i < weights.size() ;i++) {
                if (max1 < weights.get(i)) {
                    max1 = weights.get(i);
                    index1 = i;
                }
            }

            for(int i = 0; i < weights.size() ;i++){
                if(max2 < weights.get(i) && index1 != i){
                    max2 = weights.get(i);
                    index2 = i;
                }
            }

            for (int i = 0; i < populations.size(); i++) {

                //highest 2 fitness function chromosomes
                parent1 = populations.get(index1);
                parent2 = populations.get(index2);

                child = newSudoku.reproduce(parent1,parent2,posCross);

                //the bigger the fitness function is, the probability of mutation decreases
                if(Math.random() > Math.pow((double)max/162,10)/weight1)
                    child = newSudoku.mutation(child,posCross);

                population2.add(child);

                // extract the best child
                if(weights.get(i) >= max)
                    wonderChild = child;

            }

            populations = population2;
//            for(int i = 0;i< populations.size();i++){
//                populations.set(i,population2.get(i));
//            }

            max = 0;
            // extract the biggest weight
            for (Integer weight : weights) {
                if (max < weight)
                    max = weight;
            }


            bestFitnessScore = max;
            //if the fitness function stagnates, it is penalised -> mutation rate increases
            //escape local minimum
            if(max == last)
                weight1 += 0.01;
            else{
                weight1 = 1;
                last = max;
            }

            // print the best individuals
            if(bestFitnessScore > 0) {
                double res = Math.pow((double) max / 162, 10) / weight1;
                System.out.println("Best fitness score: " + max + " " + res);
                System.out.println(Arrays.toString(wonderChild.toArray()));
            }
        }
    }
}