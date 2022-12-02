import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
public class FileReader {
    StateManager stateManager;
    public FileReader(StateManager stateManager)
    {
        this.stateManager = stateManager;
    }
    public void ReadNFA()
    {
        try {
            File nfaTEXT = new File("C:\\Users\\deniz\\OneDrive\\Masaüstü\\410\\NFA1.txt");
            Scanner myReader = new Scanner(nfaTEXT);
            String currentSearch = "";
            while (myReader.hasNextLine())
            {
                String line = myReader.nextLine();

                if(line.equals("ALPHABET") || line.equals("STATES") || line.equals("START") ||
                        line.equals("FINAL") || line.equals("TRANSITIONS") || line.equals("END"))
                {
                    currentSearch = line;
                    continue;
                }

                else if(currentSearch.equals("ALPHABET")) { stateManager.inputs.add(line); }

                else if(currentSearch.equals("STATES"))
                {
                    State state = new State(line);
                    stateManager.stateNamesNFA.add(state.name);
                    stateManager.statesNFA.put(state.name,state);
                }

                else if(currentSearch.equals("START")) { stateManager.statesNFA.get(line).isStart = true; }

                else if(currentSearch.equals("FINAL")) { stateManager.statesNFA.get(line).isFinal = true; }

                else if(currentSearch.equals("TRANSITIONS"))
                {
                    String[] arrOfStr = line.split(" ", 3);

                    State state1 = stateManager.statesNFA.get(arrOfStr[0]);
                    String input = arrOfStr[1];
                    State state2 = stateManager.statesNFA.get(arrOfStr[2]);
                    state1.AddTransition(state2,input);
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
