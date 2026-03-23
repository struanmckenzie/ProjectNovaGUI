# Project Nova
NOTE: Please run in a terminal with a monospace font for the full experience

## Summary of the game
The project is a battleships-like game where two players guess coordinates of sea creatures in order to gain points and win. There are special tiles which each do different things and affect the player in different ways. The winner is the player who can find all the creatures first without dying or, if the game ends early, has the most points. This report has a summary of the features I implemented, if relevant how I implemented them, and a critical self-evaluation. Additionally it includes a link to my GitHub repo in case you want to view the development history of it.

## Description of features
All the basic features were implemented easily: the menu has the options start game, load game, help, and quit, there is a hidden board and a board shown to the player, the creatures and special objects are placed on each players’ hidden board, guesses are input as two letters to specify a coordinate, a pause menu with options of save, quit and continue is offered after guessing a coordinate, the game continues to the next player if the other chooses to continue after the pause menu, the game ends either when one player chooses to quit in the pause menu (winner has the most points) or when one player finds all of the creatures (first one wins), either way a summary of the players stats and who won are shown.

Extended features I implemented are a bomb, MegaGuess, and health tile, complex creature shape, hint option, random placement of objects, showing the player what tiles they have already guessed, prevention of guessing the same tile more than once.


On selecting start new game an array of two Player objects is created. The game asks for the name of the Explorer and Hunter, their points start at 0 and health at 100. The game places the creatures at random on each players' board and gives the Explorer the first turn.
When load game is selected the game asks for the name of a previous save, if it exists then the game loads the data from the save files into Player objects and starts the game where it left off. If it does not exist the player is told so then returns to the main menu.

I didn’t feel confident enough to implement a GUI so I took the time to design a text UI that looks like an old computer program with borders and coloured text in order to make the game look more interesting.

The boards are stored as 11x11 2d arrays. The regular board starts off blank and shows the player the tiles that they’ve already guessed and the hidden board stores the game objects and isn’t (usually,  more on this in the hint section) show to the player.

The game objects are placed randomly on each player’s hidden board at the start of a new game, taking into consideration the ratio between the size of the board and how many of each object should be placed (this is hard coded, if I had more time I’d implemet it so the player can specify the size of the board as command line arguments when launching the game which would change the number of objects dynamically) or read from files when loading a game.
Boards are displayed with a blue background with letters along the edges and + characters on every undiscovered tile so the player can easily find coordinates.

Coordinates for guesses are input as letters in the form xy, x being the letter on the x-axis and y being the letter on the y-axis. Guesses are checked to make sure they are valid: not the same coordinate as before I order to prevent cheating by racking up points from an already discovered creature, not out of bounds of the grid, and specifically letters rather than numbers. The game also checks if a whole creature was found and displays the appropriate message. After each guess the console is cleared and the new board, health, and points values are shown with an appropriate message depending on what the player discovered.

The game checks to see if any win/lose/hint conditions are met before and after each guess, if any condition is met the players are presented with the appropriate message: a summary of who won/lost and each players’ stats if it was win/lose, or presented with the hint option (more on this in the hint section) next round. 

A pause menu is offered after a valid guess which when entered has the options to save, quit or continue. I decided to add this as a convenient place to store these options so the player can choose to enter it rather than forcing the options on them every time they make a guess.

The booby trap I went for was a bomb it removes 25HP when its tile is discovered.

In order to balance the bombs I added medicinal seaweed as a power up which adds 15HP to the total health irrelevant to how much health the player already has.

The way I implemented the MegaGuess was to make it reveal a whole row rather than a block of tiles as if the player had guessed that row themself meaning they receive points for creatures revealed, health for seaweed, and damage for any bombs found. The usual messages for finding the objects are not displayed to the user as this would clutter the screen but the new board layout and values of points and health are still shown after.

As for the hint feature, rather than having it as an option all the time, I implemented it so that when the player’s health is 25HP or lower on their turn they are presented with the option to buy a hint in exchange for 10 points. If they choose to accept the offer everything on the hidden board is revealed for 10 seconds. If the player has less than 10 points left they are presented with a different screen telling them they are poor and cannot buy the hint.

## Self Critique
To criticise my program some of the code is very inefficient because I’m not used to object oriented programming or Java. I particularly dislike the way I implemented the check for if a whole creature has been found, it’s far to complicated and unreadable, if I had more time I would come up with a more generic way of doing this that didn’t take many lines of code.

Figuring out how to implement the MegaGuess challanged me, it took me a while to figure what the program should do when it was found and how I would get the program’s guess function to act differently without creating a completely new function just for the MegaGuess. The hint function was also tricky to implement for a few reasons. I had to decide when the player would be offered the hint because I didn’t want to have it in the pause menu as that is offered after their guess and I didn’t want to have the pause menu come up before their guess as this would get annoying after a while so I decided to have it come up when the players health hit a certain level before a guess.

One of the things I’m proud of is the UI, I spent some time on it to make it look like an old DOS or Unix program with the colours and style (which is why it works best in a terminal). I am pleased I learned how to clear lines from/the whole terminal, it makes the whole experience so much better. I think the only two bits of code I’m particularly pleased with is during the placement of objects when the layout for the board is set to a temporary board at the same time as validating if it will actually fit before copying that to the layout once a valid position is found and creating all the boards at the same time in the Player constructor.
