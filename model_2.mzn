int: n; % no of links in network
array[1..n] of float: C; %capacities

int: t; % no. of active jobs
set of int: jobset = 1..t;
array[jobset] of float: size; % sizes of jobs
array[jobset] of int: arrival; % arrival times
array[1..n, jobset] of 0..1: R; % R matrix
array[jobset] of int: class; % which job belongs to which class

float: mincap = min(C);
float: lspt1= sum(j in jobset where class[j] > 0) (size[j]/C[class[j]]);
float: lspt= lspt1 + sum(j in jobset where class[j] == 0) (size[j]/mincap);
set of int: time = 0..ceil(lspt);
output ["the discrete timeline: \(time)\n"];

%complition time in embedded timeline
array[jobset] of var float: e;
%all rates for discrete time line 
array[jobset, time] of var float: x;

constraint forall(i in 1..n, t in time) (sum(j in jobset)(R[i,j]*x[j,t]) <= C[i]);
constraint forall(j in jobset, t in time) (x[j,t] >= 0);

%constraint forall(j in jobset)(e[j] <= 100);
constraint forall (j in jobset)
          (sum(t in arrival[j]..3)(x[j,t]) >= size[j] /\ e[j] >= 1);

var float: util = sum(j in jobset)(size[j]/(e[j] - arrival[j]));
solve maximize util;

output["complition times = \(e)\n rates:-\n"];
output [ show_float(6, 2, x[i,j]) ++
if j == ceil(lspt) then "\n" else " " endif |i in jobset, j in time];
