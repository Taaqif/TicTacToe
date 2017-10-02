package au.edu.murdoch.ict376.tictactoe;

import android.content.res.Configuration;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TicTacToeGrid M = null;                // will hold the grid
    private static final String GRID_BOARD_KEY = "Grid";

    // Number of buttons in the grid
    private int nRows;
    private int nCols;


    // Widgets
    Button buttons[];
    TextView mWhossTurnTextView;

    int ix;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //depending on the orientation set the buttons grid number
        switch (getResources().getConfiguration().orientation){
            case Configuration.ORIENTATION_LANDSCAPE:
                nCols = 5;
                nRows = 5;

                break;
            case Configuration.ORIENTATION_PORTRAIT:
                nCols = 3;
                nRows = 3;
                break;
        }
        buttons = new Button[nRows * nCols];
        //dynamically create a grid of nRows by nCols filled with buttons on the grid layout specified
        createButtonGrid(R.id.grid);
        //reset the previous state
        if (savedInstanceState != null) {
            //restore the game through the serialised saved object
            restoreGame((TicTacToeGrid) savedInstanceState.getSerializable(GRID_BOARD_KEY));
        }else{
            //reset game if no saved state
            initGame();
        }

    }

    /**
     * creates a button grid to be used with the tictactoe grid
     * @param id the grid layout to house the buttons in
     */
    public void createButtonGrid(int id){
        GridLayout layout = (GridLayout) findViewById(id);
        layout.setColumnCount(nCols);

        //dynamically add buttons depending on the global nrows and ncols
        int count = 0;
        for (int i=0; i<nRows; i++){
            for (int j=0; j<nCols; j++) {
                int n = i * nRows + j;
                //dynamically create buttons
                Button b = new Button (this);
                //set the tag to be referenced when clicking the button
                b.setTag(count);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onPlayerClick(v);
                    }
                });
                //simple styling
                b.setBackgroundColor(Color.rgb(250,250,250));
                b.setMinHeight(0);
                b.setMinimumHeight(0);
                //add the newly created button to the grid
                layout.addView(b);
                //assign the global buttons
                buttons[n] = b;
                count++;

            }
        }
    }
    /**
     * Initialize the grid of buttons when the user clicks in the button StartOver
     * @param v
     */
    public void onStartOverClick(View v){
        initGame();
    }

    /**
     * sets the button to the corresponding turns value
     * @param v the view to set the data on
     */
    public void onPlayerClick(View v){

        // if the gave is already over, do nothing
        if (M.isGameOver()){
            Toast.makeText(MainActivity.this, getString(R.string.StartOver), Toast.LENGTH_SHORT).show();
            return;
        }

        // find the index of the button to update
        int id = getClickedButtonIndex(v);

        // Update text and check whether there is a winner
        if (id >=0) {
            boolean isUpdated = updateCell(id);

            // Now let's check whether there is a winner
            if (isUpdated){
                int whoIsWinning = checkWinner();
                if (whoIsWinning == TicTacToeGrid.cross){
                    mWhossTurnTextView.setText(R.string.TextXWinner);
                    Toast.makeText(MainActivity.this, getString(R.string.TextXWinner), Toast.LENGTH_SHORT).show();

                }else{
                    if (whoIsWinning == TicTacToeGrid.circle){
                        mWhossTurnTextView.setText(R.string.TextOWinner);
                        Toast.makeText(MainActivity.this, getString(R.string.TextOWinner), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            if (!M.isGameOver())
                setWhoIsPlayingTextView();
        }
    }

    /**
     * set the cell to the corresponding player code
     * @param ix the cell to set
     * @return true if successful
     */
    public boolean updateCell(int ix){

        boolean isUpdated = false;
        int i = (int)(ix / nCols);  // row index
        int j = ix % nCols;         // column index

        isUpdated = M.setCell(i, j);

        if (isUpdated){

            int textResId;

            if (whoIsPlaying() == TicTacToeGrid.cross) {
                textResId = R.string.x_text;
                M.setWhoIsPlaying(TicTacToeGrid.circle); // next player
            }else{
                textResId = R.string.o_text;
                M.setWhoIsPlaying(TicTacToeGrid.cross);
            }

            buttons[ix].setText(textResId);
        }
        return isUpdated;
    }

    /**
     * check the winner
     * @return the winner
     */
    public int checkWinner(){
        int i = M.whoIsWinning();
        return i;
    }

    /**
     * sets the winning text view
     */
    protected void setWhoIsPlayingTextView(){

       if (whoIsPlaying() == TicTacToeGrid.cross)
           mWhossTurnTextView.setText(R.string.TextXturn);
       else
           mWhossTurnTextView.setText(R.string.TextOturn);
   }

    /**
     * return who is playing
     * @return the playing person code
     */
   public int whoIsPlaying(){
       return M.whoIsPlaying(); //mWhoIsPlaying;
   }


    /**
     * initialise the game
     */
   private void initGame(){
        //if the board doesnt exisit, create a new one
       if (M==null){
           M = new TicTacToeGrid(nRows, nCols);
       }
       //populate the grid with blank data
       for (int i=0; i<nRows; i++){
           for (int j=0; j<nCols; j++){
               ix = i*nCols  + j;
               buttons[ix].setText(R.string.empty_text);
           }
       }
        //clear the board data
       M.clear();
        //set the first player
       mWhossTurnTextView = (TextView)findViewById(R.id.textWhoIsPlaying);
       setWhoIsPlayingTextView();
        //game is not over
       M.setIsGameOver(false);
       Toast.makeText(MainActivity.this, getString(R.string.ResetGame), Toast.LENGTH_SHORT).show();
   }

    /**
     * restores the game to its previous state
     * @param restore the tictactoe object to respore from
     */
    private void restoreGame(TicTacToeGrid restore){
        //check orientation
        //create a new 5x5 grid,
        //assign each to the corner

        switch (getResources().getConfiguration().orientation){
            case Configuration.ORIENTATION_LANDSCAPE:
                TicTacToeGrid n = null;
                //if game is over
                if(restore.isGameOver()){
                    n = new TicTacToeGrid(5,5);
                    Toast.makeText(MainActivity.this, getString(R.string.ResetGame), Toast.LENGTH_SHORT).show();
                }else{
                    //create a new game
                    n = new TicTacToeGrid(5,5);
                    for(int i = 0; i < restore.getW(); i++){
                        for(int j = 0; j < restore.getH(); j++){
                            n.set(i,j,restore.get(i,j));
                        }
                    }
                    Toast.makeText(MainActivity.this, getString(R.string.RestoreGame), Toast.LENGTH_SHORT).show();
                    n.setWhoIsPlaying(restore.whoIsPlaying());
                }
                //set the board to the passed object
                M = n;
                int resId;
                for (int i=0; i<nRows; i++){
                    for (int j=0; j<nCols; j++){
                        ix = i*nCols  + j;
                        switch (M.get(i,j)){
                            case TicTacToeGrid.circle:
                                resId = R.string.o_text;
                                break;
                            case TicTacToeGrid.cross:
                                resId = R.string.x_text;
                                break;
                            default:
                                resId = R.string.empty_text;
                        }
                        buttons[ix].setText(resId);
                    }
                }
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                //simply reset the game
                initGame();
                return;
        }



        mWhossTurnTextView = (TextView)findViewById(R.id.textWhoIsPlaying);
        setWhoIsPlayingTextView();

    }

    /**
     * uses the tag to get the index of a button
     * @param v the view to search for the tag
     * @return the index of the button
     */
   public int getClickedButtonIndex(View v) {

       return Integer.parseInt(v.getTag().toString());
   }
    @Override
    protected void onSaveInstanceState (Bundle outState) {
        //serialise the object for later use
        super.onSaveInstanceState(outState);
        outState.putSerializable(GRID_BOARD_KEY, M);
    }
}
