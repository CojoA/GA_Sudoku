import java.util.HashSet;
import java.util.Random;
import com.qqwing.Difficulty;
import com.qqwing.QQWing;
import java.util.*;
import java.util.random.RandomGenerator;

public class Main {

    public static void main(String[] args) {
        QQWing_Sudoku newSudoku = new QQWing_Sudoku();

        int[] Sudoku = newSudoku.generateSudoku(1);

        List <Integer> first;

        first =  newSudoku.population(Sudoku);

        List<Integer>posCross = new ArrayList<>();

        for (int i = first.size()-9; i < first.size(); i++)
            posCross.add(first.get(i));


        for (int i=0 ; i<9 ;i++)
            first.remove(first.size()-1);


        List<List<Integer>> populations;

        populations = newSudoku.generatePopulation(first,posCross);// generate the population

        List<Integer> sudoku = newSudoku.reconstructFunction(first,Sudoku);

        newSudoku.fitnessFunction(sudoku);


        List<List<Integer>> population2;
        List<Integer> parent1;
        List<Integer> parent2;
        List<Integer> child;
        int bestIndividual = 0;
        int max = 0;
        double weight1 = 1;
        int last = 0;
        while (bestIndividual < 161) {

            population2 = new ArrayList<>();
            Collections.shuffle(populations);
            List<Integer> weights;
            weights = newSudoku.weightedBy(populations,Sudoku);

            List<Integer> copilulMinune = new ArrayList<>();

            int max1 = 0;
            int max2 = 0;
            int index2 = 0;
            int index1 = 0;

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

                parent1 = populations.get(index1);
                parent2 = populations.get(index2);

                child = newSudoku.reproduce(parent1,parent2,posCross);

                if(Math.random() > Math.pow((double)max/162,10)/weight1)
                    child = newSudoku.mutation(child,posCross);

                population2.add(child);

                if(weights.get(i) >= max)
                    copilulMinune = child;

            }

            for(int i = 0;i< populations.size();i++){
                populations.set(i,population2.get(i));
            }



            max = 0;

            for (Integer weight : weights) {
                if (max < weight)
                    max = weight;
            }

            bestIndividual = max;
            if(max == last)
                weight1 += 0.01;
            else{
                weight1 = 1;
                last = max;
            }

            if(bestIndividual > 0) {
                double res = Math.pow((double) max / 162, 10) / weight1;
                System.out.println("Best fitness score: " + max + " " + res);
                System.out.println(Arrays.toString(copilulMinune.toArray()));
            }
        }
    }
}