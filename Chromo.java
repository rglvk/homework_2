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

		this.rawFitness = -1;   //  Fitness not yet evaluated
		this.sclFitness = -1;   //  Fitness not yet scaled
		this.proFitness = -1;   //  Fitness not yet proportionalized
	}


/*******************************************************************************
*                                MEMBER METHODS                                *
*******************************************************************************/

	//  Mutate a Chromosome Based on Mutation Type *****************************

	public void doMutation(){

        int xoverPoint1;
        int xoverPoint2;

        switch (Parameters.mutationType){

        // insertion mutation
        case 1: 
            //  Select crossover point
            xoverPoint1 = 1 + (int)(Search.r.nextDouble() * (Parameters.numGenes * Parameters.geneSize-1));
            xoverPoint2 = 1 + (int)(Search.r.nextDouble() * (Parameters.numGenes * Parameters.geneSize-1));

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
                int temp = chromosome.get(i+1);
                chromosome.set(i+1, chromosome.get(i));
                chromosome.set(i,temp);
            }

            break;

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

		switch (Parameters.xoverType){

		case 1:     //  PMX

			// arrays to store mappings
			ArrayList <Integer> map1 = new ArrayList<Integer>(48);
			ArrayList <Integer> map2 = new ArrayList<Integer>(48);
			
			for (int i = 0; i < 48; i++)
			{
				map1.add(-1);
				map2.add(-1);
			}


			//  Select crossover point
			xoverPoint1 = 1 + (int)(Search.r.nextDouble() * (Parameters.numGenes * Parameters.geneSize-1));
			xoverPoint2 = 1 + (int)(Search.r.nextDouble() * (Parameters.numGenes * Parameters.geneSize-1));

			// ensures unique points and that point 1 occurs before point 2
			if (xoverPoint1 == xoverPoint2)
			{
				if (xoverPoint1 > 1 && xoverPoint1 < 48)
				{
					xoverPoint1 = xoverPoint1-1;
					xoverPoint2 = xoverPoint1+1;
				}
				else {
					xoverPoint1 = 13;
					xoverPoint2 = 18;
				}
			}
			else if (xoverPoint2 < xoverPoint1)
			{
				int temp = xoverPoint1;
				xoverPoint1 = xoverPoint2;
				xoverPoint2 = temp;
			}

			// swap of xcrossover site
			for (int i = xoverPoint1; i <= xoverPoint2; i++)
			{
				child1.chromosome.set(i, parent2.chromosome.get(i));
				child2.chromosome.set(i, parent1.chromosome.get(i));

				map1.set(parent2.chromosome.get(i),parent1.chromosome.get(i));
				map2.set(parent1.chromosome.get(i),parent2.chromosome.get(i));
			}

			// populate map with parent if not already present
			for (int i = 0; i < 48; i++)
			{
				if (i < xoverPoint1 || i > xoverPoint2)
				{
					if (!map1.contains(parent1.chromosome.get(i)))
					{
						map1.set(i,parent1.chromosome.get(i));
					}

					if (!map2.contains(parent2.chromosome.get(i)))
					{
						map2.set(i,parent2.chromosome.get(i));
					}
				}

			}

			//mapping, i is index 
			for (int i = 0; i < 48; i++)
			{
				// if outside of the xover region
				if (i < xoverPoint1 || i > xoverPoint2)
				{
					// n1 is city at index i in parent 1
					// m1 is mapping of index n1
					int n1 = parent1.chromosome.get(i);
					int m1 = map1.get(n1);

					int n2 = parent2.chromosome.get(i);
					int m2 = map2.get(n2);

					for (int j = 0; j < 48; j++) 
					{
						if (m1 != -1) {
						n1 = m1;
						m1 = map1.get(m1);
						}
					}

					for (int j = 0; j < 48; j++) 
					{
						if (m2 != -1) {
						n2 = m2;
						m2 = map2.get(m2);
						}
					}

					child1.chromosome.set(i,n1);
					child2.chromosome.set(i,n2);
				}
			}
				
			break;

		case 2:     //  Two Point Crossover

		case 3:     //  Uniform Crossover

		default:
			System.out.println("ERROR - Bad crossover method selected");
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
}   // End of Chromo.java ******************************************************
