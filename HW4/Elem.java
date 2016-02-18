public class Elem extends NonTerminal implements Playable {
	private Playable play;

	public Elem(String pattern) {
		super("ELEM",pattern);
	}

	public void interpret() throws Exception {
		if(!isSet()) {
			throw new Exception(NOT_SET_MESSAGE);
		} else {
			String[] possible = new String[] {
				"NOTE",
				"REST",
				"VAR",
				"SYNC",
				"SEQ"
			};
			for(String s: possible) {
				play = (Playable)getComponent(s);

				if( play != null ) {
					break;
				}
			}
			try {
				play.interpret();
			} catch(Exception e) {
				System.out.println(e.getMessage());
			}
			SubElem subelem = (SubElem)getComponent("SUBELEM");
			play = subelem.value(play);
		}
	}

	public void play() {
		play.play();
	}

	public Playable changePitch(int semitones) {
		return play.changePitch(semitones);
	}

	public Playable changeTime(double factor) {
		return play.changeTime(factor);
	}

	public Playable multiply(int times) {
		return play.multiply(times);
	}
}