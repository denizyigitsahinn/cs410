import java.util.ArrayList;
import java.util.HashMap;
public class State {
    public String name;
    ArrayList<String> transitions;
    public boolean isStart;
    public boolean isFinal;
    public State(String name)
    {
        transitions = new ArrayList<String>();
        this.name = name;
        isStart = false;
        isFinal = false;
    }
    public void AddTransition(State stateConnection, String input)
    {
        transitions.add(input + " " + stateConnection.name);
    }
}
