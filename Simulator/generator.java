import java.util.*;
public class generator{

	set job_class_set = new set(); // stores the different classes :: property of the network

	double lambda = 1;//0.001; //*************lambda is number of jobs per mili second
	double time_frame = 15;//10000; // time interval for the job generation
	double arr_time;
	double inter_arr_time;	
	double p,s;
	int counter = 0;
	set job_set = new set();

	int seed = 11000;
	public Random  r_generator = new Random(seed);
		
	
	public void initialize_classes(int number_of_classes){
		job_class x;
		for(int i=0;i<number_of_classes;i++){
			x = new job_class(i);
			job_class_set.Insert(x); 
		}
	}
	public void calculate(double t_time, int number_of_classes){ //t_time is the current time of system
		arr_time = t_time;
		double end_time=arr_time+time_frame;
		while(arr_time<end_time){
			counter++;
			p  = r_generator.nextDouble();                           
			inter_arr_time = -Math.log(1.0-p)/lambda; // inter arrival time is calculated in a manner so as to gurantee qa poisson's process
			inter_arr_time = roundAvoid(inter_arr_time,2);
			arr_time = arr_time+inter_arr_time;
			arr_time = roundAvoid(arr_time, 2);
			job x = new job(counter);
			
			//*///********to be decided how to calculate this********
			
			s = 10*r_generator.nextDouble();  // size of each job
			s = roundAvoid(s,2);
 			
			//*********                                   ********

			double y = number_of_classes*r_generator.nextDouble();
			int y_i  = (int)y;
			job_class j_c = job_class_set.findClassById(y_i);
			j_c.add_job(x);

			x.set_job(arr_time, s,j_c, t_time);
			job_set.Insert(x);
		}
		System.out.println("Total Number of Jobs generated is "+job_set.NumberOfMembers());
	}


	public double roundAvoid(double value, int places) {
	    double scale = Math.pow(10, places);
	    return Math.round(value * scale) / scale;
	}

}


