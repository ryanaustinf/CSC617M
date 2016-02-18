import java.util.ArrayList;
import java.util.Iterator;

public class Var extends NonTerminal implements Playable
{
	private Playable play;
    
	public Var(String pattern)
    {
		super("VAR",pattern);
	}

	public void interpret() throws Exception
    {
        if(!isSet())
        {
        	throw new Exception(NOT_SET_MESSAGE);
		}
        else
        {
            Token t = ((Token) getComponent("varname"));
            Elem elem = (Elem)SymbolTable.instance().get(t.token());
            
            if(elem == null){
                throw new Exception("Error - Line " + t.lineNo() 
                                        + ": Variable \"" 
                                        + ((Token) getComponent("varname"))
                                            .token() + "\" is not defined.");
            }
            else
            {
                SubVar sv = (SubVar)getComponent("SUBVAR");
                sv.interpret();
                int[] indices = sv.getIndices();
                if(indices != null)
                {
                    switch(elem.type()) {
                        case "NOTE":
                        case "REST":
                            throw new Exception("Indexing not applicable.");
                        case "SYNC":
                            Playable[] originalSync = elem.getPlayables();
                            int lengthSync = 1;
                            if(indices.length == 2) {
                                lengthSync = indices[1] - indices[0] + 1;
                                Playable[] newSyncPlayables = new Playable[lengthSync];
                                for(int i = 0, curIndex = indices[0] - 1; i < lengthSync; i++, curIndex++)
                                    newSyncPlayables[i] = originalSync[curIndex];
                                play = new Sync(newSyncPlayables);
                            } else {
                                play = originalSync[indices[0] - 1];
                            }
                            break;
                        case "SEQ":
                            Playable[] originalSeq = elem.getPlayables();
                            int lengthSeq = 1;
                            if(indices.length == 2) {
                                lengthSeq = indices[1] - indices[0] + 1;
                                Playable[] newSeqPlayables = new Playable[lengthSeq];
                                for(int i = 0, curIndex = indices[0] - 1; i < lengthSeq; i++, curIndex++)
                                    newSeqPlayables[i] = originalSeq[curIndex];
                                play = new Seq(newSeqPlayables);
                            } else {
                                play = originalSeq[indices[0] - 1];
                            }
                            break;
                        default:
                    }
                } else {
                    play = elem;
                }
            }
		}
	}

	public void play()
    {
		play.play();
	}

	public Playable changePitch(int semitones)
    {
		return play.changePitch(semitones);
	}

	public Playable changeTime(double factor)
    {
		return play.changeTime(factor);
	}

	public Playable multiply(int times)
    {
		return (play = play.multiply(times));
	}
}