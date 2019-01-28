import java.util.*; 
import java.io.*; 

public class Solver{
  public static void main (String[] args){
    String filename = args[0]; 
    Sudoku sudoku = new Sudoku();
    Queue<Cell> hints = new Queue<Cell>();
    
    try { 
      hints = read(filename); 
    } 
    catch (FileNotFoundException e) 
    { 
      e.printStackTrace(); 
    }
    
    // solve the sudoku using simple logic and generalization
    sudoku.useHints(hints);
    sudoku.solve();
    
    // use the branching if it cannot be solved
    if(sudoku.numberOfSolvedCells!=81){
    sudoku= branching(sudoku);
    }
    
    sudoku.printResult();
  }
  
  public static Sudoku branching(Sudoku sudoku){
    Sudoku temp = null;
    
    // find the unsolved cell with the least number of candidates
    Cell smallest = new Cell(10,10);
    for(int i=0; i<9; i++){
      for(int j=0; j<9; j++){
        if(!sudoku.game[i][j].isSolved() && smallest.candidates.size()>sudoku.game[i][j].candidates.size()){
          smallest = sudoku.game[i][j];
        }
      }
    }
    // make a copy of that cell
    smallest = smallest.copy(); 
    
    // scan through all candidates of that cell to make a guess 
    for(int x=0; x<smallest.candidates.size(); x++){
      // make a copy of the inputted sudoku
      temp = sudoku.copy();
      //writing the guess on the cell of the copied sudoku
      temp.game[smallest.giveRow()][smallest.giveColumn()].candidates.clear();
      temp.game[smallest.giveRow()][smallest.giveColumn()].candidates.add(smallest.candidates.get(x));
      //execute simple logic rule on the guessed cell
      temp.simpleLogic(temp.game[smallest.giveRow()][smallest.giveColumn()]);
      
      // try to continue soving on the copied sudoku
      temp.solve();
      
      // if the sudoku is not solved yet, and is solvable
      if(temp.numberOfSolvedCells != 81 ){  
        if(temp.beSolved()){
          // recursively apply branching on the copied version
          temp= branching(temp);
          // stop scanning through the other candidates if the lower 
          // level of branching return a solved sudoku
          if(temp.numberOfSolvedCells == 81){
            break;
          } 
        }
      }
      // stop the loop if the guess leads to a solved sudoku without 
      // any further branching
      else break;
    }
    
    return temp;
  }
  
  // read the given hints then put it on a queue and return that queue
  public static Queue<Cell> read(String filename) 
    throws java.io.FileNotFoundException{ 
    File file = new File(filename); 
    Scanner input = new Scanner (file);
    Queue<Cell> hints=new Queue<Cell>(); 
    
    int n = input.nextInt();  
    
    for (int i=0;i<n;i++){
      hints.enqueue(new Cell(input.nextInt(),input.nextInt(),input.nextInt()));
    }
    input.close(); 
    return hints;
  } 
}