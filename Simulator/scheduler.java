import java.util.*;
public class scheduler{
	set active_job_set = new set(); // storesthe jobs which are in the system 
	set finished_job_set = new set(); // stores the jobs which are already completed

}
class job_class{
	int class_id;
	set alloted_job;
	job_class(int i){
		this.class_id = i;
		alloted_job = new set();
	}
	public void add_job(job j){
		this.alloted_job.Insert(j);
	}
	public void remove_job(job j){
		this.alloted_job.Delete(j);
	}

}
class set{
	public  linkedlist list;
	set(Object o){ 
		 list = new linkedlist(o);
	}					
	set(){ 
		 list = new linkedlist();
	}

	public Boolean IsEmpty(){
		if(this.list.IsEmpty()){
			return true;
		}
		else {return false;}
	}
	public Boolean IsMember(Object o){
		if(this.list.IsMember(o)){
			return true;
		}
		else{return false;}
	}
	public void Insert(Object o){
		if(!this.list.IsMember(o)){
			this.list.Insert(o);
		}
	}
	public void Delete(Object o){
		this.list.Delete(o);
	}
	public int NumberOfMembers(){
		return this.list.NumberOfMembers();
	}
	public job_class findClassById(int x){
		return this.list.findClassById(x);
	}
}

class linkedlist{
	public  node head;
	linkedlist(Object a){
		head = new node(a);
	}
	linkedlist(){
		head = null;
	}
	class node{
		Object data;
		node next;
		node(){
			next = null;
		}
		node(Object a){
			data = a;
			next = null;
		}
	}
	
	node temp;

	public Boolean IsMember(Object o){
		temp = head;
		while(temp != null){
			if(temp.data == o){return true;}
			temp = temp.next;			 
		}
		return false;
	}

	public Boolean IsEmpty(){
		if(head==null){
			return true;
		}
		else return false;
	}
	public void Insert(Object o){
		if(head == null){
			head = new node(o);
		}
		else{
			temp = head;
			while(temp.next!=null){
				temp = temp.next;
			}
			temp.next = new node(o);
		}
	}
	public void Delete(Object o){
		if(this.IsMember(o)){
			if(head.data == o){
				this.head = head.next;
			}
			else{
				temp = head;
				
				while(temp.next.data != o){
					temp = temp.next;
				}
				temp.next= temp.next.next;
			}
		}
	}
	public int NumberOfMembers(){
		temp = head;
		int count =0;
		while(temp!=null){
			count++;
			temp=temp.next;
		}
		return count;
	}	
	public job_class findClassById(int x){
		temp = head;
		while(temp!=null){
			if(((job_class)temp.data).class_id == x)return (job_class)temp.data;
			temp = temp.next;
		}
		return null;
	}
}