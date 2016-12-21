# CS999Independent_Study

Two main problems that are implemented as part of the independent study are:

A graph service with all the efficient graph algorithms involving client and server programming and a much more complicated problem of implementing acquire board game with client and server conforming to all its game rules. 

Used strategy pattern to implement different strategies to the player as Ordered Strategy, Random Strategy, Min-Max Strategy building a game tree. The results of the game are analyzed by playing the game for about 50 times on various strategies as random Strategy, analyzing the game tree up to two levels and analyzing the game tree till the end to place a tile and checking which strategy wins for maximum number of times.

Implementation Details

Graph Project
--------------
Problem Statement:

Design a graph library. The library must manage directed, planar graphs. The nodes are labeled. The edges of the graph are labeled with a traversal cost. They also obey the usual triangle inequality. That is, in a triangle such as the triangle inequality x + y ≥ z where x, y, and z are the costs associated with traversing these edges. The costs are real numbers may not exceed some specified cost interval. At a minimum, your library should be able to add edges to a graph, join two graphs that operate on the same cost interval and disjoint sets of nodes, and compute whether there is a path from one node to another and, if so, its cost.

Design a client and a server web service to the graph library. Design an XML parser with schema xsd for the graph service.
  
Design and implementation:

To implement the OO Concepts discussed in the introduction I have used Java programming language and coded on Eclipse IDE. I have designed interfaces first to reflect the skeleton of the implementation. Later, written the classes for all the interfaces with full implementation. Client and server XML parsers are written as StAX parsers and used ‘http://www.w3.org/2001/XMLSchema’ to validate xsd schema.

The input tag for the parser from the client looks as follows:
&lt;graphProject>&lt;graph name=\"graph1\" costInterval=\"45\">" + "&lt;edge from=\"A\" to=\"B\" cost=\"6\">&lt;/edge>&lt;edge from=\"A\" to=\"D\" cost=\"3\"/>" + "&lt;edge from=\"D\" to=\"A\" cost=\"2\"/>&lt;edge from=\"D\" to=\"B\" cost=\"1\"/>" + "&lt;edge from=\"D\" to=\"C\" cost=\"2\"/>&lt;edge from=\"A\" to=\"C\" cost=\"9\"/>" + "&lt;edge from=\"C\" to=\"E\" cost=\"8\"/>&lt;edge from=\"E\" to=\"A\" cost=\"7\"/>" + "&lt;/graph>&lt;path graph=\"graph1\" from=\"C\" to=\"A\" />&lt;/graphProject>

Output is given as:
&lt;path cost=15.0>&lt;edge from=C to=E cost=8.00 /> &lt;edge from=E to=A cost=7.00 /> &lt;/path>

Acquire Board Game
-------------------
Problem Statement:

Design an Acquire board game web service conforming to all the rules of the acquire game and object oriented concepts. Write the unit tests to test the functionalities. Write strategies to various players and test the performance of each of the player by running the game for about 50 times and seeing the pattern of game win.

Design and implementation:

To implement acquire game I have selected Java programming language and code on eclipse IDE. I have used strategy pattern to design the strategies. Client and server XML parsers are written as StAX parsers and used ‘http://www.w3.org/2001/XMLSchema’ to validate xsd schema. 

The challenging part of the acquire board game implementation is conforming to its huge list of game rules and designing the flow following all these rules. The overall rules and design flow for acquire game is as follows:

Before starting the game:

The following entities should be present -
1. Acquire board - 12*9 size with rows 1 - 12 and columns A - I each cell named as 1A, 10I etc.
2. Tiles matching the names on the cells of the board.
3. Seven colors to represent 7 different hotels.
4. Money to buy the stocks for the hotels
5. 1-6 players to play the game.

At the start of the game:

The board should be empty (all the cells unmarked).
Each player should have the following - 
1. 6 Tiles (hidden from other players)
2. $6000(money to buy hotel shares)
3. A variable to detect his turn (Boolean variable which could be false initially)
   Choose the order in which players play the game - It could be based on some strategy as in players ranked in        	the order of closeness to 1A when placing the 1st tile or in a pre-decided order.

Game Flow:

When the game starts each player places on a tile on matching cell on the board and the game starts.
At each turn player, would have three actions that can be performed - 
 1. Purchase a stock
 2. Throw a dead tile 
 3. End the turn

During the play:

When a player places a tile - 
 
•	If the tile is adjacent (horizontal or vertical) to a single marked cell, then he can pick a hotel from the existing 7 hotels. If the seven hotels already exist on the board then adjacent tile move is Invalid.
•	If the placed tile is adjacent to the existing hotel, then it adds to that hotel.
•	If the tile joins two hotels, then there would be a merger. The strongest of the two hotels would be active and the other hotel gets into the available hotels pool. Here if there is a tie the current active player can make a choice as to which hotel should be active. 

While merging the hotel all the stock holders of the dissolving hotel should be given the following   options –
	1) To trade the stock
	2) To sell the stock
	3) To keep the stock
    	They can choose one, two or all the options.
	•	A hotel can't be merged if it’s a safe corporation - a corporation with 11 or more tiles.

During the merger:

•	The players with the most and second most stockholders are the majority and the minority stockholders who get 	majority and minority stock bonuses as below -
	a)	Maintain a majority and minority bonus amounts chart.
	b)	If only 1 person owns the shares in the defunct corporation that person gets both the bonuses.
	c)	If there is a tie for majority stockholder, add both bonuses and divide evenly (minority stockholder gets 	no bonus).
	If there is a tie for minority stockholder, split the minority bonus among the tied members.
•	Stockholders in the surviving corporation get no bonus but their stock price grows higher as the hotel grows.
 
Multiple mergers:

•	It is possible that one tile merges more than two hotels. The larger corporation survives.
•	Any ties would be broken by merge maker.
•	The bonuses are catered to stockholders for each defunct corporation - larger to smaller.
  
Buying Stocks:

•	After placing the tile the player may buy the stock in any active corporation (25 stocks max for each corp.).
•	He can buy up to 3 stocks in one corp. or 2 in one corporation and 1 in the other or all three in different 	corporations or less than 3 in any combinations or not buy at all.
•	Stock price depends on name of the corporation and number of tiles (maintain a chart with the rates).

Drawing a tile:

•	Each time a player plays a tile, he draws a tile from the remaining tiles.
•	In each turn, the player can discard any dead tiles (ones that merge two safe corporations).
•	Tiles that form 8th corporation can't be traded in.
	The drawn tiles are placed on the board by the players during their turn based on the strategy they follow.

Ending the game:

The game ends in any of the following situations. 
•	One player during his turn claims all active corporations are safe or that 1 corporation has 41 tiles.
•	A player doesn't have to end the game if he feels there is advantage in continuing playing.
•	When all the tiles are done the game ends automatically or any of the player has all invalid tiles then the game ends.

Announcing the winner:
	Majority and minority shareholders' bonuses are given out and all the stocks in active corporations are sold to the stock bank. Player with the maximum money wins.

