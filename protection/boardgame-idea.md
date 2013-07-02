# peasants vs. bandits game

* 10 x 10 grid

* required rules
	* when do we add more peasants (breeding)
	* when do we add more bandits (breeding)
	* when do we remove peasants (dying)
	* when do we remove bandits (dying)
	* can a peasant change to a bandit?
	* can a bandit change to a peasant?

* peasants require 15 units of food to survive per turn
	* peasants without 15 units of food die
* each square can produce 100 units of food per turn
	* peasants can apply up to 100 units of effort, split between farming and defending
	* defending reduces the % take from the bandits (85% defending means bandits only get 15% of their target steal)
* peasants breed when they have more than 50 units of food in one turn
	* new peasant takes up residence in a random adjacent empty square if available
	* if no adjacent square available, current square can only produce 50 units of food until the "extra" peasant has room to move
	* every turn the "extra" peasant has a 25% chance of invading an occupied square
		* invading a bandit square kills both the "extra" peasant and the bandit
		* invading a peasant square kills the peasant, and the "extra" peasant becomes a bandit

* bandits require 15 units of food to survive per turn
* bandits can steal 100 units of food per turn (assuming peasant has zero defense, and bandit decides to steal 100 units)
* bandits can only steal from adjacent peasants (vertical and horizontal)
* bandits can move one square per turn (vertical or horizontal), in addition to stealing
* bandits breed when they have more than 50 units of food in one turn
	* new bandit takes up residence in a random adjacent empty square if available
	* if no adjacent square is available, bandits may attempt an invasion
		* invading a peasant square kills the peasant, who is replaced by the bandit
		* invading a bandit square kills both bandits (civil war)
		* deciding not to move kills both bandits (civil war)
* bandits who cannot find a victim in range convert into peasants

# order of operations

1 - all peasants decide on how much to farm, and how much to defend
2 - all bandits decide on where to move, and how much to steal
3 - stealing is completed
4 - eating is completed, with starving units dying
5 - breeding is completed
6 - expansion/invasion is completed
