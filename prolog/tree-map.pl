max_int(2147483647).

node(Key, Value, Priority, Left, Right).

% merge(Result, First, Second)
merge(T, null, T) :- !.
merge(T, T, null) :- !.
merge(node(K2, V2, P2, First, R2), node(K1, V1, P1, L1, R1), node(K2, V2, P2, L2, R2)) :-
    P2 < P1, !, merge(First, node(K1, V1, P1, L1, R1), L2).
merge(node(K1, V1, P1, L1, Second), node(K1, V1, P1, L1, R1), node(K2, V2, P2, L2, R2)) :-
    P2 >= P1, !, merge(Second, R1, node(K2, V2, P2, L2, R2)).

% split(Node, Key, First, Second)
split(null, K, null, null) :- !.
split(node(K, V, P, L, R), X, First, node(K, V, P, L1, R)) :-
    X < K, !, split(L, X, First, L1).
split(node(K, V, P, L, R), X, node(K, V, P, L, R1), Second) :-
    X >= K, !, split(R, X, R1, Second).

% map_remove(Node, Key, Result)
map_remove(null, X, null) :- !.
map_remove(node(K, V, P, L, R), K, Result) :-
    merge(Result, L, R), !.
map_remove(node(K, V, P, L, R), X, node(K, V, P, L1, R)) :-
    X < K, !, map_remove(L, X, L1).
map_remove(node(K, V, P, L, R), X, node(K, V, P, L, R1)) :-
    X > K, !, map_remove(R, X, R1).

% TODO [Надо переписать на спуск]
% map_put(Node, Key, Value, Result)
map_put(Node, K, V, Result) :-
    map_remove(Node, K, Node1),
    split(Node1, K, L, R),
    max_int(Seed), rand_int(Seed, P),
    merge(Tmp, node(K, V, P, null, null), R),
    merge(Result, L, Tmp).

% map_get(Node, Key, Value)
map_get(node(K, V, P, L, R), K, V) :- !.
map_get(node(K, V, P, L, R), X, Val) :-
    X < K, !, map_get(L, X, Val).
map_get(node(K, V, P, L, R), X, Val) :-
    X > K, !, map_get(R, X, Val).

% map_minKey(Node, Key)
map_minKey(node(K, V, P, null, _), K) :- !.
map_minKey(node(K, V, P, L, _), Key) :-
    map_minKey(L, Key).

% map_maxKey(Node, Key)
map_maxKey(node(K, V, P, _, null), K) :- !.
map_maxKey(node(K, V, P, _, R), Key) :-
    map_maxKey(R, Key).

% map_build(ListMap, TreeMap)
map_build([], null) :- !.
map_build([(K, V) | T], Result) :-
    map_build(T, Result1),
    map_put(Result1, K, V, Result).
