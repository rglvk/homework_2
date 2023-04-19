/******************************************************************************
*  A Teaching GA					  Developed by Hal Stringer & Annie Wu, UCF
*  Version 2, January 18, 2004
*******************************************************************************/

import java.io.*;
import java.util.*;
import java.text.*;

public class Chromo
{
/*******************************************************************************
*                            INSTANCE VARIABLES                                *
*******************************************************************************/

	ArrayList<Integer> chromosome = new ArrayList<Integer>();
	public static ArrayList<Integer> crossover_indices = new ArrayList<Integer>(Arrays.asList(29, 30, 31, 32, 33, 39, 40, 41, 42, 43));
	public double rawFitness;
	public double sclFitness;
	public double proFitness;

	static ArrayList<Double> lat_array = new ArrayList<>(Arrays.asList(33.209438, 36.068681, 32.23564, 34.07088, 40.00694, 41.82176, 39.68103, 29.63288, 
    33.94043, 46.7238, 40.09912, 39.18024, 41.66831, 38.95349, 38.02704, 30.21929, 
    44.89914, 38.99041, 42.38694, 42.29421, 44.97101, 34.36461, 38.93641, 46.85461, 
    40.82068, 39.54427, 43.13827, 40.74213, 35.08663, 40.90988, 35.90504, 47.92654, 
    39.323, 35.19599, 44.04455, 39.94934, 41.4873, 33.99288, 42.79137, 35.95164, 
    30.28522, 40.76281, 44.47374, 38.04106, 47.65435, 39.65369, 43.08027, 41.31423));

    static ArrayList<Double> long_array = new ArrayList<>(Arrays.asList(-87.54149, -94.17601, -110.95174, -118.44685, -105.26639, -72.24278, -75.75402, 
    -82.34901, -83.37305, -117.02044, -88.23852, -86.50935, -91.57953, -95.26309, 
    -84.50484, -92.04138, -68.66637, -76.94386, -72.52991, -83.71004, -93.23144, 
    -89.53963, -92.3297, -113.9655, -96.70048, -119.8163, -70.93238, -74.17903, 
    -106.6202, -73.12155, -79.04775, -97.07212, -82.10268, -97.44571, -123.0717, 
    -75.18964, -71.53446, -81.02675, -96.92542, -83.93088, -97.73389, -111.8368, 
    -73.19415, -78.5055, -122.308, -79.95745, -89.43096, -105.5643));


/*******************************************************************************
*                            INSTANCE VARIABLES                                *
*******************************************************************************/

	private static double randnum;

/*******************************************************************************
*                              CONSTRUCTORS                                    *
*******************************************************************************/

	public Chromo(){
		
		//  Set gene values to a randum sequence of 1's and 0's
		Random rand = new Random(); 
        int upperbound = 48;
        
        while (chromosome.size() < 48){
            int int_random = rand.nextInt(upperbound);
            if (chromosome.contains(int_random)) {
                continue;
            } else{
                chromosome.add(int_random);
            }

        }
		/*for(int i = 0; i < chromosome.size(); i++)
		{
			System.out.print(chromosome.get(i) + " ");
		}*/
		//System.out.println();
		this.rawFitness = -1;   //  Fitness not yet evaluated
		this.sclFitness = -1;   //  Fitness not yet scaled
		this.proFitness = -1;   //  Fitness not yet proportionalized
	}


/*******************************************************************************
*                                MEMBER METHODS                                *
*******************************************************************************/

	//  Mutate a Chromosome Based on Mutation Type *****************************

	public void doMutation(){
		
		switch (Parameters.mutationType){

		case 1:     
			//Possibly set this to occur only once per generation?
			/*System.out.print("Before mutation: ");
			for(int i = 0; i < chromosome.size(); i++)
			{
				System.out.print(chromosome.get(i) + " ");
			} 
			System.out.println();*/
			Random rand = new Random();
			ArrayList<Integer> copy_chromotest = new ArrayList<Integer>(Collections.nCopies(48, 0));
			//  Set lists as not to mess with parents
			for(int i = 0; i<chromosome.size(); i++)
			{
				copy_chromotest.set(i, chromosome.get(i));
			}
			for(int l = 0; l < 48; l++)
			{
				double mut = rand.nextDouble();
				if(mut < Parameters.mutationRate)
				{
					int startIndex = rand.nextInt(48);
					int endIndex = rand.nextInt(48);
					if(startIndex == endIndex)
					{
						if(startIndex == 0)
							endIndex = endIndex + 1;
						else
							startIndex = startIndex - 1; 
					}
					if(startIndex > endIndex)
					{
						int h = endIndex;
						endIndex = startIndex;
						startIndex = h;
					}
					System.out.println("Doing mutation. Sdex: " + startIndex + " Edex: " + endIndex);
					int subsetSize = endIndex-startIndex;
					List<Integer> subset = new ArrayList<>();
    				for (int i = 0; i < subsetSize; i++) 
					{
        				int index = (startIndex + i) % copy_chromotest.size();
       					subset.add(copy_chromotest.get(index));
    				}
					List<Integer> sortedSubset = new ArrayList<>(subset);
					for(int x = startIndex; x < subsetSize-1; x++)
					{
						double minDistance = Double.MAX_VALUE;
        				int minIndex = -1;
						for(int y = x + 1; y < subsetSize; y ++)
						{
							int c1Index = subset.get(x);
							int c2Index = subset.get(y);
							double lat1 = lat_array.get(c1Index);
							double lng1 = long_array.get(c1Index);
							double lat2 = lat_array.get(c2Index);
							double lng2 = long_array.get(c2Index);
							double dis = distance_chromo(lat1, lat2, lng1, lng2);
							if (dis < minDistance) 
							{
								minDistance = dis;
								minIndex = y;
							}
						}
						if (minIndex != -1) 
						{
							int temp = sortedSubset.get(x + 1);
							sortedSubset.set(x + 1, sortedSubset.get(minIndex));
							sortedSubset.set(minIndex, temp);
						}
					}
					for (int i = 0; i < subsetSize; i++) {
						int index = (startIndex + i) % copy_chromotest.size();
						copy_chromotest.set(index, sortedSubset.get(i));
					}
				}
			}
			chromosome = copy_chromotest;

			/*System.out.print("After mutation: ");
			for(int i = 0; i < chromosome.size(); i++)
			{
				System.out.print(chromosome.get(i) + " ");
			}
			System.out.println();*/

			break;

		case 2: //Length 4
		/*System.out.print("Before mutation: ");
		for(int i = 0; i < chromosome.size(); i++)
		{
			System.out.print(chromosome.get(i) + " ");
		}
		System.out.println();*/
		Random rand2 = new Random();
		ArrayList<Integer> copy_chromo = new ArrayList<Integer>(Collections.nCopies(48, 0));
		//  Set lists as not to mess with parents
		for(int i = 0; i<chromosome.size(); i++)
		{
			copy_chromo.set(i, chromosome.get(i));
		}
		for(int l = 0; l < 48; l++)
		{
			double mut = rand2.nextDouble();
			if(mut < Parameters.mutationRate)
			{
				int startIndex = rand2.nextInt(44);
				int endIndex = startIndex + 4;
				
				//System.out.println("Doing mutation. Sdex: " + startIndex + " Edex: " + endIndex);
				int subsetSize = endIndex-startIndex;
				List<Integer> subset = new ArrayList<>();
				for (int i = 0; i < subsetSize; i++) 
				{
					int index = (startIndex + i) % copy_chromo.size();
					   subset.add(copy_chromo.get(index));
				}
				List<Integer> sortedSubset = new ArrayList<>(subset);
				for(int x = startIndex; x < subsetSize-1; x++)
				{
					double minDistance = Double.MAX_VALUE;
					int minIndex = -1;
					for(int y = x + 1; y < subsetSize; y ++)
					{
						int c1Index = subset.get(x);
						int c2Index = subset.get(y);
						double lat1 = lat_array.get(c1Index);
						double lng1 = long_array.get(c1Index);
						double lat2 = lat_array.get(c2Index);
						double lng2 = long_array.get(c2Index);
						double dis = distance_chromo(lat1, lat2, lng1, lng2);
						if (dis < minDistance) 
						{
							minDistance = dis;
							minIndex = y;
						}
					}
					if (minIndex != -1) 
					{
						int temp = sortedSubset.get(x + 1);
						sortedSubset.set(x + 1, sortedSubset.get(minIndex));
						sortedSubset.set(minIndex, temp);
					}
				}
				for (int i = 0; i < subsetSize; i++) {
					int index = (startIndex + i) % copy_chromo.size();
					copy_chromo.set(index, sortedSubset.get(i));
				}
			}
		}
		chromosome = copy_chromo;

		/*System.out.print("After mutation: ");
		for(int i = 0; i < chromosome.size(); i++)
		{
			System.out.print(chromosome.get(i) + " ");
		}
		System.out.println();*/

		break;

		case 3: //Length 8
		/*System.out.print("Before mutation: ");
		for(int i = 0; i < chromosome.size(); i++)
		{
			System.out.print(chromosome.get(i) + " ");
		}
		System.out.println();*/
		Random rand3 = new Random();
		ArrayList<Integer> copy_chromod = new ArrayList<Integer>(Collections.nCopies(48, 0));
		//  Set lists as not to mess with parents
		for(int i = 0; i<chromosome.size(); i++)
		{
			copy_chromod.set(i, chromosome.get(i));
		}
		for(int l = 0; l < 48; l++)
		{
			double mut = rand3.nextDouble();
			if(mut < Parameters.mutationRate)
			{
				int startIndex = rand3.nextInt(40);
				int endIndex = startIndex + 8;
				
				//System.out.println("Doing mutation. Sdex: " + startIndex + " Edex: " + endIndex);
				int subsetSize3 = endIndex-startIndex;
				List<Integer> subset = new ArrayList<>();
				for (int i = 0; i < subsetSize3; i++) 
				{
					int index = (startIndex + i) % copy_chromod.size();
					   subset.add(copy_chromod.get(index));
				}
				List<Integer> sortedSubset = new ArrayList<>(subset);
				for(int x = startIndex; x < subsetSize3-1; x++)
				{
					double minDistance = Double.MAX_VALUE;
					int minIndex = -1;
					for(int y = x + 1; y < subsetSize3; y ++)
					{
						int c1Index = subset.get(x);
						int c2Index = subset.get(y);
						double lat1 = lat_array.get(c1Index);
						double lng1 = long_array.get(c1Index);
						double lat2 = lat_array.get(c2Index);
						double lng2 = long_array.get(c2Index);
						double dis = distance_chromo(lat1, lat2, lng1, lng2);
						if (dis < minDistance) 
						{
							minDistance = dis;
							minIndex = y;
						}
					}
					if (minIndex != -1) 
					{
						int temp = sortedSubset.get(x + 1);
						sortedSubset.set(x + 1, sortedSubset.get(minIndex));
						sortedSubset.set(minIndex, temp);
					}
				}
				for (int i = 0; i < subsetSize3; i++) {
					int index = (startIndex + i) % copy_chromod.size();
					copy_chromod.set(index, sortedSubset.get(i));
				}
			}
		}
		chromosome = copy_chromod;

		/*System.out.print("After mutation: ");
		for(int i = 0; i < chromosome.size(); i++)
		{
			System.out.print(chromosome.get(i) + " ");
		}
		System.out.println();*/

		break;

		case 4: //Length 16
		/*System.out.print("Before mutation: ");
		for(int i = 0; i < chromosome.size(); i++)
		{
			System.out.print(chromosome.get(i) + " ");
		}
		System.out.println();*/
		Random rand4 = new Random();
		ArrayList<Integer> copy_chrood = new ArrayList<Integer>(Collections.nCopies(48, 0));
		//  Set lists as not to mess with parents
		for(int i = 0; i<chromosome.size(); i++)
		{
			copy_chrood.set(i, chromosome.get(i));
		}
		for(int l = 0; l < 48; l++)
		{
			double mut = rand4.nextDouble();
			if(mut < Parameters.mutationRate)
			{
				int startIndex = rand4.nextInt(32);
				int endIndex = startIndex + 16;
				
				//System.out.println("Doing mutation. Sdex: " + startIndex + " Edex: " + endIndex);
				int subsetSize4 = endIndex-startIndex;
				List<Integer> subset4 = new ArrayList<>();
				for (int i = 0; i < subsetSize4; i++) 
				{
					int index = (startIndex + i) % copy_chrood.size();
					   subset4.add(copy_chrood.get(index));
				}
				List<Integer> sortedSubset4 = new ArrayList<>(subset4);
				for(int x = startIndex; x < subsetSize4-1; x++)
				{
					double minDistance = Double.MAX_VALUE;
					int minIndex = -1;
					for(int y = x + 1; y < subsetSize4; y ++)
					{
						int c1Index = subset4.get(x);
						int c2Index = subset4.get(y);
						double lat1 = lat_array.get(c1Index);
						double lng1 = long_array.get(c1Index);
						double lat2 = lat_array.get(c2Index);
						double lng2 = long_array.get(c2Index);
						double dis = distance_chromo(lat1, lat2, lng1, lng2);
						if (dis < minDistance) 
						{
							minDistance = dis;
							minIndex = y;
						}
					}
					if (minIndex != -1) 
					{
						int temp = sortedSubset4.get(x + 1);
						sortedSubset4.set(x + 1, sortedSubset4.get(minIndex));
						sortedSubset4.set(minIndex, temp);
					}
				}
				for (int i = 0; i < subsetSize4; i++) {
					int index = (startIndex + i) % copy_chrood.size();
					copy_chrood.set(index, sortedSubset4.get(i));
				}
			}
		}
		chromosome = copy_chrood;

		/*System.out.print("After mutation: ");
		for(int i = 0; i < chromosome.size(); i++)
		{
			System.out.print(chromosome.get(i) + " ");
		}
		System.out.println();*/

		break;
		/*case 4:
			/*System.out.print("Before mutation: ");
			for(int i = 0; i < chromosome.size(); i++)
			{
				System.out.print(chromosome.get(i) + " ");
			}
			System.out.println();
			int xoverPoint1;
			int xoverPoint2;
			//  Select the mutation points
            xoverPoint1 = -1;
            xoverPoint2 = 1 + (int)(Search.r.nextDouble() * (Parameters.numGenes * Parameters.geneSize-1));

			ArrayList<Integer> copy_chromotest2 = new ArrayList<Integer>(Collections.nCopies(48, 0));
			//  Set lists as not to mess with parents
			for(int i = 0; i<chromosome.size(); i++)
			{
				copy_chromotest2.set(i, chromosome.get(i));
			}

			Random randos = new Random();
			for(int l = 0; l < 48; l++)
			{
				double mut = randos.nextDouble();
				if(mut < Parameters.mutationRate)
				{
					xoverPoint1 = l;
				}
			}

			if(xoverPoint1 == -1)
			{
				break;
			}
            // ensures unique points and that point 1 occurs before point 2
            if (xoverPoint1 == xoverPoint2)
            {
                xoverPoint2 = 48-1;
            }
            else if (xoverPoint2 < xoverPoint1)
            {
                int temp = xoverPoint1;
                xoverPoint1 = xoverPoint2;
                xoverPoint2 = temp;
            }
            // moves element at point2 next to element at point 1
            for (int i = xoverPoint2-1; i > xoverPoint1; i--)
            {
                int temp; 
				temp = copy_chromotest2.get(i+1);
                copy_chromotest2.set(i+1, copy_chromotest2.get(i));
                copy_chromotest2.set(i,temp);
            }

			/*System.out.print("After mutation: ");
			for(int i = 0; i < chromosome.size(); i++)
			{
				System.out.print(chromosome.get(i) + " ");
			}
			System.out.println();

			chromosome = copy_chromotest2;
            break;

		case 5:		//Displacement mutation
			ArrayList<Integer> chromo_copy = chromosome;
			Random rands = new Random();
			double mut = rands.nextDouble();
			if(mut >= Parameters.mutationRate)
			{
				break;
			}
			System.out.println("Hey I'm running when I'm not supposed to! But case 6!");
			ArrayList<Integer> first_ten = new ArrayList<Integer>();
			for (int i = 0; i < 10; i++) {
				first_ten.add(chromo_copy.get(i));
				
			}
			if (first_ten.size() != 10) {
				System.out.println("first ten : " + first_ten.size());
			}

			ArrayList<Integer> second_ten = new ArrayList<Integer>();
			for (int i = 10; i < 20; i++) {
		
				second_ten.add(chromo_copy.get(i));
				
			}
			if (second_ten.size() != 10) {
				System.out.println("second ten : " + second_ten.size());
			}

			ArrayList<Integer> third_ten = new ArrayList<Integer>();
			for (int i = 20; i < 30; i++) {
				
				third_ten.add(chromo_copy.get(i));
				
			}
			if (third_ten.size() != 10) {
				System.out.println("third ten : " + third_ten.size());
			}

			ArrayList<Integer> last = new ArrayList<Integer>();
			for (int i = 30; i < 48; i++) {
				
				last.add(chromo_copy.get(i));
				
			}
			if (last.size() != 18) {
				System.out.println("chromo line 117: last " + last.size());
			}

			
			
			

			ArrayList<Integer> new_chromo = new ArrayList<Integer>();

			Random rando = new Random(); 
			int int_random = rando.nextInt(100);

			if (int_random < 20){
				new_chromo.addAll(first_ten);
				new_chromo.addAll(third_ten);
				new_chromo.addAll(second_ten);
				new_chromo.addAll(last);
			} else if (20 <= int_random && int_random < 40){
				new_chromo.addAll(first_ten);
				new_chromo.addAll(last);
				new_chromo.addAll(third_ten);
				new_chromo.addAll(second_ten);
				
			} else if (40 <= int_random && int_random < 60){
				new_chromo.addAll(second_ten);
				new_chromo.addAll(first_ten);
				new_chromo.addAll(third_ten);
				new_chromo.addAll(last);
			} else if (60 <= int_random && int_random < 80){
				new_chromo.addAll(first_ten);
				new_chromo.addAll(second_ten);
				new_chromo.addAll(last);
				new_chromo.addAll(third_ten);
				
				
			} else if (80 <= int_random && int_random < 100){
				new_chromo.addAll(third_ten);
				new_chromo.addAll(first_ten);
				new_chromo.addAll(last);
				new_chromo.addAll(second_ten);
				
			}



			// new_chromo.addAll(first_ten);
			// new_chromo.addAll(third_ten);
			// new_chromo.addAll(second_ten);
			// new_chromo.addAll(last);

			if (new_chromo.size() != 48){
				System.out.println("chromo line 138, " + new_chromo.size());
			}
			// return(new_chromo);
			// chromosome = new_chromo;
			for (int i = 0; i < new_chromo.size(); i ++) {
				int hold = new_chromo.get(i); // want to avoid using pointers
				chromo_copy.set(i, hold);
			}
			chromosome = chromo_copy;
		break; */
		default:
			System.out.println("ERROR - No mutation method selected");
		}
	}

/*******************************************************************************
*                             STATIC METHODS                                   *
*******************************************************************************/

	//  Select a parent for crossover ******************************************

	public static int selectParent(){

		double rWheel = 0;
		int j = 0;
		int k = 0;

		switch (Parameters.selectType){

		case 1:     // Proportional Selection
			randnum = Search.r.nextDouble();
			for (j=0; j<Parameters.popSize; j++){
				rWheel = rWheel + Search.member[j].proFitness;
				if (randnum < rWheel) return(j);
			}
			break;

		case 3:     // Random Selection
			randnum = Search.r.nextDouble();
			j = (int) (randnum * Parameters.popSize);
			return(j);

		case 2:     //  Tournament Selection

		default:
			System.out.println("ERROR - No selection method selected");
		}
	return(-1);
	}

	//  Produce a new child from two parents  **********************************

	public static void mateParents(int pnum1, int pnum2, Chromo parent1, Chromo parent2, Chromo child1, Chromo child2){

		int xoverPoint1;
		int xoverPoint2;

		//System.out.println("Doing crossover");

		switch (Parameters.xoverType){

		case 1:     //  Cycle crossover

			/*System.out.print("Parent1 before: ");
			for(int i = 0; i < child1.chromosome.size(); i++)
			{
				System.out.print(parent1.chromosome.get(i) + " ");
			}
			System.out.println();*/

			// Initalize cycle array
			ArrayList<Integer> cycle = new ArrayList<Integer>();

			//  Select crossover point
			xoverPoint1 = 1 + (int)(Search.r.nextDouble() * (Parameters.numGenes * Parameters.geneSize-1));

			// Add point to ArrayLisy
			cycle.add(xoverPoint1);

			ArrayList<Integer> copy_chromo = new ArrayList<Integer>(Collections.nCopies(48, 0));
			ArrayList<Integer> copy_chromo2 = new ArrayList<Integer>(Collections.nCopies(48, 0));
			//  Set lists as not to mess with parents
			for(int i = 0; i<parent1.chromosome.size(); i++)
			{
				copy_chromo.set(i, parent1.chromosome.get(i));
				copy_chromo2.set(i, parent2.chromosome.get(i));
			}
			

			// Get point from the same postition from other List
			xoverPoint2 =  copy_chromo2.get(xoverPoint1);

			// Get index of the above in child 1
			xoverPoint1 = copy_chromo.indexOf(xoverPoint2);

			// Fill the cycle list
			while(xoverPoint1 != cycle.get(0))
			{
				cycle.add(xoverPoint1);
				xoverPoint2 = copy_chromo2.get(xoverPoint1);
				xoverPoint1 = copy_chromo.indexOf(xoverPoint2);
			}
			
			//Swap the indexes
			for(int i : cycle)
			{
				int hold = copy_chromo.get(i);
				copy_chromo.set(i, copy_chromo2.get(i));
				copy_chromo2.set(i, hold);
			}
			
			child1.chromosome = copy_chromo;
			child2.chromosome = copy_chromo2;

			/*System.out.print("Parent1 after: ");
			for(int i = 0; i < child1.chromosome.size(); i++)
			{
				System.out.print(parent1.chromosome.get(i) + " ");
			}
			System.out.println();*/

			break;

		case 2:		//  PMX

			if(parent1.chromosome == parent2.chromosome)
				break;
				
			//  Select crossover point
			xoverPoint1 = 1 + (int)(Search.r.nextDouble() * (Parameters.numGenes * Parameters.geneSize-1));
			xoverPoint2 = 1 + (int)(Search.r.nextDouble() * (Parameters.numGenes * Parameters.geneSize-1));

			/*for(int i = 0; i<parent1.chromosome.size(); i++)
			{
				int p1 = parent1.chromosome.get(i);
				int p2 = parent2.chromosome.get(i);
				child1.chromosome.set(i,p1);
				child2.chromosome.set(i,p2);
			}*/
			ArrayList<Integer> copy1 = new ArrayList<Integer>(Collections.nCopies(48, 0));
			ArrayList<Integer> copy2 = new ArrayList<Integer>(Collections.nCopies(48, 0));
			//  Set lists as not to mess with parents
			for(int i = 0; i<parent1.chromosome.size(); i++)
			{
				copy1.set(i, parent1.chromosome.get(i));
				copy2.set(i, parent2.chromosome.get(i));
			}
			/*System.out.print("Parent1 post copy: ");
			for(int i = 0; i<parent1.chromosome.size(); i++)
			{
				System.out.print(parent1.chromosome.get(i) + " ");
			}
			System.out.println();
			System.out.print("Parent2 post copy: ");
			for(int i = 0; i<child1.chromosome.size(); i++)
			{
				System.out.print(parent2.chromosome.get(i) + " ");
			}
			System.out.println();*/

			// ensures unique points and that point 1 occurs before point 2
			if (xoverPoint1 == xoverPoint2)
			{
				xoverPoint2 = 48-1;
			}
			else if (xoverPoint2 < xoverPoint1)
			{
				int temp = xoverPoint1;
				xoverPoint1 = xoverPoint2;
				xoverPoint2 = temp;
			}
			//System.out.println("Points: " + xoverPoint1 + " " + xoverPoint2);
			// swap of xcrossover site
			for (int i = xoverPoint1; i <= xoverPoint2; i++)
			{
				//System.out.println("Swapping " + child1.chromosome.get(i) + " " + child2.chromosome.get(i));
				//child1.chromosome.set(i, parent2.chromosome.get(i));
				//child2.chromosome.set(i, parent1.chromosome.get(i));
				int hold = copy1.get(i);
				//System.out.println("Before " + child1.chromosome.get(i));
				copy1.set(i, copy2.get(i));
				//System.out.println("After " + child1.chromosome.get(i));
				copy2.set(i, hold);
			}

			//Set all non swaps to useless value
			for (int i = 0; i < 48; i++)
			{
				if (i < xoverPoint1 || i > xoverPoint2)
				{
					copy1.set(i, -2);
					copy2.set(i, -2);
				}
			}
			
			/*System.out.println("Child 1 before: ");
			for(int i = 0; i<child1.chromosome.size(); i++)
			{
				System.out.print(copy1.get(i) + " ");
			}
			System.out.println();
			System.out.println("Child 2 before: ");
			for(int i = 0; i<child1.chromosome.size(); i++)
			{
				System.out.print(copy2.get(i) + " ");
			}
			System.out.println();*/
			// Repopulate children
			for (int i = 0; i < 48; i++)
			{
				if (i < xoverPoint1 || i > xoverPoint2)
				{
					int count = 0;
					int resert = parent1.chromosome.get(i);
					while(copy1.contains(resert))
					{
						if(count > 10)
						{
							
							break;
						}
						
						int dex = copy1.indexOf(resert);
						resert = copy2.get(dex);
						//System.out.println("Stuck in while 1! Resert "+ resert + " Dex " + dex);
						//count = count + 1;
					}
					copy1.set(i,resert);
					count = 0;

					resert = parent2.chromosome.get(i);
					while(copy2.contains(resert))
					{
						if(count > 10)
						{
							
							break;
						}
						int dex = copy2.indexOf(resert);
						resert = copy1.get(dex);
						//System.out.println("Stuck in while 2! Resert "+ resert + " Dex " + dex);
						//count = count + 1;
					}
					copy2.set(i,resert);

				}
			}
			/*System.out.print("Parent1: ");
			for(int i = 0; i<parent1.chromosome.size(); i++)
			{
				System.out.print(parent1.chromosome.get(i) + " ");
			}
			System.out.println();
			System.out.print("Parent2: ");
			for(int i = 0; i<child1.chromosome.size(); i++)
			{
				System.out.print(parent2.chromosome.get(i) + " ");
			}
			System.out.println("\n"); 
			System.out.print("Child1 After: ");
			for(int i = 0; i<copy1.size(); i++)
			{
				System.out.print(copy1.get(i) + " ");
			}
			System.out.println();
			
			System.out.print("Child2: ");
			for(int i = 0; i<copy2.size(); i++)
			{
				System.out.print(copy2.get(i) + " ");
			}
			System.out.println();*/

			child1.chromosome = copy1;
			child2.chromosome = copy2;
			break;

		case 3:     //  Position based

			
			ArrayList<Integer> child_from_par_2 = new ArrayList<Integer>(Collections.nCopies(48, 0));
				
			ArrayList<Integer> parent_2_at_indices = new ArrayList<Integer>(Collections.nCopies(10, 0));
			for (int i = 0; i < 10; i++) {
				parent_2_at_indices.set(i, parent2.chromosome.get(crossover_indices.get(i)));
			}
			
			ArrayList<Integer> parent_1_not_in_crossover = new ArrayList<Integer>(Collections.nCopies(48, 888));

			for (int p = 0; p < 48; p++) {
				if (!(parent_2_at_indices.contains(parent1.chromosome.get(p)))) {
					parent_1_not_in_crossover.set(p, parent1.chromosome.get(p));
				}
			}
			parent_1_not_in_crossover.removeAll(Arrays.asList(888));
		

			ListIterator<Integer> iterator = parent_1_not_in_crossover.listIterator();
			
			for (int i = 0; i < 29; i++) {
				
				child_from_par_2.set(i, iterator.next());
				

			}

			for (int i = 29; i < 34; i++) {
				child_from_par_2.set(i, parent_2_at_indices.get(i - 29));
			}

			for (int i = 34; i < 39; i++) {
				child_from_par_2.set(i, iterator.next());
			}

			for (int i = 39; i < 44; i++) {
				child_from_par_2.set(i, parent_2_at_indices.get(i - 34));
			}

			// System.out.println("chromo line 366, child_from_par_1 size : " + child_from_par_1.size());
			// System.out.println("chromo line 271, child_from_par_2 size : " + child_from_par_2.size());

			for (int i = 44; i < 48; i++) {
				int hello = iterator.next();
			
				child_from_par_2.set(i, hello);
			}         

	// now doing child_from_par_1:
	// CHILD_FROM_PAR_1 :
	// CHILD_FROM_PAR_1 :
	// CHILD_FROM_PAR_1 :
	// CHILD_FROM_PAR_1 :
	// CHILD_FROM_PAR_1 :
	// CHILD_FROM_PAR_1 :
	// CHILD_FROM_PAR_1 :

			ArrayList<Integer> child_from_par_1 = new ArrayList<Integer>(Collections.nCopies(48, 0));

			ArrayList<Integer> parent_1_at_indices = new ArrayList<Integer>(Collections.nCopies(10, 0));
			for (int i = 0; i < 10; i++) {
				parent_1_at_indices.set(i, parent1.chromosome.get(crossover_indices.get(i)));
			}
			
			ArrayList<Integer> parent_2_not_in_crossover = new ArrayList<Integer>(Collections.nCopies(48, 888));

			for (int p = 0; p < 48; p++) {
				if (!(parent_1_at_indices.contains(parent2.chromosome.get(p)))) {
					parent_2_not_in_crossover.set(p, parent2.chromosome.get(p));
					// System.out.println("chromo line 306 : " + parent_2_not_in_crossover.size());
				}
			}
			parent_2_not_in_crossover.removeAll(Arrays.asList(888));
				
			
			

			ListIterator<Integer> second_iterator = parent_2_not_in_crossover.listIterator();
			
			for (int i = 0; i < 29; i++) {
				
				child_from_par_1.set(i, second_iterator.next());
				

			}

			for (int i = 29; i < 34; i++) {
				child_from_par_1.set(i, parent_1_at_indices.get(i - 29));
			}

			for (int i = 34; i < 39; i++) {
				child_from_par_1.set(i, second_iterator.next());
				
			}

			for (int i = 39; i < 44; i++) {
				child_from_par_1.set(i, parent_1_at_indices.get(i - 34));
			}
			// System.out.println("chromo line 332, child_from_par_1 size : " + child_from_par_1.size());
			// System.out.println("chromo line 333, child_from_par_2 size : " + child_from_par_2.size());

			for (int i = 44; i < 48; i++) {
				int hello_two = second_iterator.next();
				
				child_from_par_1.set(i, hello_two);
				
			}
		



		System.out.println("chromo line 287, child_from_par_1 size : " + child_from_par_1.size());
		System.out.println("chromo line 288, child_from_par_2 size : " + child_from_par_2.size() + "\n");

		child1.chromosome = child_from_par_1;
		child2.chromosome = child_from_par_2;
		
		break;

		default:
			System.out.println("ERROR - Bad crossover method selected: " + Parameters.xoverType);
		}

		//  Set fitness values back to zero
		child1.rawFitness = -1;   //  Fitness not yet evaluated
		child1.sclFitness = -1;   //  Fitness not yet scaled
		child1.proFitness = -1;   //  Fitness not yet proportionalized
		child2.rawFitness = -1;   //  Fitness not yet evaluated
		child2.sclFitness = -1;   //  Fitness not yet scaled
		child2.proFitness = -1;   //  Fitness not yet proportionalized
	}

	//  Produce a new child from a single parent  ******************************

	public static void mateParents(int pnum, Chromo parent, Chromo child){

		//  Create child chromosome from parental material
		child.chromosome = parent.chromosome;

		//  Set fitness values back to zero
		child.rawFitness = -1;   //  Fitness not yet evaluated
		child.sclFitness = -1;   //  Fitness not yet scaled
		child.proFitness = -1;   //  Fitness not yet proportionalized
	}

	//  Copy one chromosome to another  ***************************************

	public static void copyB2A (Chromo targetA, Chromo sourceB){

		targetA.chromosome = sourceB.chromosome;

		targetA.rawFitness = sourceB.rawFitness;
		targetA.sclFitness = sourceB.sclFitness;
		targetA.proFitness = sourceB.proFitness;
		return;
	}

	//This is here so we can do GSTM inside chromo. It is the same as our fitness function.
	public static double distance_chromo(double lat1, double lat2, double lon1, double lon2){
        // convert to radians
        double nlon1 = Math.toRadians(lon1);
        double nlon2 = Math.toRadians(lon2);
        double nlat1 = Math.toRadians(lat1);
        double nlat2 = Math.toRadians(lat2);
        // distance
        // Haversine formula
        double dlon = nlon2 - nlon1;
        double dlat = nlat2 - nlat1;
        double a = Math.pow(Math.sin(dlat / 2), 2) + Math.cos(nlat1) * Math.cos(nlat2) * Math.pow(Math.sin(dlon / 2),2);
    
        double c = 2 * Math.asin(Math.sqrt(a));

        // Radius of earth in kilometers. Use 3956
        // for miles
        double r = 6371;

        // calculate the result
        //System.out.println("Distance between " + lat1 + " " + lon1 + " and " + lat2 + " " + lon2 + " is: " + c*r);
        return(c * r);
    }
}   // End of Chromo.java ******************************************************
