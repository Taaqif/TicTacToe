Model-View-Controller
---------------------

For this application, the codebase was already defined from the
downloaded Tic-Tac-Toe project. Adjustments were made such that the
project would be easily scalable and allowed for some extended features
to be implemented as required by the project requirements. These
requirements were

> \(1) The state of the application is preserved when you rotate the device
> (switch between Portrait and Landscape orientations).\
> (2) The size of the grid changes from 3x3 to 5x5 when you switch from
> portrait to landscape and back to 3x3 when you switch back to portrait.
> Importantly:\
> a. When you switch from portrait to landscape, you should preserve the
> status of the game.\
> b. When you switch from landscape to portrait, the game starts over.

The extended model-view-controller will be discussed in the following
sections.

### Model 

Generally, the model was the Tic Tac Toe Grid class. The idea behind it
was to extend from a matrix class. By extending the matrix class, the
tic tac toe can essentially be of any size. Irrespective of what
function is using the class, it will always work the same even if this
class were to be used in a console application.

The logic behind the class is based on a winning move of n consecutive
items in a row. To check the winner, the idea was to check the rows, the
columns and the diagonals for a win **each** time a user sets a cell
value. If a win does occur, it returns true. It is up to the controller
to stop and reset the game from there. Checking the rows, columns and
diagonals all utilise generic functions such that depending on the size
of the grid, be it 3x3 or 5x5 the algorithm will check for a win
regardless. This gives a layer of extensibility as the grid can be of
any size

All functions are not dependant on any other external modules (except
Matrix.java) making this model very portable. For this application, the
model is the Matrix.java and TicTacToe.java

### View

The view consists of a few elements which allow for a dynamic experience
for the user. There are three main components involved, the grid view,
the current player view and the reset button. Firstly, the entire layout
is a linear layout meaning each component will be positioned one under
each other.

The most important component involved is the grid layout which is
entirely blank, it acts as a placeholder for the controller to then
manipulate to insert buttons depending on the orientation of the device.
This is where the buttons of the 3x3 or 5x5 grid will go depending on
the orientation. This method seemed more extensible for the future as
there is no need to create a separate layout file for each orientation.
If the program were to be extended to having a 7x7 grid, only the number
of columns and rows would need to change.

The view also contains a button to reset the game as well as a text view
to show who the current player is.

All these elements are intended to be interacted with the controller.
For this application, the view is the activity\_main.xml file

### Controller 

The controller handles user events such as clicks or taps on certain
elements. In this program, the controller firstly checks the orientation
of the device and then constructs a grid of 3x3 for portrait or 5x5 for
landscape and adds button listeners to check and set the cell using the
above described model.

Due to the model returning a boolean based on the winning conditions,
the controller implements that by making sure that if a win occurs, the
user is notified through a toast and prevents any further gameplay until
the game is reset.

When changing orientations, the controller does exactly what is
specified in (2). The game state is saved through serialising the matrix
object to be passed on to the new activity. It is then reassigned and
populates a grid based on the orientation.

If the orientation changes from portrait to landscape, the new 5x5 grid
is populated with the old 3x3 data. If the 3x3 grid is in a won state,
the game resets.

If the orientation changes from landscape to portrait, the grid is reset
regardless of any changes made prior as stated in the assignment
specification.

Class diagrams 
---------------
![class diagram](https://i.imgur.com/4yS9O3B.png)
