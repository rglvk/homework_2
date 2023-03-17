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

	// public String chromo;
	// make chromo below a static variable? 
	public ArrayList<Integer> chromo = new ArrayList<Integer>();
	public static ArrayList<Integer> crossover_indices = new ArrayList<Integer>(Arrays.asList(29, 30, 31, 32, 33, 39, 40, 41, 42, 43));
	public double rawFitness;
	public double sclFitness;
	public double proFitness;

/*******************************************************************************
*                            INSTANCE VARIABLES                                *
*******************************************************************************/

	private static double randnum;

/*******************************************************************************
*                              CONSTRUCTORS                                    *
*******************************************************************************/

	public Chromo(){
		//  Set gene values to a randum sequence of 1's and 0's
		// chromo = new ArrayList<Integer>();
        Random rand = new Random(); 
        int upperbound = 48;
        
        while (chromo.size() < 48){
            int int_random = rand.nextInt(upperbound);
            if (chromo.contains(int_random)) {
                continue;
            }else {
                chromo.add(int_random);
            }
        }
		if (this.chromo.size() < 48) {
				System.out.println("Line 56 : Chromo size is:  " + this.chromo.size());
				System.out.println("The chromo is:  " + this.chromo);
		}
		if (this.chromo.size() == 48){
			this.chromo = chromo;
		} else {
			System.out.println("Line 56 : Error in chromosome size, size is :  " + this.chromo.size());
		}

		this.rawFitness = -1;   //  Fitness not yet evaluated
		this.sclFitness = -1;   //  Fitness not yet scaled
		this.proFitness = -1;   //  Fitness not yet proportionalized

		if (this.chromo.size() < 48) {
				System.out.println("Line 56 : Chromo size is:  " + this.chromo.size());
				System.out.println("The chromo is:  " + this.chromo);
		}

		// return (chromo);
	}


/*******************************************************************************
*                                MEMBER METHODS                                *
*******************************************************************************/

	//  Mutate a Chromosome Based on Mutation Type *****************************

	public void doMutation(){

		

		// switch (Parameters.mutationType){

		// case 1:     // Displacement Mutation:
			// ArrayList<Integer> chromo_backup = variable_thousand;
			// ArrayList<Integer> chromo_copy = variable_thousand;
			ArrayList<Integer> chromo_copy = chromo;

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

			Random rand = new Random(); 
			int int_random = rand.nextInt(100);

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
			chromo = chromo_copy;

			
			
			// break;

		// default:
			// System.out.println("ERROR - No mutation method selected");
		// }
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

		switch (Parameters.xoverType){ // Position Based Crossover

		case 1:     //  Single Point Crossover
		

   
                ArrayList<Integer> child_from_par_2 = new ArrayList<Integer>(Collections.nCopies(48, 0));
               
                ArrayList<Integer> parent_2_at_indices = new ArrayList<Integer>(Collections.nCopies(10, 0));
                for (int i = 0; i < 10; i++) {
                    parent_2_at_indices.set(i, parent2.chromo.get(crossover_indices.get(i)));
                }
                
                ArrayList<Integer> parent_1_not_in_crossover = new ArrayList<Integer>(Collections.nCopies(48, 888));

                for (int p = 0; p < 48; p++) {
                    if (!(parent_2_at_indices.contains(parent1.chromo.get(p)))) {
                        parent_1_not_in_crossover.set(p, parent1.chromo.get(p));
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
                    parent_1_at_indices.set(i, parent1.chromo.get(crossover_indices.get(i)));
                }
                
                ArrayList<Integer> parent_2_not_in_crossover = new ArrayList<Integer>(Collections.nCopies(48, 888));

                for (int p = 0; p < 48; p++) {
                    if (!(parent_1_at_indices.contains(parent2.chromo.get(p)))) {
                        parent_2_not_in_crossover.set(p, parent2.chromo.get(p));
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

			child1.chromo = child_from_par_1;
			child2.chromo = child_from_par_2;
			
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
		child.chromo = parent.chromo;

		//  Set fitness values back to zero
		child.rawFitness = -1;   //  Fitness not yet evaluated
		child.sclFitness = -1;   //  Fitness not yet scaled
		child.proFitness = -1;   //  Fitness not yet proportionalized
	}

	//  Copy one chromosome to another  ***************************************

	public static void copyB2A (Chromo targetA, Chromo sourceB){
/*
		if (this.chromo.size() < 48) {
			System.out.println("Line 390 : Chromo size is:  " + this.chromo.size());
			System.out.println("The chromo is:  " + this.chromo);
		}
*/
		targetA.chromo = sourceB.chromo;

		targetA.rawFitness = sourceB.rawFitness;
		targetA.sclFitness = sourceB.sclFitness;
		targetA.proFitness = sourceB.proFitness;
/*
		if (this.chromo.size() < 48) {
			System.out.println("Line 401 : Chromo size is:  " + this.chromo.size());
			System.out.println("The chromo is:  " + this.chromo);
		}
*/
		return;
	}

}   // End of Chromo.java ******************************************************