/******************************************************************************
*  A Teaching GA					  Developed by Hal Stringer & Annie Wu, UCF
*  Version 2, January 18, 2004
*******************************************************************************/

import java.io.*;
import java.util.*;
import java.text.*;

public class PointScatter extends FitnessFunction{

/*******************************************************************************
*                            INSTANCE VARIABLES                                *
*******************************************************************************/


/*******************************************************************************
*                            STATIC VARIABLES                                  *
*******************************************************************************/


/*******************************************************************************
*                              CONSTRUCTORS                                    *
*******************************************************************************/

	public PointScatter(){
		name = "PointScatter Problem";
	}

/*******************************************************************************
*                                MEMBER METHODS                                *
*******************************************************************************/

//  COMPUTE A CHROMOSOME'S RAW FITNESS *************************************

	public void doRawFitness(Chromo X){
		X.rawFitness = run_all(X);
	}

    // run all function:
    
    public double run_all(Chromo X){
        return permut_and_min(chromo_to_polar(X));
    }

    public ArrayList<Double> chromo_to_polar(Chromo X){
        ArrayList<Double> polar_list = new ArrayList<Double>();
        String gene = new String("");
        String gene_left = new String("");
        String gene_right = new String("");
        int geneSize = Parameters.geneSize;
        int numGenes = Parameters.numGenes;
        for (int z = 0; z < numGenes; z++) {
            gene = X.chromo.substring(z * geneSize, z * geneSize + geneSize);
            gene_left = gene.substring(0, gene.length() / 2);
            gene_right = gene.substring(gene.length() / 2);
            double gene_radius = binary_to_radius(gene_left);
            double gene_angle = binary_to_angle(gene_right);
            polar_list.add(gene_radius);
            polar_list.add(gene_angle);
        }
        return polar_list;
    }
    
    public double permut_and_min(ArrayList<Double> polar_list){
        // first step is to do the permutations:
        ArrayList<Double> dist_permuts = new ArrayList<Double>();
        for (int i = 0; i < polar_list.size(); i++) {
            for (int j = i + 2; j < polar_list.size() -1; j++) {
                double radius1 = polar_list.get(i).doubleValue();
                double angle1 = polar_list.get(i + 1).doubleValue();
                double radius2 = polar_list.get(j).doubleValue();
                double angle2 = polar_list.get(j + 1).doubleValue();

                // double new_radius1 = radius1.doubleValue();
                // double new_angle1 = angle1.doubleValue();
                // double new_radius2 = radius2.doubleValue();
                // double new_angle2 = angle2.doubleValue();
                double the_distance = distance(radius1, angle1, radius2, angle2);
                dist_permuts.add(the_distance);
            }
        }
        double minimum = Collections.min(dist_permuts);
        return minimum;
    }
    

    
    public double binary_to_radius(String gene_left){
        double a_left_int = Integer.parseInt(gene_left, 2);
        double num = 1023.0f;
        double radius = a_left_int / num;
        radius = radius * 1000000.0;
        radius = Math.floor(radius);
        radius = radius / 1000000.0;
        return radius;  
        }

    public double binary_to_angle(String gene_right){
        double a_right_int = Integer.parseInt(gene_right, 2);
        double num = 1023.0f;
        double angle = a_right_int / num;
        angle = angle * 10000.0;
        angle = Math.floor(angle);
        angle = angle / 10000.0;
        angle = angle * 360.0;
        return angle;
    }
        
    public double distance(double radius_1, double angle_1, double radius_2, double angle_2){
        // convert to radians
        angle_1 = angle_1 * 3.1415926535 / 180.0;
        angle_2 = angle_2 * 3.1415926535 / 180.0;
        // distance
        double left_part = Math.pow(radius_1, 2) + Math.pow(radius_2, 2);
        double right_part = 2 * radius_1 * radius_2 * Math.cos(angle_1 - angle_2); 
        double prelim_dist = left_part - right_part;
        double distance = Math.sqrt(prelim_dist);
        return distance;
    }

//  PRINT OUT AN INDIVIDUAL GENE TO THE SUMMARY FILE *********************************

	public void doPrintGenes(Chromo X, FileWriter output) throws java.io.IOException{

		for (int i=0; i<Parameters.numGenes; i++){
			Hwrite.right(X.getGeneAlpha(i),11,output);
		}
		output.write("   RawFitness");
		output.write("\n        ");
		for (int i=0; i<Parameters.numGenes; i++){
			Hwrite.right(X.getPosIntGeneValue(i),11,output);
		}
		Hwrite.right((int) X.rawFitness,13,output);
		output.write("\n\n");
		return;
	}

/*******************************************************************************
*                             STATIC METHODS                                   *
*******************************************************************************/

}   // End of OneMax.java ******************************************************

