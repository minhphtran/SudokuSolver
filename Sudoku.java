import java.util.*; 

public class Sudoku{ 
  public int numberOfSolvedCells; 
  public Cell[][] game; 
  public int check;
  
  // construct an empty sudoku
  public Sudoku(){
     game = new Cell[9][9]; 
    for(int i = 0; i<9; i++){ 
      for(int j = 0; j<9; j++){ 
        game[i][j]=new Cell(i,j); 
      } 
    } 
    check = -1;
    numberOfSolvedCells = 0;
  }
  
  
  public void useHints(Queue<Cell> hints){
    // write the hints onto the sudoku
    for(int i = 0; i<numberOfSolvedCells;i++){
      Cell temp = hints.dequeue();
      game[temp.giveRow()][temp.giveColumn()] =temp;
      hints.enqueue(temp);
    }
    
    // execute simpleLogic on all given hints
    while(!hints.isEmpty()){
      Cell temp = hints.dequeue();
      simpleLogic(temp);
    }
  }
  
  
  public void solve(){
    ArrayList<Cell> cells = new ArrayList<Cell>();
    // execute generalization method with all the rows/ columns/ blocks in this sudoku
    for(int i=0; i<9; i++){
      for(int j=0; j<9; j++){
        cells.clear(); linkedRow(cells, game[i][j]);
        generalization(cells, game[i][j]);
        
        cells.clear(); linkedColumn(cells, game[i][j]);
        generalization(cells, game[i][j]);
        
        cells.clear(); linkedBlock(cells, game[i][j]);
        generalization(cells, game[i][j]);
      }
    }
    
    // recursively solve the sudoku until the solution cannot be improved anymore
    count();     
    if(numberOfSolvedCells !=81 && numberOfSolvedCells != check){
      check = numberOfSolvedCells;
      solve();
    }
  }
  
  
  public void simpleLogic(Cell temp){
    ArrayList<Cell> cells = new ArrayList<Cell>();
    linkedRow(cells, temp); linkedColumn(cells, temp); linkedBlock(cells, temp);
    
    // delete the value of a solved cell
    // from the candidate list of unsolved cells in the same row/column/block
    for(Cell unsolved:cells){
      unsolved.deleteCandidate(temp.giveHint());
      if(unsolved.isSolved()){
        game[unsolved.giveRow()][unsolved.giveColumn()] = unsolved;
        simpleLogic(unsolved);      
      }
    }
  }
  
  //generalization method
  public void generalization(ArrayList<Cell> cells, Cell cell){
    if(!cell.isSolved()){
      // check if an unsolved cell have a unique candidate
      // through out the given array
      for(int value:cell.candidates){
        boolean isDuplicate = false;
        for(Cell unsolved:cells){
          if(unsolved.isCandidate(value)){
            isDuplicate = true;
            break;
          }
        }
        if(!isDuplicate){
          // assign that unique value to the cell
          cell.candidates.clear();
          cell.candidates.add(value);
          // update the sudoku by simple logic with the new solved cell
          simpleLogic(cell);
          break;
        }
      }
    }
  }
  
  // create an arraylist containing all unsolved cells from the same row
  // as the given cell
  public void linkedRow(ArrayList<Cell> cells, Cell cell) {
    for (int i=0; i<9; i++) {
      if (!game[i][cell.giveColumn()].isSolved() && i != cell.giveRow()) {
        cells.add(game[i][cell.giveColumn()]);
      }
    }
  }
  
  // create an arraylist containing all unsolved cells from the same column
  // as the given cell
  public void linkedColumn(ArrayList<Cell> cells, Cell cell) {
    for (int j=0; j<9; j++) {
      if (!game[cell.giveRow()][j].isSolved() && j != cell.giveColumn()) {
        cells.add(game[cell.giveRow()][j]);
      }
    }
  }
  
  // create an arraylist containing all unsolved cells from the same block
  // as the given cell
  public void linkedBlock(ArrayList<Cell> cells, Cell cell) {
    int[] block = {cell.giveRow()/3, cell.giveColumn()/3};
    
    for (int i = 3*block[0]; i < 3*block[0] + 3; i++) {
      for (int j = 3*block[1];j < 3*block[1] + 3; j++) {
        if (!game[i][j].isSolved() && (i != cell.giveRow() || j != cell.giveColumn())) {
          cells.add(game[i][j]);
        }
      } 
    }
  }
  
  // contruct a copy of this sudoku
  public Sudoku copy(){
    Sudoku result = new Sudoku();
    
    result.numberOfSolvedCells = this.numberOfSolvedCells;
    // using the copy method from class cell to clone this sudoku
    result.game = new Cell[9][9];
    for(int i=0; i<9; i++){
      for(int j=0; j<9; j++){
        result.game[i][j] = this.game[i][j].copy();
      }
    }
    result.check = this.check;  
    return result;
  }
  
  // check if the sudoku is solvable
  public boolean beSolved(){
    // scan through all row/column/block
    // to check if each number from 1-9 appeared at least once as candidate
    for(int row = 0; row<9; row++){
      for (int i=1; i<10; i++){
        boolean solvable = false;
        for (int column = 0; column<9; column++){
          if(game[row][column].isCandidate(i)){
            solvable = true;  
            break;
          }
        }
        if(!solvable) return false;
      }
    }
    for(int column = 0; column<9; column++){
      for (int i=1; i<10; i++){
        boolean solvable = false;
        for (int row = 0; row<9; row++){
          if(game[column][row].isCandidate(i)){
            solvable = true;  
            break;
          }
        }
        if(!solvable) return false;
      }
    }
    
    for(int k = 0; k<=6; k+=3){                      
      for(int l = 0; l<=6; l+=3){
        for(int number = 1; number<10; number++){
          boolean solvable = false;
          outerloop:
            for(int i = 0; i<3; i++){               
            for(int j = 0; j<3; j++){
              if(game[i+k][j+l].isCandidate(number)){
                solvable = true;
                break outerloop;
              }
            }
          }
            if(!solvable) return false;
        }
      }
    }
    return true;
  } 
  
 // count the number of solved cells 
  public void count(){
    int number = 0;
    for(int i=0; i<9; i++){
      for(int j=0; j<9; j++)
      {
        if(game[i][j].isSolved())
        {
          number++;
        }
      }
    }
    numberOfSolvedCells = number;
  }
  
  public void printResult(){ 
    for(int i = 0 ; i<9; i++){ 
      for(int j = 0; j<9; j++){ 
       //System.out.print(game[i][j].isSolved());
        System.out.print(game[i][j].giveHint() + " " ); 
      } 
      System.out.println(); 
    } 
  } 
}
