init(MAXN) :- precalc(2, MAXN).

sieve(D, N, MAXN) :-
    N =< MAXN,
    assert(composite(N)),
    N1 is N + D,
    sieve(D, N1, MAXN).

precalc(N, MAXN) :- N > MAXN, !.
precalc(N, MAXN) :-
    not(composite(N)),
    assert(prime(N)),
    S is N * N,
    (N > 2 ->
        N1 is 2 * N
    ;  N1 is N),
    sieve(N1, S, MAXN).

precalc(N, MAXN) :-
    N1 is N + 1,
    precalc(N1, MAXN).

palindrome(N, K) :- palindrome(N, [], K).
palindrome(0, R, K) :- !, reverse(R, R).
palindrome(N, T, K) :-
    H is mod(N, K),
    N1 is div(N, K),
    palindrome(N1, [H | T], K).

prime_palindrome(N, K) :- prime(N), palindrome(N, K).

nth_prime(N, P) :- nth_prime(2, N, P).
nth_prime(P, 1, P) :- !.
nth_prime(P, N, Pp) :-
    N > 1,
    N1 is N - 1,
    next_prime(P, P1),
    nth_prime(P1, N1, Pp).

next_prime(P, P1) :- P1 is P + 1, prime(P1), !.
next_prime(P, R) :- P1 is P + 1, next_prime(P1, R).

find_divisors(N, D, [N], MAX) :- D * D > MAX, !, N > 1.
find_divisors(N, D, [], MAX) :- D * D > MAX, !.

find_divisors(N, D, [D | T], MAX) :-
    0 is mod(N, D),
    N1 is div(N, D),
    find_divisors(N1, D, T, MAX), !.

find_divisors(N, D, R, MAX) :-
    next_prime(D, D1),
    find_divisors(N, D1, R, MAX).

product(1, []) :- !.
product(N, [N]) :- prime(N).
product(N, [A, B | T]) :-
    A =< B, prime(A),
    product(N1, [B | T]),
    N is N1 * A.

prime_divisors(1, []) :- !.
prime_divisors(N, Divisors) :- number(N), !, find_divisors(N, 2, Divisors, N).
prime_divisors(N, Divisors) :- product(N, Divisors).
