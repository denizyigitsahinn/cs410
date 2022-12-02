public class Main {
    public static void main(String[] args) {

        StateManager stateManager = new StateManager();
        FileReader fileReader = new FileReader(stateManager);
        fileReader.ReadNFA();
        stateManager.TransitionCreateDFA();
        stateManager.AddNewStates();

        System.out.println("ALPHABET");
        System.out.println("0\n" + "1");

        String[] allStateNames = stateManager.allStates.toArray(new String[stateManager.allStates.size()]);
        System.out.println("\nSTATES");
        for (int i = 0; i < allStateNames.length; i++) {
            System.out.println(allStateNames[i]);
        }
        System.out.println("\n");
        System.out.println("TRANSITIONS");

        for(String transition: stateManager.transitionTable)
        {

            System.out.println(transition);
        }


    }
}