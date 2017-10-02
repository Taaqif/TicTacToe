package au.edu.murdoch.ict376.tictactoe;

/**
 * Created by 20160132 on 25/08/2016.
 */
public class Matrix {

    private int[] M;        // restricted to int elements only
    int w;
    int h;

    public Matrix(int w, int h){

        this.w = w;
        this.h = h;

        M = new int[w*h];

        for (int i=0; i<h; i++){
            for (int j=0; j<w; j++){
                M[i*w + j] = 0;
            }
        }
    }

    public int get(int row, int col){
        // can check row and col
        return M[row*w + col];
    }

    public void set(int row, int col, int val){
        M[row*w + col] = val;
    }

    int getW(){
        return w;
    }

    int getH(){
        return h;
    }


}

