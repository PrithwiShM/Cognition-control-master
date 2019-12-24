import java.util.*;
import java.io.FileWriter;   // Import the FileWriter class
import java.io.IOException;  // Import the IOException class to handle errors
import java.io.*;

public class job_simulator{
	public static generator g1 = new generator();
	public static scheduler s1 = new scheduler();
	public static double current_time = 0;//System.currentTimeMillis();//****************tbd****************
	public static String string_in = null;
			
		
	static int number_of_links = 4; //*************************
	static int number_of_active_jobs = 0;
	static double[] C = new double [number_of_links];
	
	public static void Capacity(double l){
		for(int ind=0;ind<number_of_links;ind++){
			C[ind]=l;
		} 
	}
		
	public static void main(String[] args) {
		Vector temp_vec = new Vector();
		int number_of_classes = 5; //**********************tbd***************************************************
		
		Capacity(3.0);	

		g1.initialize_classes(number_of_classes);
		g1.calculate(current_time, number_of_classes);
		
		double time_gap = 100; //8000.0;
		double limit = current_time+time_gap;
		int counter =0;
		int out =0;
		double rr; //****temp variable for rate of a job*************************
		int [][] R = new int[1][1]; 

			



		while(current_time<limit){
			current_time++;// = System.currentTimeMillis();
			
			number_of_active_jobs = s1.active_job_set.NumberOfMembers(); //size of rate vector needed
			
			temp_vec.setSize(number_of_active_jobs);          
			
		
		/*		// testing with random rates generation 	
		String str ="echo -e [";
			//System.out.println("Number of active jobs is "+number_of_active_jobs);
			for(int k=0;k<number_of_active_jobs-1;k++){   
				rr = 1*g1.r_generator.nextDouble();   //to be taken from a file
				//rr = roundAvoid(rr,2);
				temp_vec.setElementAt(rr,k);
				str = str+rr+", ";
			}
			rr = 1*g1.r_generator.nextDouble();   //to be taken from a file
			str = str+rr+"]\n----------\n==========";
			if(number_of_active_jobs!=0)temp_vec.setElementAt(rr,number_of_active_jobs-1);
		*/		
			String s= null;
	        
			if(string_in!=null && (number_of_active_jobs!=0) && out>0){ //to avoid calling minizn at the start and when number of jobs is 0
				//System.out.println(string_in);
				try {
		            
		            Process p = Runtime.getRuntime().exec(string_in);
		       		
		            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

		            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

		            // read the output from the terminal
		            
		      	    String temp_res="";  
					int k = 0;   
		            
		            if((s = stdInput.readLine()) != null) {
		                for(int index=1;index<s.length()-1;index++){
		                	if(s.charAt(index)==','){
		                		index+=2;
		                		temp_vec.setElementAt(Double.parseDouble(temp_res),k);
		                		k++;
		                		temp_res = "";
		                	}
		                	if(s.charAt(index)==']'){
								break;
							}
							temp_res+=s.charAt(index);
		               	}
						
						temp_vec.setElementAt(Double.parseDouble(temp_res),k);
		      		
		               	//System.out.println("current rates are "+temp_vec);
		            }
		        }
		        catch (IOException e) {
		            System.out.println("exception happened in reading the rates output - here's what I know: ");
		            e.printStackTrace();
		            System.exit(-1);
		        }
			}	    

			out = updater(temp_vec, current_time);
			
			if(out>0){
				counter++;
				number_of_active_jobs = s1.active_job_set.NumberOfMembers();
				System.out.println(counter+". Time since start is "+(current_time-limit+time_gap)+" milli seconds Total Active Jobs are "+number_of_active_jobs);
				
				

				if(number_of_active_jobs>0){
					linkedlist.node temp_printer = s1.active_job_set.list.head;
					for(int i=0;i<number_of_active_jobs;i++){
						job temp_print_job = (job)temp_printer.data;
						temp_print_job.print_status();
						temp_printer = temp_printer.next;
					}
				}
				else{
					System.out.println("**************************No Active Job in the System***********************************");
				}
			
			//	System.out.println(" output :: "+ out);
			}
		}

		System.out.println("No. of Jobs Finished is "+s1.finished_job_set.NumberOfMembers()+" No. of Remaining Jobs is "+(g1.job_set.NumberOfMembers()+s1.active_job_set.NumberOfMembers())+" out of which "+s1.active_job_set.NumberOfMembers()+" jobs are in system right now");
	}

	public static int updater(Vector<Double> curr_rates, double current_time){
		int flag = 0;
		int n = curr_rates.size();
		set temp_active_set = s1.active_job_set;
		set temp_finished_set = s1.finished_job_set;
		linkedlist.node temp_node = temp_active_set.list.head;
		job temp_job;
		double temp_rate;
		double new_rem_size;
		
		//old jobs update**********************
		if(!temp_active_set.IsEmpty()){
			temp_node = temp_active_set.list.head;
			int count = 0;
			while(temp_node!=null){
				temp_job = (job)temp_node.data;
				temp_node = temp_node.next;
				temp_rate = curr_rates.get(count);
				new_rem_size = temp_job.rem_size-temp_rate*(current_time-temp_job.last_updated);
				//new_rem_size = roundAvoid(new_rem_size,2);
				temp_job.update_job(new_rem_size,(new_rem_size>0),curr_rates.get(count),current_time); //updated the job
				if(new_rem_size<=0){
					temp_active_set.Delete(temp_job);
					temp_finished_set.Insert(temp_job);
					temp_job.class_alloted.alloted_job.Delete(temp_job);
					System.out.println("Job "+temp_job.job_id+" is finished");
					flag =1;
				}
				count++;
			}
		}

		//*************************new jobs update*************************
		if(!g1.job_set.IsEmpty()){
			temp_node = g1.job_set.list.head;
			while(temp_node!=null){
				temp_job = (job)temp_node.data;
				temp_node = temp_node.next;
				if(temp_job.arv_time<=current_time){
					temp_job.update_job(temp_job.rem_size, true, 0.0,current_time);
					temp_active_set.Insert(temp_job);
					g1.job_set.Delete(temp_job);
					flag =1;
				}
				else break;
			}
		}

		//**************************R matrix update********************************

		if(flag ==1){
			number_of_active_jobs = temp_active_set.NumberOfMembers();
			int [][] R = new int[number_of_links][number_of_active_jobs];
			temp_node = temp_active_set.list.head;
			int k=0;

			string_in = "minizinc --solver ipopt model_2basic.mzn -D \"class = [";
			
			if(temp_node!=null){
				temp_job = (job)temp_node.data;	
				string_in = string_in+temp_job.class_alloted.class_id;
			}
			while(temp_node!=null){
				temp_job = (job)temp_node.data;
				switch(temp_job.class_alloted.class_id){
					case 0: 
						for(int j=0;j<number_of_links;j++)
							R[j][k]=1;
						break;
					case 1:
						R[0][k]=1;
						break;
					case 2:
						R[1][k]=1;
						break;
					case 3:
						R[2][k]=1;
						break;
					case 4:
						R[3][k]=1;
						break;
				}
				temp_node = temp_node.next;
				if(temp_node!=null){
					temp_job = (job)temp_node.data;
					string_in = string_in+", "+temp_job.class_alloted.class_id;
				}
				k++;
			}
			string_in = string_in+"];";
			
			if(flag==1 && number_of_active_jobs>0){
				
				System.out.println("Size of R matix is "+number_of_links+" x "+number_of_active_jobs);
				
				string_in = string_in+ " n = "+number_of_links+"; C = ";
				string_in = string_in+"[ "+C[0];
				for(int ind = 1;ind<number_of_links;ind++){
					string_in = string_in+", "+C[ind];
				}
				string_in = string_in+"];";
				string_in = string_in+" t = "+number_of_active_jobs+";";
				string_in = string_in+"R = [| ";

				for(int i = 0; i<number_of_links; i++){
					for(int j = 0; j<number_of_active_jobs-1; j++){
						string_in = string_in+" "+R[i][j]+", ";
				//        System.out.print(R[i][j]+" ");
				 	}
				    string_in = string_in+ R[i][number_of_active_jobs-1]+"|";
				}
				string_in = string_in+"];\"";
			}
		}
		return flag; 
	}

	public static double roundAvoid(double value, int places) {
	    double scale = Math.pow(10, places);
	    return Math.round(value * scale) / scale;
	}
}