import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
public class StateManager {

    HashMap<String, State> statesNFA; // key: state name, value: state
    Set<String> newStates;
    public Set<String> allStates;

    ArrayList<String> stateNamesNFA;
    ArrayList<String> inputs;
    //  A  0   1
    public ArrayList<String> transitionTable;       //  B  A   BC
    //  C  AB  ""

    public StateManager()
    {
        statesNFA = new HashMap<String,State>();
        newStates = new HashSet<String>();
        allStates = new HashSet<String>();
        stateNamesNFA = new ArrayList<String>();
        inputs = new ArrayList<String>();
        transitionTable= new ArrayList<String>();
    }
    public void AddNewStates()
    {

        while (this.newStates.size() > 0)
        {
            String[] newStateNames = newStates.toArray(new String[newStates.size()]);
            ArrayList<State> hashSetStates = NameArrayToStateList(newStateNames);
            // DEBUG
            //System.out.println(hashSetStates.size());
            //System.out.println(hashSetStates.get(0).name);
            // ----------------
            State newState = hashSetStates.get(hashSetStates.size() - 1 ); // Get Last Item

            String transitionLineNew = newState.name + " ";

            ArrayList<State> parsedStates = GetParsedStates(newState);

            ArrayList<String> newCreatedStates = new ArrayList<String>();

            for(String input: inputs)
            {
                State totalMergedState = null;
                for(State state: parsedStates)
                {

                    ArrayList<State> sameInputStates = new ArrayList<State>();
                    for (int i = 0; i < state.transitions.size(); i++)
                    {
                        String[] arr = state.transitions.get(i).split(" ");
                        if(input.equals(arr[0]))
                        {
                            State sameInputState = statesNFA.get(arr[1]);
                            sameInputStates.add(sameInputState);
                        }
                    }
                    if(sameInputStates.size() >= 2)
                    {
                        State mergedState = null;
                        for (int i = 0; i < sameInputStates.size() - 1; i++) {
                            mergedState = MergeStates(sameInputStates.get(i),sameInputStates.get(i+1));
                            if(totalMergedState == null) { totalMergedState = mergedState;}
                            else{ totalMergedState = MergeStates(totalMergedState,mergedState);}
                        }
                    }
                    else if(sameInputStates.size() == 1){
                        if(totalMergedState == null) { totalMergedState = new State(state.name);}
                        else{ totalMergedState = MergeStates(totalMergedState,state);}
                    }
                    else{ }
                }

                // If totalMerged is different add to new States
                if(!allStates.contains(NormalizeName(totalMergedState.name))) {
                    allStates.add(NormalizeName(totalMergedState.name));
                    newCreatedStates.add(NormalizeName(totalMergedState.name));
                }
                transitionLineNew += NormalizeName(totalMergedState.name) + " ";
            }
            transitionTable.add(transitionLineNew);
            Set<String> newHashSet = new HashSet<String>();
            for(int i = 0; i < hashSetStates.size() - 1; i++)
            {
                newHashSet.add(hashSetStates.get(i).name);
            }
            for (int i = 0; i < newCreatedStates.size(); i++)
            {
                newHashSet.add(newCreatedStates.get(i));

            }
            this.newStates = newHashSet;
        }
    }

    public String NormalizeName(String stateName)
    {
        stateName = RemoveSameChars(stateName);
        char[] splitted = new char[stateName.length()];
        for (int i = 0; i < splitted.length ; i++) {
            splitted[i] = stateName.charAt(i);
        }
        // BUBBLE SORT
        int n = splitted.length;
        for (int i = 0; i < n - 1; i++){
            for (int j = 0; j < n - i - 1; j++){
                if (splitted[j] > splitted[j + 1]) {
                    // swap arr[j+1] and arr[j]
                    char temp = splitted[j];
                    splitted[j] = splitted[j + 1];
                    splitted[j + 1] = temp;
                }
            }
        }
        String normalized = "";
        for (int i = 0; i < splitted.length; i++) {
            normalized += Character.toString(splitted[i]);
        }
        return normalized;
    }

    public String RemoveSameChars(String stateName)
    {
        String[] arr = stateName.split("");
        String newName = "";
        for (int i = 0; i < arr.length; i++) {
            if(arr[i].equals("XXX") ) {continue;}
            for (int j = i + 1; j < arr.length ; j++) {
                if(arr[j].equals("XXX")) {continue;}

                if(arr[i].equals(arr[j])){ arr[j] = "XXX"; }
            }
        }
        for (String s: arr){
            if(!s.equals("XXX")) { newName += s;}
        }

        return newName;
    }

    ArrayList<State> NameArrayToStateList(String[] nameList)
    {
        ArrayList<State> list = new ArrayList<State>();

        for(String name: nameList)
        {
            list.add(new State(name));
        }
        return list;
    }

    public void TransitionCreateDFA()
    {
        // CREATE FIRST LINE
        String firstLine = "  ";
        for(String input: inputs){ firstLine += (input + " "); }
        transitionTable.add(firstLine);
        // ----------------------------------

        for(String stateName: stateNamesNFA)
        {
            State state = statesNFA.get(stateName);
            allStates.add(stateName);
            String transitionLineDFA = stateName + " ";

            for(String input: inputs)
            {
                ArrayList<State> sameInputStates = new ArrayList<State>();

                for (int i = 0; i < state.transitions.size(); i++)
                {
                    String[] arr = state.transitions.get(i).split(" ");
                    if(input.equals(arr[0]))
                    {
                        State sameInputState = statesNFA.get(arr[1]);
                        sameInputStates.add(sameInputState);
                    }
                }
                if(sameInputStates.size() >= 2)
                {
                    State totalMergedState = null;
                    for (int i = 0; i < sameInputStates.size() - 1; i++) {
                        State mergedState = MergeStates(sameInputStates.get(i),sameInputStates.get(i+1));
                        if(totalMergedState == null) { totalMergedState = mergedState; }
                        else { MergeStates(totalMergedState,mergedState);}
                    }
                    if(!totalMergedState.name.equals("X")){ newStates.add(NormalizeName(totalMergedState.name));
                        allStates.add(NormalizeName(totalMergedState.name));}
                    transitionLineDFA += (NormalizeName(totalMergedState.name) + " ");
                }
                else if(sameInputStates.size() == 1){
                    allStates.add(stateName);
                    transitionLineDFA += (sameInputStates.get(0).name + " ");
                }
                else{ transitionLineDFA += "X ";}

            }

            transitionTable.add(transitionLineDFA);
        }
    }

    public ArrayList<State> GetParsedStates(State state)
    {
        ArrayList<State> parsedStates = new ArrayList<State>();

        for (int i = 0; i < state.name.length(); i++)
        {
            String spName = state.name.substring(i,i+1);
            State sp = statesNFA.get(spName);
            parsedStates.add(sp);
        }

        return parsedStates;
    }

    public State MergeStates(State s1, State s2)
    {
        State mergedState = new State(s1.name + s2.name);
        mergedState.name = NormalizeName(mergedState.name);
        return mergedState;
    }

}

