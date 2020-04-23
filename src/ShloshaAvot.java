import java.awt.Color;

import javax.swing.JPanel;




public class ShloshaAvot extends Thread {
    Ramzor ramzor;
    JPanel panel;
    Event64 evToRed, evToGreen, evAtRed, evToShabat, evToChol;

    enum OutState {ON_CHOL, ON_SHABAT}

    ;
    OutState outState;

    enum InCholState {ON_RED, ON_RED_YELLOW, ON_GREEN, ON_BLINK, ON_YELLOW}

    ;
    InCholState inCholState;

    enum InShabatState {ON_ON, ON_OFF}

    InShabatState inShabatState;

    enum InBlinkState {ON_ON, ON_OFF}

    InBlinkState inBlinkState;

    private boolean stop = true;

    public ShloshaAvot(Ramzor ramzor, JPanel panel, int key, Event64 evToGreen, Event64 evToRed, Event64 evAtRed, Event64 evToShabat,
                       Event64 evToChol) {

        this.ramzor = ramzor;
        this.panel = panel;
        this.evToRed = evToRed;
        this.evToGreen = evToGreen;
        this.evAtRed = evAtRed;
        this.evToShabat = evToShabat;
        this.evToChol = evToChol;
        start();
    }

    public void run() {
    	MyTimer72 timer;
		Event64   evTimer;
		evTimer=new Event64();
		
        int c = 0;
        outState = OutState.ON_CHOL;
        inShabatState = InShabatState.ON_OFF;
        inCholState = InCholState.ON_RED;
        inBlinkState = InBlinkState.ON_OFF;
       
            while (true) {
                switch (outState) {
                    case ON_CHOL:                   
                        while (true) {
                            if(evToShabat.arrivedEvent()){
                                evToShabat.waitEvent();
                                outState = OutState.ON_SHABAT;
                                break;
                            }
                            switch (inCholState) {
                                case ON_RED:
                                    while (true) {
                                        if(evToShabat.arrivedEvent()){
                                            evToShabat.waitEvent();
                                            outState = OutState.ON_SHABAT;
                                            break;
                                        }
                                        if (evToGreen.arrivedEvent()) {
                                            evToGreen.waitEvent();
                                            setYellow();
                                            evTimer=new Event64();
                                    		timer=  new MyTimer72(1000,evTimer);
                                            inCholState = InCholState.ON_RED_YELLOW;
                                            break;
                                        }
                                        else
                                            yield();
                                    }

                                    break;
                                case ON_RED_YELLOW:
                                	while(true) {
        								if (evTimer.arrivedEvent()){                                     
        									evTimer.waitEvent();
        									break;
        								}
        								yield();
        							}
                                    setGreen();
                                    inCholState = InCholState.ON_GREEN;
                                    break;
                                case ON_GREEN:
                                    c = 0;
                                    while (true) {
                                        if (evToRed.arrivedEvent()) {                 
                                            evToRed.waitEvent();
                                            evTimer=new Event64();
                                    		timer=  new MyTimer72(1000,evTimer);
                                            inCholState = InCholState.ON_BLINK;
                                            break;
                                        }
                                        if(evToShabat.arrivedEvent()) {
                                            evToShabat.waitEvent();                                         
                                            outState = OutState.ON_SHABAT;
                                            break;
                                        }
                                        else
                                            yield();
                                    }
                                    break;
                                case ON_BLINK:
                                    while (inCholState != InCholState.ON_YELLOW) {
                                        switch (inBlinkState) {
                                            case ON_OFF:
                                            	while(true) {
                    								if (evTimer.arrivedEvent()){                                     
                    									evTimer.waitEvent();
                    									break;
                    								}
                    								yield();
                    							}
                                                setGreen();
                                                evTimer=new Event64();
                                        		timer=  new MyTimer72(500,evTimer);
                                                inBlinkState = InBlinkState.ON_ON;
                                                break;
                                            case ON_ON:
                                                if (c++ == 3) {
                                                	evTimer=new Event64();
                                            		timer=  new MyTimer72(500,evTimer);
                                                    inCholState = InCholState.ON_YELLOW;
                                                    setOff();
                                                    setYellow();
                                                }
                                                while(true) {
                    								if (evTimer.arrivedEvent()){                                     
                    									evTimer.waitEvent();
                    									break;
                    								}
                    								yield();
                    							}
                                                setOff();
                                                evTimer=new Event64();
                                        		timer=  new MyTimer72(500,evTimer);
                                                inBlinkState = InBlinkState.ON_OFF;
                                                break;
                                            default:
                                                break;
                                        }
                                    }
                                    break;
                                case ON_YELLOW:
                                	while(true) {
        								if (evTimer.arrivedEvent()){                                     
        									evTimer.waitEvent();
        									break;
        								}
        								yield();
        							}
                                    setRed();
                                    evAtRed.sendEvent();
                                    inCholState = InCholState.ON_RED;
                                    break;
                                default:
                                    break;
                            }
                            break;
                        }
                        break;
                        case ON_SHABAT:
                        	evTimer=new Event64();
                    		timer=  new MyTimer72(1000,evTimer);
                        while (true) {
                            if(evToChol.arrivedEvent()) {
                                evToChol.waitEvent();
                                outState = OutState.ON_CHOL;
                                inCholState = InCholState.ON_RED;

                                setRed();
                                break;
                            }

                            switch (inShabatState) {
                                case ON_OFF:
                                	while(true) {
        								if (evTimer.arrivedEvent()){                                     
        									evTimer.waitEvent();
        									break;
        								}
        								yield();
        							}
                                    setOff();
                                    evTimer=new Event64();
                            		timer=  new MyTimer72(1000,evTimer);
                                    inShabatState = InShabatState.ON_ON;
                                    break;
                                case ON_ON:
                                	while(true) {
        								if (evTimer.arrivedEvent()){                                     
        									evTimer.waitEvent();
        									break;
        								}
        								yield();
        							}
                                    setYellow();
                                    evTimer=new Event64();
                            		timer=  new MyTimer72(1000,evTimer);
                                    inShabatState = InShabatState.ON_OFF;
                                    break;
                                default:
                                    break;
                            }
                        }

                        break;
                    default:
                        break;
                }
            }
       

    }

    private void setGreen() {
        setLight(1, Color.LIGHT_GRAY);
        setLight(2, Color.LIGHT_GRAY);
        setLight(3, Color.GREEN);
    }

    private void setRed() {
        setLight(1, Color.RED);
        setLight(2, Color.LIGHT_GRAY);
        setLight(3, Color.LIGHT_GRAY);
    }

    private void setYellow() {
        setLight(2, Color.YELLOW);
    }

    private void setOff() {
        setLight(1, Color.LIGHT_GRAY);
        setLight(2, Color.LIGHT_GRAY);
        setLight(3, Color.LIGHT_GRAY);


    }

    private void setOn() {
        setLight(1, Color.LIGHT_GRAY);
        setLight(3, Color.LIGHT_GRAY);
        setLight(2, Color.YELLOW);


    }

    public void setLight(int place, Color color) {
        ramzor.colorLight[place - 1] = color;
        panel.repaint();
    }

    public boolean isStop() {
        return stop;
    }
}
