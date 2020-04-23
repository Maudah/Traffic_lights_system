


public class Controller extends Thread {
	Event64[] evAtRed, evToShabat, evToChoL, evToGeen, evToRed;
	Event64 evToShabatFromB, evToCholFromB, evRegel;

	public Controller(Event64[] evAtRed, Event64[] evToShabat, Event64[] evToChoL, Event64[] evToGeen,
			Event64[] evToRed, Event64 evToShabatFromB, Event64 evToCholFromB, Event64 evRegel) {
		super();
		this.evAtRed = evAtRed;
		this.evToShabat = evToShabat;
		this.evToChoL = evToChoL;
		this.evToGeen = evToGeen;
		this.evToRed = evToRed;
		this.evToShabatFromB = evToShabatFromB;
		this.evToCholFromB = evToCholFromB;
		this.evRegel = evRegel;
		start();
	}

	enum OutStates {ON_CHOL, ON_SHABAT}

	;
	OutStates outState;

	enum InState {
		ON_SEND_FIRST_GREEN, ON_SEND_FIRST_RED, ON_FIRST_AT_RED,
		ON_SEND_SECOND_GREEN, ON_SEND_SECOND_RED, ON_SECOND_AT_RED,
		ON_SEND_THIRD_GREEN, ON_SEND_THIRD_RED, ON_THIRD_AT_RED,
		ON_SEND_FOURTH_GREEN, ON_SEND_FOURTH_RED, ON_FOURTH_AT_RED, ON_FIRST_CONDITION, ON_SECOND_CONDITION, ON_THIRD_CONDITION, ON_FOURTH_CONDITION
	}

	InState inState;

	public Controller() {
		start();
	}

	public void run() {
		MyTimer72 timer;
		Event64   evTimer;
		evTimer=new Event64();
		timer=  new MyTimer72(5000,evTimer);


		outState = OutStates.ON_CHOL;
		try {
			sleep(1000);
		}
		catch (InterruptedException e1) {		
			e1.printStackTrace();
		}

		evSendFirstGreen();
		inState = InState.ON_SEND_FIRST_GREEN;

		try {
			while (true) {
				switch (outState) {
				case ON_CHOL:
					while (outState == OutStates.ON_CHOL) {                     	
						if (evToShabatFromB.arrivedEvent()) {
							evToShabatFromB.waitEvent();
							evSendToAllShabat();
							outState = OutStates.ON_SHABAT;
							break;
						}
						switch (inState) {
						case ON_SEND_FIRST_GREEN:
							while(true) {
								if (evTimer.arrivedEvent()){                                     
									evTimer.waitEvent();
									break;
								}
								yield();
							}

							if (evToShabatFromB.arrivedEvent()) {
								evToShabatFromB.waitEvent();
								evSendToAllShabat();
								outState = OutStates.ON_SHABAT;
								break;
							}
							evSendFirstRed();
							inState = InState.ON_SEND_FIRST_RED;
							break;
						case ON_SEND_FIRST_RED:
							while (true) {
								if (evToShabatFromB.arrivedEvent()) {
									evToShabatFromB.waitEvent();
									evSendToAllShabat();
									outState = OutStates.ON_SHABAT;
									break;
								}
								if (isEvFirstAtRed())
									break;							
								yield();
							}
							waitFirst();
							evTimer=new Event64();
							timer=  new MyTimer72(5000,evTimer);
							inState = InState.ON_FIRST_AT_RED;
							break;
						case ON_FIRST_AT_RED:					
							while(true)
							{
								if(evTimer.arrivedEvent())
									break;
								yield();
							}
							if (evToShabatFromB.arrivedEvent()) {
								evToShabatFromB.waitEvent();
								evSendToAllShabat();
								outState = OutStates.ON_SHABAT;
								break;
							}
							if (evRegel.arrivedEvent()) {
								Object j = evRegel.waitEvent();
								int i = (int) Integer.parseInt((String)j);								
								if (i == 6 || i == 7 || i == 12 || i == 13 || i == 10 || i == 9) {
									evTimer=new Event64();
									timer=  new MyTimer72(5000,evTimer);
									inState = InState.ON_SEND_FIRST_GREEN;								
									evSendFirstGreen();
									break;
								} else if (i == 4 || i == 5 || i == 6 || i == 7 || i == 9 || i == 12 || i == 13 || i == 10) {
									evTimer=new Event64();
									timer=  new MyTimer72(5000,evTimer);
									inState = InState.ON_SEND_SECOND_GREEN;
									evSendSecondGreen();
									System.out.println(i);
									break;
								} else if (i == 4 || i == 5 || i == 6 || i == 7 || i == 8 || i == 11 || i == 12 || i == 13 || i == 14 || i == 15) {
									evTimer=new Event64();
									timer=  new MyTimer72(5000,evTimer);
									inState = InState.ON_SEND_THIRD_GREEN;
									evSendThirdGreen();
									break;
								} else if (i == 4 || i == 5 || i == 8 || i == 9 || i == 10 || i == 11 || i == 14 || i == 15) {
									evTimer=new Event64();
									timer=  new MyTimer72(5000,evTimer);
									inState = InState.ON_SEND_FOURTH_GREEN;
									evSendFourthGreen();
									break;
								}

							}
							else {
								evSendSecondGreen();
								evTimer=new Event64();
								timer=  new MyTimer72(5000,evTimer);
								inState = InState.ON_SEND_SECOND_GREEN;
							}
							break;
						case ON_SEND_SECOND_GREEN:
							while(true){
								if(evTimer.arrivedEvent())
									break;
								yield();
							}
							if (evToShabatFromB.arrivedEvent()) {
								evToShabatFromB.waitEvent();
								evSendToAllShabat();
								outState = OutStates.ON_SHABAT;
								break;
							}
							evSendSecondRed();
							inState = InState.ON_SEND_SECOND_RED;
							break;
						case ON_SEND_SECOND_RED:
							while (true) {
								if (isEvSecondAtRed())
									break;
								yield();
							}
							waitSecond();
							evTimer=new Event64();
							timer=  new MyTimer72(5000,evTimer);
							inState = InState.ON_SECOND_AT_RED;
							break;
						case ON_SECOND_AT_RED:
							while(true){
								if(evTimer.arrivedEvent())
									break;
								yield();
							}
							if (evRegel.arrivedEvent()) {
								Object j = evRegel.waitEvent();
								int i = (int) Integer.parseInt((String)j);
								if (i == 6 || i == 7 || i == 12 || i == 13 || i == 10 || i == 9) {
									evTimer=new Event64();
									timer=  new MyTimer72(5000,evTimer);
									inState = InState.ON_SEND_FIRST_GREEN;
									evSendFirstGreen();
									break;
								} else if (i == 4 || i == 5 || i == 6 || i == 7 || i == 9 || i == 12 || i == 13 || i == 10) {
									evTimer=new Event64();
									timer=  new MyTimer72(5000,evTimer);
									inState = InState.ON_SEND_SECOND_GREEN;
									evSendSecondGreen();
									break;
								} else if (i == 4 || i == 5 || i == 6 || i == 7 || i == 8 || i == 11 || i == 12 || i == 13 || i == 14 || i == 15) {
									evTimer=new Event64();
									timer=  new MyTimer72(5000,evTimer);
									inState = InState.ON_SEND_THIRD_GREEN;
									evSendThirdGreen();
									break;
								} else if (i == 4 || i == 5 || i == 8 || i == 9 || i == 10 || i == 11 || i == 14 || i == 15) {
									evTimer=new Event64();
									timer=  new MyTimer72(5000,evTimer);
									inState = InState.ON_SEND_FOURTH_GREEN;
									evSendFourthGreen();
									break;
								}

							}
							evSendThirdGreen();
							evTimer=new Event64();
							timer=  new MyTimer72(5000,evTimer);
							inState = InState.ON_SEND_THIRD_GREEN;
							break;
						case ON_SEND_THIRD_GREEN:
							while(true){
								if(evTimer.arrivedEvent())
									break;								
								yield();
							}
							evSendThirdRed();
							inState = InState.ON_SEND_THIRD_RED;
							break;
						case ON_SEND_THIRD_RED:
							while (true) {
								if (isEvThirdAtRed())
									break;
								yield();
							}
							waitThird();
							evTimer=new Event64();
							timer=  new MyTimer72(5000,evTimer);
							inState = InState.ON_THIRD_AT_RED;
							break;
						case ON_THIRD_AT_RED:							
							while(true){
								if(evTimer.arrivedEvent())
									break;
								yield();
							}
							if (evRegel.arrivedEvent()) {
								Object j = evRegel.waitEvent();
								int i = (int) Integer.parseInt((String)j);
								if (i == 6 || i == 7 || i == 12 || i == 13 || i == 10 || i == 9) {
									evTimer=new Event64();
									timer=  new MyTimer72(5000,evTimer);
									inState = InState.ON_SEND_FIRST_GREEN;
									evSendFirstGreen();
									break;
								} else if (i == 4 || i == 5 || i == 6 || i == 7 || i == 9 || i == 12 || i == 13 || i == 10) {
									evTimer=new Event64();
									timer=  new MyTimer72(5000,evTimer);
									inState = InState.ON_SEND_SECOND_GREEN;
									evSendSecondGreen();
									break;
								} else if (i == 4 || i == 5 || i == 6 || i == 7 || i == 8 || i == 11 || i == 12 || i == 13 || i == 14 || i == 15) {
									evTimer=new Event64();
									timer=  new MyTimer72(5000,evTimer);
									inState = InState.ON_SEND_THIRD_GREEN;
									evSendThirdGreen();
									break;
								} else if (i == 4 || i == 5 || i == 8 || i == 9 || i == 10 || i == 11 || i == 14 || i == 15) {
									evTimer=new Event64();
									timer=  new MyTimer72(5000,evTimer);
									inState = InState.ON_SEND_FOURTH_GREEN;
									evSendFourthGreen();
									break;
								}
							}
							evSendFourthGreen();
							evTimer=new Event64();
							timer=  new MyTimer72(5000,evTimer);
							inState = InState.ON_SEND_FOURTH_GREEN;
							break;
						case ON_SEND_FOURTH_GREEN:						
							while(true){
								if(evTimer.arrivedEvent())
									break;
								yield();
							}
							evSendFourthRed();
							inState = InState.ON_SEND_FOURTH_RED;
							break;
						case ON_SEND_FOURTH_RED:
							while (true) {
								if (isEvFourthAtRed())
									break;
								yield();
							}
							waitFourth();
							evTimer=new Event64();
							timer=  new MyTimer72(5000,evTimer);
							inState = InState.ON_FOURTH_AT_RED;
							break;
						case ON_FOURTH_AT_RED:
							while(true){
								if(evTimer.arrivedEvent())
									break;
								yield();
							}
							if (evRegel.arrivedEvent()) {
								Object j = evRegel.waitEvent();
								int i = (int) Integer.parseInt((String)j);
								if (i == 6 || i == 7 || i == 12 || i == 13 || i == 10 || i == 9) {
									evTimer=new Event64();
									timer=  new MyTimer72(5000,evTimer);
									inState = InState.ON_SEND_FIRST_GREEN;
									evSendFirstGreen();
									break;
								} else if (i == 4 || i == 5 || i == 6 || i == 7 || i == 9 || i == 12 || i == 13 || i == 10) {
									evTimer=new Event64();
									timer=  new MyTimer72(5000,evTimer);
									inState = InState.ON_SEND_SECOND_GREEN;
									evSendSecondGreen();
									break;
								} else if (i == 4 || i == 5 || i == 6 || i == 7 || i == 8 || i == 11 || i == 12 || i == 13 || i == 14 || i == 15) {
									evTimer=new Event64();
									timer=  new MyTimer72(5000,evTimer);
									inState = InState.ON_SEND_THIRD_GREEN;
									evSendThirdGreen();
									break;
								} else if (i == 4 || i == 5 || i == 8 || i == 9 || i == 10 || i == 11 || i == 14 || i == 15) {
									evTimer=new Event64();
									timer=  new MyTimer72(5000,evTimer);
									inState = InState.ON_SEND_FOURTH_GREEN;
									evSendFourthGreen();
									break;
								}
							}
							evSendFirstGreen();
							evTimer=new Event64();
							timer=  new MyTimer72(5000,evTimer);
							inState = InState.ON_SEND_FIRST_GREEN;
							break;
						default:
							break;
						}
					}
					break;
				case ON_SHABAT:			
					while (true) {						
						if (evToCholFromB.arrivedEvent()) {
							evToCholFromB.waitEvent();
							evSendToAllChol();
							outState = OutStates.ON_CHOL;
							evSendFirstGreen();
							evTimer=new Event64();
							timer=  new MyTimer72(5000,evTimer);
							inState = InState.ON_SEND_FIRST_GREEN;
							break;
						} else yield();
					}
					break;
				default:
					break;
				}
			}
		} catch (Exception e) {
			//
		}
	}


	private void evSendFourthGreen() {
		evToGeen[3].sendEvent();
		evToGeen[4].sendEvent();
		evToGeen[5].sendEvent();
		evToGeen[14].sendEvent();
		evToGeen[15].sendEvent();
		evToGeen[8].sendEvent();
		evToGeen[9].sendEvent();
		evToGeen[10].sendEvent();
		evToGeen[11].sendEvent();
	}

	private boolean isEvFourthAtRed() {
		return evAtRed[3].arrivedEvent() &&
				evAtRed[4].arrivedEvent() &&
				evAtRed[5].arrivedEvent() &&
				evAtRed[14].arrivedEvent() &&
				evAtRed[15].arrivedEvent() &&
				evAtRed[8].arrivedEvent() &&
				evAtRed[9].arrivedEvent() &&
				evAtRed[10].arrivedEvent() &&
				evAtRed[11].arrivedEvent();
	}

	private void waitFourth() {
		evAtRed[3].waitEvent();
		evAtRed[4].waitEvent();
		evAtRed[5].waitEvent();
		evAtRed[14].waitEvent();
		evAtRed[15].waitEvent();
		evAtRed[8].waitEvent();
		evAtRed[9].waitEvent();
		evAtRed[10].waitEvent();
		evAtRed[11].waitEvent();
	}

	private boolean isEvThirdAtRed() {
		return evAtRed[2].arrivedEvent() &&
				evAtRed[4].arrivedEvent() &&
				evAtRed[5].arrivedEvent() &&
				evAtRed[6].arrivedEvent() &&
				evAtRed[7].arrivedEvent() &&
				evAtRed[8].arrivedEvent() &&
				evAtRed[12].arrivedEvent() &&
				evAtRed[15].arrivedEvent() &&
				evAtRed[14].arrivedEvent() &&
				evAtRed[13].arrivedEvent() &&
				evAtRed[11].arrivedEvent();
	}

	private void waitThird() {
		evAtRed[2].waitEvent();
		evAtRed[4].waitEvent();
		evAtRed[5].waitEvent();
		evAtRed[6].waitEvent();
		evAtRed[7].waitEvent();
		evAtRed[8].waitEvent();
		evAtRed[12].waitEvent();
		evAtRed[13].waitEvent();
		evAtRed[14].waitEvent();
		evAtRed[15].waitEvent();
		evAtRed[11].waitEvent();
	}

	private void evSendThirdRed() {
		evToRed[2].sendEvent();
		evToRed[4].sendEvent();
		evToRed[5].sendEvent();
		evToRed[6].sendEvent();
		evToRed[7].sendEvent();
		evToRed[8].sendEvent();
		evToRed[12].sendEvent();
		evToRed[13].sendEvent();
		evToRed[14].sendEvent();
		evToRed[15].sendEvent();
		evToRed[11].sendEvent();
	}

	private void evSendFourthRed() {
		evToRed[3].sendEvent();
		evToRed[4].sendEvent();
		evToRed[5].sendEvent();
		evToRed[14].sendEvent();
		evToRed[15].sendEvent();
		evToRed[8].sendEvent();
		evToRed[9].sendEvent();
		evToRed[10].sendEvent();
		evToRed[11].sendEvent();
	}

	private void evSendThirdGreen() {
		evToGeen[2].sendEvent();
		evToGeen[4].sendEvent();
		evToGeen[5].sendEvent();
		evToGeen[6].sendEvent();
		evToGeen[7].sendEvent();
		evToGeen[8].sendEvent();
		evToGeen[12].sendEvent();
		evToGeen[11].sendEvent();
		evToGeen[13].sendEvent();
		evToGeen[14].sendEvent();
		evToGeen[15].sendEvent();
	}

	private boolean isEvSecondAtRed() {
		return evAtRed[1].arrivedEvent() &&
				evAtRed[4].arrivedEvent() &&
				evAtRed[5].arrivedEvent() &&
				evAtRed[6].arrivedEvent() &&
				evAtRed[7].arrivedEvent() &&
				evAtRed[13].arrivedEvent() &&
				evAtRed[9].arrivedEvent() &&
				evAtRed[12].arrivedEvent() &&
				evAtRed[10].arrivedEvent();
	}

	private void waitSecond() {
		evAtRed[1].waitEvent();
		evAtRed[4].waitEvent();
		evAtRed[5].waitEvent();
		evAtRed[6].waitEvent();
		evAtRed[7].waitEvent();
		evAtRed[13].waitEvent();
		evAtRed[12].waitEvent();
		evAtRed[10].waitEvent();
		evAtRed[9].waitEvent();
	}

	private void evSendSecondRed() {
		evToRed[1].sendEvent();
		evToRed[4].sendEvent();
		evToRed[5].sendEvent();
		evToRed[6].sendEvent();
		evToRed[7].sendEvent();
		evToRed[13].sendEvent();
		evToRed[12].sendEvent();
		evToRed[10].sendEvent();
		evToRed[9].sendEvent();
	}

	private void evSendSecondGreen() {
		evToGeen[1].sendEvent();
		evToGeen[4].sendEvent();
		evToGeen[5].sendEvent();
		evToGeen[6].sendEvent();
		evToGeen[7].sendEvent();
		evToGeen[13].sendEvent();
		evToGeen[12].sendEvent();
		evToGeen[10].sendEvent();
		evToGeen[9].sendEvent();
	}

	private boolean isEvFirstAtRed() {
		return evAtRed[0].arrivedEvent() &&
				evAtRed[12].arrivedEvent() &&
				evAtRed[10].arrivedEvent() &&
				evAtRed[6].arrivedEvent() &&
				evAtRed[7].arrivedEvent() &&
				evAtRed[13].arrivedEvent() &&
				evAtRed[9].arrivedEvent();
	}

	private void waitFirst() {
		evAtRed[0].waitEvent();
		evAtRed[12].waitEvent();
		evAtRed[10].waitEvent();
		evAtRed[6].waitEvent();
		evAtRed[7].waitEvent();
		evAtRed[13].waitEvent();
		evAtRed[9].waitEvent();
	}

	private void evSendFirstRed() {
		evToRed[0].sendEvent();
		evToRed[6].sendEvent();
		evToRed[7].sendEvent();
		evToRed[12].sendEvent();
		evToRed[13].sendEvent();
		evToRed[10].sendEvent();
		evToRed[9].sendEvent();
	}

	private void evSendFirstGreen() {
		System.out.println("evSendFirstGreen ");
		evToGeen[0].sendEvent();
		evToGeen[6].sendEvent();
		evToGeen[7].sendEvent();
		evToGeen[12].sendEvent();
		evToGeen[13].sendEvent();
		evToGeen[10].sendEvent();
		evToGeen[9].sendEvent();
	}

	private void evSendToAllShabat() {
		for (int i = 0; i < 16; i++)
			evToShabat[i].sendEvent();
	}

	private void evSendToAllChol() {
		for (int i = 0; i < 16; i++)
			evToChoL[i].sendEvent();
	}
}
