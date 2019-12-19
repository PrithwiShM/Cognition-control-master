int: n; % no of links in network
array[1..n] of float: C; %capacities

int: t; % no. of active jobs
set of int: jobset = 1..t;
array[1..n, jobset] of 0..1: R; % R matrix
array[jobset] of int: class; % which job belongs to which class

%complition time in embedded timeline
%array[jobset] of var int: e;
%all rates for present time  
array[jobset] of var float: x;

constraint forall(i in 1..n) (sum(j in jobset)(R[i,j]*x[j]) <= C[i]);
constraint forall(j in jobset) (x[j] >= 0);

var float: util= sum(j in jobset)(ln(x[j]));

solve maximize util;