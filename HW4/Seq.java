import java.util.ArrayList;
import java.util.Iterator;

public class Seq extends NonTerminal implements Playable {
	private Playable[] playables;
	
	public Seq(String pattern){
		super("SEQ", pattern);
	}
	
	public Seq(Playable[] p){
		super("SEQ","seq SUBBODY");
		playables = p;
	}
	
	public void interpret() throws Exception{
		if(!isSet()) {
			throw new Exception(NOT_SET_MESSAGE);
		} else {
			Subbody sb = (Subbody) getComponent("SUBBODY");
			sb.interpret();	
			Iterator<Elem> itr = sb.getElems();
			ArrayList<Playable> elems = new ArrayList<Playable>();
			int ctr = 0;
			while(itr.hasNext() ) {
				elems.add(itr.next());
				ctr++;
			}
			playables = new Playable[ctr];
			playables = elems.toArray(playables);
		}
	}
	
	public Playable[] getPlayables(){
		return playables;
	}

	public void play() {
		for(Playable p: playables) {
			p.play();
		}
	}

	public Playable changePitch(int semitones) {
    	Playable[] newPlay = new Playable[playables.length];
        for(int i = 0; i < playables.length; i++) {
		  newPlay[i] = playables[i].changePitch(semitones);
        }
        return new Seq(newPlay);
	}

	public Playable changeTime(double factor) {
        Playable[] newPlay = new Playable[playables.length];
        for(int i = 0; i < playables.length; i++) {
		  newPlay[i] = playables[i].changeTime(factor);
        }
        return new Seq(newPlay);
	}

	public Playable multiply(int times) {
		Playable[] newSeq = new Playable[times];
		for(int i = 0; i < times; i++) {
			newSeq[i] = this;
		}
		return new Seq(newSeq);
	}

}