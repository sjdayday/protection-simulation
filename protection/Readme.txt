Protection-simulation simulates the Market for Protection (Konrad and Skaperdas, 2010:  http://link.springer.com/content/pdf/10.1007/s00199-010-0570-x.pdf)

v1.0:  bandits and peasants in the anarchy condition
* The protection function is given the specific functional form of a contest function.
* Peasants randomly choose a protection proportion.  
* Agents are randomly matched for interaction.  
* Dynamic: each agent lives one period; if its payoff is below a *survive* threshold, it dies, otherwise it has a single descendant. If its payoff is higher, above a *thrive* threshold, it has two descendants.  
* Peasants inherit the protection proportion of the parent.  
* Equilibrium:  average payoffs to bandits and peasants are equal, within a tolerance.
* Optionally, bandits may prey upon multiple peasants; peasants will then be preyed upon by multiple bandits

This is a bare-bones initial version.  Future versions will include:
* Local interaction of agents on a spatial grid, through Repast or Mason
* Replication and extension of the market for protection, to address collective organization of both predation and security.
* Free exchange of goods among agents
* Implementation of more cognitively-plausible agents

Documentation in this initial version is through the unit tests under src/test/java
Future versions will include higher-level tests, documented and executed through fitnesse:  http://fitnesse.org/

This simulation is currently stand-alone, but is intended to be part of an interacting set of economic and cognitive models. 

As this implies ongoing development, this project is organized as an open-source Java project on github:
    https://github.com/sjdayday/protection-simulation.git
This and related projects will be periodically re-uploaded to openabm.org, but will be maintained through github; the most current status will always be there.

The project is organized for Maven deployment, but is not yet available through a Maven repository; contact me if you would like the 1.0 JAR. 

To manage the complexity of multiple versions of multiple projects, a simple replication framework is also under development.  The core support of the initial version is available here:
    https://github.com/sjdayday/simulation-scenario.git
See the readme for information on other supporting projects. 

This is not a one-person project -- help is welcome!

Steve Doubleday
UC Irvine
twitter: sjdayday
stevedoubleday [at] gmail [dot] com 