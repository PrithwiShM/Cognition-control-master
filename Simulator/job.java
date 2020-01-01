
import java.util.*;
public class job{
	public int job_id;
	public double arv_time; //activation time for a job to be given by generator
	public double size; // size of the job
	public double rem_size; // residual size of job to be calculated on each iteration
	public boolean is_active; // if arv_time < current time and rem_size>0 then true; tells whether the job is in the system or not
	public job_class class_alloted; // class to which the job belongs
	public double rate; // rate calculated by solving the optimization
	public double last_updated; // stores the time when the job was last updated 
	job(int x){
		job_id = x;
		arv_time = 0.0;
		size = 0.0;
		rem_size = 0.0;
		class_alloted = null;
		is_active=false;
		rate = 0;
		last_updated = 0; 
	}
	public void set_job(double t, double s, job_class c,double c_t){  //used for initialization of the job
		this.arv_time = t;
		this.size = s;
		this.rem_size = s;
		this.class_alloted = c;
		this.last_updated = c_t;

	}
	public void update_job(double r_size, boolean status, double r,double t){ //updater, used after each iteration
		this.rem_size = r_size;
		this.is_active = status;
		this.rate = r;
		this.last_updated = t;
	}
	public int get_id(){
		return this.job_id;
	}
	public void print_status(){
		//System.out.println("Job Id: "+this.job_id+" Arrival time: "+arv_time+" Size: "+size+" Remainig Size: "+rem_size+" class_alloted: "+class_alloted.class_id+" Active: "+is_active+" Rate: "+rate+" Last Update Time: "+last_updated);
		System.out.println("Job Id: "+this.job_id+" Remainig Size: "+rem_size+" class_alloted: "+class_alloted.class_id+" Rate: "+rate);
	}
}