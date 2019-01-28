# MutRex: a mutation-based generator of fault detecting strings for regular expressions

![mutrex logo](https://raw.githubusercontent.com/fmselab/mutrex/master/mutrexweb/WebContent/icon/mutrex_mid.png)

Web interface: [http://foselab.unibg.it/mutrex/](http://foselab.unibg.it/mutrex/)

There is also a simple CLI version. You can download the jar from here <https://github.com/fmselab/mutrex/raw/master/mutrex.cli/mutrex.jar>
```
$ java -jar  mutrex.jar "aB([a-z]*)"
tests for regular expression aB([a-z]*)
"aBb" (CONF) kills [aB((\a|\z))*]
"AB" (REJECT) kills [AB([\a-\z])*, (\a|\A)\B([\a-\z])*]
"ab" (REJECT) kills [\a(\B|\b)([\a-\z])*]
...
```
## License

Mutrex is released under the under the terms of the BSD license. 
Copyright (C) Paolo Arcaini, Angelo Gargantini, Elvinia Riccobene

## Build

You can build mutrex by using eclipse. Import the projects in eclipse.

## Cite as:
[P. Arcaini](http://group-mmm.org/~arcaini/), [A. Gargantini](http://cs.unibg.it/gargantini/), [E. Riccobene](https://homes.di.unimi.it/riccobene/)
*Fault‐based test generation for regular expressions by mutation*
in Software Testing, Verification and Reliability, March 2018
[download the pdf](https://cs.unibg.it/gargantini/research/papers/mutrexSIstvr2017.pdf)

Other publication:
[P. Arcaini](http://group-mmm.org/~arcaini/), [A. Gargantini](http://cs.unibg.it/gargantini/), [E. Riccobene](https://homes.di.unimi.it/riccobene/)
*MutRex: a mutation-based generator of fault detecting strings for regular expressions*
in 12th International Workshop on Mutation Analysis (Mutation 2017), Tokyo, Japan, March 13, 2017
[download the pdf](http://cs.unibg.it/gargantini/research/papers/mutrex_mutation17.pdf)

Mutrex is used also for regular expression repairs. See [here](https://foselab.unibg.it/tearrex/) and the [paper](https://cs.unibg.it/gargantini/research/papers/regexRepair_ICTSS2018.pdf)
