import java.util.*;

public class Cell
{
  public ArrayList<Integer> candidates; 
  private int numberOfRow;
  private int numberOfColumn;
  
  
  public Cell(int row,int column)
  {
    this.candidates = new ArrayList<Integer>();  
    numberOfRow = row;
    numberOfColumn = column;
    
    //Filling all possible candidates in the candidate list of the empty cells
    for(int i= 1;i<10;i++)
    {
      candidates.add(i);
    }
  }
  
  public Cell(int row, int column, int hint)
  {
    //the candidate list of the solved cells only include its value
    this.candidates = new ArrayList<Integer>();
    candidates.add(hint);
    numberOfRow = row;
    numberOfColumn = column;
  }
  
  // Construct a copy of this cell
  public Cell copy(){
    Cell result = new Cell(this.numberOfRow, this.numberOfColumn);
    result.candidates = (ArrayList<Integer>)this.candidates.clone();
    return result;
  }
  
  public boolean isCandidate(int number) 
  {
    return candidates.contains(number);
  }
  
  // give the value of the solved cells
  public int giveHint()
  {
    return candidates.get(0);
  }

  
  public boolean isSolved()
  {
    return (candidates.size()==1);
  }
  
  public void deleteCandidate(Integer number)
  {
    if(candidates.contains(number)){
      candidates.remove(candidates.indexOf(number));
    }}
  
  public int giveRow()
  {
    return this.numberOfRow;
  }
  
  public int giveColumn()
  {
    return this.numberOfColumn;
  }
  
}
