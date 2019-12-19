import java.util.*;
public class job_simulator{
	public static generator g1 = new generator();
	public static scheduler s1 = new scheduler();
	public static double current_time = System.currentTimeMillis();//****************tbd****************
	
	static int number_of_links = 4; //*************************
	static int number_of_active_jobs = 0;
		
	public static void main(String[] args) {
		Vector temp_vec = new Vector();
		int number_of_classes = 5; //**********************tbd***************************************************
		g1.initialize_classes(number_of_classes);
		g1.calculate(current_time, number_of_classes);
		double time_gap = 8000.0;
		double limit = current_time+time_gap;
		int counter =0;
		int out =0;
		double rr;
		int [][] R = new int[1][1]; 

		while(current_time<limit){
			current_time = System.currentTimeMillis();
			
			number_of_active_jobs = s1.active_job_set.NumberOfMembers(); //size of rate vector needed
			
			temp_vec.setSize(number_of_active_jobs);          
			for(int k=0;k<number_of_active_jobs;k++){
				rr = 0.01*g1.r_generator.nextDouble();
				temp_vec.setElementAt(rr,k);
			}	
			
			out = updater(temp_vec, current_time);
			if(out>0){
				counter++;
				number_of_active_jobs = s1.active_job_set.NumberOfMembers();
				System.out.println(counter+". Time since start is "+(current_time-limit+time_gap)+" milli seconds Total Active Jobs are "+number_of_active_jobs);
				
				for(int i = 0; i<number_of_links; i++){
				    for(int j = 0; j<number_of_active_jobs; j++){
				        System.out.print(R[i][j]+" ");
				    }
				    System.out.println();
				}
				
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
		System.out.println("No. of Jobs Finished is "+s1.finished_job_set.NumberOfMembers()+" No. of Remaining Jobs is "+(g1.job_set.NumberOfMembers()+s1.active_job_set.NumberOfMembers())+" Jobs in the system right now "+s1.active_job_set.NumberOfMembers());
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
				temp_job.update_job(new_rem_size,(new_rem_size>0),curr_rates.get(count),current_time); //updated the job
				if(new_rem_size<=0){
					temp_active_set.Delete(temp_job);
					temp_finished_set.Insert(temp_job);
					temp_job.class_alloted.alloted_job.Delete(temp_job);
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

		int [][] R = new int[number_of_links][number_of_active_jobs];
		for(int i=0;i<number_of_active_jobs;i++){
			temp_node = temp_active_set.list.head;
			while(temp_node!=null){
				temp_job = (job)temp_node.data;
				switch(temp_job.class_alloted.class_id){
					case 0: 
						for(int j=0;j<number_of_links;j++)
							R[j][i]=1;
						break;
					case 1:
						R[0][i]=1;
						break;
					case 2:
						R[1][i]=1;
						break;
					case 3:
						R[2][i]=1;
						break;
					case 4:
						R[3][i]=1;
						break;
				}
				temp_node = temp_node.next;
			}
		}
        System.out.println();
		
		return flag; 
	}
}