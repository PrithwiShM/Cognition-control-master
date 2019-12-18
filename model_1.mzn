int: n; % no of links in chained network
int: f; %frequency of solving
float: tval = 1/f;
array[1..n] of float: C; %capacities
array[int] of float: arrive; %arrivaltime, this has indexing from 1

int: lastpoint; % all jobs end in this interval number
set of int: time = 0..lastpoint; %time is discretised e
set of int: classes = 0..n;

array[int] of int: act; %aggrement array whether a job is active, indexing from 1
array[int] of float: size; %sizes of current jobs, this too has indexing from 1

%setting the value of lastpoint
float: totaltime = max(i in 1..n)(size[i+1]/C[i]) + size[1]/(min(C));
lastpoint = f*ceil(totaltime);
%embedding the arrival times in discrete number line
array[int] of int: arremb = [ceil(arrive[i]*f) | i in 1..n+1];

%complition time in embedded timeline
array[classes] of var int: e;
%all rates in embedded timeline
array[classes, time] of var float: x;

%now solving only 1 time

constraint forall(j in classes, t in time) (if act[j+1] == 0 then x[j,t] = 0 endif);

constraint forall(j in 1..n, t in time) (x[0,t] + x[j,t] <= C[j]);
constraint forall(j in classes, t in time) (x[j,t] >= 0);

%relation between complition time and rates
%constraint forall(j in classes) (e[j] = max(t in time where x[j,t] > 0)(t));

constraint forall(i in classes where act[i+1] > 0)
                  ((sum(t in arremb[i+1]..e[i])(x[i,t]))*tval >= size[i+1]);
                  
var float: util = sum(i in classes where act[i+1] > 0) (size[i+1]*f/(e[i] - arrive[i+1]));
solve maximize util;