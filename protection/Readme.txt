Protection-simulation simulates the Market for Protection (Konrad and Skaperdas, 2010:  http://link.springer.com/content/pdf/10.1007/s00199-010-0570-x.pdf)

Overview:  http://www.openabm.org/model/3851/version/1/view

v1.1:  fixes, new parameter for role-shifting dynamic
* Role-shifting dynamic adds new peasant with either random protection proportion or using best-performing proportion from existing population (default)
  (ProtectionParameters.NEW_PEASANT_GETS_BEST_PROTECTION_PROPORTION defaults to true)
* Fix:  dynamics were applied after peasant population state was reset, so surrendered payoffs did not affect payoff calculations.    

v1.0:  bandits and peasants in the anarchy condition
* The protection function is given the specific functional form of a contest function.
* Peasants randomly choose a protection proportion.  
* Agents are randomly matched for interaction.  
* Dynamic: each agent lives one period; if its payoff is below a *survive* threshold, it dies, otherwise it has a single descendant. If its payoff is higher, above a *thrive* threshold, it has two descendants.  
* Peasants inherit the protection proportion of the parent.  
* Equilibrium:  average payoffs to bandits and peasants are equal, within a tolerance.
* Optionally, bandits may prey upon multiple peasants; peasants will therefore be preyed upon by multiple bandits
* Support for an evolutionary game theory comparison to the simulation using exactly three strategies:  "bandit", "high" (peasant with high protection), "low" (peasant with low protection) 

This is a bare-bones initial version.  Future versions will include:
* Local interaction of agents on a spatial grid, through Repast or Mason
* Replication and extension of the market for protection, to address collective organization of both predation and security.
* Free exchange of goods among agents
* Implementation of more cognitively-plausible agents

Documentation in this initial version is through the unit tests under src/test/java
Future versions will include higher-level tests, documented and executed through fitnesse:  http://fitnesse.org/  (see the three supporting projects that make this possible, below)

This simulation is currently stand-alone, but is intended to be part of an interacting set of economic and cognitive models. 

As this implies ongoing development, this project is organized as an open-source Java project on github:
    https://github.com/sjdayday/protection-simulation.git
This and related projects will be periodically re-uploaded to openabm.org, but will be maintained through github; the most current status will always be there.

The project is organized for Maven deployment, but is not yet available through a Maven repository; contact me if you would like the 1.1 JAR 
and are having difficulty generating it through your own Maven configuration.    

This project is stand-alone, but generating results for different combinations of parameters is most easily done with the help of three other projects: 
  simulation-scenario:  Framework for running and replicating simulations
    https://github.com/sjdayday/simulation-scenario.git
  simulation-scenario-fit:  Packaging of fitnesse and fitlibrary acceptance testing frameworks, and simulation-related test fixtures.
    https://github.com/sjdayday/simulation-scenario-fit.git   
  protection-scenario-fit:  Uses the two project above to enable fitlibrary acceptance tests and production simulations to be run against this project (the Market for Protection model)
    https://github.com/sjdayday/protection-simulation-fit.git
 
Next steps and known bugs are in ToDo.txt

This is not a one-person project -- help is welcome!

Steve Doubleday
UC Irvine
twitter: @sjdayday
stevedoubleday [at] gmail [dot] com 